package com.privatepe.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.privatepe.app.R;
import com.privatepe.app.databinding.DialogAccountInfoBinding;


public class AccountInfoDialog extends Dialog {

    DialogAccountInfoBinding binding;
    String username, password;

    public AccountInfoDialog(@NonNull Context context, String username, String password) {
        super(context);

        this.username = username;
        this.password = password;
        init();
    }

    void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_account_info, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        binding.username.setText("User Id: " + username);
        binding.password.setText("Password: " + password);
        show();

        binding.setClickListener(new EventHandler(getContext()));
    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void changePassword() {
            dismiss();
        }
    }
}