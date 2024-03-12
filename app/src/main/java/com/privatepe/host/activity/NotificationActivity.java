package com.privatepe.host.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.privatepe.host.main.Home;

public class NotificationActivity extends Activity {

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
public static MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.e("notifaiidd",""+getIntent().getIntExtra(NOTIFICATION_ID, -1));
      manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
      if(mp!=null) {
          mp.stop();
          mp.release();
      }
        //manager.cancelAll();
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
    }

    public static PendingIntent getDismissIntent(int notificationId, Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return dismissIntent;
    }



}