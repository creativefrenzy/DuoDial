package com.privatepe.host.fragments;

import static com.privatepe.host.main.Home.cardView;
import static com.privatepe.host.main.Home.unread;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.privatepe.host.BuildConfig;
import com.privatepe.host.Fast_screen.FastScreenActivity;
import com.privatepe.host.R;
import com.privatepe.host.databinding.FragmentFemaleHomeBinding;
import com.privatepe.host.model.AppUpdate.UpdateResponse;
import com.privatepe.host.model.BroadcastStart.BroadCastResponce;
import com.privatepe.host.model.EndCallData.EndCallData;
import com.privatepe.host.model.OnlineStatusResponse;
import com.privatepe.host.model.ProfileDetailsResponse;
import com.privatepe.host.model.language.LanguageResponce;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.retrofit.ApiInterface;
import com.privatepe.host.sqlite.Chat;
import com.privatepe.host.sqlite.ChatDB;
import com.privatepe.host.utils.AppLifecycle;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.NetworkCheck;
import com.privatepe.host.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FemaleHomeFragment extends Fragment implements ApiResponseInterface {
    private NetworkCheck networkCheck;
    FragmentFemaleHomeBinding binding;
    public int onlineStatus;
    private Camera mCamera;
    //private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    private ApiInterface apiService;
    private ApiResponseInterface mApiResponseInterface;
    String authToken;
    public FemaleHomeFragment() {
        // Required empty public constructor
    }
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    private AppLifecycle appLifecycle;
    //ProgressDialog mProgressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_female_home, container, false);
        myContext = getContext();
        cardView.setVisibility(View.VISIBLE);
        networkCheck = new NetworkCheck();
        sessionManager = new SessionManager(getContext());
        appLifecycle = new AppLifecycle();
        init();

        return binding.getRoot();
    }
    private void init(){
        //AppLifecycle appLifecycle = new AppLifecycle();
        pDialog = new ProgressDialog(getContext(), R.style.MyTheme);
        pDialog.setMessage("There is a\nNEW version\n\nVideo connection is more stable\nUser experience is easier to use");
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        new SessionManager(myContext).setHostAutopickup("no");
        //String token = new SessionManager(myContext).getUserToken();
        if(networkCheck.isNetworkAvailable(getContext())) {
          new ApiManager(getContext()).changeOnlineStatus(0);
        }

        if(!sessionManager.getIsRtmLoggedIn()) {
         //   appLifecycle.loginRtm(sessionManager.getUserId());
            sessionManager.setIsRtmLoggedIn(true);
        }
    }

    private void checkLastCallData() {
        try {
            if (new SessionManager(getContext()).getUserGetendcalldata().equals("error")) {
                // Log.e("callEndArrayData", new Gson().toJson(new SessionManager(getContext()).getUserEndcalldata()));
                ArrayList<EndCallData> endCallData = new ArrayList<>();

                JSONArray arr = new JSONArray(new Gson().toJson(new SessionManager(getContext()).getUserEndcalldata()));
                //loop through each object
                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonProductObject = arr.getJSONObject(i);
                    String uniqueId = jsonProductObject.getString("unique_id");
                    String endTime = jsonProductObject.getString("end_time");
                    //Log.e("inErrorofEndcall", name);

                    EndCallData endCallData1 = new EndCallData(uniqueId, endTime);

                    endCallData.add(endCallData1);
                }

                new ApiManager(getContext()).submitEndCallData(endCallData);
            }
        } catch (Exception e) {
            checkLastCallData();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ApiManager(getContext(), this).getUpdateApp();
        binding.btnStartBroad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentActivity, FastScreenActivity.class);
                startActivity(intent);
            }
        });
    }
    boolean recording = false;
    Activity currentActivity;


    @Override
    public void onResume() {
        super.onResume();
        Log.e("inFamaleHome", "onResume");
        ChatDB db = new ChatDB(getContext());
        List<Chat> peers = db.getAllPeer();
        if(peers.size()>0) {
            int countAll = 0;
            for (Chat cn : peers) {
                countAll = + countAll + db.getAllChatUnreadCount(cn.get_id());
                if(countAll > 0){
                    unread.setVisibility(View.VISIBLE);
                    unread.setText(String.valueOf(countAll));
                }else{
                    unread.setVisibility(View.INVISIBLE);
                }
            }
        }
        currentActivity = getActivity();
        if (new SessionManager(getContext()).getFastModeState() == 1) {
            //binding.fastmodeSwitch.setChecked(true);
            binding.profilePicBg.setVisibility(View.VISIBLE);
            binding.svCamera.setVisibility(View.GONE);
        }

        if (new SessionManager(getContext()).getOnlineFromBack() == 0) {
            //binding.statusSwitch.setChecked(false);
        }

        if(networkCheck.isNetworkAvailable(currentActivity)) {
            new ApiManager(getContext(), this).getProfileDetails();
            new ApiManager(getContext(), this).getUserLanguage();
            //new ApiManager(getContext(), this).getUpdateApp();
        }else{
            Toast.makeText(getContext(), "Check your connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    String userId = "", userName = "", userImg = "";

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        try {
            if (ServiceCode == Constant.GET_MAINTAINENCE_DATA) {
                UpdateResponse updateResponse = (UpdateResponse) response;
                if (!updateResponse.getResult().getApp_version().equals("")) {
                    try {
                        float versionCode = Float.parseFloat(getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName);
                      //  int versionCode = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
                        if(Float.parseFloat(updateResponse.getResult().getApp_version()) > versionCode){
                            final DownloadApkFileFromURL downloadTask = new DownloadApkFileFromURL(getContext());
                            downloadTask.execute(updateResponse.getResult().getApp_apk());
                            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true); //cancel the task
                                }
                            });
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went here", Toast.LENGTH_SHORT).show();
                }
            }
            if (ServiceCode == Constant.GET_BROADCAST_START) {
                BroadCastResponce broadCastResponce = (BroadCastResponce) response;
                if (!broadCastResponce.getBroadCastData().getChannelName().equals("")) {

                } else {
                    Toast.makeText(getContext(), "Something went here", Toast.LENGTH_SHORT).show();
                }
            }
            if (ServiceCode == Constant.PROFILE_DETAILS) {
                ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;

                if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                    if (!rsp.getSuccess().getProfile_images().get(0).getImage_name().equals("")) {
                        Glide.with(getContext()).load(rsp.getSuccess().getProfile_images().get(0).getImage_name())
                                .placeholder(R.drawable.default_profile).into(binding.profilePicBg);
                    }
                }

                String img = "";
                if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                    img = rsp.getSuccess().getProfile_images().get(0).getImage_name();
                }

                userId = String.valueOf(rsp.getSuccess().getProfile_id());
                userName = rsp.getSuccess().getName();
                userImg = img;
                //String level = String.valueOf(rsp.getSuccess().getLevel());
                //String userDob = rsp.getSuccess().getDob();
                //String[] age = userDob.split("-");

                //SessionManager sessionManager = new SessionManager(getContext());
                //String token = sessionManager.getUserToken();
                //new SessionManager(getContext()).setUserAge(getAge(Integer.parseInt(age[2]), Integer.parseInt(age[0]), Integer.parseInt(age[1])));
                new SessionManager(getContext()).setUserProfilepic(img);
                new SessionManager(getContext()).setUserName(rsp.getSuccess().getName());
                //new SessionManager(getContext()).setUserLevel(String.valueOf(rsp.getSuccess().getLevel()));
            }

            if (ServiceCode == Constant.MANAGE_ONLINE_STATUS) {
                OnlineStatusResponse reportResponse = (OnlineStatusResponse) response;
                if (reportResponse.getResult() != null) {
                    new SessionManager(getContext()).setOnlineFromBack(1);
                }
            }

            if (ServiceCode == Constant.LAN_DATA) {
                LanguageResponce rsp = (LanguageResponce) response;

                if (!rsp.getSuccess()) {
                    logoutDialog();
                }
            }
        } catch (Exception e) {
        }
    }
    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }


    void logoutDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        TextView logout = dialog.findViewById(R.id.logout);

        tv_msg.setText("You have been logout. As your access token is expired.");

        closeDialog.setVisibility(View.GONE);
        closeDialog.setOnClickListener(view -> dialog.dismiss());

        logout.setText("OK");
        logout.setOnClickListener(view -> {
            dialog.dismiss();
            String eMail = new SessionManager(getContext()).getUserEmail();
            String passWord = new SessionManager(getContext()).getUserPassword();
            new SessionManager(getContext()).logoutUser();
            new ApiManager(getContext(), this).getUserLogout();
            new SessionManager(getContext()).setUserEmail(eMail);
            new SessionManager(getContext()).setUserPassword(passWord);
            getActivity().finish();
        });
    }
    class DownloadApkFileFromURL extends AsyncTask<String, Integer, String> {
        /**
         * Before starting background thread
         * */
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadApkFileFromURL(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            pDialog.show();
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgress(progress[0]);
        }
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                URL url = new URL(f_url[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                //delete existing apk file.
                File filePath = new File(root+"/app-release.apk");
                if (filePath.exists())
                    filePath.delete();

                connection.connect();
                input = new BufferedInputStream(url.openStream(), 8192);
                output = new FileOutputStream(root+"/app-release.apk");

                byte data[] = new byte[4096];
                long total = 0;
                //int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * After completing background task
         * **/
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            mWakeLock.release();

            //update apk code by sanjay
            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    Intent installApplicationIntent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "app-release.apk");
                    if (file.exists()) {
                        file.setReadable(true);
                        installApplicationIntent.setDataAndType(FileProvider.getUriForFile(getContext(),
                                BuildConfig.APPLICATION_ID + ".provider",
                                file), "application/vnd.android.package-archive");
                    } else {
                        // DisplayUtils.getInstance().displayLog(TAG, "file not found after downloading");
                    }
                    installApplicationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    installApplicationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    installApplicationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getContext().startActivity(installApplicationIntent);
                    // ExitActivity.exitApplicationAnRemoveFromRecent(getApplicationContext());
                } catch (Exception cv) {
                }
            } else
                //end update apk code
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "app-release.apk")));
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("inFamaleHome", "onPause");

        checkLastCallData();
    }
}
