package com.klive.app.fragments;


import static com.klive.app.main.Home.cardView;
import static com.klive.app.main.Home.unread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.klive.app.Inbox.ChatAdapter;
import com.klive.app.Inbox.ChatBean;
import com.klive.app.Inbox.InboxDetails;
import com.klive.app.Inbox.UserListAdapter;
import com.klive.app.Inbox.UserModal;
import com.klive.app.Interface.ItemClick;
import com.klive.app.R;
import com.klive.app.ZegoExpress.zim.UserInfo;
import com.klive.app.activity.AgencyPolicy;
import com.klive.app.activity.SystemMsg;
import com.klive.app.adapter.AgencyListAdapter;
import com.klive.app.adapter.BannerAdapter;
import com.klive.app.response.Agency.AgencyPolicyResponse;
import com.klive.app.response.Banner.BannerResponse;
import com.klive.app.response.Banner.BannerResult;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.sqlite.Chat;
import com.klive.app.sqlite.ChatDB;
import com.klive.app.sqlite.SystemDB;
import com.klive.app.utils.Constant;
import com.klive.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MsgFragment extends Fragment implements ItemClick, ApiResponseInterface {
    private List<UserModal> data;
    public static RecyclerView recyclerView;
    public static UserListAdapter userListAdapter;
    LinearLayoutManager layoutManager;
    private boolean isRecieverRegister = false;
    private final int contactLoadLimit = 10;
    private ApiManager apiManager;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private ProgressBar progressLoader;
    private TextView Systemtv;
    private TextView systemMessageCounter;
    private TextView systemMessageDate;
    private BannerAdapter bannerAdapter;
    private Timer timer;
    private ImageView[] dots;
    ViewPager viewPager;
    LinearLayout dots_layout_lab;

    private List<BannerResult> bannerList = new ArrayList<>();

    public MsgFragment() {
        // require a empty public constructor
    }

    private CardView fixId;
    ChatDB db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.msg_fragment, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        cardView.setVisibility(View.VISIBLE);
        apiManager = new ApiManager(getContext(), this);
        apiManager.getBannerList("2");
        Log.e("CreatedFragment", "onCreateView: " + "MsgFragment");
        //new msgDialog(getActivity());
        data = new ArrayList<>();
        db = new ChatDB(getContext());
        init(v);
        return v;
    }

    private void init(View view) {
        cardView = view.findViewById(R.id.fixId);
        Systemtv = view.findViewById(R.id.msg);
        systemMessageCounter = view.findViewById(R.id.systemMessageCounter);
        progressLoader = view.findViewById(R.id.loader);
        systemMessageDate = view.findViewById(R.id.system_message_date);
        viewPager = view.findViewById(R.id.viewpager_lab);
        dots_layout_lab = view.findViewById(R.id.dots_layout_lab);
        SystemDB systemDB = new SystemDB((getContext()));
        List<Chat> lastSystem = systemDB.getLastChat("System");
        if (lastSystem.size() > 0) {
            Systemtv.setText(lastSystem.get(0).get_text_get());
            if (!lastSystem.get(0).get_time_get().equals("")) {
                systemMessageDate.setText(lastSystem.get(0).get_time_get());
            }

        }

        if (bannerList != null) {
            setBannerData();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    SystemDB ss = new SystemDB(getContext());
                    int systemMessageCount = ss.getTotalSystemUnreadCount();
                    if (systemMessageCount > 0) {
                        Log.e("ssssss", "onCreate: systemMessageCount000 " + systemMessageCount);
                        systemMessageCounter.setVisibility(View.VISIBLE);
                        systemMessageCounter.setText("" + systemMessageCount);
                        Log.e("ssssss", "onResume: systemMessageCounter000 " + systemMessageCounter.getText().toString());
                    } else {
                        systemMessageCounter.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("ssssss", "run: " + e.getMessage());
                }

            }
        }, 10);


        recyclerView = view.findViewById(R.id.userRecycler);
        //ChatDB db = new ChatDB(getContext());
        // List<Chat> peers = db.getAllPeer();

       /*        if (peers.size() > 0) {


            int countAll = 0;
            countAll = countAll + db.getAllChatUnreadCount(peers.get(0).get_id());
            // Log.i("Counter",""+countAll);


            if (countAll > 0) {
                unread.setVisibility(View.VISIBLE);
                unread.setText(String.valueOf(countAll));
            } else {
                unread.setVisibility(View.INVISIBLE);
            }


            for (Chat cn : peers) {

                //   Log.i("dd", "" + peers.size());

                List<Chat> chats = db.getLastChat(peers.get(0).get_id());
                int count = db.getChatUnreadCount(peers.get(0).get_id());
                data.add(new UserModal(chats.get(0).get_id(), chats.get(0).get_name(), chats.get(0).get_text_get() + chats.get(0).get_text_sent(), chats.get(0).get_time_get() + chats.get(0).get_time_sent(), chats.get(0).get_image(), count, chats.get(0).get_chatType()));

            }
        }*/

        // Toast.makeText(getContext(),""+data.get(0).getPeer_id()+"   "+data.get(0).getPeer_name(),Toast.LENGTH_LONG).show();

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        userListAdapter = new UserListAdapter(getContext(), data, this);
        recyclerView.setAdapter(userListAdapter);
        userListAdapter.notifyDataSetChanged();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SystemMsg.class));
            }
        });


        initScrollListener();
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
            dots[i] = new ImageView(getActivity());
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

    private void initScrollListener() {
        Log.e("MsgFragment1", "initScrollListener: ");
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) //check for scroll down
                {
                    Log.e("MsgFragment1", "onScrolled: " + "scroll down");
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();


                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            progressLoader.setVisibility(View.VISIBLE);
                            loading = false;

                            try {

                                List<Chat> peers = db.getAllPeerByLimit(data.size(), contactLoadLimit);
                                Log.e("UnreadCount222", "MSGF onResume  " + "PeerSize " + peers.size());


                                ChatDB db = new ChatDB(getContext());
                                SystemDB SystemDb = new SystemDB(getContext());

                                int countAll = 0;
                                countAll = countAll + db.getAllChatUnreadCount("");
                                OtherTotalCount = countAll + SystemDb.getTotalSystemUnreadCount();

                                Log.e("OtherTotalCount", "onReceive: OtherTotalCount  " + OtherTotalCount);


                                if (OtherTotalCount > 0) {
                                    unread.setVisibility(View.VISIBLE);
                                    unread.setText(String.valueOf(OtherTotalCount));
                                } else {
                                    unread.setVisibility(View.INVISIBLE);
                                }


                                if (peers.size() > 0) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressLoader.setVisibility(View.GONE);
                                        }
                                    }, 100);

/*
                                    int countAll = 0;

                                    countAll = countAll + db.getAllChatUnreadCount(peers.get(0).get_id());
                                    if (countAll > 0) {
                                        unread.setVisibility(View.VISIBLE);
                                        unread.setText(String.valueOf(countAll));
                                        Log.e("UnreadCount", "MSGF onResume  " + "PeerSize " + peers.size() + "MsgCount " + countAll);
                                    } else {
                                        unread.setVisibility(View.INVISIBLE);
                                    }*/

                                    for (Chat cn : peers) {

                                        List<Chat> chats = db.getLastChat(cn.get_id());
                                        int count = db.getChatUnreadCount(cn.get_id());

                                        Log.e("MessageFragment", "onResume: getChatUnreadCount " + count);

                                        data.add(new UserModal(chats.get(0).get_id(), chats.get(0).get_name(), chats.get(0).get_text_get() + chats.get(0).get_text_sent(), chats.get(0).get_time_get() + chats.get(0).get_time_sent(), chats.get(0).get_image(), count, chats.get(0).get_chatType()));
                                    }

                                    recyclerView.getAdapter().notifyDataSetChanged();

                                } else {
                                    progressLoader.setVisibility(View.GONE);
                                }


                            } catch (Exception e) {
                                Log.e("MsgFrag", "onResume: Exception " + e.getMessage());

                            }

                            //Toast.makeText(getActivity(), "loading", Toast.LENGTH_LONG).show();
                            loading = true;


                        }
                    }
                } else {

                }
            }
        });


    }

    public void onClick(String peerId, String peerName, String peerImage) {
        db.updateRead(peerId);
        startActivity(new Intent(getContext(), InboxDetails.class).putExtra("peerId", peerId).putExtra("peerName", peerName).putExtra("peerImage", peerImage));
        Log.e("SystemPeerId", peerId);
    }

    public void onResume() {
        super.onResume();

        //registerReceiver(getMyMsgRec, new IntentFilter("USER-TEXT"));
        //refreshDataList();
        //registerRe(getRecMsg, new IntentFilter("MSG-UPDATE"));


        try {
            SystemDB systemDB = new SystemDB((getContext()));
            List<Chat> lastSystem = systemDB.getLastChat("System");
            if (lastSystem.size() > 0) {
                Systemtv.setText(lastSystem.get(0).get_text_get());
                if (!lastSystem.get(0).get_time_get().equals("")) {
                    systemMessageDate.setText(lastSystem.get(0).get_time_get());
                }
            }


            // Log.e("sssss", "onResume: systemMessageCount "+systemMessageCount );
            //  new SessionManager(getContext()).setSystemMessageCounter(systemMessageCount);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        SystemDB ss = new SystemDB(getContext());
                        int systemMessageCount = ss.getTotalSystemUnreadCount();
                        if (systemMessageCount > 0) {
                            Log.e("ssssss", "onCreate: systemMessageCount111 " + systemMessageCount);
                            systemMessageCounter.setVisibility(View.VISIBLE);
                            systemMessageCounter.setText("" + systemMessageCount);
                            Log.e("ssssss", "onResume: systemMessageCounter111 " + systemMessageCounter.getText().toString());
                        } else {
                            systemMessageCounter.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception e) {
                        Log.e("ssssss", "run:11 " + e.getMessage());
                    }

                }
            }, 10);

        } catch (Exception e) {
            Log.e("sssss", "onResume: Exception " + e.getMessage());
        }


        data.clear();

        try {
            // List<Chat> peers = db.getAllPeer();
            ChatDB db = new ChatDB(getContext());
            SystemDB SystemDb = new SystemDB(getContext());

            int countAll = 0;
            countAll = countAll + db.getAllChatUnreadCount("");
            OtherTotalCount = countAll + SystemDb.getTotalSystemUnreadCount();

            Log.e("OtherTotalCount", "onReceive: OtherTotalCount  " + OtherTotalCount);


            if (OtherTotalCount > 0) {
                unread.setVisibility(View.VISIBLE);
                unread.setText(String.valueOf(OtherTotalCount));
            } else {
                unread.setVisibility(View.INVISIBLE);
            }


            List<Chat> peers = db.getAllPeerByLimit(0, contactLoadLimit);

            Log.e("UnreadCount", "MSGF onResume  " + "PeerSize " + peers.size());

            if (peers.size() > 0) {
              /*       int countAll = 0;

                countAll = countAll + db.getAllChatUnreadCount(peers.get(0).get_id());
                if (countAll > 0) {
                    unread.setVisibility(View.VISIBLE);
                    unread.setText(String.valueOf(countAll));
                    Log.e("UnreadCount", "MSGF onResume  " + "PeerSize " + peers.size() + "MsgCount " + countAll);
                } else {
                    unread.setVisibility(View.INVISIBLE);
                }*/

                for (Chat cn : peers) {

                    List<Chat> chats = db.getLastChat(cn.get_id());
                    int count = db.getChatUnreadCount(cn.get_id());

                    Log.e("MessageFragment", "onResume: getChatUnreadCount " + count);

                    data.add(new UserModal(chats.get(0).get_id(), chats.get(0).get_name(), chats.get(0).get_text_get() + chats.get(0).get_text_sent(), chats.get(0).get_time_get() + chats.get(0).get_time_sent(), chats.get(0).get_image(), count, chats.get(0).get_chatType()));
                }
            }
        } catch (Exception e) {
            Log.e("MsgFrag", "onResume: Exception " + e.getMessage());
        }


        userListAdapter.notifyDataSetChanged();
        requireActivity().registerReceiver(getRecMsg, new IntentFilter("MSG-UPDATE"));
        requireActivity().registerReceiver(getRecMsg, new IntentFilter("MSG-UPDATE-SENT"));
        requireActivity().registerReceiver(gotoSysInbox, new IntentFilter("OPEN-SYSTEM-INBOX"));
        isRecieverRegister = true;
    }

    private void refreshDataList() {
        if (data == null) {
            data = new ArrayList<>();
        } else {
            data.clear();
        }
        ChatDB db = new ChatDB(getContext());
        List<Chat> peers = db.getAllPeer();
        if (peers.size() > 0) {
            for (Chat cn : peers) {
                List<Chat> chats = db.getLastChat(cn.get_id());
                int count = db.getChatUnreadCount(cn.get_id());
                data.add(new UserModal(chats.get(0).get_id(), chats.get(0).get_name(), chats.get(0).get_text_get() + chats.get(0).get_text_sent(), chats.get(0).get_time_get() + chats.get(0).get_time_sent(), chats.get(0).get_image(), count, chats.get(0).get_chatType()));
            }
        }
        userListAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        super.onDestroy();

        try {
            if (getRecMsg != null && isRecieverRegister) {
                requireActivity().unregisterReceiver(getRecMsg);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Log.i("msgFrag", "Destroyed");


    }


    private int OtherTotalCount = 0;
    public BroadcastReceiver getRecMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String peer_id = intent.getStringExtra("peerId");
            String msg = intent.getStringExtra("msg");


            try {
                if (msg.equals("receive")) {


                    //  Log.e("recievemsggg","called");


                    if (data == null) {
                        data = new ArrayList<>();
                    } else {
                        // data.clear();
                    }
                    ChatDB db = new ChatDB(getContext());
                    SystemDB SystemDb = new SystemDB(getContext());
                    // List<Chat> peers = db.getAllPeer();


                    int countAll = 0;
                    countAll = countAll + db.getAllChatUnreadCount("");
                    OtherTotalCount = countAll + SystemDb.getTotalSystemUnreadCount();

                    Log.e("OtherTotalCount", "onReceive: OtherTotalCount  " + OtherTotalCount);


                    if (OtherTotalCount > 0) {
                        unread.setVisibility(View.VISIBLE);
                        unread.setText(String.valueOf(OtherTotalCount));
                    } else {
                        unread.setVisibility(View.INVISIBLE);
                    }


                    List<Chat> peers = db.getAllPeerByLimit(0, contactLoadLimit);

                    if (peers.size() > 0) {


                        Log.e("MessageFragment", "onReceive: total msg " + db.getAllChatUnreadCount(""));


                       /*   Log.e("MsgFragment","total peers "+peers.size());
                    for (int j=0;j<peers.size();j++)
                    {
                        Log.e("MsgFragment"," peers id "+peers.get(j).get_id()+" peers name  "+peers.get(j).get_id());

                    }*/


                        try {
                            for (int i = 0; i < peers.size(); i++) {
                                //  if (peer_id.equals(peers.get(i).get_id())) {
                                List<Chat> chats = db.getLastChat(peers.get(i).get_id());
                                int count = db.getChatUnreadCount(peers.get(i).get_id());
                                Log.e("MessageFragment", "onReceive: getChatUnreadCount " + count);
                                //     Log.e("MsgFragment", "onReceive: chat ids "+peers.get(i).get_id()+" chat name  "+peers.get(i).get_name() );
                                Log.e("MsgFragment", "onReceive: chat size " + chats.size());
                                //     Log.e("MsgFragment", "onReceive: ->  chat list "+new Gson().toJson(chats.get(0)) );
                                Log.e("MsgFragment", "onReceive: unread count " + count + "  \n\n ");


                                if (data.size() > 0) {
                                    //  Log.e("MsgFragment", "onReceive: 0  ->  data size "+data.size() );
                                    try {
                                        data.set(i, new UserModal(chats.get(0).get_id(),
                                                chats.get(0).get_name(), chats.get(0).get_text_get() + chats.get(0).get_text_sent(),
                                                chats.get(0).get_time_get() + chats.get(0).get_time_sent(), chats.get(0).get_image(),
                                                count, chats.get(0).get_chatType()));
                                    } catch (Exception e) {

                                        Log.e("MsgFragment", "onReceive: data.size>0 Exception" + e.getMessage());

                                    }
                                } else {
                                    // Log.e("MsgFragment", "onReceive: 1  ->  data size "+data.size() );
                                    try {
                                        data.add(new UserModal(chats.get(0).get_id(),
                                                chats.get(0).get_name(), chats.get(0).get_text_get() + chats.get(0).get_text_sent(),
                                                chats.get(0).get_time_get() + chats.get(0).get_time_sent(), chats.get(0).get_image(),
                                                count, chats.get(0).get_chatType()));
                                    } catch (Exception e) {
                                        Log.e("MsgFragment", "onReceive: data.size<=0 Exception" + e.getMessage());
                                    }

                                }

                                //Log.e("MsgFragment", "onReceive: data"+new Gson().toJson(data.get(i)));
                                //  }

                            }
                        } catch (Exception e) {
                            Log.e("MsgFragment", "onReceive: Exception" + e.getMessage());
                        }


                        //   Log.e("recievemsggg","called");
                    }
                    userListAdapter.notifyDataSetChanged();


                    try {
                        SystemDB systemDB = new SystemDB((getContext()));
                        List<Chat> lastSystem = systemDB.getLastChat("System");
                        if (lastSystem.size() > 0) {
                            Systemtv.setText(lastSystem.get(0).get_text_get());
                            systemMessageDate.setText(lastSystem.get(0).get_time_get());
                        }

                        int systemMessageCount = systemDB.getTotalSystemUnreadCount();

                        //    new SessionManager(getContext()).setSystemMessageCounter(systemMessageCount);

                        if (systemMessageCount > 0) {
                            systemMessageCounter.setVisibility(View.VISIBLE);
                            systemMessageCounter.setText("" + systemMessageCount);
                        } else {
                            systemMessageCounter.setVisibility(View.INVISIBLE);
                        }


                    /*    ChatDB db1 = new ChatDB(getContext());
                        int countMessage=db1.getAllChatUnreadCount("")+systemMessageCount;
                        if (countMessage > 0) {
                            unread.setVisibility(View.VISIBLE);
                            unread.setText(String.valueOf(countMessage));
                        } else {
                            unread.setVisibility(View.INVISIBLE);
                        }



                        if (systemMessageCount>0)
                        {
                            systemMessageCounter.setVisibility(View.VISIBLE);
                            systemMessageCounter.setText(""+systemMessageCount);
                        }
                        else {
                            systemMessageCounter.setVisibility(View.INVISIBLE);
                        }


                        ChatDB db1 = new ChatDB(getContext());
                        int countMessage=db1.getAllChatUnreadCount("")+systemMessageCount;
                        if (countMessage > 0) {
                            unread.setVisibility(View.VISIBLE);
                            unread.setText(String.valueOf(countMessage));
                        } else {
                            unread.setVisibility(View.INVISIBLE);
                        }

                        */


                    } catch (Exception e) {

                    }


                }
                if (msg.equals("sent")) {

                    Log.e("MsgFragment", "onReceive: " + "sent \n\n" + " ");

                    if (data == null) {
                        data = new ArrayList<>();
                    } else {
                        data.clear();
                    }
                    ChatDB db = new ChatDB(getContext());
                    //  List<Chat> peers = db.getAllPeer();


                    List<Chat> peers = db.getAllPeerByLimit(0, contactLoadLimit);
                    if (peers.size() > 0) {
                        for (Chat cn : peers) {
                        /*
                        int countAll = db.getAllChatUnreadCount(cn.get_id());
                        if(countAll > 0){
                            unread.setVisibility(View.VISIBLE);
                            unread.setText(String.valueOf(countAll));
                        }else{
                            unread.setVisibility(View.INVISIBLE);
                        }*/
                            List<Chat> chats = db.getLastChat(cn.get_id());
                            int count = db.getChatUnreadCount(cn.get_id());
                            data.add(new UserModal(chats.get(0).get_id(), chats.get(0).get_name(), chats.get(0).get_text_get() + chats.get(0).get_text_sent(), chats.get(0).get_time_get() + chats.get(0).get_time_sent(), chats.get(0).get_image(), count, chats.get(0).get_chatType()));
                        }
                    }
                    userListAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Log.e("MsgFrag", "onReceive: Exception " + e.getMessage());
            }


        }
    };


    public BroadcastReceiver gotoSysInbox = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MsgFragment1", "onReceive: MsgFrag " + intent.getStringExtra("action"));

            String action = intent.getStringExtra("action");

            if (action.equals("goto_system_inbox")) {

                startActivity(new Intent(getContext(), SystemMsg.class));

            }

        }
    };

   /* public BroadcastReceiver getMyMsgRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String peer_id = intent.getStringExtra("peerId");
            String msg = intent.getStringExtra("msg");
            if(peer_id.equals(peerId)) {
                String[] rec_data = msg.split(":rtm:");
                data.add(new ChatBean(peerId,  rec_data[1], rec_data[3], "", ""));
                chatAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(data.size());
            }
        }
    };*/


}