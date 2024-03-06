package com.privatepe.host.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
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
import android.widget.CompoundButton;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.host.Firestatus.FireBaseStatusManage;
import com.privatepe.host.R;
import com.privatepe.host.activity.CallReportActivity;
import com.privatepe.host.adapter.DailyUsersListAdapter;
import com.privatepe.host.adapter.WeeklyUsersListAdapter;
import com.privatepe.host.dialogs.DailyWeeklyBottomSheet;
import com.privatepe.host.dialogs_agency.AddLibVideoDialog;
import com.privatepe.host.model.Deletelivebroadresponse;
import com.privatepe.host.response.accountvarification.CheckFemaleVarifyResponse;
import com.privatepe.host.response.daily_weekly.DailyUserListResponse;
import com.privatepe.host.response.daily_weekly.DailyWeeklyEarningDetail;
import com.privatepe.host.response.daily_weekly.WeeklyUserListResponse;
import com.privatepe.host.response.daily_weekly.WeeklyUserRewardResponse;
import com.privatepe.host.response.temporary_block.TemporaryBlockResponse;
import com.privatepe.host.response.temporary_block.TemporaryBlockResult;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.DateFormatter;
import com.privatepe.host.utils.NetworkCheck;
import com.privatepe.host.utils.SessionManager;

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
    List<WeeklyUserRewardResponse.WeeklyRewardData> weeklyRewardDataList = new ArrayList<>();
    RecyclerView rvUserList;
    private DailyUsersListAdapter dailyUsersListAdapter;
    private WeeklyUsersListAdapter weeklyUsersListAdapter;
    GridLayoutManager gridLayoutManager;
    // private SwipeRefreshLayout mSwipeRefreshLayout;
    ApiManager apiManager;
    ImageView ivAvatarRankingOne, ivAvatarRankingTwo, ivAvatarRankingThree, ivAvatarOne, ivAvatarTwo, ivAvatarThree;
    TextView tvFirstAvatarName, tvSecondAvatarName, tvThirdAvatarName, tvFirstAvatarBean, tvSecondAvatarBean, tvThirdAvatarBean;
    ConstraintLayout clAvatarOne, clAvatarTwo, clAvatarThree;
    RelativeLayout rlBgOne, rlBgSecond, rlBgThree;
    TextView tvCharmLevelOne, tvCharmLevelSecond, tvCharmLevelThree;
    TextView tvDaily, tvWeekly, tvThisWeek, tvLastWeek, tvCallDetail;
    String selectedType = "", selectedInterval = "";
    TextView tv_next_week, tv_per_minuit, tv_weekly_earning, tv_today_call, tv_today_earning, tv_call_earning, tv_gift_earning, tv_other,tv_referral_earning;
    private Dialog unVarifiedDialog, temporaryBlockDialog;
    Switch switchBtn;
    SwipeRefreshLayout swipeToRefreshfem;
    String selfCount;

    public HomeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_daily_weekly_rank, container, false);

        networkCheck = new NetworkCheck();
        apiManager = new ApiManager(getContext(), this);
        switchBtn = view.findViewById(R.id.switchBtn);
        swipeToRefreshfem=view.findViewById(R.id.swipeToRefreshfem);
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
        tvCallDetail = view.findViewById(R.id.tvCallDetail);

        tv_next_week = view.findViewById(R.id.tv_next_week);
        tv_per_minuit = view.findViewById(R.id.tv_per_minuit);
        tv_weekly_earning = view.findViewById(R.id.tv_weekly_earning);
        tv_today_call = view.findViewById(R.id.tv_today_call);
        tv_today_earning = view.findViewById(R.id.tv_today_earning);
        tv_referral_earning = view.findViewById(R.id.tv_referral_earning);
        tv_call_earning = view.findViewById(R.id.tv_call_earning);
        tv_gift_earning = view.findViewById(R.id.tv_gift_earning);
        tv_other = view.findViewById(R.id.tv_other);
        tvThisWeek.setBackground(getResources().getDrawable(R.drawable.round_select_daily));
        tvLastWeek.setBackground(getResources().getDrawable(R.drawable.round_unselect_daily));

        selectedInterval = "this_week";
        CheckPermission();
        new SessionManager(getContext()).setHostAutopickup("no");

        Log.e("CreatedFragment", "onCreateView: " + "HomeMenuFragment");
        sessionManager = new SessionManager(getContext());

        /*if (sessionManager.getWorkSession()) {
            switchBtn.setChecked(true);
        } else {
            switchBtn.setChecked(false);
        }*/

        Log.i("isWorkOn", "" + sessionManager.getWorkSession());

        if (networkCheck.isNetworkAvailable(getContext())) {
            new ApiManager(getContext()).changeOnlineStatus(0);
        }

       /* broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String workedValue = intent.getStringExtra("isWorkedOn");
                Log.i("workedValue", "" + workedValue);

                if (workedValue.equals("false")) {
                    switchBtn.setChecked(false);
                    sessionManager.setWorkSession(false);
                }

                if (workedValue.equals("true")) {
                    switchBtn.setChecked(true);
                    sessionManager.setWorkSession(true);
                }

                Log.i("isWorkOn", "" + sessionManager.getWorkSession());

            }
        };*/
        swipeToRefreshfem.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (networkCheck.isNetworkAvailable(getContext())) {
                    apiManager.getWeeklyUserDetail();

                }else {
                    swipeToRefreshfem.setRefreshing(false);
                }
            }
        });
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // on below line we are checking
                // if switch is checked or not.
                Log.e("CHECK_FEMALE_VARIFY", "checkornot " + isChecked+" network "+networkCheck.isNetworkAvailable(getContext()));
                if(networkCheck.isNetworkAvailable(getContext())) {
                    isCheckedS = isChecked;
                    if (isChecked) {
                        apiManager.checkFemaleVarification();
                    } else {
                        apiManager.deleteBroadList();
                    }
                }else {
                    switchBtn.setChecked(false);
                }

            }
        });

        tvCallDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CallReportActivity.class);
                startActivity(intent);
            }
        });

        tvWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvWeekly.setEnabled(false);
                new DailyWeeklyBottomSheet(getContext(), getActivity(), selfCount, weeklyRewardDataList);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvWeekly.setEnabled(true);
                    }
                }, 1000);

            }
        });
        tvThisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvThisWeek.setEnabled(false);
                tvThisWeek.setBackground(getResources().getDrawable(R.drawable.round_select_daily));
                tvLastWeek.setBackground(getResources().getDrawable(R.drawable.round_unselect_daily));
                selectedInterval = "this_week";
                apiManager.getDailyUserList(selectedInterval);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvThisWeek.setEnabled(true);
                    }
                }, 1000);
            }
        });
        tvLastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLastWeek.setEnabled(false);
                tvLastWeek.setBackground(getResources().getDrawable(R.drawable.round_select_daily));
                tvThisWeek.setBackground(getResources().getDrawable(R.drawable.round_unselect_daily));
                selectedInterval = "last_week";
                apiManager.getWeeklyUserList(selectedInterval);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvLastWeek.setEnabled(true);
                    }
                }, 1000);
            }
        });

        apiManager.getDailyUserList(selectedInterval);
        apiManager.getWeeklyUserDetail();
        apiManager.getWeeklyUserReward();
        apiManager.checkFemaleVarification();

        return view;
    }
    private boolean isCheckedS=false;
private void setOnlineSwitch(){
    String hostVerifyStatus = new SessionManager(getContext()).getResUpload();
    Log.e("CHECK_FEMALE_VARIFY", "Switch btn" + hostVerifyStatus);

    if (sessionManager.getWorkSession() && hostVerifyStatus.equals("1") ) {
        if (isCheckedS) {
            new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                    "", "", "Live");
            isLive = true;
        } else {
            // if switch is unchecked.
            new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                    "", "", "Online");
            isLive = false;

        }
    } else if (hostVerifyStatus.equals("4")) {

        switchBtn.setChecked(false);
        showUnvarifiedFemaleDialog();
        // Toast.makeText(getContext(),"Account not verified yet. Your account is under review.",Toast.LENGTH_SHORT).show();
    } else {

        switchBtn.setChecked(false);
        new AddLibVideoDialog(getContext());
    }
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
        unVarifiedText.setText("Your ID verification approval is pending. Await confirmation to start work. Thank you for your patience.");
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvUserList.setLayoutManager(linearLayoutManager);
        rvUserList.setNestedScrollingEnabled(false);

    }

    private void setCharmLevel(RelativeLayout relativeLayout, int position) {

        if (list.get(position).getCharm_level() == 0) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv0));
        } else if (list.get(position).getCharm_level() >= 1 && list.get(position).getCharm_level() <= 5) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv1_5));
        } else if (list.get(position).getCharm_level() >= 6 && list.get(position).getCharm_level() <= 10) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv6_10));
        } else if (list.get(position).getCharm_level() >= 11 && list.get(position).getCharm_level() <= 15) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv11_15));
        } else if (list.get(position).getCharm_level() >= 16 && list.get(position).getCharm_level() <= 20) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv16_20));
        } else if (list.get(position).getCharm_level() >= 21 && list.get(position).getCharm_level() <= 25) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv21_25));
        } else if (list.get(position).getCharm_level() >= 26 && list.get(position).getCharm_level() <= 30) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv26_30));
        } else if (list.get(position).getCharm_level() >= 31 && list.get(position).getCharm_level() <= 35) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv31_35));
        } else if (list.get(position).getCharm_level() >= 36 && list.get(position).getCharm_level() <= 40) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv36_40));
        } else if (list.get(position).getCharm_level() >= 41 && list.get(position).getCharm_level() <= 45) {
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.charm_lv41_45));
        } else if (list.get(position).getCharm_level() >= 46 && list.get(position).getCharm_level() <= 50) {
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
        if (isLive) {
            new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                    "", "", "Live");
        }
        //Log.e("isOnCalltest11","1 :"+String.valueOf(new SessionManager(requireContext()).getHostOnCall()));
        if(new SessionManager(requireContext()).getHostOnCall()){
            apiManager.getWeeklyUserDetail();
            new SessionManager(requireContext()).setHostOnCall(false);
            //Log.e("isOnCalltest11", "2 :"+String.valueOf(new SessionManager(requireContext()).getHostOnCall()));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
            super.onHiddenChanged(hidden);
        //Log.e("isOnCalltest11","1 :onHiddenChanged :"+hidden+ "||| "+String.valueOf(new SessionManager(requireContext()).getHostOnCall()));
        if(!hidden){
            if(new SessionManager(requireContext()).getHostOnCall()){
                apiManager.getWeeklyUserDetail();
                new SessionManager(requireContext()).setHostOnCall(false);
                //Log.e("isOnCalltest11", "2 :"+String.valueOf(new SessionManager(requireContext()).getHostOnCall()));
            }
        }
    }
    @Override
    public void isError(String errorCode) {
        swipeToRefreshfem.setRefreshing(false);
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.CHECK_FEMALE_VARIFY) {
            CheckFemaleVarifyResponse checkFemaleVarifyResponse = (CheckFemaleVarifyResponse) response;
                new SessionManager(getContext()).setResUpload(checkFemaleVarifyResponse.getIs_female_verify().toString());

            //  Log.e("CHECK_FEMALE_VARIFY", "isSuccess: " + new Gson().toJson(checkFemaleVarifyResponse));

            Log.e("CHECK_FEMALE_VARIFY", "isSuccess: checkFemaleVarifyResponse " + new Gson().toJson(checkFemaleVarifyResponse));


            if (checkFemaleVarifyResponse.getIs_female_verify() == 1) {


                new ApiManager(getContext(), HomeMenuFragment.this).checkTemporaryBlock();


            } else if (checkFemaleVarifyResponse.getIs_female_verify() == 4) {
                Log.e("CHECK_FEMALE_VARIFY", "isSuccess: not varified ");
                // showUnvarifiedFemaleDialog();
                setOnlineSwitch();

            } else {
                sessionManager.setWorkSession(false);
                setOnlineSwitch();

            }

        }else if (ServiceCode == Constant.DELETE_FEMALE_BROADLIST) {
            Deletelivebroadresponse deletelivebroadresponse = (Deletelivebroadresponse) response;

            //  Log.e("CHECK_FEMALE_VARIFY", "isSuccess: " + new Gson().toJson(checkFemaleVarifyResponse));

            Log.e("CHECK_FEMALE_VARIFY", "isSuccess: deletebroadResponse " + new Gson().toJson(deletelivebroadresponse));

if(deletelivebroadresponse.getSuccess())
            setOnlineSwitch();

        }else if (ServiceCode == Constant.CHECK_TEMPORARY_BLOCK) {

            TemporaryBlockResponse temporaryBlockResponse = (TemporaryBlockResponse) response;

            if (temporaryBlockResponse != null) {
                Log.e("CHECK_TEMPORARY_BLOCK", "isSuccess: not null " + new Gson().toJson(temporaryBlockResponse));

                TemporaryBlockResult temporaryBlockResult = temporaryBlockResponse.getResult();

                Long currentTime = temporaryBlockResult.getCurrent_time();
                Long endTime = temporaryBlockResult.getEnd_time();

                Long remainingTimeInMilliSec = endTime - currentTime;
                switchBtn.setChecked(false);

                showTemporaryBlockDialog(getTimeInString2(remainingTimeInMilliSec), temporaryBlockResult.getReason());

            } else {
                Log.e("CHECK_TEMPORARY_BLOCK", "isSuccess: null ");
                if (!sessionManager.getWorkSession()) {
                        /*Intent intent = new Intent(currentActivity, FastScreenActivity.class);
                        startActivity(intent);
                        changeIcon();*/

                    sessionManager.setWorkSession(true);

                }
                setOnlineSwitch();
                Log.e("CHECK_FEMALE_VARIFY", "isSuccess: verified ");
             /*   if (CheckPermission()) {

                    if (!sessionManager.getWorkSession()) {
                        *//*Intent intent = new Intent(currentActivity, FastScreenActivity.class);
                        startActivity(intent);
                        changeIcon();*//*

                        sessionManager.setWorkSession(true);

                    } else {

                       *//* switchBtn.setChecked(false);
                        Intent closePIPIntent = new Intent("FINISH_ACTIVITY_BROADCAST");
                        closePIPIntent.putExtra("BRODCAST_FOR_PIP", "FinishThisActivity");
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(closePIPIntent);
                        sessionManager.setWorkSession(false);*//*
                    }

                    Log.i("isWorkOn", "" + sessionManager.getWorkSession());

                } else {
                    Log.e("check1Sess","Yes13"+sessionManager.getWorkSession());

                }*/

            }

        }

        if (ServiceCode == Constant.GET_DAILY_EARNING) {
            DailyUserListResponse rsp = (DailyUserListResponse) response;
            //mSwipeRefreshLayout.setRefreshing(false);
            if (list.size() > 0) {
                list.clear();
            }
            list = rsp.getResult();
            //TOTAL_PAGES = rsp.getResult().getLast_page();
            //Log.e("inHostdata", new Gson().toJson(rsp.getResult()));
            if (list.size() > 0) {
                if (newDailyList.size() > 0) {
                    newDailyList.clear();
                }
                try {
                    /*for (int i = 3; i < list.size(); i++) {
                        newDailyList.add(list.get(i));
                    }*/

                    if (list.get(0).getName() != null) {
                        tvFirstAvatarName.setText(list.get(0).getName().toLowerCase());
                    } else {
                        tvFirstAvatarName.setText("NA");
                    }
                    tvFirstAvatarBean.setText(list.get(0).getTotal_coin_earned() + "");
                    tvCharmLevelOne.setText(list.get(0).getCharm_level() + "");
                    setCharmLevel(rlBgOne, 0);
                    Glide.with(this).load(list.get(0).getProfile_images().get(0).getImage_name())
                            .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                    .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                    .error(R.drawable.fake_user_icon)).into(ivAvatarOne);

                    if (list.get(1).getName() != null) {
                        tvSecondAvatarName.setText(list.get(1).getName().toLowerCase());
                    } else {
                        tvSecondAvatarName.setText("NA");
                    }
                    tvSecondAvatarBean.setText(list.get(1).getTotal_coin_earned() + "");
                    tvCharmLevelSecond.setText(list.get(1).getCharm_level() + "");
                    setCharmLevel(rlBgSecond, 1);
                    Glide.with(this).load(list.get(1).getProfile_images().get(0).getImage_name())
                            .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                    .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                    .error(R.drawable.fake_user_icon)).into(ivAvatarTwo);

                    if (list.get(2).getName() != null) {
                        tvThirdAvatarName.setText(list.get(2).getName().toLowerCase());
                    } else {
                        tvThirdAvatarName.setText("NA");
                    }
                    tvThirdAvatarBean.setText(list.get(2).getTotal_coin_earned() + "");
                    tvCharmLevelThree.setText(list.get(2).getCharm_level() + "");
                    setCharmLevel(rlBgThree, 2);
                    Glide.with(this).load(list.get(2).getProfile_images().get(0).getImage_name())
                            .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                    .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                    .error(R.drawable.fake_user_icon)).into(ivAvatarThree);
                    int beforeIndex = 3;
                    int endIndex = list.size();
                    newDailyList.addAll(list.subList(beforeIndex, endIndex));
                    dailyUsersListAdapter = new DailyUsersListAdapter(getContext(), newDailyList, getActivity());
                    rvUserList.setAdapter(dailyUsersListAdapter);
                    dailyUsersListAdapter.notifyDataSetChanged();

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

        }

        if (ServiceCode == Constant.GET_WEEKLY_EARNING) {
            WeeklyUserListResponse weeklyUserListResponse = (WeeklyUserListResponse) response;

            if (weelyList.size() > 0) {
                weelyList.clear();
            }
            weelyList = weeklyUserListResponse.getResult();
            if (weelyList.size() > 0) {
                if (newWeeklyList.size() > 0) {
                    newWeeklyList.clear();
                }
                try {
                    /*for (int i = 3; i < weelyList.size(); i++) {
                        newWeeklyList.add(weelyList.get(i));
                    }*/

                    if (weelyList.get(0).getUser() != null && weelyList.get(0).getUser().getName() != null) {
                        tvFirstAvatarName.setText(weelyList.get(0).getUser().getName().toLowerCase());
                    } else {
                        tvFirstAvatarName.setText("NA");
                    }
                    tvFirstAvatarBean.setText(weelyList.get(0).getTotal_coin_earned() + "");
                    if (weelyList.get(0).getUser() != null) {
                        tvCharmLevelOne.setText(weelyList.get(0).getUser().getCharm_level() + "");
                    } else {
                        tvCharmLevelOne.setText("0");
                    }
                    setCharmLevel(rlBgOne, 0);
                    if (weelyList.get(0).getUser().getProfile_images().get(0).getImage_name() != null) {
                        Glide.with(this).load(weelyList.get(0).getUser().getProfile_images().get(0).getImage_name())
                                .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                        .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                        .error(R.drawable.fake_user_icon)).into(ivAvatarOne);
                    }
                    if (weelyList.get(1).getUser() != null && weelyList.get(1).getUser().getName() != null) {
                        tvSecondAvatarName.setText(weelyList.get(1).getUser().getName().toLowerCase());
                    } else {
                        tvSecondAvatarName.setText("NA");
                    }
                    tvSecondAvatarBean.setText(weelyList.get(1).getTotal_coin_earned() + "");
                    if (weelyList.get(1).getUser() != null) {
                        tvCharmLevelSecond.setText(weelyList.get(1).getUser().getCharm_level() + "");
                    } else {
                        tvCharmLevelSecond.setText("0");
                    }
                    setCharmLevel(rlBgSecond, 1);
                    if (weelyList.get(1).getUser().getProfile_images().get(0).getImage_name() != null) {
                        Glide.with(this).load(weelyList.get(1).getUser().getProfile_images().get(0).getImage_name())
                                .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                        .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                        .error(R.drawable.fake_user_icon)).into(ivAvatarTwo);
                    }
                    if (weelyList.get(2).getUser() != null && weelyList.get(2).getUser().getName() != null) {
                        tvThirdAvatarName.setText(weelyList.get(2).getUser().getName().toLowerCase());
                    } else {
                        tvThirdAvatarName.setText("NA");
                    }
                    tvThirdAvatarBean.setText(weelyList.get(2).getTotal_coin_earned() + "");
                    if (weelyList.get(2).getUser() != null) {
                        tvCharmLevelThree.setText(weelyList.get(2).getUser().getCharm_level() + "");
                    } else {
                        tvCharmLevelThree.setText("0");
                    }
                    setCharmLevel(rlBgThree, 2);
                    if (weelyList.get(2).getUser().getProfile_images().get(0).getImage_name() != null) {
                        Glide.with(this).load(weelyList.get(2).getUser().getProfile_images().get(0).getImage_name())
                                .apply(new RequestOptions().placeholder(R.drawable.fake_user_icon)
                                        .override(getResources().getDimensionPixelSize(R.dimen._38sdp), getResources().getDimensionPixelSize(R.dimen._38sdp)) // resizing
                                        .error(R.drawable.fake_user_icon)).into(ivAvatarThree);
                    }

                    int beforeIndex = 3;
                    int endIndex = weelyList.size();
                    newWeeklyList.addAll(weelyList.subList(beforeIndex, endIndex));
                    weeklyUsersListAdapter = new WeeklyUsersListAdapter(getContext(), newWeeklyList, getActivity(),true);
                    rvUserList.setAdapter(weeklyUsersListAdapter);
                    weeklyUsersListAdapter.notifyDataSetChanged();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }

        if (ServiceCode == Constant.GET_DAILY_WEEKLY_EARNING) {
            DailyWeeklyEarningDetail earningDetail = (DailyWeeklyEarningDetail) response;
            if (earningDetail.getResult().getNext_week_date() != null) {
                tv_next_week.setText("Next Week: (Starting " + DateFormatter.getInstance().formatDDMMM(earningDetail.getResult().getNext_week_date()) + ")");
            } else {
                tv_next_week.setText("Next Week: (Starting NA");
            }
            tv_per_minuit.setText(earningDetail.getResult().getCall_rate() + "/min");
            tv_weekly_earning.setText(earningDetail.getResult().getWeekly_earning() + "");
            tv_today_call.setText(earningDetail.getResult().getToday_total_calls() + "");
            tv_today_earning.setText(earningDetail.getResult().getToday_total_earning() + "");
            tv_referral_earning.setText(earningDetail.getResult().getReferal_earning() + "");
            tv_call_earning.setText(earningDetail.getResult().getToday_call_earning() + "");
            tv_gift_earning.setText(earningDetail.getResult().getToday_gift_earning() + "");
            tv_other.setText(earningDetail.getResult().getToday_other_earning() + "");
            selfCount = earningDetail.getResult().getWeekly_earning() + "";
            swipeToRefreshfem.setRefreshing(false);
        }

        if (ServiceCode == Constant.GET_WEEKLY_REWARD) {
            WeeklyUserRewardResponse rewardResponse = (WeeklyUserRewardResponse) response;
            weeklyRewardDataList.addAll(rewardResponse.getResult().getWeeklyreward());
        }
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
        temporaryBlockDialog.setCanceledOnTouchOutside(false);
        temporaryBlockDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView unVarifiedText = temporaryBlockDialog.findViewById(R.id.tv_unvarifiedMessage);
        Button OKbtn = temporaryBlockDialog.findViewById(R.id.btn_gotit);

        timeInString = timeInString + ".";

        String sourceString = "Due to" + " <b>" + reason + "</b> " + "your account has been temporary ban for next" + " <b>" + timeInString + "</b> " + "if you have any Query contact to your agency.";
        unVarifiedText.setText(Html.fromHtml(sourceString));

        // unVarifiedText.setText("You have not yet verified as host.\nPlease contact to your agency for completing the host verification.");

        OKbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchBtn.setChecked(false);
                temporaryBlockDialog.dismiss();
            }
        });

        temporaryBlockDialog.show();
    }

}