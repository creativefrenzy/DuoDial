package com.klive.app.Inbox;

import static com.klive.app.main.Home.unread;
import static com.klive.app.utils.AppLifecycle.ZEGOTOKEN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.klive.app.R;
import com.klive.app.adapter.GiftAnimationRecyclerAdapter;
import com.klive.app.model.IncomeReportResponce.IncomeReportFemale;
import com.klive.app.model.gift.GiftAnimData;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.sqlite.Chat;
import com.klive.app.sqlite.ChatDB;
import com.klive.app.sqlite.SystemDB;
import com.klive.app.utils.AppLifecycle;
import com.klive.app.utils.Constant;
import com.klive.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InboxDetails extends AppCompatActivity implements ApiResponseInterface {
    SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy");
    private static String peerId, peerName, peerImage;
    public static List<ChatBean> data;
    private EditText et_message;
    private ArrayList<String> allMsg = new ArrayList<String>();
    public static RecyclerView recyclerView;
    public static ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    ChatDB db;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private final int chatLoadLimit = 10;
    private ApiManager apiManager;
    private AppLifecycle appLifecycle;
    private int coins = 0;
    private LinearLayout toast;

    DatabaseReference chatRef;

    TextView UserStatus;
  //  private ZegoUserService userService;

    private RecyclerView giftAnimRecycler;

    List<GiftAnimData> giftdataList = new ArrayList<GiftAnimData>();

    Handler handler = new Handler();

    boolean isFirstTimeGift = false;
    private GiftAnimationRecyclerAdapter giftAnimationRecyclerAdapter;

    private SystemDB systemDB;


    private String mesaagewithcall;
    private JSONObject MessageWithCallJson;
    private JSONObject MessageWithChatJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_details);

        initZegoListener();
        init();






       // new SessionManager(this).setSystemMessageCounter(0);

        //initChatList();
    }

    private void initZegoListener() {
     /*   userService=ZegoRoomManager.getInstance().userService;

        userService.setListener(new ZegoUserServiceListener() {
            @Override
            public void onUserInfoUpdated(ZegoUserInfo userInfo) {

            }

            @Override
            public void onReceiveCallInvite(ZegoUserInfo userInfo, ZegoCallType type) {

                Log.e("inviteCall_Or_Message", "incoming call");


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (MessageWithCallJson != null) {
                            Log.e("MessageWithCallJson", MessageWithCallJson.toString());

                            Intent incoming = new Intent(InboxDetails.this, IncomingCallScreen.class);
                            try {
                                incoming.putExtra("receiver_id", MessageWithCallJson.get("UserId").toString());
                                incoming.putExtra("username", MessageWithCallJson.get("UserName").toString());
                                incoming.putExtra("unique_id", MessageWithCallJson.get("UniqueId").toString());
                                // incoming.putExtra("channel_name", MessageWithCallJson.get("UniqueId").toString());
                                incoming.putExtra("token", ZEGOTOKEN);
                                incoming.putExtra("callType", MessageWithCallJson.get("CallType").toString());
                                incoming.putExtra("is_free_call", MessageWithCallJson.get("IsFreeCall").toString());
                                incoming.putExtra("name", MessageWithCallJson.get("Name").toString());
                                incoming.putExtra("image", MessageWithCallJson.get("ProfilePicUrl").toString());
                                incoming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(incoming);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }, 200);


            }

            @Override
            public void onReceiveCallCanceled(ZegoUserInfo userInfo, ZegoCancelType cancelType) {

                Log.i("ZegoCall-Klive", "canceled " + cancelType);
                //  if(cancelType==ZegoCancelType.TIMEOUT)
                //  {
                //      Log.i("callCanceled","timeout");
                //  }
            }


            @Override
            public void onReceiveCallResponse(ZegoUserInfo userInfo, ZegoResponseType type) {
                Log.i("ZegoCall-Klive", "recieveCall response" + type);
            }

            @Override
            public void onReceiveCallEnded() {
                Log.i("ZegoCall-Klive", "recieveCall Ended");
            }

            @Override
            public void onReceiveZIMPeerMessage(ZIMMessage zimMessage, String fromUserID) {

                if (zimMessage.type == ZIMMessageType.TEXT) {
                    ZIMTextMessage textMessage = (ZIMTextMessage) zimMessage;
                    String messageString = textMessage.message;

                    //   Log.e("MessageReceived", "yes");
                    //    Log.d(TAG, "onReceivePeerMessage: " + messageString);

                    try {
                        JSONObject jsonObject = new JSONObject(messageString);

                        if (jsonObject.has("isMessageWithCall")) {

                            if (jsonObject.get("isMessageWithCall").toString().equals("yes")) {
                                mesaagewithcall = textMessage.message;
                                MessageWithCallJson = new JSONObject(jsonObject.get("CallMessageBody").toString());
                            }

                        } else if (jsonObject.has("isMessageWithChat")) {

                            if (jsonObject.get("isMessageWithChat").toString().equals("yes")) {

                                MessageWithChatJson = new JSONObject(jsonObject.get("ChatMessageBody").toString());
                                if (MessageWithChatJson != null) {
                                    Log.e("MessageWithChatJson", MessageWithChatJson.toString());

                                    saveChatInDb(fromUserID, MessageWithChatJson.get("UserName").toString(), "", MessageWithChatJson.get("Message").toString(), MessageWithChatJson.get("Date").toString(),
                                            "", MessageWithChatJson.get("Time").toString(), MessageWithChatJson.get("ProfilePic").toString());

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
                    }

                } else if (zimMessage.type == ZIMMessageType.CUSTOM) {
                    ZIMCustomMessage msg = (ZIMCustomMessage) zimMessage;
                    Log.d("TAG", "onReceivePeerMessage: " + zimMessage.type + ",msg = " + msg.\message.toString());
                }
            }

            @Override
            public void onConnectionStateChanged(ZIMConnectionState state, ZIMConnectionEvent event) {
                Log.i("onConnectionState", "connection state changed");
            }

            @Override
            public void onNetworkQuality(String userID, ZegoNetWorkQuality quality) {
                Log.i("onNetworkQuality", "network quality " + quality);
            }


        });*/
    }


    private void saveChatInDb(String peerId, String name, String sentMsg, String recMsg, String date, String sentTime, String recTime, String image,String chatType) {
        ChatDB db = new ChatDB(this);
        String timesttamp = System.currentTimeMillis() + "";
        db.addChat(new Chat(peerId, name, "", recMsg, date, "", recTime, image, 0, timesttamp,chatType));

        Intent intent = new Intent("MSG-UPDATE");
        intent.putExtra("peerId", peerId);
        intent.putExtra("msg", "receive");
        sendBroadcast(intent);


        Log.e("recievemsggg", "savedChatInDB called");
        Log.e("MessageSavedInChat", "saved");

        /*  Intent intent1 = new Intent("MSG-UPDATE-NEW");
        intent1.putExtra("peerId", peerId);
        intent1.putExtra("msg", "receive");
        appContext.sendBroadcast(intent1);

        Intent intent2 = new Intent("MSG-UPDATE3");
        intent2.putExtra("peerId", peerId);
        intent2.putExtra("msg", "receive");
        appContext.sendBroadcast(intent2);*/
    }



    private void initChatList() {
        List<Chat> chats = db.getChatList(peerId, 0, chatLoadLimit);

        for (Chat cn : chats) {
            data.add(new ChatBean(peerId, cn.get_text_get(), cn.get_time_get(), cn.get_text_sent(), cn.get_time_sent(),cn.get_chatType()));
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name() + " ,Text: " + cn.get_text_get();
            Log.d("Name: ", log);
        }

        chatAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(data.size());

        initScrollListner();
    }

    private void init() {
        db = new ChatDB(this);

       // userService = ZegoRoomManager.getInstance().userService;

        searchWordList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.searchWordsArray)));
        data = new ArrayList<>();
        peerId = getIntent().getStringExtra("peerId");
        peerName = getIntent().getStringExtra("peerName");
        peerImage = getIntent().getStringExtra("peerImage");
        apiManager = new ApiManager(getApplicationContext(), this);
        appLifecycle = new AppLifecycle();
        apiManager.getWalletHistoryFemaleNew();
        et_message = findViewById(R.id.et_message);
        et_message.setLongClickable(false);
        et_message.setTextIsSelectable(false);
        ImageView profile = findViewById(R.id.img_profile);
        TextView username = findViewById(R.id.tv_username);

        giftAnimRecycler = findViewById(R.id.gift_animation_recyclerview);
        toast = findViewById(R.id.custom_toast_layout);
        Glide.with(this).load(peerImage).placeholder(R.drawable.default_profile).into(profile);
        username.setText(peerName);
        recyclerView = findViewById(R.id.chat_recyclerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
       /* layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);*/
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(this, data);
        recyclerView.setAdapter(chatAdapter);

        UserStatus = findViewById(R.id.status);

        List<Chat> chats = db.getAllChat(peerId);



        for (Chat cn : chats) {
            data.add(new ChatBean(peerId, cn.get_text_get(), cn.get_time_get(), cn.get_text_sent(), cn.get_time_sent(),cn.get_chatType()));
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name() + " ,Text: " + cn.get_text_get();
            Log.d("Name: ", log);
        }

        chatAdapter.notifyDataSetChanged();
        //   recyclerView.smoothScrollToPosition(data.size());
        recyclerView.scrollToPosition(data.size() - 1);
        checkOnlineOfflineStatus();


        giftAnimRecycler.setHasFixedSize(true);
        giftAnimRecycler.setLayoutManager(new LinearLayoutManager(this));
         giftAnimationRecyclerAdapter = new GiftAnimationRecyclerAdapter(giftdataList, getApplicationContext(), new GiftAnimationRecyclerAdapter.OnItemInvisibleListener() {
            @Override
            public void onItemInvisible(int adapterposition) {
                //  giftdataList.remove(adapterposition);
            }
        });
        giftAnimRecycler.setAdapter(giftAnimationRecyclerAdapter);


    }




    private void checkOnlineOfflineStatus() {
        String uid = String.valueOf(data.get(0).getId());
        Log.e("onCancelledFirebase", "onDataChange: "+uid);

        //  chatRef= FirebaseDatabase.getInstance().getReference().child("Users").child("672206762");
        chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> map = null;

                if (snapshot.exists()) {
                    map = (Map<String, Object>) snapshot.getValue();
                    // Toast.makeText(getApplicationContext(),""+map.get("status"),Toast.LENGTH_LONG).show();

                    Log.e("onCancelledFirebase", "onDataChange: "+map.get("status").toString() );

                    if (map.get("status").equals("Offline")) {
                        UserStatus.setText(map.get("status").toString());
                        UserStatus.setTextColor(getResources().getColor(R.color.black));
                    }

                    if (map.get("status").equals("Online")) {
                        UserStatus.setText(map.get("status").toString());
                        UserStatus.setTextColor(getResources().getColor(R.color.black));
                    }

                } else {
                   // UserStatus.setText("Offline : Test User in development.");
                    UserStatus.setText("");
                    UserStatus.setTextColor(getResources().getColor(R.color.black));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("onCancelledFirebase", "onCancelled: "+"error " );
            }
        });


    }

    private void customToast() {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.required_coins, (ViewGroup) toast);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    private void initScrollListner() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            //Log.e("onScrolledRV", "size=" + messageBeanList.size());
                            List<Chat> messageListBeans = db.getChatList(peerId, data.size(), chatLoadLimit);
                            if (!messageListBeans.isEmpty()) {
                                for (Chat cn : messageListBeans) {
                                    data.add(new ChatBean(peerId, cn.get_text_get(), cn.get_time_get(), cn.get_text_sent(), cn.get_time_sent(),cn.get_chatType()));
                                }
                                //data.addAll(messageListBeans);
                                //convertChatListWithHeader();
                            }
                            chatAdapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(data.size());
                            //Toast.makeText(MessageActivity.this, "loading", Toast.LENGTH_LONG).show();
                            loading = true;
                        }
                    }
                }
            }
        });
    }

    public void backFun(View v) {
        onBackPressed();
    }

    public void openVideoChat(View view) {
        //callType = "video";
        //apiManager.getRemainingGiftCardFunction();
    }

    public void openGiftLayout(View v) {
        //new GiftDialog(InboxDetails.this, receiverUserId);
    }

    public void csSend(View v) {
        hideKeybaord(v);
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public void sendMsgFun(View v) {
        //coins = 5000;
        if (coins >= 0) {
            sendMessage("text", "", "");
        } else {
            customToast();
            //Toast.makeText(getApplicationContext(), "Insufficient Coins", Toast.LENGTH_LONG).show();
        }
    }


    private void sendMessage(String type, String giftId, String giftAmount) {
        String msg = "";
        if (type.equals("text")) {
            int insertIndex = data.size();
            String message_content = et_message.getText().toString();
            if (message_content.length() > 0) {
                inputSentence = message_content;
                String regex = "[-+^#<!@>$%&({*}?/.=~),'_:]*";
                inputSentence = inputSentence.replaceAll(regex, "").replaceAll("\\[", "").replaceAll("\\]", "");
                Log.e("InboxDetail", "sendMessage: "+inputSentence );
                String outPut = elasticSearch(inputSentence, searchWordList);
                message_content = outPut;
                Date date = new Date();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                String userName = sessionManager.getUserName();
                String userProfilePic = sessionManager.getUserProfilepic();
              appLifecycle.sendZegoChatMessage(peerId, message_content, dateformatter.format(date), timeformatter.format(date), userName, userProfilePic);
                ChatDB db = new ChatDB(this);
                String timesttamp = System.currentTimeMillis() + "";
                db.addChat(new Chat(peerId, peerName, message_content, "", dateformatter.format(date), timeformatter.format(date), "", peerImage, 1, timesttamp,"TEXT"));
                data.add(new ChatBean(peerId, "", "", message_content, timeformatter.format(date),"TEXT"));
                chatAdapter.notifyItemInserted(insertIndex);
                recyclerView.smoothScrollToPosition(data.size());

                Intent intent = new Intent("MSG-UPDATE-SENT");
                intent.putExtra("peerId", peerId);
                intent.putExtra("msg", "sent");
                getApplicationContext().sendBroadcast(intent);
            }
            message_content = "";
            et_message.setText("");
        }
    }

    private String inputSentence;
    private List<String> searchWordList;

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

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(getMyGift, new IntentFilter("GIFT-USER-TEXT"));
        registerReceiver(getMyMsgRec, new IntentFilter("USER-TEXT"));

        //storeStatus("Online");

       // AppLifecycle.getAppInstance().ZegoListener();
      //appLifecycle.ZegoListener();

    }

    public void onDestroy() {
        super.onDestroy();
   //   ZegoRoomManager.getInstance().unInit();
        unregisterReceiver(getMyGift);
        unregisterReceiver(getMyMsgRec);
    }

    public BroadcastReceiver getMyGift = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String giftPos = intent.getStringExtra("pos");
            String peer_id = intent.getStringExtra("peerId");
            String peerName = intent.getStringExtra("peerName");
            String peerProfilePic = intent.getStringExtra("peerProfilePic");


            try {

                if (peer_id.equals(peerId)) {

                    db.updateRead(peerId);
                    int countAll = db.getAllChatUnreadCount(peerId);
                    if (countAll > 0) {
                        unread.setVisibility(View.VISIBLE);
                        unread.setText(String.valueOf(countAll));
                    } else {
                        unread.setVisibility(View.INVISIBLE);
                    }

                    data.add(new ChatBean(peerId, giftPos, "", "", "","GIFT"));
                    recyclerView.smoothScrollToPosition(data.size());
                    chatAdapter.notifyDataSetChanged();


                }

            }
            catch (Exception e)
            {
                Log.e("InboxFragment", "onReceive: Exception "+e.getMessage() );

            }


















        /*    int giftId = Integer.parseInt(giftPos);

            isFirstTimeGift = false;


            if (peer_id.equals(peerId)) {
                NewGiftAnimation(giftId, peerName, peerProfilePic);
                Log.e("getMyGift", "" + giftPos + "  " + peer_id + "  " + peerName + "  " + peerProfilePic);
            }
*/

        }
    };




    int incPos = 0;
    private boolean isStackof3Full = false;

    private void NewGiftAnimation(int giftId, String peerName, String peerProfilePic) {

        Log.e("animationCalled","InboxDetail "+"true");

        giftAnimRecycler.setVisibility(View.VISIBLE);
        if (!isStackof3Full) {
           // giftdataList.add(new GiftAnimData(getGiftResourceId(giftId), peerName, peerProfilePic));
            giftAnimationRecyclerAdapter.notifyItemInserted(incPos);
        } else {
           // giftdataList.set(incPos, new GiftAnimData(getGiftResourceId(giftId), peerName, peerProfilePic));
            giftAnimationRecyclerAdapter.notifyItemChanged(incPos);
        }

       Log.e("GiftListSize","InboxDetail "+giftdataList.size());

        if (incPos == 2) {
            incPos = 0;
            isStackof3Full = true;
            return;
        } else {
            incPos++;
            return;
        }


    }



    public BroadcastReceiver getMyMsgRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String peer_id = intent.getStringExtra("peerId");
            String msg = intent.getStringExtra("msg");

         //   Log.e("InboxDetail", "onReceive:11 "+"message_received" );

            if (peer_id.equals(peerId)) {
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    db.updateRead(peerId);
                    int countAll = db.getAllChatUnreadCount(peerId);
                    if (countAll > 0) {
                        unread.setVisibility(View.VISIBLE);
                        unread.setText(String.valueOf(countAll));
                    } else {
                        unread.setVisibility(View.INVISIBLE);
                    }

                    data.add(new ChatBean(peerId, jsonObject.get("Message").toString(), jsonObject.get("Time").toString(), "", "","TEXT"));
                    recyclerView.smoothScrollToPosition(data.size());
                    chatAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    };


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

   /* private void giftAnimation(int position) {
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        ((ImageView) findViewById(R.id.gift_imageShow)).setVisibility(View.VISIBLE);
        switch (position) {
            case 1:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.test1);
                break;
            case 2:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.test2);
                break;
            case 3:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.test3);
                break;
            case 4:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.test4);
                break;
            case 18:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.heart);
                break;
            case 21:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.lips);
                break;
            case 22:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.bunny);
                break;
            case 23:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.rose);
                break;
            case 24:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.boygirl);
                break;
            case 25:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.sandle);
                break;
            case 26:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.frock);
                break;
            case 27:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.car);
                break;
            case 28:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.ship);
                break;
            case 29:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.tajmahal);
                break;
            case 30:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.crown);
                break;
            case 31:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.bracket);
                break;
            case 32:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.diamondgift);
                break;
            case 33:
                ((ImageView) findViewById(R.id.gift_imageShow)).setImageResource(R.drawable.lovegift);
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
    }*/

    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void isError(String errorCode) {
        Toast.makeText(getApplicationContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.TRANSACTION_HISTORY_NEW) {
          /*  WallateResponceFemale rsp = (WallateResponceFemale) response;
            coins = Integer.parseInt(rsp.getResult().getCoinWithIncomeReport().getTotalCoins());*/

            IncomeReportFemale rsp = (IncomeReportFemale) response;
            try {

                if (rsp.getResult() != null) {
                    if (coins != 0) {
                        coins = rsp.getResult().getPoints();

                    } else {
                        coins = 0;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  Double D = Double.parseDouble(rsp.getResult().getAmountInr());
            //  coins = Integer.valueOf(D.intValue());

        }
    }



    private void storeStatus(String status) {
        SessionManager sessionManager = new SessionManager(this);
        chatRef = FirebaseDatabase.getInstance().getReference().child("Users");
        String uid = String.valueOf(sessionManager.getUserId());
        String name = sessionManager.getUserName();
        String fcmToken = sessionManager.getFcmToken();

        chatRef.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                Map<String, Object> map = null;

                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if (snapshot.exists()) {
                        map = (Map<String, Object>) snapshot.getValue();

                        HashMap<String, String> details = new HashMap<>();
                        details.put("uid", uid);
                        details.put("name", name);
                        details.put("status", status);
                        details.put("fcmToken", fcmToken);

                        chatRef.child(uid).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("storebusystatus", "stored");
                            }
                        });


                    }


                }


            }
        });


    }



}