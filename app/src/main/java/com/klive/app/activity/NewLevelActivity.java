package com.klive.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.klive.app.R;
import com.klive.app.databinding.ActivityNewLevelBinding;
import com.klive.app.dialogs.MyProgressDialog;
import com.klive.app.utils.BaseActivity;
import com.klive.app.utils.SessionManager;

public class NewLevelActivity extends BaseActivity {

    private String baseUrl = "https://ringlive.in/socialmedia/female-level/";
    private String userId;
    private String pageUrl = "";
    private MyProgressDialog progressDialog;
    private ActivityNewLevelBinding binding;
    private SessionManager sessionManager;

    String TAG = "NewLevelActivity";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_level);
        progressDialog = new MyProgressDialog(this);
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();
        pageUrl = baseUrl + userId;

        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(true);
        binding.webview.getSettings().setLoadsImagesAutomatically(true);
        binding.webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        startWebView(pageUrl);


        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void startWebView(String pageUrl) {
        progressDialog.show();

        binding.webview.setWebViewClient(new WebViewClient() {

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
        binding.webview.loadUrl(pageUrl);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}