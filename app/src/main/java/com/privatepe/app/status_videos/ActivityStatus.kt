package com.privatepe.app.status_videos

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.upstream.DefaultAllocator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.privatepe.app.R
//import com.privatepe.app.ZegoExpress.zim.ZimManager
import com.privatepe.app.activity.ViewProfile
import com.privatepe.app.databinding.ActivityStatusBinding
import com.privatepe.app.dialogs.ReportDialog
import com.privatepe.app.model.UserListResponse.ProfileVideo
import com.privatepe.app.model.UserListResponseNew.ResultDataNewProfile
import com.privatepe.app.model.UserListResponseNew.UserListResponseNewData
import com.privatepe.app.response.DataFromProfileId.DataFromProfileIdResponse
import com.privatepe.app.retrofit.ApiManager
import com.privatepe.app.retrofit.ApiResponseInterface
import com.privatepe.app.sqlite.StatusDBHandler
import com.privatepe.app.status_videos.model.VideoLinkModel
import com.privatepe.app.utils.BaseActivity
import com.privatepe.app.utils.Constant
import com.privatepe.app.utils.NetworkCheck
import com.privatepe.app.utils.SessionManager
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class ActivityStatus : BaseActivity(), StatusProgressView.StoriesListener,
    View.OnClickListener, ApiResponseInterface {
    var resources: ArrayList<String>? = ArrayList()
    var thumbnailList: ArrayList<String>? = ArrayList()

    private var counter = 0
    var sharedPreferences: SharedPreferences? = null
    private val toast: LinearLayout? = null
    private var networkCheck: NetworkCheck? = null
    var UserID: String? = null
    var UserName: String? = null
    var ProfilePic: String? = null
    var Location: String? = null
    var Level: String? = null
    var mAge: String? = null
    var callRate = 0
    var userData = ArrayList<ResultDataNewProfile>()

    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private val loadControlStartPlayBufferMs = 1000
    private val loadControlMinBufferMs = 1500
    private val loadControlAfterRebufferMs = 1500
    private val loadControlDefaultMaxBufferMs = 15000

    private val onTouchListener = OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN ->                  /* pressTime = System.currentTimeMillis();
                    statusProgressView.pause();*/return@OnTouchListener false
            MotionEvent.ACTION_UP -> {}
        }
        false
    }

    private var apiManager: ApiManager? = null
    private var userId = 0
    private var hostId = 0
    private val convId = ""
  //  private var zimManager: ZimManager? = null
    private val hostIdFemale: String? = null
    private var HostProfileId: String? = null
    private var statusDBHandler: StatusDBHandler? = null
    private var localListFromDB: ArrayList<VideoLinkModel>? = null
    private var isVideoPresentInLocal = true
    private var localVideoLinkList: ArrayList<String>? = null

    private var player: ExoPlayer? = null
    var arrayList: ArrayList<ProfileVideo>? = null
    private lateinit var binding: ActivityStatusBinding
    private var clickedUrl: String? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        hideStatusBar(window,false)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_status)
        networkCheck = NetworkCheck()
        apiManager = ApiManager(this, this)
      //  zimManager = ZimManager.sharedInstance()
        sharedPreferences = getSharedPreferences("VideoApp", MODE_PRIVATE)
        statusDBHandler = StatusDBHandler(this)

        if (intent != null) {
            thumbnailList = intent.getStringArrayListExtra("thumbnailList")
           // Log.e("which activity==>", intent.getStringExtra("inWhichActivity")!!)
            if (intent.getStringExtra("inWhichActivity")
                    .equals("ProfileVideoAdapter", ignoreCase = true)
            ) {
                UserID = intent.getStringExtra("id")
                val listData = intent.getStringExtra("allListData")
                val yourArray = Gson().fromJson<ArrayList<ProfileVideo>>(
                    listData.toString(),
                    object : TypeToken<List<ProfileVideo?>?>() {}.type
                )
                arrayList = yourArray

                clickedUrl = intent.getStringExtra("clickedUrl")
                resources = intent.getStringArrayListExtra("resoureList")
                Log.e("clicked url===", clickedUrl + "")
                val sessionManager = SessionManager(this@ActivityStatus)
                UserName = sessionManager.userName
                HostProfileId = sessionManager.userId
                Log.e("clicked our ids===", "$UserName=====$HostProfileId=====$UserID")
                mAge = sessionManager.userAge
                ProfilePic = sessionManager.userProfilepic
                Level = sessionManager.userLevel
                Location = sessionManager.userLocation
                var thumbnailUrl: String? = null
                for (video in arrayList!!) {
                    if (video.videoUrl.equals(clickedUrl, ignoreCase = true)) {
                        thumbnailUrl = video.videoThumbnail
                        break
                    }
                }
                Glide.with(this@ActivityStatus).load(thumbnailUrl)
                    .placeholder(R.drawable.ic_no_image).into(binding.ivThumbnail)
                for (videoUrl in resources!!) {
                    if (videoUrl.equals(clickedUrl, ignoreCase = true)) {
                        counter = resources!!.indexOf(videoUrl)
                    }
                }

                // Log.e("resource==2=>", counter+"");
                val iv_message = findViewById<ImageView>(R.id.iv_message)
                val video_chat = findViewById<ImageView>(R.id.video_chat)
                iv_message.isEnabled = false
                video_chat.isEnabled = false
                binding.ivHost.isEnabled = false
                binding.ivCloseTopRight.visibility = View.GONE
                binding.ivReportStatusRight.visibility = View.GONE
                binding.ivDeleteRight.visibility = View.VISIBLE
            } else {
                UserName = intent.getStringExtra("name")
                UserID = intent.getStringExtra("id")
                HostProfileId = intent.getStringExtra("profileId")
                // hostIdFemale = String.valueOf(getIntent().getSerializableExtra("id2"));
                ProfilePic = intent.getStringExtra("profile_pic")
                Location = intent.getStringExtra("location")
                Level = intent.getStringExtra("level")
                mAge = intent.getStringExtra("age")
                callRate = intent.getIntExtra("callrate", 0)
                resources = intent.getStringArrayListExtra("resoureList")
                apiManager!!.getProfileData(UserID.toString(), "")
            }
            binding.ivDeleteRight.setOnClickListener(View.OnClickListener { openDeleteOption() })
            binding.ivCloseTopRight.setOnClickListener(this)
            binding.ivReportStatusRight.setOnClickListener(this)
            binding.ivHost.setOnClickListener(View.OnClickListener {
                apiManager!!.getProfileIdData(
                    HostProfileId
                )
            })
            if (ProfilePic != "") {
                Glide.with(this).load(ProfilePic).apply(
                    RequestOptions().placeholder(R.drawable.female_placeholder).error(
                        R.drawable.female_placeholder
                    )
                ).into(binding.ivHost)
            } else {
                Glide.with(this).load(R.drawable.female_placeholder).apply(RequestOptions())
                    .into(binding.ivHost)
            }
            binding.tvHostName.text = UserName
            binding.tvHostAge.text = mAge
            binding.tvHostArea.text = Location
            val levelString = "" + Level
            binding.tvLevel.text = levelString
            binding.stories.setStoriesCount(resources!!.size)
            binding.stories.setStoryDuration(15000L)
            binding.stories.setStoriesListener(this)
            binding.stories.startStories(counter)
            Handler().postDelayed({ binding.stories.pause() }, 100)
            if (resources!!.size == 1) {
                Log.e("resources size===", resources!!.size.toString() + "")
            } else {
                val reverse = findViewById<View>(R.id.reverse)
                reverse.setOnClickListener { binding.stories.reverse() }
                reverse.setOnTouchListener(onTouchListener)
                val skip = findViewById<View>(R.id.skip)
                skip.setOnClickListener { binding.stories.skip() }
                skip.setOnTouchListener(onTouchListener)
            }
        }
    }

    private fun openDeleteOption() {
        val notifyDialog = Dialog(this@ActivityStatus)
        notifyDialog.setContentView(R.layout.delete_menu)
        notifyDialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        notifyDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wmlp = notifyDialog.window!!.attributes
        wmlp.gravity = Gravity.TOP or Gravity.RIGHT
        wmlp.x = 100 //x position
        wmlp.y = 100 //y position
        notifyDialog.setCancelable(true)
        notifyDialog.show()
        val tvDelete = notifyDialog.findViewById<View>(R.id.tvDelete) as TextView
        tvDelete.setOnClickListener {
            notifyDialog.dismiss()
            var profileVideoId: Int? = null
            for (video in arrayList!!) {
                if (video.videoUrl.equals(resources!![counter], ignoreCase = true)) {
                    profileVideoId = video.id
                    break
                }
            }
            if (profileVideoId != null) ApiManager(
                this@ActivityStatus,
                this@ActivityStatus
            ).deleteProfileVideo(profileVideoId.toString())
        }
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun playWithExoplayer(videoURL: Uri) {
        try {
            closeExoPlayer()

            val builder: DefaultLoadControl.Builder =
                DefaultLoadControl.Builder().setBufferDurationsMs(
                    loadControlMinBufferMs,
                    loadControlDefaultMaxBufferMs,
                    loadControlStartPlayBufferMs,
                    loadControlAfterRebufferMs
                ).setAllocator(DefaultAllocator(true, 5 * 1024 * 1024))
                    .setPrioritizeTimeOverSizeThresholds(true)
            val defaultLoadControl: DefaultLoadControl = builder.build()
            player = ExoPlayer.Builder(this).setLoadControl(defaultLoadControl).build()
                .also { exoPlayer ->
                    binding.exoplayerView.player = exoPlayer

                    val mediaItem = MediaItem.fromUri(videoURL)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.playWhenReady = playWhenReady
                    exoPlayer.seekTo(currentItem, playbackPosition)
                    exoPlayer.prepare()
                }
            player!!.addListener(object : Player.Listener {

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
                        //do something
                        binding.ivThumbnail.visibility = View.GONE
                        binding.stories.resume()
                    }
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateDbAccordingly(
        listfromApi: ArrayList<String>?,
        localVideoLinkList: ArrayList<String>?
    ) {
        val templist = ArrayList<String>()
        templist.clear()
        for (i in listfromApi!!.indices) {
            if (localVideoLinkList!!.contains(listfromApi[i])) {
                /*Log.e(
                    "accordingupdate",
                    "updateDbAccordingly:    listfromApi element has in localVideoLinkList  " + listfromApi[i]
                )*/
                templist.add(listfromApi[i])
            }
        }
        localVideoLinkList!!.removeAll(templist)
//        Log.e("accordingupdate", "updateDbAccordingly: " + localVideoLinkList.size)
        for (j in localVideoLinkList.indices) {
            val row_Id = statusDBHandler!!.getRowId(HostProfileId, localVideoLinkList[j])
            statusDBHandler!!.removeVideoRow(row_Id)
            /*Log.e(
                "accordingupdate",
                "updateDbAccordingly: row removed " + row_Id + "  video removed   " + localVideoLinkList[j]
            )*/
        }
    }

    private fun setVideoNumToSharedPref(num: Int, userID: String?) {
        sharedPreferences!!.edit().putInt(userID, num).apply()
    }

    override fun onNext() {
        Log.e("AC_STATUS", "onNext: " + "nextClick")
        counter++
        if (localListFromDB == null || localListFromDB!!.isEmpty()) {
            Log.e("VidTest", "onNext: if " + "Play from URL")
            setThumbnailImage(thumbnailList!![counter])
            playWithExoplayer(Uri.parse(resources!![counter]))
            for (i in resources!!.indices) {
                DownloadFileWithThread(resources!![i])
            }
        } else {
            var tempPath: String? = ""
            for (i in localListFromDB!!.indices) {
                if (localListFromDB!![i].videoLink == resources!![counter]) {
                    tempPath = localListFromDB!![i].videoURI
                    isVideoPresentInLocal = true
                    break
                } else {
                    isVideoPresentInLocal = false
                }
            }
            if (isVideoPresentInLocal) {
                Log.e("VidTest", "onNext: else" + "Play from Local")
                setThumbnailImage(tempPath.toString())
                playWithExoplayer(Uri.parse(tempPath))
            } else {
                Log.e("VidTest", "onNext: else" + "Play from URL1")

                setThumbnailImage(thumbnailList!![counter])
                playWithExoplayer(Uri.parse(resources!![counter]))
                for (i in resources!!.indices) {
                    DownloadFileWithThread(resources!![i])
                }
            }
        }

    }

    override fun onPrev() {
        Log.e("AC_STATUS", "onPrev: " + "prevClick")
        //  if ((counter - 1) < 0) return;
        if (counter - 1 < 0) {
            counter = 0
            Log.e("AC_STATUS", "onPrev: " + "No sAction===")
        } else {
            counter = counter - 1
            if (localListFromDB == null || localListFromDB!!.isEmpty()) {
                Log.e("VidTest", "onPrev: " + " null")
                Log.e("VidTest", "onPrev: " + "Play from URL")
                setThumbnailImage(thumbnailList!![counter])
                playWithExoplayer(Uri.parse(resources!![counter]))
                for (i in resources!!.indices) {
                    DownloadFileWithThread(resources!![i])
                }
            } else {
                var tempPath: String? = ""
                for (i in localListFromDB!!.indices) {
                    if (localListFromDB!![i].videoLink == resources!![counter]) {
                        tempPath = localListFromDB!![i].videoURI
                        isVideoPresentInLocal = true
                        break
                    } else {
                        isVideoPresentInLocal = false
                    }
                }
                if (isVideoPresentInLocal) {
                    Log.e("VidTest", "onPrev: " + "Play from Local")
                    setThumbnailImage(tempPath.toString())
                    playWithExoplayer(Uri.parse(tempPath))
                    //  videoView.setVideoURI(Uri.parse(tempPath));
                } else {
                    Log.e("VidTest", "onPrev: " + "Play from URL1")
                    setThumbnailImage(thumbnailList!![counter])
                    playWithExoplayer(Uri.parse(resources!![counter]))
                    for (i in resources!!.indices) {
                        DownloadFileWithThread(resources!![i])
                    }
                }
            }
        }
    }

    override fun onComplete() {
        Log.e("AC_STATUS", "onComplete: " + "videoCompleted")
        setVideoNumToSharedPref(counter, UserID)
        val ss = UserID + "bool"
        sharedPreferences!!.edit().putBoolean(ss, true).apply()
        /* Intent i = new Intent(ActivityStatus.this, MainActivity.class);
        startActivity(i);*/finish()
    }

    override fun onDestroy() {
        binding.stories.destroy()
        super.onDestroy()
    }

    private val callType = ""
    fun openVideoChat(view: View?) {

        /*
       if (CheckPermission()) {
            callType = "video";
            //   apiManager.getRemainingGiftCardFunction();
            view.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                }
            }, 2000);

        } else {

        }
        */
        customErrorToast()
    }

    private fun customErrorToast() {
        val li = layoutInflater
        val layout = li.inflate(R.layout.unable_to_call_lay, toast as ViewGroup?)
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(Gravity.TOP, 0, 30)
        toast.view = layout
        toast.show()
    }

    fun gotoChatConversation(view: View?) {
        /*
        Intent intent = new Intent(ActivityStatus.this, InboxDetails.class);
        intent.putExtra("profileName", userData.get(0).getName());
        intent.putExtra("user_image", userData.get(0).getFemaleImages().get(0).getImageName());
        intent.putExtra("chatProfileId", String.valueOf(userData.get(0).getProfileId()));
        intent.putExtra("contactId", userData.get(0).getId());
        intent.putExtra("mode", true);
        intent.putExtra("channelName", "zeeplive662730982537574");
        intent.putExtra("usercount", 0);
        intent.putExtra("unreadMsgCount", 0);
        startActivity(intent);
        */
        customErrorToast()
    }

    private fun CheckPermission(): Boolean {
        val isPermissionGranted = BooleanArray(1)
        Dexter.withActivity(this@ActivityStatus).withPermissions(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                Log.e("onPermissionsChecked", "onPermissionsChecked: ")
                if (report.areAllPermissionsGranted()) {
                    Log.e("onPermissionsChecked", "all permission granted")
                    isPermissionGranted[0] = true
                } else {
                    isPermissionGranted[0] = false
                    Toast.makeText(
                        this@ActivityStatus,
                        "To use this feature Camera and Audio permissions are must.You need to allow the permissions.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Dexter.withActivity(ViewProfile.this).withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {
                Log.e("onPermissionsChecked", "onPermissionRationaleShouldBeShown")
                token.continuePermissionRequest()
            }
        }).onSameThread().check()
        return isPermissionGranted[0]
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_close_top_right -> {
                Log.e("ActivityStatus", " i am here")
                finish()
            }
            R.id.iv_report_status_right -> {
                Log.e("ActivityStatus", " i am here")
                reportUser()
            }
        }
    }

    fun reportUser() {
        Log.e("newUserId", UserID + "")
        ReportDialog(this@ActivityStatus, UserID.toString())
    }

    private fun DownloadFileWithThread(url: String) {
        if (isConnectingToInternet) {
            Thread {
                try {
                    val currentFile = File(url)
                    val fileName = currentFile.name
                    Log.i("filename", fileName)
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connect()
                    if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                        Log.e(
                            "error",
                            "Server returned HTTP " + connection.responseCode + " " + connection.responseMessage
                        )
                    }
                    var OutputFile: File? = null
                    if (fileName.contains(".mp4")) {
                        OutputFile = File(getFolderPath1("Video"), fileName)
                    } else if (fileName.contains(".png") || fileName.contains(".jpg") || fileName.contains(
                            ".jpeg"
                        )
                    ) {
                        OutputFile = File(getFolderPath1("Images"), fileName)
                    } else if (fileName.contains(".pdf")) {
                        OutputFile = File(getFolderPath1("Files"), fileName)
                    }
                    if (!OutputFile!!.exists()) {
                        OutputFile.createNewFile()
                        val fos = FileOutputStream(OutputFile)
                        val `is` = connection.inputStream
                        val buffer = ByteArray(1024)
                        var len1 = 0
                        while (`is`.read(buffer).also { len1 = it } != -1) {
                            fos.write(buffer, 0, len1) //Write new file
                        }
                        fos.close()
                        `is`.close()
                        statusDBHandler!!.addVideos(
                            HostProfileId,
                            url,
                            OutputFile.absolutePath,
                            System.currentTimeMillis().toString()
                        )
                    }
                    val finalOutputFile = OutputFile
                    Log.e(
                        "ActivityStatus",
                        "DownloadFileWithThread: Path " + finalOutputFile.absolutePath + "   url " + url
                    )
                    Log.e("ActivityStatus", "DownloadFileWithThread: host $HostProfileId")

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private val isConnectingToInternet: Boolean
        private get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

    override fun onBackPressed() {
        finish()
    }

    override fun isError(errorCode: String) {}
    private val success = false
    private val remGiftCard = 0
    private val freeSeconds: String? = null
    override fun isSuccess(response: Any, ServiceCode: Int) {
        if (ServiceCode == Constant.GET_DATA_FROM_PROFILE_ID) {

            //   Log.e("ActivityStatus", "isSuccess: "+"Goto ViewProfile" );
            //   Log.e("ActivityStatus", "isSuccess: " + new Gson().toJson(rsp));
            val rsp = response as DataFromProfileIdResponse
            val rlt = rsp.result
            Log.e("ActivityStatus", "isSuccess: " + Gson().toJson(rsp))
            val intent = Intent(this@ActivityStatus, ViewProfile::class.java)
            intent.putExtra("id", rlt.id)
            intent.putExtra("profileId", rlt.profile_id)
            intent.putExtra("level", rlt.level)
            startActivity(intent)
            finish()
        }
        if (ServiceCode == Constant.GET_PROFILE_DATA) {
            Log.e("actStatus", "isSuccess: GET_PROFILE_DATA ")
            // UserListResponse.Data userData;
            val rsp = response as UserListResponseNewData
            //userData = (ResultDataNewProfile) rsp.getResult();
            userData.addAll(rsp.result)
            userId = userData[0].id
            hostId = userData[0].profileId
            callRate = userData[0].callRate
        }
        if (ServiceCode == Constant.VIDEO_STATUS_DELETE) {
            Log.e("Delete===response==", response.toString() + "")
            Toast.makeText(this@ActivityStatus, "Video Deleted Successfully", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private fun getMessageWithCall(
        receiverId: String,
        userName: String,
        userId: String,
        uniqueId: String,
        isFreeCall: String,
        profilePic: String,
        callType: String,
        canCallTill: Long
    ): String {
        val messageObject = JSONObject()
        val OtherInfoWithCall = JSONObject()
        try {
            OtherInfoWithCall.put("UserName", userName)
            OtherInfoWithCall.put("UserId", userId)
            OtherInfoWithCall.put("UniqueId", uniqueId)
            OtherInfoWithCall.put("IsFreeCall", isFreeCall)
            OtherInfoWithCall.put("Name", userName)
            OtherInfoWithCall.put("ProfilePicUrl", profilePic)
            OtherInfoWithCall.put("CallType", callType)
            OtherInfoWithCall.put("CallAutoEnd", canCallTill)
            messageObject.put("isMessageWithCall", "yes")
            messageObject.put("CallMessageBody", OtherInfoWithCall.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return messageObject.toString()
    }

    private fun setThumbnailImage(path: String) {
        binding.ivThumbnail.visibility = View.VISIBLE
        val requestOptions = RequestOptions()
        Glide.with(this@ActivityStatus).load(path).apply(requestOptions)
            .placeholder(R.drawable.ic_no_image).into(binding.ivThumbnail)
    }

    override fun onResume() {
        super.onResume()

        try {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())

            executor.execute {

                //Background work here
                localListFromDB = statusDBHandler!!.getVideosRowList(HostProfileId)
                localVideoLinkList = statusDBHandler!!.getVideoLinksList(HostProfileId)

                handler.post {

                    if (localListFromDB == null || localListFromDB!!.isEmpty()) {
                        setThumbnailImage(thumbnailList!![counter])
                        if (intent.getStringExtra("inWhichActivity")
                                .equals("ProfileVideoAdapter", ignoreCase = true)
                        ) {
                            playWithExoplayer(Uri.parse(clickedUrl))
                            for (i in resources!!.indices) {
                                DownloadFileWithThread(resources!![i])
                            }
                        } else {
                            playWithExoplayer(Uri.parse(resources!![counter]))
                            for (i in resources!!.indices) {
                                DownloadFileWithThread(resources!![i])
                            }
                        }
                    } else {
                        updateDbAccordingly(resources, localVideoLinkList)
                        var tempPath: String? = ""
                        for (i in localListFromDB!!.indices) {
                            if (localListFromDB!![i].videoLink == resources!![counter]) {
                                tempPath = localListFromDB!![i].videoURI
                                isVideoPresentInLocal = true
                                break
                            } else {
                                isVideoPresentInLocal = false
                            }
                        }
                        if (isVideoPresentInLocal) {
                            setThumbnailImage(tempPath.toString())
                            playWithExoplayer(Uri.parse(tempPath))
                        } else {
                            setThumbnailImage(resources!![counter])
                            playWithExoplayer(Uri.parse(resources!![counter]))
                            for (i in resources!!.indices) {
                                DownloadFileWithThread(resources!![i])
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getFolderPath1(type: String): File? {
        var myFolder: File? = null
        if (type == "Video") {
            myFolder = File(getExternalFilesDir("All Files"), "Status Videos")
            if (!myFolder.exists()) {
                myFolder.mkdir()
            }
        } else if (type == "Images") {
            myFolder = File(getExternalFilesDir("All Files"), "Photos")
            if (!myFolder.exists()) {
                myFolder.mkdir()
            }
        } else if (type == "Files") {
            myFolder = File(getExternalFilesDir("All Files"), "Other Files")
            if (!myFolder.exists()) {
                myFolder.mkdir()
            }
        }
        return myFolder
    }

    override fun onPause() {
        super.onPause()
        closeExoPlayer()
    }

    private fun closeExoPlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

}