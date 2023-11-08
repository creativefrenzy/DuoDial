package com.privatepe.app.activity;

import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.privatepe.app.R;
import com.privatepe.app.databinding.ActivityLevelUpBinding;
import com.privatepe.app.dialogs.InsufficientCoinsMyaccount;
import com.privatepe.app.dialogs.MyProgressDialog;
import com.privatepe.app.fragments.metend.MyAccountFragment;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.SessionManager;

public class LevelUpActivity extends BaseActivity implements ApiResponseInterface {
    ActivityLevelUpBinding binding;
    SessionManager sessionManager;
    String Profile_Id;
    private WebView webview;
    ImageView img_back;
    MyProgressDialog progressDialog;
    String url;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(),false);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_level_up);
        sessionManager = new SessionManager(this);
        binding.setClickListener(new EventHandler(this));

        init();
    }

    @SuppressLint("JavascriptInterface")
    void init() {
       /* Glide.with(this).load(sessionManager.getUserProfilepic())
                .apply(new RequestOptions().placeholder(R.drawable.default_profile).error
                        (R.drawable.default_profile).circleCrop()).into(binding.imgUser);*/
        //  binding.tvUserName.setText(sessionManager.getUserName());
        Log.e("maleLevel", "currentLevel " + String.valueOf(sessionManager.getLevel()));
        //  calcLevel(sessionManager.getLevel());
        //calcLevel(6);
        // new MyProgressDialogNew(this);

        webview = findViewById(R.id.webview);
        img_back= findViewById(R.id.img_back);
        progressDialog = new MyProgressDialog(this);
        Profile_Id = getIntent().getStringExtra("Profile_Id");
        url = "https://ringlive.in/socialmedia/user-level/" + Profile_Id;
        Log.e("urlprofileid", "url " + url);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);


        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        startWebView(url);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void backPageLevel() {
            finish();
        }

        public void reCharge() {
            new InsufficientCoinsMyaccount(mContext, 2, Long.parseLong(MyAccountFragment.availableCoins));
        }
    }

    private void startWebView(String url) {
        progressDialog.show();

        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webview.setWebViewClient(new WebViewClient() {

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

                progressDialog.dismiss();
            }
        });

        webview.loadUrl(url);

   /* private void calcLevel(int level) {
        int dataTocalc = level;
        if (dataTocalc <= 0) {
            // lv0
            binding.tvCurrentLevel.setText("Lv 0");
            binding.tvNextLevel.setText("Lv 1");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_0);
            displayProgress(3000, 0, 5);
        } else if (dataTocalc <= 1) {
            // lv1
            binding.tvCurrentLevel.setText("Lv 1");
            binding.tvNextLevel.setText("Lv 2");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_1);
            displayProgress(3000, 0, 10);

        } else if (dataTocalc <= 2) {
            //lv2
            binding.tvCurrentLevel.setText("Lv 2");
            binding.tvNextLevel.setText("Lv 3");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_2);
            displayProgress(3000, 0, 20);

        } else if (dataTocalc <= 3) {
            //lv3
            binding.tvCurrentLevel.setText("Lv 3");
            binding.tvNextLevel.setText("Lv 4");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_3);
            displayProgress(3000, 0, 30);

        } else if (dataTocalc <= 4) {
            //lv4
            binding.tvCurrentLevel.setText("Lv 4");
            binding.tvNextLevel.setText("Lv 5");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_4);
            displayProgress(3000, 0, 40);

        } else if (dataTocalc <= 5) {
            //lv5
            binding.tvCurrentLevel.setText("Lv 5");
            binding.tvNextLevel.setText("Lv 6");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_5);
            displayProgress(3000, 0, 50);

        } else if (dataTocalc <= 6) {
            //lv6
            binding.tvCurrentLevel.setText("Lv 6");
            binding.tvNextLevel.setText("Lv 7");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_6);
            displayProgress(3000, 0, 60);

        } else if (dataTocalc <= 7) {
            //lv7
            binding.tvCurrentLevel.setText("Lv 7");
            binding.tvNextLevel.setText("Lv 8");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_7);
            displayProgress(3000, 0, 70);

        } else if (dataTocalc <= 8) {
            //lv8
            binding.tvCurrentLevel.setText("Lv 8");
            binding.tvNextLevel.setText("Lv 9");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_8);
            displayProgress(3000, 0, 80);

        } else if (dataTocalc <= 9) {
            //lv9
            binding.tvCurrentLevel.setText("Lv 9");
            binding.tvNextLevel.setText("Lv 10");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_9);
            displayProgress(2000, 0, 90);

        } else if (dataTocalc <= 10) {
            //lv10
            binding.tvCurrentLevel.setText("Lv 10");
            binding.tvNextLevel.setText("");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_10);
            displayProgress(2000, 0, 100);

        }
    }

    private void displayProgress(int animationDuration, int from, int progress) {
        binding.progressBar.animateProgress(animationDuration, from, progress);
        binding.progressBar.setProgressColors(ResourcesCompat.getColor(getResources(), R.color.white, null), ResourcesCompat.getColor(getResources(), R.color.blue, null));
        binding.tvLevelPercentage.setText(String.valueOf(progress) + "%");


    }*/

    }


}


/*
public class LevelUpActivity extends BaseActivity implements ApiResponseInterface {
    ActivityLevelUpBinding binding;
    String Profile_Id;
    SessionManager sessionManager;
    private WebView webview;
    MyProgressDialog progressDialog;
    String url;
    JavaScriptInterface JSInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(), true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_level_up);
        sessionManager = new SessionManager(this);
        binding.setClickListener(new EventHandler(this));
        init();
    }

    void init() {
      */
/*  Glide.with(this).load(sessionManager.getUserProfilepic())
                .apply(new RequestOptions().placeholder(R.drawable.default_profile).error
                        (R.drawable.default_profile).circleCrop()).into(binding.imgUser);
        binding.tvUserName.setText(sessionManager.getUserName());
        Log.e("maleLevel", "currentLevel " + String.valueOf(sessionManager.getLevel()));
        calcLevel(sessionManager.getLevel());
        calcLevel(6);*//*

        webview = findViewById(R.id.webview);
        progressDialog = new MyProgressDialog(this);
        Profile_Id=getIntent().getStringExtra("Profile_Id");
        url="https://ringlive.in/socialmedia/user-level/"+Profile_Id;
        Log.e("urlprofileid", "url " + url);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        startWebView(url);

        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;

                    switch(keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });



    }

    public class JavaScriptInterface {
        Context mContext;

        */
/** Instantiate the interface and set the context *//*

        JavaScriptInterface(Context c) {
            mContext = c;
        }


    }
    public void reCharge() {
        new InsufficientCoinsMyaccount(getApplicationContext(), 2, Integer.parseInt(MyAccountFragment.availableCoins));
    }
    private void startWebView(String url) {
        progressDialog.show();

        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webview.setWebViewClient(new WebViewClient() {

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

                progressDialog.dismiss();
            }
        });

        //Load url in webview
        webview.loadUrl(url);
    }




    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void backPageLevel() {
            finish();
        }


    }

   */
/* private void calcLevel(int level) {
        int dataTocalc = level;
        if (dataTocalc <= 0) {
            // lv0
            binding.tvCurrentLevel.setText("Lv 0");
            binding.tvNextLevel.setText("Lv 1");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_0);
            displayProgress(3000, 0, 5);
        } else if (dataTocalc <= 1) {
            // lv1
            binding.tvCurrentLevel.setText("Lv 1");
            binding.tvNextLevel.setText("Lv 2");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_1);
            displayProgress(3000, 0, 10);

        } else if (dataTocalc <= 2) {
            //lv2
            binding.tvCurrentLevel.setText("Lv 2");
            binding.tvNextLevel.setText("Lv 3");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_2);
            displayProgress(3000, 0, 20);

        } else if (dataTocalc <= 3) {
            //lv3
            binding.tvCurrentLevel.setText("Lv 3");
            binding.tvNextLevel.setText("Lv 4");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_3);
            displayProgress(3000, 0, 30);

        } else if (dataTocalc <= 4) {
            //lv4
            binding.tvCurrentLevel.setText("Lv 4");
            binding.tvNextLevel.setText("Lv 5");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_4);
            displayProgress(3000, 0, 40);

        } else if (dataTocalc <= 5) {
            //lv5
            binding.tvCurrentLevel.setText("Lv 5");
            binding.tvNextLevel.setText("Lv 6");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_5);
            displayProgress(3000, 0, 50);

        } else if (dataTocalc <= 6) {
            //lv6
            binding.tvCurrentLevel.setText("Lv 6");
            binding.tvNextLevel.setText("Lv 7");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_6);
            displayProgress(3000, 0, 60);

        } else if (dataTocalc <= 7) {
            //lv7
            binding.tvCurrentLevel.setText("Lv 7");
            binding.tvNextLevel.setText("Lv 8");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_7);
            displayProgress(3000, 0, 70);

        } else if (dataTocalc <= 8) {
            //lv8
            binding.tvCurrentLevel.setText("Lv 8");
            binding.tvNextLevel.setText("Lv 9");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_8);
            displayProgress(3000, 0, 80);

        } else if (dataTocalc <= 9) {
            //lv9
            binding.tvCurrentLevel.setText("Lv 9");
            binding.tvNextLevel.setText("Lv 10");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_9);
            displayProgress(2000, 0, 90);

        } else if (dataTocalc <= 10) {
            //lv10
            binding.tvCurrentLevel.setText("Lv 10");
            binding.tvNextLevel.setText("");
            binding.imageLevel.setImageResource(R.mipmap.icon_level_10);
            displayProgress(2000, 0, 100);

        }
    }

    private void displayProgress(int animationDuration, int from, int progress) {
        binding.progressBar.animateProgress(animationDuration, from, progress);
        binding.progressBar.setProgressColors(ResourcesCompat.getColor(getResources(), R.color.white, null), ResourcesCompat.getColor(getResources(), R.color.blue, null));
        binding.tvLevelPercentage.setText(String.valueOf(progress) + "%");


    }*//*

   @Override
   public void onBackPressed() {
       if (webview.canGoBack()) {
           webview.goBack();
       } else {
           super.onBackPressed();
       }
   }
*/
