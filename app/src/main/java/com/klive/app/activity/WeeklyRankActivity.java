package com.klive.app.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.klive.app.R;
import com.klive.app.dialogs.MyProgressDialog;
import com.klive.app.utils.SessionManager;

public class WeeklyRankActivity extends AppCompatActivity {

    WebView webView;
    String base_url = "https://ringlive.in/socialmedia/female-weekly-rewards?userid=";
    String userId, url;
    String name;
    RelativeLayout rl_close;
    TextView tv_username;
    MyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_rank);
        webView = (WebView) findViewById(R.id.webvview);
        rl_close = (RelativeLayout) findViewById(R.id.rl_close);
        tv_username = (TextView) findViewById(R.id.tv_username);
        userId = new SessionManager(this).getUserId();
        url = base_url + userId;
        init_();
    }

    private void init_() {
        progressDialog = new MyProgressDialog(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        startWebView(url);
        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void startWebView(String url) {
        progressDialog.show();
        webView.setWebViewClient(new WebViewClient() {

            //If you will not use this method url links are open in new browser not in webview

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
        webView.loadUrl(url);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
