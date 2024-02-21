package com.privatepe.app.activity.addalbum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.mlkit.vision.common.InputImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.privatepe.app.R;
import com.privatepe.app.activity.BasicInformation;
import com.privatepe.app.activity.ImagePickerActivity;
import com.privatepe.app.databinding.ActivityAddAlbumBinding;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddAlbumActivity extends AppCompatActivity implements ApiResponseInterface {
    ActivityAddAlbumBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_album);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_album);

        binding.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
                /*dialog1 = new Dialog(AddAlbumActivity.this);
                dialog1.setContentView(R.layout.choose_image_option);
                dialog1.setCancelable(false);
                dialog1.setCanceledOnTouchOutside(true);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView tv_cancel = dialog1.findViewById(R.id.tv_cancel);
                LinearLayout ll_gallery = dialog1.findViewById(R.id.ll_gallery);
                LinearLayout ll_camera = dialog1.findViewById(R.id.ll_camera);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                ll_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickImage();
                        dialog1.dismiss();
                    }
                });

                ll_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // dataList = new ArrayList<>();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra("camerasensortype", 2);
                        startActivityForResult(intent, CAMERA_REQUEST);
                        dialog1.dismiss();
                    }
                });
                dialog1.show();*/
            }
        });

        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ApiManager(AddAlbumActivity.this, AddAlbumActivity.this).uploadAlbumImageNew(albumImages);
            }
        });
    }

    Dialog dialog1;

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
                        Intent intent = new Intent(AddAlbumActivity.this, ImagePickerActivity.class);
                        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
                        // setting aspect ratio
                        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 2); // 16x9, 1x1, 3:4, 3:2
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 3);
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

    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    private static final int CAMERA_REQUEST = 610;
    MultipartBody.Part[] albumImages;
    int count = 0;

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {

                // Uri selectedImageUri = data.getParcelableExtra("path");
                // Log.e("selectimg", "selectedImageUri==" + data.getClipData().getItemCount());

               /* if (data.getClipData() != null) {
                    //dataList = new ArrayList<>();
                    count =  data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (int i = 0; i < count; i++) {
                        String newString = data.getClipData().getItemAt(i).getUri().getPath();
                        newString = newString.replace("/raw/", "");
                        dataList.add(newString);
                    }
                    albumImages = new MultipartBody.Part[dataList.size()];
                    for (int index = 0; index < dataList.size(); index++) {
                        // Log.d(TAG, "requestUploadSurvey: survey image " + index + "  " + surveyModel.getPicturesList().get(index).getImagePath());
                        File picture = new File(dataList.get(index));
                        File file = new Compressor(AddAlbumActivity.this).compressToFile(picture);
                        RequestBody surveyBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        albumImages[index] = MultipartBody.Part.createFormData("album_pic[]", file.getName(), surveyBody);
                    }
                    showImage(count, data);
                }*/
                // if (data.getData()!=null){
                Uri imageUri = data.getParcelableExtra("path");
                Log.e("picturewee", "imageUri => " + imageUri.getPath());
                String newString = imageUri.getPath();
                newString = newString.replace("/raw/", "");
                dataList.add(newString);

                albumImages = new MultipartBody.Part[dataList.size()];
                for (int index = 0; index < dataList.size(); index++) {
                    // Log.d(TAG, "requestUploadSurvey: survey image " + index + "  " + surveyModel.getPicturesList().get(index).getImagePath());
                    File picture = new File(dataList.get(index));
                    File file = new Compressor(AddAlbumActivity.this).compressToFile(picture);
                    RequestBody surveyBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    albumImages[index] = MultipartBody.Part.createFormData("album_pic[]", file.getName(), surveyBody);
                }
                showImage(count, data);
                //   }


            } catch (Exception e) {
                Log.e("picturewee", e.toString());
                Toast.makeText(this, "Please select another image", Toast.LENGTH_SHORT).show();
            }
        }
        Log.e("picturewee", String.valueOf(requestCode));

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            try {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // call this method to get the URI from the bitmap
                Uri selectedCamera = getImageUri(getApplicationContext(), photo);
                binding.img0.setImageURI(selectedCamera);
                dataList.add(selectedCamera.getPath());

                int count = dataList.size(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
               /* for (int i = 0; i < count; i++) {
                    String newString = dataList.get(i);
                  //  newString = newString.replace("/raw/", "");
                    dataList.add(newString);
                }*/

                showImageCamera(count, data);
            } catch (Exception e) {
                Log.e("picturewee", "error => " + e.toString());
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Capture", null);
        return Uri.parse(path);
    }

    List<String> dataList = new ArrayList<>();

    private void showImageToControl(ImageView imageView, String uri) {
        Glide.with(getApplicationContext())
                .load(new File(uri))
                .centerCrop()
                .into(imageView);
    }

    private void showImage(int count, Intent data) {
        switch (dataList.size()) {
            case 1:
                binding.img0.setVisibility(View.GONE);
                binding.imgDis0.setVisibility(View.VISIBLE);
                //  binding.imgDis0.setImageURI(Uri.parse(dataList.get(0)));
                showImageToControl(binding.imgDis0, dataList.get(0));
                break;
            case 2:

                binding.img1.setVisibility(View.GONE);
                binding.imgDis1.setVisibility(View.VISIBLE);
                // binding.imgDis1.setImageURI(Uri.parse(dataList.get(1)));
                showImageToControl(binding.imgDis1, dataList.get(1));
                break;
            case 3:

                binding.img2.setVisibility(View.GONE);
                binding.imgDis2.setVisibility(View.VISIBLE);
                // binding.imgDis2.setImageURI(Uri.parse(dataList.get(2)));
                showImageToControl(binding.imgDis2, dataList.get(2));

                break;
            case 4:

                binding.img3.setVisibility(View.GONE);
                binding.imgDis3.setVisibility(View.VISIBLE);
                //  binding.imgDis3.setImageURI(Uri.parse(dataList.get(3)));
                showImageToControl(binding.imgDis3, dataList.get(3));

                break;
            case 5:

                binding.img4.setVisibility(View.GONE);
                binding.imgDis4.setVisibility(View.VISIBLE);
                //  binding.imgDis4.setImageURI(Uri.parse(dataList.get(4)));
                showImageToControl(binding.imgDis4, dataList.get(4));

                break;
            case 6:

                binding.img5.setVisibility(View.GONE);
                binding.imgDis5.setVisibility(View.VISIBLE);
                //   binding.imgDis5.setImageURI(Uri.parse(dataList.get(5)));
                showImageToControl(binding.imgDis5, dataList.get(5));

                break;
        }
    }

    private void showImageCamera(int count, Intent data) {
        switch (count) {
            case 1:
                binding.img0.setImageURI(data.getClipData().getItemAt(0).getUri());
                break;
            case 2:
                binding.img0.setImageURI(data.getClipData().getItemAt(0).getUri());
                binding.img1.setImageURI(data.getClipData().getItemAt(1).getUri());
                break;
            case 3:
                binding.img0.setImageURI(data.getClipData().getItemAt(0).getUri());
                binding.img1.setImageURI(data.getClipData().getItemAt(1).getUri());
                binding.img2.setImageURI(data.getClipData().getItemAt(2).getUri());
                break;
            case 4:
                binding.img0.setImageURI(data.getClipData().getItemAt(0).getUri());
                binding.img1.setImageURI(data.getClipData().getItemAt(1).getUri());
                binding.img2.setImageURI(data.getClipData().getItemAt(2).getUri());
                binding.img3.setImageURI(data.getClipData().getItemAt(3).getUri());
                break;
            case 5:
                binding.img0.setImageURI(data.getClipData().getItemAt(0).getUri());
                binding.img1.setImageURI(data.getClipData().getItemAt(1).getUri());
                binding.img2.setImageURI(data.getClipData().getItemAt(2).getUri());
                binding.img3.setImageURI(data.getClipData().getItemAt(3).getUri());
                binding.img4.setImageURI(data.getClipData().getItemAt(4).getUri());
                break;
            case 6:
                binding.img0.setImageURI(data.getClipData().getItemAt(0).getUri());
                binding.img1.setImageURI(data.getClipData().getItemAt(1).getUri());
                binding.img2.setImageURI(data.getClipData().getItemAt(2).getUri());
                binding.img3.setImageURI(data.getClipData().getItemAt(3).getUri());
                binding.img4.setImageURI(data.getClipData().getItemAt(4).getUri());
                binding.img5.setImageURI(data.getClipData().getItemAt(5).getUri());
                break;
        }

        albumImages = new MultipartBody.Part[dataList.size()];
        for (int index = 0; index < dataList.size(); index++) {
            // Log.d(TAG, "requestUploadSurvey: survey image " + index + "  " + surveyModel.getPicturesList().get(index).getImagePath());
            File picture = new File(dataList.get(index));
            File file = null;
            try {
                file = new Compressor(AddAlbumActivity.this).compressToFile(picture);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RequestBody surveyBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            albumImages[index] = MultipartBody.Part.createFormData("album_pic[]", file.getName(), surveyBody);
        }
    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.ALBUM_UPLOADED) {
            new SessionManager(getApplicationContext()).setResUpload("1");
            startActivity(new Intent(AddAlbumActivity.this, ShotVideoActivity.class));
            finish();
        }

    }
}