package ink.kei.audiofocusswitcher.hook;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XSharedPreferences;
public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "AudioFocusHook";
    private static XSharedPreferences prefs;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!lpparam.packageName.equals("android")) return;

        // 初始化 XSharedPreferences
        if (prefs == null) {
            prefs = new XSharedPreferences("ink.kei.audiofocusswitcher", "audio_focus_config");
            prefs.makeWorldReadable();
        }

        try {
            Class<?> mediaFocusClass = XposedHelpers.findClass(
                    "com.android.server.audio.MediaFocusControl",
                    lpparam.classLoader
            );

            XposedBridge.hookAllMethods(mediaFocusClass, "requestAudioFocus",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            try {
                                prefs.reload();

                                boolean moduleEnabled = prefs.getBoolean("module_enabled", false);
                                boolean blockEnabled = prefs.getBoolean("ignore_enabled", false);
                                boolean allowVoice = prefs.getBoolean("allow_voice", true);

                                Log.d(TAG, String.format("Config: enabled=%s, block=%s, voice=%s",
                                        moduleEnabled, blockEnabled, allowVoice));

                                if (!moduleEnabled) return;

                                AudioAttributes audioAttrs = (AudioAttributes) param.args[0];
                                if (audioAttrs == null) return;

                                int usage = audioAttrs.getUsage();

                                if (blockEnabled) {
                                    if (usage == AudioAttributes.USAGE_VOICE_COMMUNICATION) {
                                        if (!allowVoice) {
                                            param.setResult(AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
                                        }
                                    } else {
                                        param.setResult(AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error in hook", e);
                            }
                        }
                    });

            Log.d(TAG, "Hook loaded successfully");

        } catch (Exception e) {
            Log.e(TAG, "Hook failed", e);
        }
    }
}