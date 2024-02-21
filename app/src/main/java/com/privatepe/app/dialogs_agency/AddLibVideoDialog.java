package com.privatepe.app.dialogs_agency;

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

import com.privatepe.app.BuildConfig;
import com.privatepe.app.R;
import com.privatepe.app.activity.addalbum.AddAlbumActivity;
import com.privatepe.app.activity.addalbum.AuditionVideoActivity;
import com.privatepe.app.activity.addalbum.ShotVideoActivity;
import com.privatepe.app.main.Home;
import com.privatepe.app.model.AppUpdate.UpdateResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.MagicProgressBar;
import com.privatepe.app.utils.SessionManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class AddLibVideoDialog extends Dialog {
    Context context;
    TextView tv_update;

    @RequiresApi(api = Build.VERSION_CODES.R)
    public AddLibVideoDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    void init() {
        this.setContentView(R.layout.addlib_video_dialog);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(true);

        TextView tv_update = findViewById(R.id.tv_update);

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String resControl = new SessionManager(getContext()).getResUpload();
                switch (resControl) {
                    case "0":
                        context.startActivity(new Intent(context, AddAlbumActivity.class));
                        break;
                    case "1":
                        context.startActivity(new Intent(context, AuditionVideoActivity.class));
                        break;
                    case "2":
                        context.startActivity(new Intent(context, ShotVideoActivity.class));
                        break;
                }*/
                dismiss();
            }
        });
        show();
    }

}


