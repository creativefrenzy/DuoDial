package com.privatepe.host.activity.addalbum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.google.gson.Gson;
import com.privatepe.host.R;
import com.privatepe.host.databinding.ActivityShotVideoBinding;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.CameraPreview;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ShotVideoActivity extends AppCompatActivity implements ApiResponseInterface {
    String filePath, fileName;
    ActivityShotVideoBinding binding;
    Boolean isRecording = false;

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
                if (isRecording) {
                    return;
                }
                isRecording = true;
                filePath = "/storage/emulated/0/Android/data/com.privatepe.host" + "/video" + System.currentTimeMillis() + ".mp4";

               /* if (filePath != null) {
                    String[] fileNme = filePath.split("KLive/");
                    fileName = fileNme[1];
                    //Log.e(TAG, "==fileName===>" + fileName);
                }*/
                initTimerBroad();
                // Start recording with main channel.
                binding.progressBar.setProgress(0f);
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBar.setColor(ContextCompat.getColor(ShotVideoActivity.this, R.color.colorAccent));
                binding.progressBar.setBackgroundColor(ContextCompat.getColor(ShotVideoActivity.this, R.color.female_background));
                binding.progressBar.setProgressBarWidth(getResources().getDimension(R.dimen._6sdp));
                binding.progressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen._8sdp));
                int animationDuration = 15000; // 1500ms = 1,5s
                binding.progressBar.setProgressWithAnimation(100, animationDuration);
                startTimerBroad();


               /* if (!prepareMediaRecorder()) {
                    Toast.makeText(getApplicationContext(), "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                }*/

                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                          //  cameraPreview.setVisibility(View.VISIBLE);
                            binding.videoview.setVisibility(View.GONE);
                            mediaRecorder.start();

                            Log.e("tracingEvents", "cam visible");

                        } catch (final Exception ex) {
                            Log.e("tracingEvents", "cam visible" + ex.getMessage());

                            // Log.i("---","Exception in thread");
                        }
                    }
                });
                recording = true;
            }
        });
        //initialize();
        binding.uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(galleryVid==false) {
               /* Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);*/
    binding.uploadImage.setEnabled(false);
    intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    intentGallery.setType("video/*");
    startActivityForResult(Intent.createChooser(intentGallery, "Select Video"), PICK_VIDEO_GALLERY_REQUEST_CODE);
    setDelay(500);

}

            }
        });

        binding.cvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llSelect.setVisibility(View.VISIBLE);
                binding.llSubmit.setVisibility(View.GONE);
              //  cameraPreview.setVisibility(View.VISIBLE);
                binding.videoview.setVisibility(View.GONE);
                galleryVid = false;

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
                        new ApiManager(ShotVideoActivity.this, ShotVideoActivity.this).sendVideo("1", newfile);
                    }
                } catch (Exception e) {
                    Log.e("errorVdoFRG", e.getMessage());
                }
            }
        });
    }
    Intent intentGallery=null;

    private Handler dhandler= new Handler();
private void setDelay(int delay){
dhandler.removeCallbacksAndMessages(null);
    dhandler.postDelayed(new Runnable() {
        @Override
        public void run() {
            binding.uploadImage.setEnabled(true);

        }
    }, delay);
}

    private void getPermission(String[] permissions) {

        Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        try {
                            if (report.areAllPermissionsGranted()) {
                                //  setupCam();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {

                            }

                            if (report.getGrantedPermissionResponses().get(0).getPermissionName().equals("android.permission.ACCESS_FINE_LOCATION")) {

                                //enableLocationSettings();
                                // enableLocationSettings();

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

    private void setupCam() {
        filePath = "/storage/emulated/0/Android/data/com.privatepe.host" + "/video" + System.currentTimeMillis() + ".mp4";
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

                    Log.e("tracingEvents", "cam visible");

                } catch (final Exception ex) {
                    Log.e("tracingEvents", "cam visible" + ex.getMessage());

                    // Log.i("---","Exception in thread");
                }
            }
        });
        recording = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Log.e("picturewee", "m here resultCode => " + requestCode);
            try {
                if (data.getData() != null) {

                    Uri selectedImageUri = data.getData();
                    filePath = getPath(selectedImageUri);
                    MediaPlayer mp = MediaPlayer.create(this, Uri.parse(filePath));
                    int duration = mp.getDuration();
                    mp.release();
                    Log.e("picturewee", "timeeduraa try => " + duration);

                    if ((duration / 1000) >= 15 || (duration / 1000) <= 10 ) {
                        Toast.makeText(this, "Video should be between 10-15 seconds.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("picturewee", "in try => " + data.getData());
                        galleryVid = true;

                        // MEDIA GALLERY
                        binding.cvVideoview.setVisibility(View.VISIBLE);
                        // cameraPreview.setVisibility(View.GONE);
                        binding.videoview.setVisibility(View.VISIBLE);
                        binding.videoview.setVideoPath(filePath);


                        MediaController mediaController = new MediaController(ShotVideoActivity.this);

                        binding.videoview.setMediaController(mediaController);

                        mediaController.setMediaPlayer(binding.videoview);

                        binding.videoview.start();

                        binding.llSelect.setVisibility(View.GONE);
                        binding.llSubmit.setVisibility(View.VISIBLE);
                    }
                }


            } catch (Exception e) {
                Log.e("picturewee", e.toString());
                Toast.makeText(this, "Please select another video", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean galleryVid = false;

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
        isRecording = false;

        if (broadPauseTimer != null)
            broadPauseTimer.cancel();
    }

    void initTimerBroad() {

        broadPauseTimer = new CountDownTimer(10000, 1000) { //25000
            public void onTick(long millisUntilFinished) {
                Log.e("tracingEvents", "InBroad Timer=>" + millisUntilFinished / 1000);

            }

            public void onFinish() {

                cancelTimerBroad();
                binding.progressBar.setVisibility(View.GONE);

                try {
                    recording = false;
                    mediaRecorder.stop(); // stop the recording
                    releaseMediaRecorder();

                    binding.llSelect.setVisibility(View.GONE);
                    binding.llSubmit.setVisibility(View.VISIBLE);
                    Log.e("tracingEvents", "submit visible");

                } catch (Exception e) {
                    Log.e("tracingEvents", "catch e" + e.getMessage());
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
                // setupCam();

            } catch (Exception e) {
            }
        }
        //   setupCam();

        String[] permissions;

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        }


        getPermission(permissions);

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
        Log.e("chadkasdfa", "Recording...");
        List<Camera.Size> rawSupportedSizes = mCamera.getParameters().getSupportedPreviewSizes();
        Log.e("sjdfjasd", " w " + mCamera.getParameters().getPreviewSize().width + " h " + mCamera.getParameters().getPreviewSize().height);
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setOrientationHint(270);
        Log.e("chekcaaa", "" + new Gson().toJson(rawSupportedSizes));
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setMaxDuration(100000); //set maximum duration 10 sec.
        mediaRecorder.setMaxFileSize(10000000); //set maximum file size 50M

        try {
            mediaRecorder.prepare();

        } catch (IllegalStateException e) {
            Log.e("tracingEvents", "cam visible c1" + e.getMessage());

            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.e("tracingEvents", "cam visible c2" + e.getMessage());

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
        if (errorCode.equals("already")) {
            new SessionManager(getApplicationContext()).setResUpload("3");
            startActivity(new Intent(ShotVideoActivity.this, AuditionVideoActivity.class));
            finish();
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.VIDEO_STATUS_UPLOAD) {
            new SessionManager(getApplicationContext()).setResUpload("3");
            startActivity(new Intent(ShotVideoActivity.this, AuditionVideoActivity.class));
            finish();
        }
    }
}