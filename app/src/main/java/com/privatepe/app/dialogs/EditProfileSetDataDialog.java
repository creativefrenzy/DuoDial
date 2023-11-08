package com.privatepe.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.privatepe.app.R;
import com.privatepe.app.activity.EditProfileActivityNew;
import com.privatepe.app.databinding.DialogEditProfileSetDataBinding;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;


public class EditProfileSetDataDialog extends Dialog implements ApiResponseInterface {

    DialogEditProfileSetDataBinding binding;
    private String Heading, CurrentValue;
    private ApiManager apiManager;
    EditProfileActivityNew activityNew;

    public EditProfileSetDataDialog(@NonNull Context context, String heading, String previousValue) {
        super(context, android.R.style.ThemeOverlay);
        Heading = heading;
        activityNew = (EditProfileActivityNew) context;
        CurrentValue = previousValue;
        init();
    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_edit_profile_set_data, null, false);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setStatusBarColor(getContext().getColor(R.color.white));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && true) {
            flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        getWindow().getDecorView().setSystemUiVisibility(flag);
        apiManager = new ApiManager(getContext(), this);

        setUp();
        show();


    }

    private void setUp() {
        binding.heading.setText(Heading);

        if (Heading.equals("Change Name")) {
            binding.editMultipurposeLay.setVisibility(View.VISIBLE);
            binding.editIntroLay.setVisibility(View.GONE);
            binding.editMultipurpose.setText(CurrentValue);
            binding.editMultipurpose.setHint("Enter your name");
            binding.saveBtn.setOnClickListener(null);

            binding.saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateData("name");
                }
            });

        } else if (Heading.equals("Edit Introduction")) {
            binding.editMultipurposeLay.setVisibility(View.GONE);
            binding.editIntroLay.setVisibility(View.VISIBLE);
            binding.editIntro.setText(CurrentValue);
            binding.saveBtn.setOnClickListener(null);

            binding.saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateData("aboutUser");
                }
            });

        }
        /*else if (Heading.equals("Change Country")) {
            binding.editMultipurposeLay.setVisibility(View.VISIBLE);
            binding.editIntroLay.setVisibility(View.GONE);
            binding.editMultipurpose.setText(CurrentValue);
            binding.editMultipurpose.setHint("Enter your city");

        }*/

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();


            }
        });

    }


    public void updateData(String type) {

        if (type.equals("name")) {
            apiManager.updateProfileDetailsNew("name",binding.editMultipurpose.getText().toString(), "", "", "", null, false);

        }  else if (type.equals("aboutUser")) {
            apiManager.updateProfileDetailsNew("aboutUser", "", "", "",binding.editIntro.getText().toString(), null, false);

        }








        // apiManager.updateProfileDetailsNew(type,binding.editMultipurpose.getText().toString());
        // apiManager.updateProfileDetails(binding.userName.getText().toString(), binding.stateTextview.getText().toString(), binding.birthday.getText().toString(), binding.aboutUser.getText().toString(), null, false);
    }


    @Override
    public void onBackPressed() {
        dismiss();

    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {


        if (ServiceCode == Constant.UPDATE_PROFILE) {
            activityNew.loadData();
          onBackPressed();
        }



    }
}
