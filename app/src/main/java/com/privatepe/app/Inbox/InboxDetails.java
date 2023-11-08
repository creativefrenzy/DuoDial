package com.privatepe.app.Inbox;



import static com.privatepe.app.utils.Constant.GET_DATA_FROM_PROFILE_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.privatepe.app.R;
import com.privatepe.app.activity.ViewProfile;
import com.privatepe.app.adapter.GiftAnimationRecyclerAdapter;
import com.privatepe.app.extras.MessageCallDataRequest;
import com.privatepe.app.model.gift.GiftAnimData;
import com.privatepe.app.model.gift.SendGiftResult;
import com.privatepe.app.response.DataFromProfileId.DataFromProfileIdResponse;
import com.privatepe.app.response.DataFromProfileId.DataFromProfileIdResult;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    }


    private void firebaseOperation() {

        rootRef = FirebaseDatabase.getInstance().getReference();

        currentUserId = String.valueOf(new SessionManager(getApplicationContext()).getUserId());
        currentUserName = new SessionManager(getApplicationContext()).getUserName();

        try {
            DatabaseReference userDBRef;
            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
            userDBRef = mFirebaseInstance.getReference("Users/" + chatProfileId);

            userDBRef.addValueEventListener(new ValueEventListener() {
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
            });
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

        if (TextUtils.isEmpty(contactId)) {
            UserInfo contactInfo = dbHandler.getContactInfo(chatProfileId, currentUserId);
            if (contactInfo != null) {
                contactId = contactInfo.getId();
            }
        }


        Log.e("VIEW_PROFILE_TEST", "getinData: id " + receiverUserId + " contactid  " + contactId + "  id session manager ");


        findViewById(R.id.img_profile).setOnClickListener(view -> {

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

    @SuppressLint("WrongConstant")
    private void init() {


        mActivity = this;
        apiManager = new ApiManager(getApplicationContext(), this);
        sessionManager = new SessionManager(this);

        searchWordList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.searchWordsArray)));

        rv_select_gifts = findViewById(R.id.rv_gift);
        rl_gift = findViewById(R.id.rl_gift);
        tv_name = findViewById(R.id.tv_username);
        mMessageView = findViewById(R.id.et_message);
        giftAnimRecycler = findViewById(R.id.gift_animation_recyclerview);

//        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mLinearLayoutManager.setStackFromEnd(true);
        rv_chat.setLayoutManager(mLinearLayoutManager);
        mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);

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
                mMessageAdapter = new MessageAdapter(this, chatMessageList);
                rv_chat.setAdapter(mMessageAdapter);
                rv_chat.scrollToPosition(unreadMsgCount);
                //end setup chat adapter
            });
        });

        MessageLoaderOnScroll();


        if (new SessionManager(getApplicationContext()).getGender().equals("male")) {
            MessageCallDataRequest messageCallDataRequest = new MessageCallDataRequest(receiverUserId);
            //apiManager.getMessageCallDataFunction(messageCallDataRequest);
            ((ImageView) findViewById(R.id.img_video_call)).setVisibility(View.VISIBLE);

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
                    sendMessage("text", "", "");

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
        if (purchasePlanStatus == 1 )/*)*/ {
            sendMessage("text", "", "");
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                Map messageTextBody = new HashMap();
                messageTextBody.put("type", type);
                messageTextBody.put("message", msg);
                messageTextBody.put("from", currentUserId);
                messageTextBody.put("fromName", currentUserName);
                messageTextBody.put("fromImage", profilePic);
                messageTextBody.put("time_stamp", System.currentTimeMillis());

                Map messageBodyDetails = new HashMap();
                messageBodyDetails.put(msgReceiverRef + "/" + messagePushId, messageTextBody);

                sendNotificationIfUserOffline(msg, chatProfileId, currentUserName, profilePic, type);
                //31/12/2021
                rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
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

           /* if (type.equals("text")) {
            }*/


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

    int incPos = 0;
    private boolean isStackof3Full = false;

    private void NewGiftAnimation(int giftId, String peerName, String peerProfilePic) {

     /*   giftAnimRecycler.setVisibility(View.VISIBLE);
        if (!isStackof3Full) {
            giftdataList.add(new GiftAnimData(getGiftResourceId(giftId), peerName, peerProfilePic));
            giftAnimationRecyclerAdapter.notifyItemInserted(incPos);
        } else {
            giftdataList.set(incPos, new GiftAnimData(getGiftResourceId(giftId), peerName, peerProfilePic));
            giftAnimationRecyclerAdapter.notifyItemChanged(incPos);
        }

        if (incPos == 2) {
            incPos = 0;
            isStackof3Full = true;
            return;
        } else {
            incPos++;
            return;
        }
*/

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
        listener= rootRef.child("Messages").child(currentUserId).addValueEventListener(new ValueEventListener() {
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


    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceivedMsg);
        unregisterReceiver(myReceivedVideoEventMsg);
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
     /*   if (fromNotification) {
            Intent intent = new Intent(this, MainActivity.class);
         //   intent.putExtra("isOpenContact", true);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }*/
        super.onBackPressed();
        rootRef.child("Messages").child(currentUserId).removeEventListener(listener);

        this.finish();
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

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void isSuccess(Object response, int ServiceCode) {
       /* try {

        }catch (Exception e){
        }*/


        if (ServiceCode == GET_DATA_FROM_PROFILE_ID) {

            DataFromProfileIdResponse rsp = (DataFromProfileIdResponse) response;

            if (rsp != null) {
                Log.e("GET_DATA_FROM_PROFILE_ID", "isSuccess: rsp " + "Not Null");
                DataFromProfileIdResult result = rsp.getResult();
                if (result != null) {
                    Log.e("GET_DATA_FROM_PROFILE_ID", "isSuccess: result " + "Not Null");
                    Intent viewProfileIntent = new Intent(InboxDetails.this, ViewProfile.class);
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
}


    // private boolean isFirst=false;


