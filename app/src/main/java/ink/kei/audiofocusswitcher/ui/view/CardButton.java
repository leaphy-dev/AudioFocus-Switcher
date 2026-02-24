package ink.kei.audiofocusswitcher.ui.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class CardButton extends LinearLayout {

    private final Context context;
    public String title = "";
    private String description = "";

    private SwitchMaterial switchView;

    public CardButton(Context context) {
        super(context);
        this.context = context;
    }

    public CardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CardButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public CardButton(Context context,
                      String title,
                      String description) {
        super(context);
        this.context = context;
        this.title = title != null ? title : "";
        this.description = description != null ? description : "";
        init();
    }

    private void init() {
        setupLayout();
        addTitleRow();
        addDivider();
        addDescription();
    }

    private void setupLayout() {
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(25, 25, 25, 25);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(25);
        drawable.setColor(Color.WHITE);
        drawable.setStroke(2, 0xFFE0E0E0);

        this.setBackground(drawable);
        this.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void addTitleRow() {
        LinearLayout titleRow = new LinearLayout(context);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        titleRow.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(16);
        titleView.setTextColor(0xFF333333);
        titleView.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        this.switchView = new SwitchMaterial(context);
//        this.switchView.setChecked(prefs != null ?
//                prefs.getBoolean(prefKey, defaultValue) : defaultValue);

//        this.switchView.setOnCheckedChangeListener();

        titleRow.addView(titleView);
        titleRow.addView(this.switchView);
        this.addView(titleRow);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener){
        this.switchView.setOnCheckedChangeListener(listener);
    }

    public void setChecked(boolean checked) {
        this.switchView.setChecked(checked);
    }

    private void addDivider() {
        View divider = new View(context);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(0xFFE0E0E0);
        divider.setPadding(0, 15, 0, 15);
        this.addView(divider);
    }

    private void addDescription() {
        TextView descView = new TextView(context);
        descView.setText(description);
        descView.setTextSize(13);
        descView.setTextColor(0xFF757575);
        descView.setPadding(0, 10, 0, 0);
        this.addView(descView);
    }

    public void setEnabled(boolean activated){
        this.switchView.setEnabled(activated);
    }
}