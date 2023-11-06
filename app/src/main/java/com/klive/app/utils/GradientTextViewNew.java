package com.klive.app.utils;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.klive.app.R;

public class GradientTextViewNew extends AppCompatTextView {


    private int colorStart = ContextCompat.getColor(getContext(), R.color.gradientLightYellow);
    private int colorEnd = ContextCompat.getColor(getContext(), R.color.colorAccent);


    public GradientTextViewNew(Context context) {
        super(context);
    }

    public GradientTextViewNew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextViewNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //Setting the gradient if layout is changed
        if (changed) {
            getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(), colorStart, colorEnd,
                    Shader.TileMode.CLAMP));
        }
    }

    public void setGradientColors(int colorStart, int colorEnd) {
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        // this forces view redrawing
        invalidate();

    }
}