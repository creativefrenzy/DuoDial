package com.klive.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.klive.app.R;

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