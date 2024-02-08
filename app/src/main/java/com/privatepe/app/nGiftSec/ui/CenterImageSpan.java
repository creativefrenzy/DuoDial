package com.privatepe.app.nGiftSec.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import java.lang.ref.WeakReference;

public class CenterImageSpan extends ImageSpan {
    public WeakReference<Drawable> mDrawableRef;

    public CenterImageSpan(Drawable drawable, int i) {
        super(drawable, i);
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> weakReference = this.mDrawableRef;
        Drawable drawable = weakReference != null ? weakReference.get() : null;
        if (drawable != null) {
            return drawable;
        }
        Drawable drawable2 = getDrawable();
        this.mDrawableRef = new WeakReference<>(drawable2);
        return drawable2;
    }

    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        Drawable cachedDrawable = getCachedDrawable();
        canvas.save();
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int i6 = fontMetricsInt.descent;
        canvas.translate(f, (float) (((i4 + i6) - ((i6 - fontMetricsInt.ascent) / 2)) - ((cachedDrawable.getBounds().bottom - cachedDrawable.getBounds().top) / 2)));
        cachedDrawable.draw(canvas);
        canvas.restore();
    }

    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        Rect bounds = getDrawable().getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fontMetricsInt2 = paint.getFontMetricsInt();
            int i3 = fontMetricsInt2.descent;
            int i4 = fontMetricsInt2.ascent;
            int i5 = ((i3 - i4) / 2) + i4;
            int i6 = (bounds.bottom - bounds.top) / 2;
            fontMetricsInt.ascent = i5 - i6;
            fontMetricsInt.top = fontMetricsInt.ascent;
            fontMetricsInt.bottom = i5 + i6;
            fontMetricsInt.descent = fontMetricsInt.bottom;
        }
        return bounds.right;
    }
}
