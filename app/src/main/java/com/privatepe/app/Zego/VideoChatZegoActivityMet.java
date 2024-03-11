package com.privatepe.app.Zego;

import static com.privatepe.app.utils.SessionManager.GENDER;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.privatepe.app.IM.GenerateTestUserSig;
import com.privatepe.app.Inbox.DatabaseHandler;
import com.privatepe.app.Inbox.MessageBean;
import com.privatepe.app.Inbox.Messages;
import com.privatepe.app.Inbox.UserInfo;
import com.privatepe.app.Interface.GiftSelectListener;
import com.privatepe.app.R;
import com.privatepe.app.activity.RatingDialogActivityNew;
import com.privatepe.app.adapter.GiftAdapter;
import com.privatepe.app.adapter.GiftAnimationRecyclerAdapter;
import com.privatepe.app.adapter.metend.MessageAdapterVDO;
import com.privatepe.app.dialogs.InsufficientCoinsMyaccount;
import com.privatepe.app.dialogs.WaitingForConnect;
import com.privatepe.app.dialogs.gift.GiftBottomSheetDialog;
import com.privatepe.app.model.EndCallData.EndCallData;
import com.privatepe.app.model.Message_;
import com.privatepe.app.model.RequestGiftRequest.RequestGiftRequest;
import com.privatepe.app.model.WalletBalResponse;
import com.privatepe.app.model.body.CallRecordBody;
import com.privatepe.app.model.gift.Gift;
import com.privatepe.app.model.gift.GiftAnimData;
import com.privatepe.app.model.gift.ResultGift;
import com.privatepe.app.model.gift.SendGiftRequest;
import com.privatepe.app.model.gift.SendGiftResult;
import com.privatepe.app.response.DataFromProfileId.DataFromProfileIdResponse;
import com.privatepe.app.response.DataFromProfileId.DataFromProfileIdResult;
import com.privatepe.app.response.metend.AddRemoveFavResponse;
import com.privatepe.app.response.metend.Misscall.RequestMissCall;
import com.privatepe.app.response.metend.UserListResponseNew.ResultDataNewProfile;
import com.privatepe.app.response.metend.UserListResponseNew.UserListResponseNewData;
import com.privatepe.app.response.newgiftresponse.NewGift;
import com.privatepe.app.response.newgiftresponse.NewGiftListResponse;
import com.privatepe.app.response.newgiftresponse.NewGiftResult;
import com.privatepe.app.retrofit.ApiInterface;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.retrofit.RetrofitInstance;
import com.privatepe.app.services.ItemClickSupport;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.NetworkCheck;
import com.privatepe.app.utils.SessionManager;
import com.squareup.picasso.Picasso;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMSignalingManager;
import com.tencent.imsdk.v2.V2TIMUserStatus;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.liteav.beauty.TXBeautyManager;
import com.tencent.liteav.device.TXDeviceManager;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoChatZegoActivityMet extends BaseActivity implements ApiResponseInterface {

    TXCloudVideoView LocalView, RemoteView;
    String gender;
    ImageView CutCallBtn;

    // ZegoUserService userService;

    private CardView mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private TextView CallerNameText;
    private NetworkCheck networkCheck;

    private ArrayList<Gift> giftArrayList = new ArrayList<>();
    private ArrayList<Message_> message_arrayList = new ArrayList<>();

    private static String token, call_rate, reciverId, unique_id, call_unique_id, UID, isFreeCall = "false", inviteId;

    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ImageView CallerImage;
    private String userId;
    private RecyclerView messagesView;
    private String startLong;
    private ApiManager apiManager;
    //AUTO_END_TIME converted to long
    private long AUTO_END_TIME;
    private String reciverName = "";
    private String user_name = "";
    private String reciverProfilePic = "";
    private String converID = "";
    Chronometer chronometer;
    RelativeLayout rl;
    private ImageView mCallBtn;
    private ImageView mMuteBtn;
    private ImageView mSwitchCameraBtn;


    private RecyclerView rv_gift;
    private GridLayoutManager gridLayoutManager;
    private GiftAdapter giftAdapter;
    private int fPosition;
    private String startTimeStamp;
    private String endTimeStamp;
    private MediaPlayer mp;
    private WaitingForConnect waitingForConnect;
    Handler handler, talkTimeHandler, giftRequestDismissHandler;
    Runnable runnable;
    private Date callStartTime;


    boolean notify = false;
    String currentUserId, currentUserName, receiverImage, userid, newParem;
    DatabaseReference rootRef;
    private boolean passMessage = false;
    private String firebaseOnlineStatus = "", firebaseFCMToken = "";
    private List<MessageBean> messageBeanList = new ArrayList<>();
    private List<MessageBean> chatMessageList = new ArrayList<>();
    private MessageAdapterVDO mMessageAdapter;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private DatabaseHandler dbHandler;
    private LinearLayoutManager mLinearLayoutManager;
    private String contactId = "";

    private static int giftPosition = -1;

    private String inputSentence;
    private List<String> searchWordList;
    private final List<Messages> messagesList = new ArrayList<>();
    private boolean RatingDialog = false;


    ArrayList<EndCallData> list = new ArrayList<>();
    private boolean mMuted;
    private String giftLong = "";


    private ViewStub mBottomViewStub;


    private boolean useExpressCustomCapture = false;

    private RecyclerView giftAnimRecycler;
    List<GiftAnimData> giftdataList = new ArrayList<GiftAnimData>();
    private GiftAnimationRecyclerAdapter giftAnimationRecyclerAdapter;
    // private ZimManager zimManager;
    // private ZimEventListener zimEventListener;

    String callType = "";

    String TAG = "VideoChatZegoActivityMet";
    private String roomID, streamID;
    private boolean callEndCheck = false;

    MediaPlayer mediaPlayer;
    Vibrator vibrator;
    private NewGift giftDatanew;

    Date endTimeVideoEvent = null;
    //6000311(errorcode)
    private boolean userEndsCall = false;
    private int userGiftCount = 0;
    long currentBalance;

    TextView totalBalanceText, totalRemainingMinutesText;
    ArrayList<ResultDataNewProfile> userData = new ArrayList<>();
    private int userIdInt;
    private Handler receiveCallHandler;
    private boolean isCallPicked=false;
    private V2TIMSignalingListener v2TIMSignalingListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        networkCheck = new NetworkCheck();
        setContentView(R.layout.activity_video_chat_zego_met);
        inviteId = getIntent().getStringExtra("inviteId");
        Constant.isReceivedFakeCall = false;
        HashMap<String, String> user = new SessionManager(this).getUserDetails();
        gender = user.get(GENDER);

        giftAnimRecycler = findViewById(R.id.gift_animation_recyclerview);
        totalBalanceText = findViewById(R.id.total_balance);
        totalRemainingMinutesText = findViewById(R.id.remaining_minutes);
        currentBalance = new SessionManager(this).getUserWallet();

        V2TIMManager v2TIMManager = V2TIMManager.getInstance();
        V2TIMSignalingManager v2TIMSignalingManager = V2TIMManager.getSignalingManager();
       v2TIMSignalingListener=  new V2TIMSignalingListener() {
            @Override
            public void onReceiveNewInvitation(String inviteID, String inviter, String groupID, List<String> inviteeList, String data) {
                super.onReceiveNewInvitation(inviteID, inviter, groupID, inviteeList, data);
                Log.e("listensdaa", "Yes invite receive " + inviter);

            }

            @Override
            public void onInviteeAccepted(String inviteID, String invitee, String data) {
                super.onInviteeAccepted(inviteID, invitee, data);
                Log.e("listensdaa", "Yes invite Accept ");


            }

            @Override
            public void onInviteeRejected(String inviteID, String invitee, String data) {
                super.onInviteeRejected(inviteID, invitee, data);
                Log.e("listensdaa", "Yes invite Reject ");
                hangUpCall(true);
                exitRoom();
                finish();
                // addCallEventTODb("video_call_rejected_by_host", "");

            }

            @Override
            public void onInvitationTimeout(String inviteID, List<String> inviteeList) {
                super.onInvitationTimeout(inviteID, inviteeList);
                Log.e("onroomeenterrc", "Yes2 " + isCallPicked+" uid "+unique_id);

                if(!isCallPicked) {
                    Log.e("onroomeenterrc", "Yes3 " + isCallPicked+" uid "+unique_id);

                    addCallEventTODb("video_call_not_answered", "");
                    // hangUpCall(true);
                    endCall();
                    //exitRoom();
                    Toast.makeText(VideoChatZegoActivityMet.this, "Not answering the call", Toast.LENGTH_LONG).show();
                }
            }
        };
        v2TIMSignalingManager.addSignalingListener(v2TIMSignalingListener);


        //  initZegoFu();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inItZegoExpressWithFu();
            }
        }, 1000);
        apiManager = new ApiManager(this, this);

        try {
            token = getIntent().getStringExtra("TOKEN");
            reciverId = getIntent().getStringExtra("ID");
            call_rate = getIntent().getStringExtra("CALL_RATE");
            unique_id = getIntent().getStringExtra("UNIQUE_ID");
            call_unique_id = getIntent().getStringExtra("UNIQUE_ID");
            AUTO_END_TIME = getIntent().getLongExtra("AUTO_END_TIME", 2000);

            apiManager.getProfileIdData(reciverId);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                if (getIntent().hasExtra("userGiftCount"))
                    userGiftCount = getIntent().getIntExtra("userGiftCount", 0);
                else {
                    dbHandler = new DatabaseHandler(getApplicationContext());
                    String userGiftValue = dbHandler.getContactGiftCount(reciverId, currentUserId);
                    if (null != userGiftValue) {
                        userGiftCount = Integer.parseInt(userGiftValue);
                    }
//                dbHandler.close();
                }
            });

            Log.e("AUTO_CUT_TEST", "onCreate: AUTO_END_TIME " + AUTO_END_TIME);

            reciverName = getIntent().getStringExtra("receiver_name");
            reciverProfilePic = getIntent().getStringExtra("receiver_image");
            converID = getIntent().getStringExtra("converID");

            callType = "video";
            // contactId = getIntent().getStringExtra("contactId");
            try {
                isFreeCall = getIntent().getStringExtra("is_free_call");
                UID = getIntent().getStringExtra("UID");
            } catch (Exception e) {
                Log.e("testttst", "onCreate: intent Exception " + e.getMessage());
            }

            Log.e("testttst", "onCreate: " + "isFreeCall  " + isFreeCall);
            Log.e("testttst", "onCreate: " + "AUTO_END_TIME  " + AUTO_END_TIME);

            Log.e("VideoChatActivity", "TOKEN " + token);
            Log.e("VideoChatActivity", "reciverId " + reciverId);
            Log.e("VideoChatActivity", "unique_id " + unique_id);
            Log.e("VideoChatActivity", "converID " + converID);
            Log.e("VideoChatActivity", "AUTO_END " + AUTO_END_TIME + "");
            Log.e("VideoChatActivity", "reciverName " + reciverName);
            Log.e("VideoChatActivity", "reciverProfilePic " + reciverProfilePic);

            if (converID.equals("") || converID.equals("null")) {
                // ((RelativeLayout) findViewById(R.id.rl_chat)).setVisibility(View.GONE);
                //  ((RelativeLayout) findViewById(R.id.rl_giftin)).setVisibility(View.GONE);
            }

            initUI();
            StringBuilder sb = new StringBuilder(reciverName);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

            ((TextView) findViewById(R.id.tv_username)).setText(sb.toString());

            ((TextView) findViewById(R.id.tv_username)).setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Poppins-Regular_0.ttf"));

            //if (gender.equals("male")) {}else {
            Picasso.get().load(reciverProfilePic).into(((ImageView) findViewById(R.id.img_profilepic)));


            List<String> ids = Arrays.asList(reciverId);
            V2TIMManager.getInstance().getUserStatus(ids, new V2TIMValueCallback<List<V2TIMUserStatus>>() {
                @Override
                public void onSuccess(List<V2TIMUserStatus> v2TIMUserStatuses) {
                    // Queried the status successfully
                   // Log.e("offLineDataLog", "from ID status=> " + new Gson().toJson(v2TIMUserStatuses));
                    if (v2TIMUserStatuses.get(0).getStatusType() != 1) {
                        apiManager.sendOfflineCallNotify(reciverId,unique_id);
                    }
                }


                @Override
                public void onError(int code, String desc) {
                    // Failed to query the status
                    //Log.e("offLineDataLog", "error code => " + code + " desc => " + desc);

                }
            });

        } catch (Exception e) {
            Log.e("error", e.getMessage());

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startCallWithExpress();
            }
        }, 1000);


        startRingtone();

        if (!((VideoChatZegoActivityMet.this).isFinishing())) {
            waitingForConnect = new WaitingForConnect(this, reciverProfilePic, reciverName);
        }

        handler = new Handler();
        talkTimeHandler = new Handler();
        giftRequestDismissHandler = new Handler();


        /*  handler.postDelayed(() -> {

            if (waitingForConnect != null) {
                waitingForConnect.dismiss();
            }
            SocketCallOperation socketCallOperation = new SocketCallOperation(new SessionManager(getApplicationContext()).getUserId(), reciverId);
            //socket.emit("misscall", new Gson().toJson(socketCallOperation));

            ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
            RequestMissCall requestMissCall = new RequestMissCall(Long.parseLong(new SessionManager(getApplicationContext()).getUserId()),
                    Integer.parseInt(reciverId), converID, "missedvideocall");

            Call<ResultMissCall> call = apiservice.sendMissCallData(requestMissCall);
            if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                call.enqueue(new Callback<ResultMissCall>() {
                    @Override
                    public void onResponse(Call<ResultMissCall> call, Response<ResultMissCall> response) {
                        //   Log.e("onResponceMissApi", new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<ResultMissCall> call, Throwable t) {
                        //  Log.e("onErrorMissApi", t.getMessage());
                    }
                });
            }
            endCall();
            //   Toast.makeText(this, "Not answering the call", Toast.LENGTH_LONG).show();
        }, 20000);*/

    /*    handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addCallEventTODb("video_call_not_answered", "");
                hangUpCall(true);
                exitRoom();
                Toast.makeText(VideoChatZegoActivityMet.this, "Not answering the call", Toast.LENGTH_LONG).show();
            }
        }, 20000);*/


        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            // ZegoCall();
        }

        new ApiManager(this, this).getWalletAmount2();
        loadLoader();
        //
        // firebaseOperation();
        // mp = MediaPlayer.create(this, R.raw.giftem);

        giftdataList.clear();
        giftAnimRecycler.setLayoutManager(new LinearLayoutManager(this));
        giftAnimationRecyclerAdapter = new GiftAnimationRecyclerAdapter(giftdataList, getApplicationContext(), new GiftAnimationRecyclerAdapter.OnItemInvisibleListener() {
            @Override
            public void onItemInvisible(int adapterpos) {
                //  giftdataList.remove(adapterpos);
            }
        });
        giftAnimRecycler.setAdapter(giftAnimationRecyclerAdapter);

    }

    private void startRingtone() {
        mediaPlayer = MediaPlayer.create(this, R.raw.accept);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }


    private void stopRingtone() {
        mediaPlayer.stop();
        vibrator.cancel();
    }


    private void inItZegoExpressWithFu() {


        initSDK();

        // zimManager.addListener(zimEventListener);
        startCallWithExpress();


    }


    private void startCallWithExpress() {

        roomID = "_room_" + unique_id;
        if (callType.equals("video")) {


        } else {
            // enableCameraImg.setVisibility(View.INVISIBLE);
            // engine.enableCamera(false);
        }
        Log.e(TAG, "startCallWithExpress:  " + callType);

        //start publish stream
        //  streamID = roomID + "_" + new SessionManager(this).getUserId();

        streamID = roomID + "Meetlive_video_call_";


        Log.e(TAG, "startCallWithExpress: " + "Call Done");


    }


    private void hangUpCall(boolean isSelf) {
        Log.e("HANGUP__", "hangUpCall: Start");

        if (isSelf) {
            // ZimManager.sharedInstance().callEnd(reciverId);
            String endCallCommand = getEndCallData();
            sendZegoCustomCommand(endCallCommand);

        }
        if (callType == "video") {
        }
        Log.e("HANGUP__", "hangUpCall: Middle");

        Log.e("HANGUP__", "hangUpCall: End");

        finish();

    }

    private String getEndCallData() {
        JSONObject messageObject = new JSONObject();
        try {
            messageObject.put("isMessageWithCallEnd", "yes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageObject.toString();
    }


    private void initZegoFu() {
        //  mBottomViewStub = (ViewStub) findViewById(R.id.fu_base_bottom);
        //  mBottomViewStub.setInflatedId(R.id.fu_base_bottom);

        //  mFURenderer = new FURenderer.Builder(VideoChatZegoActivityMet.this).maxFaces(4).inputTextureType(0).setOnTrackingStatusChangedListener(this).build();


        //  mBottomViewStub.setLayoutResource(R.layout.layout_fu_beauty);
        //  mBottomViewStub.inflate();
        //  mBeautyControlView = (BeautyControlView) findViewById(R.id.fu_beauty_control);
        //  chooseFilterType = (FUBeautyActivity.FilterType) getIntent().getSerializableExtra("FilterType");
        //  mBeautyControlView.setOnFUControlListener(mFURenderer);
        //  videoBufferType = ZegoVideoBufferType.getZegoVideoBufferType(getIntent().getIntExtra("videoBufferType", 0));
        initSDK();
    }


    @SuppressLint("LongLogTag")
    private void initSDK() {

    }


    public void startPublish(String token, String roomId) {

        Log.e("startpublish", "yes");

        //
//      CustomDialog.createDialog("登录房间中...", this).show();
        /*  String randomSuffix = String.valueOf(new Date().getTime() % (new Date().getTime() / 1000));
        String userID = "user" + randomSuffix;
        String userName = "user" + randomSuffix;*/


    }


    @Override
    public void finish() {
        super.finish();


    }

    JSONObject messageGiftData = new JSONObject();

    private void initUI() {
        LocalView = findViewById(R.id.txcvv_main);
        RemoteView = findViewById(R.id.RemoteView);

        mRemoteUidList = new ArrayList<>();
        mRemoteViewList = new ArrayList<>();
        mRemoteViewList.add((TXCloudVideoView) findViewById(R.id.RemoteView));

        chronometer = findViewById(R.id.chronometer);
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);
        rl = findViewById(R.id.giftRequest);
        mCallBtn = findViewById(R.id.btn_call);
        mMuteBtn = findViewById(R.id.btn_mute);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);

        ((TextView) findViewById(R.id.tv_sendGift)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftAnimation(giftPosition);
            }
        });

        if (gender.equals("male")) {
            mSwitchCameraBtn.setVisibility(View.VISIBLE);
        } else {
            mSwitchCameraBtn.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_giftmsg)).setText("You can request for gift by just tapping on that~");
        }

        ((ImageView) findViewById(R.id.img_send)).setEnabled(false);
        ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.inactivedownloadarrow));


        ((RelativeLayout) findViewById(R.id.rl_chat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.rl_msgsend)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.rl_end)).setVisibility(View.VISIBLE);

                /*
                         if (gender.equals("male")) {

                         } else {
                             ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                         }

               */

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(((EditText) findViewById(R.id.et_message)).getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                ((EditText) findViewById(R.id.et_message)).requestFocus();
            }
        });


        mRemoteContainer.setOnClickListener(view -> {
            if (((RelativeLayout) findViewById(R.id.rl_bottom)).getVisibility() == View.VISIBLE) {
                ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                hideKeybaord(view);
            }
            if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
                ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
                messagesView.setVisibility(View.VISIBLE);
            }
        });

        initKeyBoardListener();
        messagesView = (RecyclerView) findViewById(R.id.lv_allmessages);
        rv_gift = findViewById(R.id.rv_gift);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.HORIZONTAL, false);
        rv_gift.setLayoutManager(gridLayoutManager);
        giftAdapter = new GiftAdapter(giftArrayList, R.layout.rv_gift, getApplicationContext());
        rv_gift.setAdapter(giftAdapter);


        findViewById(R.id.rl_giftin).setOnClickListener(view -> {
            messagesView.setVisibility(View.GONE);
            findViewById(R.id.rl_giftin).setEnabled(false);
            // ((ImageView) findViewById(R.id.img_gift)).performClick();

            NewGiftListResponse response = new SessionManager(VideoChatZegoActivityMet.this).getCategoryGiftList();

            if (response != null) {
                Log.e("NewGiftListResponse2233", "initUI: NewGiftListResponse " + new Gson().toJson(response));

                bottomSheet = new GiftBottomSheetDialog(VideoChatZegoActivityMet.this, (ArrayList<NewGiftResult>) response.getResult(), new GiftSelectListener() {
                    @Override
                    public void OnGiftSelect(NewGift giftData) {
                        Log.e("VC_NEWGIFTTESTT", "OnGiftSelect: " + giftData);
                        Log.e("VC_NEWGIFTTESTT1", "onBindViewHolder: " + giftData.getGift_name());

                        int giftId = giftData.getId();
                        int giftAmount = (int) giftData.getAmount();
                        Log.e("GiftCoinTest", "giftAmount: " + giftAmount);

                        giftDatanew = giftData;

                        long currentCoin = new SessionManager(VideoChatZegoActivityMet.this).getUserWallet();
                        Log.e("GiftCoinTest", "currentCoin: " + currentCoin);


                        long sec = System.currentTimeMillis() / 1000;
                        Log.e("GiftCoinTest", "sec: " + sec);


                        long tillnow = sec - Long.parseLong(startLong);
                        Log.e("GiftCoinTest", "tillnow: " + tillnow);


                        long ECB = (currentCoin - 2 * Integer.parseInt(call_rate)) - (tillnow * Integer.parseInt(call_rate));


                        Log.e("GiftCoinTest", "initUI: " + ECB);

                        if (currentCoin > giftAmount) {
                            fPosition = 0;

                            String giftDataString = getGifData(giftArrayList.get(fPosition).getId(), new SessionManager(VideoChatZegoActivityMet.this).getUserName(), new SessionManager(VideoChatZegoActivityMet.this).getUserProfilepic(), giftDatanew);
                            sendZegoCustomCommand(giftDataString);
                            messageGiftData = setGifData(giftArrayList.get(fPosition).getId(), new SessionManager(VideoChatZegoActivityMet.this).getUserName(), new SessionManager(VideoChatZegoActivityMet.this).getUserProfilepic(), giftDatanew);
                            Log.e("chdsksaa", "giftIddd " + giftArrayList.get(fPosition).getId());

                            NewGiftAnimation(giftArrayList.get(fPosition).getId(), new SessionManager(VideoChatZegoActivityMet.this).getUserName(), new SessionManager(VideoChatZegoActivityMet.this).getUserProfilepic(), giftDatanew);
                            new ApiManager(getApplicationContext(), VideoChatZegoActivityMet.this).sendUserGift(new SendGiftRequest(Integer.parseInt(reciverId), call_unique_id, giftId, giftAmount, startTimeStamp, String.valueOf(System.currentTimeMillis())));

                            new ApiManager(getApplicationContext(), VideoChatZegoActivityMet.this).addUserGift(reciverId);
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            Handler handler = new Handler(Looper.getMainLooper());
                            final MessageBean[] messageBean = new MessageBean[1];

                            executor.execute(() -> {
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                String profilePic = sessionManager.getUserProfilepic();
                                currentUserId = sessionManager.getUserId();
                                currentUserName = sessionManager.getUserName();

                                Messages message = new Messages();
                                message.setFrom(currentUserId);
                                message.setFromName(currentUserName);
                                message.setMessage(String.valueOf(giftDatanew.getId()));
                                message.setFromImage(profilePic);
                                message.setTime_stamp(System.currentTimeMillis());
                                message.setType("gift");
                                String timestamp = System.currentTimeMillis() + "";

                                messageBean[0] = new MessageBean(currentUserId, message, true, timestamp);
                                dbHandler = new DatabaseHandler(getApplicationContext());
                                String contactId = insertOrUpdateContact(messageBean[0].getMessage(), reciverId, reciverName, reciverProfilePic, timestamp);
                        /*if (TextUtils.isEmpty(this.contactId)) {
                         this.contactId = contactId;
                        }*/
                                messageBean[0].setAccount(contactId);
                                insertChat(messageBean[0]);
                                userGiftCount = 1;
                                handler.post(() -> {
                                    //UI Thread work here
                                    Intent intentVideoCall = new Intent("VIDEO-CALL-EVENT");
                                    intentVideoCall.putExtra("msg", messageBean[0]);
                                    intentVideoCall.putExtra("userGiftCount", userGiftCount);
                                    sendBroadcast(intentVideoCall);

                                });
                            });

                        } else {
                            Log.e("GiftCoinTest", "Out of Balance");
                            Toast.makeText(VideoChatZegoActivityMet.this, "Out of Balance", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                if (!((VideoChatZegoActivityMet.this).isFinishing())) {
                    bottomSheet.show(VideoChatZegoActivityMet.this.getSupportFragmentManager(), "GiftBottomSheet");
                }


            } else {

                Log.e("NewGiftListResponse2233", "initUI: NewGiftListResponse null");
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.rl_giftin).setEnabled(true);
                }
            }, 1000);

        });


        ((ImageView) findViewById(R.id.img_gift)).setOnClickListener(v -> {

            hideKeybaord(v);


            if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.GONE) {
                ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.rl_msgsend)).setVisibility(View.GONE);
                ((RelativeLayout) findViewById(R.id.rl_end)).setVisibility(View.GONE);
                ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.VISIBLE);

                ApiInterface apiservice = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
                String authToken = Constant.BEARER + new SessionManager(getApplicationContext()).getUserToken();
                Call<ResultGift> call = apiservice.getGift(authToken);

                if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                    call.enqueue(new Callback<ResultGift>() {
                        @Override
                        public void onResponse(Call<ResultGift> call, Response<ResultGift> response) {
                            if (response.body().isStatus()) {
                                ((ImageView) findViewById(R.id.img_giftloader)).setVisibility(View.GONE);
                                giftArrayList.clear();
                                giftArrayList.addAll(response.body().getResult());
                                giftAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultGift> call, Throwable t) {
                            Log.e("Error", t.getLocalizedMessage());
                            Log.e("Error2", t.getMessage());

                        }
                    });


                }


            } else {

                ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);

            }


        });

        /*ItemClickSupport.addTo(rv_gift).setOnItemClickListener((recyclerView, position, v) -> {
            // Log.e(TAG, "initUI: autocut before gift sent  " + AUTO_END_TIME);

            if (gender.equals("male")) {
                //  int currentCoin = Integer.parseInt(((TextView) findViewById(R.id.tv_coinchat)).getText().toString());
                long currentCoin = new SessionManager(VideoChatZegoActivityMet.this).getUserWallet();

                Log.e("GiftCoinTest", "initUI: current balance " + currentCoin);

                Log.e("GiftCoinTest", "initUI: current gift amount " + giftArrayList.get(position).getAmount());

                //  Log.e("GiftCoinTest", "initUI: current balance session " + new SessionManager(VideoChatZegoActivityMet.this).getUserWallet());
                //   currentCoin = currentCoin - giftArrayList.get(position).getAmount();
                //    long CoinsIsValidForGifts =  new SessionManager(VideoChatZegoActivityMet.this).setUserWall(rsp.getResult());

                long sec = System.currentTimeMillis() / 1000;

                long tillnow = sec - Long.parseLong(startLong);

                Log.e("GiftCoinTest", "initUI: " + tillnow);

                long ECB = (currentCoin - 2 * Integer.parseInt(call_rate)) - (tillnow * Integer.parseInt(call_rate));


                Log.e("GiftCoinTest", "initUI: " + ECB+ " giftamount => "+giftArrayList.get(position).getAmount());


                if (ECB > giftArrayList.get(position).getAmount()) {
                    fPosition = position;
                    new ApiManager(getApplicationContext(), VideoChatZegoActivityMet.this).sendUserGift(new SendGiftRequest(Integer.parseInt(reciverId), call_unique_id, giftArrayList.get(position).getId(), giftArrayList.get(position).getAmount(), startTimeStamp, String.valueOf(System.currentTimeMillis())));
                } else {
                    Toast.makeText(VideoChatZegoActivityMet.this, "Out of Balance", Toast.LENGTH_LONG).show();
                }


            } else {

                apiManager.hostSendGiftRequest(new RequestGiftRequest(String.valueOf(giftArrayList.get(position).getId()), reciverId));
            }

        });*/


        ((EditText) findViewById(R.id.et_message)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    ((ImageView) findViewById(R.id.img_send)).setEnabled(true);
                    ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.activedownloadarrow));
                } else {
                    ((ImageView) findViewById(R.id.img_send)).setEnabled(false);
                    ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.inactivedownloadarrow));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ((ImageView) findViewById(R.id.img_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeybaord(view);
                sendMessage("text", "", "");
            }
        });


        ((EditText) findViewById(R.id.et_message)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                ((ImageView) findViewById(R.id.img_send)).performClick();
                return true;
            }
        });

        loadGiftData();

        enterRoom();
    }

    GiftBottomSheetDialog bottomSheet;
    private TRTCCloud mTRTCCloud;
    private TXDeviceManager mTXDeviceManager;

    private List<String> mRemoteUidList;
    private List<TXCloudVideoView> mRemoteViewList;

    private void enterRoom() {
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(new TRTCCloudImplListener(VideoChatZegoActivityMet.this));
        initCallBeautyParams();
        new FloatView(VideoChatZegoActivityMet.this,getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight()-150).initGestureListener(findViewById(R.id.smallViewRLay));

        mTXDeviceManager = mTRTCCloud.getDeviceManager();
        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        trtcParams.userId = reciverId;
        trtcParams.strRoomId = unique_id;
        trtcParams.userSig = GenerateTestUserSig.genTestUserSig(trtcParams.userId);
        trtcParams.role = TRTCCloudDef.TRTCRoleAnchor;

        mTRTCCloud.startLocalPreview(true, LocalView);
        mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT);
        mTRTCCloud.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_LIVE);
        //mTRTCCloud.setBeautyStyle(2, 2, 2, 2);
        // mTRTCCloud.setVideoEncoderParam((TUICommonDefine.VideoEncoderParams params, TUICommonDefine.Callback callback);

        TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
        encParam.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_480;
        encParam.videoBitrate = 1200;
        encParam.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
        encParam.videoFps = 15;
        mTRTCCloud.setVideoEncoderParam(encParam);

    }

private void initCallBeautyParams() {
        mTRTCCloud.getBeautyManager().setBeautyStyle(TXBeautyManager.TXBeautyStyleNature);
        mTRTCCloud.getBeautyManager().setWhitenessLevel(3f);
        mTRTCCloud.getBeautyManager().setBeautyLevel(6f);
    }
    private class TRTCCloudImplListener extends TRTCCloudListener {

        private WeakReference<VideoChatZegoActivityMet> mContext;

        public TRTCCloudImplListener(VideoChatZegoActivityMet activity) {
            super();
            mContext = new WeakReference<>(activity);
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            Log.d(TAG,
                    "onUserVideoAvailable userId " + userId + ", mUserCount " + ",available " + available);
            int index = mRemoteUidList.indexOf(userId);
            if (available) {
                if (index != -1) {
                    return;
                }
                mRemoteUidList.add(userId);
                refreshRemoteVideoViews();
            } else {
                if (index == -1) {
                    return;
                }
                mTRTCCloud.stopRemoteView(userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                mRemoteUidList.remove(index);
                refreshRemoteVideoViews();
            }
        }

        private void refreshRemoteVideoViews() {
            for (int i = 0; i < mRemoteViewList.size(); i++) {
                if (i < mRemoteUidList.size()) {
                    String remoteUid = mRemoteUidList.get(i);
                    mRemoteViewList.get(i).setVisibility(View.VISIBLE);
                    mTRTCCloud.startRemoteView(remoteUid, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                            mRemoteViewList.get(i));
                } else {
                    mRemoteViewList.get(i).setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onRemoteUserLeaveRoom(String userId, int reason) {
            super.onRemoteUserLeaveRoom(userId, reason);
            Log.e("testttst", "onRemoteUserLeaveRoom");
            endCall();
        }

        @Override
        public void onRemoteUserEnterRoom(String userId) {
            super.onRemoteUserEnterRoom(userId);
            isCallPicked=true;
            Log.e("onroomeenterrc", "Yes1 " + isCallPicked+" uid "+unique_id);
            receiveCallHandler = new Handler();
            receiveCallHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (waitingForConnect != null) {


                        waitingForConnect.dismiss();
                        stopRingtone();
                        waitingForConnect = null;
                    }
                    handler.removeCallbacksAndMessages(null);

                    startTimeStamp = String.valueOf(System.currentTimeMillis());
                    callStartTime = Calendar.getInstance().getTime();
                    RatingDialog = true;

                    callStartTime = Calendar.getInstance().getTime();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                    Long tsLong = System.currentTimeMillis() / 1000;
                    startLong = tsLong.toString();
                    Log.e("AUTO_END_TIME_TEST", "run:1 " + AUTO_END_TIME);

                    if (gender.equals("male")) {

                        Log.e("AUTO_END_TIME_TEST", "run:2 " + AUTO_END_TIME);
                        apiManager.sendCallRecord(new CallRecordBody(UID, unique_id, new CallRecordBody.Duration(String.valueOf(System.currentTimeMillis()), "")));
                        startTimeStamp = String.valueOf(System.currentTimeMillis());
                        Log.e("startCallReq", new Gson().toJson(new CallRecordBody(UID, unique_id, new CallRecordBody.Duration(String.valueOf(System.currentTimeMillis()), ""))));


                        Log.e("AUTO_END_TIME_TEST", "run:3 " + AUTO_END_TIME);

                        Log.e("AUTO_CUT_TEST", "handler " + AUTO_END_TIME);


                        // Disconnect call when balance ends
                        talkTimeHandler.postDelayed(() -> {

                            endTimeStamp = String.valueOf(System.currentTimeMillis());
                            callEndCheck = true;
                            Log.e(TAG, "onCallInvitationAccepted: " + "hangup");
                            Log.e("testttst", "onRemoteUserEnterRoom auto time end");
                            endCall();

                            Toast.makeText(VideoChatZegoActivityMet.this, "Out of Balance", Toast.LENGTH_LONG).show();

                            Log.e("AUTO_CUT_TEST", "handler cut call from function");

                        }, AUTO_END_TIME);

                        Log.e("AUTO_END_TIME_TEST", "run:4 " + AUTO_END_TIME);
                        updatedInfo();
                    }
                }
            }, 100);
        }

        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
            VideoChatZegoActivityMet activity = mContext.get();
            if (activity != null) {
                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    Log.e("testttst", "sdk callback error");
                    endCall();
                }
            }
        }
    }

    private void exitRoom() {
        if (mTRTCCloud != null) {
            mTRTCCloud.stopLocalAudio();
            mTRTCCloud.stopLocalPreview();
            mTRTCCloud.exitRoom();
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
    }

    private String getGifData(int position, String userName, String profilePic, NewGift giftDatanew) {

        JSONObject messageObject = new JSONObject();
        JSONObject GiftDataObject = new JSONObject();
        String giftData = new Gson().toJson(giftDatanew);

        try {
            GiftDataObject.put("GiftPosition", String.valueOf(position));
            GiftDataObject.put("UserName", userName);
            GiftDataObject.put("ProfilePic", profilePic);
            GiftDataObject.put("GiftData", giftData);
            messageObject.put("isMessageWithGift", "yes");
            messageObject.put("GiftMessageBody", GiftDataObject.toString());
        } catch (JSONException e) {

        }

        return messageObject.toString();
    }

    private JSONObject setGifData(int position, String userName, String profilePic, NewGift giftDatanew) {

        JSONObject messageObject = new JSONObject();
        JSONObject GiftDataObject = new JSONObject();
        String giftData = new Gson().toJson(giftDatanew);

        try {
            //GiftDataObject.put("GiftPosition", String.valueOf(position));
            messageObject.put("UserName", userName);
            messageObject.put("ProfilePic", profilePic);
            GiftDataObject.put("GiftData", giftData);
            messageObject.put("isMessageWithGift", "yes");
            messageObject.put("GiftPosition", String.valueOf(position));
            messageObject.put("GiftMessageBody", GiftDataObject.toString());
            messageObject.put("GiftImage", giftDatanew.getImage());


        } catch (JSONException e) {

        }

        return messageObject;
    }

    private void addCallEventTODb(String type, String duration) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        final MessageBean[] messageBean = new MessageBean[1];

        executor.execute(() -> {
            //Background work here
            try {
                boolean beSelf = false;
                String msg = "";
                if (type.equals("video_call_cancelled")) {
                    msg = "Call canceled";
                } else if (type.equals("video_call_rejected_by_host")) {
                    msg = "Call rejected";
                    JSONObject jsonResult = new JSONObject();

                    try {
                        jsonResult.put("type", "video_call_event");

                        jsonResult.put("caller_name", new SessionManager(VideoChatZegoActivityMet.this).getName());
                        jsonResult.put("userId", String.valueOf(userId));

                        jsonResult.put("unique_id", 0);
                        jsonResult.put("caller_image", new SessionManager(VideoChatZegoActivityMet.this).getUserProfilepic());
                        jsonResult.put("callRate", 1);
                        jsonResult.put("isFreeCall", "false");
                        jsonResult.put("totalPoints", new SessionManager(VideoChatZegoActivityMet.this).getUserWallet());
                        jsonResult.put("remainingGiftCards", "0");
                        jsonResult.put("freeSeconds", "0");

                        jsonResult.put("message", "Call rejected " + duration);
                        jsonResult.put("from", new SessionManager(VideoChatZegoActivityMet.this).getUserId());
                        jsonResult.put("fromName", new SessionManager(VideoChatZegoActivityMet.this).getUserName());
                        jsonResult.put("fromImage", new SessionManager(VideoChatZegoActivityMet.this).getUserProfilepic());
                        jsonResult.put("time_stamp", System.currentTimeMillis());
                        String msg3 = jsonResult.toString();
                        V2TIMManager.getInstance().sendC2CTextMessage(msg3,
                                reciverId, new V2TIMValueCallback<V2TIMMessage>() {
                                    @Override
                                    public void onSuccess(V2TIMMessage message) {
                                        // The one-to-one text message sent successfully
                                        Log.e("MessageSentCall", "success to => " + reciverId + " with message => " + new Gson().toJson(message));
                                    }


                                    @Override
                                    public void onError(int code, String desc) {

                                    }
                                });
                    }catch (Exception e){

                    }
                } else if (type.equals("video_call_not_answered")) {
                    msg = "Call was not answered";
                } else if (type.equals("video_call_ended_by_host")) {
                    msg = "Call ended";
                } else if (type.equals("video_call_self_cancelled")) {
                    msg = "Call canceled";
                    beSelf = true;
                    JSONObject jsonResult = new JSONObject();

                    try {
                        jsonResult.put("type", "video_call_event");

                        jsonResult.put("caller_name", new SessionManager(VideoChatZegoActivityMet.this).getName());
                        jsonResult.put("userId", String.valueOf(userId));

                        jsonResult.put("unique_id", 0);
                        jsonResult.put("caller_image", new SessionManager(VideoChatZegoActivityMet.this).getUserProfilepic());
                        jsonResult.put("callRate", 1);
                        jsonResult.put("isFreeCall", "false");
                        jsonResult.put("totalPoints", new SessionManager(VideoChatZegoActivityMet.this).getUserWallet());
                        jsonResult.put("remainingGiftCards", "0");
                        jsonResult.put("freeSeconds", "0");

                        jsonResult.put("message", "Call Cancelled " + duration);
                        jsonResult.put("from", new SessionManager(VideoChatZegoActivityMet.this).getUserId());
                        jsonResult.put("fromName", new SessionManager(VideoChatZegoActivityMet.this).getUserName());
                        jsonResult.put("fromImage", new SessionManager(VideoChatZegoActivityMet.this).getUserProfilepic());
                        jsonResult.put("time_stamp", System.currentTimeMillis());
                        String msg3 = jsonResult.toString();
                        V2TIMManager.getInstance().sendC2CTextMessage(msg3,
                                reciverId, new V2TIMValueCallback<V2TIMMessage>() {
                                    @Override
                                    public void onSuccess(V2TIMMessage message) {
                                        // The one-to-one text message sent successfully
                                        Log.e("MessageSentCall", "success to => " + reciverId + " with message => " + new Gson().toJson(message));
                                    }


                                    @Override
                                    public void onError(int code, String desc) {

                                    }
                                });
                    }catch (Exception e){

                    }
                } else if (type.equals("video_call_completed")) {
                    msg = "Call Completed " + duration;
                } else if (type.equals("video_call_completed_user")) {
                    msg = "Call Completed " + duration;
                    beSelf = true;
                    JSONObject jsonResult = new JSONObject();

                    try {
                        jsonResult.put("type", "video_call_event");

                        jsonResult.put("caller_name", new SessionManager(VideoChatZegoActivityMet.this).getName());
                        jsonResult.put("userId", String.valueOf(userId));

                        jsonResult.put("unique_id", 0);
                        jsonResult.put("caller_image", new SessionManager(VideoChatZegoActivityMet.this).getUserProfilepic());
                        jsonResult.put("callRate", 1);
                        jsonResult.put("isFreeCall", "false");
                        jsonResult.put("totalPoints", new SessionManager(VideoChatZegoActivityMet.this).getUserWallet());
                        jsonResult.put("remainingGiftCards", "0");
                        jsonResult.put("freeSeconds", "0");

                        jsonResult.put("message", "Call Completed " + duration);
                        jsonResult.put("from", new SessionManager(VideoChatZegoActivityMet.this).getUserId());
                        jsonResult.put("fromName", new SessionManager(VideoChatZegoActivityMet.this).getUserName());
                        jsonResult.put("fromImage", new SessionManager(VideoChatZegoActivityMet.this).getUserProfilepic());
                        jsonResult.put("time_stamp", System.currentTimeMillis());
                        String msg3 = jsonResult.toString();
                        V2TIMManager.getInstance().sendC2CTextMessage(msg3,
                                reciverId, new V2TIMValueCallback<V2TIMMessage>() {
                                    @Override
                                    public void onSuccess(V2TIMMessage message) {
                                        // The one-to-one text message sent successfully
                                        Log.e("MessageSentCall", "success to => " + reciverId + " with message => " + new Gson().toJson(message));
                                    }


                                    @Override
                                    public void onError(int code, String desc) {

                                    }
                                });
                    } catch (Exception e) {

                    }

                }
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                String profilePic = sessionManager.getUserProfilepic();
                currentUserId = sessionManager.getUserId();
                currentUserName = sessionManager.getUserName();

                Messages message = new Messages();
                message.setFrom(currentUserId);
                message.setFromName(currentUserName);
                message.setMessage(msg);
                message.setFromImage(profilePic);
                message.setTime_stamp(System.currentTimeMillis());
                message.setType("video_call_event");
                String timestamp = System.currentTimeMillis() + "";
                Log.e("chejckaa", "Yesss " + msg);
                messageBean[0] = new MessageBean(currentUserId, message, beSelf, timestamp);
                dbHandler = new DatabaseHandler(getApplicationContext());
                String contactId = insertOrUpdateContact(messageBean[0].getMessage(), reciverId, reciverName, reciverProfilePic, timestamp);
            /*if (TextUtils.isEmpty(this.contactId)) {
                this.contactId = contactId;
            }*/
                messageBean[0].setAccount(contactId);
                insertChat(messageBean[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.post(() -> {
                //UI Thread work here
                Intent intentVideoCall = new Intent("VIDEO-CALL-EVENT");
                intentVideoCall.putExtra("msg", messageBean[0]);
                sendBroadcast(intentVideoCall);

            });
        });

    }

    private void loadGiftData() {
        ApiInterface apiservice = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        String authToken = Constant.BEARER + new SessionManager(getApplicationContext()).getUserToken();
        Call<ResultGift> call = apiservice.getGift(authToken);
        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultGift>() {
                @Override
                public void onResponse(Call<ResultGift> call, Response<ResultGift> response) {
                    if (response.body().isStatus()) {
                        giftArrayList.clear();
                        giftArrayList.addAll(response.body().getResult());
                        giftAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ResultGift> call, Throwable t) {

                }
            });
        }


    }


/*
    private void NewGiftAnimation(int giftId, String peerName, String peerProfilePic) {



        giftdataList.add(new GiftAnimData(getGiftResourceId(giftId), peerName, peerProfilePic));
        giftAnimRecycler.setVisibility(View.VISIBLE);
        giftAnimRecycler.getAdapter().notifyDataSetChanged();
    }
*/


    int incPos = 0;
    private boolean isStackof3Full = false;


    private void NewGiftAnimation(int giftId, String peerName, String peerProfilePic, NewGift newgift) {

        mp = MediaPlayer.create(this, R.raw.giftem);
        mp.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.stop();
            }
        }, 3000);

        giftAnimRecycler.setVisibility(View.VISIBLE);

        if (!isStackof3Full) {
            // giftdataList.add(new GiftAnimData(getGiftResourceId(giftId), peerName, peerProfilePic));
            giftdataList.add(new GiftAnimData(0, peerName, peerProfilePic, newgift));
            giftAnimationRecyclerAdapter.notifyItemInserted(incPos);
        } else {
            //  giftdataList.set(incPos, new GiftAnimData(0, peerName, peerProfilePic));
            giftdataList.set(incPos, new GiftAnimData(0, peerName, peerProfilePic, newgift));
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
        } else if (type.equals("gift")) {
            msg = giftId;

        } else if (type.equals("gift_request")) {
            int request_status = 0;
            String tagLine = "Please give me this gift :" + giftId + ":" + request_status + ":" + giftAmount;
            msg = tagLine;

        } else if (type.equals("call_request")) {
            int request_status = 0;
            String tagLine = "Call me :" + request_status;
            msg = tagLine;
        } else if (type.equals("ss")) {
            //   msg = ssUrl;
        }

       /* Log.e("messageType",type);
        Log.e("messageData",msg);*/

        if (!msg.isEmpty()) {

            //       String msgSenderRef = "Messages/" + currentUserId;
            String msgReceiverRef = "Messages/" + reciverId;

            DatabaseReference dbReference = rootRef.child("Messages").child(currentUserId).child(reciverId).push();
            String messagePushId = "";

            String profilePic = new SessionManager(getApplicationContext()).getUserProfilepic();
            //  Log.e("profilePicLog", profilePic);

            Map messageTextBody = new HashMap();
            messageTextBody.put("type", type);
            messageTextBody.put("message", msg);
            messageTextBody.put("from", currentUserId);
            messageTextBody.put("fromName", currentUserName);
            messageTextBody.put("fromImage", profilePic);
            messageTextBody.put("time_stamp", System.currentTimeMillis());
            //  messageTextBody.put("is_seen", false);

            Map messageBodyDetails = new HashMap();
            //      messageBodyDetails.put(msgSenderRef + "/" + messagePushId, messageTextBody);
            messageBodyDetails.put(msgReceiverRef + "/" + messagePushId, messageTextBody);


            ((EditText) findViewById(R.id.et_message)).setText("");

            Messages message = new Messages();
            message.setFrom(currentUserId);
            message.setFromName(currentUserName);
            message.setMessage(msg);
            message.setFromImage(profilePic);
            message.setTime_stamp(System.currentTimeMillis());
            message.setType(type);


            messagesList.add(message);
            mMessageAdapter.notifyDataSetChanged();
            messagesView.smoothScrollToPosition(messagesList.size());

           /* if (type.equals("text")) {
            }*/


            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(task -> {
                // Is message sent successfully
                if (task.isSuccessful()) {

                    // Update record in contact list
               /*     addOrUpdateValueInContactList();
                    isFirstMessage = false;*/

                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            });


            String timestamp = System.currentTimeMillis() + "";
            MessageBean messageBean = new MessageBean(currentUserId, message, true, timestamp);
            updateChatAdapter(messageBean);
            String contactId = insertOrUpdateContact(messageBean.getMessage(), reciverId, reciverName, reciverProfilePic, timestamp);
            if (TextUtils.isEmpty(this.contactId)) {
                this.contactId = contactId;
            }
            messageBean.setAccount(contactId);

            insertChat(messageBean);

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


    private void insertChat(MessageBean messageBean) {
        dbHandler.addChat(messageBean);
    }


    private String insertOrUpdateContact(Messages message, String userId, String profileName, String profileImage, String timestamp) {
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
            userInfo.setGift_count(String.valueOf(userGiftCount));
            contactId = dbHandler.addContact(userInfo);
        } else { //update
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


    private void ZegoCall() {

      /*  ZegoVideoConfig videoConfig = new ZegoVideoConfig(ZegoVideoConfigPreset.PRESET_720P);
        ZegoExpressEngine.getEngine().setVideoConfig(videoConfig);
*/

      /*
        userService = ZegoRoomManager.getInstance().userService;
     //   userService.startPlaying(new SessionManager(this).getUserId(), LocalView);
        userService.setListener(new ZegoUserServiceListener() {
            @Override
            public void onUserInfoUpdated(ZegoUserInfo userInfo) {

            }

            @Override
            public void onReceiveCallInvite(ZegoUserInfo userInfo, ZegoCallType type) {

            }

            @Override
            public void onReceiveCallCanceled(ZegoUserInfo userInfo, ZegoCancelType cancelType) {


                Log.e("callzeggo", "cancel type " + cancelType);
                if (cancelType == ZegoCancelType.TIMEOUT) {

                    userService.cancelCall(ZegoCancelType.INTENT, reciverId, errorCode -> {
                        finish();
                    });
                }

            }

            @Override
            public void onReceiveCallResponse(ZegoUserInfo userInfo, ZegoResponseType type) {
                Log.e("callzeggo", "OnRecieveResponse " + type);

                if (waitingForConnect != null) {
                    waitingForConnect.dismiss();
                }

                if (type == ZegoResponseType.Accept) {
                    userService.startPlaying(reciverId, RemoteView);

                    startTimeStamp = String.valueOf(System.currentTimeMillis());
                    callStartTime = Calendar.getInstance().getTime();
                    RatingDialog = true;

                    callStartTime = Calendar.getInstance().getTime();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();


                    if (gender.equals("male")) {
                        apiManager.sendCallRecord(new CallRecordBody(UID, unique_id, new CallRecordBody.Duration(String.valueOf(System.currentTimeMillis()), "")));
                        startTimeStamp = String.valueOf(System.currentTimeMillis());
                        Log.e("startCallReq", new Gson().toJson(new CallRecordBody(UID, unique_id, new CallRecordBody.Duration(String.valueOf(System.currentTimeMillis()), ""))));

                        // Disconnect call when balance ends

                        talkTimeHandler.postDelayed(() -> {
                            endCall();
                            Toast.makeText(VideoChatZegoActivityMet.this, "Out of Balance", Toast.LENGTH_LONG).show();
                        }, AUTO_END_TIME);
                    }


                }
                if (type == ZegoResponseType.Reject) {
                    userService.cancelCall(ZegoCancelType.INTENT, reciverId, errorCode -> {
                        finish();
                    });
                }

            }

            @Override
            public void onReceiveCallEnded() {
                Log.e("callzeggo", "call ended");


                userService.cancelCall(ZegoCancelType.INTENT, reciverId, errorCode -> {
                    //startActivity(new Intent(VideoChatZegoActivityMet.this,MainActivity.class));

                    if (!gender.equals("male")) {
                        apiManager.getcallCutByHost(unique_id);
                        finish();
                    } else {
                        endCall();
                    }

                });


                //  userService.endCall(errorCode -> {
                //      startActivity(new Intent(VideoChatZegoActivityMet.this,MainActivity.class));
                //      finish();
                //  });


            }

            @Override
            public void onReceiveZIMPeerMessage(ZIMMessage zimMessage, String fromUserID) {
                Log.e("ZegoListenerBug", "onReceiveZIMPeerMessage: VideoChatActivity " );

            }

            @Override
            public void onConnectionStateChanged(ZIMConnectionState state, ZIMConnectionEvent event) {

                Log.e("callzeggo", "OnConnectionStateChange " + state);
            }

            @Override
            public void onNetworkQuality(String userID, ZegoNetWorkQuality quality) {

                Log.e("callzeggo", "NetworkQuality " + quality);


            }
        });
        */


    }

    private void updateChatAdapter(MessageBean messageBean) {
        messageBeanList.add(0, messageBean);
        addMessageInChatList(messageBean);
        mMessageAdapter.notifyDataSetChanged();
        messagesView.scrollToPosition(0);
    }

    private synchronized void updateUnreadMsgCount(String profileId) {
        UserInfo userInfoFromDb = dbHandler.getContactInfo(profileId, currentUserId);
        if (userInfoFromDb != null) {
            // set unread count 0
            userInfoFromDb.setUnread_msg_count("0");
            dbHandler.updateContact(userInfoFromDb);
        }
    }

    public void onLocalContainerClick(View view) {
        switchView(RemoteView);
        switchView(LocalView);
    }


    private void switchView(TXCloudVideoView textureView) {
        ViewGroup parent = removeFromParent(textureView);
        if (parent == mLocalContainer) {
            mRemoteContainer.addView(textureView);
        } else if (parent == mRemoteContainer) {
            mLocalContainer.addView(textureView);
        }

    }


    private ViewGroup removeFromParent(TXCloudVideoView textureView) {
        if (textureView != null) {
            ViewParent viewParent = textureView.getParent();
            if (viewParent != null) {
                ViewGroup viewGroup = (ViewGroup) viewParent;
                viewGroup.removeView(textureView);
                return viewGroup;
            }
        }
        return null;
    }


    private void addMessageInChatList(MessageBean msgBean) {
        try {
            String dateCurr = getDateByTimestamp(msgBean.getTimestamp());
            String datePrev = getDateByTimestamp(messageBeanList.get(0).getTimestamp());
            chatMessageList.add(0, msgBean);
            if (!dateCurr.equalsIgnoreCase(datePrev)) {
                MessageBean messageBean = new MessageBean();
                messageBean.setMsgDate(dateCurr);
                chatMessageList.add(1, msgBean);
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
                return;
            }

            String dateCurr = "";
            for (int i = 1; i < messageBeanList.size(); i++) {
                dateCurr = getDateByTimestamp(messageBeanList.get(i).getTimestamp());
                String datePrev = getDateByTimestamp(messageBeanList.get(i - 1).getTimestamp());
                if (dateCurr.equalsIgnoreCase(datePrev)) {
                    chatMessageList.add(messageBeanList.get(i - 1));
                } else {
                    MessageBean messageBean = new MessageBean();
                    messageBean.setMsgDate(datePrev);
                    chatMessageList.add(messageBean);
                }
            }
            chatMessageList.add(messageBeanList.get(messageBeanList.size() - 1));
            MessageBean messageBean = new MessageBean();
            messageBean.setMsgDate(dateCurr);
            chatMessageList.add(messageBean);

            /*if (unreadMsgCount > 0) {
                MessageBean messageBeanUnread = new MessageBean();
                messageBeanUnread.setMsgDate("Unread");
                chatMessageList.add(unreadMsgCount, messageBeanUnread);
            }*/

        } catch (Exception e) {
            //
        }
    }


    private void initScrollListner() {
        messagesView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            List<MessageBean> messageListBeans = dbHandler.getChatList(contactId, messageBeanList.size(), 10);
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


    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getStringExtra("action");

            if (action.equals("end")) {
               /*
                removeFromParent(LocalView);
                removeFromParent(RemoteView);
                */

                Log.e(TAG, "onBackPressed: " + "cancelWaitdialog");

                Log.e(TAG, "onReceive: myReceiver " + "hangup");

                stopRingtone();
                V2TIMSignalingManager v2TIMSignalingManager = V2TIMManager.getSignalingManager();
                Log.e("chdakdaf", "yes2 " + inviteId);

                v2TIMSignalingManager.cancel(inviteId, "Invite Ended", new V2TIMCallback() {
                    @Override
                    public void onSuccess() {
                        Log.e("listensdaa", "Yes11 cancelled" + inviteId);

                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("listensdaa", "Yes22 " + s);

                    }
                });
                hangUpCall(true);
                exitRoom();

                addCallEventTODb("video_call_self_cancelled", "");

            }

            if (action.equals("end2")) {
                if (waitingForConnect != null) {
                    waitingForConnect.dismiss();
                }
                stopRingtone();
                finish();
            }

        }
    };


    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO + "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }

            ZegoCall();

            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.

        }
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void endCall() {

        Log.e("testttst", "from end function");
      /*  removeFromParent(LocalView);
        removeFromParent(RemoteView);*/

        // Calculate call charges accordingly
        endTimeVideoEvent = Calendar.getInstance().getTime();
        getCallDuration(endTimeVideoEvent);

        hangUpCall(true);
        exitRoom();

    }


    public void onLocalAudioMuteClicked(View view) {
        //  mMuted = !mMuted;

    }

    public void onSwitchCameraClicked(View view) {
        // mRtcEngine.switchCamera();
      /*  switchView(RemoteView);
        switchView(LocalView);*/
    }

    String getCallDurationVideoCall() {
        String callDuration = "";
        if (endTimeVideoEvent == null) {
            endTimeVideoEvent = Calendar.getInstance().getTime();
        }
        if (callStartTime != null) {
            long mills = endTimeVideoEvent.getTime() - callStartTime.getTime();

            int hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills / (1000 * 60)) % 60;
            int sec = (int) (mills - hours * 3600000 - mins * 60000) / 1000;

            String seconds = String.format(Locale.ENGLISH, "%02d", sec);
            if (hours > 1) {
                callDuration = hours + ":" + mins + ":" + seconds;
            } else if (mins > 0) {
                callDuration = mins + ":" + seconds;
            } else {
                callDuration = "0:" + seconds;
            }

        }

        return callDuration;
    }

    void getCallDuration(Date endTime) {

        if (callStartTime != null) {
            long mills = endTime.getTime() - callStartTime.getTime();

            int hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills / (1000 * 60)) % 60;
            int sec = (int) (mills - hours * 3600000 - mins * 60000) / 1000;


            if (mins < 1 && sec > 1 && sec < 60) {

                Log.e(TAG, "endcall -> getCallDuration: " + "before api");

                Log.e("testttst", "getCallDuration:  Boolean.parseBoolean(isFreeCall) if  " + Boolean.parseBoolean(isFreeCall));
                apiManager.endCall(new CallRecordBody("", unique_id, Boolean.parseBoolean(isFreeCall), new CallRecordBody.Duration("", String.valueOf(System.currentTimeMillis()))));

                Log.e(TAG, "endcall -> getCallDuration: " + "after api");

                if (gender.equals("male")) {
                    endAllApi();
                }
            } else {
                int roundOf = 1;
                int totalMins;

                if (sec > 6) {
                    totalMins = mins + roundOf;
                } else {
                    totalMins = mins;
                }

               /* apiManager.endCall(new CallRecordBody("", unique_id,
                        new CallRecordBody.Duration("", String.valueOf(System.currentTimeMillis()))));*/

                Log.e(TAG, "endCall: " + "before api");

                Log.e("testttst", "getCallDuration:  Boolean.parseBoolean(isFreeCall) else " + Boolean.parseBoolean(isFreeCall));

                apiManager.endCall(new CallRecordBody("", unique_id, Boolean.parseBoolean(isFreeCall), new CallRecordBody.Duration("", String.valueOf(System.currentTimeMillis()))));

                Log.e(TAG, "endCall: " + "after api");

                if (gender.equals("male")) {
                    endAllApi();
                }
            }
        } else {
            if (talkTimeHandler != null) {
                talkTimeHandler.removeCallbacksAndMessages(null);
            }

            //   finish();
        }
    }


    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void loadLoader() {
        Glide.with(getApplicationContext())
                .load(R.drawable.loader)
                .into((ImageView) findViewById(R.id.img_giftloader));
    }


    private void initKeyBoardListener() {
        // Threshold for minimal keyboard height.
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        // Top-level window decor view.
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            // Retrieve visible rectangle inside window.
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        //             Log.e("Keyboard", "SHOW");
                        // messagesView.setSelection(messagesView.getCount() - 1);
                        messagesView.scrollToPosition(0);
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        //           Log.e("Keyboard", "HIDE");
                        ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }


    private void giftAnimation(int position) {
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.VISIBLE);
        mp = MediaPlayer.create(this, R.raw.giftem);

        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                mp.start();
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                mp.stop();
            }
        });

        switch (position) {
            case 19:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.candy);
                break;
            case 2:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.lucky);
                break;
            case 3:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.bell);
                break;
            case 4:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.leaves);
                break;
            case 5:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.kiss);
                break;
            case 6:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.candy_1);
                break;
            case 7:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rose);
                break;
            case 8:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.heart);
                break;
            case 9:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.lipstik);
                break;
            case 10:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.perfume);
                break;
            case 11:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.necklace);
                break;
            case 12:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.panda);
                break;
            case 13:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.hammer);
                break;
            case 14:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rocket);
                break;
            case 15:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.ship);
                break;
            case 16:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.ring);
                break;
            case 17:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.disney);
                break;
            case 18:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.hot_ballon);
                break;
        }

        //   ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rose);
        animFadeIn.reset();
        ((ImageView) findViewById(R.id.img_imageShow)).clearAnimation();
        ((ImageView) findViewById(R.id.img_imageShow)).startAnimation(animFadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.GONE);
            }
        }, 3000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        RemoteView.setVisibility(View.VISIBLE);
        Log.e("chdsakfaka", "resumevcall");
        registerReceiver(getMyGiftReceiver, new IntentFilter("GIFT-USER-INPUT"));
        registerReceiver(myReceiver, new IntentFilter("FBR-ENDTHIS"));


    }

    @Override
    public void onBackPressed() {


        Log.e(TAG, "onBackPressed: " + "cancelWaitdialog not visible");

        if (((RelativeLayout) findViewById(R.id.rl_bottom)).getVisibility() == View.VISIBLE) {
            ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
            //hideKeybaord(view);
        }
        if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
            ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
            messagesView.setVisibility(View.VISIBLE);
        } else {

            final Dialog dialog = new Dialog(VideoChatZegoActivityMet.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.disconnectcalldialog);

            DisplayMetrics metrics = new DisplayMetrics(); //get metrics of screen
            // getWindowManager().getDefaultDisplay().getMetrics(metrics);
            //int height = (int) (metrics.heightPixels * 0.9); //set height to 90% of total
            int width = (int) (metrics.widthPixels * 0.9); //set width to 90% of total

            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            // dialog.getWindow().setLayout(width, height); //set layout


            if (!((VideoChatZegoActivityMet.this).isFinishing())) {
                dialog.show();
            }


            TextView text = (TextView) dialog.findViewById(R.id.msg);
            TextView tv_dailogcancel = (TextView) dialog.findViewById(R.id.tv_dailogcancel);
            TextView tv_dailogconfirm = (TextView) dialog.findViewById(R.id.tv_dailogconfirm);
            if (gender.equals("male")) {
                text.setText("Are you sure to close the video call?");
            } else {
                // text.setText("If you hang up this video call now, you will not recevie coins for this present video call. Are you sure to do that?");
            }

            tv_dailogcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            tv_dailogconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    userEndsCall = true;
                    if (!gender.equals("male")) {
                        apiManager.getcallCutByHost(unique_id);
                        finish();
                    } else {
                        Log.e(TAG, "onClick: conferm Backpressed " + " hangup");
                        Log.e("testttst", "popup conf");
                        endCall();
                    }
                    // added this as disconnected by our end when call is completed to trigger the video event.
                    addCallEventTODb("video_call_completed_user", getCallDurationVideoCall());
                }
            });


        }


    }

    public void onCallClicked(View view) {
        onBackPressed();
    }

    public void onCamOff(View view) {

    }

    public void onSwitchCam(View view) {
    }

    @Override
    public void isError(String errorCode) {
        Log.e("GiftCoinTest", errorCode);
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
        bottomSheet.getWalbalance();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_DATA_FROM_PROFILE_ID) {

            DataFromProfileIdResponse rsp = (DataFromProfileIdResponse) response;
            DataFromProfileIdResult rlt = rsp.getResult();
            apiManager.getProfileDataNew("" + rlt.getId(), "");

        }


        if (ServiceCode == Constant.GET_PROFILE_DATA) {
            // UserListResponse.Data userData;
            UserListResponseNewData rsp = (UserListResponseNewData) response;
            //userData = (ResultDataNewProfile) rsp.getResult();

            userData.addAll(rsp.getResult());

            try {
                isFavourite = userData.get(0).getFavoriteByYouCount();
            } catch (Exception e) {

            }

            if (isFavourite == 0) {
                findViewById(R.id.follow_btn).setVisibility(View.VISIBLE);
                //hide for follow button
                isFavourite = 1;
            }
            userIdInt = userData.get(0).getId();

        }


        if (ServiceCode == Constant.SEND_CALL_RECORD) {
            Object rsp = response;
        }
        if (ServiceCode == Constant.FOLLOWING_HOST) {

            AddRemoveFavResponse addRemoveFavResponse = (AddRemoveFavResponse) response;
            if (addRemoveFavResponse.isSuccess()) {
                customErrorToast(addRemoveFavResponse.getResult());
                Intent intent = new Intent("FBA");
                intent.putExtra("action", "addItem");
                this.sendBroadcast(intent);
            }
        }
        if (ServiceCode == Constant.END_CALL) {
            Object rsp = response;

            if (talkTimeHandler != null) {
                talkTimeHandler.removeCallbacksAndMessages(null);
            }
            chronometer.stop();
            finish();
        }
        if (ServiceCode == Constant.WALLET_AMOUNT2) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            ((TextView) findViewById(R.id.tv_coinchat)).setText(String.valueOf(rsp.getResult().getTotal_point()));
        }

        if (ServiceCode == Constant.SEND_GIFT) {
            SendGiftResult rsp = (SendGiftResult) response;
            ((TextView) findViewById(R.id.tv_coinchat)).setText(String.valueOf(rsp.getRemainingPoints()));
            remainFromServer = rsp.getRemainingPoints();
            fromGift = true;
            try {

                try {
                    messageGiftData.put("type", "giftSend");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg2 = messageGiftData.toString();

                V2TIMManager.getInstance().sendC2CTextMessage(msg2,
                        reciverId, new V2TIMValueCallback<V2TIMMessage>() {
                            @Override
                            public void onSuccess(V2TIMMessage message) {
                                // The one-to-one text message sent successfully
                                Log.e("giftsendRes", "success to => " + reciverId + " with message => " + new Gson().toJson(message));
                            }


                            @Override
                            public void onError(int code, String desc) {

                            }
                        });

                Log.e("AUTO_CUT_TEST", "wallet before => " + new SessionManager(VideoChatZegoActivityMet.this).getUserWallet());
                new SessionManager(VideoChatZegoActivityMet.this).setUserWall(rsp.getRemainingPoints());
                Log.e("AUTO_CUT_TEST", "wallet after => " + new SessionManager(VideoChatZegoActivityMet.this).getUserWallet());

                if (walletCheckerHandler != null) {
                    walletCheckerHandler.removeCallbacksAndMessages(null);
                }

                updatedInfo();


                long tsLong = System.currentTimeMillis() / 1000;
                giftLong = Long.toString(tsLong);


                Log.e("AUTO_CUT_TEST", "giftLong => " + Integer.parseInt(giftLong) + " startLong => " + Integer.parseInt(startLong));


                long didu = ((Integer.parseInt(giftLong) - Integer.parseInt(startLong)));

                int callrateInt = Integer.parseInt(call_rate);
                // long longdidu = (rsp.getResult() - (2 * callrateInt)) - didu * callrateInt;
                // long longdidu = rsp.getResult() - (didu * callrateInt);

                long talkTime = (rsp.getResult() / callrateInt) * 60 * 1000L;

                Log.e("AUTO_CUT_TEST", "isSuccess: Wallet " + rsp.getResult());
                Log.e("AUTO_CUT_TEST", "isSuccess: start didu (time done in sec)" + didu);
                // Log.e("AUTO_CUT_TEST", "isSuccess: start longdidu (time done in sec) " + longdidu);


                Log.e("AUTO_CUT_TEST", "isSuccess: CALL_RATE " + call_rate);

                //AUTO_END_TIME = talkTime+2147483647000L;


                AUTO_END_TIME = talkTime;

                Log.e("AUTO_CUT_TEST", "talkTime  " + talkTime);
                //  startLong = giftLong;

                Log.e("AUTO_CUT_TEST", "isSuccess: MIDDLE coin after gift send " + rsp.getResult() + "  AUTO_END_TIME  " + AUTO_END_TIME);
                AUTO_END_TIME = AUTO_END_TIME - didu;
                AUTO_END_TIME = 0;
                int auto = rsp.getRemainingPoints() / Integer.parseInt(call_rate);
                AUTO_END_TIME = auto * 60 * 1000L;
                Log.e("AUTO_CUT_TEST", "AUTO_END_TIME  " + AUTO_END_TIME + " getRemainingPoints => " + rsp.getRemainingPoints() + " auto => " + auto);

                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String autoEndData = getAutoEndData(AUTO_END_TIME);
                            sendZegoCustomCommand(autoEndData);

                        }
                    }, 100);

                } catch (Exception e) {
                    Log.e("TEST_COIN_RATE", "Exception " + e.getMessage());
                }

               /*
                Log.e("TimestampDiff", didu + "");
                Log.e("walletBalance", rsp.getResult() + "");
                Log.e("AUTO_END_TIME", AUTO_END_TIME + "");
                */

                if (talkTimeHandler != null) {
                    talkTimeHandler.removeCallbacksAndMessages(null);
                }

                talkTimeHandler.postDelayed(() -> {
                    Toast.makeText(VideoChatZegoActivityMet.this, "Out of Balance", Toast.LENGTH_LONG).show();
                    Log.e("GiftCoinTest", "hangup from gift send api ");
                    Log.e("testttst", "from gift api");
                    endCall();
                }, AUTO_END_TIME);

                //giftAnimation(fPosition);
                bottomSheet.getWalbalance(rsp.getRemainingPoints());
            } catch (Exception e) {

            }

        }
    }

    private int remainFromServer = 0;
    private boolean fromGift = false;

    private String getAutoEndData(long auto_end_time) {

        JSONObject messageObject = new JSONObject();
        JSONObject autoCallCut = new JSONObject();

        try {
            autoCallCut.put("CallAutoEnd", auto_end_time);
            messageObject.put("isMessageWithAutoCallCut", "yes");
            messageObject.put("AutoCallCut", autoCallCut);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageObject.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        V2TIMManager.getSignalingManager().removeSignalingListener(v2TIMSignalingListener);

       /*     Log.e(TAG, "endCall: destroy "+"called" );
     //   endCall();

        removeFromParent(LocalView);
        removeFromParent(RemoteView);
        // Calculate call charges accordingly
        getCallDuration(Calendar.getInstance().getTime());

        hangUpCall(true);
        Log.e(TAG, "endCall: destroy "+"done" );*/


        if (receiveCallHandler != null) {
            receiveCallHandler.removeCallbacksAndMessages(null);
        }


        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        if (getMyGiftReceiver != null) {
            unregisterReceiver(getMyGiftReceiver);
        }
     /*   if (zimManager != null) {
            zimManager.removeListener(zimEventListener);
        }
*/

        stopRingtone();

        if (RatingDialog) {
            getRating();
        }

        if (LocalView != null) {
            LocalView = null;
        }

        if (RemoteView != null) {
            RemoteView = null;
        }


        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        if (walletCheckerHandler != null) {
            walletCheckerHandler.removeCallbacksAndMessages(null);
        }
        Constant.isReceivedFakeCall = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        EndCallData endCallData = new EndCallData(unique_id, String.valueOf(System.currentTimeMillis()));
        list.add(endCallData);
        new SessionManager(getApplicationContext()).setUserEndcalldata(list);
        new SessionManager(getApplicationContext()).setUserGetendcalldata("error");
    }

    public void onBeautyClicked(View view) {

    }

    public BroadcastReceiver getMyGiftReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("GiftPosition");
            String from = intent.getStringExtra("type");
            int giftId = Integer.parseInt(action);
            giftPosition = giftId;
            Log.e("chdsksaa", "Broadcast receive " + action + " " + from);
            if (from.equals("giftSend")) {
                Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.VISIBLE);
                switch (giftId) {
                    case 1:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.test1);
                        break;
                    case 2:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.test2);
                        break;
                    case 3:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.test3);
                        break;
                    case 4:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.test4);
                        break;
                    case 18:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.heart);
                        break;
                    case 21:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.lips);
                        break;
                    case 22:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.bunny);
                        break;
                    case 23:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rose);
                        break;
                    case 24:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.boygirl);
                        break;
                    case 25:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.sandle);
                        break;
                    case 26:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.frock);
                        break;
                    case 27:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.car);
                        break;
                    case 28:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.ship);
                        break;
                    case 29:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.tajmahal);
                        break;
                    case 30:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.crown);
                        break;
                    case 31:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.bracket);
                        break;
                    case 32:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.diamondgift);
                        break;
                    case 33:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.lovegift);
                        break;
                }
                //   ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rose);
                animFadeIn.reset();
                ((ImageView) findViewById(R.id.img_imageShow)).clearAnimation();
                ((ImageView) findViewById(R.id.img_imageShow)).startAnimation(animFadeIn);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.GONE);
                    }
                }, 3000);
            } else {
                ((RelativeLayout) findViewById(R.id.giftRequest)).setVisibility(View.VISIBLE);

                try {
                    if (giftRequestDismissHandler != null) {
                        giftRequestDismissHandler.removeCallbacksAndMessages(null);
                    }
                } catch (Exception e) {
                }

                giftRequestDismissHandler.postDelayed(() -> {
                            ((RelativeLayout) findViewById(R.id.giftRequest)).setVisibility(View.GONE);
                        }
                        , 10000);


                switch (giftId) {
                    case 1:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.test1);
                        break;
                    case 2:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.test2);
                        break;
                    case 3:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.test3);
                        break;
                    case 4:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.test4);
                        break;
                    case 18:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.heart);
                        break;
                    case 21:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.lips);
                        break;
                    case 22:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.bunny);
                        break;
                    case 23:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.rose);
                        break;
                    case 24:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.boygirl);
                        break;
                    case 25:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.sandle);
                        break;
                    case 26:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.frock);
                        break;
                    case 27:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.car);
                        break;
                    case 28:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.ship);
                        break;
                    case 29:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.tajmahal);
                        break;
                    case 30:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.crown);
                        break;
                    case 31:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.bracket);
                        break;
                    case 32:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.diamondgift);
                        break;
                    case 33:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.lovegift);
                        break;
                }

                ((TextView) findViewById(R.id.tv_sendGift)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        ((RelativeLayout) findViewById(R.id.giftRequest)).setVisibility(View.GONE);

                        int position = 0;
                        for (int i = 0; i < giftArrayList.size(); i++) {
                            if (giftArrayList.get(i).getId() == giftId) {
                                position = i;
                            }
                        }

                        int currentCoin = Integer.parseInt(((TextView) findViewById(R.id.tv_coinchat)).getText().toString());
                        // currentCoin = currentCoin - giftArrayList.get(position).getAmount();
                        if (currentCoin > giftArrayList.get(position).getAmount()) {
                            fPosition = position;

                            //new value remove unique_id sendUserGift api 18/5/21 Integer.parseInt(unique_id)
                            new ApiManager(getApplicationContext(), VideoChatZegoActivityMet.this).sendUserGift(new SendGiftRequest(Integer.parseInt(reciverId), call_unique_id,
                                    giftArrayList.get(position).getId(), giftArrayList.get(position).getAmount(), startTimeStamp, String.valueOf(System.currentTimeMillis())));

                            // sendMessage("gift", String.valueOf(giftId), "");

                        } else {
                            Toast.makeText(VideoChatZegoActivityMet.this, "Out of Balance", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }
    };


    public void getRating() {
        String call_duration = chronometer.getText().toString();
        if (new SessionManager(getApplicationContext()).getGender().equals("male")) {
            //rate dialog show for rating
            //new RatingDialog(VideoChatZegoActivityMet.this, reciverName, reciverProfilePic);
            Intent intent = new Intent(VideoChatZegoActivityMet.this, RatingDialogActivityNew.class);
            intent.putExtra("host_name", reciverName);
            intent.putExtra("host_id", String.valueOf(UID));
            intent.putExtra("end_time", String.valueOf(call_duration));
            intent.putExtra("host_image", reciverProfilePic);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
        }
    }


    private void endAllApi() {
        String duration = chronometer.getText().toString();

        ApiInterface apiservice = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);

        RequestMissCall requestMissCall = new RequestMissCall(Long.parseLong(new SessionManager(getApplicationContext()).getUserId()),
                Integer.parseInt(reciverId), converID, "videocall",
                duration, 0, "00000000000");

        //    Log.e("reqEndTime", new Gson().toJson(requestMissCall));

 /*       SocketCallOperation socketCallOperation = new SocketCallOperation(new SessionManager(getApplicationContext()).getUserId(), reciverId);
        //     socket.emit("callDone", new Gson().toJson(socketCallOperation));


        Call<ResultMissCall> call = apiservice.sendMissCallData(requestMissCall);
        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultMissCall>() {
                @Override
                public void onResponse(Call<ResultMissCall> call, Response<ResultMissCall> response) {
                    //             Log.e("onResponceCallEnd", new Gson().toJson(response.body()));
                }

                @Override
                public void onFailure(Call<ResultMissCall> call, Throwable t) {
                    //            Log.e("onErrorCallEnd", t.getMessage());
                }
            });
        }*/
    }

    public void RechargePopup(int type) {
        new InsufficientCoinsMyaccount(VideoChatZegoActivityMet.this, 2, new SessionManager(VideoChatZegoActivityMet.this).getUserWallet());


    }


    private Handler walletCheckerHandler = new Handler();
    private Runnable walletCheckRunnable;
    long delay = 60000;

    private void updatedInfo() {
        currentBalance = new SessionManager(this).getUserWallet();
        //  Log.e("updatedInfoV", " updatedInfo: " + currentBalance);


        walletCheckRunnable = () -> {
            Log.e("updatedInfo", "run:wallet balance => " + new SessionManager(VideoChatZegoActivityMet.this).getUserWallet());

            long timediff = (System.currentTimeMillis() / 1000) - Long.parseLong(startLong);
            Log.e("updatedInfo", "timediff => " + timediff);
            if (timediff > 0) {
                timediff = timediff / 60;
            }
            Log.e("updatedInfo", "timediff in min=> " + timediff);

            long bal = (timediff * Integer.parseInt(call_rate));
            Log.e("updatedInfo", "bal => " + bal + " callrate => " + call_rate);

            long remain = new SessionManager(VideoChatZegoActivityMet.this).getUserWallet() - bal;
            // remain=remain-se
            Log.e("updatedInfo", "remain => " + remain);

            Log.e("updatedInfo", "updatedInfo: minutes " + getMinutesFromBalance(remain, Integer.parseInt(call_rate)));

            if (fromGift) {
                remain = remainFromServer;
                Log.e("updatedInfo", "remain from server=> " + remain);
            }

            if (remain < 0) {
                endCall();
            }

            totalBalanceText.setText("" + remain);
            totalRemainingMinutesText.setText("Can call for " + getMinutesFromBalance(remain, Integer.parseInt(call_rate)) + " mins");


            walletCheckerHandler.postDelayed(walletCheckRunnable, delay);


        };
        walletCheckerHandler.postDelayed(walletCheckRunnable, 0);


    }

    long remainingMinutes;

    private long getMinutesFromBalance(long balance, int callrate) {
        remainingMinutes = (balance / callrate);
        return remainingMinutes;
    }


    private int isFavourite = 1;

    public void addToFav(View view) {
        addRemoveFav();
        apiManager.followingHost(String.valueOf(userIdInt));
        // Log.e("newUserIdrr", userIdString + "  userid  "+userId);
    }

    public void addRemoveFav() {
        findViewById(R.id.follow_btn).setEnabled(false);
        Intent myIntent = new Intent("FBR");
        myIntent.putExtra("action", "reload");
        this.sendBroadcast(myIntent);

        if (isFavourite == 0) {
            findViewById(R.id.follow_btn).setVisibility(View.VISIBLE);
            isFavourite = 1;
        } else {
            new Handler().postDelayed(() -> findViewById(R.id.follow_btn).setVisibility(View.GONE), 500);
            // binding.nonFavourite.setText("UnFollow");
            // binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_offline_background);
            isFavourite = 0;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.follow_btn).setEnabled(true);
            }
        }, 1000);
    }

    private LinearLayout toast;

    private void customErrorToast(String msg) {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.unable_to_call_lay, (ViewGroup) toast);
        TextView textView = layout.findViewById(R.id.custom_toast_message);
        textView.setText(msg);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 30);
        toast.setView(layout);
        toast.show();
    }


    private void sendZegoCustomCommand(String message) {


    }


}