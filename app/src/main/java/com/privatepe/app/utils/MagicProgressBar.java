package com.privatepe.app.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

;import com.privatepe.app.R;


public class MagicProgressBar extends View implements Runnable {
    BitmapShader bitmapShader;
    private int borderWidth;
    private PorterDuffXfermode xfermode;
    private int DEFAULT_HEIGHT_DP;
    private float maxProgress;
    private Paint textPaint;
    private Paint bgPaint;
    private Paint pgPaint;
    private String progressText;
    private Rect textRect;
    private RectF bgRectf;
    private Bitmap flikerBitmap;
    private float flickerLeft;
    private Bitmap pgBitmap;
    private Canvas pgCanvas;
    private float progress;
    private boolean isFinish;
    private int progressColor;
    private boolean isStop;
    private int loadingColor;
    private int radius;
    private int stopColor;
    private int textSize;
    private Thread thread;

    public MagicProgressBar(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public MagicProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MagicProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
        this.DEFAULT_HEIGHT_DP = 35;
        this.maxProgress = 100.0f;
        attributeValue(attributeSet);
    }

    private void attributeValue(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.FlikerProgressBar);
        try {
            this.textSize = (int) obtainStyledAttributes.getDimension(R.styleable.FlikerProgressBar_textSize, 12.0f);
            this.loadingColor = obtainStyledAttributes.getColor(R.styleable.FlikerProgressBar_loadingColor, Color.parseColor("#40c4ff"));
            this.stopColor = obtainStyledAttributes.getColor(R.styleable.FlikerProgressBar_stopColor, Color.parseColor("#ff9800"));
            this.radius = (int) obtainStyledAttributes.getDimension(R.styleable.FlikerProgressBar_radius, 0.0f);
            this.borderWidth = (int) obtainStyledAttributes.getDimension(R.styleable.FlikerProgressBar_borderWidth, 1.0f);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private void init() {
        this.bgPaint = new Paint(5);
        this.bgPaint.setStyle(Paint.Style.STROKE);
        this.bgPaint.setStrokeWidth((float) this.borderWidth);
        this.pgPaint = new Paint(1);
        this.pgPaint.setStyle(Paint.Style.FILL);
        this.textPaint = new Paint(1);
        this.textPaint.setTextSize((float) this.textSize);
        this.textRect = new Rect();
        this.bgRectf = new RectF((float) this.borderWidth, (float) this.borderWidth, (float) (getMeasuredWidth() - this.borderWidth), (float) (getMeasuredHeight() - this.borderWidth));
        if (this.isStop) {
            this.progressColor = this.stopColor;
        } else {
            this.progressColor = this.loadingColor;
        }
        this.flikerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flicker);
        this.flickerLeft = (float) (-this.flikerBitmap.getWidth());
        initPgBimap();
    }

    private void initPgBimap() {
        this.pgBitmap = Bitmap.createBitmap(getMeasuredWidth() - this.borderWidth, getMeasuredHeight() - this.borderWidth, Bitmap.Config.ARGB_8888);
        this.pgCanvas = new Canvas(this.pgBitmap);
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = MeasureSpec.getSize(i);
        int mode = MeasureSpec.getMode(i2);
        int size2 = MeasureSpec.getSize(i2);
        if (mode == Integer.MIN_VALUE) {
            size2 = dp2px(this.DEFAULT_HEIGHT_DP);
        } else if (!(mode == 0 || mode == 1073741824)) {
            size2 = 0;
        }
        setMeasuredDimension(size, size2);
        if (this.pgBitmap == null) {
            init();
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackGround(canvas);
        drawProgress(canvas);
        drawProgressText(canvas);
        drawColorProgressText(canvas);
    }

    private void drawBackGround(Canvas canvas) {
        this.bgPaint.setColor(this.progressColor);
        canvas.drawRoundRect(this.bgRectf, (float) this.radius, (float) this.radius, this.bgPaint);
    }

    private void drawProgress(Canvas canvas) {
        this.pgPaint.setColor(this.progressColor);
        float measuredWidth = (this.progress / this.maxProgress) * ((float) getMeasuredWidth());
        this.pgCanvas.save();
        this.pgCanvas.clipRect(0.0f, 0.0f, measuredWidth, (float) getMeasuredHeight());
        this.pgCanvas.drawColor(this.progressColor);
        this.pgCanvas.restore();
        if (!this.isStop) {
            this.pgPaint.setXfermode(this.xfermode);
            this.pgCanvas.drawBitmap(this.flikerBitmap, this.flickerLeft, 0.0f, this.pgPaint);
            this.pgPaint.setXfermode((Xfermode) null);
        }
        this.bitmapShader = new BitmapShader(this.pgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        this.pgPaint.setShader(this.bitmapShader);
        canvas.drawRoundRect(this.bgRectf, (float) this.radius, (float) this.radius, this.pgPaint);
    }

    private void drawProgressText(Canvas canvas) {
        this.textPaint.setColor(this.progressColor);
        this.progressText = getProgressText();
        this.textPaint.getTextBounds(this.progressText, 0, this.progressText.length(), this.textRect);
        int width = this.textRect.width();
        int height = this.textRect.height();
        canvas.drawText(this.progressText, (float) ((getMeasuredWidth() - width) / 2), (float) ((getMeasuredHeight() + height) / 2), this.textPaint);
    }


    private void drawColorProgressText(Canvas canvas) {
        this.textPaint.setColor(-1);
        int width = this.textRect.width();
        int height = this.textRect.height();
        float measuredWidth = (float) ((getMeasuredWidth() - width) / 2);
        float measuredHeight = (float) ((getMeasuredHeight() + height) / 2);
        float progressWidth = (this.progress / this.maxProgress) * ((float) getMeasuredWidth());
        if (progressWidth > measuredWidth) {
            canvas.save();
            canvas.clipRect(measuredWidth, 0.0f, Math.min(progressWidth, (((float) width) * 1.1f) + measuredWidth), (float) getMeasuredHeight());
            canvas.drawText(this.progressText, measuredWidth, measuredHeight, this.textPaint);
            canvas.restore();
        }
    }

    public void setProgress(float f) {
        if (!this.isStop) {
            if (f < this.maxProgress) {
                this.progress = f;
            } else {
                this.progress = this.maxProgress;
                finishLoad();
            }
            invalidate();
        }
    }

    public void setStop(boolean z) {
        this.isStop = z;
        if (this.isStop) {
            this.progressColor = this.stopColor;
            this.thread.interrupt();
        } else {
            this.progressColor = this.loadingColor;
            this.thread = new Thread(this);
            this.thread.start();
        }
        invalidate();
    }

    public void finishLoad() {
        this.isFinish = true;
        setStop(true);
    }

    public void toggle() {
        if (this.isFinish) {
            return;
        }
        if (this.isStop) {
            setStop(false);
        } else {
            setStop(true);
        }
    }

    public void run() {
        int width = this.flikerBitmap.getWidth();
        while (!this.isStop && !this.thread.isInterrupted()) {
            try {
                this.flickerLeft += (float) dp2px(5);
                if (this.flickerLeft >= (this.progress / this.maxProgress) * ((float) getMeasuredWidth())) {
                    this.flickerLeft = (float) (-width);
                }
                postInvalidate();
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void reset() {
        setStop(true);
        this.progress = 0.0f;
        this.isFinish = false;
        this.isStop = false;
        this.progressColor = this.loadingColor;
        this.progressText = "";
        this.flickerLeft = (float) (-this.flikerBitmap.getWidth());
        initPgBimap();
    }

    public float getProgress() {
        return this.progress;
    }

    public boolean isStop() {
        return this.isStop;
    }

    public boolean isFinish() {
        return this.isFinish;
    }

    private String getProgressText() {
        if (this.isFinish) {
            return "Downloaded";
        }
        if (this.isStop) {
            return "Go On";
        }
        return "Downloading " + this.progress + "%";
    }

    private int dp2px(int i) {
        return (int) (((float) i) * getContext().getResources().getDisplayMetrics().density);
    }
}
