package com.privatepe.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.privatepe.app.R;
import com.privatepe.app.adapter.ChatPriceListAdapter;
import com.privatepe.app.databinding.ActivityMyChatPriceBinding;
import com.privatepe.app.dialogs.chat_price.UpdateChatPriceBottomSheet;
import com.privatepe.app.response.chat_price.PriceDataModel;
import com.privatepe.app.response.chat_price.PriceListResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;

public class MyChatPriceActivity extends AppCompatActivity {

    private boolean darkText = true;

    protected void hideStatusBar(Window window) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && darkText) {
            flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        window.getDecorView().setSystemUiVisibility(flag | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }

    private ActivityMyChatPriceBinding binding;
    private LinearLayoutManager layoutManager;
    private ChatPriceListAdapter chatPriceAdapter;
    private SessionManager sessionManager;
    private ApiManager apiManager;
    private ArrayList<PriceDataModel> callPriceList = new ArrayList<>();
    private PriceListResponse priceListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_chat_price);
        sessionManager = new SessionManager(this);

        priceListResponse = sessionManager.getChatPriceListResponse();

        if (priceListResponse != null) {
            callPriceList = (ArrayList<PriceDataModel>) priceListResponse.getResult();
            layoutManager = new LinearLayoutManager(this);
            chatPriceAdapter = new ChatPriceListAdapter(callPriceList, this);
            binding.chatPriceList.setLayoutManager(layoutManager);
            binding.chatPriceList.setAdapter(chatPriceAdapter);
            binding.callrate.setText("" + sessionManager.getSelectedCallPrice());

            if (sessionManager.getGender().equals("male")) {
                binding.levelBg.setBackgroundResource(getMaleLevelImage(priceListResponse.getLevel()));
            } else {
                binding.levelBg.setBackgroundResource(getFemaleLevelImage(priceListResponse.getLevel()));
            }
            binding.levelText.setText("" + priceListResponse.getLevel());
        }


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.changeCallRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.e("CHANGE_CALL_RATE", "onClick: change call rate " );
                UpdateChatPriceBottomSheet updateChatPriceDialog = new UpdateChatPriceBottomSheet(MyChatPriceActivity.this);
                updateChatPriceDialog.show(getSupportFragmentManager(), "UpdateChatPriceBottomSheet");
            }
        });

        binding.callrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.changeCallRate.performClick();
            }
        });

        binding.tvMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.changeCallRate.performClick();
            }
        });


        binding.bottomLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getGender().equals("male")) {
                   // startActivity(new Intent(MyChatPriceActivity.this, LevelUpActivity.class));
                } else {
                    //startActivity(new Intent(MyChatPriceActivity.this, LevelActivity.class));

                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public int getFemaleLevelImage(int Level) {
        int resId = 0;

        if (Level == 0) {
            resId = R.drawable.charm_lv0;
        } else if ((Level > 0) && (Level <= 5)) {
            resId = R.drawable.charm_lv1_5;
        } else if ((Level > 5) && (Level <= 10)) {
            resId = R.drawable.charm_lv6_10;
        } else if ((Level > 10) && (Level <= 15)) {
            resId = R.drawable.charm_lv11_15;
        } else if ((Level > 15) && (Level <= 20)) {
            resId = R.drawable.charm_lv16_20;
        } else if ((Level > 20) && (Level <= 25)) {
            resId = R.drawable.charm_lv21_25;
        } else if ((Level > 25) && (Level <= 30)) {
            resId = R.drawable.charm_lv26_30;
        } else if ((Level > 30) && (Level <= 35)) {
            resId = R.drawable.charm_lv31_35;
        } else if ((Level > 35) && (Level <= 40)) {
            resId = R.drawable.charm_lv36_40;
        } else if ((Level > 40) && (Level <= 45)) {
            resId = R.drawable.charm_lv41_45;
        } else if ((Level > 45) && (Level <= 50)) {
            resId = R.drawable.charm__lv46_50;
        }

        return resId;
    }


    public int getMaleLevelImage(int Level) {
        int resId = 0;

        if (Level == 0) {
            resId = R.drawable.reach_lv0;
        } else if ((Level > 0) && (Level <= 5)) {
            resId = R.drawable.reach_lv1_5;

        } else if ((Level > 5) && (Level <= 10)) {
            resId = R.drawable.reach_lv6_10;
        } else if ((Level > 10) && (Level <= 15)) {

            resId = R.drawable.reach_lv11_15;

        } else if ((Level > 15) && (Level <= 20)) {

            resId = R.drawable.reach_lv16_20;

        } else if ((Level > 20) && (Level <= 25)) {

            resId = R.drawable.reach_lv21_25;

        } else if ((Level > 25) && (Level <= 30)) {

            resId = R.drawable.reach_lv26_30;

        } else if ((Level > 30) && (Level <= 35)) {

            resId = R.drawable.reach_lv31_35;

        } else if ((Level > 35) && (Level <= 40)) {

            resId = R.drawable.reach_lv36_40;

        } else if ((Level > 40) && (Level <= 45)) {

            resId = R.drawable.reach_lv41_45;

        } else if ((Level > 45) && (Level <= 50)) {
            resId = R.drawable.reach_lv45_50;
        }


        return resId;
    }


    public void setUpdatedCallRate(String amount) {
        sessionManager.setSelectedCallPrice((int) Double.parseDouble(amount));
        binding.callrate.setText(amount);
    }
}