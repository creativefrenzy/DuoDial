package com.klive.app.Fast_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.klive.app.Firestatus.FireBaseStatusManage;
import com.klive.app.R;
import com.klive.app.fudetector.faceunity.FURenderer;
import com.klive.app.fudetector.process.VideoFilterByProcess2;
import com.klive.app.fudetector.view.BeautyControlView;
import com.klive.app.main.Home;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.utils.AppLifecycle;
import com.klive.app.utils.BaseActivity;
import com.klive.app.utils.SessionManager;

import java.util.HashMap;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoCustomVideoCaptureHandler;
import im.zego.zegoexpress.callback.IZegoCustomVideoProcessHandler;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.constants.ZegoViewMode;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoCustomVideoProcessConfig;


public class FastScreenActivity extends BaseActivity implements FURenderer.OnTrackingStatusChangedListener {

    //  ZegoExpressEngine expressEngine;
    // TextureView mPreview;
    //   protected FURenderer mFURenderer;
    //  private ViewStub mBottomViewStub;
    //   private BeautyControlView mBeautyControlView;
//    private ZegoVideoBufferType videoBufferType;

    //  IZegoCustomVideoCaptureHandler videoCaptureFromCamera;
    //IZegoCustomVideoProcessHandler videoFilterByProcess;
    private boolean isOnStopCalled = false;

    String TAG = "FastScreenActivity";
    private BroadcastReceiver controlTimerBroad;
    private boolean isTrackedFace;
    private boolean stopTracking = false;
    DatabaseReference chatRef;

    private SessionManager sessionManager;

    private TextView onOffText,tv_online_offline;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_fastscreen);
        //mPreview = findViewById(R.id.preview);
        onOffText = findViewById(R.id.onoff);
        tv_online_offline = findViewById(R.id.tv_online_offline);

        sessionManager = new SessionManager(this);

        //initTimerBroad();
        //cancelTimerBroad();
        //initFU();
        //TimerControlByCallDialog();


        tv_online_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLive) {
                    new FireBaseStatusManage(FastScreenActivity.this, sessionManager.getUserId(), sessionManager.getUserName(),
                            "", "", "Online");
                    tv_online_offline.setText("Online");
                    isLive = false;
                } else {
                    new FireBaseStatusManage(FastScreenActivity.this, sessionManager.getUserId(), sessionManager.getUserName(),
                            "", "", "Live");
                    tv_online_offline.setText("Live");
                    isLive = true;
                }
            }
        });

    }

    private boolean isLive = false;

    private void TimerControlByCallDialog() {

        controlTimerBroad = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null) {
                    if (intent.getStringExtra("action").equals("stop")) {
                        cancelTimerBroad();
                        stopTracking = true;

                    } else if (intent.getStringExtra("action").equals("restart")) {
                        stopTracking = false;

                        if (!isTrackedFace) {
                            startTimerBroad();
                        } else {
                            cancelTimerBroad();
                        }
                    }

                }

            }
        };

    }


    @Override
    protected void onResume() {
        super.onResume();
        //GetOnlineOfflineFromFirebase();

       /* if (mBeautyControlView != null) {
            mBeautyControlView.onResume();
        }*/
        if (isOnStopCalled) {
            if (AppLifecycle.isAppInBackground) {
                AppLifecycle.isAppInBackground = false;
            } else {
                // startTimerBroad();
            }
        } else {
            //startTimerBroad();
        }

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpZegoStream();
                new FireBaseStatusManage(FastScreenActivity.this, sessionManager.getUserId(), sessionManager.getUserName(),
                        "", "", "Live");
            }
        }, 1000);*/


        //registerReceiver(controlTimerBroad, new IntentFilter("TIMER_CONTROL_BROAD"));


    }


    private void setUpZegoStream() {

      /*  videoBufferType = ZegoVideoBufferType.GL_TEXTURE_2D;

        videoFilterByProcess = new VideoFilterByProcess2(mFURenderer);

        ZegoCustomVideoProcessConfig zegoCustomVideoProcessConfig = new ZegoCustomVideoProcessConfig();
        zegoCustomVideoProcessConfig.bufferType = videoBufferType;
        expressEngine.enableCustomVideoProcessing(true, zegoCustomVideoProcessConfig);
        ZegoExpressEngine.getEngine().setCustomVideoProcessHandler(videoFilterByProcess);

        ZegoCanvas preCanvas = new ZegoCanvas(mPreview);
        preCanvas.viewMode = ZegoViewMode.ASPECT_FILL;
        ZegoExpressEngine.getEngine().startPreview(preCanvas);*/

    }

    private void initFU() {
      /*  expressEngine = ZegoExpressEngine.getEngine();

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
        mBeautyControlView.setVisibility(View.GONE);*/


        //   new ApiManager(getApplicationContext()).changeOnlineStatus(1);

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
                //new ApiManager(getApplicationContext()).changeOnlineStatus(0);

                String userId = String.valueOf(sessionManager.getUserId());
                String name = sessionManager.getUserName();
                String fcmToken = sessionManager.getFcmToken();
                //updateStatusOnFirebase(userId, name, fcmToken, "Online");

                cancelTimerBroad();
                sendCloseNoti();
                finish();
            }
        };
    }


    @Override
    public void finish() {

        Log.e("FastScreenDestroy", "finish: ");
        super.finish();
    }

    @Override
    protected void onDestroy() {

        new SessionManager(this).setWorkSession(false);

        Log.e("FastScreenDestroy", "onDestroy: before");
        // new ApiManager(getApplicationContext()).changeOnlineStatus(0);

        String userId = String.valueOf(sessionManager.getUserId());
        String name = sessionManager.getUserName();
        String fcmToken = sessionManager.getFcmToken();
        //updateStatusOnFirebase(userId, name, fcmToken, "Online");


        Log.e("FastScreenDestroy", "onDestroy: after");

        Log.e(TAG, "onDestroy: ");
        Log.e("FastScreenDestroy", "onDestroy: after super onDestroy");

        cancelTimerBroad();
        //unregisterReceiver(controlTimerBroad);

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e(TAG, "onStop: ");

        isOnStopCalled = true;

        try {
            //  expressEngine.setCustomVideoCaptureHandler(null);
            // 停止预览
          /*  expressEngine.stopPreview();
            expressEngine.setEventHandler(null);*/
            //expressEngine.destroyEngine(null);
        } catch (Exception e) {
        }


    }

    public void closeFun(View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  new ApiManager(getApplicationContext()).changeOnlineStatus(0);

                String userId = String.valueOf(sessionManager.getUserId());
                String name = sessionManager.getUserName();
                String fcmToken = sessionManager.getFcmToken();
                //updateStatusOnFirebase(userId, name, fcmToken, "Online");
                sendCloseNoti();
                finish();
            }
        }, 200);


    }

    private void sendCloseNoti() {
        Intent closeWorkIntent = new Intent("ClosedWork");
        closeWorkIntent.putExtra("isWorkedOn", "false");
        LocalBroadcastManager.getInstance(this).sendBroadcast(closeWorkIntent);
    }


    @Override
    public void onTrackingStatusChanged(int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log.e("newFace", "facedetectData" + " status = " + status);

                if (!stopTracking) {

                    if (status == 0) {
                        startTimerBroad();
                        isTrackedFace = false;

                    } else {
                        cancelTimerBroad();
                        isTrackedFace = true;
                    }

                } else {
                    if (status == 0) {
                        isTrackedFace = false;
                    } else {
                        isTrackedFace = true;
                    }


                }

                Log.e(TAG, "run: isTrackedFace " + isTrackedFace);
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
