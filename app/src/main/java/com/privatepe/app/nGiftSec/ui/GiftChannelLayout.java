package com.privatepe.app.nGiftSec.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.opensource.svgaplayer.SVGAImageView;
import com.zeeplive.app.R;
import com.zeeplive.app.activity.party.utils.CommonMethods;
import com.zeeplive.app.nGiftSec.model.Gift;
import com.zeeplive.app.nGiftSec.model.LuckyWinEntity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GiftChannelLayout extends FrameLayout {
    public Disposable GiftPictureAnimator;
    public Context context;
    public OnCurrentListener currentGiftListener;
    public boolean isDismissing;
    public boolean isShowing;
    public RelativeLayout ll_head_portrait_picture;
    public Gift mCurrentGift;
    public Disposable mDelayDisposable;
    public CircleImagesView mFromUserHead;
    public TextView mGiftCountView;
    public TextView mGiftCountX;
    public View mItemBgView;
    public PhotoView mIvGiftPicture;
    public LinearLayout mLlGiftCount;
    public LinearLayout mLlGiftNameTop;
    public LinearLayout mLlLucyWinNameTop;
    public ConstraintLayout mRlGiftInfo;
    public Disposable mSubscribe;
    public CountDownTimer mTimer;
    public TextView mTvLuckyWin;
    public TextView mTvLuckyWinBottom;
    public TextView mTvToTargetName;
    public Gift mWaitingGift;
    public OnDismissListener onDismissListener;
    public ObjectAnimator rotateAnimator;
    public ObjectAnimator scaleGiftNumAnimator;
    public AnimatorSet transXIn;
    public AnimatorSet transXOut;
    CommonMethods commonMethods;
    public TextView premiumUserListLevelTxt;
    public AppCompatImageView premiumBadge;
    public SVGAImageView svga_frameGift;
    public LinearLayout send_to_new;
    public LinearLayout sender_name_ll_new;
    public TextView sender_name_txt;

    public interface OnCurrentListener {
        void showBlindBoxGiftDetail(Gift gift);

        void showUserDetail(Gift gift);
    }

    public interface OnDismissListener {
        void onDismiss(GiftChannelLayout giftChannelLayout, Gift gift);
    }

    public GiftChannelLayout(Context context2) {
        this(context2, null, 0);
        commonMethods = new CommonMethods();
    }

    /* access modifiers changed from: private */
    public void delayLuckyWinDismiss(int i) {
        if (i == 0) {
            i = 6;
        }
        Disposable bVar = this.mDelayDisposable;
        if (bVar != null) {
            bVar.dispose();
        }
        this.mDelayDisposable = Flowable.timer(i * 1000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doFinally(new Action() {
            @Override
            public void run() throws Throwable {
                hideGift();
            }
        }).subscribe();
    }

    public void hideGiftEffects(int i) {
        if (this.transXOut == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "X", getTranslationX(), (float) SizeUtils.dp2px(50.0f));
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, ofFloat2);
            animatorSet.setInterpolator(new AccelerateInterpolator());
            animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    GiftChannelLayout.this.setAlpha(1.0f);
                    GiftChannelLayout.this.setTranslationX(0.0f);
                    GiftChannelLayout.this.mGiftCountView.setVisibility(View.GONE);
                    boolean unused = GiftChannelLayout.this.isShowing = false;
                    Gift access$000 = GiftChannelLayout.this.mCurrentGift;
                    boolean unused2 = GiftChannelLayout.this.isDismissing = false;
                    Gift unused3 = GiftChannelLayout.this.mCurrentGift = null;
                    Disposable bVar = GiftChannelLayout.this.GiftPictureAnimator;
                    if (bVar != null) {
                        bVar.dispose();
                        GiftChannelLayout.this.GiftPictureAnimator = null;
                    }
                    if (GiftChannelLayout.this.onDismissListener != null) {
                        GiftChannelLayout.this.onDismissListener.onDismiss(GiftChannelLayout.this, access$000);
                    }
                }

                public void onAnimationStart(Animator animator) {
                    boolean unused = GiftChannelLayout.this.isDismissing = true;
                }
            });
            this.transXOut = animatorSet;
        }
        this.transXOut.setDuration(i);
        this.transXOut.start();
    }

    private void intiView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.gift_channel_layout, this);
        this.mRlGiftInfo = inflate.findViewById(R.id.rlGiftInfo);
        this.mFromUserHead = inflate.findViewById(R.id.head);
        this.ll_head_portrait_picture = inflate.findViewById(R.id.ll_head_portrait_picture);
        this.mTvToTargetName = inflate.findViewById(R.id.tv_gift_target_to);
        this.mIvGiftPicture = inflate.findViewById(R.id.iv_gift);
        this.mGiftCountView = inflate.findViewById(R.id.gift_count);
        this.mGiftCountX = inflate.findViewById(R.id.gift_count_x);
        this.mLlLucyWinNameTop = inflate.findViewById(R.id.topwin);
        this.mTvLuckyWin = inflate.findViewById(R.id.tv_win);
        this.mTvLuckyWinBottom = inflate.findViewById(R.id.tv_lucky_win_bottom);
        this.mLlGiftNameTop = inflate.findViewById(R.id.cl_from_name);
        this.mItemBgView = inflate.findViewById(R.id.view_item_bg);
        this.mLlGiftCount = inflate.findViewById(R.id.ll_gift_count);
        this.premiumUserListLevelTxt = inflate.findViewById(R.id.premiumUserListLevelTxt);
        this.premiumBadge = inflate.findViewById(R.id.premiumBadge);
        this.svga_frameGift = inflate.findViewById(R.id.svga_frameGift);
        this.send_to_new = inflate.findViewById(R.id.send_to_new);
        this.sender_name_ll_new = inflate.findViewById(R.id.sender_name_ll);
        this.sender_name_txt = inflate.findViewById(R.id.sender_name_txt);
    }

    public boolean isLucyWin(Gift gift) {
        return gift instanceof LuckyWinEntity;
    }

    private void showGiftPictureAnimator(int i) {
        if (this.rotateAnimator == null) {
            this.rotateAnimator = ObjectAnimator.ofFloat(this.mIvGiftPicture, "rotation", 0.0f, 30.0f, 0.0f, -30.0f, 0.0f);
            this.rotateAnimator.setDuration(500);
            this.rotateAnimator.setRepeatCount(-1);
            this.rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
            this.rotateAnimator.setInterpolator(new LinearInterpolator());
        }
        this.rotateAnimator.start();
        Disposable bVar = this.GiftPictureAnimator;
        if (bVar != null) {
            bVar.dispose();
            this.GiftPictureAnimator = null;
        }
        this.GiftPictureAnimator = Flowable.timer(i, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        mo69956a(aLong);
                    }
                });
    }

    private void showView() {
        if (this.transXIn == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "X", -SizeUtils.dp2px(200.0f), 0.0f);
            ofFloat.setDuration(150);
            ObjectAnimator.ofFloat(this.mIvGiftPicture, "X", 0.0f, SizeUtils.dp2px(175.0f)).setDuration(300);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat);
            animatorSet.setInterpolator(new AccelerateInterpolator());
            animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    if (GiftChannelLayout.this.mCurrentGift != null && GiftChannelLayout.this.isShowing) {
                        GiftChannelLayout giftChannelLayout = GiftChannelLayout.this;
                        if (giftChannelLayout.isLucyWin(giftChannelLayout.mCurrentGift) /*|| (GiftChannelLayout.this.mCurrentGift instanceof LuckyNumWinEntity)*/) {
                            GiftChannelLayout giftChannelLayout2 = GiftChannelLayout.this;
                            giftChannelLayout2.delayLuckyWinDismiss(giftChannelLayout2.mCurrentGift.getStreamerTime());
                            return;
                        }
                        GiftChannelLayout giftChannelLayout3 = GiftChannelLayout.this;
                        giftChannelLayout3.showZoom(giftChannelLayout3.mCurrentGift);
                    }
                }
            });
            this.transXIn = animatorSet;
        }
        this.transXIn.start();
    }

    private void showWaitingGift() {
        Gift gift = this.mWaitingGift;
        if (gift != null) {
            CountDownTimer countDownTimer = this.mTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
                this.mTimer = null;
            }
            gift.mergeGift(this.mCurrentGift);
            this.mCurrentGift = gift;
            this.mWaitingGift = null;
            showZoom(this.mCurrentGift);
        }
    }

    public void showZoom(Gift gift) {
        int i = 0;
        this.mGiftCountView.setVisibility(View.VISIBLE);
        Disposable bVar = this.mSubscribe;
        if (bVar != null) {
            bVar.dispose();
        }
        try {
            i = Integer.parseInt(this.mGiftCountView.getText().toString());
        } catch (Exception unused) {
        }
        CountDownTimer countDownTimer = this.mTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (gift.getShakeTime() > 0) {
            showGiftPictureAnimator(gift.getShakeTime());
        }
        int finalI = i;
        this.mSubscribe = Observable.interval(0, 150, TimeUnit.MILLISECONDS)
                .take(gift.getCount() - i)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        show(gift);
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        countDownTextAnimation(finalI, aLong);
                    }
                });
    }

    private void timer(int i) {
        if (i == 0) {
            i = 3;
        }
        CountDownTimer countDownTimer = this.mTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.mTimer = new CountDownTimer(i * 1000, 1000) {
            public void onFinish() {
                GiftChannelLayout.this.mTimer.cancel();
                CountDownTimer unused = GiftChannelLayout.this.mTimer = null;
                GiftChannelLayout.this.hideGiftEffects(200);
            }

            public void onTick(long j) {
            }
        }.start();
    }

    /* renamed from: a */
    public /* synthetic */ void mo69954a(View view) {
        Gift gift;
        OnCurrentListener onCurrentListener = this.currentGiftListener;
        if (onCurrentListener != null && (gift = this.mCurrentGift) != null) {
            onCurrentListener.showUserDetail(gift);
        }
    }

    /* renamed from: b */
    public /* synthetic */ void mo69958b(View view) {
        Gift gift;
        if (this.currentGiftListener != null && (gift = this.mCurrentGift) != null && gift.getIsCollectiveGift() == 1) {
            this.currentGiftListener.showBlindBoxGiftDetail(this.mCurrentGift);
        }
    }

    public void cancelAnim() {
        if (this.isShowing) {
            this.isShowing = false;
            CountDownTimer countDownTimer = this.mTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            hideGiftEffects(0);
        }
    }

    public Gift getCurrentGift() {
        return this.mCurrentGift;
    }

    public boolean isDismissing() {
        return this.isDismissing;
    }

    public boolean isSameGift(Gift gift) {
        Gift gift2 = this.mCurrentGift;
        return gift2 != null && gift2.equals(gift);
    }

    public boolean isShowing() {
        return this.isShowing;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Disposable bVar = this.mSubscribe;
        if (bVar != null) {
            bVar.dispose();
        }
        CountDownTimer countDownTimer = this.mTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void receiveSameGift(Gift gift) {
        Gift gift2 = this.mWaitingGift;
        if (gift2 == null) {
            this.mWaitingGift = gift;
        } else {
            gift2.mergeGift(gift);
        }
        if (!this.isDismissing && this.mTimer != null) {
            showWaitingGift();
        }
    }

    public ObjectAnimator scaleGiftNum(TextView textView) {
        return ObjectAnimator.ofPropertyValuesHolder(textView, PropertyValuesHolder.ofFloat("scaleX", 1.5f, 0.7f, 1.0f), PropertyValuesHolder.ofFloat("scaleY", 1.5f, 0.7f, 1.0f)).setDuration(150);
    }

    public void setOnDismissListener(OnDismissListener onDismissListener2) {
        this.onDismissListener = onDismissListener2;
    }

    public void setOnGiftListener(OnCurrentListener onCurrentListener) {
        this.currentGiftListener = onCurrentListener;
    }

    public void showGiftEffects(Gift gift) {
       // Log.e("frameLog", new Gson().toJson(gift));

        this.mCurrentGift = gift;
        this.isShowing = true;
        int i = 0;
        this.mRlGiftInfo.setVisibility(View.VISIBLE);
        boolean z = isLucyWin(gift);
        //Log.e("frameLog", " z => " + z);

        if (z) {
            LuckyWinEntity luckyWinEntity = (LuckyWinEntity) gift;
            TextView textView = this.mTvLuckyWinBottom;
            textView.setText(getContext().getString(R.string.gift_by_send) + " " + luckyWinEntity.getGiftName());
            TextView textView2 = this.mTvLuckyWin;
            textView2.setText(getContext().getString(R.string.gift_won) + StringFormating(luckyWinEntity.getWinEnergy()));
            updateLevel(luckyWinEntity.getFromLevel(), String.valueOf(gift.fromUserSex));
        } else {
            if (gift.getIcon_type().equals("svg"))
                commonMethods.imageLoaderSvg(getContext(), this.mIvGiftPicture, gift.getIcon());
            else if (gift.getIcon_type().equals("png"))
                commonMethods.imageLoaderView(getContext(), this.mIvGiftPicture, gift.getIcon());
            this.mTvToTargetName.setText(gift.getToName());
            this.mGiftCountView.setText("0");
            updateLevel(gift.fromUserVlevel, String.valueOf(gift.fromUserSex));
        }
        this.mTvLuckyWinBottom.setVisibility(z ? View.VISIBLE : View.INVISIBLE);
        this.mLlLucyWinNameTop.setVisibility(z ? View.VISIBLE : View.INVISIBLE);
        this.mLlGiftNameTop.setVisibility(z ? View.INVISIBLE : View.VISIBLE);
        this.mIvGiftPicture.setVisibility(z ? View.INVISIBLE : View.VISIBLE);
        this.mLlGiftCount.setVisibility(z ? View.INVISIBLE : View.VISIBLE);
        TextView textView3 = this.mTvToTargetName;
        if (z) {
            i = 4;
        }
        textView3.setVisibility(i);
        if (gift.isHideGiftNum() && !z) {
            this.mLlGiftCount.setVisibility(View.INVISIBLE);
        }
        this.mItemBgView.setBackgroundResource(z ? R.drawable.gift_winlayout_bg : R.drawable.gift_channel_bg);
        showView();
        if (z)
            commonMethods.imageLoaderView(getContext(), this.mFromUserHead, ((LuckyWinEntity) gift).getHeadUrl());
        else
            commonMethods.imageLoaderView(getContext(), this.mFromUserHead, gift.getFromHead());
        if (gift.getShakeTime() > 0) {
            showGiftPictureAnimator(gift.getShakeTime());
        }
        if(gift.fromScreen != null) {
                if(gift.fromScreen.equalsIgnoreCase("gift_moment")) {
                    try {
                        this.send_to_new.setVisibility(View.VISIBLE);
                        this.sender_name_ll_new.setVisibility(View.VISIBLE);
                        this.sender_name_txt.setText(gift.fromName);
                        this.mLlGiftNameTop.setVisibility(View.GONE);
                        this.mTvToTargetName.setTextSize(10);
                    }catch (Exception e) {
                }
            }
        }

    }

    // for show level and badge
    public void updateLevel(String level, String gender) {
        TextView textView1 = this.premiumUserListLevelTxt;
        textView1.setText(level);
       // Log.e("frameLog", "m here");
       // Log.e("frameLog", gender+ " level => "+ level);
        // premiumBadge.setImageResource(commonMethods.setupFrame(Integer.parseInt(level)));
        // svga_frameGift
        if (gender.equals("1")) {
            commonMethods.setupFrameWithGender(Integer.parseInt(level),
                    "male", svga_frameGift, premiumBadge);
        }else {
            commonMethods.setupFrameWithGender(Integer.parseInt(level),
                    "female", svga_frameGift, premiumBadge);
        }

      /*  switch (level) {
            case "Lv1":
            case "Lv2":
                premiumBadge.setImageResource(R.drawable.src_res_level_new_lv7_outer_img);
                break;
            case "Lv3":
            case "Lv5":
            case "Lv4":
                premiumBadge.setImageResource(R.drawable.src_res_level_new_lv8_outer_img);
                break;
            case "Lv6":
            case "Lv7":
            case "Lv8":
                premiumBadge.setImageResource(R.drawable.src_res_level_new_lv9_outer_img);
                break;
            case "Lv9":
            case "Lv10":
            case "Lv15":
                premiumBadge.setImageResource(R.drawable.src_res_level_new_lv10_outer_img);
                break;
            case "Lv20":
                premiumBadge.setImageResource(R.drawable.src_res_level_new_lv20_outer_img);
                break;
            default:
                premiumBadge.setImageResource(0);
                break;
        }*/
    }

    public GiftChannelLayout(Context context2, AttributeSet attributeSet) {
        this(context2, attributeSet, 0);
    }

    public GiftChannelLayout(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        this.mTimer = null;
        this.mCurrentGift = null;
        this.mWaitingGift = null;
        this.isDismissing = false;
        this.context = context2;
        intiView();
    }

    public void mo69956a(Long l) throws Exception {
        this.GiftPictureAnimator.dispose();
        this.rotateAnimator.end();
        this.mIvGiftPicture.clearAnimation();
    }

    public void levelBackGround(boolean z, Gift gift) {
       /* if (z) {
            C2581i<Bitmap> a = C2187b.m5357c(BaseApplication.f2202a.getApplicationContext()).mo21784a();
            a.mo21736a(StringUtils.getScaleImageUrl(((LuckyWinEntity) gift).getHeadUrl(), StringUtils.Head300));
            ((C2581i) a.mo21658c(C9372g.ic_placeholder_circle)).mo21731a((ImageView) this.mFromUserHead);
            return;
        }
        C2581i<Bitmap> a2 = C2187b.m5357c(BaseApplication.f2202a.getApplicationContext()).mo21784a();
        a2.mo21736a(StringUtils.getScaleImageUrl(gift.getFromHead(), StringUtils.Head300));
        ((C2581i) a2.mo21658c(C9372g.ic_placeholder_circle)).mo21731a((ImageView) this.mFromUserHead);*/
    }

    public void hideGift() throws Exception {
        hideGiftEffects(200);
    }

    public void show(Gift gift) throws Exception {
        if (this.isShowing) {
            if (this.mWaitingGift != null) {
                showWaitingGift();
            } else {
                timer(gift.getStreamerTime());
            }
        }
    }

    public void countDownTextAnimation(int i, Long l) throws Exception {
        long longValue = l.longValue() + 1 + ((long) i);
        if (longValue < 10) {
            this.mGiftCountView.setShadowLayer(12.0f, 0.0f, 0.0f, -6975331);
            this.mGiftCountX.setShadowLayer(12.0f, 0.0f, 0.0f, -6975331);
        } else if (longValue < 100) {
            this.mGiftCountView.setShadowLayer(12.0f, 0.0f, 0.0f, -45982);
            this.mGiftCountX.setShadowLayer(12.0f, 0.0f, 0.0f, -45982);
        } else {
            this.mGiftCountView.setShadowLayer(12.0f, 0.0f, 0.0f, -6601475);
            this.mGiftCountX.setShadowLayer(12.0f, 0.0f, 0.0f, -6601475);
        }
        TextView textView = this.mGiftCountView;
        textView.setText("" + longValue);
        ObjectAnimator objectAnimator = this.scaleGiftNumAnimator;
        if (objectAnimator == null || !objectAnimator.isRunning()) {
            ObjectAnimator objectAnimator2 = this.scaleGiftNumAnimator;
            if (objectAnimator2 != null) {
                objectAnimator2.start();
                return;
            }
            this.scaleGiftNumAnimator = scaleGiftNum(this.mGiftCountView);
            this.scaleGiftNumAnimator.start();
        }
    }

    public static String StringFormating(String str) {
        if (TextUtils.isEmpty(str)) {
            return ConvertFormat(0L);
        }
        try {
            return ConvertFormat(Long.valueOf(Long.parseLong(str)).longValue());
        } catch (Exception unused) {
            return str;
        }
    }

    public static String ConvertFormat(long j2) {
        String format = new DecimalFormat("#,###", new DecimalFormatSymbols(new Locale("en", "US"))).format(j2);
        return format;
    }
}
