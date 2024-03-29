package com.privatepe.app.activity;

import static com.privatepe.app.utils.Constant.GET_FIRST_TIME_RECHARGE_LIST;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayoutMediator;
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
import com.privatepe.app.Inbox.InboxDetails;
import com.privatepe.app.R;
import com.privatepe.app.Zego.VideoChatZegoActivityMet;
import com.privatepe.app.adapter.RateCountDisplayAdapter;
import com.privatepe.app.adapter.VideoStatusAdapter;
import com.privatepe.app.adapter.metend.AlbumAdapterViewProfileMet;
import com.privatepe.app.adapter.metend.GiftCountDisplayAdapterMet;
import com.privatepe.app.adapter.metend.ProfilePagerAdapterMet;
import com.privatepe.app.databinding.ActivityViewProfileBinding;
import com.privatepe.app.databinding.ActivityViewProfileMetBinding;
import com.privatepe.app.dialogs.InsufficientCoins;
import com.privatepe.app.dialogs.ReportDialog;
import com.privatepe.app.response.metend.AdapterRes.UserListResponseMet;
import com.privatepe.app.response.metend.DisplayGiftCount.GiftCountResult;
import com.privatepe.app.response.metend.DisplayGiftCount.Result;
import com.privatepe.app.model.UserListResponseNew.UserListResponseNewData;
import com.privatepe.app.response.ProfileVideoResponse;
import com.privatepe.app.response.metend.AddRemoveFavResponse;
import com.privatepe.app.response.metend.DiscountedRecharge.DiscountedRechargeResponse;
import com.privatepe.app.response.metend.FirstTimeRechargeListResponse;
import com.privatepe.app.response.metend.GenerateCallResponce.GenerateCallResponce;
import com.privatepe.app.response.metend.RechargePlan.RechargePlanResponseNew;
import com.privatepe.app.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.privatepe.app.response.metend.UserListResponseNew.FemaleImage;
import com.privatepe.app.response.metend.UserListResponseNew.ResultDataNewProfile;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.status_videos.ActivityStatus;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.NetworkCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.privatepe.app.model.UserListResponseNew.GetRatingTag;
import com.privatepe.app.utils.SessionManager;
import com.privatepe.app.response.metend.UserListResponseNew.GiftDetails;

public class ViewProfileMet  extends BaseActivity implements ApiResponseInterface {

    int isFavourite = 0;
    ApiManager apiManager;
    int userId, callRate;
    int hostId;
    ActivityViewProfileMetBinding binding;
    ArrayList<ResultDataNewProfile> userData = new ArrayList<>();

    long walletBalance;
    private NetworkCheck networkCheck;
    private String convId = "";
    //giftcount
    RecyclerView rv_giftshow;
    GiftCountDisplayAdapterMet giftCountDisplayAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<GiftDetails> giftDetailsArrayList;
    ArrayList<Result> resultArrayList;
    //show rating and tag count 6/5/21
    RecyclerView rv_tagshow;
    RateCountDisplayAdapter rateCountDisplayAdapter;
    LinearLayoutManager linearLayoutManagerRating;
    GridLayoutManager gridLayoutManager;

    ArrayList<GetRatingTag> ratingArrayList;

    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;
    // int unique_id ;
    String hostIdFemale, hostProfileID, hostLevel;

    RecyclerView rv_albumShow;
    AlbumAdapterViewProfileMet adapter_album;
    AppLifecycle appLifecycle;
    // private ZimManager zimManager;

    private boolean isFreeCall = false;
    private ArrayList<ProfileVideoResponse> videostatusList = new ArrayList<>();
    private VideoStatusAdapter videoStatusDisplayAdapter;
    private RecyclerView rv_videostatus;
    private String dp;

    DatabaseReference firebaseref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_profile_met);


        binding.setClickListener(new EventHandler(this));
        networkCheck = new NetworkCheck();
        // zimManager = ZimManager.sharedInstance();
        init();
        // Getting all permissions before going to make a call
        getPermission();
        // createChatRoom();
    }


    private void createChatRoom() {
    /*    if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
            RequestChatRoom requestChatRoom = new RequestChatRoom("FSAfsafsdf",
                    Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                    new SessionManager(getApplicationContext()).getUserName(),
                    "ProfilePhoto", "1", userData.getProfile_id(),
                    userData.getName(), userData.getProfile_images().get(0).getImage_name(),
                    "2", 0, callRate, 0, 20, "",
                    "countrtStstic", String.valueOf(userId));

            Call<ResultChatRoom> chatRoomCall = apiservice.createChatRoom("application/json", requestChatRoom);

            chatRoomCall.enqueue(new Callback<ResultChatRoom>() {
                @Override
                public void onResponse(Call<ResultChatRoom> call, Response<ResultChatRoom> response) {
                    // Log.e("onResponseRoom: ", new Gson().toJson(response.body()));
                    try {
                        if (!response.body().getData().getId().equals("")) {
                            convId = response.body().getData().getId();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResultChatRoom> call, Throwable t) {
                    // Log.e("onResponseChatRoom: ", t.getMessage());
                }
            });

        }*/
    }

    void init() {
        appLifecycle = new AppLifecycle();
        apiManager = new ApiManager(this, this);

        rv_albumShow = findViewById(R.id.rv_albumShow);

        // collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        binding.collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.transparentBlack));
        binding.collapsingToolbar.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));

        //userData = (UserListResponse.Data) getIntent().getSerializableExtra("user_data");

        hostIdFemale = String.valueOf(getIntent().getSerializableExtra("id"));
        hostProfileID = String.valueOf(getIntent().getSerializableExtra("profileId"));
//        hostLevel = String.valueOf(getIntent().getSerializableExtra("level"));


        /*if (hostLevel.equals("0")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_0);
        } else if (hostLevel.equals("1")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_1);
        } else if (hostLevel.equals("2")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_2);
        } else if (hostLevel.equals("3")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_3);
        } else if (hostLevel.equals("4")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_4);
        } else if (hostLevel.equals("5")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_5);
        } else if (hostLevel.equals("6")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_6);
        } else if (hostLevel.equals("7")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_7);
        } else if (hostLevel.equals("8")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_8);
        } else if (hostLevel.equals("9")) {
            binding.imgLevel.setBackgroundResource(R.mipmap.icon_level_9);
        }*/

        rv_giftshow = findViewById(R.id.rv_giftshow);
        rv_videostatus = findViewById(R.id.rv_videoShow);
        //rating recyclerview for show rating
        rv_tagshow = findViewById(R.id.rv_rateShow);


        giftDetailsArrayList = new ArrayList<>();
        resultArrayList = new ArrayList<>();
        //array list inisilise object for
        ratingArrayList = new ArrayList<>();

        giftCountDisplayAdapter = new GiftCountDisplayAdapterMet(getApplicationContext(), giftDetailsArrayList, resultArrayList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_giftshow.setLayoutManager(new GridLayoutManager(this, 5));
        rv_giftshow.setAdapter(giftCountDisplayAdapter);


        binding.userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String[] copyString = binding.userId.getText().toString().split(":");
                ClipData clip = ClipData.newPlainText("id", copyString[1].trim());
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(ViewProfileMet.this, "Copied", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });


        //rate value set adapter here 6/5/21
        /*int numberOfColumns = 3;
        rv_tagshow.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        rateCountDisplayAdapter = new RateCountDisplayAdapter(this, ratingArrayList);
        rv_tagshow.setAdapter(rateCountDisplayAdapter);*/


        /*
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        rv_tagshow.setLayoutManager(staggeredGridLayoutManager);
        rateCountDisplayAdapter = new RateCountDisplayAdapter(this);
        rv_tagshow.setAdapter(rateCountDisplayAdapter);
        */

        FlexboxLayoutManager flexLayout = new FlexboxLayoutManager(ViewProfileMet.this);
        rv_tagshow.setLayoutManager(flexLayout);
        rateCountDisplayAdapter = new RateCountDisplayAdapter(ViewProfileMet.this, ratingArrayList);
        rv_tagshow.setAdapter(rateCountDisplayAdapter);

        //hide code here 11/5/21
        //  binding.setResponse(userData);
        //  isFavourite = userData.getFavorite_by_you_count();

        /*String[] dob = userData.getDob().split("-");
        int date = Integer.parseInt(dob[0]);
        int month = Integer.parseInt(dob[1]);
        int year = Integer.parseInt(dob[2]);
        binding.tvAge.setText("Age: " + getAge(year, month, date));*/
        //hide code here 19/5/21

        // setOnlineStatus();

       /* userId = userData.getId();
        hostId = userData.getProfile_id();

        apiManager.getVideoForProfile(String.valueOf(userId));

        apiManager.getGiftCountForHost(String.valueOf(userId));
        //call api getRateCountForHost 6/5/21 send host profile_id here
        apiManager.getRateCountForHost(String.valueOf(hostId));*/

        //    Log.e("id",userId+"");

        //  Log.e("userIDEmp", userId + "");
       /* callRate = userData.getCall_rate();
        binding.userName.setText(userData.getName());
        binding.userId.setText("ID : " + userData.getProfile_id());
        binding.callRateTv.setText(callRate + "/min");

        binding.audioCallRateTv.setText(userData.getAudio_call_rate() + "/min");
        binding.cityName.setText(userData.getCity());
        binding.aboutUser.setText(userData.getAbout_user());

        ProfilePagerAdapter adapter = new ProfilePagerAdapter(this, userData.getProfile_images(), true);
        binding.viewpager.setAdapter(adapter);

        new TabLayoutMediator(binding.indicatorDot, binding.viewpager,
                (tab, position) -> {
                    // tab.setText(" " + (position + 1));
                }
        ).attach();


        // Hide video call feature for female user
        if (new SessionManager(this).getGender().equals("female")) {
            binding.videoChat.setVisibility(View.GONE);
        } else {
        }*/

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_albumShow.setLayoutManager(linearLayoutManager);

        apiManager.getProfileDataNew(String.valueOf(hostIdFemale), "");
        Log.e("activitysss", "onCreate: viewProfile UserID " + hostIdFemale);

        //addRemoveFav();

    }

    //changed as per suggestion at 20/01/23 to show bg as per charm level i.e. 0 -> charm_zero image and remaining level_bbg.
    private void setHostLevel(String charmLevel) {
        hostLevel = charmLevel;
        String t = "Lv ." + hostLevel;
        binding.levelCharm.setText(t);

        if (hostLevel.equals("0")) {
            binding.charmLevelBg.setBackgroundResource(R.drawable.charm_zero);
        } else
            binding.charmLevelBg.setBackgroundResource(R.drawable.level_bbg);

    }

    private void setOnlineStatus() {

        Log.e("statuscheckfirebase", "setOnlineStatus:  setOnlineStatus ");

        firebaseref = FirebaseDatabase.getInstance().getReference().child("Users").child(hostProfileID);

        firebaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> map = null;
                if (snapshot.exists()) {
                    map = (Map<String, Object>) snapshot.getValue();
                    Log.e("statuscheckfirebase", "onDataChange: " + map.get("status"));

                    if (map.get("status").equals("Online") || map.get("status").equals("Live")) {

                        Log.e("statuscheckfirebase", "onDataChange: " + map.get("status"));
                        binding.isOnline.setText("Online");
                        binding.isOnline.setBackgroundResource(R.drawable.viewprofile_online_background);

                    } else if (map.get("status").equals("Busy")) {

                        Log.e("statuscheckfirebase", "onDataChange: " + map.get("status").toString());
                        binding.isOnline.setText("Busy");
                        binding.isOnline.setBackgroundResource(R.drawable.viewprofile_busybackground);

                    } else if (map.get("status").equals("Offline")) {

                        Log.e("statuscheckfirebase", "onDataChange: " + map.get("status"));
                        binding.isOnline.setText("Offline");
                        binding.isOnline.setBackgroundResource(R.drawable.viewprofile_offline_background);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("statuscheckfirebase", "onCancelled: " + error.getDetails());

            }
        });


    /*

        if (userData.get(0).getIsBusy() == 0) {
            if (userData.get(0).getIsOnline() == 1) {
                binding.isOnline.setText("Online");
                binding.isOnline.setBackgroundResource(R.drawable.viewprofile_online_background);
                // binding.isOnline.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0);
            } else {
                binding.isOnline.setText("Offline");
                binding.isOnline.setBackgroundResource(R.drawable.viewprofile_offline_background);
                // binding.isOnline.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_grey, 0, 0, 0);
            }
        } else {
            binding.isOnline.setText("Busy");
            //  binding.isOnline.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_orange, 0, 0, 0);
            binding.isOnline.setBackgroundResource(R.drawable.viewprofile_busybackground);
        }
*/

    }

    public void openVideoChat(View view) {
        // Check user is online before make a call
        Log.e("CallProcess", "openVideoChat: viewProfile " + " videochat clicked");

        if (CheckPermission()) {
            callType = "video";
            apiManager.getRemainingGiftCardFunction();
            view.setEnabled(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                }
            }, 2000);

        } else {

        }

      /*  try {
            try {
                if (remGiftCard > 0) {
                    apiManager.searchUser(String.valueOf(userData.getProfile_id()), "1");
                    return;
                }
            } catch (Exception e) {
            }

            if (new SessionManager(getApplicationContext()).getUserWallet() > callRate) {
                apiManager.searchUser(String.valueOf(userData.getProfile_id()), "1");
            } else {
                new InsufficientCoins(ViewProfile.this, 2, callRate);
            }
        } catch (Exception e) {
            apiManager.searchUser(String.valueOf(userData.getProfile_id()), "1");
        }*/

    }


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


        Dexter.withActivity(ViewProfileMet.this).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("onPermissionsChecked", "onPermissionsChecked: ");

                if (report.areAllPermissionsGranted()) {
                    Log.e("onPermissionsChecked", "all permission granted");
                    isPermissionGranted[0] = true;
                } else {
                    isPermissionGranted[0] = false;
                    Toast.makeText(ViewProfileMet.this, "To use this feature Camera and Audio permissions are must.You need to allow the permissions", Toast.LENGTH_SHORT).show();
                    // Dexter.withActivity(ViewProfile.this).withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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


    private String callType = "";

    public void openVoiceChat(View view) {
        // Check user is online before make a call
        callType = "audio";
        try {
            if (new SessionManager(getApplicationContext()).getUserWallet() > callRate) {
                apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
            } else {
                new InsufficientCoins(ViewProfileMet.this, 2, callRate);
            }
        } catch (Exception e) {
            apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
        }
    }

    public void goToVideoStatus(int adapterPosition) {

        Log.e("VideoStatusAdapter1", "onClick: " + adapterPosition);
        Log.e("VideoStatusAdapter1", "goToVideoStatus: " + new Gson().toJson(userData.get(0)));
        Intent intent = new Intent(ViewProfileMet.this, ActivityStatus.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name", userData.get(0).getName());
        intent.putExtra("id", String.valueOf(userData.get(0).getId()));
        intent.putExtra("profileId", String.valueOf(userData.get(0).getProfileId()));
        intent.putExtra("level", String.valueOf(hostLevel));
        intent.putExtra("location", String.valueOf(userData.get(0).getCity()));
        intent.putExtra("callrate", userData.get(0).getCallRate());
        intent.putExtra("videonum", "" + adapterPosition);

        if (userData.get(0).getFemaleImages().size() > 0) {
            intent.putExtra("profile_pic", userData.get(0).getFemaleImages().get(0).getImageName());
        } else {
            intent.putExtra("profile_pic", "");
        }

        try {
            String[] dob = userData.get(0).getDob().split("-");
            int date = Integer.parseInt(dob[0]);
            int month = Integer.parseInt(dob[1]);
            int year = Integer.parseInt(dob[2]);
            intent.putExtra("age", String.valueOf(getAge(year, month, date)));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("viewProf1", "goToVideoStatus: Exception " + e.getMessage());
        }

        Log.e("viewProf1", "goToVideoStatus: list " + userData.get(0).getFemaleVideo().size());
        ArrayList<String> list2 = getVideolinksList(userData.get(0));
        Log.e("viewProf1", "goToVideoStatus: list " + getVideolinksList(userData.get(0)));

        intent.putStringArrayListExtra("resoureList", list2);
        intent.putStringArrayListExtra("thumbnailList", getThumbnailList(userData.get(0)));
        startActivity(intent);

    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void addToFav() {
            addRemoveFav();
            apiManager.followingHost(hostIdFemale);
            Log.e("newUserId", userId + "");
        }

        public void onBack() {
            onBackPressed();
        }

        public void gotoChatConversation() {
            //Here pass userId and callRate send data on InboxDetail activity. by Kalpesh Sir..


            if (userData.size() > 0) {
                Intent intent = new Intent(ViewProfileMet.this, InboxDetails.class);
                intent.putExtra("profileName", userData.get(0).getName());
                intent.putExtra("user_image", userData.get(0).getFemaleImages().get(0).getImageName());
                intent.putExtra("chatProfileId", String.valueOf(userData.get(0).getProfileId()));
                intent.putExtra("contactId", userData.get(0).getId());
                intent.putExtra("mode", true);
                intent.putExtra("channelName", "zeeplive662730982537574");
                intent.putExtra("usercount", 0);
                intent.putExtra("unreadMsgCount", 0);
                intent.putExtra("userGiftCount", userData.get(0).getUserGiftCount());
             /*   intent.putExtra("receiver_id", String.valueOf(userData.get(0).getProfileId()));
            intent.putExtra("newParem", String.valueOf(userData.get(0).getId()));
            intent.putExtra("receiver_name", userData.get(0).getName());
            intent.putExtra("user_id", String.valueOf(userId));
            //pass  Unique_id for gift Sends 4/5/21
            //intent.putExtra("Unique_id", String.valueOf(unique_id));
            intent.putExtra("call_rate", String.valueOf(callRate));

            *//*Intent intent = new Intent(ViewProfile.this, InboxDetails.class);
            intent.putExtra("receiver_id", String.valueOf(userData.getProfile_id()));
            intent.putExtra("receiver_name", userData.getName());*//*

            if (userData.get(0).getFemaleImages() == null || userData.get(0).getFemaleImages().size() == 0) {
                intent.putExtra("receiver_image", "empty");
            } else {
                intent.putExtra("receiver_image", userData.get(0).getFemaleImages().get(0).getImageName());
            }*/
                startActivity(intent);
            }


            //    new SessionManager(ViewProfile.this).isRecentChatListUpdateNeeded(true);
        }

       /* public void gotoChatConversation() {


            if (networkCheck.isNetworkAvailable(getApplicationContext())) {

                ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

                RequestChatRoom requestChatRoom = new RequestChatRoom("FSAfsafsdf", Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                        new SessionManager(getApplicationContext()).getUserName(), "ProfilePhoto",
                        "1", userData.getProfile_id(), userData.getName(),
                        userData.getProfile_images().get(0).getImage_name(), "2",
                        0, callRate, 0, 20, "",
                        "countrtStstic", String.valueOf(userId));
                Call<ResultChatRoom> chatRoomCall = apiservice.createChatRoom("application/json", requestChatRoom);
                //     Log.e("ConId", new Gson().toJson(requestChatRoom));
                chatRoomCall.enqueue(new Callback<ResultChatRoom>() {
                    @Override
                    public void onResponse(Call<ResultChatRoom> call, Response<ResultChatRoom> response) {

                        try {
                            if (!response.body().getData().getId().equals("")) {
                                Intent intent;
                                //Intent intent = new Intent(ViewProfile.this, InboxDetails.class);
                                if (new SessionManager(ViewProfile.this).getGender().equals("female")) {
                                    intent = new Intent(ViewProfile.this, ChatEmployeeActivity.class);
                                    intent.putExtra("recID", String.valueOf(userData.getProfile_id()));
                                    intent.putExtra("recName", userData.getName());
                                    intent.putExtra("callrate", String.valueOf(userData.getCall_rate()));
                                    intent.putExtra("converID", response.body().getData().getId());
                                    intent.putExtra("tokenUserId", String.valueOf(userId));
                                    if (userData.getProfile_images() == null || userData.getProfile_images().size() == 0) {
                                        intent.putExtra("ProPic", "empty");
                                        intent.putExtra("recProfilePic", "empty");
                                    } else {
                                        intent.putExtra("ProPic", userData.getProfile_images().get(0).getImage_name());
                                        intent.putExtra("recProfilePic", userData.getProfile_images().get(0).getImage_name());
                                    }
                                } else {
                                    intent = new Intent(ViewProfile.this, ChatActivity.class);
                                    intent.putExtra("receiver_id", String.valueOf(userData.getProfile_id()));
                                    intent.putExtra("receiver_name", userData.getName());
                                    intent.putExtra("callrate", String.valueOf(userData.getCall_rate()));
                                    intent.putExtra("converID", response.body().getData().getId());
                                    intent.putExtra("tokenUserId", String.valueOf(userId));
                                    if (userData.getProfile_images() == null || userData.getProfile_images().size() == 0) {
                                        intent.putExtra("receiver_image", "empty");
                                    } else {
                                        intent.putExtra("receiver_image", userData.getProfile_images().get(0).getImage_name());
                                    }
                                }

                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultChatRoom> call, Throwable t) {
                        //         Log.e("onResponseChatRoom: ", t.getMessage());
                    }
                });
            }


        }*/

        public void reportUser() {
            new ReportDialog(ViewProfileMet.this, String.valueOf(userId));
        }
    }

    public void addRemoveFav() {
        Intent myIntent = new Intent("FBR");
        myIntent.putExtra("action", "reload");
        this.sendBroadcast(myIntent);

        if (isFavourite == 0) {
            binding.nonFavourite.setVisibility(View.VISIBLE);
            //for hide follow button
            binding.nonFavourite.setText("Follow");
            binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_fallow_background);
            isFavourite = 1;
        } else {
            new Handler().postDelayed(() -> binding.nonFavourite.setVisibility(View.GONE), 500);
            // binding.nonFavourite.setText("UnFollow");
            // binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_offline_background);
            isFavourite = 0;
        }
    }

    private void getPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
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

    private InsufficientCoins insufficientCoins;

    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            insufficientCoins = new InsufficientCoins(ViewProfileMet.this, 2, callRate);
            Log.e("insufficientCoins", "isError: ");


            insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Log.e("insufficientCoins1", "onCancel: isError ");
                    apiManager.checkFirstTimeRechargeDone();

                }
            });


        } else {
            Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {
            RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;

            // Log.e("ViewProfilerr", "isSuccess: "+new Gson().toJson(userData.get(0)));

            if (userData != null && userData.size() > 0) {
                Log.e("ViewProfilerr", "isSuccess: userlist not null ");
                try {
                    try {
                        success = rsp.getSuccess();
                        remGiftCard = rsp.getResult().getRemGiftCards();
                        freeSeconds = rsp.getResult().getFreeSeconds();
                        if (remGiftCard > 0) {
                            apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
                            return;
                        }
                    } catch (Exception e) {

                    }
                    if (new SessionManager(getApplicationContext()).getUserWallet() >= 20) {
                        apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
                    } else {
                        insufficientCoins = new InsufficientCoins(ViewProfileMet.this, 2, callRate);
                        Log.e("insufficientCoins", "isSuccess: GET_REMAINING_GIFT_CARD ");


                        insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                Log.e("insufficientCoins1", "onCancel: GET_REMAINING_GIFT_CARD");
                                apiManager.checkFirstTimeRechargeDone();
                            }
                        });

                    }
                } catch (Exception e) {
                    apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
                }

            } else {

                Log.e("ViewProfilerr", "isSuccess: userlist null ");

            }


        }


        if (ServiceCode == Constant.GET_FIRST_TIME_RECHARGE) {

            DiscountedRechargeResponse rsp = (DiscountedRechargeResponse) response;

            if (rsp.getIsRecharge() == 0) {
                Log.e("getIsRecharge", "isSuccess: ViewDetail if " + rsp.getIsRecharge());
                //  FirstTimeRechargeDialog();
                apiManager.getFirstTimeRechargeList();
            } else if (rsp.getIsRecharge() == 1) {
                Log.e("getIsRecharge", "isSuccess: ViewDetail else " + rsp.getIsRecharge());
            }
        }

        if (ServiceCode == GET_FIRST_TIME_RECHARGE_LIST) {

            FirstTimeRechargeListResponse firstTimeRechargeListResponse = (FirstTimeRechargeListResponse) response;
            RechargePlanResponseNew.Data firstRecharge = firstTimeRechargeListResponse.getResult();
            FirstTimeRechargeDialog(firstRecharge);

        }

        if (ServiceCode == Constant.GET_GIFT_COUNT) {
            GiftCountResult rsp = (GiftCountResult) response;

            try {
                resultArrayList.addAll(rsp.getResult());
                if (resultArrayList.size() == 0) {
                    binding.tvGifrecmsg.setVisibility(View.GONE);
                    rv_giftshow.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < rsp.getResult().size(); i++) {
                        giftDetailsArrayList.add(rsp.getResult().get(i).getGiftDetails());
                        //    Log.e("receviedGiftfemale", new Gson().toJson(giftDetailsArrayList));
                    }
                    //giftDetailsArrayList.add(rsp.getResult().get(0).getGiftDetails());
                    giftCountDisplayAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {

            }

        }


        /*if (ServiceCode == Constant.GENERATE_AGORA_TOKEN) {
            AgoraTokenResponse rsp = (AgoraTokenResponse) response;

            if (rsp.getResult().getNotification() != null && rsp.getResult().getNotification().getSuccess() == 1) {

             *//*   int talkTime = walletBalance / userData.get(0).getCallRate() * 1000 * 60;

                //  int talkTime2 = userData.getCall_rate() * 1000 * 60;
                // Minus 2 sec to prevent balance goes into minus
                int canCallTill = talkTime - 2000;*//*

                int CallRateInt = callRate;
                long talktime = (walletBalance / CallRateInt) * 1000;
                long canCallTill = talktime - 2000;

                Log.e("CALL_RATE_TEST", "isSuccess: ViewProfile talktime " + talktime);




                *//*
                Log.e("walletBalance", walletBalance + "");
                Log.e("callRate", userData.getCall_rate() + "");
                Log.e("talkTime", talkTime + "");
                Log.e("talkTime2", talkTime2 + "");
                Log.e("canCallTill", canCallTill + "");
                *//*


                String profilePic = new SessionManager(getApplicationContext()).getUserProfilepic();
                HashMap<String, String> user = new SessionManager(getApplicationContext()).getUserDetails();
                //  Intent intent = new Intent(ViewProfile.this, VideoChatActivity.class);
                //    appLifecycle.InitiateCall(String.valueOf(userData.get(0).getProfileId()),"Video Call","");


                Intent intent = new Intent(ViewProfile.this, VideoChatZegoActivity.class);
                intent.putExtra("TOKEN", rsp.getResult().getToken());
                intent.putExtra("ID", String.valueOf(userData.get(0).getProfileId()));
                intent.putExtra("UID", String.valueOf(userId));
                intent.putExtra("CALL_RATE", String.valueOf(userData.get(0).getCallRate()));
                intent.putExtra("UNIQUE_ID", rsp.getResult().getUnique_id());
                intent.putExtra("AUTO_END_TIME", canCallTill);
                intent.putExtra("receiver_name", userData.get(0).getName());
                intent.putExtra("converID", convId);
                intent.putExtra("userGiftCount", userData.get(0).getUserGiftCount());


            *//*    appLifecycle.InitiateCall(String.valueOf(userData.get(0).getProfileId()), "Video Call", "");
                initiateMessageWithCall(String.valueOf(userData.get(0).getProfileId()), user.get(NAME), user.get(PROFILE_ID), rsp.getResult().getUnique_id(), String.valueOf(false), profilePic, "video");
*//*
                if (userData.get(0).getFemaleImages() == null || userData.get(0).getFemaleImages().size() == 0) {
                    intent.putExtra("receiver_image", "empty");
                } else {
                    intent.putExtra("receiver_image", userData.get(0).getFemaleImages().get(0).getImageName());
                }
                startActivity(intent);


            } else {
                Toast.makeText(this, "Server is busy, Please try again", Toast.LENGTH_SHORT).show();
            }


        } */


      /*  if (ServiceCode == Constant.WALLET_AMOUNT) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            // if wallet balance greater than call rate , Generate token to make a call
            if (rsp.getResult().getTotal_point() >= callRate) {
                walletBalance = rsp.getResult().getTotal_point();
                if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                    ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                apiManager.showDialog();
                            } catch (Exception e) {
                            }
                        }
                    }, 100);
                    RequestChatRoom requestChatRoom = new RequestChatRoom("FSAfsafsdf",
                            Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                            new SessionManager(getApplicationContext()).getUserName(),
                            "ProfilePhoto", "1", userData.get(0).getProfileId(),
                            userData.get(0).getName(), userData.get(0).getFemaleImages().get(0).getImageName(),
                            "2", 0, callRate, 0, 20, "",
                            "countrtStstic", String.valueOf(userId));

                    Call<ResultChatRoom> chatRoomCall = apiservice.createChatRoom("application/json", requestChatRoom);

                    chatRoomCall.enqueue(new Callback<ResultChatRoom>() {
                        @Override
                        public void onResponse(Call<ResultChatRoom> call, Response<ResultChatRoom> response) {
                            // Log.e("onResponseRoom: ", new Gson().toJson(response.body()));
                            try {
                                apiManager.closeDialog();
                            } catch (Exception e) {
                            }
                            try {
                                if (!response.body().getData().getId().equals("")) {
                                    convId = response.body().getData().getId();

                                    //apiManager.generateAgoraToken(userId, String.valueOf(System.currentTimeMillis()), convId);


                                   *//* Log.e("userid", userId + "");
                                    Log.e("outgoingTime", String.valueOf(System.currentTimeMillis()));
                                    Log.e("convId", convId);*//*

                                   *//*
                                    Intent intent = new Intent(ViewProfile.this, ChatActivity.class);
                                    intent.putExtra("receiver_id", String.valueOf(userData.getProfile_id()));
                                    intent.putExtra("receiver_name", userData.getName());
                                    intent.putExtra("converID", response.body().getData().getId());
                                    if (userData.getProfile_images() == null || userData.getProfile_images().size() == 0) {
                                        intent.putExtra("receiver_image", "empty");
                                    } else {
                                        intent.putExtra("receiver_image", userData.getProfile_images().get(0).getImage_name());
                                    }
                                    startActivity(intent);
                                    *//*


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultChatRoom> call, Throwable t) {
                            // Log.e("onResponseChatRoom: ", t.getMessage());
                            try {
                                apiManager.generateAgoraToken(userId, String.valueOf(System.currentTimeMillis()), convId);
                                apiManager.closeDialog();
                            } catch (Exception e) {
                            }
                        }
                    });
                }

            } else {
                // Open Insufficient Coin popup
                insufficientCoins = new InsufficientCoins(ViewProfileMet.this, 2, callRate);

                Log.e("insufficientCoins", "isSuccess: WALLET_AMOUNT ");
                insufficientCoins.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        apiManager.checkFirstTimeRechargeDone();
                    }
                });


            }
        }*/

       /* if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
            ResultCall rsp = (ResultCall) response;

            //int talkTime = walletBalance / userData.get(0).getCallRate() * 1000 * 60;
            // int talkTime = walletBalance * 1000;

            //  int talkTime2 = userData.getCall_rate() * 1000 * 60;
            // Minus 2 sec to prevent balance goes into minus

            walletBalance = rsp.getResult().getPoints().getTotalPoint();

            int CallRateInt = callRate;

            long talktime = (walletBalance / CallRateInt) * 1000;
            long canCallTill = talktime - 2000;

            Log.e("CALL_RATE_TEST", "isSuccess: talktime ViewProfile " + talktime + " callrateint " + CallRateInt);

           *//* Log.e("walletBalance", walletBalance + "");
            Log.e("talkTime", talkTime + "");
            Log.e("canCallTill", canCallTill + "");*//*
            String profilePic = new SessionManager(getApplicationContext()).getUserProfilepic();
            HashMap<String, String> user = new SessionManager(getApplicationContext()).getUserDetails();


            Intent intent = new Intent(ViewProfileMet.this, VideoChatZegoActivity.class);


            // Intent intent = new Intent(ViewProfile.this, VideoChatActivity.class);
            intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
            intent.putExtra("ID", String.valueOf(userData.get(0).getProfileId()));
            intent.putExtra("UID", String.valueOf(userId));
            intent.putExtra("CALL_RATE", String.valueOf(userData.get(0).getCallRate()));
            intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());
            intent.putExtra("userGiftCount", userData.get(0).getUserGiftCount());

            //   new AppLifecycle().InitiateCall(String.valueOf(userData.get(0).getProfileId()),"Video Call","");
            //    appLifecycle.InitiateCall(String.valueOf(userData.get(0).getProfileId()), "Video Call", "");
            //     initiateMessageWithCall(String.valueOf(userData.get(0).getProfileId()), user.get(NAME), user.get(PROFILE_ID), rsp.getResult().getData().getUniqueId(), String.valueOf(false), profilePic, "video");

            if (remGiftCard > 0) {
                int newFreeSec = Integer.parseInt(freeSeconds) * 1000;
                intent.putExtra("AUTO_END_TIME", newFreeSec);
                intent.putExtra("is_free_call", "true");
                isFreeCall = true;
            } else {
                intent.putExtra("AUTO_END_TIME", canCallTill);
                intent.putExtra("is_free_call", "false");
                isFreeCall = false;
            }
            intent.putExtra("receiver_name", userData.get(0).getName());
            Log.e("actStatus", "isSuccess: viewProfile username " + userData.get(0).getName());
            Log.e("viewpppp", "isSuccess: convId  " + convId);
            intent.putExtra("converID", convId);

            if (userData.get(0).getFemaleImages() == null || userData.get(0).getFemaleImages().size() == 0) {
                intent.putExtra("receiver_image", "empty");
            } else {
                intent.putExtra("receiver_image", userData.get(0).getFemaleImages().get(0).getImageName());
            }

            try {
                Log.e("viewppppp", "isSuccess: try ");
              *//*  String data = getMessageWithCall(String.valueOf(userData.get(0).getProfileId()), user.get(NAME), user.get(PROFILE_ID), rsp.getResult().getData().getUniqueId(), String.valueOf(isFreeCall), profilePic, "video", canCallTill);

                zimManager.callInvite(String.valueOf(userData.get(0).getProfileId()), data, new ResultCallback() {
                    @Override
                    public void onZimCallback(ZIMErrorCode errorCode, String errMsg) {
                        if (errorCode == ZIMErrorCode.SUCCESS) {
                            //start call successfully
                            Log.d("ViewProfile", "onZimCallback: send call successfully.");
                            startActivity(intent);
                        } else {
                            //start call failed
                            // Toast.makeText(ViewProfile.this,"start call failed  "+errorCode,Toast.LENGTH_SHORT).show();

                        }
                    }
                });*//*

                Log.e("viewppppp", "isSuccess: try end ");
            } catch (Exception e) {
                Log.e("viewppppp", "isSuccess: Exception " + e.getMessage());

            }


        }*/


        if (ServiceCode==Constant.NEW_GENERATE_AGORA_TOKENZ)
        {
            GenerateCallResponce rsp = (GenerateCallResponce) response;

            Log.e("NEW_GENERATE_AGORA_TOKENZ", "isSuccess: "+new Gson().toJson(rsp));

            int walletBalance = rsp.getResult().getPoints().getTotalPoint();
            int CallRateInt = callRate;
            long talktime = (walletBalance / CallRateInt) * 1000L;
            long canCallTill = talktime - 2000;
            String profilePic = new SessionManager(getApplicationContext()).getUserProfilepic();
            HashMap<String, String> user = new SessionManager(getApplicationContext()).getUserDetails();
            Intent intent = new Intent(ViewProfileMet.this, VideoChatZegoActivityMet.class);
            intent.putExtra("TOKEN", rsp.getResult().getData().getSenderChannelName().getToken().getToken());
            intent.putExtra("ID", String.valueOf(userData.get(0).getProfileId()));
            intent.putExtra("UID", String.valueOf(userId));
            intent.putExtra("CALL_RATE", String.valueOf(callRate));
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
            intent.putExtra("receiver_name",  userData.get(0).getName());
            intent.putExtra("converID", "convId");
            intent.putExtra("receiver_image",  userData.get(0).getFemaleImages().get(0).getImageName());
            startActivity(intent);

        }



        if (ServiceCode == Constant.SEARCH_USER) {
            UserListResponseMet rsp = (UserListResponseMet) response;

            if (rsp != null) {
                try {
                  /*  int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                    int busyStatus = rsp.getResult().getData().get(0).getIs_busy();*/


                    Log.e("ViewProfileFirebase", "isSuccess: " + userData.get(0).getProfileId());


                    Log.e("ProfileIdTestFB", "ViewProfile isSuccess " + userData.get(0).getProfileId());
                    firebaseref = FirebaseDatabase.getInstance().getReference().child("Users").child(String.valueOf(userData.get(0).getProfileId()));


                    firebaseref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = null;
                            if (dataSnapshot.exists()) {
                                map = (Map<String, Object>) dataSnapshot.getValue();

                                if (map.get("status").equals("Online") || map.get("status").equals("Live")) {

                                    if (callType.equals("video")) {
                                       /* if (remGiftCard > 0) {
                                            apiManager.generateCallRequest(userData.get(0).getProfileId(), String.valueOf(System.currentTimeMillis()), convId, callRate,
                                                    Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                        } else {
                                            apiManager.generateCallRequest(userData.get(0).getProfileId(), String.valueOf(System.currentTimeMillis()), convId, callRate,
                                                    Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                        }*/



                                        if (remGiftCard > 0) {
                                            apiManager.generateCallRequestZ(userData.get(0).getProfileId(), String.valueOf(System.currentTimeMillis()), "0", callRate,
                                                    Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                        } else {
                                            apiManager.generateCallRequestZ(userData.get(0).getProfileId(), String.valueOf(System.currentTimeMillis()), "0", callRate,
                                                    Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                        }



                                    } else if (callType.equals("audio")) {
                                      /*  apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                                                String.valueOf(System.currentTimeMillis()));*/
                                    }

                                } else if (map.get("status").equals("Busy")) {
                                    Toast.makeText(ViewProfileMet.this, "User is Busy", Toast.LENGTH_LONG).show();
                                } else if (map.get("status").equals("Offline")) {
                                    Toast.makeText(ViewProfileMet.this, "User is Offline", Toast.LENGTH_LONG).show();

                                }

                            }

                        }
                    });


//


                  /*    if (onlineStatus == 1 && busyStatus == 0) {
                        // Check wallet balance before going to make a video call
                        //     apiManager.getWalletAmount();

                        if (callType.equals("video")) {
                            if (remGiftCard > 0) {
                                apiManager.generateCallRequest(userData.get(0).getProfileId(), String.valueOf(System.currentTimeMillis()), convId, callRate,
                                        Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                            } else {
                                apiManager.generateCallRequest(userData.get(0).getProfileId(), String.valueOf(System.currentTimeMillis()), convId, callRate,
                                        Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                            }
                        } else if (callType.equals("audio")) {
                            apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                                    String.valueOf(System.currentTimeMillis()));
                        }
                    } else if (onlineStatus == 1) {
                        Toast.makeText(this, userData.get(0).getName() + " is Busy", Toast.LENGTH_SHORT).show();

                    } else if (onlineStatus == 0) {
                        Toast.makeText(this, userData.get(0).getName() + " is Offline", Toast.LENGTH_SHORT).show();
                    }*/

                } catch (Exception e) {
                    Toast.makeText(this, "User is Offline!", Toast.LENGTH_SHORT).show();
                    new SessionManager(getApplicationContext()).setOnlineState(0);
                    finish();
                }
            }
        }

        if (ServiceCode == Constant.PLAY_VIDEO) {

        /*
            try {
                VideoPlayResponce rsp = (VideoPlayResponce) response;
                if (rsp != null) {
                    //    Log.e("getVideoForProfile", new Gson().toJson(response));
                    binding.cvVideo.setVisibility(View.VISIBLE);
                    String videourl = ((VideoPlayResponce) response).getResult().get(0).getVideoName();
                    //Log.e("vvURL", videourl);
                    Uri uri = Uri.parse(videourl);
                    binding.vvVideo.setVideoURI(uri);

                    binding.vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {

                            binding.searchLoader.setVisibility(View.GONE);

                            mediaPlayer.setVolume(0f, 0f);
                            binding.vvVideo.start();

                        }
                    });

                    binding.vvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.stop();
                            binding.cvVideo.setVisibility(View.GONE);
                            binding.vvVideo.setVisibility(View.GONE);
                        }
                    });
                }
            } catch (Exception e) {

            }
            */


        }

        if (ServiceCode == Constant.GENERATE_VOICE_CALL_TOKEN) {


         /*
            VoiceCallResponce rsp = (VoiceCallResponce) response;
            walletBalance = rsp.getResult().getPoints().getTotalPoint();
            int talkTime = walletBalance / userData.get(0).getCallRate() * 1000 * 60;

            //  int talkTime2 = userData.getCall_rate() * 1000 * 60;
            // Minus 2 sec to prevent balance goes into minus




            int canCallTill = talkTime - 2000;

            Intent intent = new Intent(ViewProfile.this, VoiceChatViewActivity.class);
            intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
            intent.putExtra("UID", String.valueOf(userId));
            //   intent.putExtra("CALL_RATE", String.valueOf(userData.getCall_rate()));
            intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());
            intent.putExtra("AUTO_END_TIME", canCallTill);
            intent.putExtra("receiver_name", userData.get(0).getName());
            //   intent.putExtra("converID", convId);

            if (userData.get(0).getFemaleImages() == null || userData.get(0).getFemaleImages().size() == 0) {
                intent.putExtra("receiver_image", "empty");
            } else {
                intent.putExtra("receiver_image", userData.get(0).getFemaleImages().get(0).getImageName());
            }
            startActivity(intent);

            */


        }


        //Show female profile data for male 10/5/21
        if (ServiceCode == Constant.GET_PROFILE_DATA) {
            // UserListResponse.Data userData;
            com.privatepe.app.response.metend.UserListResponseNew.UserListResponseNewData rsp = (com.privatepe.app.response.metend.UserListResponseNew.UserListResponseNewData) response;
            //userData = (ResultDataNewProfile) rsp.getResult();

            userData.addAll(rsp.getResult());
            // binding.setResponse(userData);

            try {
                isFavourite = userData.get(0).getFavoriteByYouCount();
                setHostLevel(userData.get(0).getCharm_level().toString());
            } catch (Exception e) {

            }

            if (isFavourite == 0) {
                binding.nonFavourite.setVisibility(View.VISIBLE);
                //hide for follow button
                binding.nonFavourite.setText("Follow");
                binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_fallow_background);
                isFavourite = 1;
            } /*else {
                //binding.nonFavourite.setText("UnFollow");
                //binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_offline_background);
                isFavourite = 0;
            }*/


            dp = rsp.getResult().get(0).getFemaleImages().get(0).getImageName();
            Log.e("vvvvvt", "isSuccess: " + dp);

            int totalvideos = rsp.getResult().get(0).getFemaleVideo().size();
            videostatusList.addAll(rsp.getResult().get(0).getFemaleVideo());


       /*     for (int i=0;i<totalvideos;i++)
            {
                Log.e("vvvvvvvd", "isSuccess: videoname "+rsp.getResult().get(0).getFemaleVideo().get(i).getVideoName() );
                videostatusList.add(rsp.getResult().get(0).getFemaleVideo().get(i).getVideoName());
            }
*/
            if (totalvideos > 0) {
                binding.liVideoStatus.setVisibility(View.VISIBLE);

            } else {

                binding.liVideoStatus.setVisibility(View.GONE);
            }
            videoStatusDisplayAdapter = new VideoStatusAdapter(getApplicationContext(), videostatusList, dp, ViewProfileMet.this);
            rv_videostatus.setLayoutManager(new GridLayoutManager(this, 5));
            rv_videostatus.setAdapter(videoStatusDisplayAdapter);


            //  Log.e("totalvideosViewProfile", "isSuccess: "+totalvideos );

            //  videostatusList=getVideolinksList(rsp.);


            String[] dob = userData.get(0).getDob().split("-");
            int date = Integer.parseInt(dob[0]);
            int month = Integer.parseInt(dob[1]);
            int year = Integer.parseInt(dob[2]);
            binding.tvAge.setText("Age: " + getAge(year, month, date));

            userId = userData.get(0).getId();
            //  Log.e("activitysss", "onCreate: ACTIVITYSTATUS UserID2 "+userId );
            hostId = userData.get(0).getProfileId();

            //   Log.e("activitysss", "onCreate: ACTIVITYSTATUS UserID3 "+hostId );

            callRate = userData.get(0).getCallRate();

            apiManager.getVideoForProfile(String.valueOf(userId));

            apiManager.getGiftCountForHost(String.valueOf(userId));
            //call api getRateCountForHost 6/5/21 send host profile_id here
            apiManager.getRateCountForHost(String.valueOf(userId));


            binding.tvCallMePrice.setText(String.valueOf(callRate));

            Log.e("ViewProfilecallrate", "callratevalueDynamic " + String.valueOf(callRate));
            binding.userName.setText(userData.get(0).getName());
            binding.userId.setText("ID : " + userData.get(0).getProfileId());

            // binding.callRateTv.setText(callRate + "/min");
            // binding.aboutUser.setText(userData.getAbout_user());
            // binding.audioCallRateTv.setText(userData.get(0).getAudioCallRate() + "/min");


            Log.e("ViewProfileImagess", "isSuccess: imagesListSize " + userData.get(0).getFemaleImages().size());

            binding.cityName.setText(userData.get(0).getCity());
            ProfilePagerAdapterMet adapter = new ProfilePagerAdapterMet(this, userData.get(0).getFemaleImages(), true);
            binding.viewpager.setAdapter(adapter);
            // binding.viewpager.setUserInputEnabled(false);

            List<FemaleImage> albumList = new ArrayList<>();
            albumList.addAll(userData.get(0).getFemaleImages());
            Log.e("listFemaleSizeIn", String.valueOf(albumList.size()));
            if (albumList.size() > 0) {
                albumList.remove(0);

                if (albumList.size() < 1) {
                    binding.liAlbum.setVisibility(View.GONE);
                }
            }
            adapter_album = new AlbumAdapterViewProfileMet(ViewProfileMet.this, albumList, true);
            rv_albumShow.setAdapter(adapter_album);

            new TabLayoutMediator(binding.indicatorDot, binding.viewpager,
                    (tab, position) -> {
                        // tab.setText(" " + (position + 1));
                    }
            ).attach();

            // Hide video call feature for female user
            if (new SessionManager(this).getGender().equals("female")) {
                binding.videoChat.setVisibility(View.GONE);
            } else {
            }

            setOnlineStatus();


        }

        //Show Rating for female 6/5/21
        if (ServiceCode == Constant.GET_RATING_COUNT) {
            UserListResponseNewData rsp = (UserListResponseNewData) response;
            // Log.e("inViewPeofileFR", new Gson().toJson(rsp.getResult()));
            ratingArrayList.addAll(rsp.getResult().get(0).getGetRatingTag());
            // Log.e("inViewPeofile", new Gson().toJson(ratingArrayList));
            //String val = String.format("%.0f", 2.11);
            String score = rsp.getResult().get(0).getRatingsAverage();
            if (TextUtils.isEmpty(score)) {
                //  binding.tvRate.setText("Score 0");
                binding.tvRate.setVisibility(View.GONE);
            } else {
                binding.tvRate.setText("Score " + score);
            }
            // binding.tvRate.setText("Score " + rsp.getResult().get(0).getRatingsAverage());
            rateCountDisplayAdapter.notifyDataSetChanged();
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

    private ArrayList<String> getVideolinksList(ResultDataNewProfile data) {

        ArrayList<String> videolist = new ArrayList<>();
        videolist.clear();

        for (int i = 0; i < data.getFemaleVideo().size(); i++) {
            videolist.add(data.getFemaleVideo().get(i).getVideoName());
            Log.e("viewprofiledata", "getVideolinksList: " + data.getFemaleVideo().get(i).getVideoUrl());

        }
        return videolist;
    }

    private ArrayList<String> getThumbnailList(ResultDataNewProfile data) {
        ArrayList<String> thumbnailList = new ArrayList<>();
        for (int i = 0; i < data.getFemaleVideo().size(); i++) {
            thumbnailList.add(data.getFemaleVideo().get(i).getVideoThumbnail());
        }
        return thumbnailList;
    }


    private Dialog firstTimeRecharge;

    private void FirstTimeRechargeDialog(RechargePlanResponseNew.Data firstRecharge) {
        Log.e("FirstTimeRechargeDialog", "FirstTimeRechargeDialog: ViewProfile");
        //   RechargePlanResponse.Data selcted = new RechargePlanResponse.Data(7, 70, 1, 210, 100, 7, true);
        RechargePlanResponseNew.Data selcted = firstRecharge;


        firstTimeRecharge = new Dialog(ViewProfileMet.this);
        firstTimeRecharge.setContentView(R.layout.descounted_recharge_popup);
        firstTimeRecharge.setCancelable(true);
        firstTimeRecharge.setCanceledOnTouchOutside(true);
        firstTimeRecharge.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        RelativeLayout container = firstTimeRecharge.findViewById(R.id.container);
        Button btn_buynow = firstTimeRecharge.findViewById(R.id.btn_buynow);

        TextView coins = firstTimeRecharge.findViewById(R.id.tv_coins);
        TextView price = firstTimeRecharge.findViewById(R.id.tv_price);

        coins.setText("" + selcted.getPoints());
        price.setText("₹" + selcted.getAmount());


      /*  if (firstTimeRecharge != null) {
            Log.e("ViewProfile", "FirstTimeRechargeDialog: not null ");
        } else {
            Log.e("ViewProfile", "FirstTimeRechargeDialog: null ");
        }
        */

        btn_buynow.setOnClickListener(view -> {
            firstTimeRecharge.dismiss();
            //Go to payment activity
            Intent intent = new Intent(ViewProfileMet.this, SelectPaymentMethod.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_plan", selcted);
            intent.putExtras(bundle);
            startActivity(intent);

        });

        container.setOnClickListener(view -> {
            //Go to payment activity
            firstTimeRecharge.dismiss();

        });

        if (!((ViewProfileMet.this).isFinishing())) {
            firstTimeRecharge.show();
        }


    }


    public String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month - 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        int ageInt = age;
        return Integer.toString(ageInt);
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








 /*   private void initiateMessageWithCall(String receiverId, String userName, String userId, String uniqueId, String isFreeCall, String profilePic, String callType) {

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
            messageObject.put("isMessageWithCall", "yes");
            messageObject.put("CallMessageBody", OtherInfoWithCall.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ZIMTextMessage zimMessage = new ZIMTextMessage();
        zimMessage.message = messageObject.toString();

  *//*      ZegoZIMManager.getInstance().zim.sendPeerMessage(zimMessage, receiverId, (message, errorInfo) -> {

            Log.e("CALLBACK", "message " + message + "   errorinfo  " + errorInfo.getMessage());

        });
*//*
    }*/


}
