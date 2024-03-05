package com.privatepe.host.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.privatepe.host.R;

public class ChooseNumber extends Dialog {
    public ChooseNumber(@NonNull Context context) {
        super(context);
        init();
    }
    void init() {
        try {
            this.setContentView(R.layout.choose_number);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}