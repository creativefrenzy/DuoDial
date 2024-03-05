package com.privatepe.host.dialogs;

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

import com.privatepe.host.R;

public class bioDialog extends Dialog {
    OnMyDialogResult myDialogResult;
    private Context context;
    private String token;
    private String bio = "";
    public bioDialog(@NonNull Context context, String token, String bio) {
        super(context);
        this.context = context;
        this.token = token;
        this.bio = bio;
        init();
    }
    void init() {
        try {
            this.setContentView(R.layout.bio);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);
            ImageView cancel =  findViewById(R.id.cancel);
            TextView counter = findViewById(R.id.count);
            TextView value =  findViewById(R.id.input);
            TextView submit =  findViewById(R.id.submit);
            show();
            if(bio != null){
                //bio = "";
                value.setText(bio);
            }

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            counter.setText(value.getText().length()+"/32");
            value.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    counter.setText(String.valueOf(s.length())+"/32");
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
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialogResult.finish(value.getText().toString());
                    dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setDialogResult(OnMyDialogResult dialogResult){
        myDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void  finish(String result);
    }
}