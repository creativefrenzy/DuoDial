package com.klive.app.fudetector.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewStub;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.klive.app.fudetector.faceunity.FURenderer;
import com.klive.app.fudetector.process.VideoFilterByProcess;
import com.klive.app.fudetector.process.VideoFilterByProcess2;
import com.klive.app.fudetector.util.ZegoUtil;
import com.klive.app.fudetector.view.BeautyControlView;
import com.klive.app.fudetector.view.CustomDialog;
import com.klive.app.R;
import com.klive.app.databinding.ActivityFuBaseBinding;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.utils.SessionManager;

import org.json.JSONObject;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoCustomVideoCaptureHandler;
import im.zego.zegoexpress.callback.IZegoCustomVideoProcessHandler;
import im.zego.zegoexpress.callback.IZegoEventHandler;
import im.zego.zegoexpress.constants.ZegoPublishChannel;
import im.zego.zegoexpress.constants.ZegoPublisherState;
import im.zego.zegoexpress.constants.ZegoRoomState;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.constants.ZegoViewMode;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoCustomVideoProcessConfig;
import im.zego.zegoexpress.entity.ZegoEngineProfile;
import im.zego.zegoexpress.entity.ZegoRoomConfig;
import im.zego.zegoexpress.entity.ZegoUser;


/**
 * 带美颜的推流界面
 */

public class FUBeautyActivity extends Activity implements FURenderer.OnTrackingStatusChangedListener {
    public final static String TAG = FUBeautyActivity.class.getSimpleName();

    private ActivityFuBaseBinding binding;

    private ViewStub mBottomViewStub;
    private BeautyControlView mBeautyControlView;

    // faceunity 美颜相关的封装类
    protected FURenderer mFURenderer;

    // 房间 ID

    // 主播流名
    private String anchorStreamID = ZegoUtil.getPublishStreamID();

    private FilterType chooseFilterType;
    private ZegoExpressEngine engine;
    private ZegoVideoBufferType videoBufferType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_fu_base);

        mBottomViewStub = (ViewStub) findViewById(R.id.fu_base_bottom);
        mBottomViewStub.setInflatedId(R.id.fu_base_bottom);


        // 创建 faceUnity 美颜实例
        mFURenderer = new FURenderer
                .Builder(this)
                .maxFaces(4)
                .inputTextureType(0)
                .setOnTrackingStatusChangedListener(this)
                .build();

        mBottomViewStub.setLayoutResource(R.layout.layout_fu_beauty);
        mBottomViewStub.inflate();

        mBeautyControlView = (BeautyControlView) findViewById(R.id.fu_beauty_control);

        //  mRoomID = getIntent().getStringExtra("roomID");
        chooseFilterType = (FilterType) getIntent().getSerializableExtra("FilterType");

        mBeautyControlView.setOnFUControlListener(mFURenderer);
        videoBufferType = ZegoVideoBufferType.getZegoVideoBufferType(getIntent().getIntExtra("videoBufferType", 0));


        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        if (bundle != null) {
            try {
                channelName = bundle.getString("roomID");
               /* token = bundle.getString("token");

                hostId = bundle.getString("hostId");
                hostCallRate = bundle.getString("callRate");

                String hostStatus = bundle.getString("hostStatus");

                if (hostStatus.equals("1")) {
                    ((TextView) findViewById(R.id.tv_lbbs)).setVisibility(View.VISIBLE);
                }

                hostName = bundle.getString("hostName");*/
            } catch (Exception e) {
            }
        }

        Log.e("vidoRenderLog", "roomID = " + channelName);
        Log.e("vidoRenderLog", "videoBufferType = " + videoBufferType);
        Log.e("vidoRenderLog", "chooseFilterType = " + chooseFilterType);
        // 初始化SDK
        initSDK();
    }

    private String hostId, hostCallRate, channelName, hostName;
    String token = "";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mBeautyControlView.isShown()) {
            mBeautyControlView.hideBottomLayoutAnimator();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBeautyControlView != null) {
            mBeautyControlView.onResume();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (videoCaptureFromCamera != null) {
            videoCaptureFromCamera.onStop(ZegoPublishChannel.MAIN);
        }
        if (videoFilterByProcess != null && videoBufferType == ZegoVideoBufferType.SURFACE_TEXTURE) {
            ((VideoFilterByProcess) videoFilterByProcess).stopAndDeAllocate();
        }
        if (videoFilterByProcess != null && videoBufferType == ZegoVideoBufferType.GL_TEXTURE_2D) {
            ((VideoFilterByProcess2) videoFilterByProcess).stopAndDeAllocate();

        }

        ZegoExpressEngine.getEngine().setCustomVideoCaptureHandler(null);
        // 停止预览
        ZegoExpressEngine.getEngine().stopPreview();

        // 在退出页面时停止推流
        ZegoExpressEngine.getEngine().stopPublishingStream();

        // 登出房间
        ZegoExpressEngine.getEngine().logoutRoom(channelName);
        ZegoExpressEngine.getEngine().stopPublishingStream();
        ZegoExpressEngine.getEngine().setEventHandler(null);
        ZegoExpressEngine.destroyEngine(null);

        /*new ApiManager(getApplicationContext()).stopBroadCastFunction(
                new SessionManager(getApplicationContext()).getUserBroadcdntoken(),
                new SessionManager(getApplicationContext()).getUserBroadid()
        );*/
        new ApiManager(getApplicationContext()).changeOnlineStatus(0);

    }

    // 前处理传递数据的类型枚举
    public enum FilterType {
        FilterType_SurfaceTexture
    }

    /**
     * 供其他Activity调用，进入本专题的方法
     *
     * @param activity
     */
    public static void actionStart(Activity activity, String roomID) {
        Intent intent = new Intent(activity, FUBeautyActivity.class);
        intent.putExtra("roomID", roomID);
        // intent.putExtra("FilterType", filterType);
        activity.startActivity(intent);
    }

    IZegoCustomVideoCaptureHandler videoCaptureFromCamera;
    IZegoCustomVideoProcessHandler videoFilterByProcess;

    /**
     * 初始化SDK逻辑
     * 初始化成功后登录房间并推流
     */
    private void initSDK() {

        Log.i("ZegoExpressEngine Version", ZegoExpressEngine.getVersion());

        // 设置外部滤镜---必须在初始化 ZEGO SDK 之前设置，否则不会回调   SyncTexture

        //  engine = ZegoExpressEngine.createEngine(GetAppIDConfig.appID, GetAppIDConfig.appSign, true, ZegoScenario.LIVE, this.getApplication(), null);

        ZegoEngineProfile profile = new ZegoEngineProfile();
        profile.appID = 36797904L;
        profile.scenario = ZegoScenario.GENERAL;
        profile.application = getApplication();
        engine = ZegoExpressEngine.createEngine(profile, null);

        videoFilterByProcess = new VideoFilterByProcess2(mFURenderer);

        ZegoCustomVideoProcessConfig zegoCustomVideoProcessConfig = new ZegoCustomVideoProcessConfig();
        zegoCustomVideoProcessConfig.bufferType = videoBufferType;
        engine.enableCustomVideoProcessing(true, zegoCustomVideoProcessConfig);
        ZegoExpressEngine.getEngine().setCustomVideoProcessHandler(videoFilterByProcess);


        // }
        // 初始化成功，登录房间并推流
        startPublish();

    }


    public void startPublish() {

        // 防止用户点击，弹出加载对话框
        CustomDialog.createDialog("登录房间中...", this).show();

       /* String randomSuffix = String.valueOf(new Date().getTime() % (new Date().getTime() / 1000));
        String userID = "user" + randomSuffix;
        String userName = "user" + randomSuffix;*/


        //ZegoExpressEngine.getEngine().loginRoom(mRoomID, new ZegoUser(userID, userName));

        String userId = new SessionManager(getApplicationContext()).getUserId();

        ZegoUser user = new ZegoUser(userId);

        ZegoRoomConfig roomConfig = new ZegoRoomConfig();
        // roomConfig.token = "04AAAAAGJNN+QAEHk2cTk0NG9vMGt1ZWJjMnYAsG0WHunPNbYK7ym7SYEFRcX8qaAuITVzLwJMyhDHQhl7KCqEXYshXo6ZoiGVVAniaUd2dEkZk5lc759ACoR1v0STh6hlBCgQRE4aNX7ck/0EJvnp35YA3aL0WGqMv+Ke3wHg6cYvSB9YqtK1oITQa6BE7YLfykrnftja1p+IyJMCzxgYVC5cfihml3rLJrWC3Asbp6F0xcstbiUHNsYkRZ1ZmuzVG3wdltsUbz1NNOeN";
        roomConfig.token = token;
        roomConfig.isUserStatusNotify = true;

/** log in to a room */
        engine.loginRoom(channelName, user, roomConfig);

        String streamId = channelName + "_stream";
        engine.startPublishingStream(streamId);
        ZegoExpressEngine.getEngine().setEventHandler(new IZegoEventHandler() {

            @Override
            public void onRoomStateUpdate(String roomID, ZegoRoomState state, int errorCode, JSONObject extendedData) {
                CustomDialog.createDialog(FUBeautyActivity.this).cancel();
                if (errorCode == 0) {
                    ZegoCanvas preCanvas = new ZegoCanvas(binding.preview);
                    preCanvas.viewMode = ZegoViewMode.ASPECT_FILL;
                    ZegoExpressEngine.getEngine().startPreview(preCanvas);
                    // 开始推流
                    ZegoExpressEngine.getEngine().startPublishingStream(roomID);
                } else {

                    Toast.makeText(FUBeautyActivity.this, "login room failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPublisherStateUpdate(String streamID, ZegoPublisherState state, int errorCode, JSONObject extendedData) {
                // 推流状态更新，errorCode 非0 则说明推流失败
                // 推流常见错误码请看文档: <a>https://doc.zego.im/CN/308.html</a>

                Log.e("streamPushLog", "streamId = " + streamID);
                Log.e("streamPushLog", "state = " + state.toString());
                Log.e("streamPushLog", "errorCode = " + errorCode);

                if (errorCode == 0) {

                    Toast.makeText(FUBeautyActivity.this, getString(R.string.tx_publish_success), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(FUBeautyActivity.this, getString(R.string.tx_publish_fail), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FURenderer信息回调~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void onTrackingStatusChanged(final int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // faceunity 是否检测到人脸的通知
//                binding.fuBaseIsTrackingText.setVisibility(status > 0 ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }
}
