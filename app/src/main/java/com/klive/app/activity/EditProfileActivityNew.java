package com.klive.app.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.klive.app.R;
import com.klive.app.databinding.ActivityEditProfileNewBinding;
import com.klive.app.dialogs.EditProfileSetDataDialog;
import com.klive.app.model.ProfileDetailsResponse;
import com.klive.app.model.UserListResponse;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.utils.BaseActivity;
import com.klive.app.utils.Constant;
import com.klive.app.utils.DateCallback;
import com.klive.app.utils.DateFormatter;
import com.klive.app.utils.Datepicker;
import com.klive.app.utils.SessionManager;

import java.io.File;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivityNew extends BaseActivity implements ApiResponseInterface {

    ActivityEditProfileNewBinding binding;
    ApiManager apiManager;
    List<UserListResponse.UserPics> imageList;

    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    private MultipartBody.Part picToUpload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_edit_profile_new);
        hideStatusBar(true);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_new);

        loadData();

        Listeners();


    }

    private void Listeners() {

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

                Intent intent = new Intent(EditProfileActivityNew.this, ImagePickerActivity.class);
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {

                Uri selectedImageUri = data.getParcelableExtra("path");
                Log.e("selectimg", "selectedImageUri==" + selectedImageUri);
                String picturePath = selectedImageUri.getPath();

                Log.e("picture", picturePath);

                if (!picturePath.equals("Not found")) {
                    File picture = new File(picturePath);
                    // Compress Image
                    File file = new Compressor(this).compressToFile(picture);

                    Glide.with(getApplicationContext()).load(file).into(binding.profilePic);

                    Log.e("selectedImageEdit", "selectedImageEdit:" + file);

                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    picToUpload = MultipartBody.Part.createFormData("profile_pic[]", file.getName(), requestBody);

                    updateData("profilePic");

                    // apiManager.updateProfileDetails("", "", "", "", picToUpload, is_album);
                } else {
                    Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Please select another image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void updateData(String type) {

        if (type.equals("dob")) {

            apiManager.updateProfileDetailsNew("dob", "", "", binding.age.getText().toString(), "", null, false);

        } else if (type.equals("profilePic")) {

            //  apiManager.updateProfileDetails("aboutUser", "", "", binding.age.getText().toString(), null, false);

            if (picToUpload != null) {
                apiManager.updateProfileDetailsNew("profilePic", "", "", "", "", picToUpload, false);
            } else {

            }


        }
        // loadData();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.PROFILE_DETAILS) {
            ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;
            imageList = rsp.getSuccess().getProfile_images();

            String profilepic = "";

            Log.e("imgAlbum", new Gson().toJson(rsp.getSuccess().getProfile_images()));
            Log.e("imgAlbumsize", String.valueOf(imageList.size()));

            //Allow add pic if less than 6
            if (imageList.size() <= 5) {
                for (int i = imageList.size() - 1; i < 6; i++) {
                    Log.e("addPlusisData", String.valueOf(imageList.size()));
                    UserListResponse.UserPics pic = new UserListResponse.UserPics();

                    pic.setImage_name("add_pic");
                    imageList.add(pic);
                }
            }


            Log.e("EditProfile", "isSuccess: updatedName " + rsp.getSuccess().getName());


            for (int j = 0; j < imageList.size(); j++) {
                if (imageList.get(j).getIs_profile_image() == 1) {
                    profilepic = imageList.get(j).getImage_name();
                }
            }

            SessionManager sessionManager = new SessionManager(this);
            sessionManager.setUserName(rsp.getSuccess().getName());
            sessionManager.setUserProfilepic(profilepic);


            binding.name.setText(rsp.getSuccess().getName());
            binding.city.setText(rsp.getSuccess().getCity());


            String[] date = rsp.getSuccess().getDob().split("-");
            String makeDob = date[1] + "-" + date[0] + "-" + date[2];

            binding.age.setText(makeDob);

            // binding.age.setText(rsp.getSuccess().getDob());

            String intro = rsp.getSuccess().getAbout_user();

            if (intro != null) {
                if (intro.length() > 11) {
                    intro = intro.substring(0, 11) + "...";
                }

            }

            binding.yourIntro.setText(intro);

            Log.e("AboutUserTest", "isSuccess: " + rsp.getSuccess().getAbout_user());

            Glide.with(getApplicationContext()).load(profilepic).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).into(binding.profilePic);


            binding.nameLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new EditProfileSetDataDialog(EditProfileActivityNew.this, "Change Name", rsp.getSuccess().getName());
                }
            });


            binding.yourIntroLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new EditProfileSetDataDialog(EditProfileActivityNew.this, "Edit Introduction", rsp.getSuccess().getAbout_user());
                }
            });


       /*     binding.cityLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new EditProfileSetDataDialog(EditProfileActivityNew.this, "Change Country", rsp.getSuccess().getCity());
                }
            });*/


            binding.ageLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Datepicker().selectDateFrom(EditProfileActivityNew.this, binding.age, "", new DateCallback() {
                        @Override
                        public void onDateGot(String date, long timeStamp) {
                            binding.age.setText(DateFormatter.getInstance().format(date));
                            updateData("dob");
                        }
                    });
                }
            });

            binding.profileLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  changeImage()
                    //  PickImage();

                    PickImage2();
                }
            });


            Log.e("updatedpic", "isSuccess: editprof " + profilepic);


        }

    }


    public void loadData() {
        apiManager = new ApiManager(this, this);
        apiManager.getProfileDetails();
    }
}