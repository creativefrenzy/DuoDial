package com.privatepe.host.dialogs_agency;

import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import android.os.PowerManager;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.core.content.FileProvider;


import com.privatepe.host.BuildConfig;
import com.privatepe.host.R;
import com.privatepe.host.main.Home;
import com.privatepe.host.model.AppUpdate.UpdateResponse;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.MagicProgressBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class UpdateVersionDialog extends Dialog implements ApiResponseInterface {
    Home context;
    TextView tv_update;
    ApiManager apiManager;
    MagicProgressBar magicProgressBar;
    float versionCode;
    float versionCodeServer;
    String getApk;
    public boolean isShow=false;

   /*  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            magicProgressBar.setProgress(msg.arg1);
            if (msg.arg1 == 100) {
                magicProgressBar.finishLoad();
                dismiss();
            }
        }
    };*/
    @RequiresApi(api = Build.VERSION_CODES.R)
    public UpdateVersionDialog(@NonNull Home context) {
        super(context);
        this.context = context;
        init();
        isShow=true;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    void init() {
        this.setContentView(R.layout.update_version_dialog);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);
        apiManager = new ApiManager(context, this);
        apiManager.getUpdateApp();

        Log.e("UpdateVersionDialog", "init: ");

        magicProgressBar = new MagicProgressBar(context);
        try {
            TextView tv_update = findViewById(R.id.tv_update);
            magicProgressBar = findViewById(R.id.magicBar);

         /*
           magicProgressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!magicProgressBar.isFinish()) {
                        magicProgressBar.toggle();
                    }
                }
            });
        */

            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               //  downLoad();
                    tv_update.setVisibility(View.GONE);
                    try {
                        Log.e("UpdateVersionDialog", "versionCodeserver " + versionCodeServer);
                        Log.e("UpdateVersionDialog", "versionCode " + versionCode);
                        versionCode = Float.parseFloat(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);

                        if (versionCodeServer > versionCode) {
                            final DownloadApkFileFromURL downloadTask = new DownloadApkFileFromURL(context);
                            downloadTask.execute(getApk);
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

  /*  private void downLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(40);
                        Message message = handler.obtainMessage();
                        message.arg1 = i + 1;
                        handler.sendMessage(message);
                        tv_update = findViewById(R.id.tv_update);
                        tv_update.setVisibility(View.GONE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }*/

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_MAINTAINENCE_DATA) {
            UpdateResponse updateResponse = (UpdateResponse) response;

            Log.e("UpdateVersionDialog", "versionCodeServer " + Float.parseFloat(updateResponse.getResult().getApp_version()));
            if (!updateResponse.getResult().getApp_version().equals("")) {
                try {
                    versionCodeServer = Float.parseFloat(updateResponse.getResult().getApp_version());
                    versionCode = Float.parseFloat(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
                    //int versionCode = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
                    Log.e("UpdateVersionDialog", "versionCodeapi " + versionCode);
                    if (Float.parseFloat(updateResponse.getResult().getApp_version()) > versionCode) {
                        // final DownloadApkFileFromURL downloadTask = new DownloadApkFileFromURL(context);
                        getApk = updateResponse.getResult().getApp_apk();
                        show();
                        isShow=true;
                        // downloadTask.execute(updateResponse.getResult().getApp_apk());
                        /*pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true); //cancel the task
                            }
                        });*/
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }

    }

    class DownloadApkFileFromURL extends AsyncTask<String, Integer, String> {
        /**
         * Before starting background thread
         */
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

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            // pDialog.setIndeterminate(false);
            // pDialog.setMax(100);
            // pDialog.setProgress(progress[0]);

            magicProgressBar.setProgress(progress[0]);
           /* if (msg.arg1 == 100) {
                magicProgressBar.finishLoad();
                dismiss();
            }*/
        }

        /**
         * Downloading file in background thread
         */
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
                File filePath = new File(root + "/app-release.apk");
                if (filePath.exists())
                    filePath.delete();

                connection.connect();
                input = new BufferedInputStream(url.openStream(), 8192);
                output = new FileOutputStream(root + "/app-release.apk");

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
         **/
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String file_url) {
            dismiss();
            isShow=false;
            mWakeLock.release();

             /*   try {
                Intent installApplicationIntent = new Intent(Intent.ACTION_VIEW);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "KLiveRelease.apk");
                if (file.exists()) {
                    file.setReadable(true);
                    installApplicationIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            file), "application/vnd.android.package-archive");
                } else {
                    // DisplayUtils.getInstance().displayLog(TAG, "file not found after downloading");
                }
                installApplicationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                installApplicationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installApplicationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getApplicationContext().startActivity(installApplicationIntent);
                // ExitActivity.exitApplicationAnRemoveFromRecent(getApplicationContext());
            } catch (Exception cv) {
            }*/
            //update apk code by sanjay


            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    Intent installApplicationIntent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "KLiveRelease.apk");
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
                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "KLiveRelease.apk")));
                } catch (Exception e) {
                   /* Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "KLiveRelease.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);*/


                    try {
                        String PATH = Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath();
                        File file = new File(PATH + "/app-release.apk");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= 24) {
                            Uri downloaded_apk = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                            intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo resolveInfo : resInfoList) {
                                context.grantUriPermission(context.getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            getContext().startActivity(intent);
                        } else {
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        getContext().startActivity(intent);
                    } catch (Exception eq) {
                        eq.printStackTrace();
                    }

                }
        }
    }




    public boolean isShow()
    {
        return isShow;
    }

}


