package com.privatepe.host.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
/*import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;*/

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import com.google.mlkit.vision.common.InputImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.privatepe.host.R;
import com.privatepe.host.utils.NetworkCheck;

import java.io.File;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfilePhoto extends AppCompatActivity {
    private NetworkCheck networkCheck;
    String picturePath = "";
    ImageView imageView;
    TextView continue_profile;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
     //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_profile_photo);
        networkCheck = new NetworkCheck();
        imageView = findViewById(R.id.userImage);
        continue_profile = findViewById(R.id.continue_profile);
    }

    public void confirm(View view) {
        if(networkCheck.isNetworkAvailable(getApplicationContext())) {
            if (!picturePath.equals("")) {
                startActivity(new Intent(getApplicationContext(), Album.class).putExtra("profileImage", picturePath));
            }else {
                Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImage() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(ProfilePhoto.this, ImagePickerActivity.class);
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
                    /*imageView.setImageURI(selectedImageUri);
                    imageView.setVisibility(View.VISIBLE);*/
                    File picture = new File(picturePath);
                    continue_profile.setBackground(getApplicationContext().getDrawable(R.drawable.active));
                    // Compress Image
                    File file = new Compressor(this).compressToFile(picture);
                    Log.e("selectedImageEdit", "selectedImageEdit:" + file);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    //RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part picToUpload = MultipartBody.Part.createFormData("profile_pic[]", file.getName(), requestBody);

                    imageBitmap = BitmapFactory.decodeFile(picturePath);
                    inputImage = InputImage.fromBitmap(imageBitmap, 90);

                    detectTextFromImage();

                    //Create request body with text description and text media type
                    //RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
                    //RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), "image-type");

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

    public void image(View view) {
        pickImage();
    }

    public void backtobasic(View view) {
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
                    picturePath="";
                    imageView.setVisibility(View.GONE);
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
                picturePath="";
                imageView.setVisibility(View.GONE);

                addnotifyDialog("1", "text");
            }
        });


       /* FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText); // call method'

                Log.e("detectTextFromImage", "onSuccess: "+"text Detected" );

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePhoto.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        });*/

    }

 /*   private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();

        if (blockList.size() == 0) {
           // Toast.makeText(this, "No Text Found In Image", Toast.LENGTH_SHORT).show();
            detectFace(imageBitmap);
        } else {
            for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
                String text = block.getText();
                //  textView.setText(text);
                Log.e("textInImageLog", text);
                continue_profile.setEnabled(false);
                continue_profile.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));


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
                                        picturePath="";
                                        imageView.setVisibility(View.GONE);

                                        addnotifyDialog("2", "No face");
                                    //    continue_profile.setEnabled(false);
                                      //  continue_profile.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                                    } else {
                                        //  apiManager.updateProfileDetails("", "", "", "", picToUpload, is_album);

                                        imageView.setImageBitmap(imageBitmap);
                                        imageView.setVisibility(View.VISIBLE);

                                     //   continue_profile.setEnabled(true);

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
                                    picturePath="";
                                    imageView.setVisibility(View.GONE);

                                    addnotifyDialog("2", "No face");

                                  //  continue_profile.setEnabled(false);
                                  //  continue_profile.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                                }
                            }
                        }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                               /* if (!is_album) {
                                    addPosterdialog.dismiss();
                                }*/
                        picturePath="";
                        imageView.setVisibility(View.GONE);

                        addnotifyDialog("2", "No face");
                      //  continue_profile.setEnabled(false);
                      //  continue_profile.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                        // Toast.makeText(EditActivity.this, "No Face found", Toast.LENGTH_SHORT).show();
                    }
                }); }



    public void addnotifyDialog(String notifyType, String reason) {

        Dialog notifyDialog = new Dialog(ProfilePhoto.this);
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