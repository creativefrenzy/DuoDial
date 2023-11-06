package com.klive.app.response.metend.newgift.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class FontIconView extends AppCompatTextView {

    public  Typeface f7780a = Typeface.createFromAsset(getContext().getAssets(), "fonts/iconfont.ttf");

    public FontIconView(Context context) {
        super(context, null, 0);
        setTypeface(f7780a);
    }

    public FontIconView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        setTypeface(f7780a);
    }

    public FontIconView(Context context, @Nullable AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        setTypeface(f7780a);
    }
}
