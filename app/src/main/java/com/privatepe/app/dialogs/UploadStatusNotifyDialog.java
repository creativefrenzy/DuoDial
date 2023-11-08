package com.privatepe.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.privatepe.app.R;
import com.privatepe.app.activity.RecordStatusActivity;
import com.privatepe.app.databinding.UploadStatusNotifyDialogBinding;

public class UploadStatusNotifyDialog extends Dialog {

    UploadStatusNotifyDialogBinding binding;
    CloseBtnListener closeBtnListener;

    public UploadStatusNotifyDialog(@NonNull Context context,CloseBtnListener listener) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.upload_status_notify_dialog, null, false);
        setContentView(binding.getRoot());
        closeBtnListener=listener;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        init();
        show();
    }

    private void init() {


        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtnListener.onCloseBtnClick();
                onBackPressed();
            }
        });


        binding.uploadNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RecordStatusActivity.class);
                getContext().startActivity(intent);
                dismiss();

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public interface CloseBtnListener {
        void onCloseBtnClick();
    }


}
