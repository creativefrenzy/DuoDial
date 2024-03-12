package com.privatepe.host.Zego;

/*import static com.privatepe.host.ZegoExpress.zim.ZimManager.busyOnCall;*/

import static com.privatepe.host.utils.SessionManager.GENDER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.privatepe.host.Firestatus.FireBaseStatusManage;
import com.privatepe.host.IM.GenerateTestUserSig;
import com.privatepe.host.Inbox.DatabaseHandler;
import com.privatepe.host.Inbox.MessageBean;
import com.privatepe.host.Inbox.Messages;
import com.privatepe.host.Inbox.UserInfo;
import com.privatepe.host.Interface.GiftSelectListener;
import com.privatepe.host.R;
/*import com.privatepe.host.ZegoExpress.zim.ZimEventListener;*/
/*import com.privatepe.host.ZegoExpress.zim.ZimManager;*/
import com.privatepe.host.adapter.GiftAdapter;
import com.privatepe.host.adapter.GiftAnimationRecyclerAdapter;
import com.privatepe.host.dialogs.gift.GiftBottomSheetDialog;
import com.privatepe.host.dialogs.gift.VideoMenuSheetDialog;
import com.privatepe.host.main.Home;
import com.privatepe.host.model.EndCallData.EndCallData;
import com.privatepe.host.model.WalletBalResponse;
import com.privatepe.host.model.body.CallRecordBody;
import com.privatepe.host.model.gift.Gift;
import com.privatepe.host.model.gift.GiftAnimData;
import com.privatepe.host.model.gift.ResultGift;
import com.privatepe.host.response.NewZegoTokenResponse;
import com.privatepe.host.response.newgiftresponse.NewGift;
import com.privatepe.host.response.newgiftresponse.NewGiftListResponse;
import com.privatepe.host.response.newgiftresponse.NewGiftResult;
import com.privatepe.host.retrofit.ApiInterface;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.retrofit.RetrofitInstance;
import com.privatepe.host.services.ItemClickSupport;
import com.privatepe.host.sqlite.Chat;
import com.privatepe.host.sqlite.ChatDB;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.NetworkCheck;
import com.privatepe.host.utils.SessionManager;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoChatZegoActivity extends BaseActivity implements ApiResponseInterface {

    String BugTAG = "GiftAnimationBug";
    TXCloudVideoView LocalView, RemoteView;
    String receiver_id, CallerName, ZegoToken, CallerUserName, CallerProfilePic, callType;
    String TAG = "VideoChatZegoActivity1212";

    TextView CallerNameText;
    Handler talkTimeHandler;
    Handler handler, giftRequestDismissHandler;

    CircleImageView CallerImage;
    private String gender;
    private String startLong;
    private Chronometer chronometer;
    private ImageView mSwitchCameraBtn;
    CardView frameLayoutLocal;
    RelativeLayout frameLayoutRemote;
    private RecyclerView messagesView;
    private RecyclerView rv_gift;
    private GridLayoutManager gridLayoutManager;
    private GiftAdapter giftAdapter;
    private ArrayList<Gift> giftArrayList = new ArrayList<>();
    private NetworkCheck networkCheck;
    private ApiManager apiManager;

    private boolean mCallEnd;
    private ImageView mCallBtn;
    private MediaPlayer mp;
    Date callStartTime;
    boolean RatingDialog = false;

    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean useExpressCustomCapture = false;
    private ArrayList<EndCallData> list = new ArrayList<>();
    private DatabaseReference chatRef;
    private String is_free_call, unique_id;
    private JSONObject MessageWithGift;
    private JSONObject MessageWithGiftJson;
    private ViewStub mBottomViewStub;
    private RecyclerView giftAnimRecycler;
    List<GiftAnimData> giftdataList = new ArrayList<>();
    private GiftAnimationRecyclerAdapter giftAnimationRecyclerAdapter;
    //  private ZimManager zimManager;
    //   private ZimEventListener zimEventListener;
    private String streamID;
    private String roomID;
    private boolean callEndCheck;
    private long AUTO_END_TIME;
    Date endTimeVideoEvent = null;
    private boolean userEndsCall = false;
    private V2TIMSignalingListener signalListener;
    private Observer<Boolean> inviteObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        networkCheck = new NetworkCheck();
        setContentView(R.layout.videochat_new);

//        FURenderer.getInstance().setup(getApplicationContext());

        apiManager = new ApiManager(this, this);

        giftAnimRecycler = findViewById(R.id.gift_animation_recyclerview);
        HashMap<String, String> user = new SessionManager(this).getUserDetails();
        gender = user.get(GENDER);
        Long tsLong = System.currentTimeMillis() / 1000;
        startLong = tsLong.toString();
        talkTimeHandler = new Handler();

        initUI();

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) && checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {

        }

        inviteObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean booleanI) {
                Log.e("inviteObserveis", "Yes " + booleanI);
                if (booleanI) {
                    Home.inviteClosed.setValue(false);
                    exitRoom();
                    hangUpCall(true);
                    if (inviteObserver != null) {
                        Home.inviteClosedIs.removeObserver(inviteObserver);
                    }
                }
            }
        };
        try {
            Home.inviteClosedIs.observe(this, inviteObserver);
        } catch (Exception e) {

        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inItZegoExpressWithFu();
            }
        }, 1000);


        if (CallerProfilePic != null) {
            Glide.with(getApplicationContext()).load(CallerProfilePic).into(CallerImage);
        }

        //storeBusyStatus("Busy");

        Log.e(BugTAG, "Setup giftAnimRecycler in " + "VideoChatActivity");
        giftdataList.clear();
        giftAnimRecycler.setLayoutManager(new LinearLayoutManager(this));
        giftAnimationRecyclerAdapter = new GiftAnimationRecyclerAdapter(giftdataList, getApplicationContext(), new GiftAnimationRecyclerAdapter.OnItemInvisibleListener() {
            @Override
            public void onItemInvisible(int adapterposition) {
                //giftdataList.remove(adapterposition);
            }
        });
        giftAnimRecycler.setAdapter(giftAnimationRecyclerAdapter);
        if (!gender.equals("male")) {
            new SessionManager(getApplicationContext()).setHostOnCall(true);

        }
    }

    private void inItZegoExpressWithFu() {

        Log.e(TAG, "inItZegoExpressWithFu: " + "fu init");

        initSDK();
        //  zimManager = ZimManager.sharedInstance();
   /*     zimEventListener = new ZimEventListener() {
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
                endCall();
                hangUpCall(false);
                startActivity(new Intent(VideoChatZegoActivity.this, FastScreenActivity.class));
                finish();
                if (!userEndsCall) {
                    addCallEventTODb("video_call_completed", getCallDurationVideoCall());
                }

            }

            @Override
            public void onConnectionStateChanged(ZIMConnectionState state, ZIMConnectionEvent event) {

            }

            @Override
            public void onReceiveZIMPeerMessage(ZIMMessage zimMessage, String fromUserID) {
                Log.d(TAG, "onReceiveZIMPeerMessage: ");


                ZIMTextMessage textMessage = (ZIMTextMessage) zimMessage;
                String messageString = textMessage.message;
                Log.d("TAG", "onReceivePeerMessage: " + messageString);
                try {
                    JSONObject jsonObject = new JSONObject(messageString);

                    if (jsonObject != null) {

                        if (jsonObject.has("isMessageWithGift")) {

                            if (jsonObject.get("isMessageWithGift").toString().equals("yes")) {
                                Log.e("giftReceived", "yesss");
                                MessageWithGiftJson = new JSONObject(jsonObject.get("GiftMessageBody").toString());
                                Log.e("giftId", "" + MessageWithGiftJson.get("GiftPosition"));


                                String giftDataString = MessageWithGiftJson.get("GiftData").toString();


                                //    Log.e("GiftModule", "onReceiveZIMPeerMessage: "+MessageWithGiftJson.get("GiftData"));

                                NewGift giftDatanew = null;

                                giftDatanew = new Gson().fromJson(giftDataString, NewGift.class);

                                Log.e("GiftModule", "onReceiveZIMPeerMessage: " + giftDatanew.getGift_name());

                                NewGiftAnimation(Integer.parseInt(MessageWithGiftJson.get("GiftPosition").toString()), MessageWithGiftJson.get("UserName").toString(), MessageWithGiftJson.get("ProfilePic").toString(), giftDatanew);

                            }

                        } else if (jsonObject.has("isMessageWithAutoCallCut")) {
                            Log.e("autoCallcutjson", " recieved");

                            if (jsonObject.get("isMessageWithAutoCallCut").toString().equals("yes")) {
                                Log.e("autoCallcutjson", " recieved2");

                                JSONObject autoCallcutjson = new JSONObject(jsonObject.get("AutoCallCut").toString());

                                Log.e("autoCallcutjson", "" + autoCallcutjson.get("CallAutoEnd"));

                                // AUTO_END_TIME = Integer.parseInt(autoCallcutjson.get("CallAutoEnd").toString());

                                AUTO_END_TIME = Long.parseLong(autoCallcutjson.get("CallAutoEnd").toString());

                                Log.e("AUTO_CUT_TEST", "onReceiveZIMPeerMessage: auto end after gift send " + AUTO_END_TIME);


                                if (talkTimeHandler != null) {
                                    talkTimeHandler.removeCallbacksAndMessages(null);
                                }

                                talkTimeHandler.postDelayed(() -> {

                                    callEndCheck = true;
                                    endCall();
                                    hangUpCall(true);

                                    Log.e(TAG, "onReceiveZIMPeerMessage: " + "endCall -> talkTimeHandler");

                                    Log.e("AUTO_CUT_TEST", "onReceiveZIMPeerMessage: " + AUTO_END_TIME);

                                    //  Toast.makeText(VideoChatZegoActivity.this, "Out of Balance", Toast.LENGTH_LONG).show();
                                }, AUTO_END_TIME);

                            }

                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };*/

        //  zimManager.addListener(zimEventListener);
        startCallWithExpress();

    }

    private void addCallEventTODb(String type, String duration) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        final String[] msg = {""};
        final boolean[] beSelf = {false};
        final MessageBean[] messageBean = new MessageBean[1];

        executor.execute(() -> {
            //Background work here
            try {


                if (type.equals("video_call_cancelled")) {
                    msg[0] = "Call canceled";
                } else if (type.equals("video_call_rejected_by_host")) {
                    msg[0] = "Call rejected";
                } else if (type.equals("video_call_not_answered")) {
                    msg[0] = "Call was not answered";
                } else if (type.equals("video_call_ended_by_host")) {
                    msg[0] = "Call ended";
                } else if (type.equals("video_call_self_cancelled")) {
                    msg[0] = "Call canceled";
                    beSelf[0] = true;
                } else if (type.equals("video_call_completed")) {
                    msg[0] = "Call Completed " + duration;
                } else if (type.equals("video_call_completed_user")) {
                    msg[0] = "Call Completed " + duration;
                    beSelf[0] = true;
                }
                if (beSelf[0]) {
                    saveChatInDb(receiver_id, CallerName, msg[0], "", "", "", "", CallerProfilePic, "video_call_event");
                    Log.e("cjhhadjaf", "A1 " + msg[0]);
                } else {
                    saveChatInDb(receiver_id, CallerName, "", msg[0], "", "", "", CallerProfilePic, "video_call_event");
                    Log.e("cjhhadjaf", "A2 " + msg[0]);

                }
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                String profilePic = sessionManager.getUserProfilepic();
                currentUserId = sessionManager.getUserId();
                currentUserName = sessionManager.getUserName();

                Messages message = new Messages();
                message.setFrom(currentUserId);
                message.setFromName(currentUserName);
                message.setMessage(msg[0]);
                message.setFromImage(profilePic);
                message.setTime_stamp(System.currentTimeMillis());
                message.setType("video_call_event");
                String timestamp = System.currentTimeMillis() + "";
                Log.e("chejckaa", "Yesss " + msg);
                messageBean[0] = new MessageBean(currentUserId, message, beSelf[0], timestamp);
                dbHandler = new DatabaseHandler(getApplicationContext());
                //String contactId = insertOrUpdateContact(messageBean[0].getMessage(), reciverId, reciverName, reciverProfilePic, timestamp);
            /*if (TextUtils.isEmpty(this.contactId)) {
                this.contactId = contactId;
            }*/
                //  messageBean[0].setAccount(contactId);
                String contactId = insertOrUpdateContact(messageBean[0].getMessage(), receiver_id, CallerName, CallerProfilePic, timestamp);
                messageBean[0].setAccount(contactId);
                insertChat(messageBean[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.post(() -> {
                Intent chatGiftIntent = new Intent("VIDEO-CALL-EVENT");
                chatGiftIntent.putExtra("pos", msg[0]);
                chatGiftIntent.putExtra("peerId", receiver_id);
                chatGiftIntent.putExtra("peerName", CallerName);
                chatGiftIntent.putExtra("peerProfilePic", CallerProfilePic);
                chatGiftIntent.putExtra("beSelf", beSelf[0]);
                sendBroadcast(chatGiftIntent);
            });
        });

    }

    String currentUserId, currentUserName;

    private void insertChat(MessageBean messageBean) {
        dbHandler.addChat(messageBean);
    }

    private String insertOrUpdateContact(Messages message, String userId3, String profileName, String profileImage, String timestamp) {
        String userId1 = "647400310";
        UserInfo userInfoFromDb = dbHandler.getContactInfo(userId1, currentUserId);
        Log.e("cjjadfaa", "yes1 " + userId1 + " " + new Gson().toJson(userInfoFromDb));
        String contactId = "";
        if (userInfoFromDb == null) { // insert
            UserInfo userInfo = new UserInfo();
            userInfo.setUser_id(userId1);
            userInfo.setUser_name(profileName);
            userInfo.setMessage(message.getMessage());
            userInfo.setUser_photo(profileImage);
            userInfo.setTime(timestamp);
            userInfo.setUnread_msg_count("0");
            userInfo.setProfile_id(currentUserId);
            userInfo.setMsg_type(message.getType());
            userInfo.setGift_count(String.valueOf(0));
            contactId = dbHandler.addContact(userInfo);
        } else { //update
            contactId = userInfoFromDb.getId();
            userInfoFromDb.setUser_name(profileName);
            userInfoFromDb.setMessage(message.getMessage());
            userInfoFromDb.setUser_photo(profileImage);
            userInfoFromDb.setTime(timestamp);
            userInfoFromDb.setUnread_msg_count("0");
            userInfoFromDb.setMsg_type(message.getType());
            userInfoFromDb.setGift_count(String.valueOf(0));
            dbHandler.updateContact(userInfoFromDb);
        }
        return contactId;
    }

    private DatabaseHandler dbHandler;

    private void saveChatInDb(String peerId, String name, String sentMsg, String recMsg, String date, String sentTime, String recTime, String image, String chatType) {
        ChatDB db = new ChatDB(this);
        String timesttamp = System.currentTimeMillis() + "";

        db.addChat(new Chat(peerId, name, sentMsg, recMsg, date, "", recTime, image, 0, timesttamp, chatType));

       /* Intent intent = new Intent("MSG-UPDATE");
        intent.putExtra("peerId", peerId);
        intent.putExtra("msg", "receive");
        sendBroadcast(intent);*/
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

    private void startCallWithExpress() {

        Log.e(TAG, "startCallWithExpress: " + "call start");

        RatingDialog = true;
        mCallEnd = false;
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        callStartTime = Calendar.getInstance().getTime();
        //  busyOnCall = false;

        // AUTO_END_TIME=10000;
        Log.e(TAG, "startCallWithExpress: " + "auto end time  " + AUTO_END_TIME);
        talkTimeHandler.postDelayed(() -> {

            callEndCheck = true;
            endCall();
            Log.e(TAG, "startCallWithExpress: endCall -> talkTimeHandler");


            hangUpCall(true);
            // Toast.makeText(VideoChatZegoActivity.this, "Out of Balance", Toast.LENGTH_LONG).show();
        }, AUTO_END_TIME);


/*        IZegoEventHandler zegoEventHandler = new IZegoEventHandler() {
            @Override
            public void onNetworkModeChanged(ZegoNetworkMode zegoNetworkMode) {
                super.onNetworkModeChanged(zegoNetworkMode);
                Log.e("networkLog", zegoNetworkMode.toString());
                //Toast.makeText(ZVideoChatActivity.this, zegoNetworkMode.toString(), Toast.LENGTH_SHORT).show();

                if (!zegoNetworkMode.toString().equals("OFFLINE")) {
                    if (callEndCheck) {
                        endCall();
                        Log.e(TAG, "onNetworkModeChanged: endCall -> zegoNetworkMode");
                        hangUpCall(true);
                    }
                }
            }


            @Override
            public void onDebugError(int errorCode, String funcName, String info) {
                super.onDebugError(errorCode, funcName, info);
                Log.d(TAG, "onDebugError: " + errorCode + " ,funcName = " + funcName + " ,info = " + info);
            }

            @Override
            public void onRoomStateUpdate(String roomID, ZegoRoomState state, int errorCode, JSONObject extendedData) {
                super.onRoomStateUpdate(roomID, state, errorCode, extendedData);
                Log.d(TAG, "onRoomStateUpdate: roomID = " + roomID + " ,state = " + state + " ,errorCode = " + errorCode + " ,extendedData = " + extendedData);
            }

            @Override
            public void onRoomStateChanged(String roomID, ZegoRoomStateChangedReason reason, int errorCode, JSONObject extendedData) {
                super.onRoomStateChanged(roomID, reason, errorCode, extendedData);
                Log.d(TAG, "onRoomStateChanged: roomID = " + roomID + " ,reason = " + reason.toString() + " ,errorCode = " + errorCode + " ,extendedData = " + extendedData);
            }

            @Override
            public void onRoomUserUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoUser> userList) {
                super.onRoomUserUpdate(roomID, updateType, userList);
                Log.d(TAG, "onRoomUserUpdate: roomID = " + roomID + " ,updateType = " + updateType + " ,userList = " + userList);
            }

            @Override
            public void onRoomStreamUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoStream> streamList, JSONObject extendedData) {
                super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData);
                Log.d(TAG, "onRoomStreamUpdate: roomID = " + roomID + " updateType = " + updateType + " ,streamList = " + streamList + " ,extendedData = " + extendedData);
                for (ZegoStream stream : streamList) {
                    String streamID = stream.streamID;
                    if (updateType == ZegoUpdateType.ADD) {
                        engine.startPlayingStream(streamID, remoteCanvas);
                    } else {
                        Log.e(TAG, "onRoomStreamUpdate: endCall -> update type(not ADD)");
                        hangUpCall(false);
                        endCall();

                    }

                }

                Log.e("zegoCustomLog", "onRoomStreamUpdate" + " roomID = " + roomID);
                Log.e("zegoCustomLog", "onRoomStreamUpdate" + " updateType = " + updateType);
                Log.e("zegoCustomLog", "onRoomStreamUpdate" + " userList = " + new Gson().toJson(streamList));

                if (updateType.toString().equals("DELETE")) {
                    Log.e(TAG, "onRoomStreamUpdate: updatetype " + "  " + updateType.toString());

                    Log.e(TAG, "onRoomStreamUpdate: endCall -> update type(DELETE)");

                }

            }

            @Override
            public void onPublisherStateUpdate(String streamID, ZegoPublisherState state, int errorCode, JSONObject extendedData) {
                super.onPublisherStateUpdate(streamID, state, errorCode, extendedData);
                Log.d(TAG, "onPublisherStateUpdate: streamID = " + streamID + " ,state = " + state + " ,errorCode = " + errorCode + " ,extendedData = " + extendedData);
            }

            @Override
            public void onPublisherQualityUpdate(String streamID, ZegoPublishStreamQuality quality) {
                super.onPublisherQualityUpdate(streamID, quality);
                Log.d(TAG, "onPublisherQualityUpdate: streamID = " + streamID + " ,videoCaptureFPS = " + quality.videoCaptureFPS + " ,videoSendFPS = " + quality.videoSendFPS + " ,audioKBPS = " + quality.audioKBPS);
            }

            @Override
            public void onPlayerStateUpdate(String streamID, ZegoPlayerState state, int errorCode, JSONObject extendedData) {
                super.onPlayerStateUpdate(streamID, state, errorCode, extendedData);
                Log.d(TAG, "onPlayerStateUpdate: streamID = " + streamID + " ,state = " + state + " ,errorCode = " + errorCode + " ,extendedData = " + extendedData);

            }

            @Override
            public void onPlayerQualityUpdate(String streamID, ZegoPlayStreamQuality quality) {
                super.onPlayerQualityUpdate(streamID, quality);
                Log.d(TAG, "onPlayerQualityUpdate: streamID = " + streamID + " ,videoRecvFPS = " + quality.videoRecvFPS + " ,videoRenderFPS = " + quality.videoRenderFPS + " ,audioKBPS = " + quality.audioKBPS);

            }

            @Override
            public void onIMRecvCustomCommand(String roomID, ZegoUser fromUser, String command) {
                super.onIMRecvCustomCommand(roomID, fromUser, command);

                try {
                    JSONObject mainObject = new JSONObject(command);

                    if (mainObject.has("isMessageWithGift")) {
                        if (mainObject.get("isMessageWithGift").equals("yes")) {
                            JSONObject dataObj = new JSONObject(mainObject.get("GiftMessageBody").toString());
                            String giftData = dataObj.get("GiftData").toString();
                            NewGift giftDatanew = new Gson().fromJson(giftData, NewGift.class);
                            NewGiftAnimation(Integer.parseInt(dataObj.get("GiftPosition").toString()), dataObj.get("UserName").toString(), dataObj.get("ProfilePic").toString(), giftDatanew);
                        }

                    } else if (mainObject.has("isMessageWithAutoCallCut")) {

                        if (mainObject.get("isMessageWithAutoCallCut").equals("yes")) {
                            JSONObject dataObj = new JSONObject(mainObject.get("AutoCallCut").toString());
                            AUTO_END_TIME = Long.parseLong(dataObj.get("CallAutoEnd").toString());
                            if (talkTimeHandler != null) {
                                talkTimeHandler.removeCallbacksAndMessages(null);
                            }

                            talkTimeHandler.postDelayed(() -> {
                                callEndCheck = true;
                                endCall();
                                hangUpCall(true);
                                Log.e(TAG, "onIMRecvCustomCommand: talkTimeHandler end call ");
                            }, AUTO_END_TIME);
                        }
                    } else if (mainObject.has("isMessageWithCallEnd")) {

                        if (mainObject.get("isMessageWithCallEnd").equals("yes")) {
                            endCall();
                            hangUpCall(false);
                            //startActivity(new Intent(VideoChatZegoActivity.this, FastScreenActivity.class));
                            finish();
                            if (!userEndsCall) {
                                addCallEventTODb("video_call_completed", getCallDurationVideoCall());
                            }

                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };*/
        roomID = "_room_" + unique_id;

        streamID = roomID + "klive_video_call_";

        Log.e("VIDEO_CHAT_ACTIVITY", "startCallWithExpress: unique id " + unique_id);

        apiManager.sendCallRecord(new CallRecordBody(receiver_id, unique_id, new CallRecordBody.Duration(String.valueOf(System.currentTimeMillis()), "")));
        new FireBaseStatusManage(VideoChatZegoActivity.this, new SessionManager(getApplicationContext()).getUserId(), new SessionManager(getApplicationContext()).getUserName(),
                "", "", "Busy");
    }


    private void hangUpCall(boolean isSelf) {

        Log.e(TAG, "hangUpCall: " + "hangup");

        if (isSelf) {
            //  zimManager.callEnd(receiver_id);

            String endCallCommand_host = getEndCallData();
            sendZegoCustomCommand(endCallCommand_host);

        }
        if (callType == "video") {
        }

        finish();
    }


    @SuppressLint("LongLogTag")
    private void initSDK() {

      /*  ZegoCanvas localPreviewCanvas = new ZegoCanvas(LocalView);
        localPreviewCanvas.viewMode = ZegoViewMode.ASPECT_FILL;
        engine.startPreview(localPreviewCanvas);*/
        // new ApiManager(this,this).getZegoNewToken(new SessionManager(this).getUserId());

    }


    @Override
    public void finish() {
        super.finish();

    }


    public void onLocalContainerClick(View view) {
        switchView(RemoteView);
        switchView(LocalView);

    }

    private void switchView(TXCloudVideoView VIEW) {

        ViewGroup parent = removeFromParent(VIEW);

        if (parent == frameLayoutLocal) {
            frameLayoutRemote.addView(VIEW);
            Log.e("changeView", "local");
        } else if (parent == frameLayoutRemote) {
            frameLayoutLocal.addView(VIEW);
            Log.e("changeView", "remote");
        }

    }

    private ViewGroup removeFromParent(TXCloudVideoView view) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ViewGroup viewGroup = (ViewGroup) parent;
            viewGroup.removeView(view);
            Log.e("changeView", "removeFromParent: " + viewGroup.toString());
            return viewGroup;

        }
        return null;
    }


    public void onBeautyClicked(View view) {

    }


    public void onCallClicked(View view) {
        if (mCallEnd) {
            mCallEnd = false;
            mCallBtn.setImageResource(R.drawable.btn_endcall);
        } else {

            onBackPressed();

        }


    }


    private void endCall() {
        /*removeFromParent(LocalView);
        removeFromParent(RemoteView);*/
        // Calculate call charges accordingly
        endTimeVideoEvent = Calendar.getInstance().getTime();
        getCallDuration(endTimeVideoEvent);
        long mills = Calendar.getInstance().getTime().getTime() - callStartTime.getTime();
        exitRoom();
        //  Log.e("callTime", "" + mills);
        // Log.e(TAG, "endCall: "+"true" );
    }

    void getCallDuration(Date endTime) {

        if (callStartTime != null) {
            long mills = endTime.getTime() - callStartTime.getTime();

            int hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills / (1000 * 60)) % 60;
            int sec = (int) (mills - hours * 3600000 - mins * 60000) / 1000;
            if (mins < 1 && sec > 1 && sec < 60) {

                // Log.e("endCALLApi",unique_id)

                apiManager.endCall(new CallRecordBody("", unique_id, Boolean.parseBoolean(is_free_call), new CallRecordBody.Duration("", String.valueOf(System.currentTimeMillis()))));

            } else {
                int roundOf = 1;
                int totalMins;

                if (sec > 6) {
                    totalMins = mins + roundOf;
                } else {
                    totalMins = mins;
                }

                /*  apiManager.endCall(new CallRecordBody("", unique_id,new CallRecordBody.Duration("", String.valueOf(System.currentTimeMillis()))));*/

                apiManager.
                        endCall(new CallRecordBody("", unique_id, Boolean.parseBoolean(is_free_call), new CallRecordBody.Duration("", String.valueOf(System.currentTimeMillis()))));
            }
        } else {
            if (talkTimeHandler != null) {
                talkTimeHandler.removeCallbacksAndMessages(null);
            }
            // finish();
        }
    }


    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VideoChatZegoActivity.this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;

    }

    RelativeLayout rl_menu;


    private void initUI() {
        LocalView = findViewById(R.id.LocalView);
        RemoteView = findViewById(R.id.RemoteView);

        mRemoteUidList = new ArrayList<>();
        mRemoteViewList = new ArrayList<>();
        mRemoteViewList.add((TXCloudVideoView) findViewById(R.id.RemoteView));

        frameLayoutRemote = findViewById(R.id.remote_video_view_container);
        frameLayoutLocal = findViewById(R.id.local_video_view_container);
        mCallBtn = findViewById(R.id.btn_call);

        CallerNameText = findViewById(R.id.tv_username);
        CallerImage = findViewById(R.id.img_profilepic);
        chronometer = findViewById(R.id.chronometer);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);
        rl_menu = findViewById(R.id.rl_menu);


        messagesView = (RecyclerView) findViewById(R.id.lv_allmessages);
        rv_gift = findViewById(R.id.rv_gift);

        if (getIntent() != null) {
            receiver_id = getIntent().getStringExtra("receiver_id");
            CallerName = getIntent().getStringExtra("name");
            CallerUserName = getIntent().getStringExtra("username");
            ZegoToken = getIntent().getStringExtra("token");
            is_free_call = getIntent().getStringExtra("is_free_call");
            Log.e(TAG, "initUI: is_free_call  " + is_free_call);
            unique_id = getIntent().getStringExtra("unique_id");
            CallerProfilePic = getIntent().getStringExtra("image");
            //  callType = getIntent().getStringExtra("callType");

            callType = "video";
            AUTO_END_TIME = getIntent().getLongExtra("CallEndTime", 2000);

            Log.e("testttst", "host side: " + AUTO_END_TIME);

            // AUTO_END_TIME = getIntent().getIntExtra("CallEndTime", 2000);
            Log.e("Auto_End_Time", "initUI: CallEndTime long " + AUTO_END_TIME);
            Log.e("Auto_End_Time", "initUI: CallEndTime  " + getIntent().getIntExtra("CallEndTime", 0));
            Log.e(TAG, "callType getIntent   " + callType);
        }


        if (CallerName.length() > 12) {
            CallerName = CallerName.substring(0, 11) + "...";
        }
        CallerNameText.setText(CallerName);


        if (gender.equals("male")) {
            mSwitchCameraBtn.setVisibility(View.GONE);
        } else {
            mSwitchCameraBtn.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_giftmsg)).setText("You can request for gift~");
            //  ((TextView) findViewById(R.id.tv_giftmsg)).setText("You can request for gift by just tapping on that~");
        }

        ((ImageView) findViewById(R.id.img_send)).setEnabled(false);
        ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.inactivedownloadarrow));


        ((RelativeLayout) findViewById(R.id.rl_chat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.rl_msgsend)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.rl_end)).setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(((EditText) findViewById(R.id.et_message)).getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                ((EditText) findViewById(R.id.et_message)).requestFocus();
            }
        });


        frameLayoutRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((RelativeLayout) findViewById(R.id.rl_bottom)).getVisibility() == View.VISIBLE) {
                    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                    hideKeybaord(view);
                }
                if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
                    ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
                    messagesView.setVisibility(View.VISIBLE);
                }
            }
        });

        //  RemoteView.setOnClickListener(new View.OnClickListener() {
        //      @Override
        //      public void onClick(View view) {
        //          if (((RelativeLayout) findViewById(R.id.rl_bottom)).getVisibility() == View.VISIBLE) {
        //              ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
        //              hideKeybaord(view);
        //          }
        //          if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
        //              ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
        //              messagesView.setVisibility(View.VISIBLE);
        //          }
        //      }
        //  });

        initKeyBoardListener();


        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.HORIZONTAL, false);
        rv_gift.setLayoutManager(gridLayoutManager);
        giftAdapter = new GiftAdapter(giftArrayList, R.layout.rv_gift, getApplicationContext());
        rv_gift.setAdapter(giftAdapter);


        ((RelativeLayout) findViewById(R.id.rl_giftin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagesView.setVisibility(View.GONE);
                // ((ImageView) findViewById(R.id.img_gift)).performClick();

                NewGiftListResponse response = new SessionManager(VideoChatZegoActivity.this).getCategoryGiftList();


                if (response != null) {
                    GiftBottomSheetDialog bottomSheet = new GiftBottomSheetDialog(VideoChatZegoActivity.this, (ArrayList<NewGiftResult>) response.getResult(), new GiftSelectListener() {
                        @Override
                        public void OnGiftSelect(NewGift giftData) {
                            Log.e("VC_NEWGIFTTESTT", "OnGiftSelect: " + giftData);
                            Log.e("VC_NEWGIFTTESTT1", "onBindViewHolder: " + giftData.getGift_name());
                        }
                    });
                    bottomSheet.show(VideoChatZegoActivity.this.getSupportFragmentManager(), "GiftBottomSheet");

                } else {


                }


            }
        });


        ((ImageView) findViewById(R.id.img_gift)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeybaord(view);

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
                                //  Log.e("onGift: ", new Gson().toJson(response.body()));

                                if (response.body().isStatus()) {

                                    /*
                                    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                                    ((RelativeLayout) findViewById(R.id.rl_msgsend)).setVisibility(View.GONE);
                                    ((RelativeLayout) findViewById(R.id.rl_end)).setVisibility(View.GONE);
                                   */

                                    ((ImageView) findViewById(R.id.img_giftloader)).setVisibility(View.GONE);

//                                  ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.VISIBLE);
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

                } else {
                    ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
                }
            }
        });


        ItemClickSupport.addTo(rv_gift).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                if (gender.equals("male")) {


                } else {
                    Log.e("testtttttt", "onItemClicked:  " + "gift request sent");
                    //apiManager.hostSendGiftRequest(new RequestGiftRequest(String.valueOf(giftArrayList.get(position).getId()), receiver_id));
                    //AppLifecycle appLifecycle = new AppLifecycle();
                    //appLifecycle.sendGiftRequest(giftArrayList.get(position).getId(), receiver_id);
                    //giftAnimation(giftArrayList.get(position).getId());
                }
            }
        });

        ((EditText) findViewById(R.id.et_message)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 > 0) {
                    ((ImageView) findViewById(R.id.img_send)).setEnabled(true);
                    ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.activedownloadarrow));
                } else {
                    ((ImageView) findViewById(R.id.img_send)).setEnabled(false);
                    ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.inactivedownloadarrow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((ImageView) findViewById(R.id.img_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeybaord(view);
                // sendMessage("text", "", "");
            }
        });

        ((EditText) findViewById(R.id.et_message)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //do what you want on the press of 'done'
                ((ImageView) findViewById(R.id.img_send)).performClick();
                return true;
            }
        });
        // loadGiftData();

        enterRoom();
        rl_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoMenuSheetDialog = new VideoMenuSheetDialog(VideoChatZegoActivity.this, isCameraOff, mIsFrontCamera, isMikeMute);
                videoMenuSheetDialog.show(VideoChatZegoActivity.this.getSupportFragmentManager(), "videogiftsheet");
            }
        });
    }

    VideoMenuSheetDialog videoMenuSheetDialog;

    private boolean mIsFrontCamera = true;
    boolean isCameraOff = false;

    public void cameraOffFun() {
        if (!isCameraOff) {
            mTRTCCloud.stopLocalPreview();
            isCameraOff = true;
        } else {
            mTRTCCloud.startLocalPreview(mIsFrontCamera, LocalView);
            isCameraOff = false;
        }
    }

    public void flipCamera() {
        // mIsFrontCamera = !mIsFrontCamera;

        if (mIsFrontCamera) {
            videoMenuSheetDialog.setCameraName("Rear Camera");
            mIsFrontCamera = false;
        } else {
            videoMenuSheetDialog.setCameraName("Front Camera");
            mIsFrontCamera = true;
        }
        mTXDeviceManager.switchCamera(mIsFrontCamera);

    }

    boolean isMikeMute = false;

    public void muteMic() {
        if (!isMikeMute) {
            mTRTCCloud.muteLocalAudio(true);
            isMikeMute = true;
        } else {
            mTRTCCloud.muteLocalAudio(false);
            isMikeMute = false;
        }
    }

    public void onSwitchCameraClicked(View view) {
        // mRtcEngine.switchCamera();
        switchView(RemoteView);
        switchView(LocalView);


    }

    private TRTCCloud mTRTCCloud;
    private TXDeviceManager mTXDeviceManager;
    private List<String> mRemoteUidList;
    private List<TXCloudVideoView> mRemoteViewList;

    private void enterRoom() {
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(new TRTCCloudImplListener(VideoChatZegoActivity.this));
        // initFuView();
        // initData();
        initCallBeautyParams();
        new FloatView(VideoChatZegoActivity.this, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight() - 150).initGestureListener(findViewById(R.id.smallViewRLay));
        mTXDeviceManager = mTRTCCloud.getDeviceManager();

        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        trtcParams.userId = receiver_id;
        trtcParams.strRoomId = unique_id;

        trtcParams.userSig = GenerateTestUserSig.genTestUserSig(trtcParams.userId);
        trtcParams.role = TRTCCloudDef.TRTCRoleAnchor;
        Log.e("chkckkaarid", "" + unique_id);

        mTRTCCloud.startLocalPreview(true, LocalView);
        mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT);
        mTRTCCloud.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);

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
        mTRTCCloud.getBeautyManager().setBeautyLevel(7f);

    }


    private class TRTCCloudImplListener extends TRTCCloudListener {

        private WeakReference<VideoChatZegoActivity> mContext;

        public TRTCCloudImplListener(VideoChatZegoActivity activity) {
            super();
            mContext = new WeakReference<>(activity);
        }

        @Override
        public void onEnterRoom(long result) {
            super.onEnterRoom(result);
            Log.e("chkckkaa", "" + "Entered Room " + result);

        }

        @Override
        public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
            super.onNetworkQuality(localQuality, remoteQuality);
            // Get your local network quality
            Log.e("chkckkaa", "" + localQuality.quality);
           /* switch(localQuality) {
                case TRTCQuality_Unknown:
                    Log.d(TAG, "SDK has not yet sensed the current network quality.");
                    break;
                case TRTCQuality_Excellent:
                    Log.d(TAG, "The current network is very good.");
                    break;
                case TRTCQuality_Good:
                    Log.d(TAG, "The current network is good.");
                    break;
                case TRTCQuality_Poor:
                    Log.d(TAG, "The current network quality barely meets the demand.");
                    break;
                case TRTCQuality_Bad:
                    Log.d(TAG, "The current network is poor, and there may be significant freezes and call delays.");
                    break;
                case TRTCQuality_VeryBad:
                    Log.d(TAG, "The current network is very poor, the communication quality cannot be guaranteed");
                    break;
                case TRTCQuality_Down:
                    Log.d(TAG, "The current network does not meet the minimum requirements.");
                    break;
                default:
                    break;
            }
            // Get the network quality of remote users
            for (TRTCCloudDef.TRTCQuality info : arrayList) {
                Log.d(TAG, "remote user : = " + info.userId + ", quality = " + info.quality);
            }*/

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
            hangUpCall(true);
            endCall();
        }


        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
            VideoChatZegoActivity activity = mContext.get();
            if (activity != null) {
                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
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

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }


    int incPos = 0;
    private boolean isStackof3Full = false;

    @SuppressLint("LongLogTag")
    private void NewGiftAnimation(int giftId, String peerName, String peerProfilePic, NewGift giftDatanew) {


        if (giftAnimationRecyclerAdapter != null) {
            Log.e("GiftAnimationBug ", "giftAnimationRecyclerAdapter" + " in VideoChatActivity not null");
        } else {
            Log.e(BugTAG, "giftAnimationRecyclerAdapter" + " in VideoChatActivity null");
        }

        if (giftAnimRecycler.getAdapter() != null) {
            Log.e("GiftAnimationBug ", "getAdapter()" + " in VideoChatActivity not null");
        } else {
            Log.e(BugTAG, "getAdapter()" + "  in VideoChatActivity null");
        }


        mp = MediaPlayer.create(this, R.raw.giftem);
        mp.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.stop();
            }
        }, 3000);

        Log.e(BugTAG, "Go into NewGiftAnimation method in " + "VideoChatActivity " + " true");
        // giftAnimRecycler.setVisibility(View.VISIBLE);
        if (!isStackof3Full) {
            giftdataList.add(new GiftAnimData(getGiftResourceId(giftId), peerName, peerProfilePic, giftDatanew));
            Log.e(BugTAG, "Gift added in giftdataList " + "VideoChatActivity " + " true");
            giftAnimationRecyclerAdapter.notifyItemInserted(incPos);
            Log.e(BugTAG, "Push to perform Animation from " + "VideoChatActivity " + " true");
        } else {
            giftdataList.set(incPos, new GiftAnimData(getGiftResourceId(giftId), peerName, peerProfilePic, giftDatanew));
            giftAnimationRecyclerAdapter.notifyItemChanged(incPos);
        }
        // Log.e("GiftListSize",""+giftdataList.size());

        Log.e(BugTAG, "GiftListSize " + "VideoChatActivity " + giftdataList.size());

        Log.e(BugTAG, "incpos " + "VideoChatActivity " + incPos);

        Log.e(BugTAG, "Visibility of Gift Recycler in " + "VideoChatActivity " + giftAnimRecycler.getVisibility());


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


    private void giftAnimation(int position) {
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.VISIBLE);
        mp = MediaPlayer.create(this, R.raw.giftem);
        Log.e("playMp3gift", "playMp3giftSongs");
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
                        //     Log.e("Keyboard", "HIDE");
                        ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
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


    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

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

        if (ServiceCode == Constant.GENERATE_ZEGO_NEW_TOKEN) {
            NewZegoTokenResponse rsp = (NewZegoTokenResponse) response;
            // startPublish(rsp.getResult().getToken(), rsp.getResult().getRoomId());
        }


    }

    protected void onResume() {
        //  initSocket();
        super.onResume();
        giftAnimRecycler.setVisibility(View.VISIBLE);
        registerReceiver(getMyGiftReceiver, new IntentFilter("GIFT-USER-INPUT"));

    }

    private static int giftPosition = -1;
    private int fPosition;
    private Handler handlerGift = new Handler();
    public BroadcastReceiver getMyGiftReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("GiftPosition");
            String from = intent.getStringExtra("type");
            String giftImage = intent.getStringExtra("GiftImage");
            int giftId = Integer.parseInt(action);
            giftPosition = giftId;
            Log.e("chdsksaa", "Broadcast receive " + giftImage);
            if (from.equals("giftSend")) {
                handlerGift.removeCallbacksAndMessages(null);
                Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.VISIBLE);

                Glide.with(VideoChatZegoActivity.this)
                        .load(giftImage)
                        .into((ImageView) findViewById(R.id.img_imageShow));
                animFadeIn.reset();
                ((ImageView) findViewById(R.id.img_imageShow)).clearAnimation();
                ((ImageView) findViewById(R.id.img_imageShow)).startAnimation(animFadeIn);

                handlerGift.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.GONE);
                    }
                }, 3000);
/*
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
                }, 3000);*/
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

         /*       ((TextView) findViewById(R.id.tv_sendGift)).setOnClickListener(new View.OnClickListener() {
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
                            new ApiManager(getApplicationContext(), VideoChatZegoActivity.this).sendUserGift(new SendGiftRequest(Integer.parseInt(reciverId), call_unique_id,
                                    giftArrayList.get(position).getId(), giftArrayList.get(position).getAmount(), startTimeStamp, String.valueOf(System.currentTimeMillis())));

                            // sendMessage("gift", String.valueOf(giftId), "");

                        } else {
                            Toast.makeText(VideoChatZegoActivity.this, "Out of Balance", Toast.LENGTH_LONG).show();
                        }

                    }
                });*/
            }
        }
    };

    public void onDestroy() {
        super.onDestroy();

        // mFURenderer.release();
        //storeBusyStatus("Live");

        Log.e(TAG, "onDestroy: " + "Activity Destroyed");

        // zimManager.removeListener(zimEventListener);
        if (RatingDialog) {
            getRating();
        }
        if (getMyGiftReceiver != null) {
            try {
                unregisterReceiver(getMyGiftReceiver);

            } catch (Exception e) {

            }
        }
    }

    public void getRating() {
        String call_duration = chronometer.getText().toString();
        if (new SessionManager(getApplicationContext()).getGender().equals("female")) {
            // manage facedete or broadcast
            if (new SessionManager(getApplicationContext()).getHostAutopickup().equals("yes")) {
                //Intent intent = new Intent(VideoChatZegoActivity.this, FastScreenActivity.class);
                //startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (((RelativeLayout) findViewById(R.id.rl_bottom)).getVisibility() == View.VISIBLE) {
            ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
            //hideKeybaord(view);
        }
        if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
            ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
            //messagesView.setVisibility(View.VISIBLE);
        } else {
            final Dialog dialog = new Dialog(VideoChatZegoActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.disconnectcalldialog);

            DisplayMetrics metrics = new DisplayMetrics();
            int width = (int) (metrics.widthPixels * 0.9);

            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();

            TextView text = (TextView) dialog.findViewById(R.id.msg);
            TextView tv_dailogcancel = (TextView) dialog.findViewById(R.id.tv_dailogcancel);
            TextView tv_dailogconfirm = (TextView) dialog.findViewById(R.id.tv_dailogconfirm);

            // text.setText("If you hang up this video call now, you will not receive coins for this present video call. Are you sure to do that?");

            text.setText("Are you sure to close the video call?");


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
                    // RtcEngine.destroy();
                    dialog.dismiss();
                    //  apiManager.getcallCutByHost(unique_id);

                    Log.e(TAG, "tv_dailogconfirm : endCall -> on click");
                    endCall();
                    hangUpCall(true);
                    // new ApiManager(getApplicationContext()).changeOnlineStatus(1);
                    addCallEventTODb("video_call_completed_user", getCallDurationVideoCall());
                }
            });

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // EndCallData endCallData = new EndCallData(unique_id, String.valueOf(System.currentTimeMillis()));
        // list.add(endCallData);
        //  endCall();

        new SessionManager(getApplicationContext()).setUserEndcalldata(list);
        new SessionManager(getApplicationContext()).setUserGetendcalldata("error");
    }


    //fuzego face tracker


    private void sendZegoCustomCommand(String message) {

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


}