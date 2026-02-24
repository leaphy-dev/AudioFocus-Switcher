package ink.kei.audiofocusswitcher.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ink.kei.audiofocusswitcher.ui.view.CardButton;
import ink.kei.audiofocusswitcher.ui.view.InfoCard;
import ink.kei.audiofocusswitcher.ui.view.AboutCard;

public class MainActivity extends Activity {

    private SharedPreferences prefs;

    private CardButton enableModelButton;
    private CardButton enableIgnore;
    private CardButton allowVoiceCall;

    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // 使用 MODE_WORLD_READABLE，LSPosed 会自动处理权限
            prefs = getSharedPreferences("audio_focus_config", Context.MODE_WORLD_READABLE);
            Log.d("MainActivity", "Prefs initialized with MODE_WORLD_READABLE");
        } catch (SecurityException e) {
            Log.e("MainActivity", "Failed to use MODE_WORLD_READABLE", e);
            prefs = getSharedPreferences("audio_focus_config", Context.MODE_PRIVATE);
        }

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(30, 30, 30, 30);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // title
        TextView title = new TextView(this);
        title.setText("AUDIO FOCUS SWITCHER");
        title.setTextSize(22);
        title.setGravity(Gravity.START);
        title.setPadding(0, 30, 0, 30);
        title.setTextColor(0xFF2196F3);
        mainLayout.addView(title);

        // LSPosed 状态卡片woc判断怎么写啊
//        LSposedStateCard lsposedStateCard = new LSposedStateCard(this);
//        mainLayout.addView(lsposedStateCard);
//        mainLayout.addView(createSpacer(10));

        //
         enableModelButton = new CardButton(this, "Enable Module",
                "Master switch to enable/disable the module");
         enableIgnore = new CardButton(this, "Block Audio Focus",
                "Make all apps think they always get focus (multiple apps can play simultaneously)");
         allowVoiceCall = new CardButton(this, "Block All Audio Focus",
                "Voice calls can still gain focus when needed");
        //
        enableModelButton.setChecked(prefs != null && prefs.getBoolean("module_enabled", false));
        enableModelButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (prefs != null) {
                prefs.edit().putBoolean("module_enabled", isChecked).apply();
                updateUiState();
            }
        });
        //
        enableIgnore.setEnabled(prefs != null && prefs.getBoolean("module_enabled", false));
        enableIgnore.setChecked(prefs != null && prefs.getBoolean("ignore_enabled", false));
        enableIgnore.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (prefs != null) {
                prefs.edit().putBoolean("ignore_enabled", isChecked).apply();
                updateUiState();
            }
        });
        //
        allowVoiceCall.setEnabled(prefs != null && prefs.getBoolean("ignore_enabled", false));
        allowVoiceCall.setChecked(prefs != null && prefs.getBoolean("allow_voice", true));
        allowVoiceCall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (prefs != null) {
                prefs.edit().putBoolean("allow_voice", isChecked).apply();
                updateUiState();
            }
        });

        mainLayout.addView(enableModelButton);
        mainLayout.addView(createSpacer(15));
        mainLayout.addView(createTextDivider("Audio Focus Block Setting"));
        mainLayout.addView(enableIgnore);
        mainLayout.addView(createSpacer(15));
        mainLayout.addView(allowVoiceCall);
        mainLayout.addView(createSpacer(15));
        mainLayout.addView(createTextDivider("About"));

        //
        mainLayout.addView(new InfoCard(this,
                "How it works:\n" +
                        "• When enabled, multiple apps can play audio simultaneously\n" +
                        "• Voice calls will still get focus if allowed\n"));
        mainLayout.addView(createSpacer(15));
        mainLayout.addView(new AboutCard(this));

        LinearLayout spacer = new LinearLayout(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mainLayout.addView(spacer);

        setContentView(mainLayout);
    }

    private void updateUiState() {
        if (prefs == null) return;

        boolean moduleEnabled = prefs.getBoolean("module_enabled", false);
        boolean ignoreEnabled = prefs.getBoolean("ignore_enabled", false);
        boolean allowVoice = prefs.getBoolean("allow_voice", true);

        enableIgnore.setEnabled(moduleEnabled);
        allowVoiceCall.setEnabled(moduleEnabled && ignoreEnabled);

    }

    private View createSpacer(int height) {
        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height));
        return spacer;
    }



    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private View createTextDivider(String text) {
        LinearLayout dividerLayout = new LinearLayout(this);
        dividerLayout.setOrientation(LinearLayout.HORIZONTAL);
        dividerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        View leftLine = new View(this);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                0, dpToPx(1), 1);
        leftLine.setLayoutParams(lineParams);
        leftLine.setBackgroundColor(0x1A000000);

        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(12);
        textView.setTextColor(0x66000000);
        textView.setPadding(dpToPx(16), 0, dpToPx(16), 0);

        View rightLine = new View(this);
        rightLine.setLayoutParams(lineParams);
        rightLine.setBackgroundColor(0x1A000000);

        dividerLayout.addView(leftLine);
        dividerLayout.addView(textView);
        dividerLayout.addView(rightLine);

        return dividerLayout;
    }

}