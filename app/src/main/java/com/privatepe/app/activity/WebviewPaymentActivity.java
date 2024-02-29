package com.privatepe.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.privatepe.app.R;
import com.privatepe.app.databinding.ActivityWebviewPaymentBinding;
import com.privatepe.app.response.HaodaPayResponse.HaodaPayModel;

public class WebviewPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityWebviewPaymentBinding binding;
    private Activity activity;
    private String title = "", webviewurl = "";
    private HaodaPayModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListener();
        setUpWebView();
    }

    private void init() {
        activity = WebviewPaymentActivity.this;
        title = "Payment";
        webviewurl = getIntent().getStringExtra("weburl");
        model = getIntent().getParcelableExtra("model");
        binding.webview.setVisibility(View.VISIBLE);
        binding.emptyLyt.setVisibility(View.GONE);

        if (title != null && !title.equalsIgnoreCase("")) {
            binding.toolbarLayout.heading.setText(title);
        }
    }

    private void setListener() {
        binding.refreshBtn.setOnClickListener(this);
    }

    private void setUpWebView() {
        binding.webview.setWebChromeClient( new MyWebChromeClient());
        binding.webview.setWebViewClient( new WebClient());
        binding.webview.setVerticalScrollBarEnabled(true);
        binding.webview.getSettings().setLoadWithOverviewMode(true);
        binding.webview.getSettings().setSupportZoom(true);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.loadUrl(model.payment_link);
        binding.toolbarLayout.backArrow.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refreshBtn:
                if (isConnectionAvailable(activity)) {
                    binding.emptyLyt.setVisibility(View.GONE);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        binding.webview.setVisibility(View.VISIBLE);
                    }, 1000);
                    binding.webview.loadUrl(model.payment_link);
                }else{
                    binding.emptyLyt.setVisibility(View.VISIBLE);
                    binding.webview.setVisibility(View.GONE);
                }
                break;
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            binding.prloading.setVisibility(View.VISIBLE);
            binding.prloading.setProgress(newProgress);
        }
    }

    public class WebClient extends WebViewClient {
        public boolean  shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Uri uri = Uri.parse(url);
            Log.e("Check_JKPay","url "+url);
            Log.e("Check_JKPay","url Payment "+uri.getQueryParameter("payment"));
            /*if("success".equals(uri.getQueryParameter("payment"))) {
                setResultAndFinish(true);
            }
            else if("failed".equals(uri.getQueryParameter("payment"))) {
                setResultAndFinish(false);
            }*/
            binding.prloading.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            binding.webview.setVisibility(View.GONE);
            binding.emptyLyt.setVisibility(View.VISIBLE);
        }
    }

    private void setResultAndFinish( boolean isSuccess) {
        Intent intent = new Intent();
        if(isSuccess) {
            // intent.putExtra("result", paymentId);
            setResult(Activity.RESULT_OK, intent);
        } else {
            setResult(Activity.RESULT_CANCELED, intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(binding.webview.canGoBack()) {
            binding.webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.webview.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.webview.destroy();
    }

    public boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}