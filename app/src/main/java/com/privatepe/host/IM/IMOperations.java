package com.privatepe.host.IM;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMLogListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMOfflinePushConfig;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;

public class IMOperations {

    V2TIMSDKListener sdkListener;

    public IMOperations(Context context) {
        initIM(context);
    }

    private void initIM(Context context) {
        V2TIMSDKConfig config = new V2TIMSDKConfig();
        // Specify the log output level
        config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO);
        // Specify the log listener
        V2TIMManager.getInstance().initSDK(context, 50000133, config);
        //
        setIMInterface(config);

        // The `sdkListener` type is `V2TIMSDKListener`.
        V2TIMManager.getInstance().addIMSDKListener(sdkListener);

    }

    private void setIMInterface(V2TIMSDKConfig config) {
        config.setLogListener(new V2TIMLogListener() {
            @Override
            public void onLog(int logLevel, String logContent) {
                // `logContent` is the SDK log content
            }
        });

        sdkListener = new V2TIMSDKListener() {
            @Override
            public void onConnecting() {
                super.onConnecting();
            }

            @Override
            public void onConnectSuccess() {
                super.onConnectSuccess();
            }

            @Override
            public void onConnectFailed(int code, String error) {
                super.onConnectFailed(code, error);
            }
        };
    }
    private void regOfflinePush() {


        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {



                V2TIMOfflinePushConfig v2TIMOfflinePushConfig = null;
// Set `businessID` as the certificate ID of the vendor and `isTPNSToken` as `false`, and report the token obtained after registration of the vendor push service.
                v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(19337, token, false);
                V2TIMManager.getOfflinePushManager().setOfflinePushConfig(v2TIMOfflinePushConfig, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        Log.e("offlinePushLog", "setOfflinePushToken err code = " + code);
                    }

                    @Override
                    public void onSuccess() {
                        Log.e("offlinePushLog", "setOfflinePushToken success token => "+token);

                        // When the app is switched to the background
                        V2TIMManager.getOfflinePushManager().doBackground(1, new V2TIMCallback() {
                            @Override
                            public void onError(int code, String desc) {
                                Log.e("offlinePushLog", "doBackground err = " + code + ", desc = " + desc);
                            }

                            @Override
                            public void onSuccess() {
                                Log.e("offlinePushLog", "doBackground success");
                            }
                        });
                        // When the app is switched back to the foreground
                        V2TIMManager.getOfflinePushManager().doForeground(new V2TIMCallback() {
                            @Override
                            public void onError(int code, String desc) {
                                Log.e("offlinePushLog", "doForeground err = " + code + ", desc = " + desc);
                            }

                            @Override
                            public void onSuccess() {
                                Log.e("offlinePushLog", "doForeground success");
                            }
                        });

                    }
                });

            } else {
                //Log.e("FirebaseTokenLog", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task ->
                Log.e("FirebaseTokenLog", "This is the token : " /*+ task.getResult()*/)
        );

    }

    public void loginIm(String userId) {
        String userID = userId;
        String userSig = GenerateTestUserSig.genTestUserSig(userID);

        V2TIMManager.getInstance().login(userID, userSig, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                //   Log.i("traceLog", "success with userID => " + userID);

                // Get the `UserID` of the logged-in user
                String loginUserID = V2TIMManager.getInstance().getLoginUser();
                // Log.i("traceLog", "loginUserID =>" + loginUserID);


                int loginStatus = V2TIMManager.getInstance().getLoginStatus();
                //  Log.i("traceLog", "loginStatus =>" + loginStatus);
                //regOfflinePush();
            }


            @Override
            public void onError(int code, String desc) {
                // The following error codes indicate an expired `userSig`, and you need to generate a new one for login again.
                // 1. ERR_USER_SIG_EXPIRED (6206)
                // 2. ERR_SVR_ACCOUNT_USERSIG_EXPIRED (70001)
                // Note: Do not call the login API in case of other error codes; otherwise, the IM SDK may enter an infinite loop of login.
                //  Log.i("traceLog", "failure, code:" + code + ", desc:" + desc);
            }
        });

    }

    public void releaseIM() {
        V2TIMManager.getInstance().removeIMSDKListener(sdkListener);


        /*V2TIMManager.getInstance().logout(new V2TIMCallback() {
            @Override
            public void onSuccess() {
                Log.i("imsdk", "success");
            }


            @Override
            public void onError(int code, String desc) {
                Log.i("imsdk", "failure, code:" + code + ", desc:" + desc);
            }
        });

        // Uninitialize the SDK
        V2TIMManager.getInstance().unInitSDK();*/
    }
}
