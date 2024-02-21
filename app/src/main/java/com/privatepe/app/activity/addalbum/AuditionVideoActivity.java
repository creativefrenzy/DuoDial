package com.privatepe.app.activity.addalbum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;

import com.privatepe.app.R;
import com.privatepe.app.activity.RecordStatusActivity;
import com.privatepe.app.databinding.ActivityAuditionVideoBinding;
import com.privatepe.app.main.Home;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.CameraPreview;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AuditionVideoActivity extends AppCompatActivity implements ApiResponseInterface {

    ActivityAuditionVideoBinding binding;
    String filePath, fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audition_video);
        binding.ivProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              /*  File myDirectory = new File(Environment.getExternalStorageDirectory(), "/KLive");
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
                binding.ivProgressBtn.setVisibility(View.GONE);
                // Start recording with main channel.
                binding.progressBar.setColor(ContextCompat.getColor(AuditionVideoActivity.this, R.color.colorAccent));
                binding.progressBar.setBackgroundColor(ContextCompat.getColor(AuditionVideoActivity.this, R.color.female_background));
                binding.progressBar.setProgressBarWidth(getResources().getDimension(R.dimen._6sdp));
                binding.progressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen._8sdp));
                int animationDuration = 16000; // 1500ms = 1,5s
                binding.progressBar.setProgressWithAnimation(100, animationDuration);
                startTimerBroad();


                if (!prepareMediaRecorder()) {
                    Toast.makeText(getApplicationContext(), "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                }
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
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

        binding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/KLive/" + fileName);

                Uri uri = Uri.parse(filePath);


                Log.e("vdoPath==1===>", uri.toString());
                try {
                    File vdo = new File(uri.getPath());
                    if (vdo.exists()) {
                        Log.e("vdoPath==2===>", vdo.getPath());
                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), vdo);
                        MultipartBody.Part newfile = MultipartBody.Part.createFormData("profile_video", vdo.getName(), requestBody);
                        new ApiManager(AuditionVideoActivity.this, AuditionVideoActivity.this).sendVideo("2", newfile);
                    }
                    //  progressDialog.hide();

                } catch (Exception e) {
                    Log.e("errorVdoFRG", e.getMessage());
                }

            }
        });

        binding.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.videoview.setVisibility(View.GONE);
                binding.rlSecond.setVisibility(View.GONE);
                binding.rlMain.setVisibility(View.VISIBLE);
                binding.ivProgressBtn.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBar.setProgress(0f);


            }
        });

        binding.videoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying) {
                    //binding.videoview.setVideoPath(filePath);

                    MediaController mediaController = new MediaController(AuditionVideoActivity.this);

                    binding.videoview.setMediaController(mediaController);

                    mediaController.setMediaPlayer(binding.videoview);
                    binding.videoview.setMediaController(null);
                    isPlaying = true;
                    binding.videoview.start();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                isPlaying = false;
                            } catch (Exception e) {
                            }
                        }
                    }, 15000);

                }
            }
        });
    }

    boolean isPlaying = false;
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
                binding.progressBar.setVisibility(View.GONE);
                cancelTimerBroad();
                try {
                    recording = false;
                    mediaRecorder.stop(); // stop the recording
                    releaseMediaRecorder();

                    binding.videoview.setVideoPath(filePath);
                    binding.videoview.seekTo(1);

                    binding.rlSecond.setVisibility(View.VISIBLE);
                    binding.rlMain.setVisibility(View.GONE);
                    binding.videoview.setVisibility(View.VISIBLE);
                    binding.videoview.setVideoPath(filePath);
                    binding.videoview.seekTo(1);
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
      //  Log.e("sjdfjasd"," w "+            mCamera.getParameters().getPreviewSize().width+" h "+mCamera.getParameters().getPreviewSize().height);
     /*   ViewGroup.LayoutParams vvParams = binding.CameraView.getLayoutParams();
        vvParams.height = 600;
        vvParams.width =270;
        binding.CameraView.setLayoutParams(vvParams);*/
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
            Log.e("mediaLog", "error => " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.e("mediaLog", "error => " + e.getMessage());

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
        if (ServiceCode == Constant.VIDEO_STATUS_UPLOAD) {
            new SessionManager(getApplicationContext()).setResUpload("3");
            startActivity(new Intent(AuditionVideoActivity.this, Home.class));
            finish();
        }

    }
}