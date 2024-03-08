package com.privatepe.app.fragments.metend;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import android.os.AsyncTask;
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

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.gson.Gson;
import com.privatepe.app.Inbox.DatabaseHandler;
import com.privatepe.app.Inbox.InboxDetails;
import com.privatepe.app.Inbox.MessageBean;
import com.privatepe.app.Inbox.Messages;
import com.privatepe.app.Inbox.UserInfo;
import com.privatepe.app.Inbox.Userlist_Adapter;
import com.privatepe.app.R;
import com.privatepe.app.Zego.CallNotificationDialog;
import com.privatepe.app.activity.MainActivity;
import com.privatepe.app.adapter.BannerAdapter;
import com.privatepe.app.extras.BannerResponseNew;
import com.privatepe.app.response.Auto_Message.AutoMessageData;
import com.privatepe.app.response.Auto_Message.AutoMessageNew.AutoMessageNewResponse;
import com.privatepe.app.response.Auto_Message.AutoMessageNew.AutoMessageNewResult;
import com.privatepe.app.response.Auto_Message.AutoMessageRequest;
import com.privatepe.app.response.Auto_Message.AutoMessageResponse;
import com.privatepe.app.response.Auto_Message.Messagedetail;
import com.privatepe.app.response.Banner.BannerResult;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class InboxFragment extends Fragment implements ApiResponseInterface {
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

    ProgressBar progressLoader;


    private BannerAdapter bannerAdapter;
    private Timer timer;
    private ImageView[] dots;
    ViewPager viewPager;
    LinearLayout dots_layout_lab;

    private List<BannerResult> bannerList = new ArrayList<>();
    private ApiManager apiManager;


    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
            Log.e("chdskasa", "Yes Inbox2");

            init();
        }

        return rootView;
    }

    private void init() {

        db = new DatabaseHandler(getActivity());
        recyclerViewContact = rootView.findViewById(R.id.contact_list);
        progressLoader = rootView.findViewById(R.id.loader);


        viewPager = rootView.findViewById(R.id.viewpager_lab);
        dots_layout_lab = rootView.findViewById(R.id.dots_layout_lab);

        apiManager = new ApiManager(getContext(), this);

        apiManager.getBannerListNew("2");
        if (bannerList != null) {
            setBannerData();
        }


        initScrollListner();
        Log.e("chdskasa", "Yes Inbox1");

        recMessage();

        //getChatData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                AutoMessageRequest autoMessageRequests = new AutoMessageRequest();
                ArrayList<Integer> integerArrayList = new ArrayList<>();
                Log.e("automessageLog", "array size => " + contactList.size());

                if (contactList.size() == 1) {
                    Log.e("automessageLog", "in == 1");

                    apiManager.getOfflineMessageListDataNew(autoMessageRequests);
                } else {
                    Log.e("automessageLog", "else section");

                    if (contactList.size() <= 15) {
                        Log.e("automessageLog", "<=15");

                        for (int i = 0; i < contactList.size(); i++) {
                            integerArrayList.add(Integer.parseInt(contactList.get(i).getUser_id()));

                        }
                        // autoMessageRequests.setUserId(integerArrayList);
                        apiManager.getOfflineMessageListDataNew(autoMessageRequests);
                    }
                }
            }
        }, 5000);

        notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(
                getContext().getPackageName(),
                R.layout.notificationinapp);


    }


    private boolean passMessage = false;
    private String currentUserId, receiverUserId;
    private boolean canRecMessage = false;
    V2TIMSimpleMsgListener simpleMsgListener;

    private boolean nameExists = false;


    private void recMessage() {
        Log.e("chdskasa", "Yes Inbox");
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

                    if (type.equals("giftSend")) {
                        Log.e("chdsksaa", msgJson.toString());
                      /*  Intent myIntent = new Intent("GIFT-USER-INPUT");
                        myIntent.putExtra("GiftPosition", msgJson.getString("GiftPosition"));
                        myIntent.putExtra("type", "giftSend");

                        getActivity().sendBroadcast(myIntent);*/

                        return;
                    }


                    if (type.equals("callrequest")) {
                        String caller_name = msgJson.getString("caller_name");
                        String userId = msgJson.getString("userId");
                        String unique_id = msgJson.getString("unique_id");
                        String caller_image = msgJson.getString("caller_image");
                        String callRate = msgJson.getString("callRate");
                        String isFreeCall = msgJson.getString("isFreeCall");
                        String totalPoints = msgJson.getString("totalPoints");
                        String remainingGiftCards = msgJson.getString("remainingGiftCards");
                        String freeSeconds = msgJson.getString("freeSeconds");


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

                        String callData = getCalldata(caller_name, userId, unique_id, isFreeCall, caller_image, "video", canCallTill, "");

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                // Toast.makeText(getApplicationContext(),"inside handler",Toast.LENGTH_SHORT).show();

                                if (AppLifecycle.AppInBackground) {
                                    //go to incoming call screen
                                    // goToIncomingCallScreen(callData);
                                } else {
                                    //go to incoming call dialog
                                    try {
                                        new CallNotificationDialog(getContext(), callData, null);
                                    } catch (Exception e) {
                                    }
                                }

                            }
                        });


                        return;
                    }
                    String messageText = msgJson.getString("message");
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

                    Messages message = new Messages();
                    message.setFrom(from);
                    message.setFromImage(fromImage);
                    message.setFromName(fromName);
                    message.setMessage(messageText);

                    message.setType(type);
                    message.setTime_stamp(Long.parseLong(time_stamp));

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

                        UserInfo userInfo = new UserInfo(contactId, message.getFrom(), message.getFromName(), message.getMessage(), timestamp, message.getFromImage(), String.valueOf(unreadCount), currentUserId, message.getType(), "");

                        contactList.add(0, userInfo);
                        setAdminContactOnTop();
                        try {
                            contactAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }

                    int count = db.getTotalUnreadMsgCount(currentUserId);
                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).chatCount(String.valueOf(count));


                        Intent myIntent = new Intent("KAL-REFRESHCHATBROADINDI");
                        myIntent.putExtra("action", "addChat");
                        myIntent.putExtra("type", type);
                        myIntent.putExtra("messageText", messageText);
                        myIntent.putExtra("from", from);
                        myIntent.putExtra("fromName", fromName);
                        myIntent.putExtra("fromImage", fromImage);
                        myIntent.putExtra("time_stamp", time_stamp);
                        getActivity().sendBroadcast(myIntent);
                    }
                    apiManager.markMessageRead(currentUserId, from);

                } catch (
                        JSONException e) {
                    throw new RuntimeException(e);
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


    }

   /* void getChatData() {

        rootRef = FirebaseDatabase.getInstance().getReference();

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

                            UserInfo userInfo = new UserInfo(contactId, message.getFrom(), message.getFromName(), message.getMessage(), timestamp, message.getFromImage(), String.valueOf(unreadCount), currentUserId, message.getType(), "");

                            contactList.add(0, userInfo);
                            setAdminContactOnTop();
                            try {
                                contactAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                            }
                        }

                        int count = db.getTotalUnreadMsgCount(currentUserId);
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).chatCount(String.valueOf(count));
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
        });

       *//* rootRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        rootRef.removeValue();*//*
    }*/


    @Override
    public void onResume() {
        super.onResume();
        displayContactList();
        getActivity().registerReceiver(refreshChatBroad, new IntentFilter("SAN-REFRESHCHATBROAD"));

        int count = db.getTotalUnreadMsgCount(currentUserId);
        if (getActivity() != null) {
            ((MainActivity) getActivity()).chatCount(String.valueOf(count));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(refreshChatBroad);
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
            if (getActivity() != null) {
                ((MainActivity) getActivity()).chatCount(String.valueOf(count));
            }
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
        UserInfo userInfoFromDb = db.getContactInfo(userId, currentUserId);
        if (userInfoFromDb == null) { // insert
            UserInfo userInfo = new UserInfo();
            userInfo.setUser_id(userId);
            userInfo.setUser_name(profileName);
            userInfo.setMessage(message.getMessage());
            userInfo.setUser_photo(profileImage);
            userInfo.setTime(timestamp);
            userInfo.setUnread_msg_count(getUnreadMsgCount("0", userId));
            userInfo.setProfile_id(currentUserId);
            userInfo.setMsg_type(message.getType());
            contactId = db.addContact(userInfo);
        } else { //update
            contactId = userInfoFromDb.getId();
            userInfoFromDb.setUser_name(profileName);
            userInfoFromDb.setMessage(message.getMessage());
            userInfoFromDb.setUser_photo(profileImage);
            userInfoFromDb.setTime(timestamp);
            userInfoFromDb.setUnread_msg_count(getUnreadMsgCount(userInfoFromDb.getUnread_msg_count(), userId));
            userInfoFromDb.setMsg_type(message.getType());
            db.updateContact(userInfoFromDb);
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

    private void initScrollListner() {
        recyclerViewContact.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();


                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                            loading = false;
                            //Log.e("onScrolledRV", "size=" + messageBeanList.size());
                            List<UserInfo> contactListDb = db.getAllContacts(currentUserId, contactList.size(), contactLoadLimit);
                            if (contactListDb != null) {

                                if (contactListDb.size() - 1 > 0) {
                                    progressLoader.setVisibility(View.VISIBLE);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressLoader.setVisibility(View.GONE);
                                        }
                                    }, 100);

                                } else {
                                    progressLoader.setVisibility(View.GONE);
                                }
                                contactList.addAll(contactListDb);
                            }
                            setAdminContactOnTop();
                            contactAdapter.notifyDataSetChanged();


                            //Toast.makeText(getActivity(), "loading", Toast.LENGTH_LONG).show();
                            loading = true;
                        }
                    }
                }
            }
        });
    }

    public static String CONTACT_OFFICIAL = "System Message";

    /*private void setAdminContactOnTop() {

        if (contactList == null || contactList.isEmpty()) return;
        UserInfo adminContactObj = null;
        for (int i = 0; i < contactList.size(); i++) {
            UserInfo contactObj = contactList.get(i);
            if (contactObj == null) continue;
            if (contactObj.getUser_name().equalsIgnoreCase(CONTACT_OFFICIAL)) {
                adminContactObj = contactObj;
                break;
            }
        }
        if (adminContactObj != null) {
            contactList.remove(adminContactObj);
            contactList.add(0, adminContactObj);
        }
    }*/

    private void setAdminContactOnTop() {

        if (contactList == null || contactList.isEmpty()) return;
        UserInfo adminContactObj = null;
        for (int i = 0; i < contactList.size(); i++) {
            UserInfo contactObj = contactList.get(i);
            if (contactObj == null) continue;
            if (contactObj.getUser_name().equalsIgnoreCase(CONTACT_OFFICIAL)) {
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
            if (getContext() != null) {
                dots[i] = new ImageView(getContext());
                if (i == current_position) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.active_lab_dots));

                } else {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.inactive_lab_dots));
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(6, 0, 8, 0);
                dots_layout_lab.addView(dots[i], layoutParams);
            }
        }
    }


    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.BANNER_LIST) {
            BannerResponseNew rsp = (BannerResponseNew) response;
            try {
                bannerList = rsp.getResult();
                bannerAdapter = new BannerAdapter(bannerList, getContext());
                viewPager.setAdapter(bannerAdapter);

            } catch (Exception e) {

            }

        }
        if (ServiceCode == Constant.AUTO_MESSAGE_DATA) {
            AutoMessageNewResponse rsp = (AutoMessageNewResponse) response;
            autoMessageNewResults = new ArrayList<>();
            autoMessageNewResults.addAll(rsp.getResult());
            Log.e("arrayLog", "overallSize => " + autoMessageNewResults.size());
            Log.e("autoMessLog", "data orig => " + new Gson().toJson(rsp));
            Log.e("autoMessLog", "data in arraylist => " + new Gson().toJson(autoMessageNewResults));

            /*for (int i = 0; i < contactList.size(); i++) {
                for (int ii = 0; ii < autoMessageNewResults.size(); ii++) {
                   // Log.e("arrayLog", "contactList => " + i);
                   // Log.e("arrayLog", "autoMessageNewResults => " + ii);
                    //Log.e("arrayLog", "display index => " + i + new Gson().toJson(autoMessageNewResults.get(ii)));

                    if (Integer.parseInt(contactList.get(i).getUser_id()) == autoMessageNewResults.get(ii).getProfileId()) {
                       *//* switch (autoMessageNewResults.get(ii).getType()) {
                            case "text":

                                break;
                            case "image":

                                break;
                            case "audio":

                                break;
                        }*//*

                        Log.e("arrayLog", "index contactList => " + i + new Gson().toJson(contactList.get(i)));
                        Log.e("arrayLog", "index autoMessageNewResults => " + i + new Gson().toJson(autoMessageNewResults.get(ii)));
                       // autoMessageNewResults.remove(ii);
                    }
                }
            }
            Log.e("arrayLog", "array size => " + autoMessageNewResults.size());*/

         /*   ArrayList<Integer> results = new ArrayList<>();
            int indexCount=-1;
            for (AutoMessageNewResult person2 : autoMessageNewResults) {
                boolean found = false;
                for (UserInfo person1 : contactList) {
                    if (person2.getProfileId() == Integer.parseInt(person1.getUser_id())) {
                        indexCount++;
                        found = true;
                        Log.e("arrayLog", "index autoMessageNewResults => " + new Gson().toJson(person2));
                        results.add(indexCount);

                    }
                }
               }
            for (int i=results.size()-1;i>=0;i--){
                autoMessageNewResults.remove(i);
            }
            Log.e("arrayLog", "array size => " + autoMessageNewResults.size());*/


            if (autoMessageNewResults.size() > 0) {
                loopCounter = 0;
                // NODE_COUNT = autoMessageData.size();
                LOOP_COUNT = autoMessageNewResults.size();

                handler = new Handler();
                startLoop();
            }

        }
    }

    MessageBean messageBean = null;
    Messages message = null;

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Perform your task here
                // For example, printing the loop count

                String timestamp = System.currentTimeMillis() + "";
                Log.e("autoMessLog", "indi message size => " + new Gson().toJson(autoMessageNewResults.get(loopCounter).getImage()));

                userId = String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId());
                title = autoMessageNewResults.get(loopCounter).getName();
                pp = autoMessageNewResults.get(loopCounter).getProfileImage();
                if (autoMessageNewResults.get(loopCounter).getType().equals("text")) {
                    msg = autoMessageNewResults.get(loopCounter).getTitle();

                    message = new Messages();
                    message.setFrom(String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()));
                    message.setFromImage(autoMessageNewResults.get(loopCounter).getProfileImage());//https://zeep.live/public/images/zeepliveofficial.png
                    message.setFromName(autoMessageNewResults.get(loopCounter).getName());
                    message.setMessage(autoMessageNewResults.get(loopCounter).getTitle());
                    message.setType(autoMessageNewResults.get(loopCounter).getType());

                    messageBean = new MessageBean(String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()),
                            message, false, timestamp);

                    String contactId = insertOrUpdateContact(messageBean.getMessage(), message.getFrom(), message.getFromName(), message.getFromImage(), timestamp);
                    messageBean.setAccount(contactId);
                    insertChat(messageBean);

                    UserInfo userInfo = new UserInfo(String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()),
                            String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()),
                            autoMessageNewResults.get(loopCounter).getName(),
                            autoMessageNewResults.get(loopCounter).getTitle(),
                            timestamp, autoMessageNewResults.get(loopCounter).getProfileImage(),
                            String.valueOf(0), currentUserId,
                            autoMessageNewResults.get(loopCounter).getType(), "");
                    contactList.add(userInfo);


                } else if (autoMessageNewResults.get(loopCounter).getType().equals("image")) {
                    msg = "Image";

                    message = new Messages();
                    message.setFrom(String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()));
                    message.setFromImage(autoMessageNewResults.get(loopCounter).getProfileImage());//https://zeep.live/public/images/zeepliveofficial.png
                    message.setFromName(autoMessageNewResults.get(loopCounter).getName());
                    message.setMessage(autoMessageNewResults.get(loopCounter).getImage());
                    message.setType(autoMessageNewResults.get(loopCounter).getType());

                    messageBean = new MessageBean(String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()),
                            message, false, timestamp);

                    String contactId = insertOrUpdateContact(messageBean.getMessage(), message.getFrom(), message.getFromName(), message.getFromImage(), timestamp);
                    messageBean.setAccount(contactId);
                    insertChat(messageBean);

                    UserInfo userInfo = new UserInfo(String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()),
                            String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()),
                            autoMessageNewResults.get(loopCounter).getName(),
                            autoMessageNewResults.get(loopCounter).getImage(),
                            timestamp, autoMessageNewResults.get(loopCounter).getProfileImage(),
                            String.valueOf(0), currentUserId,
                            autoMessageNewResults.get(loopCounter).getType(), "");
                    contactList.add(userInfo);


                } else if (autoMessageNewResults.get(loopCounter).getType().equals("audio")) {

                    msg = "Audio";


                    message = new Messages();
                    message.setFrom(String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()));
                    message.setFromImage(autoMessageNewResults.get(loopCounter).getProfileImage());//https://zeep.live/public/images/zeepliveofficial.png
                    message.setFromName(autoMessageNewResults.get(loopCounter).getName());
                    message.setMessage(autoMessageNewResults.get(loopCounter).getAudio());
                    message.setType(autoMessageNewResults.get(loopCounter).getType());

                    messageBean = new MessageBean(String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()),
                            message, false, timestamp);

                    String contactId = insertOrUpdateContact(messageBean.getMessage(), message.getFrom(), message.getFromName(), message.getFromImage(), timestamp);
                    messageBean.setAccount(contactId);
                    insertChat(messageBean);

                    UserInfo userInfo = new UserInfo(String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()),
                            String.valueOf(autoMessageNewResults.get(loopCounter).getProfileId()),
                            autoMessageNewResults.get(loopCounter).getName(),
                            autoMessageNewResults.get(loopCounter).getAudio(),
                            timestamp, autoMessageNewResults.get(loopCounter).getProfileImage(),
                            String.valueOf(0), currentUserId,
                            autoMessageNewResults.get(loopCounter).getType(), "");
                    contactList.add(userInfo);

                }


                new GenNoti().execute();
                displayContactList();

                System.out.println("Loop count: " + loopCounter);
                Log.e("loopLog", "loopCounter (ii) => " + loopCounter + " endSize => " + LOOP_COUNT);
                loopCounter++;

                if (loopCounter < LOOP_COUNT) {
                    // If loop counter hasn't reached the desired count, post the next iteration with delay
                    Log.e("loopLog", "loopCounter (ii) in condition => " + loopCounter + " endSize => " + LOOP_COUNT);
                    handler.postDelayed(this, LOOP_INTERVAL_MS);
                } else {

                    // Loop finished, do cleanup or any other required action
                    // For example, you might want to stop some operation or navigate to another activity
                    System.out.println("Loop finished");
                }
            }
        }, LOOP_INTERVAL_MS);
    }

    private int loopCounter = 0;
    //private int nodeCounter = 0;
    private Handler handler;
    private static final int LOOP_INTERVAL_MS = 20000; // Set your desired time interval in milliseconds
    //private int NODE_COUNT = 10; // Set the number of iterations
    private int LOOP_COUNT = 10; // Set the number of iterations

    //ArrayList<AutoMessageData> autoMessageData = new ArrayList<>();
    ArrayList<AutoMessageNewResult> autoMessageNewResults = new ArrayList<>();
    //ArrayList<Messagedetail> messagedetails = new ArrayList<>();
    String msg, title, pp, userId;
    NotificationManager notificationManager;

    private class GenNoti extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer > 0) {
                if (!AppLifecycle.AppInBackground) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notificationManager.cancel(integer);
                        }
                    }, 6000);
                }
            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            //Looper.prepare();
            if (getContext() != null) {
                String channelId = "channel-fbase";

                Intent notificationIntent = new Intent(getContext(), InboxDetails.class);
                notificationIntent.putExtra("mode", true);
                notificationIntent.putExtra("channelName", "zeeplive662730982537574");
                notificationIntent.putExtra("chatProfileId", userId);
                notificationIntent.putExtra("profileName", title);
                notificationIntent.putExtra("usercount", 0);
                notificationIntent.putExtra("unreadMsgCount", String.valueOf(unreadCount));
                notificationIntent.putExtra("user_image", pp);

                int requestCode = new Random().nextInt();

                PendingIntent pendingIntent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getActivity(getContext(), requestCode,
                            notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                } else {
                    pendingIntent = PendingIntent.getActivity(getContext(), requestCode,
                            notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                }

                // Create a Builder object using NotificationCompat
                // class. This will allow control over all the flags
                NotificationCompat.Builder builder
                        = new NotificationCompat
                        .Builder(getContext(),
                        channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000, 1000,
                                1000, 1000})
                        // .setOnlyAlertOnce(true)
                        .setContentIntent(pendingIntent);


                if (Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.JELLY_BEAN) {
                    builder = builder.setContent(
                            getCustomDesign(title, msg, pp));
                } // If Android Version is lower than Jelly Beans,
                // customized layout cannot be used and thus the
                // layout is set as follows
                else {
                    builder = builder.setContentTitle(title)
                            .setContentText(msg)
                            .setSmallIcon(R.mipmap.ic_launcher);
                }
                if (Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel
                            = new NotificationChannel(
                            channelId, "z_app",
                            NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(
                            notificationChannel);
                }
                int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                //notification = builder.build();

                notificationManager.notify(m, builder.build());
                //notificationManager.notify(m, new Intent);


          /*  notificationTarget = new NotificationTarget(
                    getContext(),
                    R.id.icon_notificationinapp,
                    remoteViews,
                    notification,
                    m);*/

                return m;
            }
            return 0;


        }
    }


    Notification notification;
    Bitmap bitmap;
    RemoteViews remoteViews;
    NotificationTarget notificationTarget;

    private RemoteViews getCustomDesign(String title,
                                        String message, String profileImage) {

        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);

        Log.e("fireImageLog", "actual URL => " + profileImage);
        profileImage = profileImage + "?x-oss-process=image/resize,m_lfit,w_300,h_300/quality,q_100";


        try {
            bitmap = Glide.with(getContext())
                    .asBitmap()
                    .load(profileImage)
                    .submit(300, 300)
                    .get();

           /* GlideApp.with(getContext())
                    .asBitmap()
                    .load(profileImage)
                    .into(notificationTarget);*/

            //((ImageView)rootView.findViewById(R.id.img_test)).setImageBitmap(getCircleBitmap(bitmap));

            Log.e("fireImageLog", "actual URL => " + profileImage);
            Log.e("fireImageLog", "getCircleBitmap URL => " + getCircleBitmap(bitmap));
            remoteViews.setImageViewBitmap(R.id.icon_notificationinapp, getCircleBitmap(bitmap));
            Log.e("fireImageLog", "remoteViews.getLayoutId => " + remoteViews.getLayoutId());

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
}