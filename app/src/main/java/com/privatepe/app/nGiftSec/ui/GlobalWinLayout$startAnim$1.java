package com.privatepe.app.nGiftSec.ui;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import kotlin.TypeCastException;

public final class GlobalWinLayout$startAnim$1 implements Animator.AnimatorListener {
    public final GlobalWinLayout globalWinLayout;

    public GlobalWinLayout$startAnim$1(GlobalWinLayout globalWinLayout) {
        this.globalWinLayout = globalWinLayout;
    }

    public void onAnimationCancel(Animator animator) {
        this.globalWinLayout.setAniming(false);
    }

    public void onAnimationEnd(Animator animator) {
        this.globalWinLayout.setAniming(false);
        this.globalWinLayout.setData(null);
        if (this.globalWinLayout.getParent() instanceof ViewGroup) {
            ViewParent parent = this.globalWinLayout.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                if (a(viewGroup.getTag(), "GlobalWinLayout")) {
                    viewGroup.setVisibility(View.GONE);
                    return;
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup");
        }
    }

    public void onAnimationRepeat(Animator animator) {
    }

    public void onAnimationStart(Animator animator) {
        GlobalWinLayout globalWinLayout = this.globalWinLayout;
        globalWinLayout.setX(globalWinLayout.getMScreenWidth());
        this.globalWinLayout.setVisibility(View.VISIBLE);
        if (this.globalWinLayout.getParent() instanceof ViewGroup) {
            ViewParent parent = this.globalWinLayout.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                    viewGroup.setVisibility(View.VISIBLE);
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup");
        }
    }

    public static boolean a(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

}
