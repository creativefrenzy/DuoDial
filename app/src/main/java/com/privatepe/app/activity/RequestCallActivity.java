package com.privatepe.app.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.app.R;
import com.privatepe.app.Zego.VideoChatZegoActivityMet;
import com.privatepe.app.databinding.ActivityRequestCallBinding;
import com.privatepe.app.dialogs.InsufficientCoins;
import com.privatepe.app.response.metend.AdapterRes.UserListResponseMet;
import com.privatepe.app.response.metend.GenerateCallResponce.NewGenerateCallResponse;
import com.privatepe.app.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestCallActivity extends BaseActivity implements View.OnClickListener, ApiResponseInterface {

    private ActivityRequestCallBinding binding;
    private String userID, profileID, receiverID, name, image;
    private int callRate;
    private String token, is_free_call, unique_id, callType, callTime;
    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;
    private boolean isFreeCall = false;
    private DatabaseReference chatRef;

    ApiManager apiManager;
    private InsufficientCoins insufficientCoins;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(), true);
        binding = ActivityRequestCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListner();
    }

    private void init() {
        userID = getIntent().getStringExtra("userID");
        profileID = getIntent().getStringExtra("profileID");
        receiverID = getIntent().getStringExtra("receiver_id");
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        callRate = getIntent().getIntExtra("callRate", 0);
        token = getIntent().getStringExtra("token");
        is_free_call = getIntent().getStringExtra("is_free_call");
        unique_id = getIntent().getStringExtra("unique_id");
        callType = getIntent().getStringExtra("callType");
        callTime = getIntent().getStringExtra("callTime");
        Log.e("Check_JKData", "RequestCall init profileID : "+profileID);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_call:
                if (CheckPermission()) {
                    apiManager.getRemainingGiftCardFunction();
                } else {
                    Toast.makeText(this, "Need permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.decline_call:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {
            RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;
            try {
                try {
                    success = rsp.getSuccess();
                    remGiftCard = rsp.getResult().getRemGiftCards();
                    freeSeconds = rsp.getResult().getFreeSeconds();
                    if (remGiftCard > 0) {
                        apiManager.searchUser(profileID, "1");
                        return;
                    }
                } catch (Exception e) {
                    Log.e("HomeFragment", "isSuccess: Exception " + e.getMessage());
                }
                String walletAmount = String.valueOf(new SessionManager(this).getUserWallet());

                Log.e("Check_JKData", "isSuccess: " + "  GET_REMAINING_GIFT_CARD api called middle");
                Log.e("Check_JKData", "isSuccess: callRate " + callRate + "  totalCoins: " + new SessionManager(this).getUserWallet());

                if (new SessionManager(this).getUserWallet() >= Integer.parseInt(""+callRate)) {
                    apiManager.searchUser(profileID, "1");
                    Log.e("Check_JKData", "isSuccess: search user");
                } else {
                    Log.e("insufficientCoinsDialog", "isSuccess: " + "insufficientCoinsDialog");
                    insufficientCoins = new InsufficientCoins(this, 2, Integer.parseInt(""+callRate));
                    insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            apiManager.checkFirstTimeRechargeDone();
                        }
                    });
                }
            } catch (Exception e) {
                apiManager.searchUser(profileID, "1");
            }
        }

        if (ServiceCode == Constant.SEARCH_USER) {
            UserListResponseMet rsp = (UserListResponseMet) response;
            if (rsp != null) {
                try {
                    Log.e("Check_JKData", "in search isSuccess callType : " + callType);
                    if (callType.equals("video")) {
                        chatRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> map = null;
                                if (dataSnapshot.exists()) {
                                    map = (Map<String, Object>) dataSnapshot.getValue();
                                    Log.e("Check_JKData", "onSuccess: " + map.toString());
                                    if (map.get("status").equals("Online") || map.get("status").equals("Live")) {
                                        Log.e("Check_JKData", "onDataChange: " + map.get("status").toString()+" remGiftCard : "+remGiftCard);
                                        if (remGiftCard > 0) {
                                            apiManager.generateCallRequestZ(Integer.parseInt(profileID), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(""+callRate),
                                                    Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                        } else {
                                            apiManager.generateCallRequestZ(Integer.parseInt(profileID), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(""+callRate),
                                                    Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                        }
                                    } else if (map.get("status").equals("Busy")) {
                                        Toast.makeText(RequestCallActivity.this, "User is Busy", Toast.LENGTH_LONG).show();
                                        Log.e("Check_JKData", "onDataChange: " + map.get("status").toString());
                                    } else if (map.get("status").equals("Offline")) {
                                        Toast.makeText(RequestCallActivity.this, "User is Offline", Toast.LENGTH_LONG).show();
                                        Log.e("Check_JKData", "onDataChange: " + map.get("status").toString());
                                    }
                                } else {
                                    Log.e("Check_JKData", "onSuccess: " + "does not exist");
                                }
                            }
                        });
                    } else if (callType.equals("audio")) {
                        /*   apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                         String.valueOf(System.currentTimeMillis()));*/
                    }
                } catch (Exception e) {
                    Log.e("Check_JKData", "isSuccess: " + rsp.getResult());
                    Log.e("Check_JKData", "isSuccess: Exception " + e.getMessage());
                    Toast.makeText(RequestCallActivity.this, "User is Offline!", Toast.LENGTH_SHORT).show();
                    new SessionManager(RequestCallActivity.this).setOnlineState(0);
                }
            }
        }

        if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKENZ) {
            NewGenerateCallResponse rsp = (NewGenerateCallResponse) response;

            Log.e("Check_JKData", "isSuccess NEW_GENERATE_AGORA_TOKENZ : " + new Gson().toJson(rsp));

            int walletBalance = rsp.getResult().getTotalPoint();
            int CallRateInt = Integer.parseInt(""+callRate);
            long talktime = (walletBalance / CallRateInt) * 1000L;
            long canCallTill = talktime - 2000;
            Log.e("AUTO_CUT_TESTZ", "CallNotificationDialog: canCallTill " + canCallTill);
            String profilePic = new SessionManager(this).getUserProfilepic();
            HashMap<String, String> user = new SessionManager(this).getUserDetails();
            Intent intent = new Intent(this, VideoChatZegoActivityMet.class);
//            intent.putExtra("TOKEN", rsp.getResult().getData().getSenderChannelName().getToken().getToken());
            intent.putExtra("ID", profileID);
            intent.putExtra("UID", String.valueOf(userID));
            intent.putExtra("CALL_RATE", callRate);
            intent.putExtra("UNIQUE_ID", rsp.getResult().getUniqueId());

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
            startActivity(intent);
            finish();
            Log.e("Check_JKData", "isSuccess NEW_GENERATE_AGORA_TOKENZ : go to videoChatActivity");

            JSONObject jsonResult = new JSONObject();
            try {
                jsonResult.put("type", "callrequest");
                jsonResult.put("caller_name", new SessionManager(this).getName());
                jsonResult.put("userId",  new SessionManager(this).getUserId());
                jsonResult.put("unique_id", rsp.getResult().getUniqueId());
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
    }

    private boolean CheckPermission() {
        final boolean[] isPermissionGranted = new boolean[1];
        Log.e("CHECK_PERMISSIONS", "CheckPermission: ");
        String[] permissions;
        if (Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA};
        } else {
            permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            Log.e("ViewProfile", "onCreate: Permission for below android 13");
        }

        Dexter.withActivity(this).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("onPermissionsChecked", "onPermissionsChecked: ");
                if (report.areAllPermissionsGranted()) {
                    Log.e("onPermissionsChecked", "all permission granted");
                    isPermissionGranted[0] = true;
                } else {
                    isPermissionGranted[0] = false;
                    Toast.makeText(RequestCallActivity.this, "To use this feature Camera and Audio permissions are must.You need to allow the permissions", Toast.LENGTH_SHORT).show();
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
}