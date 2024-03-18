package com.privatepe.host.Inbox;


import static com.privatepe.host.utils.Constant.GET_DATA_FROM_PROFILE_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.privatepe.host.IM.IMOperations;
import com.privatepe.host.R;
import com.privatepe.host.Zego.VideoChatZegoActivityMet;
import com.privatepe.host.activity.ProfileImagesView;
import com.privatepe.host.activity.ViewProfileMet;
import com.privatepe.host.adapter.GiftAnimationRecyclerAdapter;
import com.privatepe.host.dialogs.InsufficientCoins;
import com.privatepe.host.extras.MessageCallDataRequest;
import com.privatepe.host.model.gift.GiftAnimData;
import com.privatepe.host.model.gift.SendGiftResult;
import com.privatepe.host.response.DataFromProfileId.DataFromProfileIdResponse;
import com.privatepe.host.response.DataFromProfileId.DataFromProfileIdResult;
import com.privatepe.host.response.metend.AdapterRes.UserListResponseMet;
import com.privatepe.host.response.metend.DiscountedRecharge.DiscountedRechargeResponse;
import com.privatepe.host.response.metend.GenerateCallResponce.GenerateCallResponce;
import com.privatepe.host.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.privatepe.host.response.metend.UserListResponseNew.FemaleImage;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.AppLifecycle;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.SessionManager;
import com.squareup.picasso.Picasso;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMFriendAddApplication;
import com.tencent.imsdk.v2.V2TIMFriendCheckResult;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMFriendOperationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSignalingManager;
import com.tencent.imsdk.v2.V2TIMUserStatus;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.zelory.compressor.Compressor;
//import im.zego.zim.enums.ZIMErrorCode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class InboxDetails extends AppCompatActivity implements ApiResponseInterface {

    Activity mActivity;

    public static RecyclerView rv_chat, rv_select_gifts;

    TextView tv_name;
    private EditText mMessageView;
    SwipeRefreshLayout swipeRefreshLayout;
    public static List<Messages> messagesList = new ArrayList<>();

    RelativeLayout rl_gift;

    ImageView iv_gift;

    boolean flag = true;

    String receiverUserId;
    String receiverName;
    // String chatId;
    ValueEventListener listener;

    private LinearLayoutManager mLinearLayoutManager;
    // private PersonalChatAdapter mMessageAdapter;
    public static MessageAdapter mMessageAdapter;

    boolean notify = false;
    String currentUserId, currentUserName, receiverImage, userid, newParem;
    DatabaseReference rootRef;
    private boolean passMessage = false;
    private boolean isChatActive = true;


    private String channelName = "";
    //private final String myProfileId = "";
    public static String chatProfileId = "";
    public static String contactId = "";
    private boolean stateSingleMode = true; // single mode or channel mode
    private int channelUserCount;
    private int unreadMsgCount;
    private int userGiftCount = 0;
    private String profileName = "";
    //  private final String imgUrl = "https://www.goodmorninghdloveimages.com/wp-content/uploads/2020/04/Always-be-Happy-Whatsapp-Dp-Cute-Profile-Images.png";

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    public static DatabaseHandler dbHandler;
    private final int chatLoadLimit = 10;
    private List<MessageBean> messageBeanList;
    public static List<MessageBean> chatMessageList;
    private boolean fromNotification;
    SessionManager sessionManager;
    ImageView loader;
    private String chatProfileImage = "";
    int purchasePlanStatus = 1;
    private String firebaseOnlineStatus = "", firebaseFCMToken = "";


    SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy");
    //private Rtm rtm;
    HashMap<String, String> user;
    AppLifecycle appLifecycle;
    private RecyclerView giftAnimRecycler;
    ArrayList<GiftAnimData> giftdataList = new ArrayList<GiftAnimData>();
    private boolean itemRemoved = false;
    //   private ZimManager zimManager;
    private boolean isFreeCall = false;
    private String host_userId, level;


    private DatabaseReference firebaseRef;

    ImageView msgLoader;
    private ConstraintLayout rechargeFirst_ll;
    private LottieAnimationView lvRecharge;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_details);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        dbHandler = new DatabaseHandler(this);
        loader = findViewById(R.id.img_giftloader);

        appLifecycle = new AppLifecycle();
        findViewById(R.id.img_send).setClickable(true);
        rechargeFirst_ll = findViewById(R.id.rechargeFirst_ll);

        //  zimManager = ZimManager.sharedInstance();

        currentUserId = String.valueOf(new SessionManager(getApplicationContext()).getUserId());
        currentUserName = new SessionManager(getApplicationContext()).getUserName();
        Glide.with(getApplicationContext()).load(R.drawable.loader).into(loader);
        getinData();

        init();

        String token = new SessionManager(getApplicationContext()).getUserToken();
        String token1 = new SessionManager(getApplicationContext()).getUserToken();
         /* Intent myIntent = new Intent("KAL-CLOSEME");
            myIntent.putExtra("action", "resetEngine");
            this.sendBroadcast(myIntent);*/
        //apiManager.chatController();
        Log.e("chatProfileIdLog", chatProfileId);
        firebaseOperation();
        getChatData();
        Log.e("naval===", new SessionManager(getApplicationContext()).getRole() + "===" + new SessionManager(getApplicationContext()).getGender());
        if (new SessionManager(getApplicationContext()).getGender().equalsIgnoreCase("female")) {
            rechargeFirst_ll.setVisibility(View.GONE);
        } else {
            // rechargeFirst_ll.setVisibility(View.VISIBLE);
        }
        ((ImageView) findViewById(R.id.img_video_call)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusCheck();
            }
        });

        if (sessionManager.getFirstTimeRecharged().equals("0")) {
            lvRecharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lvRecharge.setEnabled(false);
                    insufficientCoins = new InsufficientCoins(InboxDetails.this, 2, Integer.parseInt(callRate));
                    mMessageView.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lvRecharge.setEnabled(true);
                        }
                    }, 1000);
                }
            });
        } else {
            rechargeFirst_ll.setVisibility(View.GONE);
        }
    }

    private void statusCheck() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(receiverUserId).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.e("chejadsfa", snapshot.getValue(String.class));
                    if ("Online".equalsIgnoreCase(snapshot.getValue(String.class))) {
                        Log.e("chejadsfa", snapshot.getValue(String.class));

                        callType = "video";
                        //apiManager.getRemainingGiftCardFunction();
                        apiManager.generateCallRequestZ(Integer.parseInt(host_userId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                    } else {
                        Toast.makeText(InboxDetails.this, "User is not Online", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    Intent intentExtendedProfile;
    List<FemaleImage> femaleImageList;

    public void showImage(String imageURL) {

        femaleImageList=new ArrayList<>();

        FemaleImage femaleImage=new FemaleImage();
        femaleImage.setUserId(Integer.valueOf(receiverUserId));
        femaleImage.setIsProfileImage(0);
        femaleImage.setImageName(imageURL);

        femaleImageList.add(femaleImage);
        intentExtendedProfile = new Intent(InboxDetails.this, ProfileImagesView.class);
        intentExtendedProfile.putParcelableArrayListExtra("femaleImageList", (ArrayList<? extends Parcelable>) femaleImageList);

        intentExtendedProfile.putExtra("positionOnDisplay", 0);
        startActivity(intentExtendedProfile);

    }
    DatabaseReference userDBRef;
    ValueEventListener valueEventListener;

    private void firebaseOperation() {

        rootRef = FirebaseDatabase.getInstance().getReference();

        currentUserId = String.valueOf(new SessionManager(getApplicationContext()).getUserId());
        currentUserName = new SessionManager(getApplicationContext()).getUserName();

        try {

            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
            userDBRef = mFirebaseInstance.getReference("Users/" + chatProfileId);

            valueEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        Log.e("FirebaseRealTimeDB user", snapshot.exists() + "");
                        if (snapshot.exists()) {
                            FirebaseUserModel userModel = snapshot.getValue(FirebaseUserModel.class);
                            String status = userModel.getStatus();
                            Log.e("FirebaseRealTimeDB", "status=" + status);
                            firebaseFCMToken = userModel.getFcmToken();

                            if (status.equalsIgnoreCase("Live")) {
                                Log.e("firebaseOnlineStatus", "onDataChange: " + status);
                                firebaseOnlineStatus = "Online";
                                ((TextView) findViewById(R.id.tv_userstatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_onlinestatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorGreen));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);
                                return;
                            } else if (status.equalsIgnoreCase("Online")) {
//                            Log.e("userFCMOnline", userModel.getFcmToken());
                                Log.e("firebaseOnlineStatus", "onDataChange: " + status);
                                firebaseOnlineStatus = "Online";
                                ((TextView) findViewById(R.id.tv_userstatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_onlinestatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorGreen));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);
                            } else if (status.equalsIgnoreCase("Busy")) {
                                Log.e("firebaseOnlineStatus", "onDataChange: " + status);
                                firebaseOnlineStatus = "Busy";
                                ((TextView) findViewById(R.id.tv_onlinestatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorBusy));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);
                            } else {
                                Log.e("firebaseOnlineStatus", "onDataChange: " + status);
//                            Log.e("userFCMOffline", userModel.getFcmToken());
                                firebaseOnlineStatus = "Offline";
//                              firebaseFCMToken = userModel.getFcmToken();
                                ((TextView) findViewById(R.id.tv_userstatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_onlinestatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorRedoffline));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offline, 0, 0, 0);
                            }
                            //    userDBRef.removeEventListener(this);
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            userDBRef.addValueEventListener(valueEventListener);

            /*userDBRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Log.e("FirebaseRealTimeDB user", dataSnapshot.exists() + "");
                        if (dataSnapshot.exists()) {
                            FirebaseUserModel userModel = dataSnapshot.getValue(FirebaseUserModel.class);
                            String status = userModel.getStatus();
                            Log.e("FirebaseRealTimeDB", "status=" + status);
                            firebaseFCMToken = userModel.getFcmToken();

                            if (status.equalsIgnoreCase("Live")) {
                                Log.e("firebaseOnlineStatus", "onDataChange: " + status);
                                firebaseOnlineStatus = "Online";
                                ((TextView) findViewById(R.id.tv_userstatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_onlinestatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorGreen));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);
                                return;
                            } else if (status.equalsIgnoreCase("Online")) {
//                            Log.e("userFCMOnline", userModel.getFcmToken());
                                Log.e("firebaseOnlineStatus", "onDataChange: " + status);
                                firebaseOnlineStatus = "Online";
                                ((TextView) findViewById(R.id.tv_userstatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_onlinestatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorGreen));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);
                            } else if (status.equalsIgnoreCase("Busy")) {
                                Log.e("firebaseOnlineStatus", "onDataChange: " + status);
                                firebaseOnlineStatus = "Busy";
                                ((TextView) findViewById(R.id.tv_onlinestatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorBusy));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);
                            } else {
                                Log.e("firebaseOnlineStatus", "onDataChange: " + status);
//                            Log.e("userFCMOffline", userModel.getFcmToken());
                                firebaseOnlineStatus = "Offline";
//                              firebaseFCMToken = userModel.getFcmToken();
                                ((TextView) findViewById(R.id.tv_userstatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_onlinestatus)).setText(firebaseOnlineStatus);
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorRedoffline));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offline, 0, 0, 0);
                            }
                            //    userDBRef.removeEventListener(this);
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseRealTimeDB Fail", databaseError + "");
                }
            });*/
        } catch (Exception ex) {
            //
        }

    }

    private void getinData() {
    /*    Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            receiverUserId = bundle.getString("user_id");
            receiverName = bundle.getString("user_name");

            Glide.with(this)
                    .load(bundle.getString("user_image"))
                    .into(((ImageView) findViewById(R.id.profile_image)));
        }
    */
        Intent intent = getIntent();
        channelName = intent.getStringExtra("channelName");
        chatProfileId = intent.getStringExtra("chatProfileId");
        receiverUserId = intent.getStringExtra("chatProfileId");
        if (intent.hasExtra("contactId")) {
            contactId = intent.getStringExtra("contactId");
        }
        if (receiverUserId.equals("1")) {
            //  ((ImageView) findViewById(R.id.img_gift)).setVisibility(View.GONE);
            // ((ImageView) findViewById(R.id.giftbtn)).setVisibility(View.GONE);
            //findViewById(R.id.rl_giftin1).setVisibility(View.GONE);
            ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
        }
        profileName = intent.getStringExtra("profileName");
        receiverName = intent.getStringExtra("profileName");
        stateSingleMode = intent.getBooleanExtra("mode", true);
        channelUserCount = intent.getIntExtra("usercount", 0);
        unreadMsgCount = intent.getIntExtra("unreadMsgCount", 0);
        fromNotification = intent.getBooleanExtra("fromNotification", false);
        // textViewTitle.setText(channelName + "(" + channelUserCount + ")");
        chatProfileImage = intent.getStringExtra("user_image");
        receiverImage = intent.getStringExtra("user_image");

        if(profileName.equalsIgnoreCase("System Message")){
            rechargeFirst_ll.setVisibility(View.GONE);
        }

        if (intent.hasExtra("userGiftCount"))
            userGiftCount = intent.getIntExtra("userGiftCount", 0);
        else {
            String userGiftValue = dbHandler.getContactGiftCount(chatProfileId, currentUserId);
            if (null != userGiftValue && !userGiftValue.equals("NULL")) {
                userGiftCount = Integer.parseInt(userGiftValue);
            }
        }

        level = intent.getStringExtra("userId");
        host_userId = intent.getStringExtra("userId");


        Picasso.get().load(intent.getStringExtra("user_image")).placeholder(R.drawable.default_profile).into(((ImageView) findViewById(R.id.img_profile)));
        rv_chat = findViewById(R.id.chat_recyclerview);

        contactId = "";
        if (TextUtils.isEmpty(contactId)) {
            UserInfo contactInfo = dbHandler.getContactInfo(chatProfileId, currentUserId);
            if (contactInfo != null) {
                contactId = contactInfo.getId();
                //Log.e("chatDisplayLog", "get contact from db => " + contactId);
            }
        }


        //Log.e("chatDisplayLog", "getinData: id " + receiverUserId + " contactid  " + contactId + "  id session manager ");


        findViewById(R.id.profileRV).setOnClickListener(view -> {

         /*  Intent viewProfileIntent=new Intent(InboxDetails.this,ViewProfile.class);
           Bundle bundle=new Bundle();
           bundle.putSerializable("id", host_userId);
           bundle.putSerializable("profileId",receiverUserId);
           bundle.putSerializable("level", level);
           viewProfileIntent.putExtras(bundle);
           startActivity(viewProfileIntent);*/

            apiManager.getProfileIdData(receiverUserId);


        });


    }

    ApiManager apiManager;

    int MsgLoaderOffset = 15;
    private boolean canHostChat = true;
    ImageView img_fullImage;

    @SuppressLint("WrongConstant")
    private void init() {


        mActivity = this;
        apiManager = new ApiManager(getApplicationContext(), this);
        sessionManager = new SessionManager(this);
        //apiManager.checkFirstTimeRechargeDone();


        searchWordList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.searchWordsArray)));

        rv_select_gifts = findViewById(R.id.rv_gift);
        rl_gift = findViewById(R.id.rl_gift);
        tv_name = findViewById(R.id.tv_username);
        mMessageView = findViewById(R.id.et_message);
        img_fullImage = findViewById(R.id.img_fullImage);
        giftAnimRecycler = findViewById(R.id.gift_animation_recyclerview);

//        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mLinearLayoutManager.setStackFromEnd(true);
        rv_chat.setLayoutManager(mLinearLayoutManager);
        mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);

        lvRecharge = findViewById(R.id.lvRecharge);


//        initScrollListner();
//        mMessageAdapter = new PersonalChatAdapter(this, messagesList, receiverUserId);
//        mMessageAdapter = new MessageAdapter(this, chatMessageList);
//        rv_chat.setLayoutManager(mLinearLayoutManager);
//        rv_chat.setAdapter(mMessageAdapter);
//        rv_chat.scrollToPosition(unreadMsgCount);

        // Worker thread is created to do db operations.

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            messageBeanList = new ArrayList<>();
            chatMessageList = new ArrayList<>();
            if (!TextUtils.isEmpty(contactId)) {
                //byabhishek
                // List<MessageBean> messageListBeans = dbHandler.getChatAllList(contactId);


                List<MessageBean> messageListBeans = dbHandler.getChatListWithLimit(contactId, 0, MsgLoaderOffset);


                // List<MessageBean> msgb = dbHandler.getChatListWithLimit(contactId, 0, 5);
                //  Log.e("INBOX_MSG_TEST", "init: msgb size "+msgb.size() );
                //  Log.e("INBOX_MSG_TEST", "init: "+dbHandler.getChatListWithLimit(contactId,0,10));

                Log.e("INBOX_MSG_TEST", "init: messageListBeans.size()  " + messageListBeans.size());
                // Log.e("INBOX_MSG_TEST", "getinData: " + new Gson().toJson(messageListBeans));
                if (!messageListBeans.isEmpty()) {
                    messageBeanList.addAll(messageListBeans);
                    convertChatListWithHeader();
                }
            }
            //end
            //Log.e("ChatList", "messageBeanList size="+messageBeanList.size());
            String msg = "";
            for (int i = 0; i < messageBeanList.size(); i++) {
                MessageBean messageBean = messageBeanList.get(i);
                Messages messages = messageBean.getMessage();
                //Log.e("ChatList", "message= "+messages);
                msg = " =>" + messages.getMessage() + "(" + messages.getType() + ") " + "\n " + " ";
                Log.e("INBOX_MSG_TEST1", "msg " + i + "  " + msg);
            }


            handler.post(() -> {
                //UI Thread work here
                //setup chat adapter
                Log.e("INBOX_MSG_TEST", "init: chatMessageList.size() " + chatMessageList.size());
                mMessageAdapter = new MessageAdapter(this, chatMessageList, InboxDetails.this);
                rv_chat.setAdapter(mMessageAdapter);
                rv_chat.scrollToPosition(unreadMsgCount);
                //end setup chat adapter
            });
        });

        MessageLoaderOnScroll();


        if (new SessionManager(getApplicationContext()).getGender().equals("male")) {
            MessageCallDataRequest messageCallDataRequest = new MessageCallDataRequest(receiverUserId);
            //apiManager.getMessageCallDataFunction(messageCallDataRequest);
            //((ImageView) findViewById(R.id.img_video_call)).setVisibility(View.VISIBLE);

            //    apiManager.getWalletAmount();
            // apiManager.searchUser(String.valueOf(receiverUserId), "1");
            if (!receiverUserId.equals("1")) {
                purchasePlanStatus = 1;
                //apiManager.isChatServicePurchased();
            } else {
                ((LottieAnimationView) findViewById(R.id.animation_loading)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.img_video_call)).setVisibility(View.GONE);
/*
                ((LottieAnimationView) findViewById(R.id.animation_loading)).setVisibility(View.GONE);
                ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.img_csshare)).setVisibility(View.VISIBLE);
*/
            }
        } else {
            purchasePlanStatus = 1;

            ((ImageView) findViewById(R.id.img_video_call)).setVisibility(View.GONE);
            ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);

            if (receiverUserId.equals("1")) {
                ((LottieAnimationView) findViewById(R.id.animation_loading)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.img_csshare)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
            }
        }

        /*
        if (!new SessionManager(getApplicationContext()).getGender().equals("male")) {
            ((ImageView) findViewById(R.id.img_video_call)).setVisibility(View.GONE);
        }
        */

        ((ImageView) findViewById(R.id.img_video_call)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CallProcess", "openVideoChat: InboxDetail " + "call btn clicked");


                if (CheckPermission()) {

                    // apiManager.getRemainingGiftCardFunction();

                    ((ImageView) findViewById(R.id.img_video_call)).setEnabled(false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) findViewById(R.id.img_video_call)).setEnabled(true);
                        }
                    }, 2000);
                } else {
                    //  Toast.makeText(InboxDetails.this,"To Make a call Camera and Audio permission must.Go to setting to allow the permissions",Toast.LENGTH_SHORT).show();

                }


            }


        });
/*
        swipeRefreshLayout = findViewById(R.id.message_swipe_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
*/

        //  swipeRefreshLayout.setRefreshing(true);

        tv_name.setText(receiverName);
        mMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("LongLogTag")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //sendMessage("text", "", "");
                if (sessionManager.getGender().equals("male")) {
                    if (sessionManager.getUserWallet() > 10) {
                        sendMessage("text", "", "");
                    } else {
                        //giftEmployeeBottomSheet = new GiftEmployeeBottomSheet(InboxDetails.this, receiverUserId, receiverImage, receiverName, callRate);
                        new InsufficientCoins(InboxDetails.this, 2, Integer.parseInt(callRate));

                    }
                } else {
                    if (canHostChat) {
                        sendMessage("text", "", "");
                    } else {
                        // sendMessage("text", "", "");
                        Toast.makeText(getApplicationContext(), "You can messages after reaching 5000 coins", Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });



     /*   ((SimpleItemAnimator)giftAnimRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
          giftAnimRecycler.setItemAnimator(null);*/
        //    giftAnimRecycler.getItemAnimator().endAnimations();
          /*
        RecyclerView.ItemAnimator animator = giftAnimRecycler.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        */

        //  giftAnimRecycler.setHasFixedSize(true);

        giftAnimRecycler.setLayoutManager(new LinearLayoutManager(this));

        giftAnimationRecyclerAdapter = new GiftAnimationRecyclerAdapter(giftdataList, getApplicationContext(), new GiftAnimationRecyclerAdapter.OnItemInvisibleListener() {
            @Override
            public void onItemInvisible(int adapterpos) {
             /*   Log.e("giftListSize", "_before " + giftdataList.size());

                if (giftdataList.size() > 0) {
                    giftdataList.remove(0);
                    giftAnimationRecyclerAdapter.notifyItemRemoved(0);
                }
           */
                // Log.e("listSize","adapterPos=> "+adapterpos+"ListSize=> "+adapterpos);
            }
        });

        giftAnimRecycler.setAdapter(giftAnimationRecyclerAdapter);
    }

    private boolean isLoaderDisable = false;

    private void MessageLoaderOnScroll() {

        rv_chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) //check for scroll top
                {
                    visibleItemCount = mLinearLayoutManager.getChildCount();
                    totalItemCount = mLinearLayoutManager.getItemCount();
                    pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                            loading = false;

                            showMessageLoader();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    List<MessageBean> messageListBeans = dbHandler.getChatListWithLimit(contactId, messageBeanList.size(), MsgLoaderOffset);
                                    if (!messageListBeans.isEmpty()) {
                                        messageBeanList.addAll(messageListBeans);
                                        convertChatListWithHeader();
                                    }
                                    mMessageAdapter.notifyDataSetChanged();
                                    hideMessageLoader();
                                    loading = true;
                                }
                            }, 100);
                        }
                    }
                }
            }


        });
    }

    private void showMessageLoader() {
        msgLoader = findViewById(R.id.msg_loader);
        msgLoader.setVisibility(View.VISIBLE);
        Glide.with(this).load(R.drawable.msg_loading).into(msgLoader);
    }

    private void hideMessageLoader() {
        msgLoader.setVisibility(View.INVISIBLE);
    }

    GiftAnimationRecyclerAdapter giftAnimationRecyclerAdapter;


    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMsgFun(View v) {

        if (sessionManager.getGender().equals("male")) {
            if (sessionManager.getUserWallet() > 10) {
                sendMessage("text", "", "");
            } else {
                //  giftEmployeeBottomSheet = new GiftEmployeeBottomSheet(InboxDetails.this, receiverUserId, receiverImage, receiverName, callRate);
                new InsufficientCoins(InboxDetails.this, 2, Integer.parseInt(callRate));

            }
        } else {
            if (canHostChat) {
                sendMessage("text", "", "");
            } else {
                // sendMessage("text", "", "");
                Toast.makeText(getApplicationContext(), "You can messages after reaching 5000 coins", Toast.LENGTH_SHORT).show();
            }
        }

        if (purchasePlanStatus == 1)/*)*/ {
            //sendMessage("text", "", "");
        } else {
            //todo adding new condition as new requirement
            // new GiftEmployeeBottomSheet(InboxDetails.this, receiverUserId ,receiverImage,receiverName);
            /*insufficientCoins = new InsufficientCoins(InboxDetails.this, 2, Integer.parseInt(callRate));
            Log.e("InsufficientCoins_InboxDetails", "sendMsgFun: ");

            insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    apiManager.checkFirstTimeRechargeDone();
                }
            });
            */
        }
    }


    private List<String> searchWordList;
    private String inputSentence;


    private boolean CheckPermission() {

        final boolean[] isPermissionGranted = new boolean[1];

        String[] permissions;

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA};
            Log.e("ViewProfile", "onCreate: Permission for android 13");
        } else {


            permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            Log.e("ViewProfile", "onCreate: Permission for below android 13");
        }


        Dexter.withActivity(InboxDetails.this).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("onPermissionsChecked", "onPermissionsChecked: ");

                if (report.areAllPermissionsGranted()) {
                    Log.e("onPermissionsChecked", "all permission granted");
                    isPermissionGranted[0] = true;
                } else {
                    isPermissionGranted[0] = false;
                    Toast.makeText(InboxDetails.this, "To Make a call Camera and Audio permission must.Go to setting to allow the permissions", Toast.LENGTH_SHORT).show();
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


    public static String elasticSearch(String inputWord, List<String> searchWordList) {
        String outPut = inputWord; // to handle no match condition
        String star = "";
        for (String searchWord : searchWordList) {
            if (inputWord.contains(searchWord)) {
                System.out.println("word found");
                String asterisk_val = "";
                for (int i = 0; i < searchWord.length(); i++) {
                    asterisk_val += '*';
                    star = asterisk_val;
                }
            }
            outPut = inputWord.replaceAll(searchWord, star);
            inputWord = outPut;
        }
        return outPut;
    }

    private void sendMessage(String type, String giftId, String giftAmount) {
        notify = true;
        String msg = "";
        if (type.equals("text")) {
            msg = ((EditText) findViewById(R.id.et_message)).getText().toString();
            inputSentence = msg;
            String regex = "[-+^#<!@>$%&({*}?/.=~),'_:]*";
            inputSentence = inputSentence.replaceAll(regex, "")
                    .replaceAll("\\[", "")
                    .replaceAll("\\]", "");
            String outPut = elasticSearch(inputSentence, searchWordList);
            msg = outPut;
            Date date = new Date();
            String profilePic = new SessionManager(this).getUserProfilepic();
            user = new SessionManager(getApplicationContext()).getUserDetails();
            if (msg.length() > 0) {

                //  appLifecycle.sendZegoChatMessage(receiverUserId, msg, dateformatter.format(date), timeformatter.format(date), profilePic, user.get(NAME));

            }
        } else if (type.equals("gift")) {
            msg = giftId;


            //   appLifecycle.sendZegoChatGift(Integer.parseInt(giftId), chatProfileId, new SessionManager(this).getUserName(), new SessionManager(this).getUserProfilepic());

            Log.e("sendMessage3333", "sendMessage: ");
            //todo send the uid to server
            // apiManager.addUserGift(receiverUserId);
            //giftAnimation(Integer.parseInt(giftId));
            //   NewGiftAnimation(Integer.parseInt(giftId), new SessionManager(this).getUserName(), new SessionManager(this).getUserProfilepic());

        } else if (type.equals("gift_request")) {
            int request_status = 0;
            String tagLine = "Please give me this gift :" + giftId + ":" + request_status + ":" + giftAmount;
            msg = tagLine;

        } else if (type.equals("call_request")) {
            int request_status = 0;
            String tagLine = "Call me :" + request_status;
            msg = tagLine;
        } else if (type.equals("ss")) {
        }

       /* Log.e("messageType",type);
        Log.e("messageData",msg);*/

        if (!msg.isEmpty()) {

            //       String msgSenderRef = "Messages/" + currentUserId;
            String msgReceiverRef = "Messages/" + receiverUserId;


            DatabaseReference dbReference = rootRef.child("Messages").child(currentUserId).child(receiverUserId).push();
            String messagePushId = "";

            String profilePic = new SessionManager(getApplicationContext()).getUserProfilepic();
            //  Log.e("profilePicLog", profilePic);


            if (isChatActive) {
                //31/12/2021
                getIMStatus(msg, profilePic, type, false);

            }

            ((EditText) findViewById(R.id.et_message)).setText("");


            Messages message = new Messages();
            message.setFrom(currentUserId);
            message.setFromName(currentUserName);

            if (type.equals("text")) {
                message.setMessage(msg);
            } else if (type.equals("gift")) {
                message.setMessage(giftId);
            }


            message.setFromImage(profilePic);
            message.setTime_stamp(System.currentTimeMillis());
            message.setType(type);

            messagesList.add(message);
            mMessageAdapter.notifyDataSetChanged();
            rv_chat.smoothScrollToPosition(messagesList.size());


            String timestamp = System.currentTimeMillis() + "";
            MessageBean messageBean = new MessageBean(currentUserId, message, true, timestamp);

            updateChatAdapter(messageBean);

            String contactId = insertOrUpdateContact(messageBean.getMessage(), chatProfileId, profileName, chatProfileImage, timestamp);
            if (TextUtils.isEmpty(this.contactId)) {
                this.contactId = contactId;
            }
            messageBean.setAccount(contactId);


            insertChat(messageBean);


            if (receiverUserId.equals("1")) {
                //  apiManager.sendMessageToAdmin(msg, profilePic);
            }

        }

        // Send Message in Notification
        if (notify) {

            // Send notification if User not on chat conversation screen
          /*  if (!isReceiverOnline) {
                if (currentReceiverToken == null) {
                    getUserTokenFromDatabase(receiverUserId, currentUserName, msg, type);
                } else {
                    sendMessageInNotification(msg, currentUserName, type, currentReceiverToken);
                }
            }*/
        }
        notify = false;
    }

    private void sendMessageIM(String message) {

        V2TIMManager.getInstance().sendC2CTextMessage(message,
                receiverUserId, new V2TIMValueCallback<V2TIMMessage>() {
                    @Override
                    public void onSuccess(V2TIMMessage message) {
                        // The one-to-one text message sent successfully
                        Log.e("offLineDataLog", "success to => " + receiverUserId + " with message => " + new Gson().toJson(message));

                        //dbHandler.updateMainContent(receiverUserId, 1);
                    }


                    @Override
                    public void onError(int code, String desc) {
                        // Failed to send the one-to-one text message
                        //Log.e("offLineDataLog", "error code => " + code + " desc => " + desc);
                        /*if (code == 6013) {
                            IMOperations imOperations = new IMOperations(getApplicationContext());
                            imOperations.loginIm(sessionManager.getUserId());
                            sendMessageIM(message);
                        }*/
                    }
                });
    }

    private void getIMStatus(String msg, String profilePic, String type, boolean fromMain) {

        List<String> ids = Arrays.asList(chatProfileId);
        String finalMsg = msg;
        V2TIMManager.getInstance().getUserStatus(ids, new V2TIMValueCallback<List<V2TIMUserStatus>>() {
            @Override
            public void onSuccess(List<V2TIMUserStatus> v2TIMUserStatuses) {
                // Queried the status successfully
                //Log.e("offLineDataLog", "from ID => " + new Gson().toJson(v2TIMUserStatuses)+" msg => "+msg+" profilePic =>"+profilePic+" type => "+type);
                if (v2TIMUserStatuses.get(0).getStatusType() != 1) {
                    sendNotificationIfUserOffline(finalMsg, chatProfileId, currentUserName, profilePic, type);
                } else {
                    // `msgID` returned by the API for on-demand use
                    // Log.e("offLineDataLog", "before sending => " + msg2);
                    if (finalMsg.isEmpty() || profilePic.isEmpty() || type.isEmpty()) {
                        return;
                    }
                    JSONObject jsonResult = new JSONObject();
                    try {
                        jsonResult.put("type", type);
                        jsonResult.put("message", finalMsg);
                        jsonResult.put("from", currentUserId);
                        jsonResult.put("fromName", currentUserName);
                        jsonResult.put("fromImage", profilePic);
                        jsonResult.put("time_stamp", System.currentTimeMillis());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String msg2 = jsonResult.toString();

                    sendMessageIM(msg2);
                }
            }


            @Override
            public void onError(int code, String desc) {
                // Failed to query the status
                //Log.e("offLineDataLog", "error code => " + code + " desc => " + desc);
                if (code == 6013) {
                    IMOperations imOperations = new IMOperations(InboxDetails.this);
                    imOperations.loginIm(sessionManager.getUserId());
                    //sendMessageIM(message);
                    if (fromMain) {
                        return;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getIMStatus(finalMsg, profilePic, type, fromMain);
                        }
                    }, 1000);
                }
            }
        });
    }

    int incPos = 0;
    private boolean isStackof3Full = false;

    private void NewGiftAnimation(int giftId, String peerName, String peerProfilePic) {


    }


    private int getGiftResourceId(int Pos) {
        int ResourceId = 0;

        switch (Pos) {
            case 19:
                ResourceId = R.drawable.candy;
                break;
            case 2:
                ResourceId = R.drawable.lucky;
                break;
            case 3:
                ResourceId = R.drawable.bell;
                break;
            case 4:
                ResourceId = R.drawable.leaves;
                break;
            case 5:
                ResourceId = R.drawable.kiss;
                break;
            case 6:
                ResourceId = R.drawable.candy_1;
                break;
            case 7:
                ResourceId = R.drawable.rose;
                break;
            case 8:
                ResourceId = R.drawable.heart;
                break;
            case 9:
                ResourceId = R.drawable.lipstik;
                break;
            case 10:
                ResourceId = R.drawable.perfume;
                break;
            case 11:
                ResourceId = R.drawable.necklace;
                break;
            case 12:
                ResourceId = R.drawable.panda;
                break;
            case 13:
                ResourceId = R.drawable.hammer;
                break;
            case 14:
                ResourceId = R.drawable.rocket;
                break;
            case 15:
                ResourceId = R.drawable.ship;
                break;
            case 16:
                ResourceId = R.drawable.ring;
                break;
            case 17:
                ResourceId = R.drawable.disney;
                break;
            case 18:
                ResourceId = R.drawable.hot_ballon;
                break;
        }
        return ResourceId;

    }


    void getChatData() {
        listener = rootRef.child("Messages").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String timestamp = System.currentTimeMillis() + "";
                if (passMessage) {
                    try {
                        Messages message = snapshot.getValue(Messages.class);
                        if (message.getMessage() != null) {
                            if (receiverUserId.equals(message.getFrom()) || currentUserId.equals(message.getFrom())) {
                                //  messagesList.add(message);
                                Log.e("messageBulk", new Gson().toJson(message));
                                MessageBean messageBean = new MessageBean(currentUserId, message, false, timestamp);
                                updateChatAdapter(messageBean);
                              /*  chatMessageList.add(messageBean);

                                mMessageAdapter.notifyDataSetChanged();
                                rv_chat.smoothScrollToPosition(chatMessageList.size());*/
                                updateUnreadMsgCount(chatProfileId);
                            }
                        }
                    } catch (Exception e) {
                    }
                } else {
                    passMessage = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


/*    void getChatData() {
        rootRef.child("Messages").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String timestamp = System.currentTimeMillis() + "";
                if (passMessage) {
                    try {
                        Messages message = snapshot.getValue(Messages.class);
                        if (message.getMessage() != null) {
                            if (receiverUserId.equals(message.getFrom()) || currentUserId.equals(message.getFrom())) {
                                //  messagesList.add(message);
                                Log.e("messageBulk", new Gson().toJson(message));
                               *//* MessageBean messageBean = new MessageBean(currentUserId, message, false, timestamp);
                                updateChatAdapter(messageBean);*//*
     *//*  chatMessageList.add(messageBean);

                                mMessageAdapter.notifyDataSetChanged();
                                rv_chat.smoothScrollToPosition(chatMessageList.size());*//*
                            }
                        }
                    } catch (Exception e) {
                    }
                } else {
                    passMessage = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        AppLifecycle.isChatActivityInFront = true;
        updateUnreadMsgCount(chatProfileId);
        registerReceiver(myReceivedMsg, new IntentFilter("USER-TEXT"));
        registerReceiver(myReceivedVideoEventMsg, new IntentFilter("VIDEO-CALL-EVENT"));
        registerReceiver(refreshChatIndi, new IntentFilter("KAL-REFRESHCHATBROADINDI"));
    }

    protected void onDestroy() {
        super.onDestroy();
        userDBRef.removeEventListener(valueEventListener);
        unregisterReceiver(myReceivedMsg);
        unregisterReceiver(myReceivedVideoEventMsg);
        unregisterReceiver(refreshChatIndi);
        stopPlaying();
    }

//    private boolean isPLAYING = false;
    // MediaPlayer mp;

   /* public void playAudioFile(String url, int position) {
        if (!isPLAYING) {
            isPLAYING = true;
            mp = new MediaPlayer();
            try {
                mp.setDataSource(url);
                mp.prepare();
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        isPLAYING = false;
                        stopPlaying();

                        mMessageAdapter.stopChangeUi(position);
                    }
                });
            } catch (IOException e) {
                //  Log.e(LOG_TAG, "prepare() failed");
            }
        } else {
            isPLAYING = false;
            stopPlaying();
            mMessageAdapter.stopChangeUi(position);
        }
    }*/

    private void stopPlaying() {
       /* try {
            mp.release();
            mp = null;
        } catch (Exception e) {
        }*/
        mMessageAdapter.stopChangeUi();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();
        AppLifecycle.isChatActivityInFront = false;
    }

    private void updateChatAdapter(MessageBean messageBean) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            messageBeanList.add(0, messageBean);
            addMessageInChatList(messageBean);
            //Background work here
            handler.post(() -> {
                //UI Thread work here
                mMessageAdapter.notifyDataSetChanged();
                rv_chat.scrollToPosition(0);
            });
        });
    }

    private synchronized void updateUnreadMsgCount(String profileId) {
        UserInfo userInfoFromDb = dbHandler.getContactInfo(profileId, currentUserId);
        if (userInfoFromDb != null) {
            // set unread count 0
            userInfoFromDb.setUnread_msg_count("0");
            dbHandler.updateContact(userInfoFromDb);
        }
    }

    private void addMessageInChatList(MessageBean msgBean) {
        try {
            String dateCurr = getDateByTimestamp(msgBean.getTimestamp());
            String datePrev = messageBeanList.size() > 1 ? getDateByTimestamp(messageBeanList.get(1).getTimestamp()) : "";
            chatMessageList.add(0, msgBean);
            if (!dateCurr.equalsIgnoreCase(datePrev) || chatMessageList.size() == 1) {
                MessageBean messageBean = new MessageBean();
                messageBean.setMsgDate(dateCurr);
                chatMessageList.add(1, messageBean);
            }
        } catch (Exception e) {
            //
        }
    }

    public static String getDateByTimestamp(String dateInMilliseconds) {
        String dateFormat = "dd/MM/yyyy";
        try {
            return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void convertChatListWithHeader() {
        try {
            if (messageBeanList.isEmpty()) return;
            chatMessageList.clear();
            if (messageBeanList.size() == 1) {
                MessageBean msgBean = messageBeanList.get(0);
                chatMessageList.add(msgBean);
                MessageBean messageBean = new MessageBean();
                messageBean.setMsgDate(getDateByTimestamp(msgBean.getTimestamp()));
                chatMessageList.add(messageBean);
                //  Log.e("convertChat", "convertChatListWithHeader1: " );
                return;
            }
            //  Log.e("convertChat", "convertChatListWithHeader2: " );

            String dateCurr = "";
            for (int i = 1; i < messageBeanList.size(); i++) {
                dateCurr = getDateByTimestamp(messageBeanList.get(i).getTimestamp());
                String datePrev = getDateByTimestamp(messageBeanList.get(i - 1).getTimestamp());
                Log.e("convertChat", "convertChatListWithHeader2: dateCurr  " + dateCurr + "  datePrev " + datePrev);


                if (dateCurr.equalsIgnoreCase(datePrev)) {
                    chatMessageList.add(messageBeanList.get(i - 1));
                } else {
                    MessageBean messageBean = new MessageBean();
                    messageBean.setMsgDate(datePrev);
                    chatMessageList.add(messageBeanList.get(i - 1));
                    chatMessageList.add(messageBean);
                }

                Log.e("convertChat", "convertChatListWithHeader22: " + new Gson().toJson(chatMessageList.get(i - 1)));
            }
            chatMessageList.add(messageBeanList.get(messageBeanList.size() - 1));
            MessageBean messageBean = new MessageBean();
            messageBean.setMsgDate(dateCurr);
            chatMessageList.add(messageBean);

            // Log.e("convertChat", "convertChatListWithHeader: "+chatMessageList.toString() );

            Log.e("convertChat", "convertChatListWithHeader:22  " + new Gson().toJson(chatMessageList.get(messageBeanList.size() - 1)));

            /*if (unreadMsgCount > 0) {
                MessageBean messageBeanUnread = new MessageBean();
                messageBeanUnread.setMsgDate("Unread");
                chatMessageList.add(unreadMsgCount, messageBeanUnread);
            }*/

        } catch (Exception e) {//
            Log.e("convertChat", "convertChatListWithHeader:  Exception " + e.getMessage());
        }
    }

    private String insertOrUpdateContact(Messages message, String userId, String profileName, String profileImage, String timestamp) {
        UserInfo userInfoFromDb = dbHandler.getContactInfo(userId, currentUserId);
        String contactId = "";
        if (userInfoFromDb == null) {
            //insert
            UserInfo userInfo = new UserInfo();
            userInfo.setUser_id(userId);
            userInfo.setUser_name(profileName);
            userInfo.setMessage(message.getMessage());
            userInfo.setUser_photo(profileImage);
            userInfo.setTime(timestamp);
            userInfo.setUnread_msg_count("0");
            userInfo.setProfile_id(currentUserId);
            userInfo.setMsg_type(message.getType());
            userInfo.setGift_count(String.valueOf(userGiftCount));
            contactId = dbHandler.addContact(userInfo);
        } else {
            //update
            contactId = userInfoFromDb.getId();
            userInfoFromDb.setUser_name(profileName);
            userInfoFromDb.setMessage(message.getMessage());
            userInfoFromDb.setUser_photo(profileImage);
            userInfoFromDb.setTime(timestamp);
            userInfoFromDb.setUnread_msg_count("0");
            userInfoFromDb.setMsg_type(message.getType());
            userInfoFromDb.setGift_count(String.valueOf(userGiftCount));
            dbHandler.updateContact(userInfoFromDb);
        }
        return contactId;
    }

    private void insertChat(MessageBean messageBean) {
        dbHandler.addChat(messageBean);
    }

    private void initScrollListner() {
        rv_chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) //check for scroll top
                {
                    visibleItemCount = mLinearLayoutManager.getChildCount();
                    totalItemCount = mLinearLayoutManager.getItemCount();
                    pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;


                            //Log.e("onScrolledRV", "size=" + messageBeanList.size());
                            List<MessageBean> messageListBeans = dbHandler.getChatList(contactId, messageBeanList.size(), chatLoadLimit);
                            if (!messageListBeans.isEmpty()) {

                                messageBeanList.addAll(messageListBeans);
                                convertChatListWithHeader();
                            }
                            mMessageAdapter.notifyDataSetChanged();
                            //Toast.makeText(MessageActivity.this, "loading", Toast.LENGTH_LONG).show();
                            loading = true;
                        }
                    }
                }
            }
        });
    }

    private void sendNotificationIfUserOffline(String message, String profileId, String profileName, String profileImage, String type) {
        try {
            if (firebaseOnlineStatus.equals("Online")) {

            } else {
                //   sendChatNotification(firebaseFCMToken, message, profileName, profileImage, type);
            }
          /*  DatabaseReference userDBRef;
            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance(secondApp);
            userDBRef = mFirebaseInstance.getReference("Users/" + profileId);

            userDBRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("FirebaseRealTimeDB user", dataSnapshot.exists() + "");
                    if (dataSnapshot.exists()) {
                        FirebaseUserModel userModel = dataSnapshot.getValue(FirebaseUserModel.class);
                        String status = userModel.getStatus();
                        Log.e("FirebaseRealTimeDB", "status=" + status);
                        if (status.equalsIgnoreCase("Online")) {
//                            Log.e("userFCMOnline", userModel.getFcmToken());
                            //
                        } else {
//                            Log.e("userFCMOffline", userModel.getFcmToken());
                            sendChatNotification(userModel.getFcmToken(), message, profileName, profileImage, type);
                        }
                        //  userDBRef.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseRealTimeDB Fail", databaseError + "");
                }
            });*/

        } catch (Exception ex) {
            //
        }
    }


    @Override
    public void onBackPressed() {
     /*if (fromNotification) {
            Intent intent = new Intent(this, MainActivity.class);
         //   intent.putExtra("isOpenContact", true);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }*/

       /* if (img_fullImage.getVisibility() == View.VISIBLE) {
            img_fullImage.setVisibility(View.GONE);
            return;
        }*/

        rootRef.child("Messages").child(currentUserId).removeEventListener(listener);
        super.onBackPressed();
    }

    public void backFun(View v) {
        onBackPressed();
    }


    String callRate = "25";
    private boolean success, canChat = false;
    private String freeSeconds, serverDate;
    private int chatStatus = 0;
    private int remGiftCard = 0;
    int onlineStatus, busyStatus;

    public void openVideoChat(View view) {
     /*
        callType = "video";
        apiManager.getRemainingGiftCardFunction();
     */
    }


    private Dialog firstTimeRecharge;


    private String callType = "";

    @Override
    public void isError(String errorCode) {

    }

    private InsufficientCoins insufficientCoins;

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void isSuccess(Object response, int ServiceCode) {
       /* try {

        }catch (Exception e){
        }*/
        if (ServiceCode == Constant.GET_FIRST_TIME_RECHARGE) {
            DiscountedRechargeResponse res = (DiscountedRechargeResponse) response;
            if (res.getSuccess()) {
                if (res.getIsRecharge() == 0) {

                    lvRecharge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lvRecharge.setEnabled(false);
                            insufficientCoins = new InsufficientCoins(InboxDetails.this, 2, Integer.parseInt(callRate));
                            mMessageView.setVisibility(View.GONE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    lvRecharge.setEnabled(true);
                                }
                            }, 1000);
                        }
                    });

                } else {
                    rechargeFirst_ll.setVisibility(View.GONE);
                }
            }

        }

        if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {


            RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;

            try {
                try {
                    success = rsp.getSuccess();
                    remGiftCard = rsp.getResult().getRemGiftCards();
                    freeSeconds = rsp.getResult().getFreeSeconds();
                    if (remGiftCard > 0) {
                        apiManager.searchUser(receiverUserId, "1");
                        return;
                    }
                } catch (Exception e) {

                }

                if (new SessionManager(getApplicationContext()).getUserWallet() >= Integer.parseInt(callRate)) {
                    apiManager.searchUser(receiverUserId, "1");
                } else {
                    Log.e("insufficientCoinsDialog", "isSuccess: " + "insufficientCoinsDialog");

                    insufficientCoins = new InsufficientCoins(InboxDetails.this, 2, Integer.parseInt(callRate));

                    insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {

                            apiManager.checkFirstTimeRechargeDone();
                        }
                    });


                    // apiManager.searchUser(profileId, "1");
                }
            } catch (Exception e) {
                apiManager.searchUser(receiverUserId, "1");
            }
        }
        if (ServiceCode == Constant.SEARCH_USER) {
            UserListResponseMet rsp = (UserListResponseMet) response;
            if (rsp != null) {
                try {
                    if (callType.equals("video")) {

                        if (remGiftCard > 0) {
                            apiManager.generateCallRequestZ(Integer.parseInt(host_userId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                    Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                        } else {
                            apiManager.generateCallRequestZ(Integer.parseInt(host_userId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                    Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                        }
                      /*  chatRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> map = null;
                                if (dataSnapshot.exists()) {
                                    map = (Map<String, Object>) dataSnapshot.getValue();

                                    if (map.get("status").equals("Online") || map.get("status").equals("Live")) {

                                         *//*   if (remGiftCard > 0) {
                                                apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                            } else {
                                                apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                            }

*//*




                                    } else if (map.get("status").equals("Busy")) {

                                        Toast.makeText(getApplicationContext(), "User is Busy", Toast.LENGTH_LONG).show();

                                        //  Log.e("HomeFragmentfirebase", "onDataChange: "+"Busy" );
                                        Log.e("HomeFragmentfirebase", "onDataChange: " + map.get("status").toString());
                                    } else if (map.get("status").equals("Offline")) {
                                        Toast.makeText(getApplicationContext(), "User is Offline", Toast.LENGTH_LONG).show();
                                        // Log.e("HomeFragmentfirebase", "onDataChange: "+"Offline" );
                                        Log.e("HomeFragmentfirebase", "onDataChange: " + map.get("status").toString());
                                    }
                                } else {
                                    Log.e("HomeFragmentfirebase", "onSuccess: " + "does not exist");
                                }
                            }
                        });*/

                    } else if (callType.equals("audio")) {

                        /*   apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                         String.valueOf(System.currentTimeMillis()));
                    */
                    }

                       /* Log.e("userbusycatch", "isSuccess: " + new Gson().toJson(rsp));
                        int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                        Log.e("HF_OnlineStatusss", "" + onlineStatus);
                        Log.e("userbusycatch", "isSuccess:11 " + new Gson().toJson(rsp));

                        int busyStatus = rsp.getResult().getData().get(0).getIs_busy();

                        Log.e("HF_BusyStatusss", "" + busyStatus);

                        //onlineStatus == 1 && busyStatus == 0
                        if (onlineStatus == 1 && busyStatus == 0) {
                            // Check wallet balance before going to make a video call
                            //     apiManager.getWalletAmount();

                            if (callType.equals("video")) {
                                if (remGiftCard > 0) {
                                    apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                            Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                } else {
                                    apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                            Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                }
                            } else if (callType.equals("audio")) {
                    *//*
                         apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                         String.valueOf(System.currentTimeMillis()));
                    *//*
                            }
                        } else if (onlineStatus == 1) {
                            Toast.makeText(getApplicationContext(), hostName + " is Busy", Toast.LENGTH_SHORT).show();

                        } else if (onlineStatus == 0) {
                            Toast.makeText(getApplicationContext(), hostName + " is Offline", Toast.LENGTH_SHORT).show();
                        }*/


                } catch (Exception e) {
                    Log.e("SearchUserCallTest", "isSuccess: " + rsp.getResult());
                    Log.e("userbusycatch", "isSuccess: Exception " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "User is Offline!", Toast.LENGTH_SHORT).show();

                    new SessionManager(getApplicationContext()).setOnlineState(0);
                    // finish();
                }
            }
        }

        if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKENZ) {
            GenerateCallResponce rsp = (GenerateCallResponce) response;
            Log.e("checkkkk", "" + receiverUserId);

            V2TIMManager v2TIMManager = V2TIMManager.getInstance();


            Log.e("NEW_GENERATE_AGORA_TOKENZ", "isSuccess: " + new Gson().toJson(rsp));

            long walletBalance = rsp.getResult().getPoints();
            int CallRateInt = Integer.parseInt(callRate);
            long talktime = (walletBalance / CallRateInt) * 60 * 1000L;
            //  Log.e("AUTO_CUT_TESTZ", "CallNotificationDialog: " + talktime);
            long canCallTill = talktime - 2000;
            Log.e("AUTO_CUT_TESTZ", "CallNotificationDialog: canCallTill " + canCallTill);
            String profilePic = new SessionManager(getApplicationContext()).getUserProfilepic();
            HashMap<String, String> user = new SessionManager(getApplicationContext()).getUserDetails();
            Intent intent = new Intent(getApplicationContext(), VideoChatZegoActivityMet.class);
            intent.putExtra("TOKEN", "demo");
            intent.putExtra("ID", receiverUserId);
            intent.putExtra("UID", String.valueOf(host_userId));
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
            intent.putExtra("receiver_name", receiverName);
            intent.putExtra("converID", "convId");
            intent.putExtra("receiver_image", receiverImage);
            Log.e("NEW_GENERATE_AGORA_TOKENZ", "isSuccess: go to videoChatActivity");


            JSONObject jsonResult = new JSONObject();
            try {
                jsonResult.put("type", "callrequest");

                jsonResult.put("caller_name", new SessionManager(getApplicationContext()).getName());
                jsonResult.put("userId", new SessionManager(getApplicationContext()).getUserId());
                jsonResult.put("unique_id", rsp.getResult().getUnique_id());
                jsonResult.put("callerProfileId", new SessionManager(InboxDetails.this).getUserId());

                jsonResult.put("caller_image", new SessionManager(getApplicationContext()).getUserProfilepic());
                jsonResult.put("callRate", callRate);
                jsonResult.put("isFreeCall", "false");
                jsonResult.put("totalPoints", new SessionManager(getApplicationContext()).getUserWallet());
                jsonResult.put("remainingGiftCards", "0");
                jsonResult.put("freeSeconds", "0");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            String msg2 = jsonResult.toString();

            V2TIMSignalingManager v2TIMSignalingManager = V2TIMManager.getSignalingManager();
            String inviteId = v2TIMSignalingManager.invite(receiverUserId, msg2, true, null, 20, new V2TIMCallback() {
                @Override
                public void onSuccess() {
                    Log.e("listensdaa", "Yes11 " + receiverUserId);
                    startActivity(intent);

                }

                @Override
                public void onError(int i, String s) {
                    Log.e("listensdaa", "Yes22 " + s);

                }
            });
            intent.putExtra("inviteId", inviteId);
            try {
                jsonResult.put("message", "Called");
                jsonResult.put("from", new SessionManager(InboxDetails.this).getUserId());
                jsonResult.put("fromName", new SessionManager(InboxDetails.this).getUserName());
                jsonResult.put("fromImage", new SessionManager(InboxDetails.this).getUserProfilepic());
                jsonResult.put("time_stamp", System.currentTimeMillis());

            } catch (Exception e) {

            }

            V2TIMManager.getInstance().sendC2CTextMessage(msg2,
                    receiverUserId, new V2TIMValueCallback<V2TIMMessage>() {
                        @Override
                        public void onSuccess(V2TIMMessage message) {
                            // The one-to-one text message sent successfully
                            Log.e("offLineDataLog", "success to => " + receiverUserId + " with message => " + new Gson().toJson(message));
                        }


                        @Override
                        public void onError(int code, String desc) {

                        }
                    });
        }


        if (ServiceCode == GET_DATA_FROM_PROFILE_ID) {

            DataFromProfileIdResponse rsp = (DataFromProfileIdResponse) response;

            if (rsp != null) {
                Log.e("GET_DATA_FROM_PROFILE_ID", "isSuccess: rsp " + "Not Null");
                DataFromProfileIdResult result = rsp.getResult();
                if (result != null) {
                    Log.e("GET_DATA_FROM_PROFILE_ID", "isSuccess: result " + "Not Null");
                    Intent viewProfileIntent = new Intent(InboxDetails.this, ViewProfileMet.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("id", result.getId());
                    bundle.putSerializable("profileId", result.getProfile_id());
                    bundle.putSerializable("level", result.getLevel());
                    viewProfileIntent.putExtras(bundle);
                    startActivity(viewProfileIntent);
                    Log.e("GET_DATA_FROM_PROFILE_ID", "isSuccess: " + new Gson().toJson(result));
                }
            }
        }


        if (ServiceCode == Constant.SEND_GIFT) {
            SendGiftResult rsp = (SendGiftResult) response;
            long currentCoin = rsp.getResult();

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
//                Log.e("selectedImage", "selectedImage:" + selectedImage);
                String picturePath = getProfileImagePath(this, selectedImage);
                try {
                    File file = null;
                    file = new Compressor(getApplicationContext()).compressToFile(new File(picturePath));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    picToUpload = MultipartBody.Part.createFormData("message", file.getName(), requestBody);

                    String userPicUrl = new SessionManager(getApplicationContext()).getUserProfilepic();
                    RequestBody conversationIdPic = RequestBody.create(MediaType.parse("text/plain"), userPicUrl);
                    // apiManager.sendImagetoCustomerSupport(conversationIdPic, picToUpload);
                } catch (Exception e) {
                }
            }
        }
    }

    MultipartBody.Part picToUpload;

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

    public BroadcastReceiver refreshChatIndi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            String from = intent.getStringExtra("from");
            String messageText = intent.getStringExtra("messageText");
            String type = intent.getStringExtra("type");
            String fromImage = intent.getStringExtra("fromImage");
            String fromName = intent.getStringExtra("fromName");
            String time_stamp = intent.getStringExtra("time_stamp");

            if (action.equals("addChat")) {
                Messages message = new Messages();

                message.setFrom(from);
                message.setFromImage(fromImage);
                message.setFromName(fromName);
                message.setMessage(messageText);
                message.setType(type);
                message.setTime_stamp(Long.parseLong(time_stamp));

                if (message.getMessage() != null) {
                    if (receiverUserId.equals(message.getFrom()) || currentUserId.equals(message.getFrom())) {

                        Log.e("messageBulk", new Gson().toJson(message));
                        MessageBean messageBean = new MessageBean(currentUserId, message, false, time_stamp);
                        updateChatAdapter(messageBean);
                    }
                }
            }
        }
    };

    BroadcastReceiver myReceivedMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("zegoReceivedffff", "yes");

            String str = intent.getStringExtra("msg");
            String peer_id = intent.getStringExtra("peerId");


            try {
                JSONObject jsonObject = new JSONObject(str);
                String msgg = jsonObject.getString("MessageContent");
                if (peer_id.equals(receiverUserId)) {
                    updateUnreadMsgCount(receiverUserId);
                    String msg = msgg;
                    String userProfilePic = new SessionManager(getApplicationContext()).getUserProfilepic();
                    String timestamp = System.currentTimeMillis() + "";
                    Messages message = new Messages();
                    message.setFrom(currentUserId);
                    message.setFromName(currentUserName);
                    message.setMessage(msg);
                    message.setFromImage(userProfilePic);
                    message.setTime_stamp(Long.parseLong(timestamp));
                    message.setType("text");
                    MessageBean messageBean = new MessageBean(contactId, message, false, timestamp);
                    updateChatAdapter(messageBean);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());

            }


/*
            if (peer_id.equals(receiverUserId)) {
                String[] rec_data = str.split(":rtm:");
              //  String msg = rec_data[1];
                String msg = rec_data[1];
                String userProfilePic = new SessionManager(getApplicationContext()).getUserProfilepic();

                String timestamp = System.currentTimeMillis() + "";
                Messages message = new Messages();
                message.setFrom(currentUserId);
                message.setFromName(currentUserName);
                message.setMessage(msg);
                message.setFromImage(userProfilePic);
                message.setTime_stamp(Long.parseLong(timestamp));
                message.setType("text");

                MessageBean messageBean = new MessageBean(contactId, message, false, timestamp);
                updateChatAdapter(messageBean);
                //messageBeanList.add(0, messageBean);
                //addMessageInChatList(messageBean);
                //mMessageAdapter.notifyDataSetChanged();
                //rv_chat.scrollToPosition(messageBeanList.size());
                //chatMessageList.add(chatMessageList.size()-1, messageBean);
                //addMessageInChatList(messageBean);
                //mMessageAdapter.notifyDataSetChanged();
                //rv_chat.scrollToPosition(0);
                //rv_chat.smoothScrollToPosition(chatMessageList.size());
                //rv_chat.smoothScrollToPosition(messagesList.size());


            }*/
        }
    };

    BroadcastReceiver myReceivedVideoEventMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            MessageBean msgBean = (MessageBean) intent.getSerializableExtra("msg");
            if (intent.hasExtra("userGiftCount"))
                userGiftCount = intent.getIntExtra("userGiftCount", 0);
            updateChatAdapter(msgBean);

        }
    };

    private void giftAnimation(int position) {
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        ((ImageView) findViewById(R.id.gift_imageShow)).setVisibility(View.VISIBLE);
        switch (position) {
            case 19:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.candy);
                break;
            case 2:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.lucky);
                break;
            case 3:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.bell);
                break;
            case 4:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.leaves);
                break;
            case 5:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.kiss);
                break;
            case 6:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.candy_1);
                break;
            case 7:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.rose);
                break;
            case 8:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.heart);
                break;
            case 9:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.lipstik);
                break;
            case 10:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.perfume);
                break;
            case 11:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.necklace);
                break;
            case 12:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.panda);
                break;
            case 13:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.hammer);
                break;
            case 14:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.rocket);
                break;
            case 15:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.ship);
                break;
            case 16:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.ring);
                break;
            case 17:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.disney);
                break;
            case 18:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.hot_ballon);
                break;
        }
        //   ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rose);
        animFadeIn.reset();
        ((ImageView) findViewById(R.id.gift_imageShow)).clearAnimation();
        ((ImageView) findViewById(R.id.gift_imageShow)).startAnimation(animFadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ImageView) findViewById(R.id.gift_imageShow)).setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void sendGiftFun(String url, int amount) {
        if (new SessionManager(getApplicationContext()).getUserWallet() >= amount) {
            if (userGiftCount == 0) {
                apiManager.addUserGift(receiverUserId);
                if (new SessionManager(getApplicationContext()).getGender().equals("male")) {
                    addFriendIM();
                }
            }
            userGiftCount = 1;

            sendMessage("gift", url, "");
        } else {
            new InsufficientCoins(InboxDetails.this, 2, Integer.parseInt(callRate));
        }
    }

    private void addFriendIM() {
        // 添加好友
        V2TIMFriendAddApplication application = new V2TIMFriendAddApplication(receiverUserId);
        application.setAddType(V2TIMFriendInfo.V2TIM_FRIEND_TYPE_BOTH);
        V2TIMManager.getFriendshipManager().addFriend(application, new V2TIMValueCallback<V2TIMFriendOperationResult>() {
            @Override
            public void onSuccess(V2TIMFriendOperationResult v2TIMFriendOperationResult) {
                // 添加好友请求成功
                //Log.e("ImFriendLog", "v2TIMFriendOperationResult => " + v2TIMFriendOperationResult.getResultInfo());
                checkIMFriendStatus();

            }

            @Override
            public void onError(int code, String desc) {
                // 添加好友失败
                //Log.e("ImFriendLog", "add friend code => " + code + " desc => " + desc);

            }
        });
    }

    private void checkIMFriendStatus() {
        List<String> userIDList = new ArrayList<>();
        userIDList.add(receiverUserId);
        V2TIMManager.getFriendshipManager().checkFriend(userIDList, V2TIMFriendInfo.V2TIM_FRIEND_TYPE_BOTH, new V2TIMValueCallback<List<V2TIMFriendCheckResult>>() {
            @Override
            public void onSuccess(List<V2TIMFriendCheckResult> v2TIMFriendCheckResults) {
                // 检查好友关系成功
                for (V2TIMFriendCheckResult checkResult : v2TIMFriendCheckResults) {
                    // 用户 ID
                    String userID = checkResult.getUserID();
                    // 用户和自己的好友关系
                    int relationType = checkResult.getResultType();
                    //Log.e("ImFriendLog", "userID => " + userID);
                    Log.e("ImFriendLog", "relationType => " + relationType);
                    userGiftCount = relationType;
                    // userGiftCount=3;
                   /* if (userGiftCount == 0) {
                        if (sessionManager.getGender().equals("female")) {
                            addFriendIMHOST();
                        }
                    }*/
                }
            }

            @Override
            public void onError(int code, String desc) {
                // 检查好友关系失败
                //Log.e("ImFriendLog", "code => " + code + " desc => " + desc);

            }
        });
    }
}


// private boolean isFirst=false;


