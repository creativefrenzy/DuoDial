package com.privatepe.app.activity;

import androidx.annotation.RequiresApi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.app.R;
import com.privatepe.app.login.OTPVerify;
import com.privatepe.app.model.LoginResponse;
import com.privatepe.app.response.AddReferralCardResponse;
import com.privatepe.app.response.metend.MaintainancePopUp.MaintainenceResponce;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.ErrorDialog;
import com.privatepe.app.utils.SessionManager;

import org.json.JSONException;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class SocialLogin extends BaseActivity implements View.OnClickListener, ApiResponseInterface {

    LinearLayout guest_login;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    ErrorDialog errorDialog;
    ImageView iv_ogo;

    /*------------------- Facebook variables -----------------------*/
    LoginButton loginButton;
    LinearLayout fb, gmail_btn;
    CallbackManager callbackManager;
    public static final int REQ_CODE = 9001;
    public static final int FACEBOOK_REQ_CODE = 64206;
    /*------------------- Facebook variables ends here -------------*/

    /* ----- Google+ Login Variables ------- */
    GoogleSignInClient googleApiClient;
    ApiManager apiManager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);

        getReferrerDetails();
        apiManager = new ApiManager(this, this);
        fb = findViewById(R.id.fb_btn);
        gmail_btn = findViewById(R.id.gmail_btn);
        loginButton = findViewById(R.id.facebook_login_button);
        guest_login = findViewById(R.id.guest_login);
        iv_ogo = findViewById(R.id.iv_ogo);
        guest_login.setOnClickListener(this);
        iv_ogo.setOnClickListener(this);

        errorDialog = new ErrorDialog(this, "Please Check Your Internet Connection");

        /*------------ Callback Method Of Facebook ---------------*/
        callbackManager = CallbackManager.Factory.create();
        // loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFbInfo();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SocialLogin.this, "cancel...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SocialLogin.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });


        /*------------ Callback Method Of Facebook ends here ---------------*/

        /*-------------- Google+ ----------------*/
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleApiClient = GoogleSignIn.getClient(this, googleSignInOptions);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.e("LocationService", "onCreate: ");
                    autoCountrySelect();
                } else {
                    getPermission();
                }
            }
        } catch (Exception e) {
            Log.e("LocationService", "onCreate: Exception " + e.getMessage());
        }
        hash();

        //   apiManager.getMaintainenceData();
    }

    private void getPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        try {
                            if (report.areAllPermissionsGranted()) {
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                            }

                            if (report.getGrantedPermissionResponses().get(0).getPermissionName().equals("android.permission.ACCESS_FINE_LOCATION")) {
                                // autoCountrySelect();
                                enableLocationSettings();
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }
    InstallReferrerClient referrerClient;
    String refererID;
    String referrer = "";
    public void getReferrerDetails() {
        sessionManager = new SessionManager(this);

        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {

                            response = referrerClient.getInstallReferrer();
                            if(response != null) {
                                String referrerUrl = response.getInstallReferrer();
                                long referrerClickTime = response.getReferrerClickTimestampSeconds();
                                long appInstallTime = response.getInstallBeginTimestampSeconds();
                                boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
                                referrer = response.getInstallReferrer();

                                Log.e("ReferrerisThis", "\n" + referrerUrl + "\n" + "Referrer Click Time is : " + referrerClickTime + "\nApp Install Time : " + appInstallTime);
                                int pos1 = referrerUrl.indexOf("utm_source=");
                                int pos2 = referrerUrl.indexOf("&utm_medium");
                                try {
                                    refererID = referrerUrl.substring(pos1 + 11, pos2);
                                    Log.e("ReferrerID", " " + refererID);
                                    sessionManager.setReferrerId(refererID);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.

                        //Toast.makeText(SocialLogin.this, "Feature not supported..", Toast.LENGTH_SHORT).show();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.

                        //Toast.makeText(SocialLogin.this, "Fail to establish connection", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.

                //Toast.makeText(SocialLogin.this, "Service disconnected..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    SessionManager sessionManager;
    int REQUEST_CODE_CHECK_SETTINGS = 2021;

    private void autoCountrySelect() {
        Log.e("LocationDetection", "m here");
        sessionManager = new SessionManager(this);


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType")
            Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        //       c_name = addresses.get(0).getCountryName();
                        String city_name = addresses.get(0).getLocality();

                        Log.e("locationnnn", "autoCountrySelect: " + city_name);

                 /*       Log.e("countryname", addresses.get(0).getCountryName());
                          Log.e("cityname", city_name);
                 */
                        sessionManager.setUserLocation(addresses.get(0).getCountryName());
                        sessionManager.setUserAddress(city_name);
              /*          Log.e("cityname1", addresses.get(0).getAddressLine(0).toLowerCase());
                        Log.e("cityname2", addresses.get(0).getAdminArea());
                        Log.e("cityname3", addresses.get(0).getSubLocality());*/

                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //   autoCountrySelect();
                // enableLocationSettings();
            }
        }

    }

    public void enableLocationSettings() {
        Log.e("LocationService", "enableLocationSettings: ");
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> {
                    // startUpdatingLocation(...);
                    autoCountrySelect();

                })
                .addOnFailureListener(this, ex -> {
                    Log.e("LocationService", "Failure");
                    if (ex instanceof ResolvableApiException) {
                        // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(SocialLogin.this, REQUEST_CODE_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            googleApiClient.signOut()
                    .addOnCompleteListener(this, task -> {
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_CHECK_SETTINGS == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                //user clicked OK, you can startUpdatingLocation(...);
                autoCountrySelect();
                // enableLocationSettings();
            } else {
                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
            }
        }

        if (requestCode == REQ_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        // Pass the activity result to the FacebookCallback.
        if (requestCode == FACEBOOK_REQ_CODE) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                new SessionManager(this).setUserFacebookName(account.getDisplayName());

                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                apiManager.login_FbGoogle(account.getDisplayName(), "google", account.getId(), mHash, deviceId);
                Log.e("GoogleSign", "Name=" + account.getDisplayName());
                Log.e("GoogleSign", "UserId" + account.getId());
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /*---------- Get Facebook Data ------------*/
    private void getFbInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                (object, response) -> {
                    try {
                        Log.e("LOG_TAG", "fb json object: " + object);
                        Log.e("LOG_TAG", "fb graph response: " + response);

                        String id = object.getString("id");
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        // 8/06/21 set user name in sheared prefrences
                        new SessionManager(this).setUserFacebookName(first_name + " " + last_name);

                        //Log.e("Userfacebooknamesocialp", first_name);
                        //   String last_name = object.getString("last_name");
                        //   String gender = object.getString("gender");
                        //   String birthday = object.getString("birthday");
                        //   String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";
                       /*  String email = "";
                        if (object.has("email")) {
                            email = object.getString("email");
                        }*/
                        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        apiManager.login_FbGoogle(first_name, "facebook", id, mHash, deviceId);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();
    }
    /*------- Get Facebook Data ends here ------------*/

    void checkInternetConnection() {
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_number_login:
                sessionManager.setCheckLoginTypeUser("phone");
                startActivity(new Intent(this, OTPVerify.class));
                finish();
                break;
            case R.id.guest_login:
                sessionManager.setCheckLoginTypeUser("guest");

                if (((CheckBox) findViewById(R.id.chkTT)).isChecked()) {
                    @SuppressLint("HardwareIds")
                    String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    // deviceId = deviceId + "aaswww12";

                   /* Random r = new Random();
                    int randomNumber = r.nextInt(50000);

                    Log.e("dividdd", "onClick: "+deviceId );

                    deviceId="THTFGD"+randomNumber;*/
                    //   deviceId="ssss"+"fhjdkdhttthhhhdh";
                    //   deviceId="ssss"+"hjfgkjfgdjhjhkkjhkfgdjhk";
                    //String deviceId = "1122334455000";
                    /*
                    Log.e("Device_id", deviceId);
                    Log.e("mHash", mHash);
                    Log.e("SocialLogin", "onClick: deviceid "+deviceId );
                    Log.e("SocialLogin", "onClick: mHash "+mHash );
                    */

                    // deviceId="sssstrtffty454533";

                    //  deviceId="rrffrrttteerrr";

                    new SessionManager(this).setFirstTimeLogin(true);
                    apiManager.guestRegister("guest", deviceId, mHash);

                    new SessionManager(this).setUserLoginCompleted(false);
                    // apiManager.guestRegister("guest", "rfrrrrd", mHash);
                } else {
                    Toast.makeText(this, "Please accept the agreement first", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_ogo:
               /* Intent intent = new Intent(this, Login.class);
                startActivity(intent);*/
                break;

            case R.id.fb_btn:
                if (((CheckBox) findViewById(R.id.chkTT)).isChecked()) {
                    checkInternetConnection();
                    if (activeNetwork != null) {
                        loginButton.performClick();
                    } else {
                        errorDialog.show();
                    }
                } else {
                    Toast.makeText(this, "Please accept the agreement first", Toast.LENGTH_SHORT).show();
                }
                return;

            case R.id.gmail_btn:
                sessionManager.setCheckLoginTypeUser("google");
                if (((CheckBox) findViewById(R.id.chkTT)).isChecked()) {
                    checkInternetConnection();
                    if (activeNetwork != null) {
                        new SessionManager(this).setFirstTimeLogin(true);
                        Intent signInIntent = googleApiClient.getSignInIntent();
                        startActivityForResult(signInIntent, REQ_CODE);
                        new SessionManager(this).setUserLoginCompleted(true);
                    } else {
                        errorDialog.show();
                    }
                } else {
                    Toast.makeText(this, "Please accept the agreement first", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        String c_name = new SessionManager(getApplicationContext()).getUserLocation();
        if (ServiceCode == Constant.REGISTER) {
            LoginResponse rsp = (LoginResponse) response;
            // new SessionManager(this).saveGuestStatus(Integer.parseInt(rsp.getAlready_registered()));
            new SessionManager(this).saveGuestStatus(1);
            if (c_name.equals("null")) {
                Log.e("userLocationLog", c_name);
                if (rsp.getResult().getAllow_in_app_purchase() == 0) {
                    new SessionManager(this).createLoginSession(rsp);
                    // new SessionManager(this).setUserLocation("India");
                    //   Log.e("counteryINACT", new SessionManager(this).getUserLocation());

                    Intent intent = new Intent(this, MainActivity.class);
                    finishAffinity();
                    startActivity(intent);
                } else {
                    new SessionManager(this).createLoginSession(rsp);
                    startActivity(new Intent(SocialLogin.this, LocationSelection.class));
                }
              /*  new SessionManager(this).createLoginSession(rsp);
                startActivity(new Intent(SocialLogin.this, LocationSelection.class));*/
            } else {
                new SessionManager(this).createLoginSession(rsp);
                Intent intent = new Intent(this, MainActivity.class);
                finishAffinity();
                startActivity(intent);
            }


         /*   if (rsp.getAlready_registered().equals("1"))
            {
                sessionManager.setIsNewUser(false);
            }
            else if (rsp.getAlready_registered().equals("0"))
            {
                sessionManager.setIsNewUser(true);
            }*/

            Log.e("SocialLog", "isSuccess: " + rsp.getAlready_registered());

        }
        if (ServiceCode == Constant.GUEST_REGISTER) {
            LoginResponse rsp = (LoginResponse) response;

            if (rsp.getResult() != null && rsp.getResult().getUsername() != null && rsp.getResult().getDemo_password() != null) {
                new SessionManager(this).saveGuestStatus(Integer.parseInt(rsp.getAlready_registered()));
                new SessionManager(this).saveGuestPassword(rsp.getResult().getDemo_password());
                apiManager.loginGuest(rsp.getResult().getUsername(), rsp.getResult().getDemo_password(), mHash);
            }

        }
        if (ServiceCode == Constant.LOGIN) {
            LoginResponse rsp = (LoginResponse) response;
            if (c_name.equals("null")) {
                if (rsp.getResult().getAllow_in_app_purchase() == 0) {
                    new SessionManager(this).createLoginSession(rsp);
                    //apiManager.addReferralCards(rsp.getResult().getToken(),new SessionManager(getApplicationContext()).getReferrerId(),mHash);
                    // new SessionManager(this).setUserLocation("India");
                    // Log.e("counteryINACT", new SessionManager(this).getUserLocation());


                    Intent intent = new Intent(this, MainActivity.class);
                    finishAffinity();
                    startActivity(intent);
                } else {
                    new SessionManager(this).createLoginSession(rsp);
                    //apiManager.addReferralCards(rsp.getResult().getToken(),new SessionManager(getApplicationContext()).getReferrerId(),mHash);
                    //startActivity(new Intent(SocialLogin.this, LocationSelection.class));
                    finishAffinity();
                    startActivity(new Intent(SocialLogin.this, MainActivity.class));

                }
            } else {
                new SessionManager(this).createLoginSession(rsp);
                //apiManager.addReferralCards(rsp.getResult().getToken(),new SessionManager(getApplicationContext()).getReferrerId(),mHash);
                Intent intent = new Intent(this, MainActivity.class);
                finishAffinity();
                startActivity(intent);
            }
        }
        if (ServiceCode == Constant.GET_MAINTAINENCE_DATA) {
            MaintainenceResponce rsp = (MaintainenceResponce) response;

            if (rsp.getResult().getDown() == 1) {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_maintainence);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
            }
        }
        if(ServiceCode==Constant.ADD_REFERRAL_CARD){
            assert response instanceof AddReferralCardResponse;
            AddReferralCardResponse rsp = (AddReferralCardResponse) response;
            if(rsp.getSuccess()){
                Intent intent = new Intent(this, MainActivity.class);
                finishAffinity();
                startActivity(intent);
            }
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
                // Log.e("Zeep_sha_key", md.toString());
                String something = new String(Base64.encode(md.digest(), 0));
                mHash = something;
                // Log.e("Zeep_Hash_key", something);
                // System.out.println("Hash key" + something);
            }

        } catch (PackageManager.NameNotFoundException e1) {
            //     Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            //     Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            //     Log.e("exception", e.toString());
        }
    }

    public void openAgreement(View view) {
        String urlString = "https://sites.google.com/view/zeeplive/terms";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed and open Kindle Browser
            intent.setPackage("com.amazon.cloud9");
            startActivity(intent);
        }
    }

    public void openPrivacyPolicy(View view) {
        String urlString = "https://sites.google.com/view/zeeplive/privacy";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed and open Kindle Browser
            intent.setPackage("com.amazon.cloud9");
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
