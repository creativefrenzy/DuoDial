package com.privatepe.app.nGiftSec.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.rxjava.rxlife.CompletableLife;
import com.rxjava.rxlife.RxLife;
import com.zeeplive.app.R;
import com.zeeplive.app.activity.party.utils.CommonMethods;
import com.zeeplive.app.nGiftSec.model.GlobalWinGiftEntity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

/* compiled from: GlobalWinLayout.kt */
public final class GlobalWinLayout extends HorizontalScrollView {
    public Animator animation;
    public boolean canClick;
    public Object data;
    public final String diamond;
    public volatile boolean isAniming;
    public ImageView ivGo;
    public long mDuration;
    public AppCompatImageView mGiftPic;
    public ImageView mIcon;
    public final float mScreenWidth;
    public TextView mTv1;
    public TextView mTv2;
    public TextView mTvLucky;
    public LinearLayout mllGift, ll_root;
    CommonMethods commonMethods = new CommonMethods();

    public GlobalWinLayout(Context context) {
        this(context, null, 0);
    }

    /* access modifiers changed from: private */
    public final void bindData(GlobalWinGiftEntity globalWinGiftEntity) {
        try {
            if (globalWinGiftEntity.giftType.equals("game")) {
                try {
                    ll_root.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_global_win));
                    mGiftPic.setVisibility(GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String string = getResources().getString(R.string.label_notify_game_1, substringUserName(globalWinGiftEntity.getFromNickName(), 16), Long.valueOf(globalWinGiftEntity.getWinEnergy()));
                SpannableString spannableString = new SpannableString(string);
                spannableString.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                String substringUserName = substringUserName(globalWinGiftEntity.getFromNickName(), 16);
                replaceNickNameGame(spannableString, substringUserName, string);
                TextView textView = this.mTv1;
                if (textView != null) {
                    textView.setText(spannableString);
                }
                String string2 = getResources().getString(R.string.label_notify_game_2, globalWinGiftEntity.getGiftName());
                SpannableString spannableString2 = new SpannableString(string2);
                replaceRes(spannableString2, string2, this.diamond, R.drawable.ic_diamond);
                TextView textView2 = this.mTv2;
                if (textView2 != null) {
                    textView2.setText(spannableString2);
                }

                ImageView imageView = this.mIcon;
                if (imageView != null) {
                    imageView.setImageResource(R.drawable.lucky_icon_gift_small);
                }

                commonMethods.imageLoaderView(getContext(), this.mIcon, globalWinGiftEntity.getGiftUrl());
            } else {
                try {
                    ll_root.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_global_win));
                    mIcon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.lucky_icon_gift_small));
                    mGiftPic.setVisibility(VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String string = getResources().getString(R.string.label_notify_gift_1, substringUserName(globalWinGiftEntity.getFromNickName(), 16), Integer.valueOf(globalWinGiftEntity.getGiftCount()));
                SpannableString spannableString = new SpannableString(string);
                spannableString.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                String substringUserName = substringUserName(globalWinGiftEntity.getFromNickName(), 16);
                replaceNickName(spannableString, substringUserName, string);
                TextView textView = this.mTv1;
                if (textView != null) {
                    textView.setText(spannableString);
                }
                String string2 = getResources().getString(R.string.label_notify_gift_2, globalWinGiftEntity.getGiftName(), substringUserName(globalWinGiftEntity.getToNickName(), 16), formatString(globalWinGiftEntity.getWinEnergy()));
                SpannableString spannableString2 = new SpannableString(string2);
                replaceRes(spannableString2, string2, this.diamond, R.drawable.ic_diamond);
                String substringUserName2 = substringUserName(globalWinGiftEntity.getToNickName(), 16);
                replaceNickName(spannableString2, substringUserName2, string2);
                TextView textView2 = this.mTv2;
                if (textView2 != null) {
                    textView2.setText(spannableString2);
                }

                ImageView imageView = this.mIcon;
                if (imageView != null) {
                    imageView.setImageResource(R.drawable.lucky_icon_gift_small);
                }
                if (globalWinGiftEntity.getGifticontype().equals("svg"))
                    commonMethods.imageLoaderSvg(getContext(), this.mGiftPic, globalWinGiftEntity.getGiftUrl());
                else if (globalWinGiftEntity.getGifticontype().equals("png"))
                    commonMethods.imageLoaderView(getContext(), this.mGiftPic, globalWinGiftEntity.getGiftUrl());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TextView textView3 = this.mTvLucky;
        if (textView3 != null) {
            textView3.setVisibility(View.GONE);
        }
        LinearLayout linearLayout = this.mllGift;
        if (linearLayout != null) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        TextView textView4 = this.mTv2;
        if (textView4 != null) {
            textView4.setVisibility(View.VISIBLE);
        }
        ivGo.setVisibility(globalWinGiftEntity.isGo() ? VISIBLE : GONE);
    }

    private final void replaceNickName(SpannableString spannableString, String str, String str2) {
        int a1 = str2.indexOf(str);
        if (a1 != -1) {
            setSpan(str, a1, spannableString, new ForegroundColorSpan(getContext().getResources().getColor(R.color.color_FE9555)), a1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private final void replaceNickNameGame(SpannableString spannableString, String str, String str2) {
        int a1 = str2.indexOf(str);
        if (a1 != -1) {
            setSpan(str, a1, spannableString, new ForegroundColorSpan(getContext().getResources().getColor(R.color.white)), a1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private final void replaceRes(SpannableString spannableString, String str, String str2, int i) {
        int a2 = 26;
        a2 = str.indexOf(str2);
        Drawable drawable = ContextCompat.getDrawable(getContext(),i);
        //C2875g.m6815a((Object) drawable, "drawable");
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int sp2px = AutoSizeUtils.sp2px(getContext(), 12.0f);
        double d = sp2px;
        Double.isNaN(d);
        double d2 = intrinsicHeight;
        Double.isNaN(d2);
        double d3 = intrinsicWidth;
        Double.isNaN(d3);
        drawable.setBounds(0, 0, (int) (d3 * ((d * 1.0d) / (d2 * 1.0d))), sp2px);
        setSpan(str2, a2, spannableString, new CenterImageSpan(drawable, 1), a2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /* access modifiers changed from: private */
    public final void startAnim() {
        measure(0, 0);
        float measuredWidth = (float) getMeasuredWidth();
        this.animation = ObjectAnimator.ofFloat(this, "x", this.mScreenWidth, -measuredWidth);
        Animator animator = this.animation;
        if (animator != null) {
            animator.setDuration(this.mDuration * ((long) 1000));
        }
        Animator animator2 = this.animation;
        if (animator2 != null) {
            animator2.setInterpolator(new LinearInterpolator());
        }
        Animator animator3 = this.animation;
        if (animator3 != null) {
            animator3.addListener(new GlobalWinLayout$startAnim$1(this));
        }
        Animator animator4 = this.animation;
        if (animator4 != null) {
            animator4.start();
        }
    }

    public final boolean getCanClick() {
        return this.canClick;
    }

    public final Object getData() {
        return this.data;
    }

    public final String getDiamond() {
        return this.diamond;
    }

    public final long getMDuration() {
        return this.mDuration;
    }

    public final float getMScreenWidth() {
        return this.mScreenWidth;
    }

    public final boolean isAniming() {
        return this.isAniming;
    }

   /* public void onDetachedFromWindow() { // naval comment this code for new requirement
        Log.e("onDetachedFromWindow","=====>");
        super.onDetachedFromWindow();
        Animator animator = this.animation;
        if (animator != null) {
            animator.cancel();
        }
    }*/

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mIcon = findViewById(R.id.icon);
        this.mTvLucky = findViewById(R.id.content_lucky);
        this.mllGift = findViewById(R.id.ll_gift_content);
        this.ll_root = findViewById(R.id.ll_root);
        this.mTv1 = findViewById(R.id.tv_1);
        this.mGiftPic = findViewById(R.id.giftIcon);
        this.mTv2 = findViewById(R.id.tv_2);
        this.ivGo = findViewById(R.id.iv_go);
        String a = "10";
        this.mDuration = Long.parseLong(a);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public final void setAnimation(Animator animator) {
        this.animation = animator;
    }

    public final void setAniming(boolean z) {
        this.isAniming = z;
    }

    public final void setCanClick(boolean z) {
        this.canClick = z;
    }

    public final void setData(Object obj) {
        this.data = obj;
    }

    public final void setMDuration(long j) {
        this.mDuration = j;
    }

    public final void setUp(Object obj) {
        this.data = obj;
        this.isAniming = true;
        Completable a = Completable.complete().subscribeOn(AndroidSchedulers.mainThread());
        CompletableLife a2 = a.to(RxLife.asOnMain(this));
        //app crashed during checking this issue --> live shows two time from clicking on recommended
        a2.subscribe(() -> {
            bindData((GlobalWinGiftEntity) obj);
            startAnim();
        }, throwable -> Log.e("TAG",
                "cannot save the event due to " + throwable.getMessage()));
    }

    public GlobalWinLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GlobalWinLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.diamond = "[diamond]";
        this.mScreenWidth = (float) ScreenUtils.getScreenSize(getContext())[0];
        this.mDuration = 10;
        this.canClick = true;
        LayoutInflater.from(context).inflate(R.layout.layout_global_win, this, true);
        setClipChildren(false);
    }

    public static String substringUserName(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.length() <= i) {
            return str;
        }
        return str.substring(0, i) + "...";
    }

    public static String formatString(long j) {
        String format = new DecimalFormat("#,###", new DecimalFormatSymbols(new Locale("en", "US"))).format(j);
        return format;
    }

    public static void setSpan(String str, int i2, SpannableString spannableString, Object obj, int i3, int i4) {
        spannableString.setSpan(obj, i3, str.length() + i2, i4);
    }
}