package com.privatepe.app.nGiftSec.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.zeeplive.app.R;

public class PhotoView extends AppCompatImageView {

    /* renamed from: a  reason: collision with root package name */
    public int f8260a;

    /* renamed from: b  reason: collision with root package name */
    public int f8261b;

    /* renamed from: c  reason: collision with root package name */
    public int f8262c;

    public PhotoView(Context context) {
        this(context, null, 0);
    }

    public void setImage(String str) {
        //k.a(str, this, new e().c(this.f8260a).a(this.f8261b).b(this.f8262c));
    }

    public PhotoView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PhotoView(Context context, @Nullable AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.PhotoView);
       /* this.f8260a = obtainStyledAttributes.getResourceId(R.styleable.PhotoView_placeholder, g.D.b.e.placeholder);
        this.f8261b = obtainStyledAttributes.getResourceId(R.styleable.PhotoView_error, g.D.b.e.error);
        this.f8262c = obtainStyledAttributes.getResourceId(R.styleable.PhotoView_fallback, g.D.b.e.fallback);*/
        obtainStyledAttributes.recycle();
    }
}
