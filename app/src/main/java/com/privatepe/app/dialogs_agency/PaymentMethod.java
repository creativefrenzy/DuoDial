package com.privatepe.app.dialogs_agency;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.privatepe.app.R;
import com.privatepe.app.activity.AddAccountActivity;
import com.privatepe.app.activity.AddUpiActivity;
import com.privatepe.app.databinding.BankNameListDialogBinding;
import com.privatepe.app.databinding.PaymentMethodDialogBinding;

public class PaymentMethod extends Dialog {
    PaymentMethodDialogBinding binding;
    Context context;

    public PaymentMethod(@NonNull Context context) {
        super(context);
        init();
    }

    void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.payment_method_dialog, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        show();

       // binding.setClickListener(new EventHandler(getContext()));

    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void pageClose() {
            dismiss();
        }

        public void add_Account() {
            Intent intent = new Intent(mContext, AddAccountActivity.class);
            mContext.startActivity(intent);
            dismiss();
        }

        public void add_Upi() {
            Intent intent = new Intent(mContext, AddUpiActivity.class);
            mContext.startActivity(intent);
            dismiss();
        }


    }


}