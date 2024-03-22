package com.privatepe.host.firebase;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.content.ContentValues.TAG;

import static com.privatepe.host.main.Home.callNotificationDialog;
import static com.privatepe.host.utils.AppLifecycle.getActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.privatepe.host.Firestatus.FireBaseStatusManage;
import com.privatepe.host.R;
import com.privatepe.host.Zego.CallNotificationDialog;
import com.privatepe.host.activity.HostList;
import com.privatepe.host.activity.IncomingCallScreen;
import com.privatepe.host.activity.NotificationActivity;
import com.privatepe.host.activity.RequestCallActivity;
import com.privatepe.host.dialogs.MessageNotificationDialog;
import com.privatepe.host.main.Home;
import com.privatepe.host.model.fcm.Data;
import com.privatepe.host.model.fcm.MyResponse;
import com.privatepe.host.model.fcm.Sender;
import com.privatepe.host.retrofit.ApiInterface;
import com.privatepe.host.retrofit.FirebaseApiClient;
import com.privatepe.host.sqlite.Chat;
import com.privatepe.host.sqlite.ChatDB;
import com.privatepe.host.sqlite.SystemDB;
import com.privatepe.host.utils.AppLifecycle;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    String account = "";
    String message = "";
    String profileName = "";
    String profileImage = "";
    private String type = "";
    private String call_id="";
    private MessageNotificationDialog messageNotiDialog;

    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived: " + remoteMessage.getData());
        try {
            if (remoteMessage.getData().size() > 0) {
                Log.e("kklive1", "Message  " + "inOffline Messages");
                Log.e(TAG, "Message data payload ChatData: " + remoteMessage.getData());
                Log.e(TAG, "onMessageReceivedrr: " + remoteMessage.getData());
                if (remoteMessage.getData().get("title").equals("offline_notification_callreject")) {
                    Log.e("checkhereforoff","Yes2 "+remoteMessage.getData().get("title")+" "+"offline_notification_callreject");

                    try {
                        Log.e("checkaaaa","Yes2 "+CallNotificationDialog.inviteIdCall +" "+ call_id);

                        if (callNotificationDialog != null) {

                            if(Objects.equals(CallNotificationDialog.inviteIdCall, remoteMessage.getData().get("account")) || Objects.equals(CallNotificationDialog.call_id, call_id)) {
                                Log.e("checkaaaa","Yes3 "+callNotificationDialog);

                                callNotificationDialog.stopRingtone();
                                callNotificationDialog.dismiss();
                                call_id="";
                                CallNotificationDialog.inviteIdCall="";
                            }
                        }
                        Log.e("checkhereforoff","Yes2 Try");
                        Home.clearFirst_caller_time();
                        storeBusyStatus(getApplicationContext(),"Live");
                        notificationManager1.cancel(notificationIdCall);
                        if (Home.mp != null) {
                            Home.mp.stop();
                            Home.mp.release();
                        }

                    } catch (Exception e) {
                        Log.e("checkhereforoff","Yes2 Catch"+e.getMessage());

                    }
                    return;
                }
                Map<String, String> data1 = remoteMessage.getData();
                JSONObject object1 = new JSONObject(data1.get("data"));

                // type = remoteMessage.getData().get("type");
                String title1 = object1.getString("title");
                // message = remoteMessage.getData().get("msg");
                String message1 = object1.getString("message");
                String timeStamp = object1.getString("timestamp");

                if (title1.equals("Reward point")) {
                    Log.e("kklive", "Message data payload: " + "inOffline Messages    " + message1);
                    try {
                        showNotification1("Weekly Reward", message1);
                        saveMessageIntoDB(message1, timeStamp);
                        Log.e("kklive", "onMessageReceived: title " + title1 + "  Messages  " + message1);
                    } catch (Exception e) {

                    }
                    Log.e("kklive", "onMessageReceived: 3332");
                    return;
                }

                Map<String, String> dataVideoDelete = remoteMessage.getData();
                JSONObject objectVideoDelete = new JSONObject(dataVideoDelete.get("data"));
                String titleVideoDelete = objectVideoDelete.getString("title");
                String messageVideoDelete = object1.getString("message");
                String timeStampVideoDelete = object1.getString("timestamp");

                if (titleVideoDelete.equals("deleteprofilevideobyadmin")) {
                    Log.e("VideoDeleteApprove", "onMessageReceived: " + "VideoDelete Message   " + messageVideoDelete);
                    showNotification1("Video Deleted", messageVideoDelete);
                    saveMessageIntoDB(messageVideoDelete, timeStampVideoDelete);
                }

                Map<String, String> dataVideoApprove = remoteMessage.getData();
                JSONObject objectVideoApprove = new JSONObject(dataVideoApprove.get("data"));
                String titleVideoApprove = objectVideoApprove.getString("title");
                String messageVideoApprove = objectVideoApprove.getString("message");
                String timeStampVideoApprove = objectVideoApprove.getString("timestamp");

                if (titleVideoApprove.equals("approvedprofilevideobyadmin")) {
                    Log.e("VideoDeleteApprove", "onMessageReceived: VideoApprove Message  " + messageVideoApprove);
                    showNotification1("Video Approved", messageVideoApprove);
                    saveMessageIntoDB(messageVideoApprove, timeStampVideoApprove);
                }

                Map<String, String> dataImageApprove = remoteMessage.getData();
                JSONObject objectImageApprove = new JSONObject(dataImageApprove.get("data"));
                String titleImageApprove = objectImageApprove.getString("title");
                String messageImageApprove = objectImageApprove.getString("message");
                String timeStampImageApprove = objectImageApprove.getString("timestamp");

                if (titleImageApprove.equals("approvedprofileimagebyadmin")) {
                    Log.e("ImageApprove", "onMessageReceived: ImageApprove Message  " + messageImageApprove);
                    showNotification1("Image Approved", messageImageApprove);
                    saveMessageIntoDB(messageImageApprove, timeStampImageApprove);
                }

                Map<String, String> dataImageReject = remoteMessage.getData();
                JSONObject objectImageReject = new JSONObject(dataImageReject.get("data"));
                String titleImageReject = objectImageApprove.getString("title");
                String messageImageReject = objectImageApprove.getString("message");
                String timeStampImageReject = objectImageApprove.getString("timestamp");

                if (titleImageReject.equals("deleteprofileimagebyadmin")) {
                    Log.e("ImageReject", "onMessageReceived: ImageReject Message  " + messageImageReject);
                    showNotification1("Image Rejected", messageImageReject);
                    saveMessageIntoDB(messageImageReject, timeStampImageReject);
                }

                Map<String, String> data = remoteMessage.getData();
                JSONObject object = new JSONObject(data.get("data"));
                String title = object.getString("title");
                Log.e(TAG, "onMessageReceived: title " + title);

                if (title.equals("Klive Team")) {
                    String message = object.getString("message");
                    String sender_id = object.getString("sender_id");
                    String profile_image = object.getString("profile_image");
                    String sender_name = object.getString("sender_name");
                    String receiver_id = object.getString("receiver_id");
                    String receiver_profile_id = object.getString("receiver_profile_id");
                    String receiver_name = object.getString("receiver_name");
                    String timestamp = object.getString("timestamp");
                    String[] date = timestamp.split("\\s+");
                    saveChatInDb(receiver_profile_id, "System Message", "", message, date[0], "", date[1], profile_image, "TEXT");
                    showNotification(title, message, profile_image);
                }

                Log.e("onMessageReceivedTest:", "logout middle 3");

                if (title.equalsIgnoreCase("logout")) {
                    Log.e(TAG, "onMessageReceived: " + "logout recieved");
                    Intent myIntent = new Intent("FirebaseReceiverBroad");
                    myIntent.putExtra("action", "logout");
                    this.sendBroadcast(myIntent);
                    //return;
                }

                Log.e("onMessageReceivedTest:", "logout middle 4");

                if (title.equalsIgnoreCase("logoutbyadmin")) {

                    Log.e(TAG, "onMessageReceived: " + "logout by admin recieved");
                    Intent myIntent = new Intent("FirebaseReceiverBroad");
                    myIntent.putExtra("action", "logoutbyadmin");
                    this.sendBroadcast(myIntent);
                    // return;
                }

                Log.e("onMessageReceivedTest:", "logout end");
            }
        } catch (Exception e) {
            Log.e("onMessageReceivedTest:", "Exception " + e.getMessage());
        }

        //This is for receive video call notification
        try {
            Log.e("Check_JKFakeCall", "onMessageReceived: ");
            if (remoteMessage.getData().size() > 0) {
                //  Log.e("TAG111134", "onMessageReceived: ");
                Map<String, String> data = remoteMessage.getData();
                JSONObject object = new JSONObject(data.get("data"));
                String title = object.getString("title");
                Log.e("Check_JKFakeCall", "onMessageReceived title : " + title);

             /*   if (title.equals("zegocall")) {
                    Log.e("TAG111134", "onMessageReceived: "+new Gson().toJson(object));
                    String token = object.getString("token_receiver");
                    Log.e("TAG111134", "onMessageReceived: token "+token);
                    String caller_name = object.getString("user_name");
                    String userId = object.optString("sender_id");
                    String unique_id = object.getString("unique_id");
                    String caller_image = object.getString("profile_image");
                    String outgoing_time = object.getString("outgoing_time");
                    String convId = object.optString("conversation_id");
                    String callRate = object.getString("call_rate");
                    String isFreeCall = object.getString("is_free_call");
                    String totalPoints = object.getString("total_point");
                    String remainingGiftCards = object.getString("rem_gift_cards");
                    String freeSeconds = object.getString("free_seconds");

                    long canCallTill = 0;
                    if (Integer.parseInt(remainingGiftCards) > 0) {
                        int newFreeSec = Integer.parseInt(freeSeconds) * 1000;
                        canCallTill = newFreeSec - 2000;
                    } else {
                        int callRateInt = Integer.parseInt(callRate);
                        long totalPointsLong = Long.parseLong(totalPoints);
                        long talktime = (totalPointsLong / callRateInt) * 1000L;
                        canCallTill = talktime - 2000;
                    }

                    String callData = getCalldata(caller_name, userId, unique_id, isFreeCall, caller_image, "video", canCallTill,token);
                    Handler handler=new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(getApplicationContext(),"inside handler",Toast.LENGTH_SHORT).show();
                            if (AppLifecycle.AppInBackground) {
                                //go to incoming call screen
                                goToIncomingCallScreen(callData);
                            } else {
                                //go to incoming call dialog
                                new CallNotificationDialog(AppLifecycle.getActivity(),callData);
                            }
                        }
                    });
                }*/
                if (title.equals("offline-call-push")) {
                    Log.e("callnotifyrespose", " call received");
                    new SessionManager(getApplicationContext()).setIsFromFirebaseCall(true);
                    long canCallTill = 0;
                    String call_time_user = object.getString("call_time_user");
                    if(Home.first_caller_time==0L){
                        Home.first_caller_time= Long.parseLong(call_time_user);
                        Home.setFirst_caller_time(Long.parseLong(call_time_user),"inviter");
                        storeBusyStatus(getApplicationContext(),"Busy");
                    String caller_name = object.getString("sender_name");
                    String userId = object.getString("receiver_id");
                    String unique_id = object.getString("unique_id");
                    String caller_image = object.getString("sender_profile_image");
                    String sender_profile_id= object.getString("sender_profile_id");
                    String callRate = object.getString("call_price");
                         call_id = object.getString("call_id");

                        String totalPoints = object.getString("total_point");
                     invite_id = object.getString("invite_id");
                    userfcmToken=object.getString("sender_device_token");

                    int callRateInt = Integer.parseInt(callRate);
                    long totalPointsLong = Long.parseLong(totalPoints);
                    long talktime = (totalPointsLong / callRateInt) * 60 * 1000L;
                    canCallTill = talktime - 2000;
                    Log.e("dhajkfandfas", " " + talktime + " " + canCallTill);
                    String callDataIs = getCalldata(caller_name, userId, invite_id, "false", caller_image, "video", canCallTill, "",sender_profile_id);
                    callNotification1(caller_name, "Receiving call...", callDataIs, invite_id);
                    }else if(Long.parseLong(call_time_user)>Home.first_caller_time){

                        FirebaseMessageReceiver.sendChatNotification(object.getString("sender_device_token"), "cc","call_reject_offline","cc","cc","A2");
                        return;
                    }
                  /*  String caller_name = object.getString("user_name");
                    String userId = object.getString("sender_id");
                    String profileID = object.getString("sender_profile_id");
//                    String unique_id = object.getString("unique_id");
                    String caller_image = object.getString("profile_image");
                    String callRate = object.getString("call_rate");
                    String callPrice = object.getString("call_price");
                    String callData = getFakeCalldata(caller_name, userId, profileID, caller_image, "video", callRate, callPrice);
                    if (new SessionManager(this).getUserLoginCompleted()) {
                        getFakeCall(callData);
                    }*/
                }
                if (title.equals("fakecall")) {
                    String caller_name = object.getString("user_name");
                    String userId = object.getString("sender_id");
                    String profileID = object.getString("sender_profile_id");
//                    String unique_id = object.getString("unique_id");
                    String caller_image = object.getString("profile_image");
                    String callRate = object.getString("call_rate");
                    String callPrice = object.getString("call_price");
                    String callData = getFakeCalldata(caller_name, userId, profileID, caller_image, "video", callRate, callPrice);
                    if (new SessionManager(this).getUserLoginCompleted()) {
                        getFakeCall(callData);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Check_JKFakeCall", "onMessageReceived Catch : " + e.getMessage());
        }
    }
    static String invite_id;
    public static String userfcmToken;

    private void getFakeCall(String fakeCallData) {
        Log.e("Check_JKFakeCall", "getFakeCall");
        JSONObject fakeCallJson = null;
        try {
            Log.e("Check_JKFakeCall", "getFakeCall fakeCallData : " + fakeCallData);
            fakeCallJson = new JSONObject(fakeCallData);
            if (fakeCallJson.get("isMessageWithCall").toString().equals("no")) {
                JSONObject CallMessageBody = new JSONObject(fakeCallJson.get("CallMessageBody").toString());
                if (Constant.isReceivedFakeCall) {
                    Intent i = new Intent(AppLifecycle.getActivity(), RequestCallActivity.class);
                    i.putExtra("userID", "" + CallMessageBody.get("UserId"));
                    i.putExtra("receiver_id", "" + CallMessageBody.get("UserId"));
                    i.putExtra("profileID", "" + CallMessageBody.get("profileID"));
                    i.putExtra("username", "" + CallMessageBody.get("UserName"));
                    i.putExtra("callRate", "" + CallMessageBody.get("CallPrice"));
                    i.putExtra("callType", "" + CallMessageBody.get("CallType"));
                    i.putExtra("is_free_call", "true");
                    i.putExtra("name", "" + CallMessageBody.get("Name"));
                    i.putExtra("image", "" + CallMessageBody.get("ProfilePicUrl"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        } catch (Exception e) {
            Log.e("Check_JKFakeCall", "getFakeCall Catch : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getFakeCalldata(String userName, String userId, String profileID, String profilePic, String callType, String callRate, String callPrice) {
        JSONObject messageObject = new JSONObject();
        JSONObject OtherInfoWithCall = new JSONObject();
        try {
            OtherInfoWithCall.put("UserName", userName);
            OtherInfoWithCall.put("UserId", userId);
            OtherInfoWithCall.put("profileID", profileID);
            OtherInfoWithCall.put("CallRate", callRate);
            OtherInfoWithCall.put("CallPrice", callPrice);
            OtherInfoWithCall.put("Name", userName);
            OtherInfoWithCall.put("ProfilePicUrl", profilePic);
            OtherInfoWithCall.put("CallType", callType);
            messageObject.put("isMessageWithCall", "no");
            messageObject.put("CallMessageBody", OtherInfoWithCall.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = messageObject.toString();
        return msg;
    }

    private String getCalldata(String userName, String userId, String uniqueId, String isFreeCall, String profilePic, String callType, long canCallTill, String token,String sender_profile_id) {
        JSONObject messageObject = new JSONObject();
        JSONObject OtherInfoWithCall = new JSONObject();
        try {
            OtherInfoWithCall.put("UserName", userName);
            OtherInfoWithCall.put("UserId", userId);
            OtherInfoWithCall.put("UniqueId", uniqueId);
            OtherInfoWithCall.put("IsFreeCall", isFreeCall);
            OtherInfoWithCall.put("Name", userName);
            OtherInfoWithCall.put("ProfilePicUrl", profilePic);
            OtherInfoWithCall.put("CallType", callType);
            OtherInfoWithCall.put("CallAutoEnd", canCallTill);
            OtherInfoWithCall.put("token", token);
            OtherInfoWithCall.put("callerProfileId", sender_profile_id);
            OtherInfoWithCall.put("call_id", call_id);

            messageObject.put("isMessageWithCall", "yes");
            messageObject.put("CallMessageBody", OtherInfoWithCall.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String msg = messageObject.toString();
        return msg;
    }

    public void goToIncomingCallScreen(String datawithCall) {
        JSONObject MessageWithCallJson = null;
        try {
            Log.e("TAG111134", "goToIncomingCallScreen: ");

            MessageWithCallJson = new JSONObject(datawithCall);
            Log.e(TAG, "goToIncomingCallScreen: " + MessageWithCallJson.toString() + "                 datawithCall :  " + datawithCall);

            if (MessageWithCallJson.get("isMessageWithCall").toString().equals("yes")) {
                JSONObject CallMessageBody = new JSONObject(MessageWithCallJson.get("CallMessageBody").toString());
                Intent incoming = new Intent(AppLifecycle.getActivity(), IncomingCallScreen.class);
                incoming.putExtra("receiver_id", CallMessageBody.get("UserId").toString());
                incoming.putExtra("username", CallMessageBody.get("UserName").toString());
                incoming.putExtra("unique_id", CallMessageBody.get("UniqueId").toString());
                // incoming.putExtra("token", ZEGOTOKEN);
                incoming.putExtra("token", CallMessageBody.get("token").toString());
                incoming.putExtra("callType", CallMessageBody.get("CallType").toString());
                incoming.putExtra("is_free_call", CallMessageBody.get("IsFreeCall").toString());
                incoming.putExtra("name", CallMessageBody.get("Name").toString());
                incoming.putExtra("image", CallMessageBody.get("ProfilePicUrl").toString());
                incoming.putExtra("CallEndTime", Long.parseLong(CallMessageBody.get("CallAutoEnd").toString()));
                incoming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(incoming);

                //  Log.e(TAG, "goToIncomingCallScreen: " + "  Activity Started  " + Integer.parseInt(CallMessageBody.get("CallAutoEnd").toString()));
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveMessageIntoDB(String message, String timeStamp) {
        String[] date1 = timeStamp.split("\\s+");
        String[] date2 = date1[1].split(":");
        String finalDate = "";
        if (date2.length == 3) {
            finalDate = date2[0] + ":" + date2[1];
        } else {
            finalDate = date1[1];
        }

        // String timesttamp = System.currentTimeMillis() + "";
        SystemDB db = new SystemDB(getApplicationContext());
        db.addChat(new Chat("System", "System", "", message, date1[0], "", finalDate, "", 0, timeStamp, "TEXT"));
        db.setTotalSystemUnreadCount(db.getTotalSystemUnreadCount() + 1);
        Intent intent = new Intent("MSG-UPDATE");
        intent.putExtra("peerId", "System");
        intent.putExtra("msg", "receive");
        getApplicationContext().sendBroadcast(intent);
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    // Method to get the custom Design for the display of
    // notification.
    private RemoteViews getCustomDesign(String title, String message, String profile_image) {
        Log.e("kklive", "getCustomDesign: " + "CustomNotify");
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);

        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        try {
            Bitmap bitmap = Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(profile_image)
                    .centerCrop()
                    .submit(512, 512)
                    .get();

            remoteViews.setImageViewBitmap(R.id.icon, getCircleBitmap(bitmap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return remoteViews;
    }

    public void showNotification(String title, String message, String image) {
        Intent intent = new Intent(this, HostList.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(title, message, image));
        } else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(channel_id, "web_app", IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }

    private void saveChatInDb(String peerId, String name, String sentMsg, String recMsg, String date, String sentTime, String recTime, String image, String chatType) {
        ChatDB db = new ChatDB(getApplicationContext());

        String timesttamp = System.currentTimeMillis() + "";
        db.addChat(new Chat(peerId, name, "", recMsg, date, "", recTime, image, 0, timesttamp, chatType));

        Intent intent = new Intent("MSG-UPDATE");
        intent.putExtra("peerId", peerId);
        intent.putExtra("msg", "receive");
        getApplicationContext().sendBroadcast(intent);
    }

    public void showNotification1(String title, String message) {
        Log.e("kklive", "showNotification1: ");
        // Pass the intent to switch to the MainActivity
        // Assign channel ID
        // String channel_id = "notification_channel";
        String channel_id = System.currentTimeMillis() + "";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("gotoSystemInbox", "yes");
        intent.putExtra("profileName", profileName);
        intent.putExtra("user_image", profileImage);
        intent.putExtra("chatProfileId", account);
        intent.putExtra("mode", true);
        intent.putExtra("channelName", "zeeplive662730982537574");
        intent.putExtra("usercount", 0);
        intent.putExtra("unreadMsgCount", 0);
        intent.putExtra("fromNotification", true);
        // Pass the intent to PendingIntent to start the
        // next Activity
        // Log.e(TAG, "showNotification: "+profileImage);
        // Toast.makeText(getApplicationContext(),"profile "+profileImage,Toast.LENGTH_LONG).show();
        Log.e("kklive", "showNotification1:1 ");

        @SuppressLint("WrongConstant")
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK | PendingIntent.FLAG_IMMUTABLE);

        Log.e("kklive", "showNotification1:2 ");

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                // .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        Log.e("kklive", "showNotification1:3 ");

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.

        /* Intent myIntent = new Intent("KAL-REFRESHCHATBROAD");
        myIntent.putExtra("action", "refesh");
        sendBroadcast(myIntent);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Log.e("kklive", "showNotification1: " + "custom lay");
            builder = builder.setContent(getCustomDesign(title, message, profileImage));
        }
        // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
        else {
            Log.e("kklive", "showNotification1: ");
            builder = builder.setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.logo);
        }

        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "z_app",
                    IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, builder.build());
    }

    public void callNotification1(String title, String message, String datawithCall, String invite_id1) {
        Log.e("kklive", "showNotification1: ");

        String channel_id = "CallNotifyId001";
        Log.e("callNotifyD", "Yes5 firebase " + invite_id1);


        JSONObject MessageWithCallJson = null;
        try {
            Log.e("TAG111134", "goToIncomingCallScreen: ");
            notificationIdCall = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            Intent incoming1 = new Intent(this, Home.class);

            incoming1.putExtra("callNotify", "yes2");
            incoming1.putExtra("callDataIs", datawithCall);
            incoming1.putExtra("unique_idbg", invite_id1);
            MessageWithCallJson = new JSONObject(datawithCall);
            Log.e(TAG, "goToIncomingCallScreen: " + MessageWithCallJson.toString() + "                 datawithCall :  " + datawithCall);

            if (MessageWithCallJson.get("isMessageWithCall").toString().equals("yes")) {
/*
                JSONObject CallMessageBody = new JSONObject(MessageWithCallJson.get("CallMessageBody").toString());
                Intent incoming = new Intent(this, IncomingCallScreen.class);

                incoming.putExtra("receiver_id", CallMessageBody.get("UserId").toString());
                incoming.putExtra("username", CallMessageBody.get("UserName").toString());
                incoming.putExtra("unique_id", invite_id1);

                Log.e("chkckkaarid",""+CallMessageBody.get("UniqueId").toString());
                // incoming.putExtra("token", ZEGOTOKEN);
                incoming.putExtra("token", CallMessageBody.get("token").toString());
                incoming.putExtra("callType", CallMessageBody.get("CallType").toString());
                incoming.putExtra("callType", CallMessageBody.get("CallType").toString());
                incoming.putExtra("inviteIdCall",invite_id1);
              *//* incoming.putExtra("callnotify_id",notificationIdCall);
                Log.e("notifaiidd","A "+notificationIdCall);*//*



                incoming.putExtra("is_free_call", CallMessageBody.get("IsFreeCall").toString());
                incoming.putExtra("name", CallMessageBody.get("Name").toString());
                incoming.putExtra("image", CallMessageBody.get("ProfilePicUrl").toString());
                incoming.putExtra("CallEndTime", Long.parseLong(CallMessageBody.get("CallAutoEnd").toString()));*/

                //  incoming.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Log.e("kklive", "showNotification1:1 ");

                @SuppressLint("WrongConstant")
                PendingIntent pendingIntentAccept = PendingIntent.getActivity(this, 0, incoming1, Intent.FLAG_ACTIVITY_NEW_TASK | PendingIntent.FLAG_IMMUTABLE);

                Log.e("kklive", "showNotification1:2 ");
                final int soundResId = R.raw.accept;
              /*  Uri playSound1= Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://" +FirebaseMessageReceiver.this.getPackageName() + "/"+R.raw.accept);

                Uri alarmSound =
                        RingtoneManager. getDefaultUri (RingtoneManager. TYPE_NOTIFICATION );
                MediaPlayer mp = MediaPlayer. create (FirebaseMessageReceiver.this, playSound1);
                mp.start();*/
                try {
                    Uri playSound1 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.accept);

                    NotificationActivity.mp = MediaPlayer.create(FirebaseMessageReceiver.this, playSound1);
                    NotificationActivity.mp.start();
                    Home.mp = NotificationActivity.mp;
                } catch (Exception e) {
                    Log.e("kklive", "callRIngtone " + e.getMessage());

                }

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                PendingIntent dismissIntent = NotificationActivity.getDismissIntent(notificationIdCall, this);
                Notification builder = null;
                Log.e("kklive", "ABVD2 " + notificationIdCall);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    Person incomingCaller = null;
                    incomingCaller = new Person.Builder()
                            .setName(title)
                            .setImportant(true)
                            .build();
                    Log.e("callnotigyis", "Yesss");
                    Log.e("kklive", "ABVD3 " + notificationIdCall);
                 try {
                     builder = new Notification.Builder(getApplicationContext(), channel_id)
                             .setSmallIcon(R.drawable.logo)
                             .setAutoCancel(true)
                             .setContentText(message)
                             .setStyle(Notification.CallStyle.forIncomingCall(incomingCaller, getCancelNotificationIntent(), pendingIntentAccept))
                             .addPerson(incomingCaller)
                             .setFullScreenIntent(pendingIntentAccept, true)
                             .setCategory(Notification.CATEGORY_CALL)
                             .setOngoing(true)
                             // .setOnlyAlertOnce(true)
                             .setContentIntent(pendingIntentAccept)
                             .build();
                 }catch (Exception e){
                     Log.e("kklive", "catchhh" + e.getMessage());

                 }
                    notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Log.e("kklive", "ABVD1 " + notificationIdCall);

                    NotificationChannel notificationChannel
                            = new NotificationChannel(
                            channel_id, "z_app",
                            IMPORTANCE_HIGH);
                    notificationManager1.createNotificationChannel(
                            notificationChannel);
                    // notificationChannel.setSound(playSound,audioAttributes);
                    notificationChannel.enableVibration(true);

                    Log.e("kklive", "ABVD " + notificationIdCall);

                    notificationManager1.notify(notificationIdCall, builder);

                }else {
                    Log.e("kklivesss", "ABVD Yes" + notificationIdCall);

                    NotificationCompat.Builder notificationCompat = new NotificationCompat
                            .Builder(getApplicationContext(), channel_id)
                            .setSmallIcon(R.drawable.logo)
                            .setAutoCancel(true)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .addAction(R.drawable.btn_endcall,"Dismiss",getCancelNotificationIntent())
                            .addAction(R.drawable.btn_startcall,"Accept",pendingIntentAccept)
                            // .setOnlyAlertOnce(true)
                            .setContentIntent(pendingIntentAccept);

                    notificationManager1 = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel
                                = new NotificationChannel(
                                channel_id, "z_app",
                                IMPORTANCE_HIGH);
                        notificationManager1.createNotificationChannel(
                                notificationChannel);
                        // notificationChannel.setSound(playSound,audioAttributes);
                        notificationChannel.enableVibration(true);

                    }
                    notificationManager1.notify(notificationIdCall, notificationCompat.build());

                }
                Log.e("kklive", "ABVD4" + notificationIdCall);

     /*   NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.logo)
               // .setAutoCancel(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .addAction(R.drawable.btn_endcall,"REJECT",dismissIntent)
                .addAction(R.drawable.btn_startcall,"ACCEPT",pendingIntentAccept)

                // .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntentAccept);
        Log.e("kklive", "showNotification1:3 ");
*/


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Log.e("kklive", "showNotification1: " + "custom lay");
            builder = builder.setContent(getCustomDesign(title, message, profileImage));
        }
        // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
        else {
            Log.e("kklive", "showNotification1: ");
            builder = builder.setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.logo);
                 // #0


        }*/

                // Create an object of NotificationManager class to
                // notify the
                // user of events that happen in the background.
                // Check if the Android Version is greater than Oreo

            } else {

            }

        } catch (JSONException e) {
            Log.e("kklive", "showNotification1: Catch " + e);

            e.printStackTrace();
        }
    }

    static int notificationIdCall;
    static NotificationManager notificationManager1;

    private PendingIntent getCancelNotificationIntent() {
        Intent cancelIntent = new Intent(getApplicationContext(), FirebaseMessageReceiver.CancelNotification.class);

        return PendingIntent.getBroadcast(getApplicationContext(), 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    public static class CancelNotification extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.e("jajdfasd", "A1 " + intent.getIntExtra("notiId", 0));
           // notificationManager1.cancel(notificationIdCall);
            try {
                sendChatNotification(userfcmToken, "cc","call_reject_offline","cc","cc","A3");
                Log.e("Exception_GET_NOTIFICATION_LIST", "run: try");
            } catch (Exception e) {
                Log.e("Exception_GET_NOTIFICATION_LIST", "run: Exception " + e.getMessage());
            }
            notificationManager1.cancelAll();
            storeBusyStatus(context,"Live");
            Home.clearFirst_caller_time();

            if (Home.mp != null) {
                Home.mp.stop();
                Home.mp.release();
            }



        }
    }
    private static void storeBusyStatus(Context context,String status) {
        SessionManager sessionManager = new SessionManager(context);

        if(status.equalsIgnoreCase("Live")){
            sessionManager.setIsFromFirebaseCall(false);
        }

        new FireBaseStatusManage(context, sessionManager.getUserId(), sessionManager.getUserName(),
                "", "", status);
    }
    public static void sendChatNotification(String fcmToken, String profileId, String message, String profileName, String profileImage, String type) {
        Log.e("offLineDataLog", "sendChatNotification: " + "type  " + type);
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

}