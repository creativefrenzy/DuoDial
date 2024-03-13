package com.privatepe.host.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.privatepe.host.databinding.DiaologVideoPlayerBinding;

public class CustomVideoViewDialog extends Dialog {
    private String VideoUrl_;
    private String Thumbnail;
    private Context Context_;
    private DiaologVideoPlayerBinding binding;

    public CustomVideoViewDialog(@NonNull Context context,String VideoUrl,String videoThumbnail) {
        super(context);
        this.VideoUrl_ = VideoUrl;
        this.Context_ = context;
        this.Thumbnail = videoThumbnail;
        init();
    }
    public void init(){
        binding = DiaologVideoPlayerBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());
        Window window = getWindow();
        if (window != null) {
            //window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
           // window.setGravity(Gravity.CENTER);

        }
        initializePlayer(VideoUrl_,Thumbnail);
        //show();

    }

    private ExoPlayer player;


    private void initializePlayer(String videoUrl, String thumbnail) {

        if (thumbnail != null || videoUrl != null) {
            if (thumbnail != null) {
                try {
                    Glide.with(Context_).load(thumbnail).into(binding.exoplayerViewImageView).clearOnDetach();
                }catch (Exception e) {

                }
                show();

            }
            if (videoUrl != null) {
                player = new ExoPlayer.Builder(Context_).build();
                binding.exoplayerView.setPlayer(player);
                MediaItem mediaItem = MediaItem.fromUri(videoUrl);
                player.setMediaItem(mediaItem);
                player.prepare();
                player.setRepeatMode(Player.REPEAT_MODE_ONE);
                player.play();
                player.setVolume(0);
                player.addListener(new Player.Listener() {
                    @Override
                    public void onPlaybackStateChanged(int playbackState) {
                        Player.Listener.super.onPlaybackStateChanged(playbackState);
                        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
                            //do something
                            binding.exoplayerViewImageView.setVisibility(View.GONE);

                        }
                    }
                });
                show();
            }
        }
    }
}
