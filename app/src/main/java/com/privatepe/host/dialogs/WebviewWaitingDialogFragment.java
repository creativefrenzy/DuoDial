package com.privatepe.host.dialogs;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.privatepe.host.R;
import com.privatepe.host.databinding.FragmentWebviewWaitingDialogBinding;

public class WebviewWaitingDialogFragment extends DialogFragment implements View.OnClickListener {

    public final static String TAG = WebviewWaitingDialogFragment.class.getSimpleName();
    private FragmentWebviewWaitingDialogBinding binding;

    public static WebviewWaitingDialogFragment newInstance() {
        WebviewWaitingDialogFragment fragment = new WebviewWaitingDialogFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,  R.style.DialogThemeWithBackDim);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWebviewWaitingDialogBinding.inflate(getLayoutInflater());
        init();
        setListener();
        return binding.getRoot();
    }

    private void init() {

    }

    private void setListener() {
        binding.cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelBtn:
                dismiss();
                break;
            default:
                break;
        }
    }
}