package com.privatepe.host;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.faceunity.nama.FUConfig;
import com.faceunity.nama.utils.FuDeviceUtils;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.SessionManager;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends BaseActivity {
    SessionManager sessionManager;
    int SPLASH_DISPLAY_LENGTH = 3000;
    Handler splashHandler=new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(),true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sessionManager = new SessionManager(this);

        Log.e("jjjjjj", "onCreate: "+"splash");

        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preference_login), Context.MODE_PRIVATE);
                String username = sharedPref.getString("profile_id", "");
                Intent i;
                if(!username.equals("")){
                    i = new Intent(SplashScreen.this, Home.class);
                } else {
                    i = new Intent(SplashScreen.this, LoginActivity.class);
                }
                startActivity(i);
            }
        }, SPLASH_DISPLAY_LENGTH);*/

        splashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("cjeckalogi",""+new SessionManager(getApplicationContext()).isLoggedIn());
                new SessionManager(getApplicationContext()).setUserLoaddata();
                //   sessionManager.checkLogin();
                new SessionManager(getApplicationContext()).setHostAutopickup("no");
                String c_name = new SessionManager(getApplicationContext()).getUserLocation();
                /*if (c_name.equals("null")) {
                    Intent i = new Intent(Splash.this, SocialLogin.class);
                    startActivity(i);
                } else {
                */
               // sessionManager.setResUpload("3");

                sessionManager.checkLogin();
                // }
               finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashHandler.removeCallbacksAndMessages(null);
    }
}