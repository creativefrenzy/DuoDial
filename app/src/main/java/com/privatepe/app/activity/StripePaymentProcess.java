package com.privatepe.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.privatepe.app.R;
import com.privatepe.app.dialogs.PaymentCompletedStripeDialog;
import com.privatepe.app.response.Stripe.ServerResponceStripe;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;
import com.google.gson.Gson;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.view.CardInputWidget;

public class StripePaymentProcess extends AppCompatActivity implements ApiResponseInterface {

    public String planId, planAmount;

    private String paymentIntentClientSecret;
    private Stripe stripe;
    private TextView mAmount;
    Button payButton;
    CardInputWidget cardInputWidget;
    ApiManager apiManager;
    ImageView img_sloading;

    PaymentSheet paymentSheet;
    PaymentSheet.CustomerConfiguration customerConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_stripe_payment_process);

        payButton = findViewById(R.id.purchase);
        cardInputWidget = findViewById(R.id.cardInputWidget);

        img_sloading = findViewById(R.id.img_sloading);
        apiManager = new ApiManager(this, this);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        PaymentConfiguration.init(getApplicationContext(), "pk_live_51Hro6MGZ38XEUVpOYaH7yDIKinw9SV9GRUYZKTpvXQFaDW02yprHg6lkXgS1fODtRcCjueLR3g7cLJ9tV4EtGsLb008m9XIRGl");

        getInten();

        registerReceiver(myReceiver, new IntentFilter("kal-showDialog"));

    }

    public void getInten() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            planId = bundle.getString("planid");
            planAmount = bundle.getString("planamount");
            apiManager.createStripePayment(planId, new SessionManager(getApplicationContext()).getUserName(), new SessionManager(getApplicationContext()).getUserId());
        }

    }


    private void presentPaymentSheet() {

        PaymentSheet.Address address =
                new PaymentSheet.Address.Builder()
                        .country("IN")
                        .build();

        PaymentSheet.BillingDetails billingDetails =
                new PaymentSheet.BillingDetails.Builder()
                        .address(address)
                        .build();

        customerConfig = new PaymentSheet.CustomerConfiguration(
                new SessionManager(getApplicationContext()).getUserName(),
                paymentIntentClientSecret
        );

        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder(getResources().getString(R.string.app_name))
                .customer(customerConfig)
                .defaultBillingDetails(billingDetails)
                .allowsDelayedPaymentMethods(true)
                .build();

        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d("stripeLog", "Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e("stripeLog", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Log.d("stripeLog", "Completed");
        }
        Log.e("stripeLogs", new Gson().toJson(paymentSheetResult));
        if (canExit) {
            finish();
        }
    }

    boolean canExit = true;

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.STRIPE_INIT) {
            ServerResponceStripe rsp = (ServerResponceStripe) response;
            paymentIntentClientSecret = rsp.getId();
            presentPaymentSheet();
        }

    }

    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getStringExtra("action");

            if (action.equals("show")) {
                canExit = false;
                new PaymentCompletedStripeDialog(StripePaymentProcess.this, "Payment successful.", Integer.parseInt(planAmount));
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}