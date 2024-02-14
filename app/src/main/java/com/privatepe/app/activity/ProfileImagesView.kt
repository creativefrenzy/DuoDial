package com.privatepe.app.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.privatepe.app.R
import com.privatepe.app.model.UserListResponseNew.FemaleImage
import com.privatepe.app.recycler.ProfileAdapter

class ProfileImagesView : AppCompatActivity() {
    private var vpDetailPager: ViewPager2? = null
    private var profileAdapter: ProfileAdapter? = null
    private var femaleImageList: ArrayList<FemaleImage>? = null
    private var vpIndicatorDot : TabLayout?=null
    private val handler = Handler()
    private val swipeDelay: Long = 3000 // Set your desired delay in milliseconds
    private var positionOnDisplay: Int  = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_images_view)
        vpDetailPager = findViewById(R.id.Viewpager)
        vpIndicatorDot = findViewById(R.id.indicator_dot)

        val intent = intent
        if(intent.hasExtra("positionOnDisplay")){
            positionOnDisplay = intent.getIntExtra("positionOnDisplay",0)
        }
        if (intent.hasExtra("femaleImageList")) {
            femaleImageList = intent.getParcelableArrayListExtra<FemaleImage>("femaleImageList")
        }
        profileAdapter = ProfileAdapter(this, femaleImageList, "ExtendedProfileImages")
        vpDetailPager?.adapter = profileAdapter

        TabLayoutMediator(vpIndicatorDot!!, vpDetailPager!!) { tab, position ->
            // You can set tab text or icons here if needed
        }.attach()


        startAutomaticSwipe()
        vpDetailPager?.setCurrentItem(positionOnDisplay,true)

    }
    private fun startAutomaticSwipe() {
        handler.postDelayed(swipeRunnable, swipeDelay)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Remove the runnable to prevent memory leaks
        handler.removeCallbacks(swipeRunnable)
    }
    private val swipeRunnable = object : Runnable {
        override fun run() {
            val currentItem = vpDetailPager?.currentItem ?: 0
            vpDetailPager?.setCurrentItem((currentItem + 1) % (profileAdapter?.itemCount ?: 0), true)
            handler.postDelayed(this, SWIPE_DELAY)
        }
    }
    companion object {
        const val SWIPE_DELAY: Long = 3000 // Delay in milliseconds
    }
}