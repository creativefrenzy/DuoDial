package com.privatepe.app.activity;

import static com.privatepe.app.login.OTPVerify.rsp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
/*import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;*/


import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import com.google.android.gms.tasks.Task;

import com.google.mlkit.vision.common.InputImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.privatepe.app.R;
import com.privatepe.app.adapter.AlbumAdapter;
import com.privatepe.app.model.LoginResponse;
import com.privatepe.app.model.SubmitResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.sqlite.Chat;
import com.privatepe.app.sqlite.SystemDB;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.NetworkCheck;
import com.privatepe.app.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Album extends AppCompatActivity implements ApiResponseInterface {
    private NetworkCheck networkCheck;
    String profileImage = "";
    String picturePath = "";
    ImageView userImage1;
    ApiManager apiManager;
    SessionManager session;
    MultipartBody.Part picToAlbum;
    TextView confirmButton;

    private static String mobile, android_id, token, name, age, city, language;

    List<String> imageList;


    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    List<String> dataList;
    private GridLayoutManager layoutManager;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    private RelativeLayout addImage;
    private CircleImageView addIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_album);
        networkCheck = new NetworkCheck();
        imageList = new ArrayList<>();
        dataList = new ArrayList<>();
        confirmButton = findViewById(R.id.confirm_button);

        addImage = findViewById(R.id.addImage);
        addIcon = findViewById(R.id.add_icon);
        apiManager = new ApiManager(Album.this, this);
        session = new SessionManager(Album.this);

        recyclerView = findViewById(R.id.languageRecycler);
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);


        profileImage = getIntent().getStringExtra("profileImage");

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                    if (!profileImage.equals("")) {
                        Log.e("Album", "onClick: " + "start");
                        try {
                            if (!profileImage.equals("Not found")) {

                                Log.e("Album", "onClick: " + "notfound");

                                MultipartBody.Part[] albumImages = new MultipartBody.Part[dataList.size()];

                                for (int index = 0; index < dataList.size(); index++) {
                                    // Log.d(TAG, "requestUploadSurvey: survey image " + index + "  " + surveyModel.getPicturesList().get(index).getImagePath());
                                    File file = new File(dataList.get(index));
                                    RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
                                    albumImages[index] = MultipartBody.Part.createFormData("album_pic[]", file.getName(), surveyBody);
                                }


                                File picture = new File(profileImage);
                                // Compress Image
                                File file = null;
                                file = new Compressor(Album.this).compressToFile(picture);
                                Log.e("selectedImageEdit", "selectedImageEdit:" + file);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                MultipartBody.Part picToProfile = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);

                                SharedPreferences prefsAgency = getSharedPreferences("AGENCY_DATA", MODE_PRIVATE);
                                String agency = prefsAgency.getString("agency", "");

                                SharedPreferences prefs = getSharedPreferences("OTP_DATA", MODE_PRIVATE);
                                mobile = prefs.getString("mobile", "");
                                android_id = prefs.getString("android_id", "");
                                token = prefs.getString("token", "");

                                SharedPreferences prefsBasic = getSharedPreferences("BASIC_DATA", MODE_PRIVATE);
                                name = prefsBasic.getString("name", "");
                                age = prefsBasic.getString("age", "");
                                city = prefsBasic.getString("city", "");
                                language = prefsBasic.getString("language", "");
                                //api call

                                //album upload is not being used in sign up process now
                                apiManager.updateProfileDetails(token, agency, mobile, android_id, name, age, city, language, picToProfile);


                            }
                        } catch (IOException e) {
                            //   e.printStackTrace();
                            Log.e("Album", "onClick: " + e.getMessage());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickImage() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(Album.this, ImagePickerActivity.class);
                        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
                        // setting aspect ratio
                        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
                        startActivityForResult(intent, PICK_IMAGE_GALLERY_REQUEST_CODE);
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
                    confirmButton.setBackground(getApplicationContext().getDrawable(R.drawable.active));

                    File picture = new File(picturePath);
                    File file = new Compressor(this).compressToFile(picture);
                    Log.e("selectedImageEdit", "selectedImageEdit:" + file);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    picToAlbum = MultipartBody.Part.createFormData("album_pic[]", file.getName(), requestBody);

                    imageBitmap = BitmapFactory.decodeFile(picturePath);
                    inputImage = InputImage.fromBitmap(imageBitmap, 90);

                    detectTextFromImage();

                   /* dataList.add(picturePath);
                    albumAdapter = new AlbumAdapter(getApplicationContext(), dataList);
                    recyclerView.setAdapter(albumAdapter);
                    albumAdapter.notifyDataSetChanged();*/


                } else {
                    Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("picturewee", e.toString());
                Toast.makeText(this, "Please select another image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void image1(View view) {
        pickImage();
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(getApplicationContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.UPDATE_PROFILE) {
            SubmitResponse responseType = (SubmitResponse) response;
            if (responseType.getResult().equals("Registration Successfully")) {
                String json = "Welcome to KLive. Enjoy your trip and find your true love here!\n\nDo not reveal your personal information, or open any unknown links to avoid information theft and financial loss.";
                SystemDB db = new SystemDB(getApplicationContext());

                String timesttamp = System.currentTimeMillis() + "";
                db.addChat(new Chat("System", "System", "", json, "", "", "", "", 0, timesttamp, "TEXT"));


                try {

                    if (rsp != null) {
                        if (rsp.getGuest_status() == 0 && rsp.getResult().getRole() == 2 || rsp.getResult().getRole() == 5) {
                            new SessionManager(this).saveGuestStatus(rsp.getGuest_status());
                            //new SessionManager(this).saveGuestStatus(Integer.parseInt(rsp.getAlready_registered()));
                            LoginResponse loginResponse = new LoginResponse();

                            String tempName = new SessionManager(getApplicationContext()).getName();

                            new LoginResponse.Result(rsp.getResult().getToken(), name, rsp.getResult().getGender(), rsp.getResult().getProfile_id(), city, "", rsp.getResult().getIs_online(), rsp.getGuest_status(), rsp.getResult().getAllow_in_app_purchase(), rsp.getResult().getRole(), rsp.getResult().getLogin_type(), rsp.getResult().getUsername(), "", android_id);

                            session.createLoginSession(rsp);
                            session.setUserEmail(rsp.getResult().getProfile_id());
                            session.setUserPassword(android_id);

                            new SessionManager(getApplicationContext()).setUserName(tempName);

                            startActivity(new Intent(getApplicationContext(), SubmitForm.class));
                        }
                    }


                } catch (Exception e) {
                    Log.e("Album", "isSuccess: Exception " + e.getMessage());
                }


            }
        }
    }

    public void backtoprofile(View view) {
        onBackPressed();
    }

    public void onBackPressed() {
        finish();
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


     /*   FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText); // call method

                Log.e("detectTextFromImage", "onSuccess: "+"text detected" );

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Album.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        });*/

    }

   /* private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();

        if (blockList.size() == 0) {
            //Toast.makeText(this, "No Text Found In Image", Toast.LENGTH_SHORT).show();
            detectFace(imageBitmap);

        } else {
            for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
                String text = block.getText();
                //  textView.setText(text);
                Log.e("textInImageLog", text);
                confirmButton.setEnabled(false);
                confirmButton.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                addImage.setEnabled(false);
                addnotifyDialog("1", text);

                *//*if (!is_album) {
                    addPosterdialog.dismiss();
                }*//*

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

                                        // confirmButton.setEnabled(false);
                                        // confirmButton.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                                        // addImage.setEnabled(false);
                                    } else {
                                        //  apiManager.updateProfileDetails("", "", "", "", picToUpload, is_album);

                                        dataList.add(picturePath);
                                        albumAdapter = new AlbumAdapter(getApplicationContext(), dataList);
                                        recyclerView.setAdapter(albumAdapter);
                                        albumAdapter.notifyDataSetChanged();

                                        //confirmButton.setEnabled(true);
                                        //addImage.setEnabled(true);

                                    /*    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), UploadImagefile);
                                        MultipartBody.Part picToProfile = MultipartBody.Part.createFormData("profile_pic", UploadImagefile.getName(), requestBody);
                                        apiManager.uploadProfileImage(picToProfile);*/

                                        // Toast.makeText(EditActivity.this, "Face found", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                   /* if (!is_album) {
                                        addPosterdialog.dismiss();
                                    }*/
                                    // Toast.makeText(EditActivity.this, "No Face found", Toast.LENGTH_SHORT).show();
                                    addnotifyDialog("2", "No face");

                                    /*confirmButton.setEnabled(false);
                                    confirmButton.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                                    addImage.setEnabled(false);*/
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
                        /*confirmButton.setEnabled(false);
                        confirmButton.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                        addImage.setEnabled(false);*/
                                // Toast.makeText(EditActivity.this, "No Face found", Toast.LENGTH_SHORT).show();
                            }
                        });
    }


    public void addnotifyDialog(String notifyType, String reason) {

        Dialog notifyDialog = new Dialog(Album.this);
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