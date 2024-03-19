package com.privatepe.host.activity;

//import static com.privatepe.host.ZegoExpress.zim.ZimManager.busyOnCall;

import static com.privatepe.host.utils.AppLifecycle.getActivity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.host.Firestatus.FireBaseStatusManage;
import com.privatepe.host.IM.IMOperations;
import com.privatepe.host.R;
import com.privatepe.host.Zego.VideoChatZegoActivity;
/*import com.privatepe.host.ZegoExpress.zim.CallType;
import com.privatepe.host.ZegoExpress.zim.ResultCallback;
import com.privatepe.host.ZegoExpress.zim.UserInfo;*/

/*import com.privatepe.host.ZegoExpress.zim.ZimEventListener;*/
/*import com.privatepe.host.ZegoExpress.zim.ZimManager;*/
import com.privatepe.host.main.Home;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.SessionManager;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSignalingManager;

import java.util.List;

/*import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.enums.ZIMConnectionEvent;
import im.zego.zim.enums.ZIMConnectionState;
import im.zego.zim.enums.ZIMErrorCode;*/
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;

public class IncomingCallScreen extends BaseActivity implements View.OnClickListener {

    private Vibrator vib;
    private MediaPlayer mp;
    ImageView decline_call, accept_call;

    String token, username, receiver_id, channel_name, is_free_call, unique_id, callType, callerImage = "", name,callerProfileId;
    String status = "No";
    String userpoints, receiveraudiocallRate, receiverid;
    long AUTO_END_TIME;

//  private ZegoUserService userService;

    String TAG = "IncomingCallScreen";
    // private ZimEventListener zimEventListener;
    // private ZimManager zimManager;

    RelativeLayout parent_lay;
    private DatabaseReference chatRef;

    V2TIMManager v2TIMManager;
    V2TIMSignalingManager v2TIMSignalingManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_incoming_call_screen);

        storeBusyStatus("Busy");

        TextView caller_name = findViewById(R.id.caller_name);
        decline_call = findViewById(R.id.decline_call);
        accept_call = findViewById(R.id.accept_call);
        parent_lay = findViewById(R.id.parent_layout);

        decline_call.setOnClickListener(this);
        accept_call.setOnClickListener(this);

        name = getIntent().getStringExtra("name");
        inviteIdCall = getIntent().getStringExtra("inviteIdCall");
        Log.e("onMessageReceivedrr","invite Id 1 "+inviteIdCall);
        token = getIntent().getStringExtra("token");
        username = getIntent().getStringExtra("username");
        receiver_id = getIntent().getStringExtra("receiver_id");
        //  channel_name = getIntent().getStringExtra("channel_name");
        is_free_call = getIntent().getStringExtra("is_free_call");
        unique_id = getIntent().getStringExtra("unique_id");
        callType = getIntent().getStringExtra("callType");
        callerImage = getIntent().getStringExtra("image");
        callerProfileId = getIntent().getStringExtra("callerProfileId");
        //   AUTO_END_TIME = getIntent().getIntExtra("CallEndTime", 2000);

        AUTO_END_TIME = getIntent().getLongExtra("CallEndTime", 2000);
        try{
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            manager.cancelAll();

        }catch (Exception e){

        }
       SessionManager sessionManager = new SessionManager(getApplicationContext());
        IMOperations imOperations = new IMOperations(getApplicationContext());
        imOperations.loginIm(sessionManager.getUserId());
        Log.e("AUTO_CUT_TEST", "onCreate: IncomingCallScreen AUTO_END_TIME " + AUTO_END_TIME);
        //ZegoZimListener();
        caller_name.setText(name);

        if (callType.equals("audio")) {
            userpoints = getIntent().getStringExtra("userpoints");
            receiveraudiocallRate = getIntent().getStringExtra("receiveraudiocallRate");
            receiverid = getIntent().getStringExtra("receiverid");
        }

        if (!callerImage.equals("")) {
            Glide.with(getApplicationContext())
                    .load(callerImage)
                    .apply(new RequestOptions().centerCrop())
                    .transform(new BlurTransformation(20, 4))
                    .into((ImageView) findViewById(R.id.background));

            Glide.with(getApplicationContext())
                    .load(callerImage)
                    .apply(new RequestOptions().centerCrop())
                    .transform(new CropCircleWithBorderTransformation(6, getResources().getColor(R.color.white)))
                    .into((ImageView) findViewById(R.id.caller_pic));
        }

        //Log.e("autoPickUp", new SessionManager(getApplicationContext()).getHostAutopickup());
        if (new SessionManager(getApplicationContext()).getHostAutopickup().equals("yes")) {
         /*   Intent myIntent = new Intent("KAL-CLOSEME");
            myIntent.putExtra("action", "closeme");
            this.sendBroadcast(myIntent);*/

            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) findViewById(R.id.accept_call)).performClick();
                }
            }, 2000);*/
        }// else {
        mp = MediaPlayer.create(this, R.raw.accept);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // long[] pattern = { 0, 100, 500, 100, 500, 100, 500, 100, 500, 100, 500};
        //vib.vibrate(pattern , -1);
        vib.vibrate(500);
        mp.start();
        //  }

   /*     new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                storeBusyStatus("Live");
                stopRingtone();
            }
        }, 20000);*/
        // handler = new Handler();

        v2TIMManager = V2TIMManager.getInstance();
        v2TIMSignalingManager = V2TIMManager.getSignalingManager();

    }

    private String inviteIdCall;
    private void storeBusyStatus(String status) {
        SessionManager sessionManager = new SessionManager(this);
        chatRef = FirebaseDatabase.getInstance().getReference().child("Users");
        String uid = String.valueOf(sessionManager.getUserId());
        String name = sessionManager.getUserName();
        String fcmToken = sessionManager.getFcmToken();

        new FireBaseStatusManage(IncomingCallScreen.this, sessionManager.getUserId(), sessionManager.getUserName(),
                "", "", status);


    }

    private boolean isCallPickedUp = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept_call:
                if (CheckPermission()) {

                    // callAccept();

                    Intent intent = null;
                    Log.e("chadfasdfa",""+callType);

                    if (callType.equals("video")) {
                        isCallPickedUp = true;
                        Log.e("onMessageReceivedrr","invite Id 2 "+inviteIdCall);

                        v2TIMSignalingManager.accept(inviteIdCall,
                                "Invite Accept",
                                new V2TIMCallback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.e("listensdaa", "Yes1 Invite accept ");

                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        Log.e("listensdaa", "Yes1 Invite accept error " + s);

                                    }
                                }
                        );
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // acceptCall();
                                if (callType.equals("video")) {
                                    if (inviteIdCall != null) {
                                       /* v2TIMSignalingManager.accept(inviteIdCall,
                                                "Invite Accept",
                                                new V2TIMCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        Log.e("listensdaa", "Yes1 Invite accept ");

                                                    }

                                                    @Override
                                                    public void onError(int i, String s) {
                                                        Log.e("listensdaa", "Yes1 Invite accept error " + s);

                                                    }
                                                }
                                        );*/
                                    }

                                    Intent intent = new Intent(IncomingCallScreen.this, VideoChatZegoActivity.class);
                                    intent.putExtra("token", token);
                                    intent.putExtra("username", username);
                                    intent.putExtra("receiver_id", receiver_id);
                                    intent.putExtra("is_free_call", is_free_call);
                                    intent.putExtra("unique_id", unique_id);
                                    intent.putExtra("callType", callType);
                                    intent.putExtra("name", name);
                                    intent.putExtra("image", callerImage);
                                    intent.putExtra("CallEndTime", AUTO_END_TIME);
                                    intent.putExtra("callerProfileId", callerProfileId);
                                    startActivity(intent);
                                    Log.e(TAG, "acceptCall: " + "Accepted");
                                    Log.e(TAG, "onCallInvitationReceived: receiver id " + receiver_id);
                                }

                                stopRingtone();
                                if (handler != null) {
                                    handler.removeCallbacksAndMessages(null);
                                }
                                Log.e(TAG, "init: acceptCallBtn " + "in handler");
                            }
                        }, 500);
                        // finish();

                    }
                }
                break;
            case R.id.decline_call:
                Home.clearFirst_caller_time();
                if (inviteIdCall != null) {
                    v2TIMSignalingManager.reject(inviteIdCall,
                            "Invite Reject",
                            new V2TIMCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.e("listensdaa", "Yes1 Invite reject " + receiver_id);

                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.e("listensdaa", "Yes1 Invite reject error " + receiver_id + s);

                                }
                            }
                    );
                }
                //stopRingtone();
                // status = "Yes";

                storeBusyStatus("Live");
                // busyOnCall = false;
                // rejectCall();
               /* mp.stop();
                vib.cancel();*/
                   /* Intent i = new Intent(IncomingCallScreen.this, FastScreenActivity.class);
                    startActivity(i);*/
                stopRingtone();
                // finish();


                break;
        }
    }

    private boolean CheckPermission() {
        final boolean[] isPermissionGranted = new boolean[1];
        String[] permissions;
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
            };
            Log.e("PermissionArray", "onCreate: IncomingCallScreen Permission for android 13");
        } else {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            Log.e("PermissionArray", "onCreate: IncomingCallScreen Permission for below android 13");
        }

        Dexter.withActivity(getActivity()).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("onPermissionsChecked", "onPermissionsChecked: ");
                if (report.areAllPermissionsGranted()) {
                    Log.e("onPermissionsChecked", "all permission granted");
                    isPermissionGranted[0] = true;
                } else {
                    isPermissionGranted[0] = false;
                    Toast.makeText(IncomingCallScreen.this, "To use this feature Camera and Audio permissions are must.You need to allow the permissions", Toast.LENGTH_SHORT).show();
                    // Dexter.withActivity(InboxDetails.this).withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Log.e("onPermissionsChecked", "onPermissionRationaleShouldBeShown");
                token.continuePermissionRequest();
            }
        }).onSameThread().check();
        return isPermissionGranted[0];
    }

    Handler handler;
    void stopRingtone() {
        try {
            mp.stop();
            vib.cancel();
            finish();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(callGettingBroadcast, new IntentFilter("KAL-CALLBROADCAST"));
    }

    BroadcastReceiver callGettingBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equals("endscreen")) {
                storeBusyStatus("Live");
                stopRingtone();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(callGettingBroadcast);
        // zimManager.removeListener(zimEventListener);
        if (!isCallPickedUp) {
            storeBusyStatus("Live");
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }
}