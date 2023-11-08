package com.privatepe.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.privatepe.app.R;
import com.privatepe.app.databinding.ActivityCardBinding;
import com.privatepe.app.response.metend.RemainingGiftCard.RemainingGiftCardResponce;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;

public class CardActivity extends AppCompatActivity implements ApiResponseInterface {
    ActivityCardBinding binding;
    ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_card);

        binding.settingBack.setOnClickListener(view -> onBackPressed());
        binding.setClickListener(new EventHandler(this));

        apiManager = new ApiManager(this, this);
        checkFreeGift();

    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }
    }

    private void checkFreeGift() {
        if (new SessionManager(getApplicationContext()).getGender().equals("male")) {
            Log.e("CardActivity", "step1");
            apiManager.getRemainingGiftCardDisplayFunction();
        }
    }

    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            Toast.makeText(getApplicationContext(), errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD_DISPLAY) {
            RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;
            try {
                int remGiftCard = rsp.getResult().getTotalGiftCards();
                Log.e("CardActivity","step2 "+ String.valueOf(remGiftCard));
                if (remGiftCard > 0) {
                    binding.tvCardCount.setText("x " + String.valueOf(remGiftCard));
                }
            } catch (Exception e) {
                Log.e("freeGiftCardErrorDis", e.getMessage());

            }
        }


    }



}