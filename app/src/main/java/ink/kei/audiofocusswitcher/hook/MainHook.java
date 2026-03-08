package ink.kei.audiofocusswitcher.hook;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
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
            XposedBridge.hookAllMethods(mediaFocusClass, "requestAudioFocus",new RequestAudioFocusHook(prefs));

            Log.i(TAG, "✓ Hook loaded successfully in package: " + lpparam.packageName);

        } catch (Exception e) {
            Log.e(TAG, "✗ Hook failed to load", e);
        }
    }


}