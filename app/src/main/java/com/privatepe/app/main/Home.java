package com.privatepe.app.main;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.faceunity.wrapper.faceunity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.privatepe.app.Firestatus.FireBaseStatusManage;
import com.privatepe.app.IM.IMOperations;
import com.privatepe.app.R;

//import com.privatepe.app.ZegoExpress.zim.ZimEventListener;
import com.privatepe.app.ZegoExpress.zim.ZimManager;
import com.privatepe.app.activity.SystemMsg;
import com.privatepe.app.agency.AgencyHomeFragment;
import com.privatepe.app.dialogs_agency.AddLibVideoDialog;
import com.privatepe.app.dialogs_agency.UpdateVersionDialog;
import com.privatepe.app.fragments.ProfileFragment;
import com.privatepe.app.fragments.UserMenuFragment;
import com.privatepe.app.fragments.gift.MsgFragment;
import com.privatepe.app.model.AppUpdate.UpdateResponse;
import com.privatepe.app.model.PriceList.PriceDataModel;
import com.privatepe.app.model.PriceListResponse;
import com.privatepe.app.model.ProfileDetailsResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.services.LocationUpdatesService;
import com.privatepe.app.sqlite.Chat;
import com.privatepe.app.sqlite.ChatDB;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.NetworkCheck;
import com.privatepe.app.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.entity.ZIMTextMessage;
import im.zego.zim.enums.ZIMConnectionEvent;
import im.zego.zim.enums.ZIMConnectionState;
import im.zego.zim.enums.ZIMErrorCode;
import im.zego.zim.enums.ZIMMessageType;
*/

public class Home extends BaseActivity implements ApiResponseInterface {
    private ImageView userImage;
    RelativeLayout profile;
    RelativeLayout home, msg;
    static public MutableLiveData<Boolean> inviteClosed=new MutableLiveData<Boolean>();
    static public LiveData<Boolean> inviteClosedIs=inviteClosed;
    FrameLayout frameLayout;
    public static TextView unread;
    public static CardView cardView;
    AgencyHomeFragment agencyHomeFragment = new AgencyHomeFragment();
    UserMenuFragment femaleHomeFragment = new UserMenuFragment();
    //FemaleHomeFragment femaleHomeFragment = new FemaleHomeFragment();
    MsgFragment msgFragment = new MsgFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    boolean doubleBackToExitPressedOnce = false;
    private SessionManager sessionManager;
    private String fcmToken;
    private ApiManager apiManager;
    private LocationUpdatesService mService = null;
    int REQUEST_CODE_CHECK_SETTINGS = 2021;
    private ImageView msgBox, homeView;
    private AppLifecycle appLifecycle;
    ProgressDialog pDialog;

    DatabaseReference chatRef;

    SharedPreferences sharedPreferences;

    HashMap<String, String> user;

    public ArrayList<PriceDataModel> priceDataModelArrayList = new ArrayList<>();
    private int SelectedChatPrice = 0, SelectedLevel = 0;
    private ZimManager zimManager;
    //  private ZimEventListener zimEventListener;
    private JSONObject MessageWithChatJson;
    private FragmentManager fm = getSupportFragmentManager();

    private Fragment active;

    String TAG = "Home";
    private String AppVersionCode;

    UpdateVersionDialog updateVersionDialog;
    AddLibVideoDialog addLibVideoDialog;

    public static native int fuSetup(byte[] v3data, byte[] authdata);


    Handler fHandler = new Handler();
    IMOperations imOperations;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //  WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.home);
        inviteClosed.setValue(false);

        Log.e("HomeCalled", "onCreate: ");

        /*if (authpack.A() != null) {
            FURenderer.initFURenderer(this);
        }*/
        initZim();
        NetworkCheck networkCheck = new NetworkCheck();
        sessionManager = new SessionManager(getApplicationContext());
        imOperations = new IMOperations(getApplicationContext());
        imOperations.loginIm(sessionManager.getUserId());
        apiManager = new ApiManager(getApplicationContext(), this);
        appLifecycle = new AppLifecycle();
        frameLayout = findViewById(R.id.flFragment);
        profile = findViewById(R.id.profile);
        msg = findViewById(R.id.msg);
        home = findViewById(R.id.explore);
        unread = findViewById(R.id.unreadAll);
        cardView = findViewById(R.id.bottomNavigationView);
        userImage = findViewById(R.id.userImage);
        msgBox = findViewById(R.id.msgBox);
        homeView = findViewById(R.id.homeView);
        cardView.setVisibility(View.VISIBLE);
        sharedPreferences = getSharedPreferences("VideoApp", Context.MODE_PRIVATE);


        apiManager.getCategoryGifts();

        //getUserStatusOnlineOfflineStatus();
        //storeFirebaseStatus();


        user = new SessionManager(this).getUserDetails();
        // loginZim(user.get(NAME), user.get(PROFILE_ID), user.get(PROFILE_PIC));


        new ApiManager(getApplicationContext(), this).getCallPriceList();


       /* TestDialog dialogFragment=new TestDialog();
        dialogFragment.show(getSupportFragmentManager(),"My TestDialog  Fragment");*/

        /*
        pDialog = new ProgressDialog(Home.this, R.style.MyTheme);
        pDialog.setMessage("There is a\nNEW version\n\nVideo connection is more stable\nUser experience is easier to use");
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        */


        String[] permissions;


        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        }


        getPermission(permissions);


        LoadAllFragments();


        if (getIntent().hasExtra("gotoSystemInbox")) {
            /*
            msgBox.setImageResource(R.drawable.message_selected);
            homeView.setImageResource(R.drawable.home_not_selected);
            showFragment(msgFragment);

            Intent openInboxIntent=new Intent("OPEN-SYSTEM-INBOX");
            openInboxIntent.putExtra("action","goto_system_inbox");
            sendBroadcast(openInboxIntent);
           */
            startActivity(new Intent(Home.this, SystemMsg.class));
        }


        //  new UpdateVersionDialog(Home.this);


        homeView.setImageResource(R.drawable.home_selected);
        //appLifecycle = new AppLifecycle();
        if (!sessionManager.getIsRtmLoggedIn()) {
            //  appLifecycle.loginRtm(sessionManager.getUserId());
            sessionManager.setIsRtmLoggedIn(true);
        }
        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            if (sessionManager.getRole().equals("4") || sessionManager.getRole().equals("5")) {
                // getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, agencyHomeFragment).commit();
                addFragment(agencyHomeFragment, "1");
                Log.e("userRoleLog", sessionManager.getRole());
                home.setVisibility(View.GONE);
            } else {
                //  getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, femaleHomeFragment).commit();
                // Log.e("userRoleLog", sessionManager.getRole());
                addFragment(femaleHomeFragment, "1");
            }

            fHandler.removeCallbacksAndMessages(null);
            fHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CheckAFragmentVisibleThenHideOthers();
                }
            }, 200);


        } else {
            Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_LONG).show();
        }


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //homeView.setBackgroundColor(Color.parseColor("#971BD7"));
                msgBox.setImageResource(R.drawable.message_not_selected);
                homeView.setImageResource(R.drawable.home_selected);
                // getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, femaleHomeFragment).commit();
                Log.e(TAG, "onClick: " + "Home Btn Click");

                showFragment(femaleHomeFragment);

                fHandler.removeCallbacksAndMessages(null);
                fHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CheckAFragmentVisibleThenHideOthers();
                    }
                }, 200);


            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgBox.setImageResource(R.drawable.message_selected);
                homeView.setImageResource(R.drawable.home_not_selected);
                // getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, msgFragment).commit();
                showFragment(msgFragment);


                fHandler.removeCallbacksAndMessages(null);
                fHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CheckAFragmentVisibleThenHideOthers();
                    }
                }, 200);


            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgBox.setImageResource(R.drawable.message_not_selected);
                homeView.setImageResource(R.drawable.home_not_selected);
                if (sessionManager.getRole().equals("4") || sessionManager.getRole().equals("5")) {
                    // getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, agencyHomeFragment).commit();
                    showFragment(agencyHomeFragment);
                    Log.e("userRoleLog", sessionManager.getRole());
                    home.setVisibility(View.GONE);
                } else {
                    // getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment).commit();
                    showFragment(profileFragment);
                }
                fHandler.removeCallbacksAndMessages(null);
                fHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CheckAFragmentVisibleThenHideOthers();
                    }
                }, 200);


            }
        });

        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            try {
                Glide.with(Home.this).load(sessionManager.getUserProfilepic()).placeholder(R.drawable.default_profile).into(userImage);
            } catch (Exception e) {

            }
        }

        /*//    if (sessionManager.getRole().equals("4") || sessionManager.getRole().equals("5")) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            fcmToken = instanceIdResult.getToken();
            Log.e("fcmToken", fcmToken);

            try {
                AppVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                Log.e(TAG, "fcmToken: app version " + AppVersionCode);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            apiManager.registerFcmToken("Bearer " + sessionManager.getUserToken(), fcmToken, AppVersionCode);
            // }
        });
        //  }*/

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


        try {
            ChatDB db = new ChatDB(getApplicationContext());
            List<Chat> peers = db.getAllPeer();


            if (peers.size() > 0) {
                int count = 0;


                count = count + db.getAllChatUnreadCount(peers.get(0).get_id());


                if (count > 0) {
                    unread.setVisibility(View.VISIBLE);
                    unread.setText(String.valueOf(count));
                } else {
                    unread.setVisibility(View.INVISIBLE);
                }
            }

        } catch (Exception e) {
        }

        //appLifecycle = new AppLifecycle();
        apiManager.getProfileDetails();
        //   addLibVideoDialog =new AddLibVideoDialog(Home.this);
        // sessionManager.setResUpload("0");
    }

    public void chatCount(int count) {
        if (count > 0) {
            unread.setVisibility(View.VISIBLE);
            unread.setText(String.valueOf(count));
            Log.e("CounterIncreament", "yes =>" + count);
        }
    }

    private void LoadAllFragments() {
        fm.beginTransaction().add(R.id.flFragment, msgFragment, "2").hide(msgFragment).commit();
        fm.beginTransaction().add(R.id.flFragment, profileFragment, "3").hide(profileFragment).commit();
        Log.e(TAG, "LoadAllFragments: " + " Load all fragments.");
    }


    private void addFragment(Fragment fragment, String tag) {

        if (fragment != null) {
            fm.beginTransaction().add(R.id.flFragment, fragment, "1").commit();
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


  /*  public void loginZim(String username, String userId, String userIcon) {
        Log.d("Home", "loginZim: ");
        String token = AuthInfoManager.getInstance().generateToken(userId);
    *//*    ZimManager.sharedInstance().loginZim(userId, username, userIcon, token, new ResultCallback() {
            @Override
            public void onZimCallback(ZIMErrorCode errorCode, String errMsg) {
                if (errorCode == ZIMErrorCode.SUCCESS) {
                    // Log.e("login", "success");
                    Log.e("Home", "onZimCallback: LoginZim " + "success");
                    Log.e("Home", "onZimCallback: " + "login success");
                } else {
                    //  Log.e("login", "fail " + errorCode);
                    Log.e("Home", "onZimCallback: LoginZim " + " Fail with ErrorCode " + errorCode);
                }
            }
        });*//*

    }*/

    private void initZim() {
        // zimManager = ZimManager.sharedInstance();
       /* zimEventListener = new ZimEventListener() {
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

                    JSONObject jsonObject = null;

                    try {
                        jsonObject = new JSONObject(txtMsg.message);

                        if (jsonObject.has("isMessageWithChat")) {

                            if (jsonObject.get("isMessageWithChat").toString().equals("yes")) {
                                MessageWithChatJson = new JSONObject(jsonObject.get("ChatMessageBody").toString());
                                if (MessageWithChatJson != null) {
                                    Log.e("MessageWithChatJson", MessageWithChatJson.toString());
                                    saveChatInDb(fromUserID, MessageWithChatJson.get("UserName").toString(), "", MessageWithChatJson.get("Message").toString(), MessageWithChatJson.get("Date").toString(), "", MessageWithChatJson.get("Time").toString(), MessageWithChatJson.get("ProfilePic").toString(), "TEXT");
                                    Intent intent = new Intent("USER-TEXT");
                                    intent.putExtra("peerId", fromUserID);
                                    intent.putExtra("msg", MessageWithChatJson.toString());
                                    sendBroadcast(intent);
                                }
                            }
                        } else if (jsonObject.has("isMessageWithChatGift")) {

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
                                        peerProfilePic, "GIFT");


                                Intent chatGiftIntent = new Intent("GIFT-USER-TEXT");
                                chatGiftIntent.putExtra("pos", giftPos);
                                chatGiftIntent.putExtra("peerId", fromUserID);
                                chatGiftIntent.putExtra("peerName", peerName);
                                chatGiftIntent.putExtra("peerProfilePic", peerProfilePic);
                                sendBroadcast(chatGiftIntent);
                            }

                        } //replaced isMessageWithChatGift with current response
                        *//*{"isMessageWithGift":"yes","GiftMessageBody":"{\"GiftPosition\":\"2\",\"UserName\":\"guest 769030503\",\"ProfilePic\":\"https:\\\/\\\/ringlive2022.oss-ap-south-1.aliyuncs.com\\\/ringliveProfileImages\\\/1.jpeg\",\"GiftData\":\"{\\\"amount\\\":3.0,\\\"animation_file\\\":\\\"https:\\\/\\\/ringlive2022.oss-ap-south-1.aliyuncs.com\\\/ringliveGiftImagesAnimation\\\/2022\\\/11\\\/16\\\/1668587108.svga\\\",\\\"gift_category_id\\\":2,\\\"gift_name\\\":\\\"Candy\\\",\\\"gift_type\\\":\\\"Normal\\\",\\\"id\\\":35,\\\"image\\\":\\\"https:\\\/\\\/ringlive2022.oss-ap-south-1.aliyuncs.com\\\/ringliveGiftImages\\\/2022\\\/11\\\/16\\\/1668587108.png\\\",\\\"is_animated\\\":0,\\\"sort_order\\\":0,\\\"status\\\":0}\"}"}*//*
                        else if (jsonObject.has("isMessageWithGift")) {

                            if (jsonObject.get("isMessageWithGift").toString().equals("yes")) {

//                                String giftPos = new JSONObject(jsonObject.get("GiftMessageBody").toString()).get("GiftPosition").toString();
                                String peerName = new JSONObject(jsonObject.get("GiftMessageBody").toString()).get("UserName").toString();
                                String peerProfilePic = new JSONObject(jsonObject.get("GiftMessageBody").toString()).get("ProfilePic").toString();
                                JSONObject giftMessageBody = new JSONObject(jsonObject.get("GiftMessageBody").toString());
                                JSONObject giftData = new JSONObject(giftMessageBody.get("GiftData").toString());
                                String giftId = String.valueOf(giftData.getInt("id"));
                                // Log.e("ChatGift", "Received  "+giftPos);

                                saveChatInDb(fromUserID,
                                        peerName,
                                        "",
                                        giftId,
                                        "",
                                        "",
                                        "",
                                        peerProfilePic, "GIFT");


                                Intent chatGiftIntent = new Intent("GIFT-USER-TEXT");
                                chatGiftIntent.putExtra("pos", giftId);
                                chatGiftIntent.putExtra("peerId", fromUserID);
                                chatGiftIntent.putExtra("peerName", peerName);
                                chatGiftIntent.putExtra("peerProfilePic", peerProfilePic);
                                sendBroadcast(chatGiftIntent);
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        };*/
        //  zimManager.addListener(zimEventListener);
    }


    private void saveChatInDb(String peerId, String name, String sentMsg, String recMsg, String date, String sentTime, String recTime, String image, String chatType) {
        ChatDB db = new ChatDB(this);
        String timesttamp = System.currentTimeMillis() + "";
        db.addChat(new Chat(peerId, name, "", recMsg, date, "", recTime, image, 0, timesttamp, chatType));

        Intent intent = new Intent("MSG-UPDATE");
        intent.putExtra("peerId", peerId);
        intent.putExtra("msg", "receive");
        sendBroadcast(intent);


        Log.e("recievemsggg", "savedChatInDB called");
        Log.e("MessageSavedInChat", "saved");

    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onResume() {
        super.onResume();
        Log.e("HomeCalled", "OnResume");

        CheckAFragmentVisibleThenHideOthers();

        // Log.e(TAG, "onResume: getCurrentFragment1 "+getCurrentFragment1() );
        Log.e(TAG, "onResume: getVisibleFragment " + getVisibleFragment());

        new FireBaseStatusManage(Home.this, sessionManager.getUserId(), sessionManager.getUserName(), "", "", "Online");

        // new UpdateVersionDialog(Home.this);

        if (updateVersionDialog == null) {
            Log.e("HomeCalled1", "if show dialog");
            updateVersionDialog = new UpdateVersionDialog(Home.this);
        } else {
            if (!updateVersionDialog.isShow()) {
                Log.e("HomeCalled1", "else show dialog");
                updateVersionDialog = new UpdateVersionDialog(Home.this);
            }
        }

        AppLifecycle.AppInBackground = false;


        if (cardView.getVisibility() == View.GONE) {
            cardView.setVisibility(View.VISIBLE);
        }


        try {

            ChatDB db = new ChatDB(getApplicationContext());
            List<Chat> peers = db.getAllPeer();
            Log.e("UnreadCount", "Home onResume  " + "PeerSize " + peers.size());
            if (peers.size() > 0) {
                int count = 0;
                count = count + db.getAllChatUnreadCount(peers.get(0).get_id());
                Log.e(TAG, "onResume: " + "count " + db.getAllChatUnreadCount(peers.get(0).get_id()));
                Log.e("UnreadCount", "Home onResume  " + "PeerSize " + peers.size() + " MsgCount " + count);
                if (count > 0) {
                    unread.setVisibility(View.VISIBLE);
                    unread.setText(String.valueOf(count));
                } else {
                    unread.setVisibility(View.INVISIBLE);
                }
            }


        } catch (Exception e) {
            Log.e(TAG, "onResume: Exception " + e.getMessage());
        }


        registerReceiver(getRecMsg, new IntentFilter("MSG-UPDATE"));

        registerReceiver(LogoutBroadFirebase, new IntentFilter("FirebaseReceiverBroad"));

    }


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


    private void LogoutUser() {
        new SessionManager(Home.this).logoutUser();
        apiManager.getUserLogout();
        finishAffinity();
    }


    public BroadcastReceiver getRecMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String peer_id = intent.getStringExtra("peerId");
            String msg = intent.getStringExtra("msg");
            if (msg.equals("receive")) {
                ChatDB db = new ChatDB(getApplicationContext());
                List<Chat> peers = db.getAllPeer();
                Log.e("UnreadCount", "getRecMsg  " + "PeerSize " + peers.size());

                if (peers.size() > 0) {
                    int countAll = 0;
                    countAll = countAll + db.getAllChatUnreadCount(peers.get(0).get_id());
                    if (countAll > 0) {
                        unread.setVisibility(View.VISIBLE);
                        unread.setText(String.valueOf(countAll));
                        Log.e("UnreadCount", "getRecMsg  " + "PeerSize " + peers.size() + "  MsgCount " + countAll);
                    } else {
                        unread.setVisibility(View.INVISIBLE);
                    }

                }
            }
        }
    };

    @Override
    public void onBackPressed() {

        //Fragment f = getSupportFragmentManager().findFragmentById(R.id.flFragment);

        Fragment f = active;

        if (f instanceof ProfileFragment || f instanceof MsgFragment) {
            msgBox.setImageResource(R.drawable.message_not_selected);
            homeView.setImageResource(R.drawable.home_selected);
            showFragment(femaleHomeFragment);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Back press Again to exit.", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3000);
            //getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();

        }
    }

    @Override
    public void onDestroy() {
        if (new SessionManager(getApplicationContext()).getHostAutopickup().equals("yes")) {
            Intent myIntent = new Intent("KAL-CLOSEME");
            myIntent.putExtra("action", "closeme");
            this.sendBroadcast(myIntent);
        }
        /*    zimManager.removeListener(zimEventListener);*/
        unregisterReceiver(getRecMsg);
        unregisterReceiver(LogoutBroadFirebase);
        super.onDestroy();
    }

    @Override
    public void isError(String errorCode) {


    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.PROFILE_DETAILS) {
            ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;

            if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {

                System.out.println("" + rsp.getSuccess().getProfile_images().get(0).getImage_name());
                Log.d("profilePicLog", "isSuccess: " + rsp.getSuccess().getProfile_images().get(0).getImage_name());


                //     Toast.makeText(getApplicationContext(), "hiii " + rsp.getSuccess().getProfile_images().get(0).getImage_name(), Toast.LENGTH_SHORT);

                new SessionManager(getApplicationContext()).setUserProfilepic(rsp.getSuccess().getProfile_images().get(0).getImage_name());

                Log.e("profilePicLog", new SessionManager(this).getUserProfilepic());

            }
        }
        if (ServiceCode == Constant.REGISTER_FCM_TOKEN) {
            sessionManager.saveFcmToken(fcmToken);
        }
        if (ServiceCode == Constant.GET_MAINTAINENCE_DATA) {
            UpdateResponse updateResponse = (UpdateResponse) response;
            /* if (!updateResponse.getResult().getApp_version().equals("")) {
                try {
                    float versionCode = Float.parseFloat(getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName);
                    //  int versionCode = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
                    if (Float.parseFloat(updateResponse.getResult().getApp_version()) > versionCode) {
                        final DownloadApkFileFromURL downloadTask = new DownloadApkFileFromURL(getApplicationContext());
                        downloadTask.execute(updateResponse.getResult().getApp_apk());

                        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true); //cancel the task
                            }
                        });
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }*/
        }


        /*if (ServiceCode == Constant.CALL_PRICE_LIST) {
            priceDataModelArrayList.clear();

            PriceListResponse rsp = (PriceListResponse) response;
            priceDataModelArrayList.addAll(rsp.getResult());
            SelectedChatPrice = rsp.getCall_rate();
            SelectedLevel = rsp.getLevel();

            sharedPreferences.edit().putInt("SelectedPrice", SelectedChatPrice).apply();

            //  PriceText.setText("Price: "+SelectedChatPrice+"/min");

            Log.i("size_price", "" + priceDataModelArrayList.size());
            //   Toast.makeText(getContext(),"size_price    "+priceDataModelArrayList.get(0).getAmount(),Toast.LENGTH_SHORT).show();

        }*/


    }









    /* class DownloadApkFileFromURL extends AsyncTask<String, Integer, String> {
     *//**
     * Before starting background thread
     *//*
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadApkFileFromURL(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgress(progress[0]);
        }

        *//**
     * Downloading file in background thread
     *//*
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                URL url = new URL(f_url[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                //delete existing apk file.
                File filePath = new File(root + "/KLiveRelease.apk");
                if (filePath.exists())
                    filePath.delete();

                connection.connect();
                input = new BufferedInputStream(url.openStream(), 8192);
                output = new FileOutputStream(root + "/KLiveRelease.apk");

                byte data[] = new byte[4096];
                long total = 0;
                //int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        */

    /**
     * After completing background task
     *
     * @param permissions
     *//*
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            mWakeLock.release();
         *//*   try {
                Intent installApplicationIntent = new Intent(Intent.ACTION_VIEW);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "KLiveRelease.apk");
                if (file.exists()) {
                    file.setReadable(true);
                    installApplicationIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            file), "application/vnd.android.package-archive");
                } else {
                    // DisplayUtils.getInstance().displayLog(TAG, "file not found after downloading");
                }
                installApplicationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                installApplicationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installApplicationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getApplicationContext().startActivity(installApplicationIntent);
                // ExitActivity.exitApplicationAnRemoveFromRecent(getApplicationContext());
            } catch (Exception cv) {
            }*//*
            //update apk code by sanjay
            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    Intent installApplicationIntent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "KLiveRelease.apk");
                    if (file.exists()) {
                        file.setReadable(true);
                        installApplicationIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(),
                                BuildConfig.APPLICATION_ID + ".provider",
                                file), "application/vnd.android.package-archive");
                    } else {
                        // DisplayUtils.getInstance().displayLog(TAG, "file not found after downloading");
                    }
                    installApplicationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    installApplicationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    installApplicationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getApplicationContext().startActivity(installApplicationIntent);
                    // ExitActivity.exitApplicationAnRemoveFromRecent(getApplicationContext());
                } catch (Exception cv) {
                }
            } else
                //end update apk code
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "KLiveRelease.apk")));
                } catch (Exception e) {
                   *//* Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "KLiveRelease.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);*//*


                    try {
                        String PATH = Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath();
                        File file = new File(PATH + "/KLiveRelease.apk");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= 24) {
                            Uri downloaded_apk = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                            intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo resolveInfo : resInfoList) {
                                context.grantUriPermission(context.getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        } else {
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        startActivity(intent);
                    } catch (Exception eq) {
                        eq.printStackTrace();
                    }

                }
        }
    }*/
    private void getPermission(String[] permissions) {
        Log.e(TAG, "getPermission: permissions " + permissions.length);


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

                                //enableLocationSettings();
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


    public void CheckAFragmentVisibleThenHideOthers() {

        Log.e(TAG, "CheckAFragmentVisibleThenHideOthers: ");

        SessionManager sessionManager = new SessionManager(Home.this);

        String role = sessionManager.getRole();

        Log.e(TAG, "CheckAFragmentVisibleThenHideOthers: role " + role);

        if (role.equals("4") || role.equals("5")) {

            if (agencyHomeFragment.isVisible() && agencyHomeFragment.isAdded()) {
                Log.e(TAG, "CheckAFragmentVisibleThenHideOthers: agencyHomeFragment is visible ");
                fm.beginTransaction().hide(msgFragment);

            } else if (msgFragment.isVisible() && msgFragment.isAdded()) {
                Log.e(TAG, "CheckAFragmentVisibleThenHideOthers: msgFragment is visible ");
                fm.beginTransaction().hide(agencyHomeFragment);

            }/*else {
            Log.e(TAG, "CheckAFragmentVisibleThenHideOther: no fragment visible");
        }*/


        } else {

            if (femaleHomeFragment.isVisible() && femaleHomeFragment.isAdded()) {
                Log.e(TAG, "CheckAFragmentVisibleThenHideOthers:  femaleHomeFragment is visible");

                fm.beginTransaction().hide(msgFragment);
                fm.beginTransaction().hide(profileFragment);

            } else if (msgFragment.isVisible() && msgFragment.isAdded()) {
                Log.e(TAG, "CheckAFragmentVisibleThenHideOthers:  msgFragment is visible ");

                fm.beginTransaction().hide(femaleHomeFragment);
                fm.beginTransaction().hide(profileFragment);


            } else if (profileFragment.isVisible() && profileFragment.isAdded()) {
                Log.e(TAG, "CheckAFragmentVisibleThenHideOthers:  profileFragment is visible ");

                fm.beginTransaction().hide(femaleHomeFragment);
                fm.beginTransaction().hide(msgFragment);


            } /*else {
            Log.e(TAG, "CheckAFragmentVisibleThenHideOther: no fragment visible");
        }*/

        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }


    public Fragment getCurrentFragment1() {
        return getSupportFragmentManager().findFragmentById(R.id.flFragment);

    }

   /*
   private String getCurrentVisibleFragment() {
        Fragment currentFrag=getSupportFragmentManager().findFragmentByTag("1");
        return null;
    }
  */


    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        return currentFragment;
    }


    private Fragment getActiveFrag() {
        Fragment fragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
        return fragment;
    }


    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void hideStatusBar(Window window, boolean darkText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && darkText) {
                    flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                window.getDecorView().setSystemUiVisibility(flag | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }, 10);

    }
}