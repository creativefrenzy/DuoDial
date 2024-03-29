package com.privatepe.app.dialogs;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.privatepe.app.R;

public class MyProgressDialog extends Dialog{
    public MyProgressDialog(@NonNull Context context) {
        super(context);

        init();
    }

    void init() {
        this.setContentView(R.layout.loader_layout);
        this.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);

        LottieAnimationView animationView = findViewById(R.id.animation_view);
        animationView.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(getContext().getResources().getColor(R.color.colorPrimary),
                                PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );
    }


    public void setCancelablethis(boolean b)
    {
        this.setCancelable(b);
        this.setCanceledOnTouchOutside(false);
    }
}
