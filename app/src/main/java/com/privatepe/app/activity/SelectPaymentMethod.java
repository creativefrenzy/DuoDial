package com.privatepe.app.activity;

import static com.android.billingclient.api.Purchase.PurchaseState.PURCHASED;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.gson.Gson;
import com.privatepe.app.AppsFlyerPackage.AppsFlyerEvent;
import com.privatepe.app.Inbox.DatabaseHandler;
import com.privatepe.app.Inbox.InboxDetails;
import com.privatepe.app.Inbox.MessageBean;
import com.privatepe.app.Inbox.Messages;
import com.privatepe.app.Inbox.UserInfo;
import com.privatepe.app.R;
import com.privatepe.app.databinding.ActivitySelectPaymentMethodBinding;
import com.privatepe.app.dialogs.PaymentCompletedDialog;
import com.privatepe.app.response.ReportResponse;
import com.privatepe.app.response.metend.CreatePaymentResponse;
import com.privatepe.app.response.metend.DirectUPI.RazorpayPurchaseResponse;
import com.privatepe.app.response.metend.PaymentGatewayDetails.CashFree.CFToken.CfTokenResponce;
import com.privatepe.app.response.metend.PaymentGatewayDetails.CashFree.CashFreePayment.CashFreePaymentRequest;
import com.privatepe.app.response.metend.PaymentSelector.PaymentSelectorData;
import com.privatepe.app.response.metend.PaymentSelector.PaymentSelectorResponce;
import com.privatepe.app.response.metend.PaytmDirect.PaytmResponse;
import com.privatepe.app.response.metend.RechargePlan.RechargePlanResponseNew;
import com.privatepe.app.response.metend.upi.ActiveUpiResult;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectPaymentMethod extends BaseActivity implements ApiResponseInterface/*, PaykunResponseListener*/ {

    private static final int UPI_REQUEST_CODE = 0;
    final int UPI_PAYMENT = 0;
    ApiManager apiManager;
    SessionManager sessionManager;
    RechargePlanResponseNew.Data selectedPlan;

    String upiId;
    BillingClient billingClient;
    ActivitySelectPaymentMethodBinding binding;
    String customGpayPlan = "";
    private static final String TAG = "SelectPaymentMethod";
    String transactionId = "TID" + System.currentTimeMillis();
    String orderId;


    private String PHONEPE_PACKAGE_NAME = "com.phonepe.app";
    private String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private String PAYTM_PACKAGE_NAME = "net.one97.paytm";
    private String UPI_ID;

    private ActiveUpiResult ActiveUpiResultModel;

    //   private String GOOGLE_PAY_PACKAGE_NAME="net.one97.paytm";


    String PHONEPAYUPIID = null, GOOGLEPAYUPIID = null, PAYTMUPIID = null;
    private DatabaseHandler dbHandler;
    private int unreadCount;
    AppsFlyerEvent appsFlyerManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_payment_method);

        Constant.isReceivedFakeCall = false;
        dbHandler = new DatabaseHandler(this);

        //  binding.setClickListener(EventHandler(this));

            /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */

        //for testing purpose----------------
//        binding.coins.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new PaymentCompletedDialog(SelectPaymentMethod.this, "Payment successful.", selectedPlan.getAmount());
//            }
//        });
        //-----------------------------------------------------------------------------------------------------------------------------

        //setToolbarTitle("Select Payment Method");
        selectedPlan = (RechargePlanResponseNew.Data) getIntent().getSerializableExtra("selected_plan");
        //   Log.e("selectedPlan", new Gson().toJson(selectedPlan));
        upiId = getIntent().getStringExtra("upi_id");
        appsFlyerManager = AppsFlyerEvent.getInstance(getApplicationContext());

        if (selectedPlan.getType() == 2) {
            customGpayPlan = "video_call_" + selectedPlan.getAmount();
        } else if (selectedPlan.getType() == 6) {
            customGpayPlan = "text_chat_" + selectedPlan.getAmount();
        } else if (selectedPlan.getType() == 7) {
            //  customGpayPlan = "video_call_" + 100;
            customGpayPlan = "video_call_" + selectedPlan.getAmount();
        }

        binding.coins.setText(String.valueOf(selectedPlan.getPoints()));

        apiManager = new ApiManager(this, this);
        sessionManager = new SessionManager(this);


        // String rechargeCompleteMessage = "Recharge of ₹" + selectedPlan.getAmount() + " has been successfully done." + "You got " + selectedPlan.getPoints() + " coins.";
        //setNotification("Recharge of ₹"+selectedPlan.getAmount()+" has been successfully done."+ "You got "+selectedPlan.getPoints()+" coins.");

        //  apiManager.getActiveUpiList();

        if (sessionManager.getUserLocation().equals("India")) {
            binding.price.setText("₹ " + selectedPlan.getAmount());
        } else {

            binding.price.setText("₹ " + selectedPlan.getAmount());

          /*  binding.price.setText("$ " + selectedPlan.getAmount());
            binding.upi.setText("Debit Card/Credit Card");
            binding.gPay.setVisibility(View.VISIBLE);
            binding.upi.setVisibility(View.GONE);
            binding.gPay.setChecked(true);*/
        }


        binding.customToolbar.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.buttonPay.setOnClickListener(v -> {
            //Log.e("userLoation", sessionManager.getUserLocation());
            try {
                if (sessionManager.getUserLocation().equals("India")) {
                    checkSelectedPaymentMethod();
                } else {
                    /*
                    if (binding.upi.isChecked()) {
                        startActivity(new Intent(SelectPaymentMethod.this, StripePaymentProcess.class)
                                .putExtra("planid", String.valueOf(selectedPlan.getId()))
                                .putExtra("planamount", String.valueOf(selectedPlan.getAmount())));
                        finish();
                    } else {
                        Log.e("selectedPlan", selectedPlan.getType() + "");
                        startGpayGateway();
                    }
                    */
                  /*  apiManager.tokenForInAppPurchase(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()), mHash);
                    // Log.e("tokenForInAppPurchase", "data request" + selectedPlan.getAmount() + "  "+selectedPlan.getId());
                    startGpayGateway();*/

                    checkSelectedPaymentMethod();

                }
            } catch (Exception e) {
            }
        });

        // Setup In app purchase (G-Pay)
        setUpBilling();
        if (sessionManager.getUserLocation().equals("India")) {
            payment_Selector = "pk";
            Log.e("payment_Selector", "before calling api " + payment_Selector);
            apiManager.getPaymentSelector();
        } else {
            ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.ll_paycard)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.ll_paynetbanking)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.ll_paywallet)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_gpay)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_ppay)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_paynetbanking)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_paywallet)).setVisibility(View.GONE);
            binding.llPayupi.setVisibility(View.GONE);
            //  ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
            binding.buttonPay.setVisibility(View.VISIBLE);
            hash();
        }

        // new PaymentCompletedDialog(this, transactionId, selectedPlan.getAmount());

        binding.paytmPhonepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytmType = "paytmPhonepe";
                startPaytm();
            }
        });
        binding.paytmGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytmType = "paytmGoogle";
                startPaytm();
            }
        });
        binding.paytmUpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytmType = "all";
                startPaytm();
            }
        });


        Constant.CHECK_GPAY = isUPIInstalled(this, Constant.GOOGLE_PAY_PACKAGE_NAME);
        Constant.CHECK_PHONEPE = isUPIInstalled(this, Constant.PHONEPE_PACKAGE_NAME);
        Constant.CHECK_PAYTM = isUPIInstalled(this, Constant.PAYTM_PACKAGE_NAME);
    }

    private String paytmType = "";

    private void startPaytm() {
        apiManager.createpaymentpaytm(String.valueOf(selectedPlan.getId()));
    }

    private void setNotification(String msg) {

        Messages message = new Messages();
        message.setFrom("1");
        message.setMessage(msg);
        message.setFromName("System Message");
        message.setFromImage("https://ringlive.in/public/images/notification.png");

        message.setTime_stamp(System.currentTimeMillis());
        message.setType("text");
        String timestamp = System.currentTimeMillis() + "";

        Log.e("savedData", "saved");

        String contact_Id = insertOrUpdateContact(message, "1", "System Message", "https://ringlive.in/public/images/notification.png", timestamp);
        MessageBean messageBean;
        messageBean = new MessageBean(contact_Id, message, false, timestamp);
        dbHandler.addChat(messageBean);

        Intent refreshChatIN = new Intent("SAN-REFRESHCHATBROAD");
        refreshChatIN.putExtra("action", "refesh");
        sendBroadcast(refreshChatIN);

    }


    private String insertOrUpdateContact(Messages message, String userId, String profileName, String profileImage, String timestamp) {
        String currentUserId = new SessionManager(SelectPaymentMethod.this).getUserId();
        dbHandler = new DatabaseHandler(this);
        UserInfo userInfoFromDb = dbHandler.getContactInfo(userId, currentUserId);
        String contactId = "";
        if (userInfoFromDb == null) { // insert
            UserInfo userInfo = new UserInfo();
            userInfo.setUser_id(userId);
            userInfo.setUser_name(profileName);
            userInfo.setMessage(message.getMessage());
            userInfo.setUser_photo(profileImage);
            userInfo.setTime(timestamp);
            userInfo.setUnread_msg_count("0");
            userInfo.setProfile_id(currentUserId);
            userInfo.setMsg_type(message.getType());
            contactId = dbHandler.addContact(userInfo);
        } else { //update
            contactId = userInfoFromDb.getId();
            userInfoFromDb.setUser_name(profileName);
            userInfoFromDb.setMessage(message.getMessage());
            userInfoFromDb.setUser_photo(profileImage);
            userInfoFromDb.setTime(timestamp);
            userInfoFromDb.setUnread_msg_count(getUnreadMsgCount(userInfoFromDb.getUnread_msg_count(), currentUserId));
            //   Log.e("InboxFragment", "peersValue " + getUnreadMsgCount(userInfoFromDb.getUnread_msg_count(), currentUserId));
            userInfoFromDb.setMsg_type(message.getType());
            dbHandler.updateContact(userInfoFromDb);
        }
        return contactId;
    }


    private String getUnreadMsgCount(String unreadMsgCount, String profileId) {
        if (!TextUtils.isEmpty(unreadMsgCount)) {
            unreadCount = Integer.parseInt(unreadMsgCount);
        }
        unreadCount++;
        if (!TextUtils.isEmpty(InboxDetails.chatProfileId)) {
            if (InboxDetails.chatProfileId.equals(profileId) && AppLifecycle.isChatActivityOpen()) { //current chatting user
                unreadCount = 0;
            }
        }

        return String.valueOf(unreadCount);
    }


    void checkSelectedPaymentMethod() {
        int selectedID = binding.paymentRadioGroup.getCheckedRadioButtonId();

        apiManager.createPayment(selectedPlan.getId());

        // If selected id type equals to UPI Method
      /*  if (selectedID == 2131362361) {
            startUpiGateway();

        } else {
            startGpayGateway();
        }*/
    }


    /* void startUpiGateway() {
         // START PAYMENT INITIALIZATION
         mEasyUpiPayment = new EasyUpiPayment.Builder()
                 .with(SelectPaymentMethod.this)
                 .setPayeeVpa(upiId)
                 .setPayeeName("ZeepLive")
                 .setTransactionId(transactionId)
                 .setTransactionRefId(transactionId)
                 .setDescription(selectedPlan.getName())
                 .setAmount(selectedPlan.getAmount() + ".00")
                 .build();

         // Register Listener for Events
         mEasyUpiPayment.setPaymentStatusListener(SelectPaymentMethod.this);
         mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);

         // Check if app exists or not
         if (mEasyUpiPayment.isDefaultAppExist()) {
             onAppNotFound();

         } else {
             // START PAYMENT
             mEasyUpiPayment.startPayment();
         }
     }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
        // Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.e(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.

        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    //String inPayKey=key.equals("txStatus");
                    if (key.equals("txStatus")) {
                        if (bundle.getString(key).equals("SUCCESS")) {


                            CashFreePaymentRequest cashFreePaymentRequest = new CashFreePaymentRequest(orderIdToken, String.valueOf(selectedPlan.getId()));
                            apiManager.cashFreePayment(cashFreePaymentRequest);
                            new PaymentCompletedDialog(this, "Payment successful.", selectedPlan.getAmount());
                            updatePaymentAppsflyer(selectedPlan.getAmount());
                            finish();
                            // Log.e("customData", key + " : " + bundle.getString(key));


                        }
                        return;
                    }
                    if (bundle.getString(key) != null) {
                        Log.e(TAG, key + " : " + bundle.getString(key));
                    }
                }
        }
        if (requestCode == ActivityRequestCode && data != null) {
            Log.e("PHONEPE_DIRECT", "nativeSdkForMerchantMessage => " + data.getStringExtra("nativeSdkForMerchantMessage") + " response => " +
                    data.getStringExtra("response"));
            if (!data.getStringExtra("response").isEmpty()) {
                apiManager.paytmPaymentCheck("", orderIdString);
            }
        }

        if (requestCode == UPI_INTENT_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.e(TAG, key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                    Log.e("paytmLog", " data response - " + data.getStringExtra("response"));
                }
            }
            try {
                Log.e("paytmLog", " data " + data.getStringExtra("nativeSdkForMerchantMessage"));
                Log.e("paytmLog", " data response - " + data.getStringExtra("response"));
                String raw = data.getStringExtra("response");
                String[] separated = raw.split("&");
                String raw1 = separated[2];
                String[] separated1 = raw1.split("=");
                Log.e("paytmLog", " 11111 - " + separated1[1]);
                if (separated1[1].equalsIgnoreCase("Success")) {
                    //apiManager.verifyPayment(orderId, "", "paytm");
                    new PaymentCompletedDialog(this, transactionId, selectedPlan.getAmount());
                    updatePaymentAppsflyer(selectedPlan.getAmount());
                    apiManager.paytmPaymentCheck("", orderIdString);
                    Log.e("paytmLog", " 22222 - " + separated1[1]);
                }

            } catch (Exception ex) {
                Log.e("paymentResponse", " Exception - " + ex.toString());

            }

        }
    }


  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("UPI", "UPI_RESULT_REQUEST_CODE " + requestCode);
        Log.e("UPI", "UPI_RESULT_RESULT_CODE " + resultCode);
        Log.e("UPI", "UPI_RESULT_DATA " + data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }*/


    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(this)) {
            String str = data.get(0);
            Log.e("UPI", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                CashFreePaymentRequest cashFreePaymentRequest = new CashFreePaymentRequest(approvalRefNo, String.valueOf(selectedPlan.getId()));
                apiManager.cashFreePayment(cashFreePaymentRequest);
                //new PaymentCompletedDialogNew(this, approvalRefNo, selectedPlan.getAmount(), giftCard);
                //Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "upiPaymentDataOperation: Transaction successful approvalRefNo: " + approvalRefNo);
                new PaymentCompletedDialog(this, "Payment successful.", selectedPlan.getAmount());
                updatePaymentAppsflyer(selectedPlan.getAmount());

                String rechargeCompleteMessage = "Recharge of ₹" + selectedPlan.getAmount() + " has been successfully done." + "You got " + selectedPlan.getPoints() + " coins.";
                setNotification(rechargeCompleteMessage);

            } else if ("Payment cancelled.".equals(paymentCancel)) {
                Toast.makeText(this, "Payment cancelled.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "upiPaymentDataOperation: " + "Payment cancelled.");

            } else {
                Toast.makeText(this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "upiPaymentDataOperation: " + "Transaction failed.Please try again");
            }
        } else {
            Toast.makeText(this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
            Log.e("UPI", "upiPaymentDataOperation: " + "Internet connection is not available. Please check and try again");
        }
    }


    public static boolean isConnectionAvailable(Context context) {
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


    public void startRazorPayGateway(CreatePaymentResponse.Result orderData) {

      /*  CreatePaymentResponse.Result data = orderData;

        final Activity activity = this;
        final Checkout checkout = new Checkout();
        checkout.setKeyID(data.getKey());
        //  checkout.setKeyID("rzp_test_E5erobZUHcNr6o");
        //  Log.e("razKey", data.getKey());
        try {
            JSONObject options = new JSONObject();
            // Notes Object
            JSONObject notes = new JSONObject();
            notes.put("address", data.getNotes().getAddress());
            notes.put("plan_id", data.getNotes().getPlan_id());
            notes.put("plan_name", "" + data.getNotes().getPlan_name());
            notes.put("merchant_order_id", "" + data.getNotes().getMerchant_order_id());
            notes.put("plan_amount", "" + data.getNotes().getPlan_amount());
            notes.put("plan_points", "" + data.getNotes().getPlan_points());


            options.put("notes", notes);
            options.put("name", "Zeeplive");
            options.put("description", selectedPlan.getName());
            options.put("order_id", data.getOrder_id());
            options.put("theme.color", data.getTheme().getColor());
            options.put("currency", "INR");
            String amount = String.valueOf(data.getAmount());
            options.put("amount", amount);


            if (raz_payType.equals("card")) {
               *//*
                options.put("prefill.email", sessionManager.getUserId() + "@gmail.com");
                options.put("prefill.contact", "9999885574");
          *//*
                options.put("prefill.method", "card");
            } else if (raz_payType.equals("nb")) {
                options.put("prefill.email", sessionManager.getUserId() + "@gmail.com");
                options.put("prefill.contact", "9999885574");
                options.put("prefill.method", "netbanking");
            } else if (raz_payType.equals("wp")) {
               *//* options.put("prefill.email", sessionManager.getUserId() + "@gmail.com");
                options.put("prefill.contact", "9999885574");*//*
                options.put("prefill.method", "wallet");
            } else if (raz_payType.equals("gp")) {
                options.put("prefill.email", sessionManager.getUserId() + "@gmail.com");
                options.put("prefill.contact", "9999885574");
                options.put("prefill.method", "upi");
            } else if (raz_payType.equals("pp")) {
                options.put("prefill.email", sessionManager.getUserId() + "@gmail.com");
                options.put("prefill.contact", "9999885574");
                options.put("prefill.method", "upi");
            }
            // options.put("prefill.method", "upi");
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }*/
    }

  /*  @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            apiManager.verifyPayment(razorpayPaymentID, orderId);
            new PaymentCompletedDialog(this, transactionId, selectedPlan.getAmount());
            String rechargeCompleteMessage = "Recharge of ₹" + selectedPlan.getAmount() + " has been successfully done." + "You got " + selectedPlan.getPoints() + " coins.";
            setNotification(rechargeCompleteMessage);


        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }*/

    void startGpayGateway() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    queryPurchases();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    /*----------- In app purchase Starts From Here ----------*/
    void setUpBilling() {
        /*------------------- Setup Google In-app Billing ----------------------*/
        PurchasesUpdatedListener purchaseUpdateListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                try {
                    Log.e("playResponce", new Gson().toJson(billingResult.getResponseCode()));
                    Log.e("purchasesData", new Gson().toJson(purchases));
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                        //    apiManager.rechargeWallet(purchases.get(0).getOrderId(), String.valueOf(selectedPlan.getId()), IAPSignature);

                        /*apiManager.confirmInAppPurchase(purchases.get(0).getOrderId(), String.valueOf(selectedPlan.getId()),
                                String.valueOf(selectedPlan.getAmount()), customerName, IAPSignature, mHash);*/
                        iapOrderId = purchases.get(0).getOrderId();
                        for (Purchase purchase : purchases) {
                            handlePurchase(purchase);
                        }

                    } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                        // Handle an error caused by a user cancelling the purchase flow.
                        Toast.makeText(SelectPaymentMethod.this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle any other error codes.
                        Toast.makeText(SelectPaymentMethod.this, "Error occured", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        };

        try {
            billingClient = BillingClient.newBuilder(this)
                    .setListener(purchaseUpdateListener)
                    .enablePendingPurchases()
                    .build();
        } catch (Exception e) {

        }
    }

    String iapOrderId = "";

    void handlePurchase(Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchases or your PurchasesUpdatedListener.

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                    //purchase.getPurchaseState()==purchase.
                    if (purchase.getPurchaseState() == 1) {
                        // apiManager.rechargeWallet(iapOrderId, String.valueOf(selectedPlan.getId()));
                        apiManager.confirmInAppPurchase(purchase.getOrderId(), String.valueOf(selectedPlan.getId()),
                                String.valueOf(selectedPlan.getAmount()), customerName, IAPSignature, mHash);
                    }
                }
            }
        };

        handleNonConsumableProduct(purchase);
        billingClient.consumeAsync(consumeParams, listener);
    }

    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    void handleNonConsumableProduct(Purchase purchase) {
        if (purchase.getPurchaseState() == PURCHASED) {
            if (purchase.getPurchaseState() == PURCHASED) {
                if (!purchase.isAcknowledged()) {
                    if (purchase.getPurchaseState() == 1) {
                        //       apiManager.rechargeWallet(iapOrderId, String.valueOf(selectedPlan.getId()));
                        apiManager.confirmInAppPurchase(purchase.getOrderId(), String.valueOf(selectedPlan.getId()),
                                String.valueOf(selectedPlan.getAmount()), customerName, IAPSignature, mHash);
                    }
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                }
            }

        }
    }

    void queryPurchases() {

        List<String> skuList = new ArrayList<>();
        skuList.add(customGpayPlan);
        Log.e("customGpayPlan", new Gson().toJson(customGpayPlan));
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                        // Process the result.
                        Log.e("SkuDetails", new Gson().toJson(skuDetailsList));

                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && !skuDetailsList.isEmpty()) {

                            // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
                            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetailsList.get(0)).build();

                            int responseCode = billingClient.launchBillingFlow(SelectPaymentMethod.this
                                    , billingFlowParams).getResponseCode();

                        } else {
                            Toast.makeText(SelectPaymentMethod.this, "This recharge not available on G-PAY", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /*----------- In app purchase Ends Here ----------*/



/*    @Override
    public void onTransactionSuccess() {
        // Payment Success
        apiManager.rechargeWallet(transactionId, String.valueOf(selectedPlan.getId()));
    }*/


    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    private String IAPSignature = "", customerName = "";
    private Integer UPI_INTENT_REQUEST = 1;
    Integer ActivityRequestCode = 2;
    String orderIdString = "";

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        try {
            if (ServiceCode == Constant.GET_RAZORPAY_SUCCESS) {
                RazorpayPurchaseResponse rsp = (RazorpayPurchaseResponse) response;

                new PaymentCompletedDialog(this, transactionId, selectedPlan.getAmount());
                updatePaymentAppsflyer(selectedPlan.getAmount());
            }
            if (ServiceCode == Constant.PAYTM_RESPONSE) {
                PaytmResponse rsp = (PaytmResponse) response;

                String host = "https://securegw.paytm.in/";

                orderIdString = rsp.getResult().getOrderId();
                String midString = rsp.getMid();
                String txnTokenString = rsp.getTxnToken();
                String txnAmountString = String.valueOf(selectedPlan.getAmount());

                Intent intent = new Intent();

                switch (paytmType) {
                    case "paytmPhonepe":
                        if (Constant.CHECK_PHONEPE) {
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(rsp.getDeepLink()));
                            intent.setPackage("com.phonepe.app");
                            startActivityForResult(intent, UPI_INTENT_REQUEST);
                        } else {
                            Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "paytmGoogle":
                        if (Constant.CHECK_GPAY) {
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(rsp.getDeepLink()));
                            intent.setPackage("com.google.android.apps.nbu.paisa.user");
                            startActivityForResult(intent, UPI_INTENT_REQUEST);
                        } else {
                            Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "all":
                        String callBackUrl = host + "theia/paytmCallback?ORDER_ID=" + orderIdString;
                        PaytmOrder paytmOrder = new PaytmOrder(orderIdString, midString, txnTokenString, txnAmountString, callBackUrl);
                        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {

                            @Override
                            public void onTransactionResponse(Bundle bundle) {
                                //Toast.makeText(NewSelectPaymentActivity.this, "Response (onTransactionResponse) : " + bundle.toString(), Toast.LENGTH_SHORT).show();
                                Log.e("paytmLog", "sep data => " + bundle.getString("STATUS"));
                                if (bundle.getString("STATUS").equals("TXN_SUCCESS")) {
                                    Log.e("paytmLog", bundle.toString());
                                    apiManager.verifyPayment("", orderIdString);
                                }
                            }

                            @Override
                            public void networkNotAvailable() {

                            }

                            @Override
                            public void onErrorProceed(String s) {

                            }

                            @Override
                            public void clientAuthenticationFailed(String s) {

                            }

                            @Override
                            public void someUIErrorOccurred(String s) {

                            }

                            @Override
                            public void onErrorLoadingWebPage(int i, String s, String s1) {

                            }

                            @Override
                            public void onBackPressedCancelTransaction() {

                            }

                            @Override
                            public void onTransactionCancel(String s, Bundle bundle) {

                            }
                        });

                        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
                        //transactionManager.startTransaction(this, ActivityRequestCode);
                        transactionManager.startTransactionAfterCheckingLoginStatus(this, midString, ActivityRequestCode);
                        transactionManager.setEmiSubventionEnabled(true);
                        break;

                }

                /*Intent paytmIntent = new Intent();
                paytmIntent.setComponent(new ComponentName("net.one97.paytm", "net.one97.paytm.AJRRechargePaymentActivity"));
                paytmIntent.putExtra("paymentmode", 2);
                paytmIntent.putExtra("enable_paytm_invoke", true);
                paytmIntent.putExtra("paytm_invoke", true);
                paytmIntent.putExtra("price", txnAmountString); //this is string amount
                paytmIntent.putExtra("nativeSdkEnabled", true);
                paytmIntent.putExtra("orderid", orderIdString);
                paytmIntent.putExtra("txnToken", txnTokenString);
                paytmIntent.putExtra("mid", midString);
                startActivityForResult(paytmIntent, ActivityRequestCode);*/


            }

          /*  if (ServiceCode == Constant.USER_IAP_TOKEN) {
                IAPTokenResponce rsp = (IAPTokenResponce) response;
                // This Api is used before with simple UPI gateway
                customerName = rsp.getIapData().getIapReturnData().getCustomerName();
                IAPSignature = rsp.getIapData().getSignature();
            }
            if (ServiceCode == Constant.GET_ACTIVE_UPI) {
                GetActiveUPIResponse rsp = (GetActiveUPIResponse) response;
                ActiveUpiResultModel = rsp.getResult();
                UPI_ID = rsp.getResult().getUpi();

                Log.e(TAG, "isSuccess: ActiveUpiResultModel : " + new Gson().toJson(rsp.getResult()));

                if (ActiveUpiResultModel != null) {
                    if (ActiveUpiResultModel.getGpay_upi().equals("1")) {
                        binding.llGpay.setVisibility(View.VISIBLE);
                        binding.vGpay.setVisibility(View.VISIBLE);
                        Log.e(TAG, "isSuccess: googlepay allow to show from backend");

                        if (!isUPIInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
                            binding.llGpay.setVisibility(View.GONE);
                            binding.vGpay.setVisibility(View.GONE);
                            Log.e(TAG, "isSuccess: googlepay not installed");
                        } else {
                            binding.llGpay.setVisibility(View.VISIBLE);
                            binding.vGpay.setVisibility(View.VISIBLE);
                            Log.e(TAG, "isSuccess: googlepay installed");
                        }

                    } else if (ActiveUpiResultModel.getGpay_upi().equals("0")) {
                        binding.llGpay.setVisibility(View.GONE);
                        binding.vGpay.setVisibility(View.GONE);
                        Log.e(TAG, "isSuccess: goolepay doesn't allow to show from backend");
                    }

                    if (ActiveUpiResultModel.getPhonepe_upi().equals("1")) {
                        binding.llPhonepe.setVisibility(View.VISIBLE);
                        binding.vPpay.setVisibility(View.VISIBLE);

                        Log.e(TAG, "isSuccess: PHONEPAY allow to show from backend");

                        if (!isUPIInstalled(this, PHONEPE_PACKAGE_NAME)) {

                            binding.llPhonepe.setVisibility(View.GONE);
                            binding.vPpay.setVisibility(View.GONE);
                            Log.e(TAG, "isSuccess: PHONEPAY not installed");
                        } else {
                            binding.llPhonepe.setVisibility(View.VISIBLE);
                            binding.vPpay.setVisibility(View.VISIBLE);
                            Log.e(TAG, "isSuccess: PHONEPAY installed");
                        }

                    } else if (ActiveUpiResultModel.getPhonepe_upi().equals("0")) {
                        binding.llPhonepe.setVisibility(View.GONE);
                        binding.vPpay.setVisibility(View.GONE);
                        Log.e(TAG, "isSuccess: PHONEPAY doesn't allow to show from backend");
                    }

                    if (ActiveUpiResultModel.getPaytm_upi().equals("1")) {
                        binding.llPaytm.setVisibility(View.VISIBLE);
                        binding.vPaytmpay.setVisibility(View.VISIBLE);

                        Log.e(TAG, "isSuccess: PAYTM allow to show from backend");

                        if (!isUPIInstalled(this, PAYTM_PACKAGE_NAME)) {
                            binding.llPaytm.setVisibility(View.GONE);
                            binding.vPaytmpay.setVisibility(View.GONE);
                            Log.e(TAG, "isSuccess: PAYTM not installed");
                        } else {
                            binding.llPaytm.setVisibility(View.VISIBLE);
                            binding.vPaytmpay.setVisibility(View.VISIBLE);
                            Log.e(TAG, "isSuccess: PAYTM installed");
                        }

                    } else if (ActiveUpiResultModel.getPaytm_upi().equals("0")) {
                        binding.llPaytm.setVisibility(View.GONE);
                        binding.vPaytmpay.setVisibility(View.GONE);
                        Log.e(TAG, "isSuccess: PAYTM doesn't allow to show from backend");
                    }

                    binding.llGpay.setOnClickListener(v -> {
                        Log.e(TAG, "onClick: pay with GOOGLEPAY upi");
                        payUPI(GOOGLE_PAY_PACKAGE_NAME, ActiveUpiResultModel);
                    });

                    binding.llPhonepe.setOnClickListener(v -> {
                        Log.e(TAG, "onClick: " + "pay with PHONEPAY upi");
                        payUPI(PHONEPE_PACKAGE_NAME, ActiveUpiResultModel);
                    });

                    binding.llPaytm.setOnClickListener(v -> {
                        Log.e(TAG, "onClick: " + "pay with PAYTM upi");
                        payUPI(PAYTM_PACKAGE_NAME, ActiveUpiResultModel);

                    });
                } else {
                    Log.e(TAG, "isSuccess: result null");
                }
            }*/
            if (ServiceCode == Constant.RECHARGE_WALLET) {
                //  WalletRechargeResponse rsp = (WalletRechargeResponse) response;
                // This Api is used before with simple UPI gateway
                new PaymentCompletedDialog(this, transactionId, selectedPlan.getAmount());
                updatePaymentAppsflyer(selectedPlan.getAmount());
            }
            if (ServiceCode == Constant.CREATE_PAYMENT) {

                CreatePaymentResponse rsp = (CreatePaymentResponse) response;
                if (rsp.getResult().getOrder_id() != null && rsp.getResult().getOrder_id().length() > 1) {
                    orderId = rsp.getResult().getOrder_id();
                    startRazorPayGateway(rsp.getResult());
                }

            }
            if (ServiceCode == Constant.VERIFY_PAYMENT) {
                ReportResponse rsp = (ReportResponse) response;
                if (rsp.getResult() != null) {
                    //    new PaymentCompletedDialog(this, transactionId, selectedPlan.getAmount());
                }
            }
            if (ServiceCode == Constant.GET_PAYMENT_SELECTOR) {
                PaymentSelectorResponce rsp = (PaymentSelectorResponce) response;

                ArrayList<PaymentSelectorData> paymentSelectorDataArrayList = new ArrayList<>();

                paymentSelectorDataArrayList = rsp.getPaymentSelectorData();
                Log.e("inActivity", paymentSelectorDataArrayList.get(0).getName());

                // paymentSelectorDataArrayList.clear();
                if (paymentSelectorDataArrayList.size() == 1) {
                    if (paymentSelectorDataArrayList.get(0).getName().equals("Razorpay")) {
                        //razorpay
                        payment_Selector = "raz";
                    } else if (paymentSelectorDataArrayList.get(0).getName().equals("Cashfree")) {
                        //cashfree
                        payment_Selector = "cf";
                         /*   ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                        //      apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));
                        binding.upi.setVisibility(View.GONE);
                        binding.buttonPay.setVisibility(View.GONE);
                     */
                        // checkAvailPaymentMethod();
                    }
                } else {
                    //cashfree
                    payment_Selector = "cf";
                    /*if (paymentSelectorDataArrayList.size() == 0) {
                        payment_Selector = "pk";
                    }*/

                    /*
                    binding.upi.setVisibility(View.VISIBLE);
                    binding.buttonPay.setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.VISIBLE);

                    ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                    //               apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));
                    binding.upi.setVisibility(View.VISIBLE);
                    binding.buttonPay.setVisibility(View.VISIBLE);
*/

                    //   checkAvailPaymentMethod();
                }
               /* if (paymentSelectorDataArrayList.size() == 1) {
                    if (paymentSelectorDataArrayList.get(0).getName().equals("Razorpay")) {
                        binding.upi.setVisibility(View.VISIBLE);
                        binding.buttonPay.setVisibility(View.VISIBLE);
                        ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.GONE);
                        ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                        ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.GONE);
                    } else if (paymentSelectorDataArrayList.get(0).getName().equals("Cashfree")) {
                        ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                        //      apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));
                        binding.upi.setVisibility(View.GONE);
                        binding.buttonPay.setVisibility(View.GONE);
                        checkAvailPaymentMethod();
                    }
                } else {
                    binding.upi.setVisibility(View.VISIBLE);
                    binding.buttonPay.setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.VISIBLE);

                    ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                    //               apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));
                    binding.upi.setVisibility(View.VISIBLE);
                    binding.buttonPay.setVisibility(View.VISIBLE);
                    checkAvailPaymentMethod();
                }*/

            /* if (rsp.getResult().getName().equals("Razorpay")) {

                binding.upi.setVisibility(View.VISIBLE);
                binding.buttonPay.setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.GONE);
            } else if (rsp.getResult().getName().equals("Cashfree")) {
                ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.VISIBLE);
                apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()));
                binding.upi.setVisibility(View.GONE);
                binding.buttonPay.setVisibility(View.GONE);
                checkAvailPaymentMethod();
            }*/
                setPaymentUI(rsp.getPaymentSelectorData().get(0));
                Log.e("payment_Selector", "after calling api " + payment_Selector);

            }
            if (ServiceCode == Constant.GET_CFTOKEN) {
                CfTokenResponce rsp = (CfTokenResponce) response;
                token = rsp.getCftoken();
                orderIdToken = rsp.getOrderId();
                appIDCashFree = rsp.getAppId();
                orderAmountCashfree = rsp.getDeductableAmout();
             /*   cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
                cfPaymentService.setOrientation(0);

                if (carryParmentProcessOf.equals("gpay")) {
                    cfPaymentService.gPayPayment(this, getInputParams(), token, stage);
                } else if (carryParmentProcessOf.equals("phonepay")) {
                    cfPaymentService.phonePePayment(this, getInputParams(), token, stage);
                }*/
            }
        } catch (Exception e) {
        }
    }

    private void setPaymentUI(PaymentSelectorData paymentSelectorData) {
        if (paymentSelectorData.getPaytmUpi().equals("1")) {
            binding.groupPaytm.setVisibility(View.VISIBLE);

        }
    }

    private void checkAvailPaymentMethod() {
        if (appInstalledOrNot("com.phonepe.app")) {
            ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.VISIBLE);
//            ((View) findViewById(R.id.v_gpay)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_ppay)).setVisibility(View.VISIBLE);

        } else {
            ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_ppay)).setVisibility(View.GONE);
        }

        if (appInstalledOrNot("com.google.android.apps.nbu.paisa.user")) {
            ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.VISIBLE);
            ((View) findViewById(R.id.v_gpay)).setVisibility(View.VISIBLE);

        } else {
            ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_gpay)).setVisibility(View.GONE);
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    String token = "TOKEN_DATA";
    String stage = "PROD";
    String orderIdToken = "";
    String appIDCashFree = "";
    String orderAmountCashfree = "";

    private Map<String, String> getInputParams() {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */

        String appId = appIDCashFree;
        String orderId = orderIdToken;


        orderAmountCashfree = String.valueOf(selectedPlan.getAmount());

        String orderNote = "Test Order";
        String customerName = "John Doe";
        String customerPhone = "9900012345";
        String customerEmail = sessionManager.getUserId() + "@gmail.com";

        Map<String, String> params = new HashMap<>();

       /*   params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmountCashfree);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        params.put(PARAM_NOTIFY_URL, "https://zeep.live/api/verifyPayment/");*/

        return params;
    }

//    CFPaymentService cfPaymentService;


    enum SeamlessMode {
        CARD, WALLET, NET_BANKING, UPI_COLLECT, PAY_PAL
    }

    SeamlessMode currentMode = SeamlessMode.CARD;

   /* private Map<String, String> getSeamlessCheckoutParams() {
        Map<String, String> params = getInputParams();
        switch (currentMode) {
            case CARD:
                params.put(PARAM_PAYMENT_OPTION, "card");
                params.put(PARAM_CARD_NUMBER, "4434260000000008");//Replace Card number
                params.put(PARAM_CARD_MM, "05"); // Card Expiry Month in MM
                params.put(PARAM_CARD_YYYY, "2025"); // Card Expiry Year in YYYY
                params.put(PARAM_CARD_HOLDER, "John Doe"); // Card Holder name
                params.put(PARAM_CARD_CVV, "123"); // Card CVV
                break;
            case WALLET:
                params.put(PARAM_PAYMENT_OPTION, "wallet");
                params.put(PARAM_WALLET_CODE, "4007"); // Put one of the wallet codes mentioned here https://dev.cashfree.com/payment-gateway/payments/wallets
                break;
            case NET_BANKING:
                params.put(PARAM_PAYMENT_OPTION, "nb");
                params.put(PARAM_BANK_CODE, "3333"); // Put one of the bank codes mentioned here https://dev.cashfree.com/payment-gateway/payments/netbanking
//                params.put(PARAM_NOTIFY_URL, "https://zeep.live/api/verifyPayment/");
                break;
            case UPI_COLLECT:
                params.put(PARAM_PAYMENT_OPTION, "upi");
                params.put(PARAM_UPI_VPA, "VALID_VPA");
                //               params.put(PARAM_NOTIFY_URL, "https://zeep.live/api/verifyPayment/");
                break;
            case PAY_PAL:
                params.put(PARAM_PAYMENT_OPTION, "paypal");
                break;
        }
        return params;
    }*/

    private String carryParmentProcessOf = "";

    public void pay_UPI(View view) {
        //  cfPaymentService.upiPayment(this, getInputParams(), token, stage);
    }

    public void Gpay(View view) {
        if (payment_Selector.equals("raz")) {
            raz_payType = "gp";
            apiManager.createPayment(selectedPlan.getId());
        } else if (payment_Selector.equals("pk")) {
            //newPayKun(1);
        } else {

            if (appInstalledOrNot("com.google.android.apps.nbu.paisa.user")) {
                carryParmentProcessOf = "gpay";
                apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));

            } else {
                Toast.makeText(this, "Please install G-PAY", Toast.LENGTH_SHORT).show();
            }
            //  cfPaymentService.gPayPayment(this, getInputParams(), token, stage);
        }
    }


    public void phonepe_pay(View view) {
        if (payment_Selector.equals("raz")) {
            raz_payType = "pp";
            apiManager.createPayment(selectedPlan.getId());
        } else if (payment_Selector.equals("pk")) {
            //newPayKun(2);
        } else {
            if (appInstalledOrNot("com.phonepe.app")) {
                carryParmentProcessOf = "phonepay";
                apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));

            } else {
                Toast.makeText(this, "Please install PhonePE", Toast.LENGTH_SHORT).show();
            }
            //   cfPaymentService.phonePePayment(this, getInputParams(), token, stage);
        }

    }

    private String raz_payType = "", payment_Selector = "raz";

    public void card_pay(View view) {
        //    cfPaymentService.doPayment(this, getSeamlessCheckoutParams(), token, stage);

      /*  startActivity(new Intent(SelectPaymentMethod.this, CashfreeCardPaymentActivity.class)
                .putExtra("token", token)
                .putExtra("selectId", String.valueOf(selectedPlan.getAmount()))
                .putExtra("orderIdToken", orderIdToken));
        finish();*/

        /*raz_payType = "card";
        apiManager.createPayment(selectedPlan.getId());*/


        //commented when connecting upi

        if (payment_Selector.equals("raz") || payment_Selector.equals("cf")) {
            raz_payType = "card";
            apiManager.createPayment(selectedPlan.getId());
        } else if (payment_Selector.equals("pk")) {
            //newPayKun(5);
        }


    }

    public void nb_pay(View view) {
       /* currentMode = SeamlessMode.NET_BANKING;
        cfPaymentService.doPayment(this, getSeamlessCheckoutParams(), token, stage);*/
       /* raz_payType = "nb";
        apiManager.createPayment(selectedPlan.getId());*/


        //commented when connecting upi
        if (payment_Selector.equals("raz") || payment_Selector.equals("cf")) {
            raz_payType = "nb";
            apiManager.createPayment(selectedPlan.getId());
        } else if (payment_Selector.equals("pk")) {
            // newPayKun(4);
        }


    }

    public void wallet_pay(View view) {
        //prev commented
       /* currentMode = SeamlessMode.NET_BANKING;
        cfPaymentService.doPayment(this, getSeamlessCheckoutParams(), token, stage);*/
        /*raz_payType = "wp";
        apiManager.createPayment(selectedPlan.getId());*/


        //commented when connecting upi code
        Log.e("getPaymentSelectorData", payment_Selector);
        if (payment_Selector.equals("raz") || payment_Selector.equals("cf")) {
            raz_payType = "wp";
            apiManager.createPayment(selectedPlan.getId());
        } else if (payment_Selector.equals("pk")) {
            //newPayKun(3);
        }


    }


    private void payUPI(String packageName, ActiveUpiResult activeUpiResultModel) {


        //   Log.e(TAG, "wallet_pay: ActiveUpiResultModel    " + new Gson().toJson(ActiveUpiResultModel));

        String amount = String.valueOf(selectedPlan.getAmount());

        // String amount = "1";
        //  String upiId = activeUpiResultModel.getUpi();
        String name = activeUpiResultModel.getPn();
        String note = "MeetLive !";
        String transectionId = activeUpiResultModel.getTr();
        String transectionRefId = activeUpiResultModel.getTr();
        String mc = activeUpiResultModel.getMc();
        String url = activeUpiResultModel.getUrl();


        Log.e(TAG, "payUPI: amount " + amount);
        Log.e(TAG, "payUPI: name " + name);
        Log.e(TAG, "payUPI: note " + note);
        Log.e(TAG, "payUPI: transectionId " + transectionId);
        Log.e(TAG, "payUPI: transectionRefId " + transectionRefId);
        Log.e(TAG, "payUPI: mc " + mc);
        Log.e(TAG, "payUPI: url " + url);


        String activeUpi = null;

        if (isUPIInstalled(this, packageName)) {

            if (packageName.equals(PHONEPE_PACKAGE_NAME)) {

                activeUpi = activeUpiResultModel.getPhonepe_id();

                Log.e(TAG, "payUPI: activeUpi  PhonePayUpi " + activeUpi);


            } else if (packageName.equals(GOOGLE_PAY_PACKAGE_NAME)) {

                activeUpi = activeUpiResultModel.getGpay_id();

                Log.e(TAG, "payUPI: activeUpi GooglePayUpi " + activeUpi);

            } else {
                activeUpi = activeUpiResultModel.getUpi_ids();
                Log.e(TAG, "payUPI: activeUpi PaytmUpiId " + activeUpi);
            }
            // Log.e(TAG, "payUPI: activeUpi " + activeUpi + "\n\n\n\n" + " ");

            payWithUpi(amount, activeUpi, name, note, transectionRefId, transectionId, mc, url, packageName);

        } else {

          /*
            if (packageName.equals(PHONEPE_PACKAGE_NAME)) {
                Toast.makeText(this, "Please Install PhonePe", Toast.LENGTH_SHORT).show();
            } else if (packageName.equals(GOOGLE_PAY_PACKAGE_NAME)) {
                Toast.makeText(this, "Please Install GPay", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please Install PayTm", Toast.LENGTH_SHORT).show();
            }
            */

        }


    }


    private void payWithUpi(String amount, String upiId, String name, String note, String transactionRefId, String transactionId, String mc, String url, String packageName) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", mc)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("tr", transactionRefId)
                .appendQueryParameter("ti", transactionId)
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("url", url)
                .build();

        Log.e("UPI", "UPI_URI " + uri);
        Intent UPIintent = new Intent(Intent.ACTION_VIEW);
        UPIintent.setData(uri);
        UPIintent.setPackage(packageName);

        try {
            startActivityForResult(UPIintent, UPI_REQUEST_CODE);
        } catch (Exception e) {
            Log.e("TAG", "" + e.getMessage());
            Toast.makeText(this, "Please try another UPI", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isUPIInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private String mHash = "";

    private void hash() {
        PackageInfo info;
        try {

            info = getPackageManager().getPackageInfo(
                    this.getPackageName(), PackageManager.GET_SIGNATURES);

            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                //       Log.e("Zeep_sha_key", md.toString());
                String something = new String(Base64.encode(md.digest(), 0));
                mHash = something;
                // Log.e("Zeep_Hash_key", something);
                System.out.println("Hash key" + something);
            }

        } catch (PackageManager.NameNotFoundException e1) {
            //     Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            //     Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            //     Log.e("exception", e.toString());
        }
    }


    //paykun
   /* @Override
    public void onPaymentSuccess(Transaction paymentMessage) {
        CashFreePaymentRequest cashFreePaymentRequest = new CashFreePaymentRequest(paymentMessage.getPaymentId(), String.valueOf(selectedPlan.getId()));
        apiManager.cashFreePayment(cashFreePaymentRequest);
        new PaymentCompletedDialog(this, paymentMessage.getPaymentId(), selectedPlan.getAmount());
    }

    @Override
    public void onPaymentError(PaymentMessage paymentMessage) {
        if (paymentMessage.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_CANCELLED)) {
            Toast.makeText(this, "Your Transaction is cancelled", Toast.LENGTH_SHORT).show();
        } else if (paymentMessage.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_FAILED)) {
            Toast.makeText(this, "Your Transaction is failed", Toast.LENGTH_SHORT).show();
        } else if (paymentMessage.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_NETWORK_NOT_AVAILABLE)) {
            Toast.makeText(this, "Internet Issue", Toast.LENGTH_SHORT).show();
        } else if (paymentMessage.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SERVER_ISSUE)) {
            Toast.makeText(this, "Server issue", Toast.LENGTH_SHORT).show();
        } else if (paymentMessage.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_ACCESS_TOKEN_MISSING)) {
            Toast.makeText(this, "Access Token missing", Toast.LENGTH_SHORT).show();
        } else if (paymentMessage.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_MERCHANT_ID_MISSING)) {
            Toast.makeText(this, "Merchant Id is missing", Toast.LENGTH_SHORT).show();
        } else if (paymentMessage.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_INVALID_REQUEST)) {
            Toast.makeText(this, "Invalid Request", Toast.LENGTH_SHORT).show();
        }
    }

    private void newPayKun(int payType) {
        String merchantIdLive = "734111967908329";
        String accessTokenLive = "585426D66F9A3AD6D7F224E34E0C90F8";
        String productName = "Selected Plan";
        String orderId = String.valueOf(System.currentTimeMillis());
        String amount = String.valueOf(selectedPlan.getAmount());
        String customerName = new SessionManager(getApplicationContext()).getUserName();//getUserId
        String customerPhone = "1090010000";
        String customerEmail = customerName + "@gmail.com";

        // Create Map for payment methods
        HashMap<PaymentTypes, PaymentMethods> payment_methods = new HashMap<>();

        if (payType == 1 || payType == 2) {
            // Create payment method object to be added in payment method Map
            PaymentMethods paymentMethod = new PaymentMethods();
            paymentMethod.enable = true; // True if you want to enable this method or else false, default is true
            paymentMethod.priority = 1; // Set priority for payment method order at checkout page
            paymentMethod.set_as_prefered = true; // If you want this payment method to show in prefered payment method then set it to true

            // Add payment method into our Map
            payment_methods.put(PaymentTypes.UPI, paymentMethod);

            // Example for netbanking
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 2;
            paymentMethod.set_as_prefered = true;
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.KKBK, 1));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.HDFC, 2));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.ICIC, 3));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.SBIN, 4));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.UTIB, 5));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.ANDB, 6));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.UBIN, 7));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.BARB, 8));
            payment_methods.put(PaymentTypes.NB, paymentMethod);

            // Example for wallet
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = false;
            payment_methods.put(PaymentTypes.WA, paymentMethod);

            // Example for card payment
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.DCCC, paymentMethod);

            // Example for UPI Qr
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.UPIQRCODE, paymentMethod);

            // Example for EMI
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.EMI, paymentMethod);

        } else if (payType == 3) {
            PaymentMethods paymentMethod = new PaymentMethods();
            paymentMethod.enable = false; // True if you want to enable this method or else false, default is true
            paymentMethod.priority = 1; // Set priority for payment method order at checkout page
            paymentMethod.set_as_prefered = true; // If you want this payment method to show in prefered payment method then set it to true

            // Add payment method into our Map
            payment_methods.put(PaymentTypes.UPI, paymentMethod);

            // Example for netbanking
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 2;
            paymentMethod.set_as_prefered = true;
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.KKBK, 1));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.HDFC, 2));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.ICIC, 3));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.SBIN, 4));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.UTIB, 5));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.ANDB, 6));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.UBIN, 7));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.BARB, 8));
            payment_methods.put(PaymentTypes.NB, paymentMethod);

            // Example for wallet
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = true;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = false;
            payment_methods.put(PaymentTypes.WA, paymentMethod);

            // Example for card payment
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.DCCC, paymentMethod);

            // Example for UPI Qr
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.UPIQRCODE, paymentMethod);

            // Example for EMI
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.EMI, paymentMethod);
        } else if (payType == 4) {
            PaymentMethods paymentMethod = new PaymentMethods();
            paymentMethod.enable = false; // True if you want to enable this method or else false, default is true
            paymentMethod.priority = 1; // Set priority for payment method order at checkout page
            paymentMethod.set_as_prefered = true; // If you want this payment method to show in prefered payment method then set it to true

            // Add payment method into our Map
            payment_methods.put(PaymentTypes.UPI, paymentMethod);

            // Example for netbanking
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = true;
            paymentMethod.priority = 2;
            paymentMethod.set_as_prefered = true;
        *//*    paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.KKBK, 1));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.HDFC, 2));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.ICIC, 3));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.SBIN, 4));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.UTIB, 5));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.ANDB, 6));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.UBIN, 7));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.BARB, 8));
        *//*
            payment_methods.put(PaymentTypes.NB, paymentMethod);

            // Example for wallet
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = false;
            payment_methods.put(PaymentTypes.WA, paymentMethod);

            // Example for card payment
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.DCCC, paymentMethod);

            // Example for UPI Qr
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.UPIQRCODE, paymentMethod);

            // Example for EMI
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.EMI, paymentMethod);
        } else if (payType == 5) {
            PaymentMethods *//*paymentMethod = new PaymentMethods();
            paymentMethod.enable = false; // True if you want to enable this method or else false, default is true
            paymentMethod.priority = 1; // Set priority for payment method order at checkout page
            paymentMethod.set_as_prefered = true; // If you want this payment method to show in prefered payment method then set it to true

            // Add payment method into our Map
            payment_methods.put(PaymentTypes.UPI, paymentMethod);

            // Example for netbanking
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 2;
            paymentMethod.set_as_prefered = true;
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.KKBK, 1));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.HDFC, 2));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.ICIC, 3));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.SBIN, 4));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.UTIB, 5));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.ANDB, 6));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.UBIN, 7));
            paymentMethod.sub_methods.add(new Sub_Methods(SubPaymentTypes.BARB, 8));
            payment_methods.put(PaymentTypes.NB, paymentMethod);

            // Example for wallet
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = false;
            payment_methods.put(PaymentTypes.WA, paymentMethod);

            // Example for card payment
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = true;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.DCCC, paymentMethod);

            // Example for UPI Qr
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.UPIQRCODE, paymentMethod);

            // Example for EMI
            paymentMethod = new PaymentMethods();
            paymentMethod.enable = false;
            paymentMethod.priority = 3;
            paymentMethod.set_as_prefered = true;
            payment_methods.put(PaymentTypes.EMI, paymentMethod);
        }


        // Now, Create object for paykun transaction
        PaykunTransaction paykunTransaction = new PaykunTransaction(merchantIdLive, accessTokenLive, true);

        //try {
        // Set all request data
        // Set all request data
        paykunTransaction.setCurrency("INR");
        paykunTransaction.setCustomer_name(customerName);
        paykunTransaction.setCustomer_email(customerEmail);
        paykunTransaction.setCustomer_phone(customerPhone);
        paykunTransaction.setProduct_name(productName);
        paykunTransaction.setOrder_no(orderId);
        paykunTransaction.setAmount(amount);
        paykunTransaction.setLive(true); // Currently only live transactions is supported so keep this as true

        // Optionally you can customize color and merchant logo for checkout page
        paykunTransaction.setTheme_color("#7b1fa2");
        paykunTransaction.setTheme_logo("https://zeep.live/public/images/zeepliveofficial.png");

        // Set the payment methods Map object that we have prepared above, this is optional
        paykunTransaction.setPayment_methods(payment_methods);

        try {
            new PaykunApiCall.Builder(SelectPaymentMethod.this).sendJsonObject(paykunTransaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
       *//* } catch (Exception e) {
            e.printStackTrace();
        }*//*

    }*/
    String currency = null;
    public void updatePaymentAppsflyer(double amount){
        if(currency == null){
            if (new SessionManager(SelectPaymentMethod.this).getUserLocation().equals("India")) {
                currency = "INR";
            } else {
                currency = "USD";
            }
        }
        appsFlyerManager.trackCoinPurchase(amount,currency);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.isReceivedFakeCall = true;
    }
}


