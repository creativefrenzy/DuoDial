package com.privatepe.host.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.privatepe.host.Interface.openProfileDetails
import com.privatepe.host.R
import com.privatepe.host.adapter.MonthlyRankUsersListAdapter
import com.privatepe.host.databinding.ActivityInvitationRewardsBinding
import com.privatepe.host.dialogs.DialogInvitationReword
import com.privatepe.host.dialogs.DialogInviteMonthlyRank
import com.privatepe.host.model.InvitationRewardReponse
import com.privatepe.host.retrofit.ApiManager
import com.privatepe.host.retrofit.ApiResponseInterface
import com.privatepe.host.utils.Constant
import com.privatepe.host.utils.NetworkCheck
import com.privatepe.host.utils.PaginationScrollListenerLinear

class InvitationRewardsActivity : AppCompatActivity(), ApiResponseInterface , openProfileDetails {

    private lateinit var binding: ActivityInvitationRewardsBinding
    private val PAGE_START = 1
    private var isLastPageApi = false
    private var isLoadingApi = false
    private var TOTAL_PAGES = 0
    private var currentPage = PAGE_START
    var apiManager: ApiManager? = null
    private var dataItemList: MutableList<InvitationRewardReponse.Result.Data> = mutableListOf()
    private var adapter: MonthlyRankUsersListAdapter? = null
    private var networkCheck: NetworkCheck? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar(window)
        binding = ActivityInvitationRewardsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.inviteFrndsBtn.setOnClickListener {
            DialogInviteMonthlyRank(this).show()
            binding.inviteFrndsBtn.isEnabled = false
            val handler = Handler()
            handler.postDelayed({
                binding.inviteFrndsBtn.isEnabled = true
            }, 1000)}
        apiManager = ApiManager(this, this)
        isLoadingApi = true
        networkCheck = NetworkCheck()
        if(networkCheck!!.isNetworkAvailable(this)){
            apiManager?.getInviteRewardsData(currentPage)
            //noData()
        }else{
            noData()

        }
        binding.infoBtn.setOnClickListener {

                DialogInvitationReword(this).show()
                binding.infoBtn.isEnabled = false
                val handler = Handler()
                handler.postDelayed({
                    binding.infoBtn.isEnabled = true
                }, 1000)

        }

    }

    private fun noData() {
        val layoutParams: ViewGroup.LayoutParams = binding.monthlyRankRl.layoutParams
        layoutParams.height = resources.getDimensionPixelSize(R.dimen._250sdp)
        binding.monthlyRankRl.layoutParams = layoutParams
        binding.monthlyRankNoDataImg.visibility = View.VISIBLE
        binding.monthlyRankNoDataTxt.visibility = View.VISIBLE
    }


    private fun hideStatusBar(window: Window?) {

        window!!.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    override fun isError(errorCode: String?) {
        noData()
    }

    override fun isSuccess(response: Any?, ServiceCode: Int) {
        if (ServiceCode == Constant.INVITATION_REWARD_LIST) {
            val rsp = response as InvitationRewardReponse
            if (rsp.success && rsp.result.data.isNotEmpty()) {


                if (currentPage == 1) {
                    if (rsp.result.cards > 0) {
                        binding.cardLay.visibility = View.VISIBLE
                        //binding.cardAmt.text = Utils.formatValue(rsp.result.cards)
                    }
                    if (rsp.result.myInvitationRewardAmount > 0) {
                        binding.diamondsBg.visibility = View.INVISIBLE
                        binding.inviteFrndsTxt.visibility = View.INVISIBLE

                        binding.myInvitationRewardsTxt.visibility = View.VISIBLE
                        binding.myRewardsAmt.visibility = View.VISIBLE
                        binding.myRewardsAmt.text = rsp.result.myInvitationRewardAmount.toString()

                        // Replace with your desired drawable resource ID
                        val drawableResourceId = R.drawable.beans

                        // Replace these values with your desired size
                        val desiredWidth = resources.getDimensionPixelSize(R.dimen._22sdp)
                        val desiredHeight = resources.getDimensionPixelSize(R.dimen._25sdp)

                        // Load the drawable from the resource
                        val originalDrawable = ContextCompat.getDrawable(this, drawableResourceId)

                        // Check if the drawable is not null
                        if (originalDrawable != null) {
                            // Create a new BitmapDrawable with the desired size
                            val bitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, Bitmap.Config.ARGB_8888)
                            val canvas = Canvas(bitmap)
                            originalDrawable.setBounds(0, 0, desiredWidth, desiredHeight)
                            originalDrawable.draw(canvas)

                            val resizedDrawable = BitmapDrawable(resources, bitmap)

                            // Set the drawable on the TextView
                            binding.myRewardsAmt.setCompoundDrawablesWithIntrinsicBounds(null, null, resizedDrawable, null)
                        }
                    }
                    dataItemList = rsp.result.data as MutableList<InvitationRewardReponse.Result.Data>
                    val linearLayoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.monthlyRv.layoutManager = linearLayoutManager
                    if (dataItemList.size>0){
                        adapter = MonthlyRankUsersListAdapter(this, dataItemList,this)
                        binding.monthlyRv.adapter = adapter
                    }else{
                        noData()
                    }



                    binding.monthlyRv.addOnScrollListener(object :
                        PaginationScrollListenerLinear(linearLayoutManager) {
                        override fun loadMoreItems() {
                            if (currentPage == TOTAL_PAGES) {
                            } else {
                                isLoadingApi = true
                                currentPage += 1
                                Constant.CURRENT_PAGE_NO = currentPage
                                apiManager?.getInviteRewardsData(currentPage)
                            }
                        }

                        override fun getTotalPageCount(): Int {
                            return TOTAL_PAGES
                        }

                        override fun isLastPage(): Boolean {
                            return isLastPageApi
                        }

                        override fun isLoading(): Boolean {
                            return isLoadingApi
                        }
                    })

//                    binding.infoBtn.setOnClickListener {
//                        if(rsp.result.commissionlist.isNotEmpty()){
//                            DialogInvitationReword( rsp.result.commissionlist,this).show()
//                            binding.infoBtn.isEnabled = false
//                            val handler = Handler()
//                            handler.postDelayed({
//                                binding.infoBtn.isEnabled = true
//                            }, 1000)
//                        }else{
//                            Toast.makeText(this,"Currently no information is available", Toast.LENGTH_SHORT).show()
//                        }
//                    }

                } else {
                    dataItemList.addAll(rsp.result.data)
                    adapter?.notifyDataSetChanged()
                }
                isLoadingApi = false
                TOTAL_PAGES = rsp.result.lastPage

                if (currentPage == TOTAL_PAGES) {
                    isLastPageApi = true
                }
            }
        }
    }

    override fun openProfileDetails(profileId: String?, id: String?) {
        openProfileDetailsOnViewProfile(profileId,id)
    }
    private fun openProfileDetailsOnViewProfile(profileId: String?, id: String?) {
        val intent = Intent(this, ViewProfileMet::class.java)
        intent.putExtra("profile__Id", profileId)
        intent.putExtra("__Id", id)
        intent.putExtra("Viewer", "Host")
        startActivity(intent)
    }

}