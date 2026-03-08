package ink.kei.audiofocusswitcher.hook;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;

class RequestAudioFocusHook extends XC_MethodHook {

    private static final String TAG = "AudioFocusHook";
    private final XSharedPreferences prefs;

    public RequestAudioFocusHook(XSharedPreferences prefs){
        this.prefs = prefs;

    }
    @Override
    protected void beforeHookedMethod(MethodHookParam param) {
        try {
            prefs.reload();

            boolean moduleEnabled = prefs.getBoolean("module_enabled", false);
            boolean blockEnabled = prefs.getBoolean("ignore_enabled", false);
            boolean allowVoice = prefs.getBoolean("allow_voice", true);

            if (!moduleEnabled) {
                Log.d(TAG, "Module disabled, skipping hook");
                return;
            }

            AudioAttributes audioAttrs = (AudioAttributes) param.args[0];
            if (audioAttrs == null) {
                Log.w(TAG, "AudioAttributes is null, skipping");
                return;
            }

            int usage = audioAttrs.getUsage();
            String usageName = getUsageName(usage);

            Log.i(TAG, String.format("Usage: %s(%d) | Block enabled: %s", usageName, usage, blockEnabled));

            if (blockEnabled) {
                if (usage == AudioAttributes.USAGE_VOICE_COMMUNICATION) {
                    if (!allowVoice) {
                        param.setResult(AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
                        Log.i(TAG, "ACTION: Blocked voice communication from [%s] - Granting fake focus");
                    } else {
                        Log.d(TAG, "Voice communication from [%s] allowed, passing through");
                    }
                } else {
                    param.setResult(AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
                    Log.i(TAG, String.format("ACTION: Blocked %s(%d) - Granting fake focus",
                            usageName, usage));
                }
            } else {
                Log.d(TAG, String.format("Block disabled, passing %s request ",
                        usageName));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in hook", e);
        }
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) {
        Object result = param.getResult();
        if (result != null) {
            Log.d(TAG, "Hook result: " + result.toString());
        }
    }

    private String getUsageName(int usage) {
        switch (usage) {
            case AudioAttributes.USAGE_UNKNOWN:
                return "UNKNOWN";
            case AudioAttributes.USAGE_MEDIA:
                return "MEDIA";
            case AudioAttributes.USAGE_VOICE_COMMUNICATION:
                return "VOICE_COMMUNICATION";
            case AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING:
                return "VOICE_COMM_SIGNALLING";
            case AudioAttributes.USAGE_ALARM:
                return "ALARM";
            case AudioAttributes.USAGE_NOTIFICATION:
                return "NOTIFICATION";
            case AudioAttributes.USAGE_NOTIFICATION_RINGTONE:
                return "RINGTONE";
            case AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST:
                return "NOTIFICATION_COMM_REQUEST";
            case AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT:
                return "NOTIFICATION_COMM_INSTANT";
            case AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED:
                return "NOTIFICATION_COMM_DELAYED";
            case AudioAttributes.USAGE_NOTIFICATION_EVENT:
                return "NOTIFICATION_EVENT";
            case AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY:
                return "ASSISTANCE_ACCESSIBILITY";
            case AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE:
                return "NAVIGATION_GUIDANCE";
            case AudioAttributes.USAGE_ASSISTANCE_SONIFICATION:
                return "ASSISTANCE_SONIFICATION";
            case AudioAttributes.USAGE_GAME:
                return "GAME";
            case AudioAttributes.USAGE_ASSISTANT:
                return "ASSISTANT";
            default:
                return "USAGE_" + usage;
        }
    }
}