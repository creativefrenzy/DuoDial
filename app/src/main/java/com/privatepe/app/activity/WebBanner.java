package com.privatepe.app.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.privatepe.app.R;
import com.privatepe.app.dialogs.MyProgressDialog;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.SessionManager;

public class WebBanner extends BaseActivity {

    WebView webView;
    String url,name;
    RelativeLayout rl_close;
    TextView tv_username;

    MyProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(),true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_banner);
        webView = (WebView)findViewById(R.id.webvview);
        rl_close= (RelativeLayout)findViewById(R.id.rl_close);
        url=getIntent().getStringExtra("url");
        name=getIntent().getStringExtra("name");
        tv_username= (TextView) findViewById(R.id.tv_username);
        init();
    }

    void init() {
        progressDialog = new MyProgressDialog(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        url=url+new SessionManager(getApplicationContext()).getUserId();
        startWebView(url);
        tv_username.setText(name);
        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private void startWebView(String url) {
        progressDialog.show();

        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webView.setWebViewClient(new WebViewClient() {

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
        webView.loadUrl(url);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}