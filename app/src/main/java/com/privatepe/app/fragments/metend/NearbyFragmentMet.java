package com.privatepe.app.fragments.metend;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.app.R;
import com.privatepe.app.Zego.VideoChatZegoActivityMet;
import com.privatepe.app.activity.MainActivity;
import com.privatepe.app.activity.setting.SettingsConfig;
import com.privatepe.app.activity.videoCall.VideoChatIMActivity;
import com.privatepe.app.adapter.OfferImageAdapter;
import com.privatepe.app.adapter.metend.HomeUserAdapterMet;
import com.privatepe.app.dialogs.InsufficientCoins;
import com.privatepe.app.response.metend.AdapterRes.UserListResponseMet;
import com.privatepe.app.response.metend.GenerateCallResponce.GenerateCallResponce;
import com.privatepe.app.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.PaginationAdapterCallback;
import com.privatepe.app.utils.PaginationScrollListener;
import com.privatepe.app.utils.SessionManager;
/*import com.tencent.qcloud.tuikit.TUICommonDefine;
import com.tencent.qcloud.tuikit.tuicallengine.TUICallDefine;
import com.tencent.qcloud.tuikit.tuicallkit.TUICallKit;*/

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragmentMet extends Fragment implements ApiResponseInterface, PaginationAdapterCallback {

    private String AppID;

    private AutoScrollViewPager offerBanner;
    OfferImageAdapter offerImageAdapter;

    RecyclerView userList;
    HomeUserAdapterMet homeUserAdapter;
    List<UserListResponseMet.Data> list;
    ApiManager apiManager;
    GridLayoutManager gridLayoutManager;

    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private TextView tv_popular, tv_newone;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ViewGroup viewGroup;

    AppLifecycle appLifecycle;
    private DatabaseReference chatRef;

  //  ZimManager zimManager;
    private InsufficientCoins insufficientCoins;
    private boolean isFreeCall = false;
    private ShimmerFrameLayout ShimmerCallLay;
    private Boolean isItForFirstTime = true;

    public NearbyFragmentMet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewGroup = view.findViewById(android.R.id.content);
        offerBanner = view.findViewById(R.id.offer_banner);
        userList = view.findViewById(R.id.user_list);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        userList.setLayoutManager(gridLayoutManager);

        tv_popular = view.findViewById(R.id.tv_popular);
        tv_newone = view.findViewById(R.id.tv_newone);
        ShimmerCallLay  =view.findViewById(R.id.ShimmerCallLay);

        apiManager = new ApiManager(getContext(), this);
        // apiManager.getPromotionBanner();

       // zimManager = ZimManager.sharedInstance();

        userList.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                showProgress();
                // mocking network delay for API call
                new Handler().postDelayed(() -> apiManager.getUserListWithLastCallLatestNextPage(String.valueOf(currentPage), ""), 500);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        // showProgress();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // mSwipeRefreshLayout.setRefreshing(false);
                currentPage = 1;
                isLastPage = false;
                list.clear();
                apiManager.getUserListWithLastCallLatest(String.valueOf(currentPage), "");
            }
        });

        //  markerForLang();
        checkFreeGift();

       /* tv_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_popular.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_popular.setTextSize(18);

                // Typeface typeface = getResources().getFont(R.font.lobster);
                //or to support all versions use
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_bold);
                tv_popular.setTypeface(typeface);

                tv_newone.setTextColor(getResources().getColor(R.color.black));
                tv_newone.setTextSize(14);

                typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
                tv_newone.setTypeface(typeface);

                apiManager.getPopularList(String.valueOf(currentPage), "");

            }
        });

        tv_newone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_newone.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_newone.setTextSize(18);

                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_bold);
                tv_newone.setTypeface(typeface);


                tv_popular.setTextColor(getResources().getColor(R.color.black));
                tv_popular.setTextSize(14);

                typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
                tv_popular.setTypeface(typeface);


            }
        });

        tv_newone.performClick();
*/
        showProgress();
       // apiManager.getUserListWithLastCallLatest(String.valueOf(currentPage), "");

        ((MainActivity) getActivity()).checkLocationSatae();

        appLifecycle = new AppLifecycle();
       /* try {
            AppID = getActivity().getString(R.string.app_id_rtm);
            // Initialize the RTM client
            mRtmClient = RtmClient.createInstance(getActivity(), AppID,
                    new RtmClientListener() {
                        @Override
                        public void onConnectionStateChanged(int state, int reason) {
                            String text = "Connection state changed to " + state + "Reason: " + reason + "\n";
                            *//*  writeToMessageHistory(text);*//*
                        }

                        @Override
                        public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String s) {
                            String text = "Connection state changed to " + s+ "\n";
                        }

                        @Override
                        public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {
                            String text = "Connection state changed to " + s+ "\n";
                        }

                        @Override
                        public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {
                        }

                        @Override
                        public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {
                        }

                        @Override
                        public void onTokenExpired() {
                        }

                        @Override
                        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {
                        }

                        @Override
                        public void onMessageReceived(RtmMessage rtmMessage, String peerId) {
                            //saveReceivedMsg(rtmMessage.getText(), peerId);

                        }
                    });

        } catch (Exception e) {
            throw new RuntimeException("RTM initialization failed!");
        }*/
       /* HashMap<String, String> user = new SessionManager(getContext()).getUserDetails();
        initializeRTM(user.get(PROFILE_ID));*/
        return view;
    }


    /* public void initializeRTM(String username*//*, String profile_id, String unique_id, String channel_name, String token, String call_type, String is_free_call*//*){
        mRtmClient.createMessage();

        mRtmClient.login(null, username, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                //sendMsg(username, profile_id, unique_id, channel_name, token, call_type, is_free_call);
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
            }
        });
    }

    private void sendMsg(String username, String profile_id, String unique_id, String channel_name, String token, String call_type, String is_free_call, String name, String image){
        final RtmMessage message = mRtmClient.createMessage();
        message.setText(username+","+profile_id+","+unique_id+","+channel_name+","+token+","+call_type+","+is_free_call+","+name+","+image);
        SendMessageOptions option = new SendMessageOptions();
        option.enableOfflineMessaging = true;

        mRtmClient.sendMessageToPeer(profile_id, message, option, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String text = message.getText();
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                String text = errorInfo.toString();
            }
        });
    }*/

    @Override
    public void onResume() {
        super.onResume();
        Log.e("testResume", "onResume NearbyFragment" );
        if(isItForFirstTime){
            apiManager.getUserListWithLastCallLatest(String.valueOf(currentPage), "");
            isItForFirstTime = false;
        }
    }

    private void checkFreeGift() {
        if (new SessionManager(getContext()).getGender().equals("male")) {
            //     apiManager.getRemainingGiftCardDisplayFunction();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            new InsufficientCoins(getContext(), 2, Integer.parseInt(callRate));
        } else {
            Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    //    lateinit var dialog1: Dialog
    private Dialog dialog1;

    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        try {
            /*

            if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {
                RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;

                try {
                    try {
                        success = rsp.getSuccess();
                        remGiftCard = rsp.getResult().getRemGiftCards();
                        freeSeconds = rsp.getResult().getFreeSeconds();
                        if (remGiftCard > 0) {
                            apiManager.searchUser(profileId, "1");
                            return;
                        }
                    } catch (Exception e) {
                    }

                    if (new SessionManager(getContext()).getUserWallet() >= 20) {
                        apiManager.searchUser(profileId, "1");
                    } else {
                        new InsufficientCoins(getContext(), 2, Integer.parseInt(callRate));
                    }
                } catch (Exception e) {
                    apiManager.searchUser(profileId, "1");
                }

            }
            if (ServiceCode == Constant.SEARCH_USER) {
                UserListResponse rsp = (UserListResponse) response;

                if (rsp != null) {
                    try {
                        int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                        int busyStatus = rsp.getResult().getData().get(0).getIs_busy();

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


                            }
                        } else if (onlineStatus == 1) {
                            Toast.makeText(getContext(), hostName + " is Busy", Toast.LENGTH_SHORT).show();

                        } else if (onlineStatus == 0) {
                            Toast.makeText(getContext(), hostName + " is Offline", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "User is Offline!", Toast.LENGTH_SHORT).show();
                        new SessionManager(getContext()).setOnlineState(0);
                        //     finish();
                    }
                }
            }
            if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
                ResultCall rsp = (ResultCall) response;
                //Log.e("newWalletValue", rsp.getResult().getPoints().getTotalPoint() + "");
                long walletBalance = rsp.getResult().getPoints().getTotalPoint();
                //int talkTime = walletBalance / userData.get(0).getCallRate() * 1000 * 60;
                long talkTime = walletBalance * 1000;
                //  int talkTime2 = userData.getCall_rate() * 1000 * 60;
                // Minus 2 sec to prevent balance goes into minus
                long canCallTill = talkTime - 2000;

                String profilePic = new SessionManager(getContext()).getUserProfilepic();
                HashMap<String, String> user = new SessionManager(getContext()).getUserDetails();
//                Intent intent = new Intent(getContext(), VideoChatActivity.class);

                Intent intent = new Intent();
                intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
                intent.putExtra("ID", profileId);
                intent.putExtra("UID", String.valueOf(userId));
                intent.putExtra("CALL_RATE", callRate);
                intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());



                if (remGiftCard > 0) {
                    int newFreeSec = Integer.parseInt(freeSeconds) * 1000;
                    intent.putExtra("AUTO_END_TIME", newFreeSec);
                    intent.putExtra("is_free_call", "true");
                } else {
                    intent.putExtra("AUTO_END_TIME", canCallTill);
                    intent.putExtra("is_free_call", "false");
                }
                intent.putExtra("receiver_name", hostName);
                intent.putExtra("converID", "convId");

                intent.putExtra("receiver_image", hostImage);
                //  startActivity(intent);

            }

*/


            if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {


                RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;


                Log.e("HomeFragment", "isSuccess: " + "  GET_REMAINING_GIFT_CARD api called");
                Log.e("HomeFragment", "isSuccess: success " + rsp.getSuccess());
                Log.e("HomeFragment", "isSuccess: remGiftCard " + rsp.getResult().getRemGiftCards());
                Log.e("HomeFragment", "isSuccess: freeSeconds " + rsp.getResult().getFreeSeconds());

                try {
                    try {
                        success = rsp.getSuccess();
                        remGiftCard = rsp.getResult().getRemGiftCards();
                        freeSeconds = rsp.getResult().getFreeSeconds();
                        if (remGiftCard > 0) {
                            apiManager.searchUser(profileId, "1");
                            return;
                        }
                    } catch (Exception e) {

                        Log.e("HomeFragment", "isSuccess: Exception " + e.getMessage());
                    }
                    String walletAmount = String.valueOf(new SessionManager(getContext()).getUserWallet());

                    Log.e("HomeFragment", "isSuccess: " + "  GET_REMAINING_GIFT_CARD api called middle");

                    Log.e("HomeFragment", "isSuccess: callRate " + callRate + "  totalCoins: " + new SessionManager(getContext()).getUserWallet());

                    if (new SessionManager(getContext()).getUserWallet() >= Integer.parseInt(callRate)) {
                        apiManager.searchUser(profileId, "1");
                    } else {
                        Log.e("insufficientCoinsDialog", "isSuccess: " + "insufficientCoinsDialog");
                        insufficientCoins = new InsufficientCoins(getContext(), 2, Integer.parseInt(callRate));

                        insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {

                                apiManager.checkFirstTimeRechargeDone();
                            }
                        });


                        // apiManager.searchUser(profileId, "1");
                    }
                } catch (Exception e) {
                    apiManager.searchUser(profileId, "1");
                }
            }
            if (ServiceCode == Constant.SEARCH_USER) {
                UserListResponseMet rsp = (UserListResponseMet) response;
                if (rsp != null) {
                    try {
                        //  int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                        //   Log.e(TAG, "isSuccess:  onlineStatus "+onlineStatus );
                        if (callType.equals("video")) {

                            chatRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(@NonNull DataSnapshot dataSnapshot) {
                                    Map<String, Object> map = null;
                                    if (dataSnapshot.exists()) {
                                        map = (Map<String, Object>) dataSnapshot.getValue();

                                        Log.e("HomeFragmentfirebase", "onSuccess: " + map.toString());

                                        if (map.get("status").equals("Online") || map.get("status").equals("Live")) {

                                            Log.e("HomeFragmentfirebase", "onDataChange: " + map.get("status").toString());

                                         /*   if (remGiftCard > 0) {
                                                apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                            } else {
                                                apiManager.generateCallRequest(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                            }*/


                                            if (remGiftCard > 0) {
                                                apiManager.generateCallRequestZ(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                            } else {
                                                apiManager.generateCallRequestZ(Integer.parseInt(profileId), String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                                        Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                            }



                                        } else if (map.get("status").equals("Busy")) {

                                            Toast.makeText(getContext(), "User is Busy", Toast.LENGTH_LONG).show();

                                            //  Log.e("HomeFragmentfirebase", "onDataChange: "+"Busy" );
                                            Log.e("HomeFragmentfirebase", "onDataChange: " + map.get("status").toString());
                                        } else if (map.get("status").equals("Offline")) {
                                            Toast.makeText(getContext(), "User is Offline", Toast.LENGTH_LONG).show();
                                            // Log.e("HomeFragmentfirebase", "onDataChange: "+"Offline" );
                                            Log.e("HomeFragmentfirebase", "onDataChange: " + map.get("status").toString());
                                        }
                                    } else {
                                        Log.e("HomeFragmentfirebase", "onSuccess: " + "does not exist");
                                    }
                                }
                            });

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
                            Toast.makeText(getContext(), hostName + " is Busy", Toast.LENGTH_SHORT).show();

                        } else if (onlineStatus == 0) {
                            Toast.makeText(getContext(), hostName + " is Offline", Toast.LENGTH_SHORT).show();
                        }*/


                    } catch (Exception e) {
                        Log.e("userbusycatch", "isSuccess: Exception " + e.getMessage());
                        Toast.makeText(getContext(), "User is Offline!", Toast.LENGTH_SHORT).show();

                        new SessionManager(getContext()).setOnlineState(0);
                        // finish();
                    }
                }
            }

            if (ServiceCode==Constant.NEW_GENERATE_AGORA_TOKENZ)
            {
                GenerateCallResponce rsp = (GenerateCallResponce) response;

                Log.e("NEW_GENERATE_AGORA_TOKENZ", "isSuccess: "+new Gson().toJson(rsp));
                startCall(profileId,rsp.getResult().getData().getUniqueId().toString());

              /*  int walletBalance = rsp.getResult().getPoints().getTotalPoint();
                int CallRateInt = Integer.parseInt(callRate);
                long talktime = (walletBalance / CallRateInt) * 1000L;
                long canCallTill = talktime - 2000;
                String profilePic = new SessionManager(getContext()).getUserProfilepic();
                HashMap<String, String> user = new SessionManager(getContext()).getUserDetails();
                Intent intent = new Intent(getContext(), VideoChatIMActivity.class);
                intent.putExtra("TOKEN", rsp.getResult().getData().getSenderChannelName().getToken().getToken());
                intent.putExtra("ID", profileId);
                intent.putExtra("UID", String.valueOf(userId));
                intent.putExtra("CALL_RATE", callRate);
                intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());

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
                intent.putExtra("receiver_name", hostName);
                intent.putExtra("converID", "convId");
                intent.putExtra("receiver_image", hostImage);
                intent.putExtra(Constant.ROOM_ID,  new SessionManager(getContext()).getUserId());
                intent.putExtra(Constant.USER_ID,  String.valueOf(userId));
                startActivity(intent);*/

            }











            /*if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
                ResultCall rsp = (ResultCall) response;

                //Log.e("newWalletValue", rsp.getResult().getPoints().getTotalPoint() + "");
                long walletBalance = rsp.getResult().getPoints().getTotalPoint();

                //int talkTime = walletBalance / Integer.parseInt(callRate) * 1000 * 60;

                // int talkTime2 = userData.getCall_rate() * 1000 * 60;
                // Minus 2 sec to prevent balance goes into minus

               *//*
                 if (remGiftCard>0){
                    walletBalance=70;
                }*//*

                // Log.e("CALL_RATE_TEST", "isSuccess: callRate "+callRate);

                int CallRateInt = Integer.parseInt(callRate);

                //AUTO_END_TIME converted to long

                long talktime = (walletBalance / CallRateInt) * 1000;

                Log.e("AUTO_CUT_TEST", "CallNotificationDialog: " + talktime);

                //  Log.e("CALL_RATE_TEST", "isSuccess: talktime HomeFrag "+talktime+" callrateint "+CallRateInt);

                long canCallTill = talktime - 2000;

                Log.e("AUTO_CUT_TEST", "onCreate: AUTO_END_TIME " + canCallTill);

               *//*
                int talkTime = walletBalance * 1000;
                int canCallTill = talkTime - 2000;
                *//*

                String profilePic = new SessionManager(getContext()).getUserProfilepic();

                HashMap<String, String> user = new SessionManager(getContext()).getUserDetails();
                Intent intent = new Intent(getContext(), VideoChatZegoActivity.class);
                intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
                intent.putExtra("ID", profileId);
                intent.putExtra("UID", String.valueOf(userId));
                intent.putExtra("CALL_RATE", callRate);
                intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());

                *//*
                appLifecycle.InitiateCall(String.valueOf(profileId), "Video Call", "");
                initiateMessageWithCall(String.valueOf(profileId), user.get(NAME), user.get(PROFILE_ID), rsp.getResult().getData().getUniqueId(), String.valueOf(false), profilePic, "video");
                *//*

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
                intent.putExtra("receiver_name", hostName);
                intent.putExtra("converID", "convId");
                intent.putExtra("receiver_image", hostImage);

                Log.e("HomeFragment", "isSuccess: guest name " + user.get(NAME));


                //AUTO_END_TIME converted to long
                String data = getMessageWithCall(String.valueOf(profileId), user.get(NAME),
                        user.get(PROFILE_ID), rsp.getResult().getData().getUniqueId(),
                        String.valueOf(isFreeCall), profilePic, "video", canCallTill);

                //2147483647
                Log.e("AUTO_CUT_TEST", "isSuccess: max value  " + canCallTill);


                //  Log.e(TAG, "isSuccess: profile id " + String.valueOf(profileId));
                //  Log.e(TAG, "isSuccess: data " + data);

                try {
                    Log.e("HomeFragment", "isSuccess: try start ");
               *//*     zimManager.callInvite(String.valueOf(profileId), data, (errorCode, errMsg) -> {
                        if (errorCode == ZIMErrorCode.SUCCESS) {
                            //start call successfully
                            Log.d("HomeFragment", "onZimCallback: send call successfully.");
                            startActivity(intent);
                        } else {
                            //start call failed
                            // Toast.makeText(getContext(),"start call failed  "+errorCode,Toast.LENGTH_SHORT).show();
                            Log.d("HomeFragment", "onZimCallback: tart call failed. " + errorCode);

                        }
                        Log.e("HomeFragment", "isSuccess: try end ");
                    });*//*
                } catch (Exception e) {
                    Log.e("HomeFragment", "isSuccess: Exception " + e.getMessage());
                }

            }*/


            if (ServiceCode == Constant.USER_LIST) {
                UserListResponseMet rsp = (UserListResponseMet) response;

                mSwipeRefreshLayout.setRefreshing(false);

                list = rsp.getResult().getData();
                TOTAL_PAGES = rsp.getResult().getLast_page();
                if (list.size() > 0) {
                    homeUserAdapter = new HomeUserAdapterMet(getActivity(), NearbyFragmentMet.this, "dashboard", NearbyFragmentMet.this);
                    // userList.setItemAnimator(new DefaultItemAnimator());
                    userList.setAdapter(homeUserAdapter);

                    // Shuffle Data

                    // Collections.shuffle(list);

                    // Set data in adapter
                    homeUserAdapter.addAll(list);

                    if (currentPage < TOTAL_PAGES) {
                        homeUserAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                    ShimmerCallLay.stopShimmer();
                    ShimmerCallLay.setVisibility(View.GONE);
                }

                consentReminder();
            }

            if (ServiceCode == Constant.USER_LIST_NEXT_PAGE) {
                UserListResponseMet rsp = (UserListResponseMet) response;

                mSwipeRefreshLayout.setRefreshing(false);

                homeUserAdapter.removeLoadingFooter();
                isLoading = false;

                List<UserListResponseMet.Data> results = rsp.getResult().getData();

                // Shuffle Data
                //  Collections.shuffle(results);

                list.addAll(results);


                homeUserAdapter.addAll(results);

                if (currentPage != TOTAL_PAGES) homeUserAdapter.addLoadingFooter();
                else isLastPage = true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void retryPageLoad() {
        //   apiManager.getUserListNextPage(String.valueOf(currentPage), "");
    }

    void resetPages() {
        // Reset Current page when refresh data
        this.currentPage = 1;
        this.isLastPage = false;
    }

    void consentReminder() {
        if (!new SessionManager(getContext()).getConsent()) {
            // new ConsentDialog(this);
        }
    }

    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private String callType = "", profileId = "", callRate = "", hostName = "", hostImage = "";
    private int userId;

    public void startVideoCall(String profileId, String callRate, int userId, String hostName, String hostImage) {
        Log.e("STARTVIDEOCALL_NEARBY", "startVideoCall: nearby " + userId);

        if (CheckPermission()) {
            callType = "video";
            this.profileId = profileId;
            this.callRate = callRate;
            this.userId = userId;
            this.hostName = hostName;
            this.hostImage = hostImage;
            Log.e("startCallRR", "startVideoCall: userid " + userId + " profileid " + profileId);
            Log.e("ProfileIdTestFB", "HomeFragment startVideoCall: " + profileId);
            chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(profileId);
            apiManager.getRemainingGiftCardFunction();
        }
    }

   // private TUICallDefine.MediaType mMediaType = TUICallDefine.MediaType.Video;

    private void startCall(String userId,String uniqueId) {
    /*    TUICallDefine.CallParams callParams = new TUICallDefine.CallParams();
        callParams.timeout = 30;
        callParams.userData = userId;
        TUICommonDefine.RoomId roomId = new TUICommonDefine.RoomId();
        // roomId.intRoomId = userId;
        roomId.strRoomId = uniqueId;
        callParams.roomId = roomId;
        //TUICallDefine.CallParams callParams = createCallParams();
        Log.e("chadkd","Yes1");
        if (callParams == null) {
            TUICallKit.createInstance(getContext()).call(userId, mMediaType);
        } else {
            TUICallKit.createInstance(getContext()).call(userId, mMediaType, callParams, null);
        }*/
    }
   /* private TUICallDefine.CallParams createCallParams() {
        try {
            if (SettingsConfig.callTimeOut != 30 || !TextUtils.isEmpty(SettingsConfig.userData) || !TextUtils.isEmpty(SettingsConfig.offlineParams)
                    || SettingsConfig.intRoomId != 0 || !TextUtils.isEmpty(SettingsConfig.strRoomId)) {
                Log.e("chadkd","Yes2");

                TUICallDefine.CallParams callParams = new TUICallDefine.CallParams();
                callParams.timeout = SettingsConfig.callTimeOut;
                callParams.userData = SettingsConfig.userData;
                if (!TextUtils.isEmpty(SettingsConfig.offlineParams)) {
                    callParams.offlinePushInfo = new Gson().fromJson(SettingsConfig.offlineParams, TUICallDefine.OfflinePushInfo.class);
                }
                if (SettingsConfig.intRoomId != 0 || !TextUtils.isEmpty(SettingsConfig.strRoomId)) {
                    TUICommonDefine.RoomId roomId = new TUICommonDefine.RoomId();
                    roomId.intRoomId = SettingsConfig.intRoomId;
                    roomId.strRoomId = SettingsConfig.strRoomId;
                    callParams.roomId = roomId;
                }
                return callParams;
            }

        } catch (Exception e) {
        }
        return null;
    }*/
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


        Dexter.withActivity(getActivity()).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("onPermissionsChecked", "onPermissionsChecked: ");

                if (report.areAllPermissionsGranted()) {
                    Log.e("onPermissionsChecked", "all permission granted");
                    isPermissionGranted[0] = true;
                } else {
                    isPermissionGranted[0] = false;
                    Toast.makeText(getContext(), "To use this feature Camera and Audio permissions are must.You need to allow the permissions", Toast.LENGTH_SHORT).show();
                }


              /*        if (report.getDeniedPermissionResponses().size() + report.getGrantedPermissionResponses().size() == 3) {
                    Log.e("onPermissionsChecked", ""+report.getDeniedPermissionResponses().size()+ "    "+report.getGrantedPermissionResponses().size() );

                    if (report.isAnyPermissionPermanentlyDenied()) {
                        isPermissionGranted[0] = false;
                        Log.e("onPermissionsChecked", "all permission denied");


                    }
                }*/
              /*  else {
                    isPermissionGranted[0] = false;

                    Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }*/


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                Log.e("onPermissionsChecked", "onPermissionRationaleShouldBeShown");
                token.continuePermissionRequest();

            }


        }).onSameThread().check();

        return isPermissionGranted[0];
    }


    private String getMessageWithCall(String receiverId, String userName, String userId, String uniqueId, String isFreeCall, String profilePic, String callType, long canCallTill) {
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
            messageObject.put("isMessageWithCall", "yes");
            messageObject.put("CallMessageBody", OtherInfoWithCall.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String msg = messageObject.toString();
        return msg;
    }


}
