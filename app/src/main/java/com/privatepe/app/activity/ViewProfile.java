package com.privatepe.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
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
import com.privatepe.app.R;
/*import com.privatepe.app.ZegoExpress.zim.ZimManager;*/
import com.privatepe.app.adapter.AlbumAdapterViewProfile;
import com.privatepe.app.adapter.GiftCountDisplayAdapter;
import com.privatepe.app.adapter.ProfilePagerAdapter;
import com.privatepe.app.adapter.RateCountDisplayAdapter;
import com.privatepe.app.adapter.VideoStatusAdapter;
import com.privatepe.app.databinding.ActivityViewProfileBinding;
import com.privatepe.app.dialogs.ReportDialog;
import com.privatepe.app.model.UserListResponseNew.FemaleImage;
import com.privatepe.app.model.UserListResponseNew.GetRatingTag;

import com.privatepe.app.model.UserListResponseNew.ResultDataNewProfile;
import com.privatepe.app.model.UserListResponseNew.UserListResponseNewData;
import com.privatepe.app.recycler.ProfileAdapter;
import com.privatepe.app.response.DisplayGiftCount.GiftCountResult;
import com.privatepe.app.response.DisplayGiftCount.GiftDetails;
import com.privatepe.app.response.DisplayGiftCount.Result;
import com.privatepe.app.response.ProfileVideoResponse;
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
import java.util.List;
import java.util.Map;

//import im.zego.zim.enums.ZIMErrorCode;


public class ViewProfile extends BaseActivity implements ApiResponseInterface {
    int isFavourite = 0;
    ApiManager apiManager;
    int userId, callRate;
    int hostId;
    ActivityViewProfileBinding binding;
    ArrayList<ResultDataNewProfile> userData = new ArrayList<>();

    long walletBalance;
    private NetworkCheck networkCheck;
    private String convId = "";
    //giftcount
    RecyclerView rv_giftshow;
    GiftCountDisplayAdapter giftCountDisplayAdapter;
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
    AlbumAdapterViewProfile adapter_album;
    AppLifecycle appLifecycle;
   // private ZimManager zimManager;

    private boolean isFreeCall = false;
    private ArrayList<ProfileVideoResponse> videostatusList = new ArrayList<>();
    private VideoStatusAdapter videoStatusDisplayAdapter;
    private RecyclerView rv_videostatus;
    private String dp;

    DatabaseReference firebaseref;
    public static ProfileAdapter adapterProfileImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_profile);
        binding.setClickListener(new EventHandler(this));
        networkCheck = new NetworkCheck();
       // zimManager = ZimManager.sharedInstance();

      //  binding.videoChat.setBackgroundResource(R.drawable.main_button_bg);

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
        hostLevel = String.valueOf(getIntent().getSerializableExtra("level"));


      /*  if (hostLevel.equals("0")) {
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
        }
*/
        rv_giftshow = findViewById(R.id.rv_giftshow);
        rv_videostatus = findViewById(R.id.rv_videoShow);
        //rating recyclerview for show rating
        rv_tagshow = findViewById(R.id.rv_rateShow);

        String t = "Lv ." + hostLevel;
        binding.levelCharm.setText(t);

        giftDetailsArrayList = new ArrayList<>();
        resultArrayList = new ArrayList<>();
        //array list inisilise object for
        ratingArrayList = new ArrayList<>();

        giftCountDisplayAdapter = new GiftCountDisplayAdapter(getApplicationContext(), giftDetailsArrayList, resultArrayList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_giftshow.setLayoutManager(new GridLayoutManager(this, 5));
        rv_giftshow.setAdapter(giftCountDisplayAdapter);

        binding.userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("id", binding.userId.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(ViewProfile.this, "Copied", Toast.LENGTH_SHORT);
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

        FlexboxLayoutManager flexLayout = new FlexboxLayoutManager(ViewProfile.this);
        rv_tagshow.setLayoutManager(flexLayout);
        rateCountDisplayAdapter = new RateCountDisplayAdapter(ViewProfile.this, ratingArrayList);
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

        apiManager.getProfileData(String.valueOf(hostIdFemale), "");
        Log.e("activitysss", "onCreate: viewProfile UserID " + hostIdFemale);

        //addRemoveFav();

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

        customErrorToast();




     /*   if (CheckPermission()) {
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
        */





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

    private LinearLayout toast;
    private void customErrorToast() {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.unable_to_call_lay, (ViewGroup) toast);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 30);
        toast.setView(layout);
        toast.show();
    }

    private boolean CheckPermission() {

        final boolean[] isPermissionGranted = new boolean[1];

        Dexter.withActivity(ViewProfile.this).withPermissions(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("onPermissionsChecked", "onPermissionsChecked: ");

                if (report.areAllPermissionsGranted()) {
                    Log.e("onPermissionsChecked", "all permission granted");
                    isPermissionGranted[0] = true;
                } else {
                    isPermissionGranted[0] = false;
                    Toast.makeText(ViewProfile.this, "To use this feature Camera and Audio permissions are must.You need to allow the permissions", Toast.LENGTH_SHORT).show();
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



    public void goToVideoStatus(int adapterPosition) {
        // Intent vsIntent = new Intent(ViewProfile.this, ActivityStatus.class);
        // vsIntent.putExtra("videonum", adapterPosition);
        Log.e("VideoStatusAdapter1", "onClick: " + adapterPosition);

        Log.e("VideoStatusAdapter1", "goToVideoStatus: " + new Gson().toJson(userData.get(0)));

        Intent intent = new Intent(ViewProfile.this, ActivityStatus.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("inWhichActivity","ViewProfile");
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

       /* int totalvideos =userData.get(0).getFemaleVideo().size();

            for (int i=0;i<totalvideos;i++)
            {
                Log.e("vvvvvvvd", "isSuccess: videoname "+userData.get(0).getFemaleVideo().get(i).getVideoName() );
                videostatusList.add(rsp.getResult().get(0).getFemaleVideo().get(i).getVideoName());
            }*/

        Log.e("viewProf1", "goToVideoStatus: list " + userData.get(0).getFemaleVideo().size());
        ArrayList<String> list2 = getVideolinksList(userData.get(0));
        Log.e("viewProf1", "goToVideoStatus: list " + getVideolinksList(userData.get(0)));
        intent.putExtra("allListData", new Gson().toJson(userData.get(0).getFemaleVideo()));
        Log.e("onSingleTap11", "onSingleTap: " + "list size  " + list2);
        intent.putStringArrayListExtra("resoureList", list2);
        intent.putStringArrayListExtra("thumbnailList", getThumbnailList(userData.get(0)));

        startActivity(intent);

        // Log.e("VIEW_PROFILE_TEST", "onSingleTap: id  "+list.get(position).getId()+" profileId  "+list.get(position).getProfile_id()+" level  "+list.get(position).getLevel() );
        // startActivity(vsIntent);


    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void addToFav() {
            addRemoveFav();
          //  apiManager.doFavourite(userId);
            Log.e("newUserId", userId + "");
        }

        public void onBack() {
            onBackPressed();
        }

        public void gotoChatConversation() {
            customErrorToast();
            //Here pass userId and callRate send data on InboxDetail activity. by Kalpesh Sir..
        /*    if (userData.size() > 0) {
                Intent intent = new Intent(ViewProfile.this, InboxDetails.class);
                intent.putExtra("profileName", userData.get(0).getName());
                intent.putExtra("user_image", userData.get(0).getFemaleImages().get(0).getImageName());
                intent.putExtra("chatProfileId", String.valueOf(userData.get(0).getProfileId()));
                intent.putExtra("contactId", userData.get(0).getId());
                intent.putExtra("mode", true);
                intent.putExtra("channelName", "zeeplive662730982537574");
                intent.putExtra("usercount", 0);
                intent.putExtra("unreadMsgCount", 0);
             *//*   intent.putExtra("receiver_id", String.valueOf(userData.get(0).getProfileId()));
            intent.putExtra("newParem", String.valueOf(userData.get(0).getId()));
            intent.putExtra("receiver_name", userData.get(0).getName());
            intent.putExtra("user_id", String.valueOf(userId));
            //pass  Unique_id for gift Sends 4/5/21
            //intent.putExtra("Unique_id", String.valueOf(unique_id));
            intent.putExtra("call_rate", String.valueOf(callRate));

            *//**//*Intent intent = new Intent(ViewProfile.this, InboxDetails.class);
            intent.putExtra("receiver_id", String.valueOf(userData.getProfile_id()));
            intent.putExtra("receiver_name", userData.getName());*//**//*

            if (userData.get(0).getFemaleImages() == null || userData.get(0).getFemaleImages().size() == 0) {
                intent.putExtra("receiver_image", "empty");
            } else {
                intent.putExtra("receiver_image", userData.get(0).getFemaleImages().get(0).getImageName());
            }*//*
                startActivity(intent);
            }

*/
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
            new ReportDialog(ViewProfile.this, String.valueOf(userId));
        }
    }

    public void addRemoveFav() {
        Intent myIntent = new Intent("FBR");
        myIntent.putExtra("action", "reload");
        this.sendBroadcast(myIntent);

        if (isFavourite == 0) {
            //for hide follow button
            // binding.nonFavourite.setText("Follow");
            // binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_fallow_background);
            isFavourite = 1;
        } else {
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

   // private InsufficientCoins insufficientCoins;

    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {



        } else {
            Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {


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


        //Show female profile data for male 10/5/21
        if (ServiceCode == Constant.GET_PROFILE_DATA) {
            // UserListResponse.Data userData;
            UserListResponseNewData rsp = (UserListResponseNewData) response;
            //userData = (ResultDataNewProfile) rsp.getResult();

            userData.addAll(rsp.getResult());
            // binding.setResponse(userData);
            for (int i=0;i<userData.size();i++){
                if(userData.get(0).getFemaleImages().get(i).getIsProfileImage()==1){
                    Glide.with(this).load(userData.get(0).getFemaleImages().get(i).getImageName()).into(binding.profileImageImg);
                }
            }
            adapterProfileImages = new ProfileAdapter(this, rsp.getResult().get(0).getFemaleImages(),"ViewProfile");
            binding.profileImagesRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            binding.profileImagesRecView.setAdapter(adapterProfileImages);

            ArrayList<String> myList = new ArrayList<String>();
            for(int i = 0; i < rsp.getResult().get(0).getFemaleImages().size(); i++){
                myList.add(rsp.getResult().get(0).getFemaleImages().get(i).getImageName());
            }

            binding.profileImageImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewProfile.this,ProfileImagesView.class);
                    intent.putParcelableArrayListExtra("femaleImageList", (ArrayList<? extends Parcelable>) rsp.getResult().get(0).getFemaleImages());
                    startActivity(intent);

                }
            });


            try {
                isFavourite = userData.get(0).getFavoriteByYouCount();
            } catch (Exception e) {

            }

            if (isFavourite == 0) {
                //hide for follow button
                //binding.nonFavourite.setText("Follow");
                //binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_fallow_background);
                isFavourite = 1;
            } else {
                //binding.nonFavourite.setText("UnFollow");
                //binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_offline_background);
                isFavourite = 0;
            }


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
            videoStatusDisplayAdapter = new VideoStatusAdapter(getApplicationContext(), videostatusList, dp, ViewProfile.this);
            rv_videostatus.setLayoutManager(new GridLayoutManager(this, 5));
            rv_videostatus.setAdapter(videoStatusDisplayAdapter);
            if(videostatusList.size()>0) {
                initializePlayer(videostatusList.get(0).getVideoName(),videostatusList.get(0).getVideoThumbnail());
            }


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
            ProfilePagerAdapter adapter = new ProfilePagerAdapter(this, userData.get(0).getFemaleImages(), true);
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
            adapter_album = new AlbumAdapterViewProfile(ViewProfile.this, albumList, true);
            rv_albumShow.setAdapter(adapter_album);

            new TabLayoutMediator(binding.indicatorDot, binding.viewpager,
                    (tab, position) -> {
                        // tab.setText(" " + (position + 1));
                    }
            ).attach();

            // Hide video call feature for female user
          /*  if (new SessionManager(this).getGender().equals("female")) {
                binding.videoChat.setVisibility(View.GONE);
            } else {
            }*/

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


    }
    private ExoPlayer player;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(player!=null){
            player.release();
        }
    }
    private void initializePlayer(String videoUrl, String thumbnail) {
        if(thumbnail != null || videoUrl!=null){
            binding.shortVideoStatus.setVisibility(View.VISIBLE);
            if(thumbnail!=null){
                Glide.with(this).load(thumbnail).placeholder(R.drawable.ic_no_image).into(binding.exoplayerViewImageView);
            }
            if(videoUrl !=null){
                player= new ExoPlayer.Builder(this).build();
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
/*

    private void FirstTimeRechargeDialog(RechargePlanResponseNew.Data firstRecharge) {
        Log.e("FirstTimeRechargeDialog", "FirstTimeRechargeDialog: ViewProfile");
        //   RechargePlanResponse.Data selcted = new RechargePlanResponse.Data(7, 70, 1, 210, 100, 7, true);
        RechargePlanResponseNew.Data selcted = firstRecharge;


        firstTimeRecharge = new Dialog(ViewProfile.this);
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


        btn_buynow.setOnClickListener(view -> {
            //Go to payment activity
            Intent intent = new Intent(ViewProfile.this, SelectPaymentMethod.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_plan", selcted);
            intent.putExtras(bundle);
            startActivity(intent);
            firstTimeRecharge.dismiss();
        });

        container.setOnClickListener(view -> {
            //Go to payment activity
            firstTimeRecharge.dismiss();

        });

        firstTimeRecharge.show();
    }
*/


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
