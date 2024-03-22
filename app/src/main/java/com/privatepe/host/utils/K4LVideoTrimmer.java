package com.privatepe.host.utils;

import static com.tencent.liteav.base.ThreadUtils.runOnUiThread;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import life.knowledge4.videotrimmer.R.id;
import life.knowledge4.videotrimmer.R.layout;
import life.knowledge4.videotrimmer.R.string;
import life.knowledge4.videotrimmer.interfaces.OnProgressVideoListener;
import life.knowledge4.videotrimmer.interfaces.OnRangeSeekBarListener;
import life.knowledge4.videotrimmer.utils.BackgroundExecutor;
import life.knowledge4.videotrimmer.utils.UiThreadExecutor;
import life.knowledge4.videotrimmer.view.ProgressBarView;
import life.knowledge4.videotrimmer.view.RangeSeekBarView;
import life.knowledge4.videotrimmer.view.Thumb;
import life.knowledge4.videotrimmer.view.TimeLineView;

public class K4LVideoTrimmer extends FrameLayout implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, OnRangeSeekBarListener, OnProgressVideoListener {
    private static final String TAG = K4LVideoTrimmer.class.getSimpleName();
    private static final int MIN_TIME_FRAME = 1000;
    private SeekBar mHolderTopView;
    private RangeSeekBarView mRangeSeekBarView;
    private RelativeLayout mLinearVideo;
    private VideoView mVideoView;
    private ImageView mPlayView;
    private TextView mTextSize;
    private TextView mTextTimeFrame;
    private TextView mTextTime;
    private TimeLineView mTimeLineView;
    private Uri mSrc;
    private String mFinalPath;
    private int mMaxDuration;
    private List<OnProgressVideoListener> mListeners;
    private com.privatepe.host.utils.OnTrimVideoListener mOnTrimVideoListener;

    private int mDuration;
    private int mTimeVideo;
    private int mStartPosition;
    private int mEndPosition;
    private long mOriginSizeFile;
    private boolean mResetSeekBar;
    @NonNull
    private final MessageHandler mMessageHandler;
    private static final int SHOW_PROGRESS = 2;
    private GestureDetector mGestureDetector;
    @NonNull
    private final GestureDetector.SimpleOnGestureListener mGestureListener;
    @NonNull
    private final View.OnTouchListener mTouchListener;

    public K4LVideoTrimmer(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public K4LVideoTrimmer(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDuration = 0;
        this.mTimeVideo = 0;
        this.mStartPosition = 0;
        this.mEndPosition = 0;
        this.mResetSeekBar = true;
        this.mMessageHandler = new MessageHandler(this);
        this.mGestureListener = new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (K4LVideoTrimmer.this.mVideoView.isPlaying()) {
                    K4LVideoTrimmer.this.mPlayView.setVisibility(View.VISIBLE);
                    K4LVideoTrimmer.this.mMessageHandler.removeMessages(2);
                    K4LVideoTrimmer.this.mVideoView.pause();
                } else {
                    K4LVideoTrimmer.this.mPlayView.setVisibility(View.GONE);
                    if (K4LVideoTrimmer.this.mResetSeekBar) {
                        K4LVideoTrimmer.this.mResetSeekBar = false;
                        K4LVideoTrimmer.this.mVideoView.seekTo(K4LVideoTrimmer.this.mStartPosition);
                    }

                    K4LVideoTrimmer.this.mMessageHandler.sendEmptyMessage(2);
                    K4LVideoTrimmer.this.mVideoView.start();
                }

                return true;
            }
        };
        this.mTouchListener = new View.OnTouchListener() {
            public boolean onTouch(View v, @NonNull MotionEvent event) {
                K4LVideoTrimmer.this.mGestureDetector.onTouchEvent(event);
                return true;
            }
        };
        this.init(context);
    }

    private void init(Context context) {
        Log.e("chafafa","=> Init");

        LayoutInflater.from(context).inflate(layout.view_time_line, this, true);
        this.mHolderTopView = (SeekBar)this.findViewById(id.handlerTop);
        ProgressBarView progressVideoView = (ProgressBarView)this.findViewById(id.timeVideoView);
        this.mRangeSeekBarView = (RangeSeekBarView)this.findViewById(id.timeLineBar);
        this.mLinearVideo = (RelativeLayout)this.findViewById(id.layout_surface_view);
        this.mVideoView = (VideoView)this.findViewById(id.video_loader);
        this.mPlayView = (ImageView)this.findViewById(id.icon_video_play);
        this.mTextSize = (TextView)this.findViewById(id.textSize);
        this.mTextTimeFrame = (TextView)this.findViewById(id.textTimeSelection);
        this.mTextTime = (TextView)this.findViewById(id.textTime);
        this.mTimeLineView = (TimeLineView)this.findViewById(id.timeLineView);
        View viewButtonCancel = this.findViewById(id.btCancel);
        View viewButtonSave = this.findViewById(id.btSave);
        if (viewButtonCancel != null) {
            viewButtonCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    K4LVideoTrimmer.this.mOnTrimVideoListener.cancelAction();
                    Log.e("chafafa","=> Cancel");

                }
            });
        }

        if (viewButtonSave != null) {
            viewButtonSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    try{
                        Log.e("chafafa","=> Try");
                        if (K4LVideoTrimmer.this.mStartPosition <= 0 && K4LVideoTrimmer.this.mEndPosition >= K4LVideoTrimmer.this.mDuration) {
                            Log.e("checkDuration",K4LVideoTrimmer.this.mDuration+"");
                            K4LVideoTrimmer.this.mOnTrimVideoListener.getResult(K4LVideoTrimmer.this.mSrc);
                        } else {
                            K4LVideoTrimmer.this.mPlayView.setVisibility(View.VISIBLE);
                            K4LVideoTrimmer.this.mVideoView.pause();
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(K4LVideoTrimmer.this.getContext(), K4LVideoTrimmer.this.mSrc);
                            long METADATA_KEY_DURATION = Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
                            File file = new File(K4LVideoTrimmer.this.mSrc.getPath());
                            if (K4LVideoTrimmer.this.mTimeVideo < 1000) {
                                if (METADATA_KEY_DURATION - (long)K4LVideoTrimmer.this.mEndPosition > (long)(1000 - K4LVideoTrimmer.this.mTimeVideo)) {
                                    K4LVideoTrimmer.this.mEndPosition = K4LVideoTrimmer.this.mEndPosition + (1000 - K4LVideoTrimmer.this.mTimeVideo);
                                } else if (K4LVideoTrimmer.this.mStartPosition > 1000 - K4LVideoTrimmer.this.mTimeVideo) {
                                    K4LVideoTrimmer.this.mStartPosition = K4LVideoTrimmer.this.mStartPosition - (1000 - K4LVideoTrimmer.this.mTimeVideo);
                                }
                            }

                            K4LVideoTrimmer.this.startTrimVideo(context,file, K4LVideoTrimmer.this.mFinalPath, K4LVideoTrimmer.this.mStartPosition, K4LVideoTrimmer.this.mEndPosition, K4LVideoTrimmer.this.mOnTrimVideoListener);
                        }
                    }catch (Exception e){
                        Log.e("chafafa","=> Catch "+e);

                    }


                }
            });
        }

        this.mListeners = new ArrayList();
        this.mListeners.add(this);
        this.mListeners.add(progressVideoView);
        this.mHolderTopView.setMax(1000);
        this.mHolderTopView.setSecondaryProgress(0);
        this.mRangeSeekBarView.addOnRangeSeekBarListener(this);
        this.mRangeSeekBarView.addOnRangeSeekBarListener(progressVideoView);
        int marge = ((Thumb)this.mRangeSeekBarView.getThumbs().get(0)).getWidthBitmap();
        int widthSeek = this.mHolderTopView.getThumb().getMinimumWidth() / 2;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)this.mHolderTopView.getLayoutParams();
        lp.setMargins(marge - widthSeek, 0, marge - widthSeek, 0);
        this.mHolderTopView.setLayoutParams(lp);
        lp = (RelativeLayout.LayoutParams)this.mTimeLineView.getLayoutParams();
        lp.setMargins(marge, 0, marge, 0);
        this.mTimeLineView.setLayoutParams(lp);
        lp = (RelativeLayout.LayoutParams)progressVideoView.getLayoutParams();
        lp.setMargins(marge, 0, marge, 0);
        progressVideoView.setLayoutParams(lp);
        this.mHolderTopView.setOnSeekBarChangeListener(this);
        this.mVideoView.setOnPreparedListener(this);
        this.mVideoView.setOnCompletionListener(this);
        this.mVideoView.setOnErrorListener(this);
        this.mTimeLineView.setEnabled(false);
        this.mGestureDetector = new GestureDetector(this.getContext(), this.mGestureListener);
        this.mVideoView.setOnTouchListener(this.mTouchListener);
        this.setDefaultDestinationPath();
       /* saveTrimmedVideo();*/

    }

    private void saveTrimmedVideo() {
        try{
            Log.e("dfdfdfdf","=> Try");
            if (K4LVideoTrimmer.this.mStartPosition <= 0 && K4LVideoTrimmer.this.mEndPosition >= K4LVideoTrimmer.this.mDuration) {
                K4LVideoTrimmer.this.mOnTrimVideoListener.getResult(K4LVideoTrimmer.this.mSrc);
            } else {
                K4LVideoTrimmer.this.mPlayView.setVisibility(View.VISIBLE);
                K4LVideoTrimmer.this.mVideoView.pause();
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(K4LVideoTrimmer.this.getContext(), K4LVideoTrimmer.this.mSrc);
                long METADATA_KEY_DURATION = Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
                File file = new File(K4LVideoTrimmer.this.mSrc.getPath());
                if (K4LVideoTrimmer.this.mTimeVideo < 1000) {
                    if (METADATA_KEY_DURATION - (long)K4LVideoTrimmer.this.mEndPosition > (long)(1000 - K4LVideoTrimmer.this.mTimeVideo)) {
                        K4LVideoTrimmer.this.mEndPosition = K4LVideoTrimmer.this.mEndPosition + (1000 - K4LVideoTrimmer.this.mTimeVideo);
                    } else if (K4LVideoTrimmer.this.mStartPosition > 1000 - K4LVideoTrimmer.this.mTimeVideo) {
                        K4LVideoTrimmer.this.mStartPosition = K4LVideoTrimmer.this.mStartPosition - (1000 - K4LVideoTrimmer.this.mTimeVideo);
                    }
                }

                K4LVideoTrimmer.this.startTrimVideo(getContext(),file, K4LVideoTrimmer.this.mFinalPath, K4LVideoTrimmer.this.mStartPosition, K4LVideoTrimmer.this.mEndPosition, K4LVideoTrimmer.this.mOnTrimVideoListener);
            }
        }catch (Exception e){
            Log.e("chafafa","=> Catch "+e);

        }
    }

    public void setVideoURI(Uri videoURI) {
        this.mSrc = videoURI;
        this.getSizeFile();
        this.mVideoView.setVideoURI(this.mSrc);
        this.mVideoView.requestFocus();
        this.mTimeLineView.setVideo(this.mSrc);
        /*saveTrimmedVideo();*/
    }

    public void setDestinationPath(String finalPath) {
        this.mFinalPath = finalPath;
        Log.d(TAG, "Setting custom path " + this.mFinalPath);
    }

    private void setDefaultDestinationPath() {
        File folder = Environment.getExternalStorageDirectory();
        this.mFinalPath = folder.getPath() + File.separator;
        Log.d(TAG, "Setting default path " + this.mFinalPath);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int duration = (int)((long)(this.mDuration * progress) / 1000L);
        if (fromUser) {
            if (duration < this.mStartPosition) {
                this.setProgressBarPosition(this.mStartPosition);
                duration = this.mStartPosition;
            } else if (duration > this.mEndPosition) {
                this.setProgressBarPosition(this.mEndPosition);
                duration = this.mEndPosition;
            }

            this.setTimeVideo(duration);
        }

    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        this.mMessageHandler.removeMessages(2);
        this.mVideoView.pause();
        this.mPlayView.setVisibility(View.VISIBLE);
        this.updateProgress(false);
    }

    public void onStopTrackingTouch(@NonNull SeekBar seekBar) {
        this.mMessageHandler.removeMessages(2);
        this.mVideoView.pause();
        this.mPlayView.setVisibility(View.VISIBLE);
        int duration = (int)((long)(this.mDuration * seekBar.getProgress()) / 1000L);
        this.mVideoView.seekTo(duration);
        this.setTimeVideo(duration);
        this.updateProgress(false);
    }

    public void onPrepared(@NonNull MediaPlayer mp) {
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        float videoProportion = (float)videoWidth / (float)videoHeight;
        int screenWidth = this.mLinearVideo.getWidth();
        int screenHeight = this.mLinearVideo.getHeight();
        float screenProportion = (float)screenWidth / (float)screenHeight;
        ViewGroup.LayoutParams lp = this.mVideoView.getLayoutParams();
        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int)((float)screenWidth / videoProportion);
        } else {
            lp.width = (int)(videoProportion * (float)screenHeight);
            lp.height = screenHeight;
        }

        this.mVideoView.setLayoutParams(lp);
        this.mPlayView.setVisibility(View.VISIBLE);
        this.mDuration = this.mVideoView.getDuration();
        this.setSeekBarPosition();
        this.setTimeFrames();
        this.setTimeVideo(0);
    }

    private void setSeekBarPosition() {
        if (this.mDuration >= this.mMaxDuration) {
            this.mStartPosition = /*this.mDuration / 2 - this.mMaxDuration / 2*/0;
            this.mEndPosition = this.mMaxDuration;
            this.mRangeSeekBarView.setThumbValue(0, (float)(this.mStartPosition * 100 / this.mDuration));
            this.mRangeSeekBarView.setThumbValue(1, (float)(this.mEndPosition * 100 / this.mDuration));
        } else {
            this.mStartPosition = 0;
            this.mEndPosition = this.mDuration;
        }

        this.setProgressBarPosition(this.mStartPosition);
        this.mVideoView.seekTo(this.mStartPosition);
        this.mTimeVideo = this.mDuration;
        this.mRangeSeekBarView.initMaxWidth();
    }



    private void startTrimVideo(@NonNull Context context,@NonNull final File file, @NonNull final String dst, final int startVideo, final int endVideo, @NonNull final com.privatepe.host.utils.OnTrimVideoListener callback) {

        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0L, "") {
            public void execute() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // UI update code here
                            try {
                                TrimVideoUtils.startTrim(context,file, dst, (long)startVideo, (long)endVideo, callback);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                }
        });
    }

    private void setTimeFrames() {
        String seconds = this.getContext().getString(string.short_seconds);
        this.mTextTimeFrame.setText(String.format("%s %s - %s %s", this.stringForTime(this.mStartPosition), seconds, this.stringForTime(this.mEndPosition), seconds));
    }

    private void setTimeVideo(int position) {
        String seconds = this.getContext().getString(string.short_seconds);
        this.mTextTime.setText(String.format("%s %s", this.stringForTime(position), seconds));
        saveTrimmedVideo();
    }

    public void onCreate(RangeSeekBarView rangeSeekBarView, int index, float value) {

    }

    public void onSeek(RangeSeekBarView rangeSeekBarView, int index, float value) {
        switch (index) {
            case 0:
                this.mStartPosition = (int)((float)this.mDuration * value / 100.0F);
                this.mVideoView.seekTo(this.mStartPosition);
                break;
            case 1:
                this.mEndPosition = (int)((float)this.mDuration * value / 100.0F);
        }

        this.setProgressBarPosition(this.mStartPosition);
        this.setTimeFrames();
        this.mTimeVideo = this.mEndPosition - this.mStartPosition;
    }

    public void onSeekStart(RangeSeekBarView rangeSeekBarView, int index, float value) {
    }

    public void onSeekStop(RangeSeekBarView rangeSeekBarView, int index, float value) {
        this.mMessageHandler.removeMessages(2);
        this.mVideoView.pause();
        this.mPlayView.setVisibility(View.VISIBLE);
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60 % 60;
        int hours = totalSeconds / 3600;
        Formatter mFormatter = new Formatter();
        return hours > 0 ? mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString() : mFormatter.format("%02d:%02d", minutes, seconds).toString();
    }

    private void getSizeFile() {
        if (this.mOriginSizeFile == 0L) {
            File file = new File(this.mSrc.getPath());
            this.mOriginSizeFile = file.length();
            long fileSizeInKB = this.mOriginSizeFile / 1024L;
            if (fileSizeInKB > 1000L) {
                long fileSizeInMB = fileSizeInKB / 1024L;
                this.mTextSize.setText(String.format("%s %s", fileSizeInMB, this.getContext().getString(string.megabyte)));
            } else {
                this.mTextSize.setText(String.format("%s %s", fileSizeInKB, this.getContext().getString(string.kilobyte)));
            }
        }

    }

    public void setOnTrimVideoListener(OnTrimVideoListener onTrimVideoListener) {
        this.mOnTrimVideoListener = onTrimVideoListener;
    }

    public void setMaxDuration(int maxDuration) {
        this.mMaxDuration = maxDuration * 1000;
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        this.mVideoView.seekTo(0);

    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    public void onTrimmedVideoResult(Uri trimmedVideoUri) {
        // Handle the trimmed video URI here
        saveTrimmedVideo();
    }

    public void getResult(Uri uri) {
        // Handle the result here
        onTrimmedVideoResult(uri);
    }

    private void updateProgress(boolean all) {
        if (this.mDuration != 0) {
            int position = this.mVideoView.getCurrentPosition();
            if (all) {
                Iterator var3 = this.mListeners.iterator();

                while(var3.hasNext()) {
                    OnProgressVideoListener item = (OnProgressVideoListener)var3.next();
                    item.updateProgress(position, this.mDuration, (float)(position * 100 / this.mDuration));
                }
            } else {
                ((OnProgressVideoListener)this.mListeners.get(1)).updateProgress(position, this.mDuration, (float)(position * 100 / this.mDuration));
            }

        }
    }

    public void updateProgress(int time, int max, float scale) {
        if (this.mVideoView != null) {
            if (time >= this.mEndPosition) {
                this.mMessageHandler.removeMessages(2);
                this.mVideoView.pause();
                this.mPlayView.setVisibility(View.VISIBLE);
                this.mResetSeekBar = true;
            } else {
                if (this.mHolderTopView != null) {
                    this.setProgressBarPosition(time);
                }

                this.setTimeVideo(time);
            }
        }
    }

    private void setProgressBarPosition(int position) {
        if (this.mDuration > 0) {
            long pos = 1000L * (long)position / (long)this.mDuration;
            this.mHolderTopView.setProgress((int)pos);
        }

    }

    public void destroy() {
        BackgroundExecutor.cancelAll("", true);
        UiThreadExecutor.cancelAll("");
    }

    private static class MessageHandler extends Handler {
        @NonNull
        private final WeakReference<K4LVideoTrimmer> mView;

        MessageHandler(K4LVideoTrimmer view) {
            this.mView = new WeakReference(view);
        }

        public void handleMessage(Message msg) {
            K4LVideoTrimmer view = (K4LVideoTrimmer)this.mView.get();
            if (view != null && view.mVideoView != null) {
                view.updateProgress(true);
                if (view.mVideoView.isPlaying()) {
                    this.sendEmptyMessageDelayed(0, 10L);
                }

            }
        }
    }
}
