package com.klive.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.klive.app.R;
import com.klive.app.dialogs.MyProgressDialog;

public class PrivacyPolicyActivity extends AppCompatActivity {

    MyProgressDialog progressDialog;
    private WebView webview;
    TextView heading;
    String Policy;
    ImageView back_arrow;
    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_privacy_policy);
        setSupportActionBar(findViewById(R.id.toolbar));
        Policy = getIntent().getStringExtra("Policy");
        init();

    }

    void init() {
        progressDialog = new MyProgressDialog(this);

        webview = findViewById(R.id.webview);
        back_arrow = findViewById(R.id.back_arrow);
        heading = findViewById(R.id.heading);
        if (Policy.equals("UserAgreement")) {
            url = "https://video.ringlive.in/Klive/Agreement.html";
            heading.setText("User Agreement");
        } else {
            url = "https://video.ringlive.in/Klive/Privacy.html";
            heading.setText("Privacy Policy");
        }

       /* webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);*/

        startWebView(url);
        back_arrow.setOnClickListener(new View.OnClickListener() {
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}