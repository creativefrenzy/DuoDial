package com.privatepe.app.nGiftSec.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.zeeplive.app.R;

public class CircleImagesView extends PhotoView {

    public int f8247d;

    public int f8248e;

    public int f8249f;

    public Paint f8250g;

    public Matrix f8251h;

    public BitmapShader f8252i;

    public RectF f8253j;

    public int f8254k;

    public int f8255l;

    public float f8256m;

    public boolean f8257n;

    public final RectF f8258o;

    public final Paint f8259p;

    public CircleImagesView(Context context) {
        this(context, null, 0);
    }

    public final RectF a() {
        return new RectF(0.0f, 0.0f, getWidth(), getHeight());
    }

    public int getBorderWidth() {
        return this.f8255l;
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        Bitmap bitmap;
        if (getDrawable() != null) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                if (drawable instanceof BitmapDrawable) {
                    bitmap = ((BitmapDrawable) drawable).getBitmap();
                } else {
                    int intrinsicWidth = drawable.getIntrinsicWidth();
                    int intrinsicHeight = drawable.getIntrinsicHeight();
                    Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
                    Canvas canvas2 = new Canvas(createBitmap);
                    drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
                    drawable.draw(canvas2);
                    bitmap = createBitmap;
                }
                Shader.TileMode tileMode = Shader.TileMode.REPEAT;
                this.f8252i = new BitmapShader(bitmap, tileMode, tileMode);
                int i2 = this.f8247d;
                float f2 = 1.0f;
                if (i2 == 1) {
                    f2 = ((this.f8257n ? this.f8248e : this.f8248e - (this.f8255l * 2)) * 1.0f) / Math.min(bitmap.getWidth(), bitmap.getHeight());
                } else if (i2 == 2 && !(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())) {
                    f2 = Math.max(((this.f8257n ? getWidth() : getWidth() - (this.f8255l * 2)) * 1.0f) / bitmap.getWidth(), ((this.f8257n ? getHeight() : getHeight() - (this.f8255l * 2)) * 1.0f) / bitmap.getHeight());
                }
                this.f8251h.setScale(f2, f2);
                this.f8252i.setLocalMatrix(this.f8251h);
                this.f8250g.setShader(this.f8252i);
            }
            if (this.f8247d == 2) {
                if (this.f8253j == null) {
                    this.f8253j = new RectF(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    int i3 = this.f8255l;
                    if (i3 > 0 && !this.f8257n) {
                        this.f8253j.inset(i3, i3);
                        RectF rectF = this.f8253j;
                        int i4 = this.f8255l;
                        rectF.offset(-i4, -i4);
                    }
                }
                if (this.f8255l <= 0) {
                    RectF rectF2 = this.f8253j;
                    int i5 = this.f8249f;
                    canvas.drawRoundRect(rectF2, i5, i5, this.f8250g);
                } else if (this.f8257n) {
                    RectF rectF3 = this.f8253j;
                    int i6 = this.f8249f;
                    canvas.drawRoundRect(rectF3, i6, i6, this.f8250g);
                    float f3 = this.f8249f - (this.f8255l / 2);
                    canvas.drawRoundRect(this.f8258o, f3, f3, this.f8259p);
                } else {
                    canvas.save();
                    int i7 = this.f8255l;
                    canvas.translate(i7, i7);
                    float f4 = this.f8249f - this.f8255l;
                    canvas.drawRoundRect(this.f8253j, f4, f4, this.f8250g);
                    canvas.restore();
                    float f5 = this.f8249f - (this.f8255l / 2);
                    canvas.drawRoundRect(this.f8258o, f5, f5, this.f8259p);
                }
            } else {
                canvas.save();
                float f6 = this.f8255l;
                int i8 = this.f8248e;
                float f7 = i8 / 2.0f;
                float f8 = i8 / 2.0f;
                float f9 = this.f8249f;
                if (!this.f8257n && f6 > 0.0f) {
                    canvas.translate(f6, f6);
                    f7 -= f6;
                    f8 -= f6;
                    f9 -= f6;
                }
                canvas.drawCircle(f7, f8, f9 - getPaddingLeft(), this.f8250g);
                canvas.restore();
                if (f6 > 0.0f) {
                    canvas.drawCircle(this.f8258o.centerX(), this.f8258o.centerY(), this.f8256m, this.f8259p);
                }
            }
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void onMeasure(int i2, int i3) {
        super.onMeasure(i2, i3);
        if (this.f8247d == 1) {
            this.f8248e = Math.min(getMeasuredWidth(), getMeasuredHeight());
            int i4 = this.f8248e;
            if (i4 != 0) {
                this.f8249f = i4 / 2;
                setMeasuredDimension(i4, i4);
            }
        }
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            super.onRestoreInstanceState(bundle.getParcelable("state_instance"));
            this.f8247d = bundle.getInt("state_type");
            this.f8249f = bundle.getInt("state_border_radius");
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("state_instance", super.onSaveInstanceState());
        bundle.putInt("state_type", this.f8247d);
        bundle.putInt("state_border_radius", this.f8249f);
        return bundle;
    }

    @Override // android.view.View
    public void onSizeChanged(int i2, int i3, int i4, int i5) {
        super.onSizeChanged(i2, i3, i4, i5);
        if (this.f8247d == 2) {
            this.f8253j = new RectF(0.0f, 0.0f, i2, i3);
            int i6 = this.f8255l;
            if (i6 > 0 && !this.f8257n) {
                this.f8253j.inset(i6, i6);
                RectF rectF = this.f8253j;
                int i7 = this.f8255l;
                rectF.offset(-i7, -i7);
            }
            this.f8258o.set(a());
            RectF rectF2 = this.f8258o;
            int i8 = this.f8255l;
            rectF2.inset(i8 / 2.0f, i8 / 2.0f);
            return;
        }
        this.f8258o.set(a());
        this.f8256m = Math.min((this.f8258o.height() - this.f8255l) / 2.0f, (this.f8258o.width() - this.f8255l) / 2.0f);
    }

    public void setBorderColor(int i2) {
        a(this.f8255l, i2);
    }

    public void setBorderWidth(int i2) {
        a(i2, this.f8254k);
    }

    public void setRadius(int i2) {
        int a2;
        if (this.f8247d != 1 && this.f8249f != (a2 = a(i2))) {
            this.f8249f = a2;
            invalidate();
        }
    }

    public void setType(int i2) {
        if (this.f8247d != i2) {
            this.f8247d = i2;
            int i3 = this.f8247d;
            if (!(i3 == 2 || i3 == 1)) {
                this.f8247d = 1;
            }
            requestLayout();
        }
    }

    public CircleImagesView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public void a(int i2, int i3) {
        if (this.f8255l != i2 || this.f8254k != i3) {
            if (this.f8255l != i2) {
                this.f8255l = i2;
                float f2 = i2;
                this.f8259p.setStrokeWidth(f2);
                this.f8256m = Math.min((this.f8258o.height() - f2) / 2.0f, (this.f8258o.width() - f2) / 2.0f);
                this.f8253j = new RectF(0.0f, 0.0f, getWidth(), getHeight());
                int i4 = this.f8255l;
                if (i4 > 0 && !this.f8257n) {
                    this.f8253j.inset(i4, i4);
                    RectF rectF = this.f8253j;
                    int i5 = this.f8255l;
                    rectF.offset(-i5, -i5);
                }
                this.f8258o.set(a());
                RectF rectF2 = this.f8258o;
                int i6 = this.f8255l;
                rectF2.inset(i6 / 2.0f, i6 / 2.0f);
            }
            if (this.f8254k != i3) {
                this.f8254k = i3;
                this.f8259p.setColor(i3);
            }
            invalidate();
        }
    }

    public CircleImagesView(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.f8254k = -16777216;
        this.f8255l = 0;
        this.f8258o = new RectF();
        this.f8259p = new Paint();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet,  R.styleable.CircleImageView);
        this.f8249f = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircleImageView_radius, a(10));
        this.f8247d = obtainStyledAttributes.getInt(R.styleable.CircleImageView_type, 1);
        this.f8255l = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircleImageView_border_width, 0);
        this.f8254k = obtainStyledAttributes.getColor(R.styleable.CircleImageView_border_color, -16777216);
        this.f8257n = obtainStyledAttributes.getBoolean(R.styleable.CircleImageView_border_overlay, false);
        obtainStyledAttributes.recycle();
        this.f8251h = new Matrix();
        this.f8250g = new Paint();
        this.f8250g.setAntiAlias(true);
        this.f8259p.setStyle(Paint.Style.STROKE);
        this.f8259p.setAntiAlias(true);
        this.f8259p.setColor(this.f8254k);
        this.f8259p.setStrokeWidth(this.f8255l);
    }

    public int a(int i2) {
        return (int) TypedValue.applyDimension(1, i2, getResources().getDisplayMetrics());
    }
}
