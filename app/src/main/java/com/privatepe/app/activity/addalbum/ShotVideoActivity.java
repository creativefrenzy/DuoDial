package com.privatepe.app.activity.addalbum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;

import com.privatepe.app.R;
import com.privatepe.app.databinding.ActivityShotVideoBinding;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.CameraPreview;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ShotVideoActivity extends AppCompatActivity implements ApiResponseInterface {
    String filePath, fileName;
    ActivityShotVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shot_video);

        binding.cvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    File myDirectory = new File(Environment.getExternalStorageDirectory(), "/KLive");
                if (!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }*/
                filePath = "/storage/emulated/0/Android/data/com.privatepe.app" + "/video" + System.currentTimeMillis() + ".mp4";

               /* if (filePath != null) {
                    String[] fileNme = filePath.split("KLive/");
                    fileName = fileNme[1];
                    //Log.e(TAG, "==fileName===>" + fileName);
                }*/
                initTimerBroad();
                startTimerBroad();


                if (!prepareMediaRecorder()) {
                    Toast.makeText(getApplicationContext(), "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                }
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            cameraPreview.setVisibility(View.VISIBLE);
                            binding.videoview.setVisibility(View.GONE);
                            mediaRecorder.start();
                        } catch (final Exception ex) {
                            // Log.i("---","Exception in thread");
                        }
                    }
                });
                recording = true;
            }
        });
        initialize();

        binding.cvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);*/

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_GALLERY_REQUEST_CODE);
            }
        });

        binding.cvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llSelect.setVisibility(View.VISIBLE);
                binding.llSubmit.setVisibility(View.GONE);
            }
        });

        binding.cvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(filePath);
                try {
                    File vdo = new File(uri.getPath());
                    if (vdo.exists()) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), vdo);
                        MultipartBody.Part newfile = MultipartBody.Part.createFormData("profile_video", vdo.getName(), requestBody);
                        new ApiManager(ShotVideoActivity.this,ShotVideoActivity.this).sendVideo("1", newfile);
                    }
                } catch (Exception e) {
                    Log.e("errorVdoFRG", e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Log.e("picturewee", "m here resultCode => " + requestCode);
            try {
                if (data.getData() != null) {
                    Log.e("picturewee", "in try => " + data.getData());

                    Uri selectedImageUri = data.getData();

                    // MEDIA GALLERY
                    filePath = getPath(selectedImageUri);
                    binding.videoview.setVisibility(View.VISIBLE);
                    binding.videoview.setVideoPath(filePath);



                    MediaController mediaController = new MediaController(ShotVideoActivity.this);

                    binding.videoview.setMediaController(mediaController);

                    mediaController.setMediaPlayer(binding.videoview);

                    binding.videoview.start();

                    binding.llSelect.setVisibility(View.GONE);
                    binding.llSubmit.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                Log.e("picturewee", e.toString());
                Toast.makeText(this, "Please select another video", Toast.LENGTH_SHORT).show();
            }
        }
    }

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

    private static final int PICK_VIDEO_GALLERY_REQUEST_CODE = 611;

    boolean recording = false;

    void startTimerBroad() {

        broadPauseTimer.start();
    }

    CountDownTimer broadPauseTimer = null;

    void cancelTimerBroad() {

        if (broadPauseTimer != null)
            broadPauseTimer.cancel();
    }

    void initTimerBroad() {

        broadPauseTimer = new CountDownTimer(16000, 1000) { //25000
            public void onTick(long millisUntilFinished) {
                Log.e("tracingEvents", "InBroad Timer=>" + millisUntilFinished / 1000);

            }

            public void onFinish() {

                cancelTimerBroad();
                try {
                    recording = false;
                    mediaRecorder.stop(); // stop the recording
                    releaseMediaRecorder();

                    binding.llSelect.setVisibility(View.GONE);
                    binding.llSubmit.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
            }
        };
    }

    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;

    public void initialize() {
        cameraPreview = (LinearLayout) binding.CameraView;

        mPreview = new CameraPreview(getApplicationContext(), mCamera);
        cameraPreview.addView(mPreview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasCamera(getApplicationContext())) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
        }

        if (mCamera == null) {
            // if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(getApplicationContext(), "No front facing camera found.", Toast.LENGTH_LONG).show();
            }
            try {
                mCamera = Camera.open(findFrontFacingCamera());
                mCamera.setDisplayOrientation(90);
                mPreview.refreshCamera(mCamera);
            } catch (Exception e) {
            }
        }
    }

    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void chooseCamera() {
        // if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                try {
                    mCamera = Camera.open(cameraId);
                    mCamera.setDisplayOrientation(90);
                    // mPicture = getPictureCallback();
                    mPreview.refreshCamera(mCamera);
                } catch (Exception e) {
                }
            }
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }


    private boolean prepareMediaRecorder() {

        mediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setOrientationHint(270);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setMaxDuration(100000); //set maximum duration 10 sec.
        mediaRecorder.setMaxFileSize(10000000); //set maximum file size 50M

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode== Constant.VIDEO_STATUS_UPLOAD){
            new SessionManager(getApplicationContext()).setResUpload("3");
            finish();
        }
    }
}