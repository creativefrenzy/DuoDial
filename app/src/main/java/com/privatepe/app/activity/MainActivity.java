package com.privatepe.app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.app.AppsFlyerPackage.AppsFlyerEvent;
import com.privatepe.app.Firestatus.FireBaseStatusManage;
import com.privatepe.app.IM.IMOperations;
import com.privatepe.app.Inbox.DatabaseHandler;
import com.privatepe.app.Inbox.InboxDetails;
import com.privatepe.app.Inbox.MessageBean;
import com.privatepe.app.Inbox.Messages;
import com.privatepe.app.Inbox.UserInfo;
import com.privatepe.app.R;
import com.privatepe.app.ZegoExpress.zim.ZimManager;
import com.privatepe.app.dialogs.CompleteProfileDialog;
import com.privatepe.app.fragments.metend.HomeFragmentMet;
import com.privatepe.app.fragments.metend.MessageMenuFragment;
import com.privatepe.app.fragments.metend.MyAccountFragment;
import com.privatepe.app.fragments.metend.UserMenuFragmentMet;
import com.privatepe.app.model.OnlineStatusResponse;
import com.privatepe.app.model.ProfileDetailsResponse;
import com.privatepe.app.response.RecentActiveHostModel;
import com.privatepe.app.response.metend.Ban.BanResponce;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.MyCountDownTimer;
import com.privatepe.app.utils.NetworkCheck;
import com.privatepe.app.utils.SessionManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity implements
        ApiResponseInterface, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int PICK_IMAGE_CAMERA_REQUEST_CODE = 0;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 1;
    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static TextView tv_countChat;
    //fragment new Code
    // final Fragment homeFragment = new HomeFragment();
    // final Fragment femaleHomeFragment = new FemaleHomeFragment();
    //Fragment broadcastFragment = new BroadcastFragment();
    private FragmentManager fm = getSupportFragmentManager();
    public Fragment active;
    public boolean nxtPageMsg = false;
    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equals("logout")) {
                logoutDialog();
            }
        }
    };

    String TAG = "MainActivity";
    boolean doubleBackToExitPressedOnce = false;
    SessionManager sessionManager;
    Fragment fragment = null;
    DatabaseReference chatRef;
    ApiManager apiManager;
    String fcmToken;
    /* In-app Update Variables */
    AppUpdateManager mAppUpdateManager;
    InstallStateUpdatedListener installStateUpdatedListener;
    int MY_REQUEST_CODE = 2020;
    int REQUEST_CODE_CHECK_SETTINGS = 2021;

    // Fragment nearbyMenuFragment = new NearbyMenuFragment();
    // Fragment onCamFragment = new OnCamFragment();
    // Fragment nearBy = new NearByListFragment();
    // Fragment searchFragment = new SearchFragment();
    // Fragment myFavourite = new MyFavourite();
    // Fragment recentRecharges = new RecentRecharges();
    //Fragment messageFragment = new MessageFragment();

    // Fragment messageEmployeeFragment = new MessageEmployeeFragment();
    int isGuest = 0;
    HashMap<String, String> user;
    TextView badgeText;
    BroadcastReceiver LogoutBroadFirebase = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equalsIgnoreCase("logout")) {
                Log.e(TAG, "LogoutBroadFirebase:" + "id banned logout device block");
                LogoutUser();
            }

            if (action.equalsIgnoreCase("logoutbyadmin")) {
                Log.e(TAG, "LogoutBroadFirebase:" + "id banned logout admin block");
                LogoutUser();
            }
        }
    };
    int isBlock = 0;
    private NetworkCheck networkCheck;
    private String country_name = "";
    private String city_name = "";
    private String state_name = "";
    private String facebook_name = "";
    private String guest_name;
    private AppLifecycle appLifecycle;
    private ZimManager zimManager;
    private JSONObject MessageWithChatJson;
    private DatabaseHandler dbHandler;
    private int unreadCount;
    private String fGender = "";
    private MyCountDownTimer myCountDownTimer;
    private boolean inCount = false;
    private boolean gotoNearBy = false;
    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyLocationReceiver myLocationReceiver;
    // A reference to the service used to get location updates.
    // private LocationUpdatesService mService = null;
    // Tracks the bound state of the service.
    private boolean mBound = false;

  /*
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };*/

    private Dialog dialog1;

    public static String getProfileImagePath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    IMOperations imOperations;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.content_main);

        dbHandler = new DatabaseHandler(this);

      /*if (authpack.A() != null) {
            FURenderer.initFURenderer(this);
        }*/

        initZim();
        user = new SessionManager(this).getUserDetails();
        //  TempChatDialogue();

        Toolbar toolbar = findViewById(R.id.toolbar);

       /* myLocationReceiver = new MyLocationReceiver();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        //Bind to the service. If the service is in foreground mode, this signals to the service
        //that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);*/

        networkCheck = new NetworkCheck();
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(this);
        imOperations = new IMOperations(getApplicationContext());
        imOperations.loginIm(sessionManager.getUserId());
        sessionManager.setUserLocation("India");

        //Setting Temp location
        //sessionManager.setUserLocation("India");

        // Facebook Analaytics
        AppEventsLogger.newLogger(this);

        // Bottom navigationview

        //fm.beginTransaction().add(R.id.fragment_view, broadcastFragment, "2").hide(broadcastFragment).commit();
        //  fm.beginTransaction().add(R.id.fragment_view, onCamFragment, "2").hide(onCamFragment).commit();
        //   fm.beginTransaction().add(R.id.fragment_view, searchFragment, "3").hide(searchFragment).commit();
        //   fm.beginTransaction().add(R.id.fragment_view, myFavourite, "4").hide(myFavourite).commit();
        //   fm.beginTransaction().add(R.id.fragment_view, recentRecharges, "5").hide(recentRecharges).commit();
        //fm.beginTransaction().add(R.id.fragment_view, messageFragment, "6").hide(messageFragment).commit();
        //   fm.beginTransaction().add(R.id.fragment_view, messageEmployeeFragment, "7").hide(messageEmployeeFragment).commit();
        //fm.beginTransaction().add(R.id.fragment_view, myAccountFragment, "8").hide(myAccountFragment).commit();
        loadAllFragments();
        if (sessionManager.getGender() != null) {
            if (sessionManager.getGender().equals("male")) {
                // fragment = new HomeFragment();
                sessionManager.setLangState(0);
                sessionManager.setOnlineState(0);
                addFragment(userMenuFragmentMet, "1");
                // replaceFragment(new UserMenuFragmentMet(), "1");
                ((ImageView) findViewById(R.id.img_newMenuOnCam)).setVisibility(View.GONE);
            }
        }

        sessionManager = new SessionManager(getApplicationContext());
        appLifecycle = new AppLifecycle();
        if (!sessionManager.getIsRtmLoggedIn()) {
            //   appLifecycle.loginRtm(sessionManager.getUserId());
            sessionManager.setIsRtmLoggedIn(true);
        }
        apiManager = new ApiManager(this, this);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Log.e("FirebaseTokenLog", "retrieve token successful : " + token);
                //Log.e("appVersion","appVersion = "+appVersion);
                fcmToken = token;
                String AppVersionCode = "";
                try {
                    AppVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    Log.e(TAG, "fcmToken: app version " + AppVersionCode);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                apiManager.registerFcmToken("Bearer " + sessionManager.getUserToken(), fcmToken, AppVersionCode);
            } else {
                //Log.e("FirebaseTokenLog", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task ->
                Log.e("FirebaseTokenLog", "This is the token : " /*+ task.getResult()*/)
        );

        //chatRef = FirebaseDatabase.getInstance().getReference().child("Users");
        apiManager.getProfileDetails();
        apiManager.getRechargeListNew();
        apiManager.getCategoryGifts();
        apiManager.getStoreTablist();

        // apiManager.getNotificationsList();

        //apiManager.getGfiftList();
        /* apiManager.getProfileIdData("205489733");*/

        /* In app update */
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            } else {
                //  Log.i("ProductList", "InstallStateUpdatedListener: state: " + state.installStatus());
            }
        };

        checkForUpdates();

        // getChatList();
        // initSocket();
        // sessionManager.setOnlineState(1);
        // sessionManager.setLangState(0);

        drawBadge();

        facebook_name = sessionManager.getUserFacebookName();
        Log.e("userfacebookname", facebook_name);
        checkGuestLogin();
        String[] permissions;
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION};
            Log.e(TAG, "onCreate: Permission for android 13");
        } else {
            permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION};
            Log.e(TAG, "onCreate: Permission for below android 13");
        }

        getPermission(permissions);

        if (sessionManager.getGender().equals("male")) {
            //  getPermission();
            fGender = "1";
        } else {
            fGender = "0";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                enableLocationSettings();
            }
        } else {
            enableLocationSettings();
        }

        getNotiIntentFun();
        tv_countChat = findViewById(R.id.tv_countChat);

      /*  ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1);
        // If you have access to the external storage, do whatever you need

        try {
            if (Environment.isExternalStorageManager()) {
                Log.e("inEnvirementRequest", "success");

// If you don't have access, launch a new activity to show the user the system's dialog
// to allow access to the external storage
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } catch (NoSuchMethodError e) {
            Log.e("inEnvirementRequest", "m in error");
            if (Environment.isExternalStorageRemovable()) {
                Log.e("inEnvirementRequest", "success 2");
            } else {
                Log.e("inEnvirementRequest", "m in error 2");
            }
        }

        new UpdateVersionDialog(MainActivity.this);*/
        getChatData();
    }

    private DatabaseReference rootRef;
    private String currentUserId, receiverUserId;

    private boolean passMessage = false;
    private DatabaseHandler db;

    void getChatData() {
        db = new DatabaseHandler(getApplicationContext());
        rootRef = FirebaseDatabase.getInstance().getReference();
        currentUserId = String.valueOf(new SessionManager(getApplicationContext()).getUserId());
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
                        MessageBean messageBean = new MessageBean(message.getFrom(), message, false, timestamp);

                        String contactId = insertOrUpdateContact(messageBean.getMessage(), message.getFrom(), message.getFromName(), message.getFromImage(), timestamp);
                        messageBean.setAccount(contactId);
                        insertChat(messageBean);

                        int count = db.getTotalUnreadMsgCount(currentUserId);
                        chatCount(String.valueOf(count));

                        Intent refreshChatIN = new Intent("SAN-REFRESHCHATBROAD");
                        refreshChatIN.putExtra("action", "refesh");
                        sendBroadcast(refreshChatIN);
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
        int count = db.getTotalUnreadMsgCount(currentUserId);
        chatCount(String.valueOf(count));
       /* rootRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        rootRef.removeValue();*/
    }

    public void loginZim(String username, String userId, String userIcon) {
        Log.d(TAG, "loginZim: ");
     /*   String token = AuthInfoManager.getInstance().generateToken(userId);
        ZimManager.sharedInstance().loginZim(userId, username, userIcon, token, new ResultCallback() {
            @Override
            public void onZimCallback(ZIMErrorCode errorCode, String errMsg) {
                if (errorCode == ZIMErrorCode.SUCCESS) {
                    Log.e("loginZim", "Main Activity " + "success");
                } else {
                    Log.e("loginZim", "Main Activity " + "fail " + errorCode);
                }
            }
        });*/
    }

    private void initZim() {
        // zimManager = ZimManager.sharedInstance();

     /*
        zimEventListener = new ZimEventListener() {
            @Override
            public void onCallInvitationCancelled(UserInfo userInfo, CallType cancelType) {

            }

            @Override
            public void onCallInvitationAccepted(UserInfo userInfo) {

            }

            @Override
            public void onCallInvitationRejected(UserInfo userInfo) {

            }

            @Override
            public void onCallInvitationTimeout() {

            }

            @Override
            public void onCallInviteesAnsweredTimeout() {

            }

            @Override
            public void onReceiveCallEnded() {

            }

            @Override
            public void onConnectionStateChanged(ZIMConnectionState state, ZIMConnectionEvent event) {

            }

            @Override
            public void onReceiveZIMPeerMessage(ZIMMessage zimMessage, String fromUserID) {
                ZIMMessageType type = zimMessage.getType();
                if (type == ZIMMessageType.TEXT) {
                    ZIMTextMessage txtMsg = (ZIMTextMessage) zimMessage;
                    Log.e("MainActivity", "onReceiveZIMPeerMessage: " + txtMsg.message);

                    //  revMsgTv.setText(txtMsg.message);

                    try {
                        *//*     if (txtMsg.message.equals("User busy on call")) {

         *//**//* Toast.makeText(getApplicationContext(), "User busy on call", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("FBR-ENDTHIS");
                            intent.putExtra("action", "end2");
                            sendBroadcast(intent);*//**//*
                            return;
                        }*//*

                        JSONObject jsonObject = new JSONObject(txtMsg.message);
                        if (jsonObject.has("isMessageWithChat")) {
                            if (jsonObject.get("isMessageWithChat").toString().equals("yes")) {
                                MessageWithChatJson = new JSONObject(jsonObject.get("ChatMessageBody").toString());
                                Log.e("ReceivedMessage", "=> " + MessageWithChatJson.getString("MessageContent"));

                                Intent intent = new Intent("USER-TEXT");
                                intent.putExtra("peerId", fromUserID);
                                intent.putExtra("msg", MessageWithChatJson.toString());
                                sendBroadcast(intent);

                                SessionManager sessionManager = new SessionManager(MainActivity.this);

                                String userProfilePic = sessionManager.getUserProfilepic();
                                String userId = sessionManager.getUserId();
                                String name = sessionManager.getUserName();

                                Messages message = new Messages();
                                message.setFrom(userId);
                                message.setMessage(MessageWithChatJson.getString("MessageContent"));
                                message.setFromName(name);
                                message.setFromImage(userProfilePic);

                                message.setTime_stamp(System.currentTimeMillis());
                                message.setType("text");
                                String timestamp = System.currentTimeMillis() + "";

                                Log.e("savedData", "saved");

                                String contact_Id = insertOrUpdateContact(message, fromUserID, MessageWithChatJson.get("UserName").toString(), MessageWithChatJson.get("ProfilePic").toString(), timestamp);
                                MessageBean messageBean;
                                messageBean = new MessageBean(contact_Id, message, false, timestamp);
                                dbHandler.addChat(messageBean);

                                Intent refreshChatIN = new Intent("SAN-REFRESHCHATBROAD");
                                refreshChatIN.putExtra("action", "refesh");
                                sendBroadcast(refreshChatIN);
                                Log.e("ZegoListenerBug", "onReceiveZIMPeerMessage: ApplifeCycle " + "sent");
                            }
                        }
                    } catch (JSONException e) {

                    }

                   *//* JSONObject jsonObject= null ;
                    try {
                        jsonObject = new JSONObject(txtMsg.message);
                        if (jsonObject.has("isMessageWithChat")) {
                            if (jsonObject.get("isMessageWithChat").toString().equals("yes")) {
                                MessageWithChatJson = new JSONObject(jsonObject.get("ChatMessageBody").toString());
                                if (MessageWithChatJson != null) {
                                    Log.e("MessageWithChatJson", MessageWithChatJson.toString());
                                    saveChatInDb(fromUserID, MessageWithChatJson.get("UserName").toString(), "", MessageWithChatJson.get("Message").toString(), MessageWithChatJson.get("Date").toString(), "", MessageWithChatJson.get("Time").toString(), MessageWithChatJson.get("ProfilePic").toString(),"TEXT");

                                    Intent intent = new Intent("USER-TEXT");
                                    intent.putExtra("peerId", fromUserID);
                                    intent.putExtra("msg", MessageWithChatJson.toString());
                                    sendBroadcast(intent);
                                }
                            }
                        }
                        else if (jsonObject.has("isMessageWithChatGift")) {
                            if (jsonObject.get("isMessageWithChatGift").toString().equals("yes")) {
                                String giftPos = new JSONObject(jsonObject.get("ChatGiftMessageBody").toString()).get("GiftPos").toString();
                                String peerName = new JSONObject(jsonObject.get("ChatGiftMessageBody").toString()).get("UserName").toString();
                                String peerProfilePic = new JSONObject(jsonObject.get("ChatGiftMessageBody").toString()).get("ProfilePic").toString();

                                // Log.e("ChatGift", "Received  "+giftPos);

                                saveChatInDb(fromUserID,
                                        peerName,
                                        "",
                                        giftPos,
                                        "",
                                        "",
                                        "",
                                        peerProfilePic,"GIFT");

                                Intent chatGiftIntent = new Intent("GIFT-USER-TEXT");
                                chatGiftIntent.putExtra("pos", giftPos);
                                chatGiftIntent.putExtra("peerId", fromUserID);
                                chatGiftIntent.putExtra("peerName", peerName);
                                chatGiftIntent.putExtra("peerProfilePic", peerProfilePic);
                                sendBroadcast(chatGiftIntent);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*//*
                }
            }
        };*/

        //  zimManager.addListener(zimEventListener);
    }

    private void insertChat(MessageBean messageBean) {
        db.addChat(messageBean);
    }

    private String insertOrUpdateContact(Messages message, String userId, String profileName, String profileImage, String timestamp) {
        String currentUserId = new SessionManager(MainActivity.this).getUserId();
        dbHandler = new DatabaseHandler(this);
        UserInfo userInfoFromDb = dbHandler.getContactInfo(userId, currentUserId);
        String contactId = "";
        if (userInfoFromDb == null) { // insert
            UserInfo userInfo = new UserInfo();
            userInfo.setUser_id(userId);
            userInfo.setUser_name(profileName);
            userInfo.setMessage(message.getMessage());
            userInfo.setUser_photo(profileImage);
            userInfo.setTime(timestamp);
            userInfo.setUnread_msg_count("0");
            userInfo.setProfile_id(currentUserId);
            userInfo.setMsg_type(message.getType());
            contactId = dbHandler.addContact(userInfo);
        } else { //update
            contactId = userInfoFromDb.getId();
            userInfoFromDb.setUser_name(profileName);
            userInfoFromDb.setMessage(message.getMessage());
            userInfoFromDb.setUser_photo(profileImage);
            userInfoFromDb.setTime(timestamp);
            userInfoFromDb.setUnread_msg_count(getUnreadMsgCount(userInfoFromDb.getUnread_msg_count(), currentUserId));
            Log.e("InboxFragment", "peersValue " + getUnreadMsgCount(userInfoFromDb.getUnread_msg_count(), currentUserId));
            userInfoFromDb.setMsg_type(message.getType());
            dbHandler.updateContact(userInfoFromDb);
        }
        return contactId;
    }

    private String getUnreadMsgCount(String unreadMsgCount, String profileId) {
        if (!TextUtils.isEmpty(unreadMsgCount)) {
            unreadCount = Integer.parseInt(unreadMsgCount);
        }
        unreadCount++;
        if (!TextUtils.isEmpty(InboxDetails.chatProfileId)) {
            if (InboxDetails.chatProfileId.equals(profileId) && AppLifecycle.isChatActivityOpen()) { //current chatting user
                unreadCount = 0;
            }
        }

        return String.valueOf(unreadCount);
    }

    public void chatCount(String count) {
        tv_countChat.setText(count);
        Log.e("CounterIncreament", "yes =>" + count);
    }

    private void getNotiIntentFun() {
        try {
            Intent intent = getIntent();
            boolean fromNotification = intent.getBooleanExtra("fromNotification", false);
            if (fromNotification) {
                String channelName = intent.getStringExtra("channelName");
                String chatProfileId = intent.getStringExtra("chatProfileId");
                String profileName = intent.getStringExtra("profileName");
                String profileImage = intent.getStringExtra("user_image");
                boolean stateSingleMode = intent.getBooleanExtra("mode", true);
                int channelUserCount = intent.getIntExtra("usercount", 0);
                int unreadMsgCount = intent.getIntExtra("unreadMsgCount", 0);

                Log.e("profileImageMain", profileImage);

                unselectAllMenu();
                ((ImageView) findViewById(R.id.img_newMenuMessage)).setImageResource(R.drawable.conversationselected);
                showFragment(messageMenuFragment);
                //replaceFragment(new MessageMenuFragment(), "6");
                detachOncam();

                startActivity(new Intent(MainActivity.this, InboxDetails.class)
                        .putExtra("channelName", channelName)
                        .putExtra("chatProfileId", chatProfileId)
                        .putExtra("profileName", profileName)
                        .putExtra("mode", stateSingleMode)
                        .putExtra("usercount", channelUserCount)
                        .putExtra("unreadMsgCount", unreadMsgCount)
                        .putExtra("fromNotification", fromNotification)
                        .putExtra("user_image", profileImage));
            }
        } catch (Exception e) {

        }
      /*  DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.e("firebaseCheck","connected");
                    System.out.println("connected");
                } else {
                    System.out.println("not connected");
                    Log.e("firebaseCheck","not connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
                Log.e("firebaseCheck","Listener was cancelled");
            }
        });*/
    }

    private void getPermission(String[] permissions) {
        Log.e(TAG, "getPermission: permissions array length " + permissions.length);
        Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        try {
                            if (report.areAllPermissionsGranted()) {

                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {

                            }
                            if (report.getGrantedPermissionResponses().get(0).getPermissionName().equals("android.permission.ACCESS_FINE_LOCATION")) {
                                enableLocationSettings();
                                // enableLocationSettings();
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkGuestLogin() {
        if (sessionManager.getGender().equals("male")) {
            isGuest = sessionManager.getGuestStatus();
            Log.e("inActivity", "" + new SessionManager(this).getGuestStatus());
            if (isGuest != 1) {
                if (isGuest == 0) {
                    new CompleteProfileDialog(this, facebook_name);
                    isGuest = 1;
                } else if (facebook_name != null) {
                    new CompleteProfileDialog(this, facebook_name);
                }
                if (!sessionManager.getUserAskpermission().equals("no")) {
                    //  new PermissionDialog(MainActivity.this);
                }
            } else {
                //apiManager.getRemainingGiftCardDisplayFunction();
            }
        }
    }

    public boolean canGetLocation() {
        boolean result = true;
        LocationManager lm;
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            networkEnabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return gpsEnabled && networkEnabled;
    }

    public void enableLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //  LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                try {
                    //    mService.requestLocationUpdates();
                } catch (Exception e) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enableLocationSettings();
                        }
                    }, 2000);
                }
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CODE_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private void drawBadge() {
      /*  BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, itemView, true);
        badgeText = badge.findViewById(R.id.notificationsinbag);
        badgeText.setText("0");*/
    }

    /*  void checkOnlineAvailability(String uid, String name, String image) {
          DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
          connectedRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  boolean connected = snapshot.getValue(Boolean.class);
                  if (connected) {
                      // Change online status when user comes back on app
                      HashMap<String, String> details = new HashMap<>();
                      details.put("uid", uid);
                      details.put("name", name);
                      details.put("image", image);
                      details.put("status", "Online");
                      details.put("fcmToken", fcmToken);
                      chatRef.child(uid).setValue(details);
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {
                  Log.e(TAG, "Listener was cancelled");
              }
          });
      }
  */
    void checkForUpdates() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.registerListener(installStateUpdatedListener);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this, MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                MainActivity.this.popupSnackbarForCompleteUpdate();
            } else {
                Log.e("ProductList", "checkForAppUpdateAvailability: something else");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("selectedImage", "selectedImage:" + data);
        if (REQUEST_CODE_CHECK_SETTINGS == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                //user clicked OK, you can startUpdatingLocation(...);
                //          autoCountrySelect();
                enableLocationSettings();
            } else {
                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
            }
        }
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("ProductList", "onActivityResult: app download failed");
            }
        } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null || requestCode == PICK_IMAGE_CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK) {
                        Intent Intent = new Intent("FBR-USER-IMAGE");
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        // call this method to get the URI from the bitmap
                        Uri selectedCamera = getImageUri(getApplicationContext(), photo);
                        // call this method to get the actual path of capture image
                        String finalCameraImage = getProfileImagePath(this, selectedCamera);
                        Log.e("selectedCameraImage", "selectedCameraImage:" + finalCameraImage);
                        if (!finalCameraImage.equals("Not found")) {
                            Intent.putExtra("uri", finalCameraImage);
                            Intent.putExtra("fromCam", "yes");
                            this.sendBroadcast(Intent);
                        }
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        Intent myIntent = new Intent("FBR-USER-IMAGE");
                        Uri selectedImage = data.getData();
                        // Log.e("selectedImage", "selectedImage:" + selectedImage);
                        String picturePath = getProfileImagePath(this, selectedImage);
                        if (!picturePath.equals("Not found")) {
                            myIntent.putExtra("uri", picturePath);
                            myIntent.putExtra("fromCam", "no");
                            this.sendBroadcast(myIntent);
                            // Log.e("selectedImage", "selectedImage:" + picturePath);
                        }
                    }
                    break;
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Capture", null);
        return Uri.parse(path);
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container_main),
                "New app is ready!",
                Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocationReceiver);
        unregisterReceiver(myReceiver);
        startCountDown();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            //unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    public void reStartEngine() {
        /*Intent myIntent = new Intent("KAL-CLOSEME");
        myIntent.putExtra("action", "resetEngine");
        this.sendBroadcast(myIntent);*/
    }

    public void showMyEarning() {
        //  EditProfileFragment editProfileFragment = new EditProfileFragment();
        //fm.beginTransaction().add(R.id.fragment_view, editProfileFragment, "10").hide(editProfileFragment).commit();
        //showFragment(editProfileFragment);
        //  addFragment(editProfileFragment, "10");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        if (sessionManager.getFirstTimeRecharged().equals("0")) {
            apiManager.checkFirstTimeRechargeDone();
        }

        /*  LocalBroadcastManager.getInstance(this).registerReceiver(myLocationReceiver,new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));*/
        reginFirebase();
        // loginZim(user.get(NAME), user.get(PROFILE_ID), user.get(PROFILE_PIC));
        registerReceiver(myReceiver, new IntentFilter("FBR-IMAGE"));
        //reStartEngine();
        //  Menu menu = navigation.getMenu();
        registerReceiver(LogoutBroadFirebase, new IntentFilter("FirebaseReceiverBroad"));
        // Change status when user open app
        try {
            if (sessionManager.getGender().equals("male")) {
                if (myCountDownTimer != null) {
                    inCount = false;
                    myCountDownTimer.cancel();
                }
                //   menu.findItem(R.id.navigation_search).setIcon(R.drawable.ic_oncam);
                //   menu.findItem(R.id.navigation_search).setTitle("Live");
                // if male user is offline hit api to change status
                apiManager.changeOnlineStatus(1);
                if (sessionManager.getUserLoginCompleted()) {
                    if (!sessionManager.isTopicSubscribed("fake_call"))
                        FireBaseStatusManage.subscribeToTopic(MainActivity.this, "fake_call");
                    if (!sessionManager.getFakeCall()) {
                        apiManager.getRecentActiveHost();
                    }
                }
            } else {
               /* menu.findItem(R.id.navigation_favourite).setIcon(R.drawable.ic_recent_recharges);
                menu.findItem(R.id.navigation_favourite).setTitle("Recent");
                menu.findItem(R.id.navigation_search).setIcon(R.drawable.ic_favorite);
                menu.findItem(R.id.navigation_search).setTitle("Live");*/
                apiManager.getBanList();
            }
        } catch (Exception e) {
        }
    }

    /*void verifyUserRegisteredFirebase(String uid, String name, String image) {
        chatRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.child("name").exists()) {
                        // Set for female user only(prevent server load from php)
                        if (!sessionManager.getGender().equals("male")) {
                            // Prepare offline status for existing user
                            HashMap<String, String> details = new HashMap<>();
                            details.put("uid", uid);
                            details.put("name", name);
                            details.put("image", image);
                            details.put("status", "Offline");
                            details.put("fcmToken", fcmToken);

                            // for disconnected state
                            chatRef.child(uid).onDisconnect().setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        checkOnlineAvailability(uid, name, image);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Not Working", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        HashMap<String, String> details = new HashMap<>();
                        details.put("uid", uid);
                        details.put("name", name);
                        details.put("image", image);
                        details.put("fcmToken", fcmToken);

                        // Set for female user only(prevent server load from php)
                        if (!sessionManager.getGender().equals("male")) {
                            details.put("status", "Offline");
                        }

                        chatRef.child(uid).setValue(details).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Set for female user only(prevent server load for php)
                                if (!sessionManager.getGender().equals("male")) {
                                    // for disconnected state
                                    chatRef.child(uid).onDisconnect().setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                checkOnlineAvailability(uid, name, image);
                                            }
                                        }
                                    });
                                }
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void startCountDown() {
        //hide code startCountDown 9/04/21
       /* if (!inCount) {
            myCountDownTimer = new MyCountDownTimer(10000, 1000, getApplicationContext());
            inCount = true;
            myCountDownTimer.start();
        }*/
    }

    private void loadAllFragments() {
        fm.beginTransaction().add(R.id.fragment_view, messageMenuFragment, "6").commit();
        fm.beginTransaction().add(R.id.fragment_view, myAccountFragment, "8").hide(myAccountFragment).commit();
        Log.e(TAG, "LoadAllFragments: " + " Load all fragments.");
    }

    UserMenuFragmentMet userMenuFragmentMet = new UserMenuFragmentMet();
    MessageMenuFragment messageMenuFragment = new MessageMenuFragment();
    MyAccountFragment myAccountFragment = new MyAccountFragment();

    private void addFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            fm.beginTransaction().add(R.id.fragment_view, userMenuFragmentMet, "1").commit();
            active = fragment;
            Log.e(TAG, "addFragment: " + fragment);
        }
    }

    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            if (fm == null) {
                fm = getSupportFragmentManager();
            }
            if (active != null) {
                fm.beginTransaction().hide(active).show(fragment).commit();
                active = fragment;
                Log.e(TAG, "showFragment: " + fragment.toString());
            } else {
                Log.e(TAG, "showFragment: active is null ");
            }
        } else {
            Log.e(TAG, "showFragment: fragment is null");
        }
    }

    private void replaceFragment(Fragment fragment, String tag) {
       /* fm.beginTransaction().replace(R.id.fragment_view, fragment, tag).commit();
        active = fragment;*/
    }

    public void showFollowers() {
        /*Intent myIntent = new Intent("FBR");
        myIntent.putExtra("action", "reload");
        this.sendBroadcast(myIntent);*/
        // showFragment(myFavourite);
    }

    private void detachOncam() {
        /*try {
            getSupportFragmentManager().beginTransaction().remove(onCamFragment).commitAllowingStateLoss();
        } catch (Exception e) {
        }*/
    }

    /* private boolean loadFragment(Fragment fragment) {
         //switching fragment
         if (fragment != null) {
             getSupportFragmentManager()
                     .beginTransaction()
                     .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                     .replace(R.id.fragment_view, fragment)
                     .commit();
             return true;
         }
         return false;
     }*/

    public void loadSearchFragement() {
      /*  fm.beginTransaction().add(R.id.fragment_view, searchFragment, "3").hide(searchFragment).commit();
        showFragment(searchFragment);
        if (sessionManager.getGender().equals("male")) {
            startCountDown();
        }*/
    }

    @Override
    public void onBackPressed() {
        unselectAllMenu();
        ((ImageView) findViewById(R.id.img_newMenuHome)).setImageResource(R.mipmap.home_tab_on);
        ((LinearLayout) findViewById(R.id.cvbottom_navigation)).setBackgroundResource(R.color.tab_bg_color);
        if (active instanceof UserMenuFragmentMet || active instanceof HomeFragmentMet) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "click BACK again to go Exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> {
                doubleBackToExitPressedOnce = false;
            }, 2000);
        } else {
            if (sessionManager.getGender().equals("male")) {
                showFragment(userMenuFragmentMet);
                //replaceFragment(new UserMenuFragmentMet(), "1");
                if (myCountDownTimer != null) {
                    inCount = false;
                    myCountDownTimer.cancel();
                }
//       fragment = new HomeFragment();
            } else {
             /*   if (active instanceof SettingFragment || active instanceof IncomeReportFragment) {
                    unselectAllMenu();
                    ((ImageView) findViewById(R.id.img_newMenuProfile)).setImageResource(R.drawable.avatarselected);
                    active = myAccountFragment;
                    showMenu();
                    fm.popBackStackImmediate();
                } else if (active instanceof EmployeeViewProfileFragment) {
                    unselectAllMenu();
                    ((ImageView) findViewById(R.id.img_newMenuRecentRecharge)).setImageResource(R.drawable.recentrechargeselect);
                    active = recentRecharges;
                    showMenu();
                    fm.popBackStackImmediate();
                } else if (active instanceof InboxDetailsFragment) {
                    unselectAllMenu();
                    ((ImageView) findViewById(R.id.img_newMenuMessage)).setImageResource(R.drawable.conversationselected);
                    active = messageFragment;
                    showMenu();
                    fm.popBackStackImmediate();
                } else {
                    showMenu();
                    showFragment(femaleHomeFragment);
                }*/
            }
        }
    }

    public void hideMenu() {
        ((LinearLayout) findViewById(R.id.cvbottom_navigation)).setVisibility(View.GONE);
    }

    public void showMenu() {
        ((LinearLayout) findViewById(R.id.cvbottom_navigation)).setVisibility(View.VISIBLE);
    }

    public void loadHomeFragment() {
        detachOncam();
      /*  onCamFragment = new OnCamFragment();
        fm.beginTransaction().add(R.id.fragment_view, onCamFragment, "2").hide(onCamFragment).commit();
        showFragment(onCamFragment);
        navigation.getMenu().getItem(1).setChecked(true);*/
    }

    @Override
    protected void onDestroy() {
        // Change status when user open app
        if (sessionManager != null && sessionManager.getGender() != null) {
            if (sessionManager.getGender().equals("male")) {
                apiManager.changeOnlineStatus(0);
            } else {
                if (new SessionManager(getApplicationContext()).getHostAutopickup().equals("yes")) {
                    Intent myIntent = new Intent("KAL-CLOSEME");
                    myIntent.putExtra("action", "closeme");
                    this.sendBroadcast(myIntent);
                }
            }
        }
        unregisterReceiver(LogoutBroadFirebase);
        imOperations.releaseIM();

        /*  zimManager.removeListener(zimEventListener);*/
        super.onDestroy();
    }

    private void LogoutUser() {
        new SessionManager(MainActivity.this).logoutUser();
        apiManager.getUserLogout();
        finishAffinity();
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
    /*if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD_DISPLAY) {
            RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;
            try {
                int remGiftCard = rsp.getResult().getRemGiftCards();
                if (remGiftCard > 0) {
                    dialog1 = new Dialog(this);
                    dialog1.setContentView(R.layout.freecard_layout);
                    dialog1.setCancelable(false);
                    dialog1.setCanceledOnTouchOutside(true);
                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    TextView tv_freecardcount = dialog1.findViewById(R.id.tv_freecardcount);
                    Button btn_gotit = dialog1.findViewById(R.id.btn_gotit);
                    tv_freecardcount.setText(remGiftCard + " gift cards received. Enjoy your free chance of video call.");
                    btn_gotit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });
                    dialog1.show();
                }
            } catch (Exception e) {
            }
        }*/

        if (ServiceCode == Constant.USER_LOCATION_UPDATED) {
            // Toast.makeText(getApplicationContext(), "Location Updated to server", Toast.LENGTH_SHORT).show();
           /* if (gotoNearBy) {
                unselectAllMenu();
                ((ImageView) findViewById(R.id.img_newMenuOnCam)).setImageResource(R.drawable.playbuttonselected);

                nearbyMenuFragment = new NearbyMenuFragment();
                fm.beginTransaction().add(R.id.fragment_view, nearbyMenuFragment, "2").hide(nearbyMenuFragment).commit();
                showFragment(nearbyMenuFragment);
            }*/
        }

        if (ServiceCode == Constant.REGISTER_FCM_TOKEN) {
            sessionManager.saveFcmToken(fcmToken);
            //saveUserTokenIntoFirebase(fcmToken);
        }

        if (ServiceCode == Constant.MANAGE_ONLINE_STATUS) {
            OnlineStatusResponse reportResponse = (OnlineStatusResponse) response;
        }
        if (ServiceCode == Constant.PROFILE_DETAILS) {
            ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;
            if (rsp.getSuccess() != null) {
                Log.e("Check_JKData", "Profile ID : "+rsp.getSuccess().getProfile_id());
                String img = "";
                if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                    img = rsp.getSuccess().getProfile_images().get(0).getImage_name();
                }

               /* // Register User into firebase
                if (sessionManager.getGender().equals("male")) {
                    verifyUserRegisteredFirebase(String.valueOf(rsp.getSuccess().getProfile_id()), rsp.getSuccess().getName(), img);
                }*/
                //reginFirebase();
                initAppFlyNew();
            }
        }

        if (ServiceCode == Constant.BAN_DATAP) {
            BanResponce reportResponse = (BanResponce) response;
            if (reportResponse.getResult() != null) {
                if (reportResponse.getResult().getIsBanned() == 1) {
                    ((CardView) findViewById(R.id.cv_ban)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_banmsg)).setText("You are ban, please contact your manager.");
                    isBlock = 1;
                    //navigation.setVisibility(View.GONE);
                } else {
                    ((CardView) findViewById(R.id.cv_ban)).setVisibility(View.GONE);
                }
            }
        }
        if (ServiceCode == Constant.RECENT_ACTIVE_HOST_DETAILS) {
            RecentActiveHostModel rsp = (RecentActiveHostModel) response;
            if (rsp.result != null) {
                if (Constant.isReceivedFakeCall) {
                    sessionManager.setFakeCall(true);
                    Intent i = new Intent(MainActivity.this, RequestCallActivity.class);
                    i.putExtra("userID", ""+rsp.result.user_id);
                    i.putExtra("receiver_id", ""+rsp.result.user_id);
                    i.putExtra("profileID", ""+rsp.result.profile_id);
                    i.putExtra("username", rsp.result.name);
                    i.putExtra("unique_id", "unique_id");
                    i.putExtra("callRate", ""+rsp.result.call_price);
                    i.putExtra("callType", "video");
                    i.putExtra("is_free_call", "is_free_call");
                    i.putExtra("name", rsp.result.name);
                    i.putExtra("image", rsp.result.profile_image);
                    startActivity(i);
                }
            }
        }
    }

    private void initAppFlyNew() {
        AppsFlyerEvent appsFlyerManager = AppsFlyerEvent.getInstance(getApplicationContext());
        appsFlyerManager.customerIdAndLogSession();
        //appsFlyerManager.trackDeviceConfig();
    }

    public int isBlockFunction() {
        return isBlock;
    }

    public void updateMessageCount(int msgCount) {
        if (msgCount >= 99) {
            tv_countChat.setText("99+");
            //((TextView) findViewById(R.id.tv_unreadmain)).setText("99+");
        } else {
            tv_countChat.setText(String.valueOf(msgCount));
            // ((TextView) findViewById(R.id.tv_unreadmain)).setText(String.valueOf(msgCount));
        }
    }

    public void logoutDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        TextView logout = dialog.findViewById(R.id.logout);

        tv_msg.setText("You have been logout. As your access token is expired.");

        closeDialog.setVisibility(View.GONE);
        closeDialog.setOnClickListener(view -> dialog.dismiss());

        logout.setText("OK");
        logout.setOnClickListener(view -> {
            dialog.dismiss();
            String eMail = new SessionManager(getApplicationContext()).getUserEmail();
            String passWord = new SessionManager(getApplicationContext()).getUserPassword();
            new SessionManager(getApplicationContext()).logoutUser();
            new ApiManager(getApplicationContext(), this).getUserLogout();
            new SessionManager(getApplicationContext()).setUserEmail(eMail);
            new SessionManager(getApplicationContext()).setUserPassword(passWord);

            if (new SessionManager(getApplicationContext()).getHostAutopickup().equals("yes")) {
                Intent myIntent = new Intent("KAL-CLOSEME");
                myIntent.putExtra("action", "closeme");
                this.sendBroadcast(myIntent);
            }
            finish();
        });
    }

    public void newProfileMenu(View v) {
        unselectAllMenu();
        ((ImageView) findViewById(R.id.img_newMenuProfile)).setImageResource(R.mipmap.account_tab_on);
        ((LinearLayout) findViewById(R.id.cvbottom_navigation)).setBackgroundResource(R.color.tab_bg_color);
      /*  myAccountFragment = new MyAccountFragment();
        fm.beginTransaction().add(R.id.fragment_view, myAccountFragment, "2").hide(myAccountFragment).commit();*/
        //showFragment(myAccountFragment);
        showFragment(myAccountFragment);
        // replaceFragment(new MyAccountFragment(), "8");
        /*if (sessionManager.getGender().equals("male")) {
            detachOncam();
        }*/
    }

    public void newChatMenu(View v) {
        unselectAllMenu();
        showFragment(messageMenuFragment);

        // replaceFragment(new MessageMenuFragment(), "6");
        ((ImageView) findViewById(R.id.img_newMenuMessage)).setImageResource(R.mipmap.message_tab_on);
        //messageFragment.getView().setBackgroundResource(R.color.white);
        ((LinearLayout) findViewById(R.id.cvbottom_navigation)).setBackgroundResource(R.color.white);
        //showFragment(messageFragment);
        detachOncam();
    }

    public void recentRechargeNewMenu(View v) {
        unselectAllMenu();
        ((ImageView) findViewById(R.id.img_newMenuRecentRecharge)).setImageResource(R.drawable.recentrechargeselect);
      /*  recentRecharges = new RecentRecharges();
        fm.beginTransaction().add(R.id.fragment_view, recentRecharges, "2").hide(recentRecharges).commit();
        showFragment(recentRecharges);*/
    }

    public void nearLocation(View v) {
        unselectAllMenu();
        ((ImageView) findViewById(R.id.nearBy)).setImageResource(R.drawable.ic_location);
        //((ImageView) findViewById(R.id.img_newMenuOnCam)).setVisibility(View.GONE);
        if (sessionManager.getGender().equals("male")) {
            //  onCamFragment = new OnCamFragment();
           /* nearbyMenuFragment = new NearbyMenuFragment();
            fm.beginTransaction().add(R.id.fragment_view, nearbyMenuFragment, "2").hide(nearbyMenuFragment).commit();*/
            gotoNearBy = true;
            /*nearbyMenuFragment = new NearbyMenuFragment();
            fm.beginTransaction().add(R.id.fragment_view, nearbyMenuFragment, "2").hide(nearbyMenuFragment).commit();*/
          /*  nearBy = new NearByListFragment();
            fm.beginTransaction().add(R.id.fragment_view, nearBy, "2").hide(nearBy).commit();
            showFragment(nearBy);
            */

          /*broadcastFragment = new BroadcastFragment();
            fm.beginTransaction().add(R.id.fragment_view, broadcastFragment, "2").hide(broadcastFragment).commit();
            showFragment(broadcastFragment);*/

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    enableLocationSettings();
                }
            } else {
                enableLocationSettings();
            }*/
            //startCountDown();
        } else {
            loadSearchFragement();
        }
    }

    public void maleOnCamMenu(View v) {
        unselectAllMenu();
        ((ImageView) findViewById(R.id.img_newMenuOnCam)).setImageResource(R.drawable.playbuttonselected);
        //((ImageView) findViewById(R.id.img_newMenuOnCam)).setVisibility(View.GONE);
        if (sessionManager.getGender().equals("male")) {
            //  onCamFragment = new OnCamFragment();
           /* nearbyMenuFragment = new NearbyMenuFragment();
            fm.beginTransaction().add(R.id.fragment_view, nearbyMenuFragment, "2").hide(nearbyMenuFragment).commit();*/
            gotoNearBy = true;
            /*nearbyMenuFragment = new NearbyMenuFragment();
            fm.beginTransaction().add(R.id.fragment_view, nearbyMenuFragment, "2").hide(nearbyMenuFragment).commit();*/
         /*   onCamFragment = new OnCamFragment();
            fm.beginTransaction().add(R.id.fragment_view, onCamFragment, "2").hide(onCamFragment).commit();
            showFragment(onCamFragment);*/

            /*broadcastFragment = new BroadcastFragment();
            fm.beginTransaction().add(R.id.fragment_view, broadcastFragment, "2").hide(broadcastFragment).commit();
            showFragment(broadcastFragment);*/

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    enableLocationSettings();
                }
            } else {
                enableLocationSettings();
            }*/
            //startCountDown();
        } else {
            loadSearchFragement();
        }
    }

    public void newHomeMenu(View v) {
        unselectAllMenu();
        ((ImageView) findViewById(R.id.img_newMenuHome)).setImageResource(R.mipmap.home_tab_on);
        ((LinearLayout) findViewById(R.id.cvbottom_navigation)).setBackgroundResource(R.color.tab_bg_color);
        //Toast.makeText(this, sessionManager.getGender(), Toast.LENGTH_SHORT).show();
        if (sessionManager.getGender().equals("male")) {
            showFragment(userMenuFragmentMet);

            //replaceFragment(new UserMenuFragmentMet(), "1");
            //showFragment(userMenuFragment);
            if (myCountDownTimer != null) {
                inCount = false;
                myCountDownTimer.cancel();
            }
            detachOncam();
            //userMenuFragment.onResume();
        }
    }

    private void unselectAllMenu() {
        ((ImageView) findViewById(R.id.img_newMenuHome)).setImageResource(R.mipmap.home_tab_off);
        ((ImageView) findViewById(R.id.nearBy)).setImageResource(R.drawable.ic_location);
        ((ImageView) findViewById(R.id.img_newMenuOnCam)).setImageResource(R.drawable.playbuttonunselect);
        //((ImageView) findViewById(R.id.img_newMenuOnCam)).setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.img_newMenuRecentRecharge)).setImageResource(R.drawable.recentrechargeunselect);
        ((ImageView) findViewById(R.id.img_newMenuMessage)).setImageResource(R.mipmap.message_tab_off);
        ((ImageView) findViewById(R.id.img_newMenuProfile)).setImageResource(R.mipmap.account_tab_off);
        gotoNearBy = false;
    }

    public void rankMenu(View v) {
        Toast.makeText(this, "Comming Soon", Toast.LENGTH_SHORT).show();
    }

    public void updateuserInfo() {
        recreate();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        /*if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }*/
    }

    public void checkLocationSatae() {
        Log.e("userCityLog", "in cust function");
        if (sessionManager.getUserAddress().equals("null")) {
            Log.e("userCityLog", "in condition");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    enableLocationSettings();
                }
            } else {
                enableLocationSettings();
            }
        }
    }

    public void loadCHatForUser(String channelName, String chatProfileId, String contactId, String profileName,
                                int unreadMsgCount, String user_image) {
        /*InboxDetailsFragment inboxDetailsFragment = new InboxDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("mode", true);
        bundle.putString("channelName", channelName);
        bundle.putString("chatProfileId", chatProfileId);
        bundle.putString("contactId", contactId);
        bundle.putString("profileName", profileName);
        bundle.putInt("usercount", 0);
        bundle.putInt("unreadMsgCount", unreadMsgCount);
        bundle.putString("user_image", user_image);
        inboxDetailsFragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_view, inboxDetailsFragment, "10").hide(inboxDetailsFragment).commit();
        showFragment(inboxDetailsFragment);*/
    }

    private class MyLocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            Location location = null;
            if (location != null) {
                /*Toast.makeText(MainActivity.this, Utils.getLocationText(location), Toast.LENGTH_SHORT).show();*/
                //LatLng myLat = new LatLng(20.389434, 72.830514);
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    // addresses = geocoder.getFromLocation(myLat.latitude, myLat.longitude, 1);

                    String countryName = addresses.get(0).getCountryName();
                    //  String stateName = addresses.get(0).getAdminArea();
                    String city_name = addresses.get(0).getLocality();
                    String oldcityName = sessionManager.getUserAddress();

                    sessionManager.setUserLocation(addresses.get(0).getCountryName());
                    sessionManager.setUserAddress(city_name);

                    Log.e("LocationDetection", "countryname " + countryName);
                    // Log.e("LocationDetection", "stateName " + stateName);
                    Log.e("LocationDetection", "cityName " + city_name);
                    //   Log.e("LocationDetection", "cityName session " + sessionManager.getUserAddress());

                    if (!oldcityName.equals(city_name)) {
                        //  Log.e("city_Sataus", "different city");
                        //apiManager.getUserListLastCall(city_name, String.valueOf(myLat.latitude), String.valueOf(myLat.longitude));
                        apiManager.getUserLatLonUpdated(countryName, city_name, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        // apiManager.getUserListNearby("1", "");

                        apiManager.getUserListWithLastCallLatest("1", "");
                    } else {
                        /*if (gotoNearBy) {
                            unselectAllMenu();
                            ((ImageView) findViewById(R.id.img_newMenuOnCam)).setImageResource(R.drawable.playbuttonselected);

                            nearbyMenuFragment = new NearbyMenuFragment();
                            fm.beginTransaction().add(R.id.fragment_view, nearbyMenuFragment, "2").hide(nearbyMenuFragment).commit();
                            showFragment(nearbyMenuFragment);
                        }*/
                    }
                    // mService.removeLocationUpdates();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void reginFirebase() {
        new FireBaseStatusManage(MainActivity.this, sessionManager.getUserId(), sessionManager.getUserName(),
                "", "Male", "Online");
    }
}