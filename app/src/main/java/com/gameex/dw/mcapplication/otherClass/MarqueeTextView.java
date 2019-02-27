package com.gameex.dw.mcapplication.otherClass;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context,AttributeSet attrs) {
        super(context, attrs);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MarqueeTextView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return super.isFocused();
    }
}
