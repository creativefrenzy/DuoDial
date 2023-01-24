package com.klive.app.status_videos;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.klive.app.R;
import com.klive.app.Zego.VideoChatZegoActivity;
import com.klive.app.ZegoExpress.zim.ZimManager;
import com.klive.app.activity.RecordStatusActivity;
import com.klive.app.activity.ViewProfile;
import com.klive.app.dialogs.MyProgressDialog;
import com.klive.app.dialogs.ReportDialog;
import com.klive.app.model.UserListResponse;
import com.klive.app.model.UserListResponseNew.ResultDataNewProfile;
import com.klive.app.model.UserListResponseNew.UserListResponseNewData;
import com.klive.app.model.VideoStatus.StatusProgressView;
import com.klive.app.response.DataFromProfileId.DataFromProfileIdResponse;
import com.klive.app.response.DataFromProfileId.DataFromProfileIdResult;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.sqlite.StatusDBHandler;
import com.klive.app.status_videos.model.VideoLinkModel;
import com.klive.app.utils.BaseActivity;
import com.klive.app.utils.Constant;
import com.klive.app.utils.NetworkCheck;
import com.klive.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import im.zego.zim.enums.ZIMErrorCode;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityStatus extends BaseActivity implements StatusProgressView.StoriesListener, View.OnClickListener, ApiResponseInterface {

    ArrayList<String> resources = new ArrayList<>();
    private long duration = 0L;
    private long pressTime = 0L;
    private long limit = 500L;
    private StatusProgressView statusProgressView;

    private int counter = 0;

    SharedPreferences sharedPreferences;
    ImageView iv_close_top_right, iv_report_status_right, iv_delete_right;
    CircleImageView iv_host;
    TextView tv_host_name, tv_host_age, tv_host_area;

    private int Image;
    private String Name, Age, Area;
    private LinearLayout toast;
    private NetworkCheck networkCheck;
    private ImageView ivThumbnail;
    String UserID, UserName, ProfilePic, Location, Level, mAge;
    int callRate;
    private boolean isFreeCall = false;

    DatabaseReference firebaseRefrence;

    ArrayList<ResultDataNewProfile> userData = new ArrayList<>();

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                 /* pressTime = System.currentTimeMillis();
                    statusProgressView.pause();*/
                    return false;
                case MotionEvent.ACTION_UP:
                /*  long now = System.currentTimeMillis();
                    statusProgressView.resume();
                    return limit < now - pressTime;*/
            }
            return false;
        }
    };

    private View img_level;
    private ApiManager apiManager;
    private long walletBalance;
    private int userId;
    private int hostId;
    private String convId = "";
    private ZimManager zimManager;
    private String hostIdFemale;
    private String HostProfileId;
    private StatusDBHandler statusDBHandler;

    private ArrayList<VideoLinkModel> localListFromDB;
    private boolean isVideoPresentInLocal = true;
    private ArrayList<String> localVideoLinkList;
    private TextView tv_level;

    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;
    ArrayList<UserListResponse.ProfileVideo> arrayList;
    private String clickedUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_status);
        networkCheck = new NetworkCheck();
        apiManager = new ApiManager(this, this);
        zimManager = ZimManager.sharedInstance();
        sharedPreferences = getSharedPreferences("VideoApp", Context.MODE_PRIVATE);
        statusDBHandler = new StatusDBHandler(this);

        iv_host = findViewById(R.id.iv_host);
        iv_close_top_right = findViewById(R.id.iv_close_top_right);
        iv_delete_right = findViewById(R.id.iv_delete_right);
        iv_report_status_right = findViewById(R.id.iv_report_status_right);
        ivThumbnail = findViewById(R.id.ivThumbnail);

        statusProgressView = findViewById(R.id.stories);

        tv_host_name = findViewById(R.id.tv_host_name);
        tv_host_age = findViewById(R.id.tv_host_age);
        tv_host_area = findViewById(R.id.tv_host_area);

        img_level = findViewById(R.id.img_level);

        tv_level = findViewById(R.id.tv_level);

        exoPlayerView = findViewById(R.id.exoplayer_view);
        //getLocationOnScreen(iv_delete_right);
        if (getIntent() != null) {
            Log.e("which activity==>", getIntent().getStringExtra("inWhichActivity"));


            if (getIntent().getStringExtra("inWhichActivity").equalsIgnoreCase("ProfileVideoAdapter")) {
                UserID = getIntent().getStringExtra("id");
                String listData = getIntent().getStringExtra("allListData");

                ArrayList<UserListResponse.ProfileVideo> yourArray = new Gson().fromJson(listData.toString(), new TypeToken<List<UserListResponse.ProfileVideo>>() {
                }.getType());
                arrayList = yourArray;

                ivThumbnail.setVisibility(View.VISIBLE);
                clickedUrl = getIntent().getStringExtra("clickedUrl");
                resources = getIntent().getStringArrayListExtra("resoureList");
                Log.e("clicked url===", clickedUrl + "");
                SessionManager sessionManager = new SessionManager(ActivityStatus.this);
                UserName = sessionManager.getUserName();
                HostProfileId = sessionManager.getUserId();
                Log.e("clicked our ids===", UserName + "=====" + HostProfileId + "=====" + UserID);
                mAge = sessionManager.getUserAge();
                ProfilePic = sessionManager.getUserProfilepic();
                Level = sessionManager.getUserLevel();
                Location = sessionManager.getUserLocation();
                String thumbnailUrl = null;
                for (UserListResponse.ProfileVideo video : arrayList) {
                    if (video.getVideoUrl().equalsIgnoreCase(clickedUrl)) {
                        thumbnailUrl = video.getVideoThumbnail();
                        break;
                    }
                }
                Glide.with(ActivityStatus.this).load(thumbnailUrl).placeholder(R.drawable.ic_no_image).into(ivThumbnail);

                for (String videoUrl : resources) {
                    if (videoUrl.equalsIgnoreCase(clickedUrl)) {
                        counter = resources.indexOf(videoUrl);
                    }
                }

                // Log.e("resource==2=>", counter+"");
                ImageView iv_message = findViewById(R.id.iv_message);
                ImageView video_chat = findViewById(R.id.video_chat);
                iv_message.setEnabled(false);
                video_chat.setEnabled(false);
                iv_host.setEnabled(false);
                iv_close_top_right.setVisibility(View.GONE);
                iv_report_status_right.setVisibility(View.GONE);
                iv_delete_right.setVisibility(View.VISIBLE);
            } else {
                UserName = getIntent().getStringExtra("name");
                UserID = getIntent().getStringExtra("id");
                HostProfileId = getIntent().getStringExtra("profileId");
                // hostIdFemale = String.valueOf(getIntent().getSerializableExtra("id2"));
                ProfilePic = getIntent().getStringExtra("profile_pic");
                Location = getIntent().getStringExtra("location");
                Level = getIntent().getStringExtra("level");
                mAge = getIntent().getStringExtra("age");
                callRate = getIntent().getIntExtra("callrate", 0);
                resources = getIntent().getStringArrayListExtra("resoureList");

                ivThumbnail.setVisibility(View.VISIBLE);
                exoPlayerView.setVisibility(View.GONE);

                /*if (!getIntent().getStringExtra("videonum").equals("")) {
                    Log.e("videonumvideonum", "onCreate: " + getIntent().getStringExtra("videonum"));
                    counter = Integer.parseInt(getIntent().getStringExtra("videonum").toString());
                    if (counter > resources.size() - 1) {
                        counter = resources.size() - 1;
                    }

                } else {
                    Log.e("videonumvideonum", "onCreate: no field with videonum ");
                    counter = getVideoNumFromSharedPref(UserID);
                    if (counter > resources.size() - 1) {
                        counter = resources.size() - 1;
                    }

                }*/

                Log.e("activitysss", "onCreate: ACTIVITYSTATUS UserID " + UserID);
                apiManager.getProfileData(String.valueOf(UserID), "");

                Log.e("testcount", "onCreate: " + "counter   " + counter);
                // counter = 0;

                Log.e("testcount", "onCreate: " + "counter1   " + counter);
            }


            iv_delete_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDeleteOption();
                }
            });


            iv_close_top_right.setOnClickListener(this);
            iv_report_status_right.setOnClickListener(this);

            iv_host.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    apiManager.getProfileIdData(HostProfileId);
                }
            });


            if (!ProfilePic.equals("")) {
                Glide.with(this).load(ProfilePic).apply(new RequestOptions().placeholder(R.drawable.female_placeholder).error(R.drawable.female_placeholder)).into(iv_host);
            } else {
                Glide.with(this).load(R.drawable.female_placeholder).apply(new RequestOptions()).into(iv_host);
            }

            tv_host_name.setText(UserName);
            tv_host_age.setText(mAge);
            tv_host_area.setText(Location);

            String levelString = "" + Level;

            tv_level.setText(levelString);

            statusProgressView.setStoriesCount(resources.size());
            statusProgressView.setStoryDuration(15000L);
            statusProgressView.setStoriesListener(this);
            statusProgressView.startStories(counter);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    statusProgressView.pause();
                }
            }, 100);


            if (resources.size() == 1) {
                Log.e("resources size===", resources.size() + "");
            } else {
                View reverse = findViewById(R.id.reverse);
                reverse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusProgressView.reverse();
                    }
                });
                reverse.setOnTouchListener(onTouchListener);

                View skip = findViewById(R.id.skip);
                skip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusProgressView.skip();
                    }
                });
                skip.setOnTouchListener(onTouchListener);
            }
        }
    }

    private void openDeleteOption() {

        Dialog notifyDialog = new Dialog(ActivityStatus.this);
        notifyDialog.setContentView(R.layout.delete_menu);
        notifyDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        notifyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = notifyDialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wmlp.x = 100;   //x position
        wmlp.y = 100;   //y position
        notifyDialog.setCancelable(true);
        notifyDialog.show();
        TextView tvDelete = (TextView) notifyDialog.findViewById(R.id.tvDelete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDialog.dismiss();

                Integer profileVideoId = null;
                for (UserListResponse.ProfileVideo video : arrayList) {
                    if (video.getVideoUrl().equalsIgnoreCase(resources.get(counter))) {
                        profileVideoId = video.getId();
                        break;
                    }
                }
                if (profileVideoId != null)
                    new ApiManager(ActivityStatus.this, ActivityStatus.this).deleteProfileVideo(String.valueOf(profileVideoId));

            }
        });
    }

    private void PlayWithExoplayer(Uri videoURL) {
        try {

            closeExoPlayer();

            exoPlayer = ExoPlayerFactory.newSimpleInstance(ActivityStatus.this, new DefaultTrackSelector());
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL), dataSourceFactory, extractorsFactory, null, null);
            MediaSource mediaSource = new ExtractorMediaSource(videoURL, new DefaultDataSourceFactory(this, "ua"), new DefaultExtractorsFactory(), null, null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);


            exoPlayer.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {
                    Log.e("EXOPLAYER_LISTENER", "onTimelineChanged: manifest " + manifest);
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                    Log.e("EXOPLAYER_LISTENER", "onTracksChanged: " + trackSelections);
                }

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    Log.e("EXOPLAYER_LISTENER", "onLoadingChanged: " + isLoading);
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Log.e("EXOPLAYER_LISTENER", "onPlayerStateChanged: " + playbackState);

                    if (playbackState == exoPlayer.STATE_READY) {
                        /*if (progressDialog != null) {   //naval
                            progressDialog.dismiss();
                        }*/

                        viewGoneAnimator(ivThumbnail);

                        exoPlayerView.setVisibility(View.VISIBLE);

                        statusProgressView.resume();

                    }

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Log.e("EXOPLAYER_LISTENER", "onPlayerError: " + error.getMessage());

                    viewGoneAnimator(ivThumbnail);
                    exoPlayerView.setVisibility(View.VISIBLE);
                    /*if (progressDialog != null) {  //naval
                        progressDialog.dismiss();
                    }*/

                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {


                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewGoneAnimator(final View view) {

        view.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });

    }

    public void pauseStory() {

    }

    public void resumeStory() {

    }

    private void updateDbAccordingly(ArrayList<String> listfromApi, ArrayList<String> localVideoLinkList) {

        ArrayList<String> templist = new ArrayList<>();

        templist.clear();

        for (int i = 0; i < listfromApi.size(); i++) {

            if (localVideoLinkList.contains(listfromApi.get(i))) {
                Log.e("accordingupdate", "updateDbAccordingly:    listfromApi element has in localVideoLinkList  " + listfromApi.get(i));
                templist.add(listfromApi.get(i));
            }

        }
        localVideoLinkList.removeAll(templist);
        Log.e("accordingupdate", "updateDbAccordingly: " + localVideoLinkList.size());

        for (int j = 0; j < localVideoLinkList.size(); j++) {

            String row_Id = statusDBHandler.getRowId(HostProfileId, localVideoLinkList.get(j));
            statusDBHandler.removeVideoRow(row_Id);

            Log.e("accordingupdate", "updateDbAccordingly: row removed " + row_Id + "  video removed   " + localVideoLinkList.get(j));

        }
        Log.e("accordingupdate", "updateDbAccordingly: updated list size localVideoLinkList  " + localVideoLinkList.size());
        Log.e("accordingupdate", "updateDbAccordingly: updated list localVideoLinkList  " + statusDBHandler.getVideoLinksList(HostProfileId));


    }

    private void setVideoNumToSharedPref(int num, String userID) {

        sharedPreferences.edit().putInt(userID, num).apply();

    }

    private int getVideoNumFromSharedPref(String userID) {
        int video_num = 0;

        if (sharedPreferences.contains(userID)) {
            video_num = sharedPreferences.getInt(userID, 0);
        } else {
            setVideoNumToSharedPref(0, userID);
        }
        return video_num;
    }

    @Override
    public void onNext() {

        Log.e("AC_STATUS", "onNext: " + "nextClick");
        counter++;

        if (localListFromDB == null || localListFromDB.isEmpty()) {
            Log.e("VidTest", "onNext: if " + "Play from URL");

            ivThumbnail.setVisibility(View.VISIBLE);
            exoPlayerView.setVisibility(View.GONE);
            RequestOptions requestOptions = new RequestOptions();
            Glide.with(ActivityStatus.this)
                    .load(resources.get(counter))
                    .apply(requestOptions)
                    .placeholder(R.drawable.ic_no_image)
                    .thumbnail(Glide.with(ActivityStatus.this).load(Uri.parse(resources.get(counter))))
                    .into(ivThumbnail);
            // Glide.with( ActivityStatus.this ).load(resources.get(counter)).into(ivThumbnail);

            PlayWithExoplayer(Uri.parse(resources.get(counter)));

            for (int i = 0; i < resources.size(); i++) {
                DownloadFileWithThread(resources.get(i));
            }


        } else {

            String tempPath = "";

            for (int i = 0; i < localListFromDB.size(); i++) {
                // if (localListFromDB.get(i))
                // Log.e("VidTest", "onNext: local link " + localListFromDB.get(i).getVideoLink() + "  api link  " + resources.get(counter));
                if (localListFromDB.get(i).getVideoLink().equals(resources.get(counter))) {
                    tempPath = localListFromDB.get(i).getVideoURI();
                    isVideoPresentInLocal = true;
                    break;
                } else {
                    isVideoPresentInLocal = false;
                }

            }

            if (isVideoPresentInLocal) {
                Log.e("VidTest", "onNext: else" + "Play from Local");
                ivThumbnail.setVisibility(View.VISIBLE);
                exoPlayerView.setVisibility(View.GONE);
                RequestOptions requestOptions = new RequestOptions();
                Glide.with(ActivityStatus.this)
                        .load(tempPath)
                        .apply(requestOptions)
                        .placeholder(R.drawable.ic_no_image)
                        .thumbnail(Glide.with(ActivityStatus.this).load(tempPath))
                        .into(ivThumbnail);
                PlayWithExoplayer(Uri.parse(tempPath));

            } else {
                Log.e("VidTest", "onNext: else" + "Play from URL1");

                ivThumbnail.setVisibility(View.VISIBLE);
                exoPlayerView.setVisibility(View.GONE);
                // Glide.with( ActivityStatus.this ).load(resources.get(counter)).into(ivThumbnail);
                RequestOptions requestOptions = new RequestOptions();
                Glide.with(ActivityStatus.this)
                        .load(Uri.parse(resources.get(counter)))
                        .apply(requestOptions)
                        .placeholder(R.drawable.ic_no_image)
                        .thumbnail(Glide.with(ActivityStatus.this).load(Uri.parse(resources.get(counter))))
                        .into(ivThumbnail);
                PlayWithExoplayer(Uri.parse(resources.get(counter)));

                for (int i = 0; i < resources.size(); i++) {
                    DownloadFileWithThread(resources.get(i));
                }

            }

        }

        // setVideoNumToSharedPref(counter, UserID);
        //Log.i("num", "" + getVideoNumFromSharedPref(UserID) + "      " + counter);

    }

    @Override
    public void onPrev() {
        Log.e("AC_STATUS", "onPrev: " + "prevClick");
        //  if ((counter - 1) < 0) return;

        if ((counter - 1) < 0) {
            counter = 0;
            Log.e("AC_STATUS", "onPrev: " + "No sAction===");
        } else {
            counter = counter - 1;

            if (localListFromDB == null || localListFromDB.isEmpty()) {

                Log.e("VidTest", "onPrev: " + " null");

                Log.e("VidTest", "onPrev: " + "Play from URL");

                ivThumbnail.setVisibility(View.VISIBLE);
                exoPlayerView.setVisibility(View.GONE);

                RequestOptions requestOptions = new RequestOptions();
                Glide.with(ActivityStatus.this)
                        .load(Uri.parse(resources.get(counter)))
                        .apply(requestOptions)
                        .placeholder(R.drawable.ic_no_image)
                        .thumbnail(Glide.with(ActivityStatus.this).load(Uri.parse(resources.get(counter))))
                        .into(ivThumbnail);

                PlayWithExoplayer(Uri.parse(resources.get(counter)));

                for (int i = 0; i < resources.size(); i++) {
                    DownloadFileWithThread(resources.get(i));
                }

            } else {

                String tempPath = "";
                for (int i = 0; i < localListFromDB.size(); i++) {
                    if (localListFromDB.get(i).getVideoLink().equals(resources.get(counter))) {
                        tempPath = localListFromDB.get(i).getVideoURI();
                        isVideoPresentInLocal = true;
                        break;
                    } else {
                        isVideoPresentInLocal = false;
                    }

                }

                if (isVideoPresentInLocal) {
                    Log.e("VidTest", "onPrev: " + "Play from Local");
                    ivThumbnail.setVisibility(View.VISIBLE);
                    exoPlayerView.setVisibility(View.GONE);
                    RequestOptions requestOptions = new RequestOptions();
                    Glide.with(ActivityStatus.this)
                            .load(tempPath)
                            .apply(requestOptions)
                            .placeholder(R.drawable.ic_no_image)
                            .thumbnail(Glide.with(ActivityStatus.this).load(tempPath))
                            .into(ivThumbnail);
                    PlayWithExoplayer(Uri.parse(tempPath));
                    //  videoView.setVideoURI(Uri.parse(tempPath));
                } else {
                    Log.e("VidTest", "onPrev: " + "Play from URL1");

                    ivThumbnail.setVisibility(View.VISIBLE);
                    exoPlayerView.setVisibility(View.GONE);
                    RequestOptions requestOptions = new RequestOptions();
                    Glide.with(ActivityStatus.this)
                            .load(Uri.parse(resources.get(counter)))
                            .apply(requestOptions)
                            .placeholder(R.drawable.ic_no_image)
                            .thumbnail(Glide.with(ActivityStatus.this).load(Uri.parse(resources.get(counter))))
                            .into(ivThumbnail);
                    PlayWithExoplayer(Uri.parse(resources.get(counter)));

                    for (int i = 0; i < resources.size(); i++) {
                        DownloadFileWithThread(resources.get(i));
                    }

                }

                //  Log.e("VidTest", "onPrev: " + "not null");
            }


            //setVideoNumToSharedPref(counter, UserID);
            //Log.i("num", "" + getVideoNumFromSharedPref(UserID) + "      " + counter);

        }

    }

    @Override
    public void onComplete() {
        Log.e("AC_STATUS", "onComplete: " + "videoCompleted");
        setVideoNumToSharedPref(counter, UserID);
        String ss = UserID + "bool";
        sharedPreferences.edit().putBoolean(ss, true).apply();
        /* Intent i = new Intent(ActivityStatus.this, MainActivity.class);
        startActivity(i);*/
        finish();
    }

    @Override
    protected void onDestroy() {
        statusProgressView.destroy();

        super.onDestroy();
    }

    private String callType = "";

    public void openVideoChat(View view) {

       /*
       if (CheckPermission()) {
            callType = "video";
            //   apiManager.getRemainingGiftCardFunction();
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

        customErrorToast();

    }

    private void customErrorToast() {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.unable_to_call_lay, (ViewGroup) toast);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 30);
        toast.setView(layout);
        toast.show();
    }

    public void gotoChatConversation(View view) {
        /*
        Intent intent = new Intent(ActivityStatus.this, InboxDetails.class);
        intent.putExtra("profileName", userData.get(0).getName());
        intent.putExtra("user_image", userData.get(0).getFemaleImages().get(0).getImageName());
        intent.putExtra("chatProfileId", String.valueOf(userData.get(0).getProfileId()));
        intent.putExtra("contactId", userData.get(0).getId());
        intent.putExtra("mode", true);
        intent.putExtra("channelName", "zeeplive662730982537574");
        intent.putExtra("usercount", 0);
        intent.putExtra("unreadMsgCount", 0);
        startActivity(intent);
        */
        customErrorToast();

    }

    private boolean CheckPermission() {

        final boolean[] isPermissionGranted = new boolean[1];

        Dexter.withActivity(ActivityStatus.this).withPermissions(Manifest.permission.RECORD_AUDIO,
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
                    Toast.makeText(ActivityStatus.this, "To use this feature Camera and Audio permissions are must.You need to allow the permissions.", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_close_top_right:
                Log.e("ActivityStatus", " i am here");
                //  Intent intent = new Intent(this, MainActivity.class);
                // finishAffinity();
                finish();
                // this.startActivity(intent);
                break;
            case R.id.iv_report_status_right:
                Log.e("ActivityStatus", " i am here");
                // customToast();
                reportUser();
                break;
        }

    }

    public void reportUser() {
        Log.e("newUserId", UserID + "");
        new ReportDialog(ActivityStatus.this, String.valueOf(UserID));
    }

    private void DownloadFileWithThread(String url) {

        if (isConnectingToInternet()) {
            new Thread(() -> {
                try {
                    File currentFile = new File(url);
                    String fileName = currentFile.getName();

                    Log.i("filename", fileName);
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e("error", "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
                    }

                    File OutputFile = null;

                    if (fileName.contains(".mp4")) {
                        OutputFile = new File(getFolderPath1("Video"), fileName);
                    } else if (fileName.contains(".png") || fileName.contains(".jpg") || fileName.contains(".jpeg")) {
                        OutputFile = new File(getFolderPath1("Images"), fileName);
                    } else if (fileName.contains(".pdf")) {
                        OutputFile = new File(getFolderPath1("Files"), fileName);
                    }
                    if (!OutputFile.exists()) {
                        OutputFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(OutputFile);
                        InputStream is = connection.getInputStream();

                        byte[] buffer = new byte[1024];
                        int len1 = 0;
                        while ((len1 = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len1);//Write new file
                        }
                        fos.close();
                        is.close();

                        statusDBHandler.addVideos(HostProfileId, url, OutputFile.getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
                    }
                    File finalOutputFile = OutputFile;

                    Log.e("ActivityStatus", "DownloadFileWithThread: Path " + finalOutputFile.getAbsolutePath() + "   url " + url);
                    Log.e("ActivityStatus", "DownloadFileWithThread: host " + HostProfileId);

                    //  statusDBHandler.addVideos(HostProfileId, url, finalOutputFile.getAbsolutePath(), String.valueOf(System.currentTimeMillis()));

                 /*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                   */

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
        }
    }


    private File getFolderPath(String type) {

        File myFolder = null;

        if (type.equals("Video")) {
            myFolder = new File(Environment.getExternalStorageDirectory(), "Status Videos");

            if (!myFolder.exists()) {
                myFolder.mkdir();
            }

        } else if (type.equals("Images")) {

            myFolder = new File(Environment.getExternalStorageDirectory(), "Photos");

            if (!myFolder.exists()) {
                myFolder.mkdir();
            }

        } else if (type.equals("Files")) {

            myFolder = new File(Environment.getExternalStorageDirectory(), "Other Files");

            if (!myFolder.exists()) {
                myFolder.mkdir();
            }
        }
        return myFolder;
    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void customToast() {

     /*
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.custom_toast_minimum, (ViewGroup) toast);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
      */

    }


    @Override
    public void onBackPressed() {
        // startActivity(new Intent(ActivityStatus.this, MainActivity.class));
        finish();
    }


    @Override
    public void isError(String errorCode) {

    }

    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;

    @Override
    public void isSuccess(Object response, int ServiceCode) {


        if (ServiceCode == Constant.GET_DATA_FROM_PROFILE_ID) {

            //   Log.e("ActivityStatus", "isSuccess: "+"Goto ViewProfile" );
            //   Log.e("ActivityStatus", "isSuccess: " + new Gson().toJson(rsp));

            DataFromProfileIdResponse rsp = (DataFromProfileIdResponse) response;
            DataFromProfileIdResult rlt = rsp.getResult();
            Log.e("ActivityStatus", "isSuccess: " + new Gson().toJson(rsp));
            Intent intent = new Intent(ActivityStatus.this, ViewProfile.class);
            intent.putExtra("id", rlt.getId());
            intent.putExtra("profileId", rlt.getProfile_id());
            intent.putExtra("level", rlt.getLevel());
            startActivity(intent);
            finish();


        }

        if (ServiceCode == Constant.GET_PROFILE_DATA) {
            Log.e("actStatus", "isSuccess: GET_PROFILE_DATA ");
            // UserListResponse.Data userData;
            UserListResponseNewData rsp = (UserListResponseNewData) response;
            //userData = (ResultDataNewProfile) rsp.getResult();

            userData.addAll(rsp.getResult());
            // binding.setResponse(userData);

            String[] dob = userData.get(0).getDob().split("-");
            int date = Integer.parseInt(dob[0]);
            int month = Integer.parseInt(dob[1]);
            int year = Integer.parseInt(dob[2]);

            // tv_host_age.setText("Age: " + getAge(year, month, date));

            userId = userData.get(0).getId();
            hostId = userData.get(0).getProfileId();
            callRate = userData.get(0).getCallRate();

            Log.e("actStatus", "isSuccess: GET_PROFILE_DATA " + new Gson().toJson(userData.get(0)));
            Log.e("activitysss", "onCreate: ACTIVITYSTATUS UserID222 " + userId + "   host id " + hostId);

        }

        if (ServiceCode == Constant.VIDEO_STATUS_DELETE) {
            Log.e("Delete===response==", response.toString() + "");
            Toast.makeText(ActivityStatus.this, "Video Deleted Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
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


    @Override
    protected void onResume() {
        super.onResume();

        statusProgressView.startStories(counter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                statusProgressView.pause();
            }
        }, 100);

        try {

            localListFromDB = statusDBHandler.getVideosRowList(HostProfileId);
            localVideoLinkList = statusDBHandler.getVideoLinksList(HostProfileId);

            Log.e("VidTest", "onCreate: localListFromDB " + new Gson().toJson(localListFromDB));
            Log.e("accordingupdateee", "onCreate: localListFromDB " + localVideoLinkList);


            if (localListFromDB == null || localListFromDB.isEmpty()) {
                Log.e("VidTest", "onCreate: " + "Play from URL");
                Log.e("videonumvideonum", "onCreate: Play from URL22 ");

                ivThumbnail.setVisibility(View.VISIBLE);
                exoPlayerView.setVisibility(View.GONE);

                if (getIntent().getStringExtra("inWhichActivity").equalsIgnoreCase("ProfileVideoAdapter")) {
                    Log.e("play from url", "============>" + resources.size() + "-----" + new Gson().toJson(resources));
                    PlayWithExoplayer(Uri.parse(clickedUrl));
                    for (int i = 0; i < resources.size(); i++) {

                        DownloadFileWithThread(resources.get(i));
                    }
                } else {
                    RequestOptions requestOptions = new RequestOptions();
                    Glide.with(ActivityStatus.this)
                            .load(Uri.parse(resources.get(counter)))
                            .apply(requestOptions)
                            .placeholder(R.drawable.ic_no_image)
                            .thumbnail(Glide.with(ActivityStatus.this).load(Uri.parse(resources.get(counter))))
                            .into(ivThumbnail);
                    PlayWithExoplayer(Uri.parse(resources.get(counter)));

                    for (int i = 0; i < resources.size(); i++) {

                        DownloadFileWithThread(resources.get(i));
                    }
                }
                statusProgressView.startStories(counter);
            } else {
                statusProgressView.startStories(counter);
                updateDbAccordingly(resources, localVideoLinkList);

                String tempPath = "";

                for (int i = 0; i < localListFromDB.size(); i++) {

                    if (localListFromDB.get(i).getVideoLink().equals(resources.get(counter))) {
                        tempPath = localListFromDB.get(i).getVideoURI();
                        isVideoPresentInLocal = true;
                        break;
                    } else {
                        isVideoPresentInLocal = false;
                    }

                }
                if (isVideoPresentInLocal) {
                    Log.e("videonumvideonum", "onCreate: Play from Local ");
                    Log.e("VidTest", "onCreate: " + "Play from Local");
                    ivThumbnail.setVisibility(View.VISIBLE);
                    exoPlayerView.setVisibility(View.GONE);
                    RequestOptions requestOptions = new RequestOptions();
                    Glide.with(ActivityStatus.this)
                            .load(tempPath)
                            .apply(requestOptions)
                            .placeholder(R.drawable.ic_no_image)
                            .thumbnail(Glide.with(ActivityStatus.this).load(tempPath))
                            .into(ivThumbnail);

                    PlayWithExoplayer(Uri.parse(tempPath));

                } else {
                    //  statusProgressView.pause();
                    Log.e("videonumvideonum", "onCreate: Play from URL1 ");
                    Log.e("VidTest", "onCreate: " + "Play from URL");

                    ivThumbnail.setVisibility(View.VISIBLE);
                    exoPlayerView.setVisibility(View.GONE);
                    RequestOptions requestOptions = new RequestOptions();
                    Glide.with(ActivityStatus.this)
                            .load(Uri.parse(resources.get(counter)))
                            .apply(requestOptions)
                            .placeholder(R.drawable.ic_no_image)
                            .thumbnail(Glide.with(ActivityStatus.this).load(Uri.parse(resources.get(counter))))
                            .into(ivThumbnail);
                    PlayWithExoplayer(Uri.parse(resources.get(counter)));

                    //   videoView.setVideoURI(Uri.parse(resources.get(counter)));

                    for (int i = 0; i < resources.size(); i++) {
                        DownloadFileWithThread(resources.get(i));
                    }

                }

            }

            //Log.e("hh", "" + getVideoNumFromSharedPref(UserID) + "      " + counter);


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

    }

    private File getFolderPath1(String type) {

        File myFolder = null;

        if (type.equals("Video")) {
            myFolder = new File(getExternalFilesDir("All Files"), "Status Videos");

            if (!myFolder.exists()) {
                myFolder.mkdir();
            }

        } else if (type.equals("Images")) {

            myFolder = new File(getExternalFilesDir("All Files"), "Photos");

            if (!myFolder.exists()) {
                myFolder.mkdir();
            }

        } else if (type.equals("Files")) {

            myFolder = new File(getExternalFilesDir("All Files"), "Other Files");

            if (!myFolder.exists()) {
                myFolder.mkdir();
            }
        }
        return myFolder;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  pausePlayer();

        closeExoPlayer();

    }

    private void closeExoPlayer() {
        // exoPlayer.stop();
        //  exoPlayer.release();
        releasePlayer();

        // exoPlayerView.setPlayer(null);
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

}