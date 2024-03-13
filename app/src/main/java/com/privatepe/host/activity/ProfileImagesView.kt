package com.privatepe.host.activity

import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.privatepe.host.databinding.ActivityProfileImagesViewBinding
import com.privatepe.host.model.HostUserPicNew
import com.privatepe.host.recycler.ProfileAdapter
import com.privatepe.host.response.metend.UserListResponseNew.FemaleImage


class ProfileImagesView : AppCompatActivity() {
    private var profileAdapter: ProfileAdapter? = null
    private var femaleImageList: ArrayList<FemaleImage>? = null
    private val handler = Handler()
    private val swipeDelay: Long = 10000 // Set your desired delay in milliseconds
    private var positionOnDisplay: Int  = 0
    private var profilePicture: ArrayList<HostUserPicNew>?=null
    private lateinit var binding : ActivityProfileImagesViewBinding
    private  var player: ExoPlayer?=null
    private var videoThumbnail: String?=null
    private var videoUrl: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileImagesViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if(intent.hasExtra("whatTheViewFor")){
            if(intent.getStringExtra("whatTheViewFor").equals("VideoView")){
                if(intent.hasExtra("videoThumbnail__")){
                    videoThumbnail = intent.getStringExtra("videoThumbnail__")

                }
                if(intent.hasExtra("videoUrl__")){
                    videoUrl = intent.getStringExtra("videoUrl__")
                }
                binding.profileVideos.visibility = View.VISIBLE
                binding.indicatorDot.visibility =View.GONE
                binding.Viewpager.visibility=View.GONE
                initializePlayer(videoUrl,videoThumbnail)

            }

        }else {
            if(intent.hasExtra("positionOnDisplay")){
                positionOnDisplay = intent.getIntExtra("positionOnDisplay",0)
            }
            if (intent.hasExtra("femaleImageList")) {
                femaleImageList = intent.getParcelableArrayListExtra<FemaleImage>("femaleImageList")
            }
            if(femaleImageList!=null){
                profileAdapter = ProfileAdapter(this, femaleImageList, "ExtendedProfileImages")
                binding.Viewpager.adapter = profileAdapter
            }

            if(intent.hasExtra("picturesList")){
                profilePicture = intent.getParcelableArrayListExtra<HostUserPicNew>("picturesList")
            }
            if (profilePicture != null) {
                profileAdapter = ProfileAdapter(this, profilePicture, "EditProfileActivity",true)
                binding.Viewpager.adapter = profileAdapter
            }
            TabLayoutMediator(binding.indicatorDot, binding.Viewpager) { _, _ ->
            }.attach()


            startAutomaticSwipe()
            binding.Viewpager.setCurrentItem(positionOnDisplay,true)
        }






    }
    private fun initializePlayer(videoUrl: String?, thumbnail: String?) {
        if (thumbnail != null || videoUrl != null) {
            if (thumbnail != null) {
                try {
                    Glide.with(this).load(thumbnail)
                        .into(binding.exoplayerViewImageView)
                } catch (e: Exception) {
                }
            }
            if (videoUrl != null) {
                player = ExoPlayer.Builder(this).build()
                binding.exoplayerView.player = player
                val mediaItem = MediaItem.fromUri(videoUrl)
                player!!.setMediaItem(mediaItem)
                player!!.prepare()
                player!!.repeatMode = Player.REPEAT_MODE_ONE
                player!!.play()
                player!!.volume = 0f
                player!!.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
                            //do something
                            binding.exoplayerViewImageView.visibility = View.GONE
                        }
                    }
                })
            }
        }
    }
    private fun startAutomaticSwipe() {
        handler.postDelayed(swipeRunnable, swipeDelay)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Remove the runnable to prevent memory leaks
        handler.removeCallbacks(swipeRunnable)
        player?.release()
    }
    private val swipeRunnable = object : Runnable {
        override fun run() {
            val currentItem = binding.Viewpager.currentItem ?: 0
            binding.Viewpager.setCurrentItem((currentItem + 1) % (profileAdapter?.itemCount ?: 0), true)
            handler.postDelayed(this, SWIPE_DELAY)
        }
    }
    companion object {
        const val SWIPE_DELAY: Long = 3000 // Delay in milliseconds
    }
}