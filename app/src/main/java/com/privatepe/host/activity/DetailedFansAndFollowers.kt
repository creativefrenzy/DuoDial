package com.privatepe.host.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.bumptech.glide.Glide
import com.privatepe.host.R
import com.privatepe.host.databinding.ActivityDetailedForFollowerAndFansBinding

import com.privatepe.host.response.metend.AddRemoveFavResponse
import com.privatepe.host.retrofit.ApiManager
import com.privatepe.host.retrofit.ApiResponseInterface
import com.privatepe.host.utils.Constant.FOLLOWING_HOST

class DetailedFansAndFollowers : AppCompatActivity() ,ApiResponseInterface{
    private lateinit var binding :ActivityDetailedForFollowerAndFansBinding
    private lateinit var apiManager : ApiManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailedForFollowerAndFansBinding.inflate(layoutInflater)
        setContentView(binding.root)


        apiManager = ApiManager(this, this)
        val profile_img: String? = intent.getStringExtra("profile_img")
        val profile_name: String? = intent.getStringExtra("profile_name")
        val profile_lvl: String? = intent.getStringExtra("profile_lvl")
        val profile_is_0nline: String? = intent.getStringExtra("profile_is_0nline")
        val favorite_by_you_count: String? = intent.getStringExtra("favorite_by_you_count")
        val profile_id: String? = intent.getStringExtra("profile_id")
        val total_beans: String? = intent.getStringExtra("total_beans")
        val gender: String? = intent.getStringExtra("gender")

        //Log.d("trtgh", " || "+profile_is_0nline.toString()+" || "+favorite_by_you_count.toString()+"||"+profile_id.toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnFollowing.setOnClickListener{
            apiManager.followingHost(profile_id)
        }
        if (gender.equals("male")) binding.imgGender.setImageResource(R.drawable.male_sign)
        else binding.imgGender.setImageResource(R.drawable.female_sign)




        binding.profileName.text = profile_name

        Glide.with(this)
            .load(profile_img)
            .error(R.drawable.default_profile) // Apply RequestOptions
            .into(binding.prifileImage)
        binding.tvLevel.text = "Lvl "+profile_lvl
        if(favorite_by_you_count.equals("0")){
            binding.btnFollowing.text="Follow"

        }else{
            binding.btnFollowing.text="Following"

        }

        if(profile_is_0nline.equals("0")){
            binding.tvOffline.text="Offline"
            binding.imgProfileStatus.setImageResource(R.color.red)
        }else{
            binding.tvOffline.text="Online"
            binding.imgProfileStatus.setImageResource(R.color.green)
        }

        binding.id.text= profile_id
        binding.myEarnings.text= total_beans
    }

    override fun isError(errorCode: String?) {
        TODO("Not yet implemented")
    }

    override fun isSuccess(response: Any?, ServiceCode: Int) {
        if(ServiceCode==FOLLOWING_HOST){
            val followResult = response as AddRemoveFavResponse
            if(followResult.isSuccess){
                binding.btnFollowing.text="Following"
            }
        }
    }
}