package com.klive.app.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
/*import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;*/
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
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
import com.klive.app.R;
import com.klive.app.dialogs.bioDialog;
import com.klive.app.dialogs.cityDialog;
import com.klive.app.dialogs.languageDialog;
import com.klive.app.dialogs.nameDialog;
import com.klive.app.dialogs.whtasappDialog;
import com.klive.app.model.Profile;
import com.klive.app.model.ProfileDetailsResponse;
import com.klive.app.model.UpdateProfileNewResponse;
import com.klive.app.model.UserListResponse;
import com.klive.app.recycler.ProfileAdapter;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.utils.Constant;
import com.klive.app.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditActivity extends AppCompatActivity implements ApiResponseInterface {
    SessionManager sessionManager;
    HashMap<String, String> user;
    RelativeLayout name, age, city, language, bio, whatsapp, profile_lay;
    DatePickerDialog picker;
    TextView userName, date, cityView, langView;
    private ApiManager apiManager;
    CircleImageView viewImage, edit;
    ImageView profile_pic, img_posterdisplay;
    ImageView back;
    String picturePath = "";
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    RelativeLayout logoff;

    String Userbio = "";

    RelativeLayout addImage, addImageRecycler;
    public static RecyclerView recyclerView;
    public static RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;
    private List<UserListResponse.UserPics> imgList;
    private boolean isAlbum = false;
    private File UploadImagefile;
    private Integer currentLevel;

    public EditActivity() {
        // require a empty public constructor
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    boolean isImageStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.edit);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //about = v.findViewById(R.id.aboutRl);
        imgList = new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());
        apiManager = new ApiManager(getApplicationContext(), this);
        user = new SessionManager(getApplicationContext()).getUserDetails();

        new ApiManager(getApplicationContext(), this).getProfileDetails();
        init();
        new ApiManager(getApplicationContext(), this).getNewImageUploadStatus();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
        String img_url = sharedPref.getString(getString(R.string.img_url), "");

        //if(!img_url.equals("")) {
        Glide.with(getApplicationContext()).load(sessionManager.getUserProfilepic()).placeholder(R.drawable.default_profile).into(viewImage);
        //}

        Glide.with(getApplicationContext())
                .load(sessionManager.getUserProfilepic())
                .placeholder(R.drawable.default_profile)
                .into(profile_pic);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameDialog name = new nameDialog(EditActivity.this, "Bearer " + sessionManager.getUserToken(), sessionManager.getUserName());
                name.show();
                name.setDialogResult(new nameDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result) {
                        userName.setText(result);
                        // langId = id;
                        //isLanguageSelected = true;
                        sessionManager.setUserName(result);
                        apiManager.updateProfileDetails(result, "name");
                    }
                });
            }
        });
        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new ageDialog(EditActivity.this);

                final Calendar cldr = Calendar.getInstance();
                cldr.add(Calendar.YEAR, -18);
                long upperLimit = cldr.getTimeInMillis();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //date.setText(year + "-" + dayOfMonth + "-" + (monthOfYear + 1));

                                date.setText(getAge(year, (monthOfYear + 1), dayOfMonth));
                                //isAgeSelected = true;
                                //sessionManager.setUserAge(year + "-" + dayOfMonth + "-" + (monthOfYear + 1));
                                //enableButton();
                                new SessionManager(getApplicationContext()).setUserDob(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                sessionManager.setUserAge(getAge(year, (monthOfYear + 1), dayOfMonth));
                                apiManager.updateProfileDetails(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "age");
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(upperLimit);
                picker.show();
            }
        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new cityDialog(EditActivity.this, sessionManager.getUserToken());
                cityDialog cityDialog = new cityDialog(EditActivity.this, "Bearer " + sessionManager.getUserToken());
                cityDialog.show();
                cityDialog.setDialogResult(new cityDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result) {
                        cityView.setText(result);
                        //isCitySelected = true;
                        sessionManager.setUserAddress(result);
                        apiManager.updateProfileDetails(result, "city");
                    }
                });
            }
        });
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageDialog languageDialog = new languageDialog(EditActivity.this, "Bearer " + sessionManager.getUserToken());
                languageDialog.show();
                languageDialog.setDialogResult(new languageDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result, String id) {
                        langView.setText(result);
                        //langId = id;
                        //isLanguageSelected = true;
                        sessionManager.setLanguage(result);
                        sessionManager.setLanguage(result);
                        apiManager.updateProfileDetails(id, "language");
                    }
                });
            }
        });
        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new bioDialog(EditActivity.this);
                bioDialog bio = new bioDialog(EditActivity.this, "Bearer " + sessionManager.getUserToken(), sessionManager.getBio());
                bio.show();
                bio.setDialogResult(new bioDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result) {
                        //langView.setText(result);
                        // langId = id;
                        //isLanguageSelected = true;
                        sessionManager.setBio(result);
                        apiManager.updateProfileDetails(result, "bio");
                    }
                });
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*WhatsappFragment fragment = new WhatsappFragment();
                fragment.setCancelable(true);
                fragment.show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");*/

                new whtasappDialog(EditActivity.this);
            }
        });
        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SessionManager(getApplicationContext()).logoutUser();
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dispatchTakePictureIntent();
                isAlbum = false;
                // if(currentLevel != null && currentLevel>=3){
                // pickImage();
                PickImage2();
                // }else
                // Toast.makeText(EditActivity.this,"you can not upload profile image because you level is less then 3.",Toast.LENGTH_SHORT).show();
            }
        });

        profile_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPhotoUrl = new SessionManager(getApplicationContext()).getUserNewPhoto();
                Log.e("isImageStatus=====", isImageStatus + "");
                if (isImageStatus) {
                    addposterDialog();
                } else {
                    //  pickImage();
                    PickImage2();
                }

            }
        });

        addImageRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAlbum = true;
                pickImageAlbum();
            }
        });
        // apiManager.updateProfileDetails("", "", "", "", "");
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        city = findViewById(R.id.city);
        language = findViewById(R.id.language);
        bio = findViewById(R.id.bio);
        whatsapp = findViewById(R.id.whatsapp);
        date = findViewById(R.id.date);
        back = findViewById(R.id.back);
        viewImage = findViewById(R.id.viewImage);
        edit = findViewById(R.id.edit);
        logoff = findViewById(R.id.logout);
        cityView = findViewById(R.id.cityView);
        langView = findViewById(R.id.langView);
        userName = findViewById(R.id.userName);
        profile_pic = findViewById(R.id.profile_pic);
        addImageRecycler = findViewById(R.id.addImageRecycler);
        profile_lay = findViewById(R.id.profile_lay);
        cityView.setText(sessionManager.getUserAddress());
        langView.setText(sessionManager.getLanguage());
        userName.setText(sessionManager.getUserName());
        Userbio = sessionManager.getBio();
        String age = sessionManager.getUserDob();
        String[] dobAge = age.split("-");

        date.setText(getAge(Integer.parseInt(dobAge[2]), Integer.parseInt(dobAge[0]), Integer.parseInt(dobAge[1])));
        cityView.setText(sessionManager.getUserAddress());

        langView.setText(sessionManager.getLanguage());

        recyclerView = findViewById(R.id.images);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProfileAdapter(getApplicationContext(), imgList);
        recyclerView.setAdapter(adapter);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            viewImage.setImageBitmap(imageBitmap);
            try {
                if(  Uri.parse(getImageUri(getApplicationContext(), imageBitmap).toString())!=null   ){
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver() , Uri.parse(getImageUri(getApplicationContext(), imageBitmap).toString()));
                }
            }
            catch (Exception e) {
                //handle exception
            }
            String path = getImageUri(getApplicationContext(), imageBitmap).toString();
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    getString(R.string.shared_preference), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.img_url), path);
            editor.commit();
        }

    }*/
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, simpleDateFormat.format(date), null);
        return Uri.parse(path);
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

/*        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }*/

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.PROFILE_DETAILS) {
            ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;

            //String level = String.valueOf(rsp.getSuccess().getLevel());
            String userDob = rsp.getSuccess().getDob();
            String[] age = userDob.split("-");

            SessionManager sessionManager = new SessionManager(getApplicationContext());
            String img = "";
            if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                img = rsp.getSuccess().getProfile_images().get(0).getImage_name();
            }
            new SessionManager(getApplicationContext()).setUserAge(getAge(Integer.parseInt(age[2]), Integer.parseInt(age[0]), Integer.parseInt(age[1])));
            new SessionManager(getApplicationContext()).setUserLevel(String.valueOf(rsp.getSuccess().getLevel()));
            new SessionManager(getApplicationContext()).setBio(String.valueOf(rsp.getSuccess().getAbout_user()));
            new SessionManager(getApplicationContext()).setUserAddress(String.valueOf(rsp.getSuccess().getCity()));
            new SessionManager(getApplicationContext()).setUserDob(String.valueOf(rsp.getSuccess().getDob()));
            new SessionManager(getApplicationContext()).setLanguage(String.valueOf(rsp.getSuccess().getUser_languages().get(0).getLanguage_name()));

            new SessionManager(getApplicationContext()).setUserProfilepic(img);
            currentLevel = rsp.getSuccess().getLevel();
            Log.e("edit level==>", rsp.getSuccess().getLevel() + "");

            Glide.with(getApplicationContext())
                    .load(img)
                    .placeholder(R.drawable.default_profile)
                    .into(viewImage);


            // level.setText("Lv "+sessionManager.getUserLevel());
            // user_profile_name.setText(sessionManager.getName()+", "+sessionManager.getUserAge());
        }

        if (ServiceCode == Constant.UPDATE_PROFILE_NEW) {

            UpdateProfileNewResponse updateProfileNewResponse = (UpdateProfileNewResponse) response;
            if (updateProfileNewResponse.getSuccess()) {
                isImageStatus = true;
                new SessionManager(getApplicationContext()).setUserNewPhoto(updateProfileNewResponse.getData().getImage_name());
                addposterDialog();
            }

        }

        if (ServiceCode == Constant.PROFILE_NEW_IMAGE_STATUS) {

            Log.e("PROFILE_IMAGE_STATUS==", new Gson().toJson(response));
            UpdateProfileNewResponse updateProfileNewResponse = (UpdateProfileNewResponse) response;
            if (updateProfileNewResponse.getSuccess()) {
                isImageStatus = true;
                new SessionManager(getApplicationContext()).setUserNewPhoto(updateProfileNewResponse.getData().getImage_name());
            } else
                isImageStatus = false;
        }
    }

    public void addposterDialog() {

        Dialog addPosterdialog = new Dialog(EditActivity.this);
        addPosterdialog.setContentView(R.layout.dialog_profile_pick_image);
        addPosterdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addPosterdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addPosterdialog.setCancelable(true);
        addPosterdialog.show();

        TextView tv_dailogmsg = addPosterdialog.findViewById(R.id.tv_dailogmsg);

        String text = "Please upload a <font color=\"#a20be9\">clear</font> and <font color=\"#a20be9\">beautiful</font> poster of <font color=\"#a20be9\">yourself</font>";
        tv_dailogmsg.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        img_posterdisplay = addPosterdialog.findViewById(R.id.img_posterdisplay);

        Button btnUpload = addPosterdialog.findViewById(R.id.btnUpload);

        String newPhotoUrl = new SessionManager(getApplicationContext()).getUserNewPhoto();
        Log.e("newPhotoUrl===>", newPhotoUrl + "");
        if (!newPhotoUrl.equalsIgnoreCase("null")) {
            Log.e("newPhotoUrl===>", "if====>" + newPhotoUrl + "");
            Glide.with(getApplicationContext())
                    .load(newPhotoUrl)
                    .placeholder(R.drawable.default_profile)
                    .into(img_posterdisplay);
            btnUpload.setText("Under Review");
            btnUpload.setBackgroundResource(R.drawable.pending_button_bg);
        } else {

            Log.e("newPhotoUrl===>", "else===>" + new SessionManager(getApplicationContext()).getUserProfilepic());
            Glide.with(getApplicationContext())
                    .load(new SessionManager(getApplicationContext()).getUserProfilepic())
                    .placeholder(R.drawable.default_profile)
                    .into(img_posterdisplay);
        }
        ImageView img_backarrow = addPosterdialog.findViewById(R.id.img_backarrow);

        img_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (flag.equals("opendailog")) {
                    finish();
                } else {*/
                addPosterdialog.dismiss();
                // }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //boolean is_album = false;
                if (btnUpload.getText().toString().equalsIgnoreCase("upload"))
                    // pickImage();
                    PickImage2();
                else
                    Toast.makeText(EditActivity.this, "Your Image already in review", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void PickImage2() {

        String permissions;
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        }

        Dexter.withActivity(this).withPermission(permissions).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Intent intent = new Intent(EditActivity.this, ImagePickerActivity.class);
                intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
                // setting aspect ratio
                intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
                intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
                intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
                startActivityForResult(intent, PICK_IMAGE_GALLERY_REQUEST_CODE);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();


    }



    private void pickImageAlbum() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(EditActivity.this, ImagePickerActivity.class);
                        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
                        // setting aspect ratio
                        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
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
                    if (!isAlbum) {

                        File picture = new File(picturePath);


                        UploadImagefile = new Compressor(EditActivity.this).compressToFile(picture);
                        Log.e("selectedImageEdit", "selectedImageEdit:" + UploadImagefile);

                        imageBitmap = BitmapFactory.decodeFile(picturePath);
                        inputImage = InputImage.fromBitmap(imageBitmap, 90);

                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), UploadImagefile);
                        MultipartBody.Part picToProfile = MultipartBody.Part.createFormData("profile_pic", UploadImagefile.getName(), requestBody);
                        //   apiManager.uploadProfileImage(picToProfile);
                        detectTextFromImage();
                    } else if (isAlbum) {
                        /*imgList.add(new Profile(2,  selectedImageUri.toString(), "yes"));
                        adapter.notifyDataSetChanged();*/ //naval
                    }
                    //new ApiManager(getApplicationContext(), this).getProfileDetails();
                    //apiManager.updateProfileDetails("", "", "", "", picToUpload, is_album);
                } else {
                    Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("picturewee", e.toString());
                Toast.makeText(this, "Please select another image", Toast.LENGTH_SHORT).show();
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
                //Log.e("textDet", "onSuccess  => " + text.getText().toString());
                if (text.getText().isEmpty()) {
                    // Toast.makeText(EditProfile.this, "No text found", Toast.LENGTH_SHORT).show();
                    detectFace(imageBitmap);
                } else {
                    // Toast.makeText(EditProfile.this, "Text Found", Toast.LENGTH_SHORT).show();
                   /* if (!is_album) {
                        addPosterdialog.dismiss();
                    }*/
                    addnotifyDialog("1", "text");
                }
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Log.e("textDet", "onFailure => " + e.getMessage());
               /* if (!is_album) {
                    addPosterdialog.dismiss();
                }*/
                addnotifyDialog("1", "text");
            }
        });

       /* FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText); // call method

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        });*/

    }

   /* private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();

        if (blockList.size() == 0) {
         //   Toast.makeText(this, "No Text Found In Image", Toast.LENGTH_SHORT).show();
            detectFace(imageBitmap);
        } else {
            for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
                String text = block.getText();
                //  textView.setText(text);
                Log.e("textInImageLog", text);
                *//*if (!is_album) {
                    addPosterdialog.dismiss();
                }*//*
               addnotifyDialog("1", text);
              //  Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            }
        }
    }*/

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
                                //drawFace(faces);
                                //   Log.e("foundFaceLog", faces.toString());
                                try {
                                    if (faces.get(0).toString().equals("")) {
                                        /*if (!is_album) {
                                            addPosterdialog.dismiss();
                                        }*/
                                        // Toast.makeText(EditActivity.this, "No Face found", Toast.LENGTH_SHORT).show();
                                        addnotifyDialog("2", "No face");
                                    } else {
                                        //  apiManager.updateProfileDetails("", "", "", "", picToUpload, is_album);
                                        viewImage.setImageBitmap(imageBitmap);
                                        viewImage.setVisibility(View.VISIBLE);
                                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), UploadImagefile);
                                        MultipartBody.Part picToProfile = MultipartBody.Part.createFormData("profile_pic", UploadImagefile.getName(), requestBody);
                                        apiManager.uploadProfileImageNew(picToProfile);

                                        //   Toast.makeText(EditActivity.this, "Face found", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                   /* if (!is_album) {
                                        addPosterdialog.dismiss();
                                    }*/
                                    // Toast.makeText(EditActivity.this, "No Face found", Toast.LENGTH_SHORT).show();
                                    addnotifyDialog("2", "No face");
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
                                // Toast.makeText(EditActivity.this, "No Face found", Toast.LENGTH_SHORT).show();
                            }
                        });
    }


    public void addnotifyDialog(String notifyType, String reason) {

        Dialog notifyDialog = new Dialog(EditActivity.this);
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