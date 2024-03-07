package com.privatepe.host

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.privatepe.host.databinding.ActivityAllAboutMyProfileBinding
import com.privatepe.host.dialogs.CustomVideoViewDialog
import com.privatepe.host.model.ProfileDetailsResponse
import com.privatepe.host.retrofit.ApiManager
import com.privatepe.host.retrofit.ApiResponseInterface
import com.privatepe.host.utils.Constant
import com.privatepe.host.utils.NetworkCheck
import com.privatepe.host.utils.SessionManager

class AllAboutMyProfile : AppCompatActivity(),ApiResponseInterface {
    private lateinit var binding: ActivityAllAboutMyProfileBinding
    private var apiManager: ApiManager? = null
    private var networkCheck: NetworkCheck? = null
    private var sessionManager: SessionManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllAboutMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiManager =ApiManager(this,this)
        networkCheck= NetworkCheck()
        sessionManager = SessionManager(this)
        if(networkCheck!!.isNetworkAvailable(this)){
            apiManager!!.getProfileDetails()
        }else{
            Toast.makeText(this,"Network Unavailable",Toast.LENGTH_SHORT).show()
        }
        binding.tvAboutMe2.text = "Hii guys, how are you, i am ${sessionManager!!.name}"+" Lets friends."


    }

    override fun isError(errorCode: String?) {

    }
    private var Count :Int = 1

    override fun isSuccess(response: Any?, ServiceCode: Int) {
        if (ServiceCode == Constant.PROFILE_DETAILS) {
            val rsp = response as ProfileDetailsResponse
            if (rsp.success != null) {
                if (rsp.success.profile_images != null && rsp.success.profile_images.size > 0) {
                    Log.e("test12321",":"+rsp.success.profile_images.size)
                    for (item in rsp.success.profile_images) {
                        Log.e("test12321",":"+rsp.success.profile_images.size+"||"+item)
                        if (item.is_profile_image==1) {
                            showPicture(item.image_name,binding.imgProfilePic0)
                        }
                        else{
                            when (Count) {
                                1 -> {
                                    showPicture(item.image_name,binding.imgProfilePic1)
                                    Count++
                                }
                                2 -> {
                                    showPicture(item.image_name,binding.imgProfilePic2)
                                    Count++
                                }
                                3 -> {
                                    showPicture(item.image_name,binding.imgProfilePic3)
                                    Count++
                                }
                                4 -> {
                                    showPicture(item.image_name,binding.imgProfilePic4)
                                    Count++
                                }
                                5 -> {
                                    showPicture(item.image_name,binding.imgProfilePic5)
                                    Count++
                                }
                            }

                        }



                    }
                    if(rsp.success.profileVideo.size>0){
                        for(item in rsp.success.profileVideo){
                            if(item.type==1){
                                showPicture(item.videoThumbnail,binding.imgStatusVideo)
                                showVideo(binding.imgStatusVideo,item.videoUrl,item.videoThumbnail)
                            }
                            if(item.type==2){
                                showPicture(item.videoThumbnail,binding.imgAudition)
                                showVideo(binding.imgAudition,item.videoUrl,item.videoThumbnail)
                            }
                        }
                    }
                }

            }
        }
    }

    private fun showVideo(video: AppCompatImageView, videoUrl: String?, videoThumbnail: String) {
        video.setOnClickListener(){
            CustomVideoViewDialog(this,videoUrl,videoThumbnail)
        }
    }

    private fun showPicture(imageName: String?, imgProfilePic: AppCompatImageView) {
        try {
            Glide.with(this)
                .load(imageName)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.default_profile)
                .centerCrop()
                .into(imgProfilePic)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }
}