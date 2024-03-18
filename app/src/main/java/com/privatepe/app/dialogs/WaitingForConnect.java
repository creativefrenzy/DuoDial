package com.privatepe.app.dialogs;

import static com.privatepe.app.Zego.VideoChatZegoActivityMet.inviteId;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.privatepe.app.R;
import com.privatepe.app.Zego.VideoChatZegoActivityMet;
import com.privatepe.app.databinding.DailogWaitingToConnectBinding;
import com.privatepe.app.model.fcm.Data;
import com.privatepe.app.model.fcm.MyResponse;
import com.privatepe.app.model.fcm.Sender;
import com.privatepe.app.retrofit.ApiInterface;
import com.privatepe.app.retrofit.FirebaseApiClient;
import com.privatepe.app.utils.SessionManager;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserStatus;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingForConnect extends Dialog {

    DailogWaitingToConnectBinding binding;

    public WaitingForConnect(@NonNull Context context, String image_url, String userName) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        init(image_url, userName);
    }

    void init(String image_url, String userName) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dailog_waiting_to_connect, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        try {
            binding.callerName.setText(userName);

            Glide.with(getContext())
                    .load(image_url)
                    .apply(new RequestOptions().centerCrop())
                    .transform(new CropCircleWithBorderTransformation(6, getContext().getResources().getColor(R.color.white)))
                    .into(binding.callerPic);

        } catch (Exception e) {
        }
        show();
    }

    private void sendChatNotification(String fcmToken, String profileId, String message, String profileName, String profileImage, String type) {
        Log.e("offLineDataLog", "sendChatNotification: " + "fcmtoken  " + fcmToken);
        Data data = new Data("offline_notification_callreject", profileId, message, profileName, profileImage, type);
        Sender sender = new Sender(data, fcmToken);
        Log.e("offLineDataLog", new Gson().toJson(sender));
        // Log.e("offLineDataLog", "sendChatNotification: "+sender.notification.getTitle() );
        ApiInterface apiService = FirebaseApiClient.getClient().create(ApiInterface.class);

        apiService.sendNotificationInBox(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.e("offline_notification_home", new Gson().toJson(response.body()));
                //Log.e("offline_notification", new Gson().toJson(response.message()));
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e("notificationFailour", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent=new Intent("FBR-ENDTHIS");
        myIntent.putExtra("action","end");
        getContext().sendBroadcast(myIntent);
        List<String> ids = Arrays.asList(VideoChatZegoActivityMet.reciverId);
        sendChatNotification(VideoChatZegoActivityMet.fcmToken_host, inviteId,"call_reject_offline","cc","cc","cc");

        V2TIMManager.getInstance().getUserStatus(ids, new V2TIMValueCallback<List<V2TIMUserStatus>>() {
            @Override
            public void onSuccess(List<V2TIMUserStatus> v2TIMUserStatuses) {
                // Queried the status successfully
                // Log.e("offLineDataLog", "from ID status=> " + new Gson().toJson(v2TIMUserStatuses));
                if (v2TIMUserStatuses.get(0).getStatusType() != 1) {

                    try {
                        sendChatNotification(VideoChatZegoActivityMet.fcmToken_host, inviteId,"call_reject_offline","cc","cc","cc");
                        Log.e("Exception_GET_NOTIFICATION_LIST", "run: try");
                    } catch (Exception e) {
                        Log.e("Exception_GET_NOTIFICATION_LIST", "run: Exception " + e.getMessage());
                    }
                }
            }


            @Override
            public void onError(int code, String desc) {
                // Failed to query the status
                //Log.e("offLineDataLog", "error code => " + code + " desc => " + desc);

            }
        });


    }
}