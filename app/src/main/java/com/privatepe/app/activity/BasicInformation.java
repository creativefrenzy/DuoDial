package com.privatepe.app.activity;

import static com.privatepe.app.login.OTPVerify.rsp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.privatepe.app.R;
import com.privatepe.app.databinding.ActivityBasicInformationBinding;
import com.privatepe.app.dialogs.cityDialog;
import com.privatepe.app.dialogs.languageDialog;
import com.privatepe.app.model.LoginResponse;
import com.privatepe.app.model.SubmitResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.sqlite.Chat;
import com.privatepe.app.sqlite.SystemDB;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.NetworkCheck;
import com.privatepe.app.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BasicInformation extends BaseActivity implements ApiResponseInterface {
    private NetworkCheck networkCheck;
    DatePickerDialog picker;
    TextView agetxt, citytxt, languagetxt, next;
    String langId = "";
    EditText nametxt;
    EditText agency_id;
    ApiManager apiManager;

    private String blockCharacterSet = "~#^|$%&!\\/!@#$%^&*(){}_[]|\\?/<>,.:-'';§£¥.+\\ ";
    private boolean isNameFill = false;
    private boolean isAgeSelected = false;
    private boolean isCitySelected = false;
    private boolean isLanguageSelected = false;
    String picturePath = "";
    CircleImageView imageView;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;

    SessionManager session;
    TextView confirmButton;
    private static String mobile, android_id, token, name, age, city, language;
    String agency;
    MultipartBody.Part picToProfile;
    private Boolean imageViewSet = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("BasicInformationAct", "onCreate: ");
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ActivityBasicInformationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_basic_information);
        binding.setClickListener(new EventHandler(this));
        networkCheck = new NetworkCheck();
        init();

    }

    private void init() {
        nametxt = findViewById(R.id.name);
        agetxt = findViewById(R.id.ageView);
        citytxt = findViewById(R.id.cityView);
        languagetxt = findViewById(R.id.languageView);
        next = findViewById(R.id.next);

        agency_id = findViewById(R.id.agency);
        apiManager = new ApiManager(BasicInformation.this, this);
        imageView = findViewById(R.id.userImage);

        InputFilter filter1 = new InputFilter.LengthFilter(15);
        agency_id.setFilters(new InputFilter[]{filter, filter1});
        confirmButton = findViewById(R.id.confirm_button);
        session = new SessionManager(BasicInformation.this);

        agency_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0 || s.toString().trim().length() < 2) {
                    //  Toast.makeText('Please fi');
                } else {
                    //   apiManager.getAgencyInfo(token, agency_id.getText().toString());

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


        nametxt.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        //name.setLongClickable(false);
        //name.setTextIsSelectable(false);

        nametxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    isNameFill = false;
                    next.setBackground(AppCompatResources.getDrawable(BasicInformation.this, R.drawable.inactive));
                    next.setEnabled(false);
                } else {
                    isNameFill = true;
                    next.setEnabled(true);
                    enableButton();
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
        nametxt.setFilters(new InputFilter[]{acceptonlyAlphabetValuesnotNumbersMethod(), filter1});
    }

    public static InputFilter acceptonlyAlphabetValuesnotNumbersMethod() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean isCheck = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) {
                        sb.append(c);
                    } else {
                        isCheck = false;
                    }
                }
                if (isCheck)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString spannableString = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, spannableString, 0);
                        return spannableString;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
                Matcher match = pattern.matcher(String.valueOf(c));
                return match.matches();
            }
        };
    }

    private void enableButton() {
        if (isNameFill && isAgeSelected && isCitySelected && isLanguageSelected) {
            AppCompatResources.getDrawable(BasicInformation.this, R.drawable.active);
        }
    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void image() {
            pickImage();
        }

        public void age() {
            final Calendar cldr = Calendar.getInstance();
            cldr.add(Calendar.YEAR, -18);
            long upperLimit = cldr.getTimeInMillis();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            picker = new DatePickerDialog(BasicInformation.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            agetxt.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            isAgeSelected = true;
                            enableButton();
                        }
                    }, year, month, day);
            picker.getDatePicker().setMaxDate(upperLimit);
            picker.show();
        }

        public void city() {
            SharedPreferences prefs = getSharedPreferences("OTP_DATA", MODE_PRIVATE);
            String token = prefs.getString("token", "");
            cityDialog cityDialog = new cityDialog(BasicInformation.this, token);
            cityDialog.show();
            cityDialog.setDialogResult(new cityDialog.OnMyDialogResult() {
                @Override
                public void finish(String result) {
                    citytxt.setText(result);
                    isCitySelected = true;
                    enableButton();
                }
            });
       /* new cityDialog(BasicInformation.this);
        back*/
        }

        public void language() {
            SharedPreferences prefs = getSharedPreferences("OTP_DATA", MODE_PRIVATE);
            String token = prefs.getString("token", "");
            languageDialog languageDialog = new languageDialog(BasicInformation.this, token);
            languageDialog.show();
            languageDialog.setDialogResult(new languageDialog.OnMyDialogResult() {
                @Override
                public void finish(String result, String id) {
                    languagetxt.setText(result);
                    langId = id;
                    isLanguageSelected = true;
                    enableButton();
                }
            });
        }

        public void confirm() {
            if (networkCheck.isNetworkAvailable(getApplicationContext())) {

                if (!picturePath.equals("")) {
                    try {
                        if (!picturePath.equals("Not found")) {
                            if (imageViewSet) {
                                File picture = new File(picturePath);
                                // Compress Image
                                File file = null;
                                file = new Compressor(BasicInformation.this).compressToFile(picture);
                                Log.e("selectedImageEdit", "selectedImageEdit:" + file);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                picToProfile = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);


                                // SharedPreferences prefsAgency = getSharedPreferences("AGENCY_DATA", MODE_PRIVATE);
                                //api call
                            } else {
                                Toast.makeText(getApplicationContext(), "Select Another Image", Toast.LENGTH_SHORT).show();

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Select Image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (agency_id.getText().toString().equals("")) {
                    SharedPreferences prefs = getSharedPreferences("OTP_DATA", MODE_PRIVATE);
                    String token = prefs.getString("token", "");
                    agency_id.setError("Empty or Invalid Agency ID");
                    agency_id.requestFocus();
                    return;
                    //    apiManager.getAgencyInfo(token, agency_id.getText().toString());
                }

                if (nametxt.getText().toString().equals("")) {
                    nametxt.setError("Enter Name");
                    nametxt.requestFocus();
                    //  Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (agetxt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Age", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e("nameLog", "nameFromControl = " + nametxt.getText().toString());

                if (citytxt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Country", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (languagetxt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Language", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences pref = getSharedPreferences("OTP_DATA", MODE_PRIVATE);
                mobile = pref.getString("mobile", "");
                android_id = pref.getString("android_id", "");
                token = pref.getString("token", "");


                new SessionManager(getApplicationContext()).setUserName(nametxt.getText().toString());
                agency = agency_id.getText().toString().trim();
                SharedPreferences.Editor editor = getSharedPreferences("BASIC_DATA", MODE_PRIVATE).edit();
                editor.clear();
                editor.putString("name", nametxt.getText().toString());
                editor.putString("age", agetxt.getText().toString());
                editor.putString("city", citytxt.getText().toString());
                editor.putString("languageName", languagetxt.getText().toString());
                editor.putString("language", langId);

                Log.e("language", langId);

                editor.apply();
                agency = agency_id.getText().toString().trim();
                SharedPreferences prefsBasic = getSharedPreferences("BASIC_DATA", MODE_PRIVATE);
                name = nametxt.getText().toString();
                age = agetxt.getText().toString();
                city = citytxt.getText().toString();
                language = langId;
                Log.e("nameLog", "nameFromControl = " + nametxt.getText().toString());

                Log.e("nameLog", "nameFromSession = " + new SessionManager(getApplicationContext()).getName());
                Log.e("nameLog", "nameFromSession 2 = " + new SessionManager(getApplicationContext()).getUserName());
                apiManager.updateProfileDetails(token, agency, mobile, android_id, name, age, city, language, picToProfile);


            } else {
                Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_SHORT).show();
            }
        }

        public void back() {
            onBackPressed();
        }

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

    @Override
    public void isError(String errorCode) {
        Toast.makeText(BasicInformation.this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void pickImage() {
        String permission;
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        }
        Dexter.withActivity(this)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(BasicInformation.this, ImagePickerActivity.class);
                        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
                        // setting aspect ratio
                        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 2); // 16x9, 1x1, 3:4, 3:2
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 3);
                        startActivityForResult(intent, PICK_IMAGE_GALLERY_REQUEST_CODE);

                      /*  Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);*/
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                // Uri selectedImageUri = data.getData();
                Uri selectedImageUri = data.getParcelableExtra("path");
                Log.e("selectimg", "selectedImageUri==" + selectedImageUri);
                picturePath = selectedImageUri.getPath();

                //loadProfile(selectedImageUri.toString());
                Log.e("picture", picturePath);

                if (!picturePath.equals("Not found")) {
                    imageView.setImageURI(selectedImageUri);
                    imageView.setVisibility(View.VISIBLE);
                    File picture = new File(picturePath);
                    // Compress Image
                    next.setBackground(getApplicationContext().getDrawable(R.drawable.active));
                    Log.e("onActivityResult Image", "Set_Image =" + imageViewSet);


                    File file = new Compressor(this).compressToFile(picture);
                    Log.e("selectedImageEdit", "selectedImageEdit:" + file);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    //RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part profile_pic = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);
                    Log.e("selectedImageEdit", "selectedImageEdit:" + file);

                    imageBitmap = BitmapFactory.decodeFile(picturePath);
                    inputImage = InputImage.fromBitmap(imageBitmap, 90);

                    detectTextFromImage();
                    SharedPreferences prefs = getSharedPreferences("OTP_DATA", MODE_PRIVATE);
                    mobile = prefs.getString("mobile", "");
                    android_id = prefs.getString("android_id", "");
                    token = prefs.getString("token", "");


                } else {
                    Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("picturewee", e.toString());
                Toast.makeText(this, "Please select another image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.UPDATE_PROFILE) {
            Log.e("UPDATE_PROFILE 1", "is here");

            try {
                SubmitResponse responseType = (SubmitResponse) response;
                if (!responseType.getResult().equals("Invalid Agency Id")) {

                    if (responseType.getResult().equals("Registration Successfully")) {
                        String json = "Welcome to Private Pe. Enjoy your trip and find your true love here!\n\nDo not reveal your personal information, or open any unknown links to avoid information theft and financial loss.";
                        SystemDB db = new SystemDB(getApplicationContext());

                        String timesttamp = System.currentTimeMillis() + "";
                        db.addChat(new Chat("System", "System", "", json, "", "", "", "", 0, timesttamp, "TEXT"));
                        if (rsp.getGuest_status() == 0 && rsp.getResult().getRole() == 2 || rsp.getResult().getRole() == 5) {
                            new SessionManager(this).saveGuestStatus(rsp.getGuest_status());
                            //new SessionManager(this).saveGuestStatus(Integer.parseInt(rsp.getAlready_registered()));
//                            LoginResponse loginResponse = new LoginResponse();

                            String tempName = new SessionManager(getApplicationContext()).getName();

                            new LoginResponse.Result(rsp.getResult().getToken(), name, rsp.getResult().getGender(), rsp.getResult().getProfile_id(), city, "", rsp.getResult().getIs_online(), rsp.getGuest_status(), rsp.getResult().getAllow_in_app_purchase(), rsp.getResult().getRole(), rsp.getResult().getLogin_type(), rsp.getResult().getUsername(), "", android_id);

                            session.createLoginSession(rsp);
                            session.setUserEmail(rsp.getResult().getProfile_id());
                            session.setUserPassword(android_id);

                            new SessionManager(getApplicationContext()).setUserName(tempName);

                            startActivity(new Intent(getApplicationContext(), SubmitForm.class));
                        }
                    }
                } else {
                    agency_id.requestFocus();
                    agency_id.setError("Invalid Agency Id");
                }
            } catch (Exception ex) {
                Log.e("Registration Exception", "not complete" + ex.getMessage());
            }

        }

    }

    Bitmap imageBitmap;
    InputImage inputImage;

    private void detectTextFromImage() {

        TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        Task<Text> task = textRecognizer.process(inputImage);
        task.addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                if (text.getText().isEmpty()) {
                    detectFace(imageBitmap);
                } else {
                    picturePath = "";
                    imageView.setVisibility(View.GONE);
                    addnotifyDialog("1", "text");
                }
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                picturePath = "";
                imageView.setVisibility(View.GONE);

                addnotifyDialog("1", "text");
            }
        });
    }

    FaceDetectorOptions highAccuracyOpts =
            new FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                    .build();

    private void detectFace(Bitmap bitmap) {
        int rotationDegree = 0;

        InputImage image = InputImage.fromBitmap(bitmap, rotationDegree);

        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);

        detector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {

                                try {
                                    if (faces.get(0).toString().equals("")) {

                                        Toast.makeText(BasicInformation.this, "No Face found", Toast.LENGTH_SHORT).show();
                                        addnotifyDialog("2", "No face");
                                        next.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                                        imageViewSet = false;
                                        picturePath = "";
                                        Log.e("detectFace1 ", "Set_Image==" + imageViewSet);

                                    } else {
                                        //  apiManager.updateProfileDetails("", "", "", "", picToUpload, is_album);
                                        //continue_profile.setEnabled(true);
                                        next.setEnabled(true);
                                        imageViewSet = true;
                                        Log.e("detectFace2 ", "Set_Image==" + imageViewSet);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(BasicInformation.this, "No Face found", Toast.LENGTH_SHORT).show();
                                    addnotifyDialog("2", "No face");
                                    next.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                                    //next.setEnabled(false);
                                    picturePath = "";
                                    imageViewSet = false;
                                }
                            }
                        }).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                               /* if (!is_album) {
                                    addPosterdialog.dismiss();
                                }*/
                                addnotifyDialog("2", "No face");
                                //  continue_profile.setEnabled(false);
                                //  continue_profile.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                                next.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                                //    next.setEnabled(false);
                                imageViewSet = false;
                                picturePath = "";
                                Toast.makeText(BasicInformation.this, "No Face found", Toast.LENGTH_SHORT).show();
                            }
                        });
    }


    public void addnotifyDialog(String notifyType, String reason) {

        Dialog notifyDialog = new Dialog(BasicInformation.this);
        notifyDialog.setContentView(R.layout.dialog_cust_notify);
        notifyDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        notifyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notifyDialog.setCancelable(true);
        notifyDialog.show();

        TextView tv_confirm = notifyDialog.findViewById(R.id.tv_confirm);
        TextView tv_msg = notifyDialog.findViewById(R.id.tv_msg);
        if (notifyType.equals("1")) {
           /* String[] newStr = reason.split("\\s+");
            for (int i = 0; i < newStr.length; i++) {
                System.out.println(newStr[i]);
                reason = newStr[0];
            }*/
            reason = "advertisement";
        }
        tv_msg.setText("Failed to upload \"Photo\" for the reason\n of \"" + reason + "\". Please try to change one.");

        tv_confirm.setOnClickListener(view -> {
            notifyDialog.dismiss();
        });

    }


}