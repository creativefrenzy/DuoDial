package com.privatepe.app.nGiftSec.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.zeeplive.app.R;
import com.zeeplive.app.nGiftSec.model.Gift;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GiftControlLayout extends FrameLayout implements GiftChannelLayout.OnDismissListener {
    public int CHANNEL_HEIGHT;
    public int MAX_CHANNEL_COUNT;
    public int currentGiftChannelPosition;
    public List<GiftChannelLayout> giftChannels;
    public List<Gift> giftList;
    public GiftChannelLayout.OnCurrentListener onCurrentListener;

    public GiftControlLayout(Context context) {
        this(context, null, 0);
    }

    private Gift getNextGift() {
        List<Gift> list = this.giftList;
        if (!hasNextGift()) {
            return null;
        }
        return list.remove(0);
    }

    private void initTypedArray(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.GiftControlLayout);
        this.MAX_CHANNEL_COUNT = obtainStyledAttributes.getInteger(R.styleable.GiftControlLayout_max_channel_count, 3);
        obtainStyledAttributes.recycle();
    }

    private GiftChannelLayout newGiftChannel(int i) {
        GiftChannelLayout giftChannelLayout = new GiftChannelLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(-1, this.CHANNEL_HEIGHT);
        layoutParams.bottomMargin = i;
        layoutParams.gravity = 80;
        giftChannelLayout.setLayoutParams(layoutParams);
        giftChannelLayout.setOnDismissListener(this);
        giftChannelLayout.setOnGiftListener(this.onCurrentListener);
        return giftChannelLayout;
    }

    private void showNextGift(GiftChannelLayout giftChannelLayout, Gift gift) {
        if (giftChannelLayout.getVisibility() != View.VISIBLE) {
            removeView(giftChannelLayout);
            return;
        }
        Gift nextGift = getNextGift();
        if (nextGift == null) {
            removeView(giftChannelLayout);
        } else {
            giftChannelLayout.showGiftEffects(nextGift);
        }
    }

    public void addGift(Gift gift) {
        boolean z;
        List<GiftChannelLayout> list = this.giftChannels;
        for (int i = 0; i < list.size(); i++) {
            GiftChannelLayout giftChannelLayout = list.get(i);
            if (giftChannelLayout.isSameGift(gift) && !giftChannelLayout.isDismissing() && giftChannelLayout.isShowing()) {
                giftChannelLayout.receiveSameGift(gift);
                this.currentGiftChannelPosition = i;
                return;
            }
        }
        int i2 = 0;
        while (true) {
            if (i2 >= list.size()) {
                break;
            }
            GiftChannelLayout giftChannelLayout2 = list.get(i2);
            if (giftChannelLayout2.isSameGift(gift) && giftChannelLayout2.isDismissing()) {
                gift.mergeGift(giftChannelLayout2.getCurrentGift());
                this.currentGiftChannelPosition = i2;
                break;
            }
            i2++;
        }
        int i3 = 0;
        while (i3 < this.MAX_CHANNEL_COUNT) {
            if (list.size() <= i3) {
                GiftChannelLayout newGiftChannel = newGiftChannel(getResources().getDimensionPixelOffset(R.dimen.dp_55) * i3);
                addView(newGiftChannel);
                list.add(newGiftChannel);
                this.currentGiftChannelPosition = i3;
                newGiftChannel.showGiftEffects(gift);
                return;
            }
            GiftChannelLayout giftChannelLayout3 = list.get(i3);
            if (giftChannelLayout3.getParent() == null) {
                addView(giftChannelLayout3, i3);
            }
            if (giftChannelLayout3.isShowing()) {
                i3++;
            } else {
                this.currentGiftChannelPosition = i3;
                giftChannelLayout3.showGiftEffects(gift);
                return;
            }
        }
        long userId = 1;
        int size = this.giftList.size();
        int i4 = 0;
        while (true) {
            z = true;
            if (i4 >= size) {
                z = false;
                break;
            }
            Gift gift2 = this.giftList.get(i4);
            if (gift2.equals(gift)) {
                gift2.mergeGift(gift);
                gift = gift2;
                break;
            }
            if (gift2.getFromUserId() != userId) {
                if (gift.getFromUserId() != userId) {
                    if (gift2.getToUserId() != userId && gift.getToUserId() == userId) {
                        this.giftList.add(i4, gift);
                        break;
                    }
                } else {
                    this.giftList.add(i4, gift);
                    break;
                }
            }
            i4++;
        }
        if (!z) {
            this.giftList.add(gift);
            this.currentGiftChannelPosition = 3;
        }
    }

    public int getCurrentGiftChannelPosition() {
        return this.currentGiftChannelPosition;
    }

    public boolean hasNextGift() {
        List<Gift> list = this.giftList;
        return list != null && list.size() > 0;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        switchRoom();
    }

    public void onDismiss(GiftChannelLayout giftChannelLayout, Gift gift) {
        showNextGift(giftChannelLayout, gift);
    }

    public void setOnCurrentListener(GiftChannelLayout.OnCurrentListener onCurrentListener2) {
        this.onCurrentListener = onCurrentListener2;
    }

    public void switchRoom() {
        this.giftList.clear();
        for (GiftChannelLayout cancelAnim : this.giftChannels) {
            cancelAnim.cancelAnim();
        }
    }

    public GiftControlLayout(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GiftControlLayout(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.giftChannels = new ArrayList();
        this.giftList = new LinkedList();
        this.currentGiftChannelPosition = 2;
        initTypedArray(context, attributeSet);
        this.CHANNEL_HEIGHT = getResources().getDimensionPixelOffset(R.dimen.dp_55);
    }
}
