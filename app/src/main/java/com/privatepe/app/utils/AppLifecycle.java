package com.privatepe.app.utils;


import static com.privatepe.app.utils.SessionManager.PROFILE_ID;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.privatepe.app.Firestatus.FireBaseStatusManage;
import com.privatepe.app.ZegoExpress.AuthInfoManager;
/*import com.privatepe.app.ZegoExpress.zim.ZimManager;*/
import com.privatepe.app.fudetector.faceunity.FURenderer;
import com.privatepe.app.fudetector.faceunity.authpack;
import com.privatepe.app.sqlite.Chat;
import com.privatepe.app.sqlite.ChatDB;


import java.util.HashMap;

import im.zego.zegoexpress.ZegoExpressEngine;

import im.zego.zegoexpress.constants.ZegoScenario;

import im.zego.zegoexpress.entity.ZegoEngineProfile;

/*
import im.zego.zim.entity.ZIMTextMessage;
import im.zego.zim.enums.ZIMErrorCode;
*/


public class AppLifecycle extends Application implements LifecycleObserver {

    private static Activity curActivity;
    HashMap<String, String> user;
    private String AppID = "";

    //ChatListener chatListener = new InboxDetails();

    private static Context appContext;
    public static boolean wasInBackground;

    public static boolean isAppInBackground;
    public static boolean AppInBackground = false;

    public static boolean isChatActivityInFront;


    private final String TAG = AppLifecycle.class.getSimpleName();
    public static AppLifecycle mInstance;

    public static String ZEGOTOKEN;
    private String mesaagewithcall;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        mInstance = this;

        if (authpack.A() != null) {
            FURenderer.initFURenderer(this);
        }

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        setActivityCallback();
        initZegoSdk();
    }


    public static Context getInstance() {
        return appContext;
    }


    public static AppLifecycle getAppInstance() {
        return mInstance;
    }
    public static boolean isChatActivityOpen() {
        return AppLifecycle.isChatActivityInFront;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        // app moved to foreground
        // wasInBackground = true;
        // isAppInBackground=false;
        AppInBackground = false;
        Log.e("app_state", "onMoveToBackground: " + AppInBackground);

        user = new SessionManager(appContext).getUserDetails();
        ZEGOTOKEN = AuthInfoManager.getInstance().generateToken(user.get(PROFILE_ID));
        // FirebaseDatabase.getInstance().goOnline();
        Log.e("Applifecycle", "appInForground");
        Log.e(TAG, "onMoveToForeground: " + user.get(PROFILE_ID));
      //  loginZim(user.get(NAME), user.get(PROFILE_ID), user.get(PROFILE_PIC));

        // loginZim(user.get(NAME), "12553781", user.get(PROFILE_PIC));


    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onMoveToBackground() {
        // app moved to background
        // wasInBackground = false;
        isAppInBackground = true;
        AppInBackground = true;

        Log.e("app_state", "onMoveToBackground: " + AppInBackground);
        Log.e("Applifecycle", "appInBackground");
         /*    if (new SessionManager(getApplicationContext()).getHostAutopickup().equals("yes")) {

            Log.i("background", "conditional");

            Intent myIntent = new Intent("KAL-CLOSEME");
            myIntent.putExtra("action", "closeme");
            this.sendBroadcast(myIntent);
        }*/
        //  new ApiManager(appContext).changeOnlineStatusBack(0);
        new SessionManager(appContext).setUserLoaddata();

        /*      Intent closePIPIntent = new Intent("FINISH_ACTIVITY_BROADCAST");
        closePIPIntent.putExtra("BRODCAST_FOR_PIP", "FinishThisActivity");
        LocalBroadcastManager.getInstance(appContext).sendBroadcast(closePIPIntent);
        new SessionManager(appContext).setWorkSession(false);*/

        Log.i("background", "without condition");

        //  FirebaseDatabase.getInstance().goOffline();
        new FireBaseStatusManage(appContext, new SessionManager(appContext).getUserId(), new SessionManager(appContext).getUserName(),
                "", "", "Offline");
    }

    public static AppLifecycle the() {
        return mInstance;
    }

    public AppLifecycle() {
        mInstance = this;
    }


    private void initZegoSdk() {
       // ZimManager.sharedInstance().init(1052832069, this);
        ZegoEngineProfile profile = new ZegoEngineProfile();
        profile.appID = 1052832069;
        profile.application = this;
        profile.scenario = ZegoScenario.GENERAL;
        ZegoExpressEngine.createEngine(profile, null);
        ZegoExpressEngine.getEngine().enableHardwareEncoder(true);
        ZegoExpressEngine.getEngine().enableHardwareDecoder(true);
    }


    public void loginZim(String username, String userId, String userIcon) {
        Log.d(TAG, "loginZim: ");

     //   String token = AuthInfoManager.getInstance().generateToken(userId);

     /*
        ZimManager.sharedInstance().loginZim(userId, username, userIcon, token, new ResultCallback() {
            @Override
            public void onZimCallback(ZIMErrorCode errorCode, String errMsg) {
                if (errorCode == ZIMErrorCode.SUCCESS) {
                    // Log.e("login", "success");
                    Log.e(TAG, "onZimCallback: LoginZim  userid " + userId + "  login success");
                    Log.e(TAG, "onZimCallback: " + "login success");

                } else {
                    //  Log.e("login", "fail " + errorCode);
                    Log.e(TAG, "onZimCallback: LoginZim " + " Fail with ErrorCode " + errorCode);

                }
            }
        });

        */

    }

    public void sendZegoChatMessage(String peerId, String message_content, String date, String time, String userName, String userProfilePic) {
      /*  JSONObject messageObject = new JSONObject();
        JSONObject chatDataObject = new JSONObject();
        try {
            chatDataObject.put("MessageContent", message_content);
            chatDataObject.put("Date", date);
            chatDataObject.put("Time", time);
            chatDataObject.put("ProfilePic", userProfilePic);
            chatDataObject.put("UserName", userName);
            messageObject.put("isMessageWithChat", "yes");
            messageObject.put("ChatMessageBody", chatDataObject.toString());
        } catch (JSONException e) {

        }

        ZIMTextMessage zimMessage = new ZIMTextMessage();
        zimMessage.message = messageObject.toString();
        ZimManager.sharedInstance().sendMessage(zimMessage, peerId);*/
    }

    private void saveChatInDb(String peerId, String name, String sentMsg, String recMsg, String date, String sentTime, String recTime, String image, String chatType) {
        ChatDB db = new ChatDB(appContext);
        String timesttamp = System.currentTimeMillis() + "";
        db.addChat(new Chat(peerId, name, "", recMsg, date, "", recTime, image, 0, timesttamp, chatType));
        Intent intent = new Intent("MSG-UPDATE");
        intent.putExtra("peerId", peerId);
        intent.putExtra("msg", "receive");
        appContext.sendBroadcast(intent);
        Log.e("recievemsggg", "savedChatInDB called");
        Log.e("MessageSavedInChat", "saved");
    }


    private void setActivityCallback() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                curActivity = activity;
                // Log.e(TAG, "onActivityStarted: "+activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.e(TAG, "onActivityResumed: " + "activity: " + activity + "  resumed.");
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }


    //get current activity
    public static Activity getActivity() {
        return curActivity;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        ZegoExpressEngine.destroyEngine(null);
        //ZimManager.sharedInstance().destroyZim();

    }


}