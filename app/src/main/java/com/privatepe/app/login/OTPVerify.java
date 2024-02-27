package com.privatepe.app.login;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.privatepe.app.R;
import com.privatepe.app.activity.BasicInformation;
import com.privatepe.app.activity.CountryCodeActivity;
import com.privatepe.app.activity.SocialLogin;
import com.privatepe.app.activity.addalbum.AddAlbumActivity;
import com.privatepe.app.activity.addalbum.AuditionVideoActivity;
import com.privatepe.app.activity.addalbum.ShotVideoActivity;
import com.privatepe.app.main.Home;
import com.privatepe.app.model.LoginResponse;
import com.privatepe.app.response.Otptwillow.OtpTwillowResponce;
import com.privatepe.app.response.Otptwillow.OtpTwillowVerifyResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.sqlite.Chat;
import com.privatepe.app.sqlite.SystemDB;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.NetworkCheck;
import com.privatepe.app.utils.SessionManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

//import im.zego.zim.enums.ZIMErrorCode;

public class OTPVerify extends BaseActivity implements ApiResponseInterface, View.OnClickListener {
    private NetworkCheck networkCheck;
    public static LoginResponse rsp;
    private FirebaseAuth mAuth;
    private EditText edtOTP, edtNumber;
    private TextView resend, getotp, timer;
    private static String number = "";
    private TextView verifyOTPBtn;
    private static String phone = "";
    private String verificationId;
    private String blockCharacterSet = "~#^|$%&!\\/!@#$%^&*(){}_[]|\\?/<>,.:-'';§£¥.+\\ ";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int AUDIO_PERMISSION_CODE = 101;
    private ApiManager apiManager;
    private SessionManager session;
    private String fcmToken;
    private static String android_id = "";
    private static int otpNumber = 0;
    private String SmsApi = "https://2factor.in/API/V1/2084a5d9-c0a0-11eb-8089-0200cd936042/SMS/";


    HashMap<String, String> user;

    Spinner countryCodeSpinner;
    private LinearLayout countryCodeSpinnerLay;

    String CoutryCode = "+91";
    String phoneNum;
    TextView countryCodeText;
    private String yourNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // hideStatusBar(getWindow(),true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        networkCheck = new NetworkCheck();
        mAuth = FirebaseAuth.getInstance();
        apiManager = new ApiManager(this, this);
        session = new SessionManager(this);
        //checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        //checkPermission(Manifest.permission.RECORD_AUDIO, CAMERA_PERMISSION_CODE);


        init();

        SharedPreferences.Editor editor = getSharedPreferences("OTP_DATA", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        if (getIntent() != null) {
            if (getIntent().getStringExtra("countryCode") != null) {
                CoutryCode = getIntent().getStringExtra("countryCode").toString();
                // Toast.makeText(getApplicationContext(),""+CoutryCode,Toast.LENGTH_SHORT).show();

            }
            if (getIntent().getStringExtra("phonenum") != null) {
                phoneNum = getIntent().getStringExtra("phonenum").toString();
                edtNumber.setText(phoneNum);
                //  Toast.makeText(getApplicationContext(),""+CoutryCode,Toast.LENGTH_SHORT).show();
            }

        }

        countryCodeText.setText(CoutryCode);


        if (TextUtils.isEmpty(edtNumber.getText().toString())) {
            // Toast.makeText(OTPVerify.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
        } else {
            if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                //  phone = "+91" + edtNumber.getText().toString();

                phone = CoutryCode + edtNumber.getText().toString();
                //sendVerificationCode(phone);
                sendOtp2Factor(phone);
            } else {
                Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_SHORT).show();
            }
        }
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtNumber.getText().toString()) || edtNumber.getText().toString().length() < 10) {
                    //    Toast.makeText(OTPVerify.this, "Please enter valid number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                    Toast.makeText(OTPVerify.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                        //verifyCode(edtOTP.getText().toString());
                        //getPermission(permissions,2);
                         verifyOtp2Factor();
                        //    apiManager.login(edtNumber.getText().toString(), android_id, mHash);
                    } else {
                        Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_SHORT).show();
                    }
                    //apiManager.login(number, android_id);
                }
            }
        });

        edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0 || s.toString().trim().length() < 4) {
                    verifyOTPBtn.setEnabled(false);
                    verifyOTPBtn.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                } else {
                    verifyOTPBtn.setBackground(getApplicationContext().getDrawable(R.drawable.active));
                    verifyOTPBtn.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        hash();
    }

    @SuppressLint("HardwareIds")
    private void init() {
        resend = findViewById(R.id.resend);
        getotp = findViewById(R.id.get_otp);
        timer = findViewById(R.id.timer);
        resend.setOnClickListener(this);
        getotp.setOnClickListener(this);
        timer.setOnClickListener(this);
        number = getIntent().getStringExtra("number");
        edtOTP = findViewById(R.id.otp);
        edtNumber = findViewById(R.id.input_number);
        verifyOTPBtn = findViewById(R.id.verify);

        countryCodeText = findViewById(R.id.countrycode);

        countryCodeSpinner = findViewById(R.id.ccp);

        countryCodeSpinnerLay = findViewById(R.id.countrySpinnerLay);
        // countryCodeSpinnerLay.setOnClickListener(this);

        countryCodeSpinnerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OTPVerify.this, CountryCodeActivity.class);
                intent.putExtra("phonenum", edtNumber.getText().toString());
                intent.putExtra("countryCode", countryCodeText.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        }



        // countryCodeSpinner.setEnabled(false);
        // countryCodeSpinner.setOnClickListener(this);

        edtOTP.setLongClickable(false);
        edtOTP.setTextIsSelectable(false);

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        InputFilter filter1 = new InputFilter.LengthFilter(11);
        edtNumber.setFilters(new InputFilter[]{filter, filter1});
        InputFilter filter2 = new InputFilter.LengthFilter(6);
        edtOTP.setFilters(new InputFilter[]{filter, filter2});
    }

    private void sendOtp2Factor(String phone) {
        yourNum = phone;
        Random rand = new Random();
        otpNumber = rand.nextInt(999999);
        //  Log.e("OTP_NUM", "" + otpNumber);
        //  edtOTP.setText(""+otpNumber);
        SmsApi = SmsApi + "/" + phone + "/" + String.format("%06d", otpNumber);
        //apiManager.sendOTP(SmsApi);
        //  new RequestTask().execute(SmsApi);

        apiManager.otp2Factor(CoutryCode, edtNumber.getText().toString(), android_id, mHash);

    }

    private void verifyOtp2Factor() {
        // startActivity(new Intent(OTPVerify.this,ConfirmAgency.class));
        if (!edtOTP.getText().toString().isEmpty()) {
            if (yourNum.equals(CoutryCode + edtNumber.getText().toString())) {
                //   Log.e("OTPVerify", "verifyOtp2Factor: UserName "+edtNumber.getText().toString()+" device id  "+android_id+" mhash "+mHash);
                apiManager.loginUserMobileLatest(CoutryCode,edtNumber.getText().toString(),session_uuid, edtOTP.getText().toString(), android_id, mHash);
               // apiManager.otp2FactorVerify(session_uuid, edtOTP.getText().toString(), mHash);
            } else {
                Toast.makeText(getApplicationContext(), "Mobile number changed during request otp.", Toast.LENGTH_LONG).show();
            }
            // Log.e("mob_no",""+yourNum+"    "+CoutryCode+edtNumber.getText().toString());
            // startActivity(new Intent(OTPVerify.this,ConfirmAgency.class));
        } else {
            Toast.makeText(getApplicationContext(), "Invalid or Empty OTP", Toast.LENGTH_LONG).show();
        }
    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            String responseString = null;
            try {
                URL url = new URL(uri[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    // Do normal input or output stream reading
                } else {
                    //response = "FAILED";
                    //See documentation for more info on response handling
                }
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    apiManager.login(edtNumber.getText().toString(), android_id, mHash);
                } else {
                    Toast.makeText(OTPVerify.this, "Invalid OTP or it might be expire.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number)            // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                edtOTP.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            //Toast.makeText(OTPVerify.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(OTPVerify.this, "Limit Exceeded...\nTry again later.", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void isError(String errorCode) {
        // Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
        Log.e("OTPVerify", "isError: " + errorCode);

    }

    String session_uuid = "";
    String[] permissions;



    private void getPermission(String[] permissions,int resControl) {
        Log.e("otpverifyPerm", "getPermission: permissions " + permissions.length);


        Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        try {        Log.e("otpverifyPerm", "getPermission11: permissions " );


                            if (report.areAllPermissionsGranted()) {
                                Log.e("otpverifyPerm", "getPermission22: permissions " + resControl);

                                if(resControl==0){
                                    intentt = new Intent(OTPVerify.this, AddAlbumActivity.class);
                                    intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    OTPVerify.this.startActivity(intentt);
                                }else if(resControl==2){
                                    intentt = new Intent(OTPVerify.this, ShotVideoActivity.class);
                                    intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    OTPVerify.this.startActivity(intentt);
                                }else if(resControl==3){
                                    intentt = new Intent(OTPVerify.this, AuditionVideoActivity.class);
                                    intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    OTPVerify.this.startActivity(intentt);
                                }


                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Log.e("otpverifyPerm", "getPermission33: permissions " + report.getDeniedPermissionResponses().get(0).getPermissionName()+" "+report.getDeniedPermissionResponses().size());

                            }

                            if (report.getGrantedPermissionResponses().get(0).getPermissionName().equals("android.permission.ACCESS_FINE_LOCATION")) {
                                Log.e("otpverifyPerm", "getPermission44: permissions " + resControl);

                                //enableLocationSettings();
                                // enableLocationSettings();

                            }
                        } catch (Exception e) {
                            Log.e("otpverifyPerm", "getPermission55: permissions " + e);

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
    Intent intentt;


    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_OTP_2FACTOR_VERIFY) {
            OtpTwillowVerifyResponse rsp = (OtpTwillowVerifyResponse) response;
            if (rsp.getSuccess()) {
                apiManager.login(edtNumber.getText().toString(), android_id, mHash);
            } else {
                Toast.makeText(getApplicationContext(), "Mobile number changed during request otp.", Toast.LENGTH_LONG).show();
            }
        }
        if (ServiceCode == Constant.GET_OTP_2FACTOR) {
            OtpTwillowResponce rsp = (OtpTwillowResponce) response;
            if (rsp.getSuccess()) {
                session_uuid = rsp.getSession_uuid();
                // resend.setVisibility(View.VISIBLE);
            } else {
                // resend.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), rsp.getError().toString(), Toast.LENGTH_LONG).show();
            }
        }
        if (ServiceCode == Constant.LOGIN) {
            rsp = (LoginResponse) response;

            if (rsp.getGuest_status() == 0 && rsp.getResult().getRole() == 2) {
                SharedPreferences.Editor editor = getSharedPreferences("OTP_DATA", MODE_PRIVATE).edit();
                editor.clear();
                editor.putString("mobile", edtNumber.getText().toString());
                editor.putString("android_id", android_id);
                editor.putString("token", "Bearer " + rsp.getResult().getToken());
                editor.putString("profile_id", rsp.getResult().getProfile_id());
                editor.apply();
                // loginZim(rsp.getResult().getName(), rsp.getResult().getProfile_id(),  "");
                Intent intent = new Intent(this, BasicInformation.class);
                finishAffinity();
                startActivity(intent);

            } else if (rsp.getGuest_status() == 1 && rsp.getResult().getRole() == 2) {
                new SessionManager(this).saveGuestStatus(rsp.getGuest_status());
                session.createLoginSession(rsp);
                session.setUserEmail(rsp.getResult().getProfile_id());
                session.setUserPassword(android_id);
                new SessionManager(OTPVerify.this).setResUpload(String.valueOf(rsp.getResult().getVerification_status()));

                String textHindi = "डेटिंग ऐप पर पैसे बनाने के रहस्य \n" +
                        "1. याद रखिये सबसे पहले चेहरा दिखाए,सबसे पहले अधिकतम समय उन्हें दिखाईये वो क्या चाहते है (जैसे शरीर के अंग) ,वो जलद से बोर हो जायँगे।\n" +
                        "2. एक बार जब आप कॉल उठाएंगे,तो याद रखिए हाई बोलना। शुरुआत के 15s तक ,उनसे बात करने की।कोशिश किजिये।\n" +
                        "A. आप उनको बुला सकते है ,हाई डार्लिंग/हनी/बेबी\n" +
                        "B. यह आपके लिए बहुत जरूरी है कि बर्फ को तोड़ना (परिचय), आप पूछ सकते है \n" +
                        "C. बेबी ,आर यू सिंगल ?\n" +
                        "D. आपको उनकी प्रशंसा करनी है, कुछ अच्छा बोलना है उनके बारे  में जिससे उनको अच्छा लगे।\n" +
                        " i. बेबी आप बहुत सुंदर दिखाई देते है\n" +
                        " ii. आप बहुत बहादुर हैंं।\n" +
                        " iii. ऐस लगता है आप लड़कियों को लेकर अच्छी रूचि रखते है। \n" +
                        " iv. मुझे आपकी आँखें बहुत अच्छी लगी,वह बहुत सेक्सी हैंं।\n" +
                        " v. मुझे आपकी मांसपेशी बहुत पसंद आई,आप बहुत आकर्षक हो।\n" +
                        " vi. किस तरह की लड़की आप पसंद करते है ।\n" +
                        "3. जितने ज्यादा आप गिफ्ट्स पायंगे, उतने लम्बी आपकी कॉल चलेगी , आपकि सब्से कीमती यूज़र्स में सिफारिश की जाएगी ।\n" +
                        "\n" +
                        "तो आप किसके लिए इंतजार कर रहे हैंं, काम करना चालू करे, कमाईये $200+ एक हफ्ते में !";

                String textEnglish = "The secret of making money in dating apps \n" +
                        "1. Remeber to show your face at first, most of the time if you show them what they want (like body parts ) at first, they will get bored easily. \n" +
                        "2. Once calls come, remeber to say hi. Try to talk to him at the first 15s.\n" +
                        "  1）You can call them, hey daring/honey/baby \n" +
                        "  2）It is very important for you to break the ice first, you can ask: Baby, you single?\n" +
                        "  3）You can compliment them, say something good about them that will make them very happy.\n" +
                        "    1. Baby you look so handsome.\n" +
                        "    2. You're very manly. \n" +
                        "    3. You seem to have a good taste for girls.\n" +
                        "    4. i like your eyes, they are so sexy.\n" +
                        "    5. i like your mustle, you are so charming.\n" +
                        "    6. What kind of girls do you prefer?\n" +
                        "3. The more gift you receive, the longer your call duration lasts, you will be recommended to more rich users, andd win more coins and bonus.\n" +
                        "What are you waiting for, start work and earn $200+ a week!";
                String json = "Welcome to Private Pe. Enjoy your trip and find your true love here!\n\nDo not reveal your personal information, or open any unknown links to avoid information theft and financial loss.";
                SystemDB db = new SystemDB(getApplicationContext());
                getApplicationContext().deleteDatabase("chatSystemDb");
                getApplicationContext().deleteDatabase("chatRtmDb");


                String timesttamp = System.currentTimeMillis() + "";


                db.addChat(new Chat("System", "System", "", json, "", "", "", "", 0, timesttamp, "TEXT"));
                db.setTotalSystemUnreadCount(db.getTotalSystemUnreadCount() + 1);
                db.addChat(new Chat("System", "System", "", textHindi, "", "", "", "", 0, timesttamp, "TEXT"));
                db.setTotalSystemUnreadCount(db.getTotalSystemUnreadCount() + 1);
                db.addChat(new Chat("System", "System", "", textEnglish, "", "", "", "", 0, timesttamp, "TEXT"));
                db.setTotalSystemUnreadCount(db.getTotalSystemUnreadCount() + 1);

                Intent intent1 = new Intent("MSG-UPDATE");
                intent1.putExtra("peerId", "System");
                intent1.putExtra("msg", "receive");
                sendBroadcast(intent1);



    int resControl = rsp.getResult().getVerification_status();

    switch (resControl) {
        case 0:
        case 2:
        case 3:
            getPermission(permissions,resControl);
            break;
        default:
            Intent intent = new Intent(this, Home.class);
            finishAffinity();
            startActivity(intent);

    }


            } else if (rsp.getResult().getRole() == 4) {
                new SessionManager(this).saveGuestStatus(rsp.getGuest_status());
                session.createLoginSession(rsp);
                session.setUserEmail(rsp.getResult().getProfile_id());
                session.setUserPassword(android_id);

                /*String json = "Welcome to Private Pe. Enjoy your trip and find your true love here!\n\nDo not reveal your personal information, or open any unknown links to avoid information theft and financial loss.";
                SystemDB db = new SystemDB(getApplicationContext());
                db.addChat(new Chat("System", "System", "", json, "", "", "", ""));*/
                Intent intent = new Intent(this, Home.class);
                finishAffinity();
                startActivity(intent);
            } else if (rsp.getGuest_status() == 0 && rsp.getResult().getRole() == 5) {
                new SessionManager(this).saveGuestStatus(rsp.getGuest_status());
                session.createLoginSession(rsp);
                session.setUserEmail(rsp.getResult().getProfile_id());
                session.setUserPassword(android_id);
                /*String json = "Welcome to Private Pe. Enjoy your trip and find your true love here!\n\nDo not reveal your personal information, or open any unknown links to avoid information theft and financial loss.";
                SystemDB db = new SystemDB(getApplicationContext());
                db.addChat(new Chat("System", "System", "", json, "", "", "", ""));*/

                Intent intent = new Intent(this, BasicInformation.class);
                finishAffinity();
                startActivity(intent);
            } else if (rsp.getGuest_status() == 1 && rsp.getResult().getRole() == 5) {
                new SessionManager(this).saveGuestStatus(rsp.getGuest_status());
                session.createLoginSession(rsp);
                session.setUserEmail(rsp.getResult().getProfile_id());
                session.setUserPassword(android_id);
                //  loginZim(rsp.getResult().getName(), rsp.getResult().getProfile_id(),  "");
                /*String json = "Welcome to Private Pe. Enjoy your trip and find your true love here!\n\nDo not reveal your personal information, or open any unknown links to avoid information theft and financial loss.";
                SystemDB db = new SystemDB(getApplicationContext());
                db.addChat(new Chat("System", "System", "", json, "", "", "", ""));*/
                Intent intent = new Intent(this, Home.class);
                finishAffinity();
                startActivity(intent);
            }
        }
    }


    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            //apiManager.login(edtNumber.getText().toString(), android_id);
            Toast.makeText(getApplicationContext(), "Input valid OTP.", Toast.LENGTH_LONG).show();
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(OTPVerify.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(OTPVerify.this, new String[]{permission}, requestCode);
        } else {
            //Toast.makeText(OTPVerify.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(OTPVerify.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OTPVerify.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(OTPVerify.this, "Audio Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OTPVerify.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        if (v == resend) {
            if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                getotp.setVisibility(View.GONE);
                resend.setVisibility(View.GONE);
                timer();
                timer.setVisibility(View.VISIBLE);
                //mAuth.getInstance().signOut();
                //mAuth = FirebaseAuth.getInstance();
                //sendVerificationCode(phone);
                otpNumber = 0;
                SmsApi = "https://2factor.in/API/V1/2084a5d9-c0a0-11eb-8089-0200cd936042/SMS/";
                // phone = "+91" + edtNumber.getText().toString();
                phone = CoutryCode + edtNumber.getText().toString();
                sendOtp2Factor(phone);
            } else {
                Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == getotp) {
            if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                if (TextUtils.isEmpty(edtNumber.getText().toString())) {
                    Toast.makeText(OTPVerify.this, "Please enter number", Toast.LENGTH_SHORT).show();
                    return;
                }
                getotp.setVisibility(View.GONE);
                resend.setVisibility(View.GONE);
                edtOTP.requestFocus();

                timer();
                timer.setVisibility(View.VISIBLE);
                //    phone = "+91" + edtNumber.getText().toString();

                phone = CoutryCode + edtNumber.getText().toString();
                Log.i("otpPHONE", "" + phone);
                //sendVerificationCode(phone);
                sendOtp2Factor(phone);
            } else {
                Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == countryCodeSpinnerLay) {

            // startActivity(new Intent(OTPVerify.this, CountryCodeActivity.class));
            // finish();

        }


    }

    private void timer() {

        //by amit singh

        DisableNumEtAndCountryChooser();

        new CountDownTimer(120 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {


                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);

                // timer.setText(minutes+":"+seconds);

                if (minutes == 0) {
                    timer.setText(twoDigitString(seconds));
                } else {
                    timer.setText(twoDigitString(minutes) + ":" + twoDigitString(seconds));
                }


            }

            private String twoDigitString(int number) {

                if (number == 0) {
                    return "00";
                } else if (number / 10 == 0) {
                    return "0" + number;
                }
                return String.valueOf(number);
            }

            public void onFinish() {
                otpNumber = 0;
                getotp.setVisibility(View.GONE);
                timer.setVisibility(View.GONE);
                resend.setVisibility(View.VISIBLE);

                EnableNumEtAndCountryChooser();

            }

        }.start();
    }

    private void DisableNumEtAndCountryChooser() {
        //disable upper num layout
        countryCodeSpinnerLay.setEnabled(false);
        edtNumber.setEnabled(false);
    }

    private void EnableNumEtAndCountryChooser() {
        //enable upper num layout
        countryCodeSpinnerLay.setEnabled(true);
        edtNumber.setEnabled(true);
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
                //Log.e("Klive_sha_key", md.toString());
                String something = new String(Base64.encode(md.digest(), 0));
                mHash = something;
                //Log.e("Klive_Hash_key", something);
                // System.out.println("Hash key" + something);
                // Log.e("hhhhhhhhhhh", "hash: "+something );
            }

        } catch (PackageManager.NameNotFoundException e1) {
            //     Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            //     Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            //     Log.e("exception", e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SocialLogin.class));
        finish();
    }
}