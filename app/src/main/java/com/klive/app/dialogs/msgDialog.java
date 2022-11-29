package com.klive.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.klive.app.R;

public class msgDialog extends Dialog {

    public msgDialog(@NonNull Context context) {
        super(context);
        init();
    }
    void init() {
        try {
            this.setContentView(R.layout.msg);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.CENTER);
            this.setCancelable(true);

            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}