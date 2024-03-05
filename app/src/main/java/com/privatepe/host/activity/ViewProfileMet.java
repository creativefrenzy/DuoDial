package com.privatepe.host.activity;

import static com.privatepe.host.utils.Constant.GET_FIRST_TIME_RECHARGE_LIST;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
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
import android.os.Parcelable;
import android.support.v4.media.session.PlaybackStateCompat;
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

import com.bumptech.glide.Glide;
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
import com.privatepe.host.Inbox.InboxDetails;
import com.privatepe.host.Interface.ViewProfIleImagePosition;
import com.privatepe.host.R;
import com.privatepe.host.Zego.VideoChatZegoActivityMet;
import com.privatepe.host.adapter.RateCountDisplayAdapter;
import com.privatepe.host.adapter.VideoStatusAdapter;
import com.privatepe.host.adapter.metend.AlbumAdapterViewProfileMet;
import com.privatepe.host.adapter.metend.GiftCountDisplayAdapterMet;
import com.privatepe.host.adapter.metend.ProfilePagerAdapterMet;
import com.privatepe.host.databinding.ActivityViewProfileBinding;
import com.privatepe.host.databinding.ActivityViewProfileMetBinding;
import com.privatepe.host.dialogs.InsufficientCoins;
import com.privatepe.host.dialogs.ReportDialog;
import com.privatepe.host.recycler.ProfileAdapter;
import com.privatepe.host.response.metend.AdapterRes.UserListResponseMet;
import com.privatepe.host.model.UserListResponseNew.UserListResponseNewData;
import com.privatepe.host.response.ProfileVideoResponse;
import com.privatepe.host.response.metend.AddRemoveFavResponse;
import com.privatepe.host.response.metend.DiscountedRecharge.DiscountedRechargeResponse;
import com.privatepe.host.response.metend.FirstTimeRechargeListResponse;
import com.privatepe.host.response.metend.GenerateCallResponce.GenerateCallResponce;
import com.privatepe.host.response.metend.RechargePlan.RechargePlanResponseNew;
import com.privatepe.host.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.privatepe.host.response.metend.UserListResponseNew.FemaleImage;
import com.privatepe.host.response.metend.UserListResponseNew.ResultDataNewProfile;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.status_videos.ActivityStatus;
import com.privatepe.host.utils.AppLifecycle;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.NetworkCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.privatepe.host.model.UserListResponseNew.GetRatingTag;
import com.privatepe.host.utils.SessionManager;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSignalingManager;

public class ViewProfileMet  extends BaseActivity implements ApiResponseInterface, ViewProfIleImagePosition {

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
    //ArrayList<GiftDetails> giftDetailsArrayList;
   // ArrayList<Result> resultArrayList;
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
    RelativeLayout li_video_status;
    TextView text_Video;
    private String dp;

    DatabaseReference firebaseref;
    public static ProfileAdapter adapterProfileImages;
    Intent intentExtendedProfile;


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
        li_video_status = findViewById(R.id.li_video_status);
        text_Video = findViewById(R.id.text_Video);
        //giftDetailsArrayList = new ArrayList<>();
        //resultArrayList = new ArrayList<>();
        //array list inisilise object for
        ratingArrayList = new ArrayList<>();

//        giftCountDisplayAdapter = new GiftCountDisplayAdapterMet(getApplicationContext(), giftDetailsArrayList, resultArrayList);
//        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        rv_giftshow.setLayoutManager(new GridLayoutManager(this, 5));
//        rv_giftshow.setAdapter(giftCountDisplayAdapter);


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
            statusCheck(view);


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
    private void statusCheck(View view){
        FirebaseDatabase.getInstance().getReference().child("Users").child(userData.get(0).getProfileId().toString()).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.e("chejadsfa", snapshot.getValue(String.class));
                    if("Live".equalsIgnoreCase(snapshot.getValue(String.class))) {
                    callType = "video";
                    // apiManager.getRemainingGiftCardFunction();
                    apiManager.generateCallRequestZ(userData.get(0).getProfileId(), String.valueOf(System.currentTimeMillis()), "0", callRate,
                            Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                    view.setEnabled(false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setEnabled(true);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(ViewProfileMet.this, "User is not Live", Toast.LENGTH_SHORT).show();
                }
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    @Override
    public void setImagePositionView(int position) {
        Log.d("1234tesf", "setImagePositionView : " + position);
        if (intentExtendedProfile != null) {
            intentExtendedProfile.putExtra("positionOnDisplay", position);
            startActivity(intentExtendedProfile);
        } else {
            Toast.makeText(ViewProfileMet.this, "NO INTERNET", Toast.LENGTH_SHORT).show();
        }
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
            new Handler().postDelayed(() -> binding.nonFavourite.setText("Following"), 500);
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

//        if (ServiceCode == Constant.GET_GIFT_COUNT) {
//            GiftCountResult rsp = (GiftCountResult) response;
//
//            try {
//                resultArrayList.addAll(rsp.getResult());
//                if (resultArrayList.size() == 0) {
//                    binding.tvGifrecmsg.setVisibility(View.GONE);
//                    rv_giftshow.setVisibility(View.GONE);
//                } else {
//                    for (int i = 0; i < rsp.getResult().size(); i++) {
//                        giftDetailsArrayList.add(rsp.getResult().get(i).getGiftDetails());
//                        //    Log.e("receviedGiftfemale", new Gson().toJson(giftDetailsArrayList));
//                    }
//                    //giftDetailsArrayList.add(rsp.getResult().get(0).getGiftDetails());
//                    giftCountDisplayAdapter.notifyDataSetChanged();
//                }
//            } catch (Exception e) {
//
//            }
//
//        }

        if (ServiceCode==Constant.NEW_GENERATE_AGORA_TOKENZ)
        {
            GenerateCallResponce rsp = (GenerateCallResponce) response;
            String profileId=String.valueOf(userData.get(0).getProfileId());

            Log.e("checkkkk",""+profileId);

            V2TIMManager v2TIMManager = V2TIMManager.getInstance();


            //Log.e("NEW_GENERATE_AGORA_TOKENZ", "isSuccess: " + new Gson().toJson(rsp));

            long walletBalance = rsp.getResult().getPoints();
            int CallRateInt = callRate;
            long talktime = (walletBalance / CallRateInt) * 60*1000L;
            Log.e("AUTO_CUT_TESTZ", "CallNotificationDialog: " + talktime);
            long canCallTill = talktime - 2000;
            Log.e("AUTO_CUT_TESTZ", "CallNotificationDialog: canCallTill " + canCallTill);
            String profilePic = new SessionManager(ViewProfileMet.this).getUserProfilepic();
            HashMap<String, String> user = new SessionManager(ViewProfileMet.this).getUserDetails();
            Intent intent = new Intent(ViewProfileMet.this, VideoChatZegoActivityMet.class);
            intent.putExtra("TOKEN", "demo");
            intent.putExtra("ID", profileId);
            intent.putExtra("UID", String.valueOf(userId));
            intent.putExtra("CALL_RATE", String.valueOf(callRate));
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
            intent.putExtra("receiver_name",  userData.get(0).getName());
            intent.putExtra("converID", "convId");
            intent.putExtra("receiver_image",  userData.get(0).getFemaleImages().get(0).getImageName());
            //Log.e("NEW_GENERATE_AGORA_TOKENZ", "isSuccess: go to videoChatActivity");


            JSONObject jsonResult = new JSONObject();
            try {
                jsonResult.put("type", "callrequest");

                jsonResult.put("caller_name", new SessionManager(ViewProfileMet.this).getName());
                jsonResult.put("userId", new SessionManager(ViewProfileMet.this).getUserId());
                jsonResult.put("callerProfileId", new SessionManager(ViewProfileMet.this).getUserId());

                jsonResult.put("unique_id", rsp.getResult().getUnique_id());
                jsonResult.put("caller_image", new SessionManager(ViewProfileMet.this).getUserProfilepic());
                jsonResult.put("callRate", String.valueOf(callRate));
                jsonResult.put("isFreeCall", "false");
                jsonResult.put("totalPoints", new SessionManager(ViewProfileMet.this).getUserWallet());
                jsonResult.put("remainingGiftCards", "0");
                jsonResult.put("freeSeconds", "0");




            } catch (JSONException e) {
                e.printStackTrace();
            }
            String msg2 = jsonResult.toString();
            V2TIMSignalingManager v2TIMSignalingManager=V2TIMManager.getSignalingManager();
            String inviteId=   v2TIMSignalingManager.invite(  profileId, msg2, true, null, 20, new V2TIMCallback() {
                @Override
                public void onSuccess() {
                    Log.e("listensdaa","Yes11 Invitesent"+profileId);
                    startActivity(intent);

                }

                @Override
                public void onError(int i, String s) {
                    Log.e("listensdaa","Yes22 "+s);

                }
            });
            Log.e("chdakdaf","yes "+inviteId);
            intent.putExtra("inviteId",inviteId);


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
            com.privatepe.host.response.metend.UserListResponseNew.UserListResponseNewData rsp = (com.privatepe.host.response.metend.UserListResponseNew.UserListResponseNewData) response;
            //userData = (ResultDataNewProfile) rsp.getResult();

            userData.addAll(rsp.getResult());
            // binding.setResponse(userData);
            for (int i = 0; i < userData.get(0).getFemaleImages().size(); i++) {
                if (userData.get(0).getFemaleImages().get(i).getIsProfileImage() == 1) {
                    Glide.with(this).load(userData.get(0).getFemaleImages().get(i).getImageName()).into(binding.profileImageImg);
                }
            }
            try {
                binding.tvFollowers.setText(String.valueOf(userData.get(0).getFavoriteCount()));
            }catch (Exception e) {

            }
            adapterProfileImages = new ProfileAdapter(this, rsp.getResult().get(0).getFemaleImages(), "ViewProfileMet", ViewProfileMet.this);
            binding.profileImagesRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.profileImagesRecView.setAdapter(adapterProfileImages);

            intentExtendedProfile = new Intent(ViewProfileMet.this, ProfileImagesView.class);
            intentExtendedProfile.putParcelableArrayListExtra("femaleImageList", (ArrayList<? extends Parcelable>) rsp.getResult().get(0).getFemaleImages());

            videostatusList.addAll(rsp.getResult().get(0).getFemaleVideo());
            if (videostatusList.size() > 0) {
                initializePlayer(videostatusList.get(0).getVideoName(), videostatusList.get(0).getVideoThumbnail());
            }
            binding.profileImageImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.e("checkLogs",new Gson().toJson(rsp.getResult().get(0).getFemaleImages()));

                    intentExtendedProfile.putExtra("positionOnDisplay", 0);
                    startActivity(intentExtendedProfile);

                }
            });


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
            }else {
                binding.nonFavourite.setVisibility(View.VISIBLE);
                binding.nonFavourite.setEnabled(false);
            }
            /*else {
                //binding.nonFavourite.setText("UnFollow");
                //binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_offline_background);
                isFavourite = 0;
            }*/


            dp = rsp.getResult().get(0).getFemaleImages().get(0).getImageName();
            Log.e("vvvvvt", "isSuccess: " + dp);

            int totalvideos = rsp.getResult().get(0).getFemaleVideo().size();
            //videostatusList.addAll(rsp.getResult().get(0).getFemaleVideo());


       /*     for (int i=0;i<totalvideos;i++)
            {
                Log.e("vvvvvvvd", "isSuccess: videoname "+rsp.getResult().get(0).getFemaleVideo().get(i).getVideoName() );
                videostatusList.add(rsp.getResult().get(0).getFemaleVideo().get(i).getVideoName());
            }
*/
//            if (totalvideos > 0) {
//                binding.liVideoStatus.setVisibility(View.VISIBLE);
//
//            } else {
//
//                binding.liVideoStatus.setVisibility(View.GONE);
//            }
//            if (videostatusList.size() > 2) {
//                li_video_status.setVisibility(View.VISIBLE);
//                text_Video.setVisibility(View.VISIBLE);
//                videoStatusDisplayAdapter = new VideoStatusAdapter(getApplicationContext(), videostatusList, dp, ViewProfileMet.this);
//                rv_videostatus.setLayoutManager(new GridLayoutManager(this, 5));
//                rv_videostatus.setAdapter(videoStatusDisplayAdapter);
//
//            }

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

            callRate = userData.get(0).getCallPrice();

            //apiManager.getVideoForProfile(String.valueOf(userId));

           // apiManager.getGiftCountForHost(String.valueOf(userId));
            //call api getRateCountForHost 6/5/21 send host profile_id here
            apiManager.getRateCountForHost(String.valueOf(userId));


            binding.tvCallMePrice.setText( "\u20B9"+String.valueOf(callRate));

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
    private ExoPlayer player;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    private void initializePlayer(String videoUrl, String thumbnail) {
        if (thumbnail != null || videoUrl != null) {
            binding.shortVideoStatus.setVisibility(View.VISIBLE);
            if (thumbnail != null) {
                Glide.with(this).load(thumbnail).placeholder(R.drawable.ic_no_image).into(binding.exoplayerViewImageView);
            }
            if (videoUrl != null) {
                player = new ExoPlayer.Builder(this).build();
                binding.exoplayerView.setPlayer(player);
                MediaItem mediaItem = MediaItem.fromUri(videoUrl);
                player.setMediaItem(mediaItem);
                player.prepare();
                player.setRepeatMode(Player.REPEAT_MODE_ONE);
                player.play();
                player.setVolume(0);
                player.addListener(new Player.Listener() {
                    @Override
                    public void onPlaybackStateChanged(int playbackState) {
                        Player.Listener.super.onPlaybackStateChanged(playbackState);
                        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
                            //do something
                            binding.exoplayerViewImageView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
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
