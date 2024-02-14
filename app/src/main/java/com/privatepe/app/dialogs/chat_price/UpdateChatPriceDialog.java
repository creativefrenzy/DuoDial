package com.privatepe.app.dialogs.chat_price;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.privatepe.app.R;
import com.privatepe.app.databinding.UpdateChatPriceDialogBinding;


public class UpdateChatPriceDialog extends Dialog {
    private UpdateChatPriceDialogBinding binding;


    public UpdateChatPriceDialog(@NonNull Context context) {
        super(context);
         binding= DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.update_chat_price_dialog, null, false);
         setContentView(binding.getRoot());
         getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         show();

    }
}
