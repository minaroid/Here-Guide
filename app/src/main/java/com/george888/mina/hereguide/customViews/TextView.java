package com.george888.mina.hereguide.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by minageorge on 1/23/18.
 */

public class TextView extends android.support.v7.widget.AppCompatTextView {

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Splash.otf");
            setTypeface(tf);
        }
    }
}
