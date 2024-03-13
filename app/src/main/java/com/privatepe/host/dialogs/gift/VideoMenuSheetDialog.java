package com.privatepe.host.dialogs.gift;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.privatepe.host.R;
import com.privatepe.host.Zego.VideoChatZegoActivity;

public class VideoMenuSheetDialog extends BottomSheetDialogFragment {

    private VideoChatZegoActivity videoChatZegoActivityMet;
    boolean isCameraOff, mIsFrontCamera, isMikeMute;

    public VideoMenuSheetDialog(VideoChatZegoActivity ctx, boolean isCameraOff, boolean mIsFrontCamera, boolean isMikeMute) {
        videoChatZegoActivityMet = ctx;
        this.isCameraOff = isCameraOff;
        this.mIsFrontCamera = mIsFrontCamera;
        this.isMikeMute = isMikeMute;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    Switch cameraOffSwitchBtn, micSwitchBtn,switchCameraSwitchBtn;
    TextView tv_cameraName;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_menusheet_dialog, container, false);
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable());

        cameraOffSwitchBtn = view.findViewById(R.id.cameraOffSwitchBtn);
        switchCameraSwitchBtn = view.findViewById(R.id.switchCameraSwitchBtn);
        micSwitchBtn = view.findViewById(R.id.micSwitchBtn);
        tv_cameraName = view.findViewById(R.id.tv_cameraName);

        if (isCameraOff) {
            cameraOffSwitchBtn.setChecked(true);
        } else {
            cameraOffSwitchBtn.setChecked(false);
        }
        if (mIsFrontCamera) {
            switchCameraSwitchBtn.setChecked(true);
            tv_cameraName.setText("Front Camera");
        } else {
            switchCameraSwitchBtn.setChecked(false);
            tv_cameraName.setText("Rear Camera");
        }
        if (isMikeMute) {
            micSwitchBtn.setChecked(true);
        } else {
            micSwitchBtn.setChecked(false);
        }

        cameraOffSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                videoChatZegoActivityMet.cameraOffFun();
            }
        });
        switchCameraSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                videoChatZegoActivityMet.flipCamera();
            }
        });
        micSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                videoChatZegoActivityMet.muteMic();
            }
        });


        return view;

    }

    public void setCameraName(String name) {
        tv_cameraName.setText(name);
    }

}


