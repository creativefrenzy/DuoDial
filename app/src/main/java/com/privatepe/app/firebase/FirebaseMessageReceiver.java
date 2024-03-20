package com.privatepe.app.firebase;

import static android.content.ContentValues.TAG;

import static com.privatepe.app.utils.AppLifecycle.getActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.privatepe.app.R;
import com.privatepe.app.activity.HostList;
import com.privatepe.app.activity.IncomingCallScreen;
import com.privatepe.app.activity.RequestCallActivity;
import com.privatepe.app.dialogs.MessageNotificationDialog;
import com.privatepe.app.main.Home;
import com.privatepe.app.sqlite.Chat;
import com.privatepe.app.sqlite.ChatDB;
import com.privatepe.app.sqlite.SystemDB;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    String account = "";
    String message = "";
    String profileName = "";
    String profileImage = "";
    private String type = "";
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

                Log.e("checkhereforoff", "Yes1 " + remoteMessage.getData().get("title") + " " + "offline_notification_callreject");

                if (remoteMessage.getData().get("title").equals("offline_notification_callreject")) {
                    Log.e("checkhereforoff", "Yes2 " + remoteMessage.getData().get("title") + " " + "offline_notification_callreject");

                    try {
                        Log.e("checkhereforoff", "Yes2 Try");

                        Intent myIntent = new Intent("FBR-ENDTHIS");
                        myIntent.putExtra("action", "call_reject");
                        getApplicationContext().sendBroadcast(myIntent);
                    } catch (Exception e) {
                        Log.e("checkhereforoff", "Yes2 Catch" + e.getMessage());

                    }
                    return;
                }

                Map<String, String> data1 = remoteMessage.getData();
                JSONObject object1 = new JSONObject(data1.get("data"));

                // type = remoteMessage.getData().get("type");
                String title1 = object1.getString("title");
                // message = remoteMessage.getData().get("msg");
                String message1 = object1.getString("message");
                Log.e("checkhereforoff", "Yes1 " + title1 + " " + "offline_notification_callreject");

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

                if (title.equals("stripe_recharge_succeeded")) {
                    Intent myIntent = new Intent("kal-showDialog");
                    myIntent.putExtra("action", "show");
                    getApplicationContext().sendBroadcast(myIntent);
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

    private String getCalldata(String userName, String userId, String uniqueId, String isFreeCall, String profilePic, String callType, long canCallTill, String token) {
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
                    new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
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
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, builder.build());
    }
}