package ink.kei.audiofocusswitcher.ui.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoCard extends LinearLayout {

    private final Context context;
    private String message = "";

    public InfoCard(Context context){
        super(context);
        this.context = context;
    }

    public InfoCard(Context context, AttributeSet args){
        super(context,args);
        this.context = context;

    }

    public InfoCard(Context context, AttributeSet args, int defStyleAttr){
        super(context,args, defStyleAttr);
        this.context = context;
    }

    public InfoCard(Context context, String message){
        super(context);
        this.context = context;
        this.message = message;
        this.layout();
    }

    private void layout(){
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(25, 25, 25, 25);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(20);
        drawable.setColor(0xFFF5F5F5);
        drawable.setStroke(1, 0xFFD0D0D0);

        this.setBackground(drawable);

        TextView infoView = new TextView(context);
        infoView.setText(message);
        infoView.setTextSize(13);
        infoView.setTextColor(0xFF555555);
        infoView.setLineSpacing(5, 1);

        this.addView(infoView);
    }
}
