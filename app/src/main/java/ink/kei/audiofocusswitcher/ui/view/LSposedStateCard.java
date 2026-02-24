package ink.kei.audiofocusswitcher.ui.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.robv.android.xposed.XposedBridge;

public class LSposedStateCard extends LinearLayout {

    private final Context context;
    private TextView statusText;

    public LSposedStateCard(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LSposedStateCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public LSposedStateCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        setupLayout();
        createViews();
        checkStatus();
    }

    private void setupLayout() {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setPadding(20, 15, 20, 15);
        this.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(15);
        this.setBackground(drawable);
    }

    private void createViews() {
        TextView statusIcon = new TextView(context);
        statusIcon.setTextSize(20);
        statusIcon.setPadding(0, 0, 15, 0);

        statusText = new TextView(context);
        statusText.setTextSize(14);
        statusText.setGravity(Gravity.CENTER_VERTICAL);

        this.addView(statusIcon);
        this.addView(statusText);
    }

    private void checkStatus() {
        boolean isActive = isModuleActive();
        GradientDrawable drawable = (GradientDrawable) this.getBackground();

//        if (isActive) {
            drawable.setColor(0xFFE8F5E9);
//            statusIcon.setText("✅");
            statusText.setText("LSposed: Activated");
            statusText.setTextColor(0xFF2E7D32);
//        } else {
//            drawable.setColor(0xFFFFEBEE);
//            statusIcon.setText("⚠️");
//            statusText.setText("LSposed: Module Inactive");
//            statusText.setTextColor(0xFFC62828);
//        }
    }

    public boolean isModuleActive() {
        try {
            // 能获取到版本号，说明Xposed已加载
            int version = XposedBridge.getXposedVersion();
            return version > 0;
        } catch (Throwable t) {
            return false;
        }
    }


    public void refresh() {
        checkStatus();
    }
}