package com.privatepe.app.Zego;

//import static com.privatepe.app.ZegoExpress.zim.ZimManager.busyOnCall;
import static com.privatepe.app.utils.AppLifecycle.ZEGOTOKEN;
import static com.privatepe.app.utils.AppLifecycle.getActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.app.Firestatus.FireBaseStatusManage;
import com.privatepe.app.R;
/*import com.privatepe.app.ZegoExpress.zim.CallType;
import com.privatepe.app.ZegoExpress.zim.ResultCallback;*/
/*import com.privatepe.app.ZegoExpress.zim.UserInfo;*/
/*import com.privatepe.app.ZegoExpress.zim.ZimEventListener;
import com.privatepe.app.ZegoExpress.zim.ZimManager;*/
import com.privatepe.app.activity.IncomingCallScreen;
import com.privatepe.app.databinding.CallNotificationDialogBinding;
import com.privatepe.app.utils.SessionManager;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMSignalingManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/*import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.enums.ZIMConnectionEvent;*//*
import im.zego.zim.enums.ZIMConnectionState;
import im.zego.zim.enums.ZIMErrorCode;*/

public class CallNotificationDialog extends Dialog {
private String inviteIdCall;
    private int width;
    String TAG = "CallNotificationDialog";

    private final CallNotificationDialogBinding binding;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
  //  private ZimManager zimManager;
 //   private ZimEventListener zimEventListener;
    String token, username, receiver_id, is_free_call, unique_id, callType, callerImage = "", name;
    long AUTO_END_TIME;
    int paddingW = 30;
    private DatabaseReference chatRef;

    Handler handler;
    V2TIMManager v2TIMManager;
    V2TIMSignalingManager v2TIMSignalingManager;

    public CallNotificationDialog(Context context, String callerdata,String inviteIdIM) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.call_notification_dialog, null, false);
        setContentView(binding.getRoot());
        Log.e(TAG, "CallNotificationDialog: " );
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        getWindow().setBackgroundDrawable(colorDrawable);
        getWindow().setGravity(Gravity.TOP);
        inviteIdCall=inviteIdIM;
         v2TIMManager = V2TIMManager.getInstance();
         v2TIMSignalingManager=V2TIMManager.getSignalingManager();

        handler = new Handler();

        handler.postDelayed(() -> {
                    binding.rejectCallBtn.callOnClick();
                }
                , 25000);

        // startTime=System.currentTimeMillis();

        // Log.e(TAG, "CallNotificationDialog: "+startTime);

        // width = ViewGroup.LayoutParams.WRAP_CONTENT;

        /*
        getWindow().getDecorView().setTop(0);
        getWindow().getDecorView().setLeft(0);
        getWindow().getDecorView().setRight(0);
       */

        /*
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        getWindow().setAttributes(layoutParams);
        */

        // getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,0);


        //  getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);


        //  WindowManager.LayoutParams wlp = getWindow().getAttributes();
        //  wlp.horizontalMargin=0;
        //  getWindow().setAttributes(wlp);

        /*
           WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
           layoutParams.horizontalWeight=0;
           layoutParams.horizontalMargin=0;
           layoutParams.flags=LayoutParams.FLAG_FULLSCREEN|LayoutParams.FLAG_LAYOUT_NO_LIMITS;
           getWindow().setAttributes(layoutParams);
        */

        /*
        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(Window.FEATURE_ACTION_MODE_OVERLAY);
        */

        // getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        storeBusyStatus("Busy");
        Log.e(TAG, "CallNotificationDialog: " + new Gson().toJson(callerdata));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;


        getWindow().setLayout(width - paddingW, ViewGroup.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);
        binding.shortParentLayout.setVisibility(View.VISIBLE);

        PauseTheTimer();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        },200);



        mediaPlayer = MediaPlayer.create(getContext(), R.raw.accept);
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams playbackParams = new PlaybackParams();
            playbackParams.setSpeed(0.5f);
            mediaPlayer.setPlaybackParams(playbackParams);
        }
        */

        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        init(callerdata);

    }

    private void PauseTheTimer() {
        Intent intent = new Intent("TIMER_CONTROL_BROAD");
        intent.putExtra("action", "stop");
        getContext().sendBroadcast(intent);
    }


    private void RestartTheTimer() {

        Intent intent = new Intent("TIMER_CONTROL_BROAD");
        intent.putExtra("action", "restart");
        getContext().sendBroadcast(intent);
    }


    private void storeBusyStatus(String status) {
       /* Log.e("storeBusyStatus", ": userid " + String.valueOf(new SessionManager(getContext()).getUserId()));
        SessionManager sessionManager = new SessionManager(getContext());
        chatRef = FirebaseDatabase.getInstance().getReference().child("Users");
        String uid = String.valueOf(sessionManager.getUserId());

        String name = sessionManager.getUserName();
        String fcmToken = sessionManager.getFcmToken();

        chatRef.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                Map<String, Object> map = null;

                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if (snapshot.exists()) {
                        map = (Map<String, Object>) snapshot.getValue();

                        HashMap<String, String> details = new HashMap<>();
                        details.put("uid", uid);
                        details.put("name", name);
                        details.put("status", status);
                        details.put("fcmToken", fcmToken);

                        chatRef.child(uid).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("storebusystatus", "stored");
                            }
                        });


                    }


                }


            }
        });*/
    }


    private void init(String callerdata) {

      //  ZegoZimListener();

        JSONObject MessageWithCallJson = null;

        try {
            MessageWithCallJson = new JSONObject(callerdata);

            if (MessageWithCallJson.get("isMessageWithCall").toString().equals("yes")) {
                JSONObject CallMessageBody = new JSONObject(MessageWithCallJson.get("CallMessageBody").toString());
                name = CallMessageBody.get("Name").toString();
               // token = ZEGOTOKEN;
                token = CallMessageBody.get("token").toString();
                Log.e("TAGZEGOTOKEN", "init: "+token);
                username = CallMessageBody.get("UserName").toString();


                receiver_id = CallMessageBody.get("UserId").toString();
                is_free_call = CallMessageBody.get("IsFreeCall").toString();


                Log.e("hzzzzz", "init: " + " IsFreeCall " + is_free_call);

                unique_id = CallMessageBody.get("UniqueId").toString();

                callType = CallMessageBody.get("CallType").toString();

                callerImage = CallMessageBody.get("ProfilePicUrl").toString();

                //  AUTO_END_TIME = Integer.parseInt(CallMessageBody.get("CallAutoEnd").toString());

                AUTO_END_TIME = Long.parseLong(CallMessageBody.get("CallAutoEnd").toString());

                // Log.e("CALL_RATE_TEST", "init: AUTO_END_TIME "+AUTO_END_TIME );

                Log.e("AUTO_CUT_TEST", "CallNotificationDialog: " + AUTO_END_TIME);

                //     Log.e(TAG, "init: username1 "+username );

                if (username.length() > 12) {
                    username = username.substring(0, 11) + "...";
                }

                binding.callerNameShort.setText(username);

                //  Log.e(TAG, "init: username1 "+username );
                //  Log.e(TAG, "init: username2 "+ binding.callerNameShort.getText().toString() );
                Glide.with(getContext()).load(callerImage).placeholder(R.drawable.default_profile).into(binding.callerProfilePic);

            } else {


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

     /* binding.shortParentLayout.setOnClickListener(v -> {

        });*/


        binding.acceptCallBtn.setOnClickListener(v -> {

            Log.e(TAG, "init: acceptCallBtn " + "start");

            try {
                if (CheckPermission()) {
                    Log.e(TAG, "init: acceptCallBtn " + " CheckPermission");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           // acceptCall();
                            if (callType.equals("video")) {
                                if(inviteIdCall!=null){
                                v2TIMSignalingManager.accept(inviteIdCall,
                                        "Invite Accept",
                                        new V2TIMCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Log.e("listensdaa","Yes1 Invite accept ");

                                            }

                                            @Override
                                            public void onError(int i, String s) {
                                                Log.e("listensdaa","Yes1 Invite accept error "+s);

                                            }
                                        }
                                );}
                                Intent intent = new Intent(getContext(), VideoChatZegoActivity.class);
                                intent.putExtra("token", token);
                                intent.putExtra("username", username);
                                intent.putExtra("receiver_id", receiver_id);
                                intent.putExtra("is_free_call", is_free_call);
                                intent.putExtra("unique_id", unique_id);
                                intent.putExtra("callType", callType);
                                intent.putExtra("name", name);
                                intent.putExtra("image", callerImage);
                                intent.putExtra("CallEndTime", AUTO_END_TIME);
                                getContext().startActivity(intent);
                                Log.e(TAG, "acceptCall: " + "Accepted");
                                Log.e(TAG, "onCallInvitationReceived: receiver id " + receiver_id);
                            }

                            stopRingtone();
                            if (handler != null) {
                                handler.removeCallbacksAndMessages(null);
                            }
                            DismissThisDialog();
                            Log.e(TAG, "init: acceptCallBtn " + "in handler");
                        }
                    }, 500);
                } else {

                }
            } catch (Exception e) {
                Log.e(TAG, "init: Exception acceptCallBtn " + e.getMessage());

            }


        });


        binding.rejectCallBtn.setOnClickListener(v -> {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(inviteIdCall!=null){
                    v2TIMSignalingManager.reject(inviteIdCall,
                            "Invite Reject",
                            new V2TIMCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.e("listensdaa","Yes1 Invite reject "+receiver_id);

                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.e("listensdaa","Yes1 Invite reject error "+receiver_id+s);

                                }
                            }
                    );}
                    SessionManager sessionManager = new SessionManager(getContext());
                    new FireBaseStatusManage(getActivity(), sessionManager.getUserId(), sessionManager.getUserName(), "", "", "Live");
                  //  rejectCall();
                  //  busyOnCall = false;
                    stopRingtone();
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    DismissThisDialog();
                }
            }, 500);


        });


    }

    private void DismissThisDialog() {

        Log.e(TAG, "DismissThisDialog: called ");
        RestartTheTimer();
      //  zimManager.removeListener(zimEventListener);

        try {
            if (this != null) {
                dismiss();
            }
        } catch (Exception e) {
            Log.e(TAG, "DismissThisDialog: Exception " + e.getMessage());
        }

    }


    private void acceptCall() {
        //go to videochat activity
        //  busyOnCall=false;

     /*   Log.e("AUTO_CUT_TEST", "CallNotificationDialog: " + AUTO_END_TIME);

        ZimManager.sharedInstance().callAccept(new ResultCallback() {
            @Override
            public void onZimCallback(ZIMErrorCode errorCode, String errMsg) {

                if (errorCode == ZIMErrorCode.SUCCESS) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (callType.equals("video")) {
                                Intent intent = new Intent(getContext(), VideoChatZegoActivity.class);
                                intent.putExtra("token", token);
                                intent.putExtra("username", username);
                                intent.putExtra("receiver_id", receiver_id);
                                intent.putExtra("is_free_call", is_free_call);
                                intent.putExtra("unique_id", unique_id);
                                intent.putExtra("callType", callType);
                                intent.putExtra("name", name);
                                intent.putExtra("image", callerImage);
                                intent.putExtra("CallEndTime", AUTO_END_TIME);
                                getContext().startActivity(intent);
                                Log.e(TAG, "acceptCall: " + "Accepted");
                                Log.e(TAG, "onCallInvitationReceived: receiver id " + receiver_id);
                            }

                        }
                    }, 100);

                } else {

                    Log.e(TAG, "onZimCallback: ErrorCode: " + errorCode);
                }

            }
        });*/


    }


    private boolean CheckPermission() {

        final boolean[] isPermissionGranted = new boolean[1];

        String[] permissions;


        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
            };
            Log.e("PermissionArray", "CheckPermission: CallNotificationDialog Permission for android 13");
        } else {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            Log.e("PermissionArray", "CheckPermission: CallNotificationDialog Permission for below android 13");
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
                    Toast.makeText(getContext(), "To use this feature Camera and Audio permissions are must.You need to allow the permissions", Toast.LENGTH_SHORT).show();
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


  /*  private void rejectCall() {
        SessionManager sessionManager = new SessionManager(getContext());
        new FireBaseStatusManage(getActivity(), sessionManager.getUserId(), sessionManager.getUserName(),
                "", "", "Live");
        storeBusyStatus("Online");
        busyOnCall = false;
        //reject the call
        Log.e(TAG, "rejectCall: " + "RejectCall");

        ZimManager.sharedInstance().callReject(new ResultCallback() {
            @Override
            public void onZimCallback(ZIMErrorCode errorCode, String errMsg) {

                if (errorCode == ZIMErrorCode.SUCCESS) {
                    Log.e(TAG, "onZimCallback: " + "Call Rejected");
                } else {
                    Log.e(TAG, "onZimCallback: ErrorCode: " + errorCode);
                }


            }
        });

    }
*/

    private void ZegoZimListener() {
       /* zimManager = ZimManager.sharedInstance();
        zimEventListener = new ZimEventListener() {
            @Override
            public void onCallInvitationCancelled(UserInfo userInfo, CallType cancelType) {
                Log.e(TAG, "onCallInvitationCancelled: " + "Call cancelled.");

            }

            @Override
            public void onCallInvitationAccepted(UserInfo userInfo) {

            }

            @Override
            public void onCallInvitationRejected(UserInfo userInfo) {

            }

            @Override
            public void onCallInvitationTimeout() {
                Log.e(TAG, "onCallInvitationTimeout: " + "true");

               *//* stopRingtone();
                DismissThisDialog();*//*

            }

            @Override
            public void onCallInviteesAnsweredTimeout() {

            }

            @Override
            public void onReceiveCallEnded() {
                //  storeBusyStatus("Online");
                storeBusyStatus("Online");
                busyOnCall = false;
                stopRingtone();
                DismissThisDialog();
                *//*ZimManager zimManager=new ZimManager();
                zimManager.busyOnCall=false;*//*

            }

            @Override
            public void onConnectionStateChanged(ZIMConnectionState state, ZIMConnectionEvent event) {

            }

            @Override
            public void onReceiveZIMPeerMessage(ZIMMessage zimMessage, String fromUserID) {
                Log.d(TAG, "onReceiveZIMPeerMessage: ");
            }
        };
        zimManager.addListener(zimEventListener);*/
    }


    public void stopRingtone() {
        mediaPlayer.stop();
        vibrator.cancel();
    }

    private void openIncomingCallScreen(String datawithCall) {
        JSONObject MessageWithCallJson = null;
        try {
            MessageWithCallJson = new JSONObject(datawithCall);
            Log.e(TAG, "goToIncomingCallScreen: " + String.valueOf(MessageWithCallJson) + "          datawithCall :  " + datawithCall);

            if (MessageWithCallJson.get("isMessageWithCall").toString().equals("yes")) {

                JSONObject CallMessageBody = new JSONObject(MessageWithCallJson.get("CallMessageBody").toString());

                Intent incoming = new Intent(getContext(), IncomingCallScreen.class);
                incoming.putExtra("receiver_id", CallMessageBody.get("UserId").toString());
                incoming.putExtra("username", CallMessageBody.get("UserName").toString());
                incoming.putExtra("unique_id", CallMessageBody.get("UniqueId").toString());
                incoming.putExtra("token", ZEGOTOKEN);
                incoming.putExtra("callType", CallMessageBody.get("CallType").toString());
                incoming.putExtra("is_free_call", CallMessageBody.get("IsFreeCall").toString());
                incoming.putExtra("name", CallMessageBody.get("Name").toString());
                incoming.putExtra("image", CallMessageBody.get("ProfilePicUrl").toString());
                incoming.putExtra("CallEndTime", Integer.parseInt(CallMessageBody.get("CallAutoEnd").toString()));
                // incoming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(incoming);

                Log.e(TAG, "goToIncomingCallScreen: " + "  Activity Started  " + Integer.parseInt(CallMessageBody.get("CallAutoEnd").toString()));


            } else {


            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "openIncomingCallScreen: " + e.getMessage());
        }


    }


    @Override
    public void onBackPressed() {


    }


}
