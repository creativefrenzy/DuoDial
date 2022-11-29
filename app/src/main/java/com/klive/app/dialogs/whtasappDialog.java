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

public class whtasappDialog extends Dialog {

    public whtasappDialog(@NonNull Context context) {
        super(context);
        init();
    }
    void init() {
        try {
            this.setContentView(R.layout.whatsapp);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);

            ImageView cancel =  findViewById(R.id.cancel);
            TextView value =  findViewById(R.id.input_number);
            TextView submit =  findViewById(R.id.submit);
            show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            value.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(s.toString().trim().length()==0){
                        submit.setEnabled(false);
                        submit.setTextColor(getContext().getResources().getColor(R.color.unselect_button));
                    } else {
                        submit.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                        submit.setEnabled(true);
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
