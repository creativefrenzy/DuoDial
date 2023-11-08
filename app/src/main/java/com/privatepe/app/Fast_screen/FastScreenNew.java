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
import com.privatepe.app.fudetector.capture.VideoCaptureFromCamera;
import com.privatepe.app.fudetector.capture.VideoCaptureFromCamera2;
import com.privatepe.app.fudetector.faceunity.FURenderer;
import com.privatepe.app.fudetector.faceunity.authpack;
import com.privatepe.app.fudetector.process.VideoFilterByProcess;
import com.privatepe.app.fudetector.process.VideoFilterByProcess2;
import com.privatepe.app.fudetector.view.BeautyControlView;
import com.privatepe.app.model.ProfileDetailsResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.SessionManager;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoCustomVideoCaptureHandler;
import im.zego.zegoexpress.callback.IZegoCustomVideoProcessHandler;
import im.zego.zegoexpress.constants.ZegoPublishChannel;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.constants.ZegoVideoConfigPreset;
import im.zego.zegoexpress.constants.ZegoViewMode;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoCustomVideoCaptureConfig;
import im.zego.zegoexpress.entity.ZegoCustomVideoProcessConfig;
import im.zego.zegoexpress.entity.ZegoVideoConfig;

public class FastScreenNew extends BaseActivity implements FURenderer.OnTrackingStatusChangedListener, ApiResponseInterface {

    ActivityFastScreenNewBinding binding;
    SessionManager sessionManager;
    protected FURenderer mFURenderer;
    private ViewStub mBottomViewStub;
    private BeautyControlView mBeautyControlView;
    private ZegoVideoBufferType videoBufferType;
    private boolean useExpressCustomCapture = false;

    IZegoCustomVideoCaptureHandler videoCaptureFromCamera;
    IZegoCustomVideoProcessHandler videoFilterByProcess;
    private ZegoExpressEngine engine;

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
        mBeautyControlView.hideBottomLayoutAnimator();
        mBottomViewStub.setVisibility(View.GONE);
        binding.goLiveBtn.setVisibility(View.VISIBLE);
        binding.lowerLay.setVisibility(View.VISIBLE);
    }


    private void initFuZego() {
        mBottomViewStub = (ViewStub) findViewById(R.id.fu_base_bottom);
        mBottomViewStub.setInflatedId(R.id.fu_base_bottom);
        mFURenderer = new FURenderer.Builder(this).maxFaces(4).inputTextureType(0).setOnTrackingStatusChangedListener(this).build();
        mBottomViewStub.setLayoutResource(R.layout.layout_fu_beauty);
        mBottomViewStub.inflate();
        mBeautyControlView = (BeautyControlView) findViewById(R.id.fu_beauty_control);
        mBeautyControlView.setOnFUControlListener(mFURenderer);
        videoBufferType = ZegoVideoBufferType.GL_TEXTURE_2D;
        mBottomViewStub.setVisibility(View.GONE);


        initSDK();
    }


    private void initSDK() {

        engine = ZegoExpressEngine.getEngine();
        if (useExpressCustomCapture && videoBufferType == ZegoVideoBufferType.SURFACE_TEXTURE) {
            videoCaptureFromCamera = new VideoCaptureFromCamera2((mFURenderer));
        } else if (useExpressCustomCapture && videoBufferType == ZegoVideoBufferType.RAW_DATA) {
            videoCaptureFromCamera = new VideoCaptureFromCamera(mFURenderer);
        } else if (!useExpressCustomCapture && videoBufferType == ZegoVideoBufferType.SURFACE_TEXTURE) {
            videoFilterByProcess = new VideoFilterByProcess(mFURenderer);
        } else if (!useExpressCustomCapture && videoBufferType == ZegoVideoBufferType.GL_TEXTURE_2D) {
            videoFilterByProcess = new VideoFilterByProcess2(mFURenderer);
        }
        if (useExpressCustomCapture) {
            ZegoCustomVideoCaptureConfig zegoCustomVideoCaptureConfig = new ZegoCustomVideoCaptureConfig();
            zegoCustomVideoCaptureConfig.bufferType = videoBufferType;
            engine.enableCustomVideoCapture(true, zegoCustomVideoCaptureConfig);
            engine.setCustomVideoCaptureHandler(videoCaptureFromCamera);
        } else {
            ZegoCustomVideoProcessConfig zegoCustomVideoProcessConfig = new ZegoCustomVideoProcessConfig();
            zegoCustomVideoProcessConfig.bufferType = videoBufferType;
            engine.enableCustomVideoProcessing(true, zegoCustomVideoProcessConfig);
            engine.setCustomVideoProcessHandler(videoFilterByProcess);
        }

        ZegoVideoConfig videoConfig = new ZegoVideoConfig(ZegoVideoConfigPreset.PRESET_720P);
        engine.setVideoConfig(videoConfig);

        ZegoCanvas preCanvas = new ZegoCanvas(binding.CamView);
        preCanvas.viewMode = ZegoViewMode.ASPECT_FILL;
        ZegoExpressEngine.getEngine().startPreview(preCanvas);

    }


    @Override
    public void onTrackingStatusChanged(int status) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("FastScreenNew", "onTrackingStatusChanged: trackerView  status " + status + "   ");
                if (status == 1) {
                    binding.trackerView.setVisibility(View.VISIBLE);
                } else {

                    binding.trackerView.setVisibility(View.GONE);
                }

                Log.e("FastScreenNew", "onTrackingStatusChanged: trackerView  " + status + "   " + binding.trackerView.getVisibility());
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onResume() {
        super.onResume();


        if (mBeautyControlView != null) {
            mBeautyControlView.onResume();
        }

        if (authpack.A() != null) {
         //   Log.e(TAG, "onResume: Init "+"FaceUnity " );
            FURenderer.initFURenderer(this);
        }

        initSDK();



    }


    @Override
    public void finish() {
        super.finish();

        if (videoCaptureFromCamera != null) {
            videoCaptureFromCamera.onStop(ZegoPublishChannel.MAIN);
        }
        if (videoFilterByProcess != null && videoBufferType == ZegoVideoBufferType.SURFACE_TEXTURE) {
            ((VideoFilterByProcess) videoFilterByProcess).stopAndDeAllocate();
        }
        if (videoFilterByProcess != null && videoBufferType == ZegoVideoBufferType.GL_TEXTURE_2D) {
            ((VideoFilterByProcess2) videoFilterByProcess).stopAndDeAllocate();
        }
        ZegoExpressEngine.getEngine().setCustomVideoCaptureHandler(null);
        // 停止预览
        ZegoExpressEngine.getEngine().stopPreview();

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