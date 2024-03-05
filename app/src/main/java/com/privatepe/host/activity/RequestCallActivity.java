package com.privatepe.host.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.host.R;
import com.privatepe.host.Zego.VideoChatZegoActivityMet;
import com.privatepe.host.databinding.ActivityRequestCallBinding;
import com.privatepe.host.dialogs.InsufficientCoins;
import com.privatepe.host.response.metend.GenerateCallResponce.GenerateCallResponce;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.SessionManager;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSignalingManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class RequestCallActivity extends BaseActivity implements View.OnClickListener, ApiResponseInterface {

    private ActivityRequestCallBinding binding;
    private String userID, profileID, receiverID, name, image;
    private String callRate;
    private String token, is_free_call, unique_id, callType, callTime;
    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;
    private boolean isFreeCall = false;
    private DatabaseReference chatRef;

    ApiManager apiManager;
    private InsufficientCoins insufficientCoins;

    private Vibrator vib;
    private MediaPlayer mp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(), true);
        binding = ActivityRequestCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constant.isReceivedFakeCall = false;
        init();
        setListner();
        setRing();
    }

    private void init() {
        userID = getIntent().getStringExtra("userID");
        profileID = getIntent().getStringExtra("profileID");
        receiverID = getIntent().getStringExtra("receiver_id");
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        callRate = getIntent().getStringExtra("callRate");
        token = getIntent().getStringExtra("token");
        is_free_call = getIntent().getStringExtra("is_free_call");
        unique_id = getIntent().getStringExtra("unique_id");
        callType = getIntent().getStringExtra("callType");
        callTime = getIntent().getStringExtra("callTime");
        initUI();
        apiManager = new ApiManager(this, this);
    }

    private void initUI() {
        binding.callerName.setText(name);
        Glide.with(RequestCallActivity.this).load(image).into(binding.callerPic);
        Glide.with(RequestCallActivity.this).load(image).into(binding.background);

        chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(profileID);
    }

    private void setListner() {
        binding.acceptCall.setOnClickListener(this);
        binding.declineCall.setOnClickListener(this);
    }

    private void setRing() {
        mp = MediaPlayer.create(this, R.raw.accept);
        mp.setLooping(true);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(500);
        mp.start();
    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.accept_call:
                if (CheckPermission()) {
                    if (new SessionManager(this).getUserWallet() >= Integer.parseInt(callRate)) {
                        apiManager.generateCallRequestZ(Integer.parseInt(profileID), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(""+callRate),
                                Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                    } else {
                        Log.e("insufficientCoinsDialog", "isSuccess: " + "insufficientCoinsDialog");
                        insufficientCoins = new InsufficientCoins(RequestCallActivity.this, 2, Integer.parseInt(callRate));
                        insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                apiManager.checkFirstTimeRechargeDone();
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Need permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.decline_call:
                stopRingtone(true);
                break;
            default:
                break;
        }
        new Handler().postDelayed(() -> {
            v.setEnabled(true);
        }, 1000);
    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        try {
            if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKENZ) {
                GenerateCallResponce rsp = (GenerateCallResponce) response;
                Log.e("checkkkk",""+profileID);
                V2TIMManager v2TIMManager = V2TIMManager.getInstance();

                long walletBalance = rsp.getResult().getPoints();
                int CallRateInt = Integer.parseInt(callRate);
                long talktime = (walletBalance / CallRateInt) * 60*1000L;
                Log.e("AUTO_CUT_TESTZ", "CallNotificationDialog: " + talktime+" "+callRate+" "+walletBalance);
                long canCallTill = talktime - 2000;
                Log.e("AUTO_CUT_TESTZ", "CallNotificationDialog: canCallTill " + canCallTill);
                String profilePic = new SessionManager(this).getUserProfilepic();
                HashMap<String, String> user = new SessionManager(this).getUserDetails();
                Intent intent = new Intent(this, VideoChatZegoActivityMet.class);
                intent.putExtra("TOKEN", "demo");
                intent.putExtra("ID", profileID);
                intent.putExtra("UID", String.valueOf(userID));
                intent.putExtra("CALL_RATE", callRate);
                intent.putExtra("UNIQUE_ID", rsp.getResult().getUnique_id());

                if (remGiftCard > 0) {
                    int newFreeSec = Integer.parseInt(freeSeconds) * 1000;
                    canCallTill = newFreeSec;
                    newFreeSec = newFreeSec - 2000;
                    intent.putExtra("AUTO_END_TIME", newFreeSec);
                    intent.putExtra("is_free_call", "true");
                    isFreeCall = true;
                    Log.e("callCheckLog", "in free section with freeSeconds =>" + freeSeconds);
                } else {
                    //AUTO_END_TIME converted to long
                    intent.putExtra("AUTO_END_TIME", canCallTill);
                    intent.putExtra("is_free_call", "false");
                    isFreeCall = false;
                }
                intent.putExtra("receiver_name", name);
                intent.putExtra("converID", "convId");
                intent.putExtra("receiver_image", image);

                JSONObject jsonResult = new JSONObject();
                try {
                    jsonResult.put("type", "callrequest");
                    jsonResult.put("caller_name", new SessionManager(this).getName());
                    jsonResult.put("userId", new SessionManager(this).getUserId());
                    jsonResult.put("unique_id", rsp.getResult().getUnique_id());
                    jsonResult.put("caller_image", new SessionManager(this).getUserProfilepic());
                    jsonResult.put("callRate", "1");
                    jsonResult.put("isFreeCall", "false");
                    jsonResult.put("totalPoints", new SessionManager(this).getUserWallet());
                    jsonResult.put("remainingGiftCards", "0");
                    jsonResult.put("freeSeconds", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg2 = jsonResult.toString();
                V2TIMSignalingManager v2TIMSignalingManager=V2TIMManager.getSignalingManager();
                String inviteId=   v2TIMSignalingManager.invite(  profileID, msg2, true, null, 20, new V2TIMCallback() {
                    @Override
                    public void onSuccess() {
                        Log.e("listensdaa","Yes11 Invitesent"+profileID);
                        startActivity(intent);
                        stopRingtone(true);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("listensdaa","Yes22 "+s);
                    }
                });
                Log.e("chdakdaf","yes "+inviteId);
                intent.putExtra("inviteId",inviteId);

                jsonResult.put("message", "Called");
                jsonResult.put("from", new SessionManager(this).getUserId());
                jsonResult.put("fromName", new SessionManager(this).getUserName());
                jsonResult.put("fromImage", new SessionManager(this).getUserProfilepic());
                jsonResult.put("time_stamp", System.currentTimeMillis());

                V2TIMManager.getInstance().sendC2CTextMessage(msg2,
                        profileID, new V2TIMValueCallback<V2TIMMessage>() {
                            @Override
                            public void onSuccess(V2TIMMessage message) {
                                // The one-to-one text message sent successfully
                                Log.e("offLineDataLog", "success to => " + profileID + " with message => " + new Gson().toJson(message));
                            }

                            @Override
                            public void onError(int code, String desc) {

                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean CheckPermission() {
        final boolean[] isPermissionGranted = new boolean[1];
        //Log.e("Check_JKFakeCall", "CheckPermission: ");
        String[] permissions;
        if (Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA};
        } else {
            permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //Log.e("Check_JKFakeCall", "onCreate: Permission for below android 13");
        }

        Dexter.withActivity(this).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                //Log.e("Check_JKFakeCall", "onPermissionsChecked: ");
                if (report.areAllPermissionsGranted()) {
                    //Log.e("Check_JKFakeCall", "all permission granted");
                    isPermissionGranted[0] = true;
                } else {
                    isPermissionGranted[0] = false;
                    Toast.makeText(RequestCallActivity.this, "To use this feature Camera and Audio permissions are must.You need to allow the permissions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                //Log.e("Check_JKFakeCall", "onPermissionRationaleShouldBeShown");
                token.continuePermissionRequest();
            }
        }).onSameThread().check();
        return isPermissionGranted[0];
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRingtone(false);
        Constant.isReceivedFakeCall = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.isReceivedFakeCall = true;
    }

    void stopRingtone(boolean isFinish) {
        try {
            mp.stop();
            vib.cancel();
            if (isFinish)
                finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}