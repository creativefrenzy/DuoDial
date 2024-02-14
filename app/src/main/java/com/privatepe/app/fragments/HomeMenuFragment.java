package com.privatepe.app.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.app.Fast_screen.FastScreenActivity;
import com.privatepe.app.Firestatus.FireBaseStatusManage;
import com.privatepe.app.R;
import com.privatepe.app.adapter.DailyUsersListAdapter;
import com.privatepe.app.adapter.WeeklyUsersListAdapter;
import com.privatepe.app.response.accountvarification.CheckFemaleVarifyResponse;
import com.privatepe.app.response.daily_weekly.DailyUserListResponse;
import com.privatepe.app.response.daily_weekly.DailyWeeklyEarningDetail;
import com.privatepe.app.response.daily_weekly.WeeklyUserListResponse;
import com.privatepe.app.response.temporary_block.TemporaryBlockResponse;
import com.privatepe.app.response.temporary_block.TemporaryBlockResult;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.DateFormatter;
import com.privatepe.app.utils.NetworkCheck;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HomeMenuFragment extends BaseFragment implements ApiResponseInterface {

    private NetworkCheck networkCheck;
    BroadcastReceiver broadcastReceiver;

    SessionManager sessionManager;
    private List<DailyUserListResponse.Result> list = new ArrayList<>();
    private List<DailyUserListResponse.Result> newDailyList = new ArrayList<>();
    private List<WeeklyUserListResponse.Result> weelyList = new ArrayList<>();
    private List<WeeklyUserListResponse.Result> newWeeklyList = new ArrayList<>();
    RecyclerView rvUserList;
    private DailyUsersListAdapter dailyUsersListAdapter;
    private WeeklyUsersListAdapter weeklyUsersListAdapter;
    GridLayoutManager gridLayoutManager;
    // private SwipeRefreshLayout mSwipeRefreshLayout;
    ApiManager apiManager;
    ImageView ivAvatarRankingOne,ivAvatarRankingTwo,ivAvatarRankingThree,ivAvatarOne,ivAvatarTwo,ivAvatarThree;
    TextView tvFirstAvatarName,tvSecondAvatarName,tvThirdAvatarName,tvFirstAvatarBean,tvSecondAvatarBean,tvThirdAvatarBean;
    ConstraintLayout clAvatarOne,clAvatarTwo,clAvatarThree;
    RelativeLayout rlBgOne,rlBgSecond,rlBgThree;
    TextView tvCharmLevelOne,tvCharmLevelSecond,tvCharmLevelThree;
    TextView tvDaily,tvWeekly,tvThisWeek,tvLastWeek;
    String selectedType = "", selectedInterval="";
    TextView tv_next_week,tv_per_minuit,tv_weekly_earning,tv_today_call,tv_today_earning,tv_call_earning,tv_gift_earning,tv_other;
    private Dialog unVarifiedDialog, temporaryBlockDialog;
    Switch switchBtn;
    public HomeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_blank, container, false);
        View view = inflater.inflate(R.layout.activity_daily_weekly_rank, container, false);

        networkCheck = new NetworkCheck();
        apiManager = new ApiManager(getContext(),this);
        switchBtn = view.findViewById(R.id.switchBtn);
        rvUserList = view.findViewById(R.id.rvUserList);
        ivAvatarRankingOne = view.findViewById(R.id.iv_avatar_ranking_one);
        ivAvatarRankingTwo = view.findViewById(R.id.iv_avatar_ranking_second);
        ivAvatarRankingThree = view.findViewById(R.id.iv_avatar_ranking_third);
        ivAvatarOne = view.findViewById(R.id.iv_avatar_one);
        ivAvatarTwo = view.findViewById(R.id.iv_avatar_second);
        ivAvatarThree = view.findViewById(R.id.iv_avatar_third);
        tvFirstAvatarName = view.findViewById(R.id.tvFirstAvatarName);
        tvSecondAvatarName = view.findViewById(R.id.tvSecondAvatarName);
        tvThirdAvatarName = view.findViewById(R.id.tvThirdAvatarName);
        tvFirstAvatarBean = view.findViewById(R.id.tvFirstAvatarBean);
        tvSecondAvatarBean = view.findViewById(R.id.tvSecondAvatarBean);
        tvThirdAvatarBean = view.findViewById(R.id.tvThirdAvatarBean);
        clAvatarOne = view.findViewById(R.id.cl_avatar_one);
        clAvatarTwo = view.findViewById(R.id.cl_avatar_two);
        clAvatarThree = view.findViewById(R.id.cl_avatar_three);
        rlBgOne = view.findViewById(R.id.rl_bg_one);
        rlBgSecond = view.findViewById(R.id.rl_bg_second);
        rlBgThree = view.findViewById(R.id.rl_bg_three);
        tvCharmLevelOne = view.findViewById(R.id.tvCharmLevelOne);
        tvCharmLevelSecond = view.findViewById(R.id.tvCharmLevelSecond);
        tvCharmLevelThree = view.findViewById(R.id.tvCharmLevelThree);
        tvThisWeek = view.findViewById(R.id.tvThisWeek);
        tvLastWeek = view.findViewById(R.id.tvLastWeek);
        tvDaily = view.findViewById(R.id.tvDaily);
        tvWeekly = view.findViewById(R.id.tvWeekly);

        tv_next_week = view.findViewById(R.id.tv_next_week);
        tv_per_minuit = view.findViewById(R.id.tv_per_minuit);
        tv_weekly_earning = view.findViewById(R.id.tv_weekly_earning);
        tv_today_call = view.findViewById(R.id.tv_today_call);
        tv_today_earning = view.findViewById(R.id.tv_today_earning);
        tv_call_earning = view.findViewById(R.id.tv_call_earning);
        tv_gift_earning = view.findViewById(R.id.tv_gift_earning);
        tv_other = view.findViewById(R.id.tv_other);
        tvThisWeek.setBackground(getResources().getDrawable(R.drawable.round_select_daily));
        tvLastWeek.setBackground(getResources().getDrawable(R.drawable.round_unselect_daily));

        selectedInterval = "this_week";

        new SessionManager(getContext()).setHostAutopickup("no");

        Log.e("CreatedFragment", "onCreateView: " + "HomeMenuFragment");
        sessionManager = new SessionManager(getContext());

        if (sessionManager.getWorkSession()) {
            //startWork.setImageResource(R.drawable.off_work);
            switchBtn.setChecked(true);
        } else {
            switchBtn.setChecked(false);
            //startWork.setImageResource(R.drawable.start);
        }

        Log.i("isWorkOn", "" + sessionManager.getWorkSession());
        //String token = new SessionManager(myContext).getUserToken();
        if (networkCheck.isNetworkAvailable(getContext())) {
            new ApiManager(getContext()).changeOnlineStatus(0);
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String workedValue = intent.getStringExtra("isWorkedOn");

                Log.i("workedValue", "" + workedValue);

                if (workedValue.equals("false")) {
                    switchBtn.setChecked(false);
                    //startWork.setImageResource(R.drawable.start);
                    sessionManager.setWorkSession(false);

                }

                if (workedValue.equals("true")) {

                    //startWork.setImageResource(R.drawable.off_work);
                    switchBtn.setChecked(true);
                    sessionManager.setWorkSession(true);

                }

                Log.i("isWorkOn", "" + sessionManager.getWorkSession());

            }
        };

        switchBtn.setOnClickListener(view1 -> {
            Log.e("UserMenuFragment", "onCreateView: Status Video List Size " + sessionManager.getStatusVideoListSize());

            if (isLive) {
                new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                        "", "", "Online");
                isLive = false;
               // startWork.setImageResource(R.drawable.start);
            } else {
                new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                        "", "", "Live");
                isLive = true;
                //startWork.setImageResource(R.drawable.off_work);
            }

        });

        tvThisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvThisWeek.setBackground(getResources().getDrawable(R.drawable.round_select_daily));
                tvLastWeek.setBackground(getResources().getDrawable(R.drawable.round_unselect_daily));
                selectedInterval = "this_week";
                Log.e("naval", selectedInterval+"===="+selectedType);
                apiManager.getDailyUserList(selectedInterval);
            }
        });
        tvLastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLastWeek.setBackground(getResources().getDrawable(R.drawable.round_select_daily));
                tvThisWeek.setBackground(getResources().getDrawable(R.drawable.round_unselect_daily));
                selectedInterval = "last_week";
                Log.e("naval", selectedInterval+"===="+selectedType);
                apiManager.getWeeklyUserList(selectedInterval);
            }
        });
        Log.e("naval", selectedInterval+"===="+selectedType);
        apiManager.getWeeklyUserDetail();
        apiManager.getDailyUserList(selectedInterval);
        return view;
    }
    private boolean isLive = false;

    private void showUnvarifiedFemaleDialog() {
        unVarifiedDialog = new Dialog(getContext());
        unVarifiedDialog.setContentView(R.layout.unvarified_female_dialog);
        unVarifiedDialog.setCancelable(false);
        unVarifiedDialog.setCanceledOnTouchOutside(true);
        unVarifiedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView unVarifiedText = unVarifiedDialog.findViewById(R.id.tv_unvarifiedMessage);
        Button OKbtn = unVarifiedDialog.findViewById(R.id.btn_gotit);
        unVarifiedText.setText("You have not yet verified as host.\nPlease contact to your agency for completing the host verification.");
        OKbtn.setOnClickListener(view -> unVarifiedDialog.dismiss());
        unVarifiedDialog.show();
    }

    private boolean CheckPermission() {

        final boolean[] isPermissionGranted = new boolean[1];

        String[] permissions;

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
            };
            Log.e("PermissionArray", "onCreate: UserMenuFrag Permission for android 13");
        } else {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            Log.e("PermissionArray", "onCreate: UserMenuFrag Permission for below android 13");
        }
        Dexter.withActivity(getActivity()).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("onPermissionsChecked", "onPermissionsChecked: ");

                if (report.areAllPermissionsGranted()) {
                    Log.e("onPermissionsChecked", "all permission granted");
                    isPermissionGranted[0] = true;
                } else {
                    Log.e("onPermissionsChecked", "all permission not granted");
                    isPermissionGranted[0] = false;
                    Toast.makeText(getContext(), "To use this feature Camera and Audio permissions are must.You need to allow the permissions", Toast.LENGTH_SHORT).show();
                    // Dexter.withActivity(InboxDetails.this).withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    //   new PermissionDialog(getActivity());
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
    private void changeIcon() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //startWork.setImageResource(R.drawable.off_work);
                switchBtn.setChecked(true);
            }
        }, 1000);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void alertOkClicked() {

    }
    @Override
    protected void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rvUserList.setLayoutManager(linearLayoutManager);
        rvUserList.setNestedScrollingEnabled(false);

    }
    private void setCharmLevel(RelativeLayout relativeLayout,int position){

        if(list.get(position).getCharm_level() ==0){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv0));
        }else if(list.get(position).getCharm_level() >=1 && list.get(position).getCharm_level() <=5){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv1_5));
        }else if(list.get(position).getCharm_level() >=6 && list.get(position).getCharm_level() <=10){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv6_10));
        }else if(list.get(position).getCharm_level() >=11 && list.get(position).getCharm_level() <=15){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv11_15));
        }else if(list.get(position).getCharm_level() >=16 && list.get(position).getCharm_level() <=20){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv16_20));
        }else if(list.get(position).getCharm_level() >=21 && list.get(position).getCharm_level() <=25){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv21_25));
        }else if(list.get(position).getCharm_level() >=26 && list.get(position).getCharm_level() <=30){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv26_30));
        }else if(list.get(position).getCharm_level() >=31 && list.get(position).getCharm_level() <=35){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv31_35));
        }else if(list.get(position).getCharm_level() >=36 && list.get(position).getCharm_level() <=40){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv36_40));
        }else if(list.get(position).getCharm_level() >=41 && list.get(position).getCharm_level() <=45){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv41_45));
        }else if(list.get(position).getCharm_level() >=46 && list.get(position).getCharm_level() <=50){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm__lv46_50));
        }
    }
    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }
    @Override
    protected void initListners() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onStop() {
        super.onStop();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("ClosedWork"));
        if (isLive){
            new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                    "", "", "Live");
        }
    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.CHECK_FEMALE_VARIFY) {
            CheckFemaleVarifyResponse checkFemaleVarifyResponse = (CheckFemaleVarifyResponse) response;

            //  Log.e("CHECK_FEMALE_VARIFY", "isSuccess: " + new Gson().toJson(checkFemaleVarifyResponse));

            Log.e("CHECK_FEMALE_VARIFY", "isSuccess: checkFemaleVarifyResponse " + new Gson().toJson(checkFemaleVarifyResponse));


            if (checkFemaleVarifyResponse.getIs_female_verify() == 1) {


                new ApiManager(getContext(), HomeMenuFragment.this).checkTemporaryBlock();


            } else if (checkFemaleVarifyResponse.getIs_female_verify() == 2) {
                Log.e("CHECK_FEMALE_VARIFY", "isSuccess: not varified ");
                showUnvarifiedFemaleDialog();
            }


        } else if (ServiceCode == Constant.CHECK_TEMPORARY_BLOCK) {

            TemporaryBlockResponse temporaryBlockResponse = (TemporaryBlockResponse) response;

            if (temporaryBlockResponse != null) {
                Log.e("CHECK_TEMPORARY_BLOCK", "isSuccess: not null " + new Gson().toJson(temporaryBlockResponse));

                TemporaryBlockResult temporaryBlockResult = temporaryBlockResponse.getResult();

                Long currentTime = temporaryBlockResult.getCurrent_time();
                Long endTime = temporaryBlockResult.getEnd_time();

                Long remainingTimeInMilliSec = endTime - currentTime;

                showTemporaryBlockDialog(getTimeInString2(remainingTimeInMilliSec), temporaryBlockResult.getReason());

            } else {
                Log.e("CHECK_TEMPORARY_BLOCK", "isSuccess: null ");

                Log.e("CHECK_FEMALE_VARIFY", "isSuccess: verified ");
                if (CheckPermission()) {
                    if (!sessionManager.getWorkSession()) {
                        Intent intent = new Intent(currentActivity, FastScreenActivity.class);
                        startActivity(intent);
                        changeIcon();
                        sessionManager.setWorkSession(true);
                    } else {
                       // startWork.setImageResource(R.drawable.start);
                        switchBtn.setChecked(false);
                        Intent closePIPIntent = new Intent("FINISH_ACTIVITY_BROADCAST");
                        closePIPIntent.putExtra("BRODCAST_FOR_PIP", "FinishThisActivity");
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(closePIPIntent);
                        sessionManager.setWorkSession(false);
                    }

                    Log.i("isWorkOn", "" + sessionManager.getWorkSession());

                } else {
                }
            }
        }

        if (ServiceCode == Constant.GET_DAILY_EARNING) {
            DailyUserListResponse rsp = (DailyUserListResponse) response;
            //mSwipeRefreshLayout.setRefreshing(false);
            if(list.size() >0) {
                list.clear();
            }
            list = rsp.getResult();
            //TOTAL_PAGES = rsp.getResult().getLast_page();
            //Log.e("inHostdata", new Gson().toJson(rsp.getResult()));
            if (list.size() > 0) {
                if(newDailyList.size() >0) {
                    newDailyList.clear();
                }
                try {
                    for (int i = 3; i < list.size(); i++) {
                        newDailyList.add(list.get(i));
                    }

                    dailyUsersListAdapter = new DailyUsersListAdapter(getContext(), newDailyList, getActivity());
                    rvUserList.setAdapter(dailyUsersListAdapter);
                    dailyUsersListAdapter.notifyDataSetChanged();

                    tvFirstAvatarName.setText(list.get(0).getName().toLowerCase());
                    tvFirstAvatarBean.setText(list.get(0).getDaily_earning_beans() + "");
                    tvCharmLevelOne.setText(list.get(0).getCharm_level() + "");
                    setCharmLevel(rlBgOne, 0);
                    Glide.with(this).load(list.get(0).getProfile_images().get(0).getImage_name())
                            .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                    .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                    .error(R.drawable.fake_user_icon)).into(ivAvatarOne);

                    tvSecondAvatarName.setText(list.get(1).getName().toLowerCase());
                    tvSecondAvatarBean.setText(list.get(1).getDaily_earning_beans() + "");
                    tvCharmLevelSecond.setText(list.get(1).getCharm_level() + "");
                    setCharmLevel(rlBgSecond, 1);
                    Glide.with(this).load(list.get(1).getProfile_images().get(0).getImage_name())
                            .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                    .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                    .error(R.drawable.fake_user_icon)).into(ivAvatarTwo);

                    tvThirdAvatarName.setText(list.get(2).getName().toLowerCase());
                    tvThirdAvatarBean.setText(list.get(2).getDaily_earning_beans() + "");
                    tvCharmLevelThree.setText(list.get(2).getCharm_level()+"");
                    setCharmLevel(rlBgThree, 2);
                    Glide.with(this).load(list.get(2).getProfile_images().get(0).getImage_name())
                            .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                    .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                    .error(R.drawable.fake_user_icon)).into(ivAvatarThree);
                /*if (currentPage < TOTAL_PAGES) {
                   // discoverUserAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }*/


                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }

        }

        if (ServiceCode == Constant.GET_WEEKLY_EARNING) {
            WeeklyUserListResponse weeklyUserListResponse = (WeeklyUserListResponse) response;

            if(weelyList.size() >0) {
                weelyList.clear();
            }
            weelyList = weeklyUserListResponse.getResult();
            if (weelyList.size() > 0) {
                if(newWeeklyList.size() >0) {
                    newWeeklyList.clear();
                }
                try {
                    for (int i = 3; i < weelyList.size(); i++) {
                        newWeeklyList.add(weelyList.get(i));
                    }

                    weeklyUsersListAdapter = new WeeklyUsersListAdapter(getContext(), newWeeklyList, getActivity());
                    rvUserList.setAdapter(weeklyUsersListAdapter);
                    weeklyUsersListAdapter.notifyDataSetChanged();

                    tvFirstAvatarName.setText(weelyList.get(0).getUser().getName().toLowerCase());
                    tvFirstAvatarBean.setText(weelyList.get(0).getTotal_coin_earned() + "");
                    tvCharmLevelOne.setText(weelyList.get(0).getUser().getCharm_level() + "");
                    setCharmLevel(rlBgOne, 0);
                    if(weelyList.get(0).getProfile_images() != null) {
                        Glide.with(this).load(weelyList.get(0).getProfile_images().get(0).getImage_name())
                                .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                        .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                        .error(R.drawable.fake_user_icon)).into(ivAvatarOne);
                    }
                    tvSecondAvatarName.setText(weelyList.get(1).getUser().getName().toLowerCase());
                    tvSecondAvatarBean.setText(weelyList.get(1).getTotal_coin_earned() + "");
                    tvCharmLevelSecond.setText(weelyList.get(1).getUser().getCharm_level() + "");
                    setCharmLevel(rlBgSecond, 1);
                    if(weelyList.get(1).getProfile_images() != null) {
                        Glide.with(this).load(weelyList.get(1).getProfile_images().get(0).getImage_name())
                                .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                        .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                        .error(R.drawable.fake_user_icon)).into(ivAvatarTwo);
                    }
                    tvThirdAvatarName.setText(weelyList.get(2).getUser().getName().toLowerCase());
                    tvThirdAvatarBean.setText(weelyList.get(2).getTotal_coin_earned() + "");
                    tvCharmLevelThree.setText(weelyList.get(2).getUser().getCharm_level()+"");
                    setCharmLevel(rlBgThree, 2);
                    if(weelyList.get(2).getProfile_images() != null) {
                        Glide.with(this).load(weelyList.get(2).getProfile_images().get(0).getImage_name())
                                .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                        .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                        .error(R.drawable.fake_user_icon)).into(ivAvatarThree);
                    }
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        }
        if (ServiceCode == Constant.GET_DAILY_WEEKLY_EARNING) {
            DailyWeeklyEarningDetail earningDetail =(DailyWeeklyEarningDetail) response;
            tv_next_week.setText("Next Week: (Starting "+ DateFormatter.getInstance().formatDDMMM(earningDetail.getResult().getNext_week_date())+")");
            tv_per_minuit.setText(earningDetail.getResult().getCall_rate()+"/min");
            tv_weekly_earning.setText(earningDetail.getResult().getWeekly_earning()+"");
            tv_today_call.setText(earningDetail.getResult().getToday_total_calls()+"");
            tv_today_earning.setText(earningDetail.getResult().getToday_total_earning()+"");
            tv_call_earning.setText(earningDetail.getResult().getToday_call_earning()+"");
            tv_gift_earning.setText(earningDetail.getResult().getToday_gift_earning()+"");
            tv_other.setText(earningDetail.getResult().getToday_other_earning()+"");
        }
    }

    private String getTimeInString(Long remainingTimeInMilliSec) {

        String timeInString = "";

        Long remainingTimeInSec = remainingTimeInMilliSec / 1000;

        Long remainingTimeInMin = remainingTimeInSec / 60;

        Long remainingTimeInHour = remainingTimeInMin / 60;


        if (remainingTimeInHour > 0) {


        } else if (remainingTimeInMin > 0) {


        } else if (remainingTimeInSec > 0) {


        }

        return timeInString;
    }

    private String getTimeInString2(Long remainingTimeInMilliSec) {

        String timeInString = "";

        int seconds = (int) (remainingTimeInMilliSec / 1000) % 60;
        int minutes = (int) ((remainingTimeInMilliSec / (1000 * 60)) % 60);
        int hours = (int) ((remainingTimeInMilliSec / (1000 * 60 * 60)) % 24);

        if (hours == 0) {

            String suffix = "";

            if (minutes > 1) {
                suffix = " Minutes ";
            } else {
                suffix = " Minute ";
            }
            timeInString = minutes + suffix;

            Log.e("ttttttkkkk", "getTimeInString2: remaining time " + timeInString);


        } else if (minutes == 0) {
            String suffix = "";

            if (seconds > 1) {
                suffix = " Seconds ";
            } else {
                suffix = " Second ";
            }

            //    timeInString = seconds + suffix;

            timeInString = "1" + "Minute";

            Log.e("ttttttkkkk", "getTimeInString2: remaining time " + timeInString);
        } else {
            //  timer.setText(twoDigitString(minutes) + ":" + twoDigitString(seconds));

            String suffixH = "";
            String suffixM = "";

            if (minutes > 1) {
                suffixM = " Minutes ";
            } else {
                suffixM = " Minute ";
            }


            if (hours > 1) {
                suffixH = " Hours ";
            } else {
                suffixH = " Hour ";
            }

            timeInString = hours + suffixH + minutes + suffixM;


            //    timeInString = hours + " Hours " + minutes + " Minutes ";
            Log.e("ttttttkkkk", "getTimeInString2: remaining time " + timeInString);
        }

        return timeInString;
    }

    private void showTemporaryBlockDialog(String timeInString, String reason) {

        temporaryBlockDialog = new Dialog(getContext());
        temporaryBlockDialog.setContentView(R.layout.temporary_block_dialog);
        temporaryBlockDialog.setCancelable(false);
        temporaryBlockDialog.setCanceledOnTouchOutside(true);
        temporaryBlockDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView unVarifiedText = temporaryBlockDialog.findViewById(R.id.tv_unvarifiedMessage);
        Button OKbtn = temporaryBlockDialog.findViewById(R.id.btn_gotit);

        timeInString = timeInString + ".";

        String sourceString = "Due to" + " <b>" + reason + "</b> " + "your account has been temporary ban for next" + " <b>" + timeInString + "</b> " + "if you have any Query contact to your agency.";
        unVarifiedText.setText(Html.fromHtml(sourceString));

        // unVarifiedText.setText("You have not yet verified as host.\nPlease contact to your agency for completing the host verification.");

        OKbtn.setOnClickListener(view -> temporaryBlockDialog.dismiss());
        temporaryBlockDialog.show();
    }

}