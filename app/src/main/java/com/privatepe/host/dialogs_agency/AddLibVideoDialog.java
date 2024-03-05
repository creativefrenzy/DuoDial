package com.privatepe.host.dialogs_agency;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.privatepe.host.R;

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
                    case "2":
                        context.startActivity(new Intent(context, ShotVideoActivity.class));
                        break;
                    case "3":
                        context.startActivity(new Intent(context, AuditionVideoActivity.class));
                        break;
                }*/
                dismiss();
            }
        });
        show();
    }

}


