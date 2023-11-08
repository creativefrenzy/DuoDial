package com.privatepe.app.activity;

import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.privatepe.app.R;
import com.privatepe.app.databinding.ActivityRatingDialogNewBinding;
import com.privatepe.app.utils.BaseActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingDialogActivityNew extends BaseActivity {
    ActivityRatingDialogNewBinding binding;
    String hostId;
    String name;
    String image;
    String callTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(),true);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rating_dialog_new);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rating_dialog_new);
        binding.setClickListener(new EventHandler(this));
        // getWindow().setStatusBarColor(getResources().getColor(R.color.transparent_new));
        binding.cons.setBackgroundResource(R.drawable.select_payment_method_new_bg);
        init();
    }


    void init() {
        try {
            Intent in = getIntent();
            Bundle b = in.getExtras();
            if (b != null) {
                name = b.getString("host_name");
                hostId = b.getString("host_id");
                image = b.getString("host_image");
                callTime = b.getString("end_time");
            }
            CircleImageView img_profile = findViewById(R.id.iv_host);
            TextView tv_hostname = findViewById(R.id.tv_name_and_age);
            Glide.with(getApplicationContext()).load(image).circleCrop().into(((CircleImageView) findViewById(R.id.iv_host)));
            tv_hostname.setText(name);

        } catch (Exception e) {

        }
    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }
        public void closeButton() {
            finish();
            //overridePendingTransition(0, R.anim.splashfadeout);
        }

        public void likeEmoji() {
            finish();
            //overridePendingTransition(0, R.anim.splashfadeout);
        }
        public void badEmoji() {
            finish();
            //overridePendingTransition(0, R.anim.splashfadeout);

        }

    }

}
