package com.privatepe.app.Fast_screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.privatepe.app.R;
import com.privatepe.app.databinding.ActivityFastScreenNewBinding;
import com.privatepe.app.model.ProfileDetailsResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.SessionManager;


public class FastScreenNew extends BaseActivity implements ApiResponseInterface {

    ActivityFastScreenNewBinding binding;
    SessionManager sessionManager;
    private ViewStub mBottomViewStub;
    private boolean useExpressCustomCapture = false;


    boolean isProfileImage=true;
    String DemoImageUrl="https://ringlive.in/public/ProfileImages/1.jpeg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(), true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fast_screen_new);
        sessionManager = new SessionManager(this);
        initFuZego();
        binding.closeBtn.setOnClickListener(v ->   finishThisActivity(1));
      /*  binding.beautyIcon.setOnClickListener(v -> showFilterView("beauty"));
        binding.filterIcon.setOnClickListener(v -> showFilterView("filter"));

        binding.CamView.setOnClickListener(v -> hideFilterView());*/

        new ApiManager(this,this).getProfileDetails();

     /*   binding.upperCard.setOnClickListener(v -> Toast.makeText(getApplicationContext(),"Go to Poster Activity",Toast.LENGTH_SHORT).show()); */

    }

    private void finishThisActivity(int type) {
        if(type==0)
        {

        }
        else if (type==1)
        {
            Intent closeWorkIntent = new Intent("ClosedWork");
            closeWorkIntent.putExtra("isWorkedOn", "false");
            LocalBroadcastManager.getInstance(this).sendBroadcast(closeWorkIntent);

        }

        finish();

    }

    private void showFilterView(String type) {
        mBottomViewStub.setVisibility(View.VISIBLE);
        binding.goLiveBtn.setVisibility(View.GONE);
        binding.lowerLay.setVisibility(View.GONE);

       /* if (type.equals("beauty")) {
            mBeautyControlView.mBottomCheckGroup.setCheckedStateForView(R.id.beauty_radio_face_beauty,true);
           mBeautyControlView.clickViewBottomRadio(R.id.beauty_radio_face_beauty);
           mBeautyControlView.openSelectedView(R.id.beauty_radio_face_beauty);


        } else if (type.equals("filter")) {
         mBeautyControlView.mBottomCheckGroup.setCheckedStateForView(R.id.beauty_radio_filter,true);
            mBeautyControlView.clickViewBottomRadio(R.id.beauty_radio_filter);
            mBeautyControlView.openSelectedView(R.id.beauty_radio_filter);
        }*/


    }


    private void hideFilterView() {
        mBottomViewStub.setVisibility(View.GONE);
        binding.goLiveBtn.setVisibility(View.VISIBLE);
        binding.lowerLay.setVisibility(View.VISIBLE);
    }


    private void initFuZego() {
        mBottomViewStub = (ViewStub) findViewById(R.id.fu_base_bottom);
        mBottomViewStub.setInflatedId(R.id.fu_base_bottom);
        mBottomViewStub.inflate();
        mBottomViewStub.setVisibility(View.GONE);


        initSDK();
    }


    private void initSDK() {

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onResume() {
        super.onResume();


        initSDK();
    }


    @Override
    public void finish() {
        super.finish();


    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void isError(String errorCode) {
        Log.e("FastScreenNew", "isError: "+errorCode.toString() );
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;

        if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {

            System.out.println("profileImage  "+rsp.getSuccess().getProfile_images().get(0).getImage_name());

            new SessionManager(getApplicationContext()).setUserProfilepic(rsp.getSuccess().getProfile_images().get(0).getImage_name());

            if(rsp.getSuccess().getProfile_images().get(0).getImage_name().equals(DemoImageUrl))
            {
                isProfileImage=false;

                Log.e("FastScreenNew", "isSuccess: profileImage "+isProfileImage );

               // Toast.makeText(getApplicationContext()," profileImage "+isProfileImage,Toast.LENGTH_SHORT).show();
                
            }
            else {
                isProfileImage=true;
            }


            if (!isProfileImage) {
                binding.defaultPic.setVisibility(View.VISIBLE);
                binding.profilePic.setVisibility(View.GONE);
                binding.passedOrNot.setVisibility(View.GONE);
                binding.passedOrNot.setText("");
            } else {
                binding.defaultPic.setVisibility(View.GONE);
                binding.profilePic.setVisibility(View.VISIBLE);
                binding.passedOrNot.setVisibility(View.VISIBLE);

                Glide.with(getApplicationContext())
                        .load(sessionManager.getUserProfilepic())
                        .apply(new RequestOptions().centerCrop())
                        .into(binding.profilePic);
                binding.passedOrNot.setText("Passed");
            }


            binding.goLiveBtn.setOnClickListener(v -> {
                if (!isProfileImage) {
                    Toast.makeText(getApplicationContext(), "You can not go Live", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(getApplicationContext(), "You can go Live", Toast.LENGTH_SHORT).show();
                   // Log.e(TAG, "isSuccess: "+ );
                    startActivity(new Intent(FastScreenNew.this,FastScreenActivity.class));
                    finishThisActivity(0);
                }

            });



        }


    }
}