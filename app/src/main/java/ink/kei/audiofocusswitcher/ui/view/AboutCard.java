package ink.kei.audiofocusswitcher.ui.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutCard extends LinearLayout {

    private Context context;

    public AboutCard(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setBackgroundResource(android.R.drawable.editbox_background);
        int paddingDp = dpToPx(16);
        setPadding(paddingDp, paddingDp, paddingDp, paddingDp);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        TextView titleView = createTitleView();
        addView(titleView);

        addView(createDivider());

        addView(createInfoRow("Author", "leaphy-dev(github)"));

        addView(createInfoRow("Version", getAppVersion()));

        addView(createLinkRow());

        setOnClickListener(v -> openGitHub());
    }

    private TextView createTitleView() {
        TextView titleView = new TextView(context);
        titleView.setText("About AudioFocus Switcher");
        titleView.setTextSize(18);
        titleView.setTextColor(0xFF2196F3);
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(0, 0, 0, dpToPx(12));
        return titleView;
    }

    private View createDivider() {
        View divider = new View(context);
        divider.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(1)));
        divider.setBackgroundColor(0x1A000000);
        return divider;
    }

    private LinearLayout createInfoRow(String label, String value) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(HORIZONTAL);
        row.setPadding(0, dpToPx(8), 0, dpToPx(8));

        TextView labelView = new TextView(context);
        labelView.setText(label + ":");
        labelView.setTextSize(14);
        labelView.setTextColor(0xDE000000);
        labelView.setLayoutParams(new LayoutParams(
                dpToPx(80), LayoutParams.WRAP_CONTENT));

        TextView valueView = new TextView(context);
        valueView.setText(value);
        valueView.setTextSize(14);
        valueView.setTextColor(0x8A000000);

        row.addView(labelView);
        row.addView(valueView);
        return row;
    }

    private LinearLayout createLinkRow() {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(HORIZONTAL);
        row.setPadding(0, dpToPx(8), 0, dpToPx(8));
        row.setBackgroundColor(0x0D2196F3); // 浅色背景
        row.setGravity(Gravity.CENTER_VERTICAL);

        // 标签
        TextView labelView = new TextView(context);
        labelView.setText("GitHub:");
        labelView.setTextSize(14);
        labelView.setTextColor(0xDE000000);
        labelView.setLayoutParams(new LayoutParams(
                dpToPx(80), LayoutParams.WRAP_CONTENT));

        // 链接
        TextView linkView = new TextView(context);
        linkView.setText("leaphy-dev/AudioFocus-Switcher");
        linkView.setTextSize(14);
        linkView.setTextColor(0xFF4CAF50);

        // 箭头
        TextView arrowView = new TextView(context);
        arrowView.setText("↗");
        arrowView.setTextSize(16);
        arrowView.setTextColor(0xFF4CAF50);
        arrowView.setPadding(dpToPx(8), 0, 0, 0);

        row.addView(labelView);
        row.addView(linkView);
        row.addView(arrowView);

        return row;
    }

    private void openGitHub() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/leaphy-dev/AudioFocus-Switcher"));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("AboutCard", "Failed to open GitHub", e);
        }
    }

    private String getAppVersion() {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
            return "-1";
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}