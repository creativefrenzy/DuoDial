package com.privatepe.app.zfastdev;

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

import com.privatepe.app.R;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;


public class ZFashScreenActivity extends AppCompatActivity implements ApiResponseInterface{
    TextureView mPreview;
    private ViewStub mBottomViewStub;

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
        startTimerBroad();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpZegoStream();
            }
        }, 1000);
    }

    private void setUpZegoStream(){
    }

    private void initFU() {
        //expressEngine = ZegoExpressEngine.getEngine();


        mBottomViewStub = (ViewStub) findViewById(R.id.fu_base_bottom);
        mBottomViewStub.setInflatedId(R.id.fu_base_bottom);


        mBottomViewStub.inflate();


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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}