package com.privatepe.app.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.privatepe.app.adapter.MyFollowingAdapter
import com.privatepe.app.databinding.ActivityMyFollowersBinding
import com.privatepe.app.dialogs.MyProgressDialog
import com.privatepe.app.model.DataFollowers
import com.privatepe.app.model.FollowersModelClass
import com.privatepe.app.model.MyTopFansData
import com.privatepe.app.model.MyTopFansModel
import com.privatepe.app.retrofit.ApiManager
import com.privatepe.app.retrofit.ApiResponseInterface
import com.privatepe.app.utils.Constant
import com.privatepe.app.utils.PaginationScrollListenerLinear


class MyFollowersActivity : AppCompatActivity() , ApiResponseInterface{
    private lateinit var binding : ActivityMyFollowersBinding
    private lateinit var Screen : String
    private lateinit var apiManager : ApiManager
    private var FollowingAdapter : MyFollowingAdapter? =null
    private var layoutManager: LinearLayoutManager? = null
    private val FIRST_PAGE : Int =1
    private var progressDialog: MyProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar(window, true)
        binding = ActivityMyFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = MyProgressDialog(this)

        val intent = intent
        if(intent.hasExtra("Screen")){
            Screen = intent.getStringExtra("Screen").toString()
            //if screen is MYFOLLOWERS set it As Title
            //if screen is MYTOPFANS set it As Title
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recUsers.setLayoutManager(layoutManager)


        binding.tvScreenView.text = Screen
        apiManager = ApiManager(this, this)


        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        pagination()



    }

    override fun onResume() {
        super.onResume()

        if(Screen.equals("MY FOLLOWERS")){
            myFollowersData.clear()
            progressDialog!!.show()
            apiManager.getFollowers(2,FIRST_PAGE)
            FollowingAdapter = MyFollowingAdapter(this,myFollowersData,"MY_FOLLOWERS")
        }else{
            myTopFansData.clear()
            progressDialog!!.show()
            apiManager.getTopFanUserList(FIRST_PAGE)
            FollowingAdapter = MyFollowingAdapter(this,myTopFansData)


        }
        binding.recUsers.adapter=FollowingAdapter
    }
    private fun loadNextPage() {
        progressDialog!!.show()
        if (Screen.equals("MY FOLLOWERS")) {
            apiManager.getFollowers(2, CURRENT_PAGE)
        } else {
            apiManager.getTopFanUserList(CURRENT_PAGE)
        }
    }

    fun hideStatusBar(window: Window, darkText: Boolean) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        var flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && darkText) {
            flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.decorView.systemUiVisibility = flag or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    override fun isError(errorCode: String?) {

    }
    private var TOTAL_PAGES : Int =0
    private var CURRENT_PAGE : Int = FIRST_PAGE
    private var isLoadingit : Boolean= false
    private var isLastPageit : Boolean= false

    var myTopFansData: MutableList<MyTopFansData> = ArrayList()
    var myFollowersData: MutableList<DataFollowers> = ArrayList()
    override fun isSuccess(response: Any?, ServiceCode: Int) {
        if(ServiceCode == Constant.TOP_FAN_USER_LIST){
            val listOfTopFans = response as MyTopFansModel
            if(listOfTopFans.success){
                progressDialog?.dismiss()
                isLoadingit = false
                isLastPageit = false
                myTopFansData.addAll(listOfTopFans.result.data)
                FollowingAdapter?.notifyDataSetChanged()
                TOTAL_PAGES = listOfTopFans.result.last_page
                if(CURRENT_PAGE==TOTAL_PAGES){
                    isLastPageit = true
                }

            }

        }
        if(ServiceCode == Constant.FOLLOWER_USER_LIST){
            val listOfFollowers = response as FollowersModelClass
            if(listOfFollowers.success){
                progressDialog?.dismiss()
                isLoadingit = false
                isLastPageit = false
                listOfFollowers.result.data?.let { myFollowersData.addAll(it) }
                FollowingAdapter?.notifyDataSetChanged()
                TOTAL_PAGES = listOfFollowers.result.last_page ?: 0
                if(CURRENT_PAGE==TOTAL_PAGES) {
                    isLastPageit = true
                }

            }
        }
    }
    private fun pagination(){
        binding.recUsers.addOnScrollListener(object : PaginationScrollListenerLinear(layoutManager!!) {
            override fun loadMoreItems() {
                isLoadingit=true
                CURRENT_PAGE++
                loadNextPage()
            }

            override fun isLastPage(): Boolean {
                return isLastPageit
            }

            override fun isLoading(): Boolean {
                return isLoadingit
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }
        })
    }
}
