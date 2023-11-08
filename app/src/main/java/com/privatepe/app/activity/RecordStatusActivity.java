package com.privatepe.app.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.core.content.ContextCompat;


import com.google.firebase.database.DatabaseReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.privatepe.app.R;
import com.privatepe.app.dialogs.MyProgressDialog;
import com.privatepe.app.fudetector.capture.VideoCaptureFromCamera;
import com.privatepe.app.fudetector.capture.VideoCaptureFromCamera2;
import com.privatepe.app.fudetector.faceunity.FURenderer;
import com.privatepe.app.fudetector.process.VideoFilterByProcess;
import com.privatepe.app.fudetector.process.VideoFilterByProcess2;
import com.privatepe.app.model.Video.VideoResponce;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;


import com.privatepe.app.utils.SessionManager;


import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoCustomVideoCaptureHandler;
import im.zego.zegoexpress.callback.IZegoCustomVideoProcessHandler;
import im.zego.zegoexpress.callback.IZegoDataRecordEventHandler;
import im.zego.zegoexpress.constants.ZegoDataRecordState;
import im.zego.zegoexpress.constants.ZegoDataRecordType;
import im.zego.zegoexpress.constants.ZegoPublishChannel;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.constants.ZegoVideoConfigPreset;
import im.zego.zegoexpress.constants.ZegoViewMode;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoCustomVideoCaptureConfig;
import im.zego.zegoexpress.entity.ZegoCustomVideoProcessConfig;
import im.zego.zegoexpress.entity.ZegoDataRecordConfig;
import im.zego.zegoexpress.entity.ZegoDataRecordProgress;
import im.zego.zegoexpress.entity.ZegoVideoConfig;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class RecordStatusActivity extends BaseActivity implements FURenderer.OnTrackingStatusChangedListener, ApiResponseInterface {

    ZegoExpressEngine expressEngine;
    TextureView mPreview;
    protected FURenderer mFURenderer;
    private ZegoVideoBufferType videoBufferType;
    IZegoCustomVideoCaptureHandler videoCaptureFromCamera;
    IZegoCustomVideoProcessHandler videoFilterByProcess;

    String TAG = "RecordStatusActivity";

    DatabaseReference chatRef;

    private SessionManager sessionManager;

    private TextView onOffText;
    private TextView txtProgress;
    String streamID;
    String filePath, fileName;
    ImageView ivProgressBtn;
    CircularProgressBar circularProgressBar;
    VideoView videoView;
    RelativeLayout rlVideoPreview, rlPreview, rl_close, rlDelete, rlSend, rlRetry;
    LinearLayout llMessage;
    private boolean useExpressCustomCapture = false;
    CountDownTimer broadPauseTimer = null;
    private Dialog unVarifiedDialog;
    int status;
    int trackFaceCount;
    private MyProgressDialog progressDialog;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), false);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_record_status);

        initView();

        getPermission();

    }

    private void initView() {
        mPreview = findViewById(R.id.preview);
        onOffText = findViewById(R.id.onoff);

        sessionManager = new SessionManager(this);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.progressBar);
        videoView = (VideoView) findViewById(R.id.videoView);
        rlPreview = (RelativeLayout) findViewById(R.id.rlPreview);
        rlVideoPreview = (RelativeLayout) findViewById(R.id.rlVideoPreview);
        rl_close = (RelativeLayout) findViewById(R.id.rl_close);
        rlDelete = (RelativeLayout) findViewById(R.id.rlDelete);
        rlSend = (RelativeLayout) findViewById(R.id.rlSend);
        rlRetry = (RelativeLayout) findViewById(R.id.rlRetry);
        llMessage = (LinearLayout) findViewById(R.id.llMessage);
        ivProgressBtn = (ImageView) findViewById(R.id.ivProgressBtn);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);

        expressEngine = ZegoExpressEngine.getEngine();
        initFU();
        setZegoEventHandler();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initSDK() {

        expressEngine = ZegoExpressEngine.getEngine();
        if (useExpressCustomCapture && videoBufferType == ZegoVideoBufferType.SURFACE_TEXTURE) {
            videoCaptureFromCamera = new VideoCaptureFromCamera2((mFURenderer));
        } else if (useExpressCustomCapture && videoBufferType == ZegoVideoBufferType.RAW_DATA) {
            videoCaptureFromCamera = new VideoCaptureFromCamera(mFURenderer);
        } else if (!useExpressCustomCapture && videoBufferType == ZegoVideoBufferType.SURFACE_TEXTURE) {
            videoFilterByProcess = new VideoFilterByProcess(mFURenderer);
        } else if (!useExpressCustomCapture && videoBufferType == ZegoVideoBufferType.GL_TEXTURE_2D) {
            videoFilterByProcess = new VideoFilterByProcess2(mFURenderer);
        }
        if (useExpressCustomCapture) {
            ZegoCustomVideoCaptureConfig zegoCustomVideoCaptureConfig = new ZegoCustomVideoCaptureConfig();
            zegoCustomVideoCaptureConfig.bufferType = videoBufferType;
            expressEngine.enableCustomVideoCapture(true, zegoCustomVideoCaptureConfig);
            expressEngine.setCustomVideoCaptureHandler(videoCaptureFromCamera);
        } else {
            ZegoCustomVideoProcessConfig zegoCustomVideoProcessConfig = new ZegoCustomVideoProcessConfig();
            zegoCustomVideoProcessConfig.bufferType = videoBufferType;
            expressEngine.enableCustomVideoProcessing(true, zegoCustomVideoProcessConfig);
            expressEngine.setCustomVideoProcessHandler(videoFilterByProcess);
        }

        ZegoVideoConfig videoConfig = new ZegoVideoConfig(ZegoVideoConfigPreset.PRESET_720P);
        expressEngine.setVideoConfig(videoConfig);

        ZegoCanvas preCanvas = new ZegoCanvas(mPreview);
        preCanvas.viewMode = ZegoViewMode.ASPECT_FILL;
        ZegoExpressEngine.getEngine().startPreview(preCanvas);

    }

    private void initFU() {

        rlVideoPreview.setVisibility(View.GONE);

        mFURenderer = new FURenderer
                .Builder(RecordStatusActivity.this)
                .maxFaces(1)
                .inputTextureType(0)
                .setOnTrackingStatusChangedListener(RecordStatusActivity.this)
                .build();

        videoBufferType = ZegoVideoBufferType.GL_TEXTURE_2D;
        initSDK();
        ivProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ZegoVideoConfig videoConfig = new ZegoVideoConfig(ZegoVideoConfigPreset.PRESET_720P);
                expressEngine.setVideoConfig(videoConfig);

                ZegoDataRecordConfig config = new ZegoDataRecordConfig();

                File myDirectory = new File(Environment.getExternalStorageDirectory(), "/KLive");
                if (!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }
                filePath = myDirectory + "/video" + System.currentTimeMillis() + ".mp4";
                config.filePath = filePath;
                if (filePath != null) {
                    String[] fileNme = filePath.split("KLive/");
                    fileName = fileNme[1];
                    Log.e(TAG, "==fileName===>" + fileName);
                }
                config.recordType = ZegoDataRecordType.DEFAULT;
                initTimerBroad();
                ivProgressBtn.setVisibility(View.GONE);
                // Start recording with main channel.
                expressEngine.startRecordingCapturedData(config, ZegoPublishChannel.MAIN);
                circularProgressBar.setColor(ContextCompat.getColor(RecordStatusActivity.this, R.color.colorAccent));
                circularProgressBar.setBackgroundColor(ContextCompat.getColor(RecordStatusActivity.this, R.color.female_background));
                circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen._6sdp));
                circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen._8sdp));
                int animationDuration = 16000; // 1500ms = 1,5s
                circularProgressBar.setProgressWithAnimation(100, animationDuration);
                startTimerBroad();

            }
        });

    }

//    Configuration configureWith;


    private void getPermission() {

        String[] permissions;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_VIDEO};
        } else {

            permissions = new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        }

        Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        try {
                            if (report.areAllPermissionsGranted()) {

                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {

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


    private void compressvideo(Uri uri) {

        // progressDialog = new MyProgressDialog(this);
        // progressDialog.show();


        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing your Video");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();

        Log.e("LightCompressor111", "enter");

        List<Uri> uris = new ArrayList<>();
        uris.add(uri);
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "/KLive");
        /*configureWith = new Configuration();
        configureWith.setMinBitrateCheckEnabled(false);
        configureWith.setQuality(VideoQuality.HIGH);

        //AppSpecificStorageConfiguration configuration=new AppSpecificStorageConfiguration("compressed_video", Environment.getExternalStorageDirectory()+ "/KLive");

     *//*   File desFile = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES), "status_compressed_video.mp4");
        if (desFile.exists()) {
            desFile.delete();
        }*//*

        SharedStorageConfiguration sharedStorageConfiguration = new SharedStorageConfiguration("status_compressed_video", SaveLocation.movies);


        try {

            VideoCompressor.start(getApplicationContext(), uris, false, sharedStorageConfiguration, null, configureWith, new CompressionListener() {
                @Override
                public void onStart(int i) {
                    Log.e("LightCompressor111", "start");
                }

                @Override
                public void onSuccess(int i, long l, @Nullable String s) {

                    Log.e("LightCompressor111", "onSuccess: path " + s + " size " + l);
                    Log.e("RecordAc", "onSuccess: compressor ");

                    sendVideo(s);


                }

                @Override
                public void onFailure(int i, @NonNull String s) {
                    Log.e("LightCompressor111", "onFailure " + s);
                }

                @Override
                public void onProgress(int i, float v) {
                    Log.e("LightCompressor111", "progress " + i);
                }

                @Override
                public void onCancelled(int i) {

                }
            });

        } catch (Exception e) {
            Log.e("LightCompressor111", "run: " + e.getMessage());
        }
*/

    }


    private void sendVideo(String path) {

        //  Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/KLive/" + fileName);

        Uri uri = Uri.parse(path);

        File file = new File(path);

        Log.e("vdoPath==1===>", uri.toString());
        try {
            File vdo = new File(uri.getPath());
            if (vdo.exists()) {
                Log.e("vdoPath==2===>", vdo.getPath());
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), vdo);
                MultipartBody.Part newfile = MultipartBody.Part.createFormData("profile_video", vdo.getName(), requestBody);
                new ApiManager(RecordStatusActivity.this, RecordStatusActivity.this).sendVideo(newfile);
            }
            //  progressDialog.hide();

            mProgress.dismiss();

        } catch (Exception e) {
            Log.e("errorVdoFRG", e.getMessage());
        }

    }

    private void showDialog() {
        unVarifiedDialog = new Dialog(this);
        unVarifiedDialog.setContentView(R.layout.recording_fail_dialog);
        unVarifiedDialog.setCancelable(false);
        unVarifiedDialog.setCanceledOnTouchOutside(true);
        unVarifiedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvTitle = unVarifiedDialog.findViewById(R.id.tvTitle);
        TextView tvMessage = unVarifiedDialog.findViewById(R.id.tvMessage);
        Button btnContinue = unVarifiedDialog.findViewById(R.id.btnContinue);
        tvTitle.setText("Failed to record video");
        tvMessage.setText("Please show your face for at least 5 seconds when you record the video or you will be rejected.");
        btnContinue.setText("Continue");
        btnContinue.setOnClickListener(view -> unVarifiedDialog.dismiss());
        unVarifiedDialog.show();
    }

    void startTimerBroad() {

        broadPauseTimer.start();
    }

    private void setZegoEventHandler() {
        // set recording function callback
        expressEngine.setDataRecordEventHandler(new IZegoDataRecordEventHandler() {

            public void onCapturedDataRecordStateUpdate(ZegoDataRecordState state, int errorCode, ZegoDataRecordConfig config, ZegoPublishChannel channel) {
                // You can handle the logic of the state change during the recording process according to the error code or the recording state, such as UI prompts on the interface, etc.
                Log.e(TAG, "==onCapturedDataRecordStateUpdate==" + errorCode + "===" + state.name());
            }

            public void onCapturedDataRecordProgressUpdate(ZegoDataRecordProgress progress, ZegoDataRecordConfig config, ZegoPublishChannel channel) {
                // You can handle the logic of the progress change of the recording process according to the recording progress here, such as UI prompts on the interface, etc.
                Log.e(TAG, "==onCapturedDataRecordProgressUpdate==" + progress.currentFileSize + "====" + progress.duration + "");

            }

        });
    }

    void cancelTimerBroad() {

        if (broadPauseTimer != null)
            broadPauseTimer.cancel();
    }

    void initTimerBroad() {

        broadPauseTimer = new CountDownTimer(16000, 1000) { //25000
            public void onTick(long millisUntilFinished) {
                Log.e("tracingEvents", "InBroad Timer=>" + millisUntilFinished / 1000);
                Log.e("status", "status=>" + status + "");
                if (status == 1) {
                    trackFaceCount++;
                    Log.e("trackFacecount===", trackFaceCount + "");
                }
            }

            public void onFinish() {

                circularProgressBar.setVisibility(View.GONE);
                expressEngine.stopRecordingCapturedData(ZegoPublishChannel.MAIN);
                cancelTimerBroad();

                llMessage.setVisibility(View.GONE);
                rlPreview.setVisibility(View.GONE);

                Log.e("initTimerBroad==>", "onfinish==" + "--" + trackFaceCount + "");
                if (trackFaceCount < 5) {
                    trackFaceCount = 0;
                    llMessage.setVisibility(View.VISIBLE);
                    rlPreview.setVisibility(View.VISIBLE);
                    circularProgressBar.setVisibility(View.VISIBLE);
                    circularProgressBar.setColor(ContextCompat.getColor(RecordStatusActivity.this, R.color.female_background));
                    circularProgressBar.setBackgroundColor(ContextCompat.getColor(RecordStatusActivity.this, R.color.female_background));
                    circularProgressBar.setProgressWithAnimation(0);
                    ivProgressBtn.setVisibility(View.VISIBLE);
                    initFU();
                    showDialog();

                } else {
                    rlVideoPreview.setVisibility(View.VISIBLE);
                    // MediaController mediaController = new MediaController(RecordStatusActivity.this);
               /* MediaController mediaController= new MediaController(RecordStatusActivity.this){
                    @Override
                    public void hide() {

                    }
                };*/
                    // mediaController.setAnchorView(videoView);

                    //specify the location of media file
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/KLive/" + fileName);

                    //videoView.setMediaController(mediaController);
                    videoView.setVideoURI(uri);
                    videoView.requestFocus();
                    videoView.start();
                    rlSend.setVisibility(View.VISIBLE);
                    rlRetry.setVisibility(View.VISIBLE);
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // do something when the end of the video is reached

                        }
                    });

                    Log.e(TAG, "====>" + uri.toString() + "====>" + uri.getPath());

                    rlDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            trackFaceCount = 0;
                            llMessage.setVisibility(View.VISIBLE);
                            rlPreview.setVisibility(View.VISIBLE);
                            rlSend.setVisibility(View.GONE);
                            rlRetry.setVisibility(View.GONE);
                            rlDelete.setVisibility(View.GONE);
                            circularProgressBar.setVisibility(View.VISIBLE);
                            circularProgressBar.setColor(ContextCompat.getColor(RecordStatusActivity.this, R.color.female_background));
                            circularProgressBar.setBackgroundColor(ContextCompat.getColor(RecordStatusActivity.this, R.color.female_background));
                            circularProgressBar.setProgressWithAnimation(0);
                            ivProgressBtn.setVisibility(View.VISIBLE);
                            initFU();
                        }
                    });

                    rlSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            Uri uri1 = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/KLive/" + fileName));
                            Log.e("LightCompressor111", "sendVideo: uri " + uri + " uri1 " + uri1);

                            File file = new File(uri1.getPath());
                            Log.e("LightCompressor111", "sendVideo: size " + file.length());

                            sendVideo(uri1.getPath());

                            //  compressvideo(uri1);

                        }
                    });

                    rlRetry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rlDelete.setVisibility(View.VISIBLE);
                            rlRetry.setVisibility(View.GONE);
                        }
                    });

                }
            }
        };
    }


    @Override
    public void finish() {

        Log.e(TAG, "finish: ");
        super.finish();
    }

    @Override
    protected void onDestroy() {

        new SessionManager(this).setWorkSession(false);

        expressEngine.stopRecordingCapturedData(ZegoPublishChannel.MAIN);

        Log.e(TAG, "onDestroy: super onDestroy");

        cancelTimerBroad();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e(TAG, "onStop: ");

        try {
            expressEngine.stopPreview();
            expressEngine.setEventHandler(null);
        } catch (Exception e) {
        }


    }

    public void closeFun(View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                trackFaceCount = 0;
            }
        }, 200);

    }

    @Override
    public void onTrackingStatusChanged(int statuss) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status = statuss;
                Log.e("status", "status=>" + status + "");

            }

        });
    }

    @Override
    public void isError(String errorCode) {
        Log.e("RecordAc", "isError: errorCode " + errorCode);
        if (errorCode.equals("OnFailure_timeout_CloseActivity")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 200);
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        Log.e("RecordAc", "isSuccess: ");

        if (ServiceCode == Constant.VIDEO_STATUS_UPLOAD) {
            VideoResponce videoResponce = (VideoResponce) response;
            if (videoResponce.getSuccess()) {
                Log.e("RecordAc", "isSuccess: true ");
                Toast.makeText(RecordStatusActivity.this, videoResponce.getResult().toString(), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);
            } else {
                Log.e("RecordAc", "isSuccess: false ");
            }

        }
    }


}
