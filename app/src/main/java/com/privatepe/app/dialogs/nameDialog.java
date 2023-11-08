package com.privatepe.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.privatepe.app.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class nameDialog extends Dialog {
    OnMyDialogResult myDialogResult;
    private Context context;
    private String token;
    private String name;
    public nameDialog(@NonNull Context context, String token, String name) {
        super(context);
        this.context = context;
        this.token = token;
        this.name = name;
        init();
    }
    void init() {
        try {
            this.setContentView(R.layout.name);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);
            ImageView cancel =  findViewById(R.id.cancel);
            TextView counter = findViewById(R.id.count);
            TextView value =  findViewById(R.id.input);
            TextView submit =  findViewById(R.id.submit);
            value.setText(name);
            show();
            value.setLongClickable(false);
            value.setTextIsSelectable(false);
          //  value.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
            value.setFilters(new InputFilter[]{acceptonlyAlphabetValuesnotNumbersMethod()});
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            counter.setText(value.getText().length()+"/20");
            value.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    counter.setText(String.valueOf(s.length())+"/20");
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
    //Accept Only Alphabet in EditText
    public static InputFilter acceptonlyAlphabetValuesnotNumbersMethod() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean isCheck = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) {
                        sb.append(c);
                    } else {
                        isCheck = false;
                    }
                }
                if (isCheck)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString spannableString = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, spannableString, 0);
                        return spannableString;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
                Matcher match = pattern.matcher(String.valueOf(c));
                return match.matches();
            }
        };
    }
}