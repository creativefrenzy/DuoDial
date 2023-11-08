package com.privatepe.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.privatepe.app.Interface.OnConfirmClickListner;
import com.privatepe.app.R;
import com.privatepe.app.databinding.TradingTransferDialogBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class TradingTransferDialog extends Dialog {

    TradingTransferDialogBinding binding;
    OnConfirmClickListner onConfirmClickListner;


    public TradingTransferDialog(@NonNull Context context, String transferToUserUserName, String transferToUserId, String transferAmount, String selectionType, OnConfirmClickListner listner) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.trading_transfer_dialog, null, false);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        onConfirmClickListner = listner;


        init(transferToUserUserName, transferToUserId, transferAmount, selectionType);

        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmClickListner.onConfirmClicked(transferToUserId, transferAmount, selectionType);
                dismiss();

            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    private void init(String transferToUserUserName, String transferToUserId, String transferAmount, String selectionType) {

        binding.username.setText("User Name : " + transferToUserUserName);
        binding.userid.setText("User ID : " + transferToUserId);
        binding.diamonds.setText("Diamond Amount : " + transferAmount);
        binding.account.setText("Account : " + selectionType);

        show();


    }

    private String getFormatedAmount(int amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }


}
