package com.privatepe.app.fragments;

import static com.privatepe.app.main.Home.cardView;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.app.Fast_screen.FastScreenActivity;
import com.privatepe.app.Firestatus.FireBaseStatusManage;
import com.privatepe.app.R;
import com.privatepe.app.activity.WeeklyRankActivity;
import com.privatepe.app.adapter.HomeMenuPagerAdapter;
import com.privatepe.app.dialogs_agency.AddLibVideoDialog;
import com.privatepe.app.response.accountvarification.CheckFemaleVarifyResponse;
import com.privatepe.app.response.temporary_block.TemporaryBlockResponse;
import com.privatepe.app.response.temporary_block.TemporaryBlockResult;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.NetworkCheck;
import com.privatepe.app.utils.SessionManager;
import com.tencent.imsdk.common.IMCallback;
import com.tencent.imsdk.message.Message;
import com.tencent.imsdk.signaling.SignalingListener;
import com.tencent.imsdk.signaling.SignalingManager;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMSignalingInfo;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMSignalingManager;
import com.tencent.imsdk.v2.V2TIMSignalingManagerImpl;


import java.util.List;

public class UserMenuFragment extends BaseFragment implements ApiResponseInterface {
    private TabLayout tabLayout;
    private ViewPager tabViewpager;
    private NetworkCheck networkCheck;
    private HomeMenuPagerAdapter homeMenuPagerAdapter;
    private ImageView startWork;
    private ImageView img_rank, img_graph;

    private boolean isWorkOn = false;

    SharedPreferences sharedPreferences;


    BroadcastReceiver broadcastReceiver;

    SessionManager sessionManager;
    private Dialog unVarifiedDialog, temporaryBlockDialog;


    public UserMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_blank, container, false);
        View view = inflater.inflate(R.layout.fragment_user_menu, container, false);
        cardView.setVisibility(View.VISIBLE);
        networkCheck = new NetworkCheck();
        tabLayout = view.findViewById(R.id.tabLayout);
        tabViewpager = view.findViewById(R.id.tabViewpager);
        startWork = view.findViewById(R.id.btn_startwork);
        img_rank = view.findViewById(R.id.img_rank);
        new SessionManager(getContext()).setHostAutopickup("no");

        Log.e("CreatedFragment", "onCreateView: " + "UserMenuFragment");


        // sharedPreferences = getActivity().getSharedPreferences("VideoApp", Context.MODE_PRIVATE);


        sessionManager = new SessionManager(getContext());

        if (sessionManager.getWorkSession()) {
            startWork.setImageResource(R.drawable.off_work);
        } else {
            startWork.setImageResource(R.drawable.start);
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

                    startWork.setImageResource(R.drawable.start);
                    sessionManager.setWorkSession(false);

                }

                if (workedValue.equals("true")) {
                    // isWorkOn = true;
                    startWork.setImageResource(R.drawable.off_work);
                    sessionManager.setWorkSession(true);

                }

                Log.i("isWorkOn", "" + sessionManager.getWorkSession());

            }
        };

        new ApiManager(getContext(), UserMenuFragment.this).checkFemaleVarification();

        startWork.setOnClickListener(view1 -> {
            Log.e("UserMenuFragment", "onCreateView: Status Video List Size " + sessionManager.getStatusVideoListSize());

           /* if (sessionManager.getStatusVideoListSize() != null) {

                if (Integer.parseInt(sessionManager.getStatusVideoListSize()) == 0) {
                    Log.e("UserMenuFragment", "onCreateView: no video status");
                    new UploadStatusNotifyDialog(getContext(), new UploadStatusNotifyDialog.CloseBtnListener() {
                        @Override
                        public void onCloseBtnClick() {
                            new ApiManager(getContext(), UserMenuFragment.this).checkFemaleVarification();
                        }
                    });
                } else {
                    Log.e("UserMenuFragment", "onCreateView: you have video status");
                    new ApiManager(getContext(), UserMenuFragment.this).checkFemaleVarification();
                }


            }*/

            /*if (sessionManager.getWorkSession()) {
                startWork.setImageResource(R.drawable.off_work);
            } else {
                startWork.setImageResource(R.drawable.start);
            }*/
            if (sessionManager.getWorkSession()) {
                if (isLive) {
                    new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                            "", "", "Online");
                    isLive = false;
                    startWork.setImageResource(R.drawable.start);
                } else {
                    new FireBaseStatusManage(getContext(), sessionManager.getUserId(), sessionManager.getUserName(),
                            "", "", "Live");
                    isLive = true;
                    startWork.setImageResource(R.drawable.off_work);
                }
            } else {
                new ApiManager(getContext(), UserMenuFragment.this).checkFemaleVarification();
            }

          /*    Log.e("StartWork", "onCreateView: startWork " + CheckPermission());
            if (CheckPermission()) {
                if (!sessionManager.getWorkSession()) {
                    // Intent intent = new Intent(currentActivity, FastScreenNew.class);
                    Intent intent = new Intent(currentActivity, FastScreenActivity.class);
                    startActivity(intent);
                    changeIcon();
                    sessionManager.setWorkSession(true);
                } else {
                    startWork.setImageResource(R.drawable.start);
                    Intent closePIPIntent = new Intent("FINISH_ACTIVITY_BROADCAST");
                    closePIPIntent.putExtra("BRODCAST_FOR_PIP", "FinishThisActivity");
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(closePIPIntent);
                    sessionManager.setWorkSession(false);
                }

                Log.i("isWorkOn", "" + sessionManager.getWorkSession());


            } else {


            }*/
            //  if (!isWorkOn) {
            //    //  setInPref(true);
            //      isWorkOn=true;
            //      Intent intent = new Intent(currentActivity, FastScreenActivity.class);
            //      startActivity(intent);
            //      changeIcon();
            //      //startWork.setImageResource(R.drawable.off_work);
            //      // isWorkOn=true;

            //  } else {

            //      startWork.setImageResource(R.drawable.start);
            //       isWorkOn=false;
            //    //  setInPref(false);

            //      Intent closePIPIntent = new Intent("FINISH_ACTIVITY_BROADCAST");
            //      closePIPIntent.putExtra("BRODCAST_FOR_PIP", "FinishThisActivity");
            //      LocalBroadcastManager.getInstance(getContext()).sendBroadcast(closePIPIntent);
            //  }
            //  startWork.setImageResource(R.drawable.off_work);


        });
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
                startWork.setImageResource(R.drawable.off_work);
            }
        }, 1000);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*binding.fastmodeSwitch.setOnCheckedChangeListener((compoundButton, b) -> {

            if (((Home) getActivity()).isBlockFunction() == 1) {
                binding.fastmodeSwitch.setEnabled(false);
            } else {

            }
        });*/
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        homeMenuPagerAdapter = new HomeMenuPagerAdapter(getChildFragmentManager(), context);
        tabViewpager.setAdapter(homeMenuPagerAdapter);
        tabLayout.setupWithViewPager(tabViewpager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(homeMenuPagerAdapter.getTabView(i));

        }
        homeMenuPagerAdapter.setOnSelectView(tabLayout, 0);


    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                homeMenuPagerAdapter.setOnSelectView(tabLayout, position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                homeMenuPagerAdapter.setUnSelectView(tabLayout, position);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        img_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WeeklyRankActivity.class);
                startActivity(intent);
                // requireActivity().overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

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
    }

    @Override
    public void isError(String errorCode) {

    }

    AddLibVideoDialog addLibVideoDialog;

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.CHECK_FEMALE_VARIFY) {
            CheckFemaleVarifyResponse checkFemaleVarifyResponse = (CheckFemaleVarifyResponse) response;

            //  Log.e("CHECK_FEMALE_VARIFY", "isSuccess: " + new Gson().toJson(checkFemaleVarifyResponse));

            Log.e("CHECK_FEMALE_VARIFY", "isSuccess: checkFemaleVarifyResponse " + new Gson().toJson(checkFemaleVarifyResponse));


            if (checkFemaleVarifyResponse.getIs_female_verify() == 1) {


                new ApiManager(getContext(), UserMenuFragment.this).checkTemporaryBlock();


            } else if (checkFemaleVarifyResponse.getIs_female_verify() == 2) {
                Log.e("CHECK_FEMALE_VARIFY", "isSuccess: not varified ");
                //showUnvarifiedFemaleDialog();
                addLibVideoDialog = new AddLibVideoDialog(getContext());
            }


        } else if (ServiceCode == Constant.CHECK_TEMPORARY_BLOCK) {

            TemporaryBlockResponse temporaryBlockResponse = (TemporaryBlockResponse) response;

            if (temporaryBlockResponse != null) {
                Log.e("CHECK_TEMPORARY_BLOCK", "isSuccess: not null " + new Gson().toJson(temporaryBlockResponse));

                //    TemporaryBlockResult temporaryBlockResult=temporaryBlockResponse.getResult();


                //   Log.e("CHECK_TEMPORARY_BLOCK", "isSuccess: endTime "+new Gson().toJson(temporaryBlockResponse.getResult().getEnd_time()) );


                //  Long RemainingMillisec=temporaryBlockResult.getEnd_time()

                TemporaryBlockResult temporaryBlockResult = temporaryBlockResponse.getResult();


                Long currentTime = temporaryBlockResult.getCurrent_time();
                Long endTime = temporaryBlockResult.getEnd_time();


                Long remainingTimeInMilliSec = endTime - currentTime;


                //  getTimeInString2(remainingTimeInMilliSec);
                //  Log.e("timeDiff", "isSuccess: timeDiff "+timeDiff );


                showTemporaryBlockDialog(getTimeInString2(remainingTimeInMilliSec), temporaryBlockResult.getReason());

            } else {
                Log.e("CHECK_TEMPORARY_BLOCK", "isSuccess: null ");

                Log.e("CHECK_FEMALE_VARIFY", "isSuccess: verified ");
                if (CheckPermission()) {
                    if (!sessionManager.getWorkSession()) {

                        sessionManager.setWorkSession(true);
                    } else {
                        startWork.setImageResource(R.drawable.start);
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