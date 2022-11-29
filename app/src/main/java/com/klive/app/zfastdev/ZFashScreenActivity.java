package com.klive.app.zfastdev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.TextView;

import com.klive.app.R;
import com.klive.app.fudetector.faceunity.FURenderer;
import com.klive.app.fudetector.process.VideoFilterByProcess2;
import com.klive.app.fudetector.view.BeautyControlView;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoCustomVideoCaptureHandler;
import im.zego.zegoexpress.callback.IZegoCustomVideoProcessHandler;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.constants.ZegoViewMode;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoCustomVideoProcessConfig;
import im.zego.zegoexpress.entity.ZegoEngineProfile;

public class ZFashScreenActivity extends AppCompatActivity implements ApiResponseInterface, FURenderer.OnTrackingStatusChangedListener {
    ZegoExpressEngine expressEngine;
    TextureView mPreview;
    protected FURenderer mFURenderer;
    private ViewStub mBottomViewStub;
    private BeautyControlView mBeautyControlView;
    private ZegoVideoBufferType videoBufferType;

    IZegoCustomVideoCaptureHandler videoCaptureFromCamera;
    IZegoCustomVideoProcessHandler videoFilterByProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_zfash_screen);
        mPreview = findViewById(R.id.preview);
        initTimerBroad();
        cancelTimerBroad();
        initFU();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBeautyControlView != null) {
            mBeautyControlView.onResume();
        }
        startTimerBroad();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpZegoStream();
            }
        }, 1000);
    }

    private void setUpZegoStream(){
        ZegoEngineProfile profile = new ZegoEngineProfile();
        profile.appID = 1052832069L;
        profile.scenario = ZegoScenario.GENERAL;
        profile.application = getApplication();

        expressEngine = ZegoExpressEngine.createEngine(profile, null);

        videoBufferType = ZegoVideoBufferType.GL_TEXTURE_2D;

        videoFilterByProcess = new VideoFilterByProcess2(mFURenderer);

        ZegoCustomVideoProcessConfig zegoCustomVideoProcessConfig = new ZegoCustomVideoProcessConfig();
        zegoCustomVideoProcessConfig.bufferType = videoBufferType;
        expressEngine.enableCustomVideoProcessing(true, zegoCustomVideoProcessConfig);
        ZegoExpressEngine.getEngine().setCustomVideoProcessHandler(videoFilterByProcess);

        ZegoCanvas preCanvas = new ZegoCanvas(mPreview);
        preCanvas.viewMode = ZegoViewMode.ASPECT_FILL;
        ZegoExpressEngine.getEngine().startPreview(preCanvas);
    }

    private void initFU() {
        //expressEngine = ZegoExpressEngine.getEngine();


        mBottomViewStub = (ViewStub) findViewById(R.id.fu_base_bottom);
        mBottomViewStub.setInflatedId(R.id.fu_base_bottom);

        mFURenderer = new FURenderer
                .Builder(this)
                .maxFaces(4)
                .inputTextureType(0)
                .setOnTrackingStatusChangedListener(this)
                .build();

        mBottomViewStub.setLayoutResource(R.layout.layout_fu_beauty);
        mBottomViewStub.inflate();

        mBeautyControlView = (BeautyControlView) findViewById(R.id.fu_beauty_control);
        mBeautyControlView.setOnFUControlListener(mFURenderer);
        mBeautyControlView.setVisibility(View.GONE);



        new ApiManager(getApplicationContext()).changeOnlineStatus(1);

    }

    CountDownTimer broadPauseTimer = null;

    void startTimerBroad() {

        broadPauseTimer.start();
    }

    void cancelTimerBroad() {

        findViewById(R.id.tv_nofacedetected).setVisibility(View.GONE);
        findViewById(R.id.tv_livemsg).setVisibility(View.GONE);
        findViewById(R.id.tv_facedata).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tv_facedata)).setVisibility(View.GONE);

        if (broadPauseTimer != null)
            broadPauseTimer.cancel();
    }

    void initTimerBroad() {
        broadPauseTimer = new CountDownTimer(25000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.e("tracingEvents", "InBroad Timer=>" + millisUntilFinished / 1000);
                findViewById(R.id.tv_nofacedetected).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_livemsg).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_facedata).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.tv_facedata)).setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                new ApiManager(getApplicationContext()).changeOnlineStatus(0);
                cancelTimerBroad();
                sendCloseNoti();
                finish();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new ApiManager(getApplicationContext()).changeOnlineStatus(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelTimerBroad();
        try {
            expressEngine.setCustomVideoCaptureHandler(null);
            // 停止预览
            expressEngine.stopPreview();
            expressEngine.setEventHandler(null);
            expressEngine.destroyEngine(null);
        } catch (Exception e) {
        }
    }

    public void closeFun(View v) {
        new ApiManager(getApplicationContext()).changeOnlineStatus(0);
        sendCloseNoti();
        finish();
    }
    private  void sendCloseNoti(){
        Intent closeWorkIntent = new Intent("ClosedWork");
        closeWorkIntent.putExtra("isWorkedOn", "false");
        LocalBroadcastManager.getInstance(this).sendBroadcast(closeWorkIntent);
    }


    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

    }

    @Override
    public void onTrackingStatusChanged(int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log.e("newFace", "facedetectData" + " status = " + status);
                if (status == 0) {
                    startTimerBroad();
                } else {
                    cancelTimerBroad();
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}