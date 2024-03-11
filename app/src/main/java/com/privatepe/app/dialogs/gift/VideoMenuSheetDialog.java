package com.privatepe.app.dialogs.gift;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.privatepe.app.Interface.GiftSelectListener;
import com.privatepe.app.R;
import com.privatepe.app.Zego.VideoChatZegoActivity;
import com.privatepe.app.Zego.VideoChatZegoActivityMet;
import com.privatepe.app.adapter.gift.GiftTabPagerAdapter;
import com.privatepe.app.fragments.gift.GiftTabFragment;
import com.privatepe.app.response.newgiftresponse.NewGift;
import com.privatepe.app.response.newgiftresponse.NewGiftResult;
import com.privatepe.app.utils.CustomViewPager;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class VideoMenuSheetDialog extends BottomSheetDialogFragment {

    private VideoChatZegoActivityMet videoChatZegoActivityMet;
    boolean isCameraOff, mIsFrontCamera, isMikeMute;

    public VideoMenuSheetDialog(VideoChatZegoActivityMet ctx, boolean isCameraOff, boolean mIsFrontCamera, boolean isMikeMute) {
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


