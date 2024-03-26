package com.privatepe.host.fragments.gift;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.content.ContentValues.TAG;

import static com.privatepe.host.main.Home.callNotificationDialog;
import static com.privatepe.host.main.Home.storeBusyStatus;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.privatepe.host.Firestatus.FireBaseStatusManage;
import com.privatepe.host.Inbox.DatabaseHandler;
import com.privatepe.host.Inbox.InboxDetails;
import com.privatepe.host.Inbox.MessageBean;
import com.privatepe.host.Inbox.Messages;
import com.privatepe.host.Inbox.UserInfo;
import com.privatepe.host.Inbox.Userlist_Adapter;
import com.privatepe.host.R;
import com.privatepe.host.Zego.CallNotificationDialog;
import com.privatepe.host.activity.IncomingCallScreen;
import com.privatepe.host.activity.NotificationActivity;
import com.privatepe.host.adapter.BannerAdapter;
import com.privatepe.host.firebase.FirebaseMessageReceiver;
import com.privatepe.host.main.Home;
import com.privatepe.host.response.Banner.BannerResponse;
import com.privatepe.host.response.Banner.BannerResult;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.AppLifecycle;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.SessionManager;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMSignalingManager;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MsgFragment extends Fragment implements ApiResponseInterface {

    View rootView;
    RecyclerView recyclerViewContact;
    private DatabaseHandler db;
    private List<UserInfo> contactList = new ArrayList<>();
    private Userlist_Adapter contactAdapter;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private LinearLayoutManager layoutManager;
    private final int contactLoadLimit = 10;
    int unreadCount = 0;
    static NotificationManager call_notificationManager1;

    ProgressBar progressLoader;


    private BannerAdapter bannerAdapter;
    private Timer timer;
    private ImageView[] dots;
    ViewPager viewPager;
    LinearLayout dots_layout_lab;

    private List<BannerResult> bannerList = new ArrayList<>();
    private Home activityIs;
    private ApiManager apiManager;
    private static String inviteIdIM;
    JSONObject msgJson = null;

    public MsgFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.msg_fragment, container, false);
        apiManager = new ApiManager(getContext(), this);


        apiManager.getBannerList("2");
        Log.e("listensdaa", "Yes1 ");
        activityIs = (Home) getActivity();
        V2TIMManager.getSignalingManager().addSignalingListener(new V2TIMSignalingListener() {
            @Override
            public void onInvitationTimeout(String inviteID, List<String> inviteeList) {
                super.onInvitationTimeout(inviteID, inviteeList);
                Log.e("listensdaa", "Timeout invite" + inviteID);
                storeBusyStatus(getContext(), "Live");
                Home.clearFirst_caller_time();
                boolean AppOnForeground = isAppOnForeground(getActivity(), getActivity().getPackageName());

                if (!AppOnForeground) {
                    try {
                        call_notificationManager1.cancel(notificationIdCall);
                        if (Home.mp != null) {
                            Home.mp.stop();
                            Home.mp.release();
                        }
                    }catch (Exception e){

                    }
                }

                if (callNotificationDialog != null) {
                    callNotificationDialog.stopRingtone();
                    callNotificationDialog.dismiss();
                }else {
                    Log.e("listetesss", "incomecalls raccaaf" );

                    Intent myIntent = new Intent("KAL-CALLBROADCAST");
                    myIntent.putExtra("action", "endscreen");
                    getActivity().sendBroadcast(myIntent);
                }
              /*  if (!activityIs.isFinishing()) {
                    Home.inviteClosed.postValue(true);
                }*/

            }

            @Override
            public void onReceiveNewInvitation(String inviteID, String inviter, String groupID, List<String> inviteeList, String data) {
                super.onReceiveNewInvitation(inviteID, inviter, groupID, inviteeList, data);
                Log.e("listensdaa", "Yes invite receive " + inviteID + data);
                inviteIdIM = inviteID;
                Home.inviteClosed.postValue(false);
                try {
                    msgJson = new JSONObject(data);
                    if (msgJson.has("call_time")) {

                        String call_time_user = msgJson.getString("call_time");
                        String fcm_token_user = msgJson.getString("fcm_tokenUser");
                        if (Home.first_caller_time == 0L) {
                            FirebaseMessageReceiver.userfcmToken = fcm_token_user;

                            Log.e("listensdaa1221", "Yes1 Entered call " + call_time_user);

                            Log.e("checkHerecall", "" + msgJson.getString("caller_name"));
                            Home.first_caller_time = Long.parseLong(call_time_user);
                            Home.setFirst_caller_time(Long.parseLong(call_time_user), inviter);
                            callnotify();
                        } else if (Long.parseLong(call_time_user) > Home.first_caller_time) {
                            Log.e("listensdaa1221", "Yes2 Rejected call " + call_time_user);

                            V2TIMManager.getSignalingManager().reject(inviteIdIM,
                                    "Invite Reject",
                                    new V2TIMCallback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.e("listensdaa", "Yes1 Invite reject " + inviter);
                                            FirebaseMessageReceiver.sendChatNotification(fcm_token_user, "cc", "call_reject_offline", "cc", "cc", "A4");

                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Log.e("listensdaa", "Yes1 Invite reject error " + s);
                                            FirebaseMessageReceiver.sendChatNotification(fcm_token_user, "cc", "call_reject_offline", "cc", "cc", "A5");

                                        }
                                    }
                            );


                        }

                    } else {
                        Log.e("listensdaa1221", "Yes3 No callTimeUser ");

                        callnotify();

                    }

                } catch (JSONException e) {
                    //throw new RuntimeException(e);
                }

            }

            @Override
            public void onInvitationCancelled(String inviteID, String inviter, String data) {
                super.onInvitationCancelled(inviteID, inviter, data);
                Log.e("listensdaa", "Yes Cancelled " + inviteID);
                if (Objects.equals(Home.first_caller_Id, inviter)) {
                    storeBusyStatus(getActivity(), "Live");
                    Home.clearFirst_caller_time();
                } else {
                    return;
                }
                boolean AppOnForeground = isAppOnForeground(getActivity(), getActivity().getPackageName());

                if (!AppOnForeground) {
                    call_notificationManager1.cancel(notificationIdCall);
                    if (Home.mp != null) {
                        try {
                            Home.mp.stop();
                            Home.mp.release();
                        } catch (Exception e) {

                        }
                    }
                }

                if (AppLifecycle.isCallReportActivityInFront) {
                    Intent myIntent = new Intent("KAL-CALLBROADCAST");
                    myIntent.putExtra("action", "endscreen");
                    getContext().sendBroadcast(myIntent);
                }

                if (callNotificationDialog != null) {
                    callNotificationDialog.stopRingtone();
                    callNotificationDialog.dismiss();
                }
                if (!activityIs.isFinishing()) {
                    Home.inviteClosed.postValue(true);
                }

            }
        });
        init(rootView);

        return rootView;
    }

    private void callnotify() {
        try {
            storeBusyStatus(getActivity(), "Busy");
            String caller_name = msgJson.getString("caller_name");
            String userId = msgJson.getString("userId");
            Log.e("listensdaa123", "Yes invite receive " + userId);

            String unique_id = msgJson.getString("unique_id");
            String caller_image = msgJson.getString("caller_image");
            String callerProfileId = msgJson.getString("callerProfileId");
            String callRate = msgJson.getString("callRate");
            String isFreeCall = msgJson.getString("isFreeCall");
            String totalPoints = msgJson.getString("totalPoints");
            String remainingGiftCards = msgJson.getString("remainingGiftCards");
            String freeSeconds = msgJson.getString("freeSeconds");
            // String callerProfileId = msgJson.getString("callerProfileId");
            // Log.e("callprofileid", "caller_pid => " + callerProfileId);

            Log.e("messageBulk", "caller_image => " + caller_image);
            Log.e("messageBulk", "unique_id => " + unique_id);
            Log.e("testttst", "totalPoints => " + totalPoints);
            Log.e("testttst", "callRate => " + callRate);

            long canCallTill = 0;
            if (Integer.parseInt(remainingGiftCards) > 0) {
                int newFreeSec = Integer.parseInt(freeSeconds) * 1000;
                canCallTill = newFreeSec - 2000;
            } else {
                int callRateInt = Integer.parseInt(callRate);
                long totalPointsLong = Long.parseLong(totalPoints);
                long talktime = (totalPointsLong / callRateInt) * 60 * 1000L;
                canCallTill = talktime - 2000;
            }

            String callData = getCalldata(caller_name, userId, unique_id, isFreeCall, caller_image, "video", canCallTill, "", callerProfileId);
            Log.e("calldataaa", "" + callData);
            if (AppLifecycle.isCallReportActivityInFront && !AppLifecycle.AppInBackground) {
                               /* Intent myIntent = new Intent("KAL-CALLBROADCAST");
                                myIntent.putExtra("action", "callRequest");
                                myIntent.putExtra("callData", callData);
                                myIntent.putExtra("inviteIdIM", inviteIdIM);
                                getContext().sendBroadcast(myIntent);*/
                goToIncomingCallScreen(callData);
                return;
            }
            boolean AppOnForeground = isAppOnForeground(getActivity(), getActivity().getPackageName());
            if (!AppOnForeground) {
                Home.fromCallNotify = true;
                Home.callDataSet = callData;
                Home.unique_id_ser = unique_id;
                callNotification1(caller_name, "Receiving call...", callData, unique_id);

            } else {
                callNotificationDialog = new CallNotificationDialog(getContext(), callData, inviteIdIM);


            }
           /* Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("calldataaa", AppLifecycle.AppInBackground + "");
                    Log.e("calldataaa", AppLifecycle.isCallReportActivityInFront + "");

                    if (AppLifecycle.isCallReportActivityInFront && !AppLifecycle.AppInBackground) {
                               *//* Intent myIntent = new Intent("KAL-CALLBROADCAST");
                                myIntent.putExtra("action", "callRequest");
                                myIntent.putExtra("callData", callData);
                                myIntent.putExtra("inviteIdIM", inviteIdIM);
                                getContext().sendBroadcast(myIntent);*//*
                     //   goToIncomingCallScreen(callData);ṁ

                        return;
                    }


                    // Toast.makeText(getApplicationContext(),"inside handler",Toast.LENGTH_SHORT).show();

                    if (AppLifecycle.AppInBackground) {
                        //go to incoming call screen
                        Log.e("callNotifyD", "" + "inBackground");


                        //goToIncomingCallScreen(callData);
                    } else {
                        //go to incoming call dialog

                    }

                }
            });*/
        } catch (Exception e) {

        }

    }

    private void init(View view) {

        db = new DatabaseHandler(getActivity());
        recyclerViewContact = rootView.findViewById(R.id.contact_list);
        progressLoader = rootView.findViewById(R.id.loader);

        TextView title = rootView.findViewById(R.id.title);

        viewPager = rootView.findViewById(R.id.viewpager_lab);
        dots_layout_lab = rootView.findViewById(R.id.dots_layout_lab);

        title.setText("INBOX");
        if (bannerList != null) {
            Log.d("BannerList", "Not Null");

            setBannerData();
        } else {
            Log.d("BannerList", "Null");
        }

        getChatData();


    }

    private DatabaseReference rootRef;
    private boolean passMessage = false;
    private String currentUserId, receiverUserId;

    private boolean nameExists = false;
    private boolean canRecMessage = false;
    V2TIMSimpleMsgListener simpleMsgListener;

    void getChatData() {

        simpleMsgListener = new V2TIMSimpleMsgListener() {


            @Override
            public void onRecvC2CTextMessage(String msgID, V2TIMUserInfo sender, String text) {
                super.onRecvC2CTextMessage(msgID, sender, text);
                //  Log.i("traceLog", "text => " + text + " sender => " + new Gson().toJson(sender));
                Log.e("messageBulk", "fragment msgID => " + msgID + " sender => " + new Gson().toJson(sender) + " text => " + text);

                if (!canRecMessage) {
                    return;
                }

                String timestamp = System.currentTimeMillis() + "";

                try {
                    JSONObject msgJson = new JSONObject(text);
                    String type = msgJson.getString("type");
                    String messageText = "";

                 /*   if (type.equals("textVDO")) {
                        if (msgJson.has("message")) {
                            messageText = msgJson.getString("message");
                        }
                        String from = msgJson.getString("from");
                        String fromName = msgJson.getString("fromName");
                        String fromImage = msgJson.getString("fromImage");

                        Intent myIntent = new Intent("GIFT-USER-INPUT");
                        myIntent.putExtra("message", messageText);
                        myIntent.putExtra("type", type);
                        myIntent.putExtra("from", from);
                        myIntent.putExtra("fromName", fromName);
                        myIntent.putExtra("fromImage", fromImage);
                        getActivity().sendBroadcast(myIntent);
                        return;
                    }
                    if (type.equals("giftSend")) {
                        Log.e("chdsksaa", msgJson.toString());
                        Intent myIntent = new Intent("GIFT-USER-INPUT");
                        myIntent.putExtra("GiftPosition", msgJson.getString("GiftPosition"));
                        myIntent.putExtra("type", "giftSend");
                        myIntent.putExtra("GiftImage", msgJson.getString("GiftImage"));
                        if (getActivity() != null) {
                            getActivity().sendBroadcast(myIntent);
                        }


                        return;
                    }*/

                  /*  if (type.equals("callrequest")) {
                        String caller_name = msgJson.getString("caller_name");
                        String userId = msgJson.getString("userId");
                        String unique_id = msgJson.getString("unique_id");
                        String caller_image = msgJson.getString("caller_image");
                        String callRate = msgJson.getString("callRate");
                        String isFreeCall = msgJson.getString("isFreeCall");
                        String totalPoints = msgJson.getString("totalPoints");
                        String remainingGiftCards = msgJson.getString("remainingGiftCards");
                        String freeSeconds = msgJson.getString("freeSeconds");

                        Log.e("messageBulk", "caller_image => " + caller_image);
                        Log.e("messageBulk", "unique_id => " + unique_id);

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

                        String callData = getCalldata(caller_name, userId, unique_id, isFreeCall, caller_image, "video", canCallTill, "",);

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                // Toast.makeText(getApplicationContext(),"inside handler",Toast.LENGTH_SHORT).show();

                                if (AppLifecycle.AppInBackground) {
                                    //go to incoming call screen
                                    goToIncomingCallScreen(callData);
                                } else {
                                    //go to incoming call dialog
                                    //callNotificationDialog = new CallNotificationDialog(getContext(), callData, inviteIdIM);

                                }

                            }
                        });
                        return;

                    }*/

                    if (msgJson.has("message")) {
                        messageText = msgJson.getString("message");
                    }
                    String from = msgJson.getString("from");
                    String fromName = msgJson.getString("fromName");
                    String fromImage = msgJson.getString("fromImage");
                    String time_stamp = msgJson.getString("time_stamp");

                   /* if (tempTimeStamp.equals(time_stamp)){
                        return;
                    }
                    tempTimeStamp=time_stamp;*/
                    if (type.isEmpty() || messageText.isEmpty() || time_stamp.isEmpty() || fromImage.isEmpty()) {
                        return;
                    }

                    Messages message1 = new Messages();
                    message1.setFrom(from);
                    message1.setFromImage(fromImage);
                    message1.setFromName(fromName);
                    message1.setMessage(messageText);
                    message1.setType(type);
                    message1.setTime_stamp(Long.parseLong(time_stamp));

                    if (contactList.size() != 0) {
                        Log.e("checkkass", "Yes1");

                        if (!currentUserId.equals(message1.getFrom())) {
                            MessageBean messageBean = new MessageBean(message1.getFrom(), message1, false, timestamp);

                            String contactId = insertOrUpdateContact(messageBean.getMessage(), message1.getFrom(), message1.getFromName(), message1.getFromImage(), timestamp);
                            messageBean.setAccount(contactId);
                            insertChat(messageBean);
                        }
                        boolean isContactAvailable = false;
                        for (int i = 0; i < contactList.size(); i++) {
                            if (!currentUserId.equals(message1.getFrom())) {
                                Log.e("inProcess", "updateArea");
                                UserInfo contactObj = contactList.get(i);
                                if (contactObj.getUser_id().equals(message1.getFrom())) {
                                    contactObj.setUser_id(message1.getFrom());
                                    contactObj.setUser_name(message1.getFromName());
                                    contactObj.setTime(timestamp);
                                    contactObj.setUser_photo(message1.getFromImage());
                                    contactObj.setMessage(message1.getMessage());
                                    contactObj.setProfile_id(currentUserId);
                                    contactObj.setMsg_type(message1.getType());
                                    contactObj.setUnread_msg_count(String.valueOf(unreadCount));
                                    contactList.remove(i);
                                    contactList.add(0, contactObj);
                                    setAdminContactOnTop();
                                    contactAdapter.notifyDataSetChanged();
                                    isContactAvailable = true;
                                    break;
                                }
                            }
                        }

                        if (!isContactAvailable) {
                            UserInfo userInfo = new UserInfo("", message1.getFrom(), message1.getFromName(), message1.getMessage(), timestamp, message1.getFromImage(), String.valueOf(unreadCount), currentUserId, message1.getType(), "");
                            contactList.add(0, userInfo);
                            setAdminContactOnTop();
                            contactAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("checkkass", "Yes1");
                        MessageBean messageBean = new MessageBean(message1.getFrom(), message1, false, timestamp);

                        String contactId = insertOrUpdateContact(messageBean.getMessage(), message1.getFrom(),
                                message1.getFromName(), message1.getFromImage(), timestamp);
                        messageBean.setAccount(contactId);
                        insertChat(messageBean);

                        UserInfo userInfo = new UserInfo(contactId, message1.getFrom(), message1.getFromName(), message1.getMessage(), timestamp, message1.getFromImage(), String.valueOf(unreadCount), currentUserId, message1.getType(), "");

                        contactList.add(0, userInfo);
                        setAdminContactOnTop();
                        try {
                            contactAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }

                    int count = db.getTotalUnreadMsgCount(currentUserId);
                    if (getActivity() != null) {
                        ((Home) getActivity()).chatCount(count);
                    }

                    Intent myIntent = new Intent("KAL-REFRESHCHATBROADINDI");
                    myIntent.putExtra("action", "addChat");
                    myIntent.putExtra("type", type);
                    myIntent.putExtra("messageText", messageText);
                    myIntent.putExtra("from", from);
                    myIntent.putExtra("fromName", fromName);
                    myIntent.putExtra("fromImage", fromImage);
                    myIntent.putExtra("time_stamp", time_stamp);
                    requireActivity();
                    requireActivity().sendBroadcast(myIntent);
                    // apiManager.markMessageRead(currentUserId, from);

                } catch (
                        JSONException e) {
                    Log.e("checkcatcheakae","Yes 1 "+e.getMessage() );
                    // throw new RuntimeException(e);'
                }

            }

        };

        V2TIMManager.getInstance().addSimpleMsgListener(simpleMsgListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canRecMessage = true;
            }
        }, 3000);



      /*  rootRef = FirebaseDatabase.getInstance().getReference();

        currentUserId = String.valueOf(new SessionManager(getContext()).getUserId());

        rootRef.child("Messages").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String timestamp = System.currentTimeMillis() + "";
                try {
                    if (passMessage) {
                        Messages message = snapshot.getValue(Messages.class);

                        Log.e("messageDataInFrafment", new Gson().toJson(message));
                        if (message.getMessage() == null) {
                            return;
                        }
                       *//* if (message.getMessage().contains("activated")) {
                            Log.e("inFragment", "init");
                            Intent myIntent = new Intent("KAL-REFRESHCOINS");
                            myIntent.putExtra("action", "refresh");
                            getContext().sendBroadcast(myIntent);
                        }*//*

                        if (contactList.size() != 0) {
                            if (!currentUserId.equals(message.getFrom())) {
                                MessageBean messageBean = new MessageBean(message.getFrom(), message, false, timestamp);

                                String contactId = insertOrUpdateContact(messageBean.getMessage(), message.getFrom(), message.getFromName(), message.getFromImage(), timestamp);
                                messageBean.setAccount(contactId);
                                insertChat(messageBean);
                            }
                            boolean isContactAvailable = false;
                            for (int i = 0; i < contactList.size(); i++) {
                                if (!currentUserId.equals(message.getFrom())) {
                                    Log.e("inProcess", "updateArea");
                                    UserInfo contactObj = contactList.get(i);
                                    if (contactObj.getUser_id().equals(message.getFrom())) {
                                        contactObj.setUser_id(message.getFrom());
                                        contactObj.setUser_name(message.getFromName());
                                        contactObj.setTime(timestamp);
                                        contactObj.setUser_photo(message.getFromImage());
                                        contactObj.setMessage(message.getMessage());
                                        contactObj.setProfile_id(currentUserId);
                                        contactObj.setMsg_type(message.getType());
                                        contactObj.setUnread_msg_count(String.valueOf(unreadCount));
                                        contactList.remove(i);
                                        contactList.add(0, contactObj);
                                        setAdminContactOnTop();
                                        contactAdapter.notifyDataSetChanged();
                                        isContactAvailable = true;
                                        break;
                                    }
                                }
                            }

                            if (!isContactAvailable) {
                                UserInfo userInfo = new UserInfo("", message.getFrom(), message.getFromName(), message.getMessage(), timestamp, message.getFromImage(), String.valueOf(unreadCount), currentUserId, message.getType(), "");
                                contactList.add(0, userInfo);
                                setAdminContactOnTop();
                                contactAdapter.notifyDataSetChanged();
                            }
                        } else {

                            MessageBean messageBean = new MessageBean(message.getFrom(), message, false, timestamp);

                            String contactId = insertOrUpdateContact(messageBean.getMessage(), message.getFrom(),
                                    message.getFromName(), message.getFromImage(), timestamp);
                            messageBean.setAccount(contactId);
                            insertChat(messageBean);

                            UserInfo UserInfo = new UserInfo(contactId, message.getFrom(), message.getFromName(), message.getMessage(), timestamp, message.getFromImage(), String.valueOf(unreadCount), currentUserId, message.getType(), "");

                            contactList.add(0, UserInfo);
                            setAdminContactOnTop();

                        }

                        int count = db.getTotalUnreadMsgCount(currentUserId);
                        if (getActivity() != null) {
                            ((Home) getActivity()).chatCount(count);
                        }
                    } else {
                        passMessage = true;
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("inInboxFragment", "FirebaseCanceled");
            }
        });*/


    }

    private boolean isAppOnForeground(Context context, String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLifecycle.isCallReportActivityInFront = false;
        Log.e("fromnoitijadf", "Yes msg onresume");

        displayContactList();
        getActivity().registerReceiver(refreshChatBroad, new IntentFilter("SAN-REFRESHCHATBROAD"));

        int count = db.getTotalUnreadMsgCount(currentUserId);
        if (getActivity() != null) {
            ((Home) getActivity()).chatCount(count);
        }
    }

    private String getCalldata(String userName, String userId, String uniqueId, String isFreeCall, String profilePic, String callType, long canCallTill, String token, String callerProfileId) {
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
            OtherInfoWithCall.put("callerProfileId", callerProfileId);
            messageObject.put("isMessageWithCall", "yes");
            messageObject.put("CallMessageBody", OtherInfoWithCall.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String msg = messageObject.toString();
        return msg;
    }

    @Override
    public void onPause() {
        super.onPause();
        AppLifecycle.isCallReportActivityInFront = true;
        getActivity().unregisterReceiver(refreshChatBroad);
    }

    public BroadcastReceiver refreshChatBroad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("ReferChatBoard", "yes");

            String action = intent.getStringExtra("action");
            if (action.equals("refesh")) {
                displayContactList();
            }
        }
    };

    private void displayContactList() {


        try {
            String timestamp = System.currentTimeMillis() + "";
            currentUserId = String.valueOf(new SessionManager(getContext()).getUserId());
            int count = db.getTotalUnreadMsgCount(currentUserId);
            if (contactList == null) {
                contactList = new ArrayList<>();
            } else {
                contactList.clear();
            }

            //Log.e("contactList", new Gson().toJson(contactList));
            //creating the adapter object

            List<UserInfo> contactListDb = db.getAllContacts(currentUserId, 0, contactLoadLimit);
            if (contactListDb != null) {
                contactList.addAll(contactListDb);
            }

            if (contactList.size() == 0) {

                Messages message = new Messages();
                message.setFrom("1");
                message.setFromImage("https://ringlive.in/public/images/notification.png");//https://zeep.live/public/images/zeepliveofficial.png
                message.setFromName("System Message");
                message.setMessage("Welcome to Private Pe. Enjoy your trip and find your true love here!\n" +
                        "\n" +
                        "Do not reveal your personal information, or open any unknown links to avoid information theft and financial loss.");
                message.setType("text");

                MessageBean messageBean = new MessageBean(message.getFrom(), message, false, timestamp);
                String contactId = insertOrUpdateContact(messageBean.getMessage(), message.getFrom(), message.getFromName(), message.getFromImage(), timestamp);
                messageBean.setAccount(contactId);
                insertChat(messageBean);

            }
            setAdminContactOnTop();
            contactAdapter = new Userlist_Adapter(getActivity(), R.layout.user_list_item, contactList);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewContact.setLayoutManager(layoutManager);
            recyclerViewContact.setAdapter(contactAdapter);

        } catch (Exception e) {
            //
        }


    }


    private String insertOrUpdateContact(Messages message, String userId, String
            profileName, String profileImage, String timestamp) {
        String contactId = "";
        UserInfo UserInfoFromDb = db.getContactInfo(userId, currentUserId);
        Log.e("cjjadfaa", "yes2 " + userId + " " + new Gson().toJson(UserInfoFromDb));

        if (UserInfoFromDb == null) { // insert
            UserInfo UserInfo = new UserInfo();
            UserInfo.setUser_id(userId);
            UserInfo.setUser_name(profileName);
            UserInfo.setMessage(message.getMessage());
            UserInfo.setUser_photo(profileImage);
            UserInfo.setTime(timestamp);
            UserInfo.setUnread_msg_count(getUnreadMsgCount("0", userId));
            UserInfo.setProfile_id(currentUserId);
            UserInfo.setMsg_type(message.getType());
            contactId = db.addContact(UserInfo);
        } else { //update
            contactId = UserInfoFromDb.getId();
            UserInfoFromDb.setUser_name(profileName);
            UserInfoFromDb.setMessage(message.getMessage());
            UserInfoFromDb.setUser_photo(profileImage);
            UserInfoFromDb.setTime(timestamp);
            UserInfoFromDb.setUnread_msg_count(getUnreadMsgCount(UserInfoFromDb.getUnread_msg_count(), userId));
            UserInfoFromDb.setMsg_type(message.getType());
            db.updateContact(UserInfoFromDb);
        }
        return contactId;
    }

    private String getUnreadMsgCount(String unreadMsgCount, String profileId) {
        if (!TextUtils.isEmpty(unreadMsgCount)) {
            unreadCount = Integer.parseInt(unreadMsgCount);
        }
        unreadCount++;
        if (!TextUtils.isEmpty(InboxDetails.chatProfileId)) {
            if (InboxDetails.chatProfileId.equals(profileId) && isChatActivityOpen()) { //current chatting user
                unreadCount = 0;
            }
        }
        return String.valueOf(unreadCount);
    }

    private boolean isChatActivityOpen() {
        return AppLifecycle.isChatActivityInFront;
    }

    private void insertChat(MessageBean messageBean) {
        db.addChat(messageBean);
    }


    private void setAdminContactOnTop() {

        if (contactList == null || contactList.isEmpty()) return;
        UserInfo adminContactObj = null;
        for (int i = 0; i < contactList.size(); i++) {
            UserInfo contactObj = contactList.get(i);
            if (contactObj == null) continue;
            if (contactObj.getUser_name().equalsIgnoreCase("System Message")) {
                adminContactObj = contactObj;
                contactList.remove(adminContactObj);
            }
        }

        if (adminContactObj == null) {
            adminContactObj = db.getContactInfo("1", currentUserId); //official contact
        }
        if (adminContactObj != null) {
            contactList.add(0, adminContactObj);
        }
    }


    private void setBannerData() {
        try {
            bannerAdapter = new BannerAdapter(bannerList, getContext());
            viewPager.setAdapter(bannerAdapter);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    viewPager.post(new Runnable() {
                        @Override
                        public void run() {
                            if (bannerList != null && bannerList.size() > 1) {
                                viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % bannerList.size());
                            }

                        }
                    });
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 5000, 5000);
            //override createDots methods here
            createDots(0);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    createDots(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

        } catch (Exception e) {
        }
    }


    private void createDots(int current_position) {
        if (dots_layout_lab != null)
            dots_layout_lab.removeAllViews();
        dots = new ImageView[bannerList.size()];

        for (int i = 0; i < bannerList.size(); i++) {

            dots[i] = new ImageView(getContext());
            if (i == current_position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_lab_dots));

            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.inactive_lab_dots));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(6, 0, 8, 0);
            dots_layout_lab.addView(dots[i], layoutParams);


        }

    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.BANNER_LIST) {
            BannerResponse rsp = (BannerResponse) response;
            try {
                bannerList = rsp.getResult();
                bannerAdapter = new BannerAdapter(bannerList, getContext());
                viewPager.setAdapter(bannerAdapter);

            } catch (Exception e) {

            }

        }
    }

    private void goToIncomingCallScreen(String datawithCall) {
        JSONObject MessageWithCallJson = null;
        try {

            MessageWithCallJson = new JSONObject(datawithCall);
            Log.e(TAG, "goToIncomingCallScreen: " + MessageWithCallJson.toString() + "                 datawithCall :  " + datawithCall);

            if (MessageWithCallJson.get("isMessageWithCall").toString().equals("yes")) {

                JSONObject CallMessageBody = new JSONObject(MessageWithCallJson.get("CallMessageBody").toString());

                Intent incoming = new Intent(AppLifecycle.getActivity(), IncomingCallScreen.class);
                incoming.putExtra("receiver_id", CallMessageBody.get("UserId").toString());
                incoming.putExtra("username", CallMessageBody.get("UserName").toString());
                incoming.putExtra("inviteIdCall", inviteIdIM);
                incoming.putExtra("unique_id", CallMessageBody.get("UniqueId").toString());
                // incoming.putExtra("token", ZEGOTOKEN);
                incoming.putExtra("token", CallMessageBody.get("token").toString());
                incoming.putExtra("callType", CallMessageBody.get("CallType").toString());
                incoming.putExtra("is_free_call", CallMessageBody.get("IsFreeCall").toString());
                incoming.putExtra("name", CallMessageBody.get("Name").toString());
                incoming.putExtra("image", CallMessageBody.get("ProfilePicUrl").toString());
                incoming.putExtra("CallEndTime", Long.parseLong(CallMessageBody.get("CallAutoEnd").toString()));
                incoming.putExtra("callerProfileId", CallMessageBody.get("callerProfileId").toString());

                incoming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(incoming);

                //  Log.e(TAG, "goToIncomingCallScreen: " + "  Activity Started  " + Integer.parseInt(CallMessageBody.get("CallAutoEnd").toString()));
            } else {


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    String profileImage = "";


    public void callNotification1(String title, String message, String datawithCall, String invite_id1) {
        Log.e("kklive", "showNotification1: ");

        String channel_id = "CallNotifyId001";


        JSONObject MessageWithCallJson = null;
        try {
            Log.e("TAG111134", "goToIncomingCallScreen: ");
            notificationIdCall = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            Intent incoming1 = new Intent(getActivity(), Home.class);

         /*   incoming1.putExtra("callNotify","yes1");
            incoming1.putExtra("callDataIs",datawithCall);
            Log.e("callNotifyD","Yes3 "+invite_id1);

            incoming1.putExtra("unique_idbg", invite_id1);*/
            //  incoming1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

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
                PendingIntent pendingIntentAccept = PendingIntent.getActivity(getActivity(), 0, incoming1, PendingIntent.FLAG_IMMUTABLE);

                Log.e("kklive", "showNotification1:2 ");
                final int soundResId = R.raw.accept;
                Uri playSound1 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActivity().getPackageName() + "/" + R.raw.accept);

                Uri alarmSound =
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Home.mp = MediaPlayer.create(getActivity(), playSound1);
                Home.mp.start();
                NotificationActivity.mp = Home.mp;


                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                PendingIntent dismissIntent = NotificationActivity.getDismissIntent(notificationIdCall, getActivity());

                Notification builder = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    Person incomingCaller = null;
                    incomingCaller = new Person.Builder()
                            .setName(title)
                            .setImportant(true)
                            .build();
                    Log.e("callnotigyis", "Yesss");

                    builder = new Notification.Builder(getActivity(), channel_id)
                            .setSmallIcon(R.drawable.logo)
                            .setAutoCancel(true)
                            .setContentText(message)
                            .setStyle(Notification.CallStyle.forIncomingCall(incomingCaller, getCancelNotificationIntent(), pendingIntentAccept))
                            .addPerson(incomingCaller)
                            .setFullScreenIntent(pendingIntentAccept, true)
                            .setCategory(Notification.CATEGORY_CALL)
                            .setOngoing(true)
                            // .setOnlyAlertOnce(true)
                           // .setContentIntent(pendingIntentAccept)
                            .build();

                    call_notificationManager1 = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationChannel notificationChannel
                            = new NotificationChannel(
                            channel_id, "z_app",
                            IMPORTANCE_HIGH);
                    call_notificationManager1.createNotificationChannel(
                            notificationChannel);
                    //  notificationChannel.setSound(playSound,audioAttributes);
                    // notificationChannel.enableVibration(true);

                    call_notificationManager1.notify(notificationIdCall, builder);
                } else {
                    Log.e("callnotigyis", "Yesss2");


                    NotificationCompat.Builder notificationCompat = new NotificationCompat
                            .Builder(getContext(), channel_id)
                            .setSmallIcon(R.drawable.logo)
                            .setAutoCancel(true)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .addAction(R.drawable.btn_endcall, "Dismiss", getCancelNotificationIntent())
                            .addAction(R.drawable.btn_startcall, "Accept", pendingIntentAccept)
                            // .setOnlyAlertOnce(true)
                            .setContentIntent(pendingIntentAccept);

                    call_notificationManager1 = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                    call_notificationManager1.notify(notificationIdCall, notificationCompat.build());
                }

                Log.e("kklive", "showNotification1:3 ");



               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
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

    private static int notificationIdCall;

    private RemoteViews getCustomDesign(String title, String message, String profile_image) {
        Log.e("kklive", "getCustomDesign: " + "CustomNotify");
        RemoteViews remoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.notification);

        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        try {
            Bitmap bitmap = Glide.with(getActivity())
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

    private PendingIntent getCancelNotificationIntent() {
        Intent cancelIntent = new Intent(getActivity(), CancelNotification.class);

        return PendingIntent.getBroadcast(getActivity(), 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    public static class CancelNotification extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.e("jajdfasd","A1 "+intent.getIntExtra("notiId",0));
            // call_notificationManager1.cancel(notificationIdCall);
            call_notificationManager1.cancelAll();
            storeBusyStatus(context, "Live");
            Home.clearFirst_caller_time();
            if (Home.mp != null) {
                Home.mp.stop();
                Home.mp.release();
            }
            if (inviteIdIM != null) {
                V2TIMManager.getSignalingManager().reject(inviteIdIM,
                        "Invite Reject",
                        new V2TIMCallback() {
                            @Override
                            public void onSuccess() {
                                Log.e("listensdaa", "Yes1 Invite reject ");

                            }

                            @Override
                            public void onError(int i, String s) {
                                Log.e("listensdaa", "Yes1 Invite reject error " + s);

                            }
                        }
                );
            }

        }

    }

}