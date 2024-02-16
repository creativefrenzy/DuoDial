package com.privatepe.app.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.R;
import com.privatepe.app.adapter.WeeklyUsersRewardAdapter;
import com.privatepe.app.response.daily_weekly.WeeklyUserRewardResponse;

import java.util.ArrayList;
import java.util.List;

public class DailyWeeklyBottomSheet extends Dialog {

    public static final String TAG = DailyWeeklyBottomSheet.class.getSimpleName();

    Activity activity;
    Context context;
    List<WeeklyUserRewardResponse.WeeklyRewardData> weeklyRewardDataList = new ArrayList<>();
    WeeklyUsersRewardAdapter weeklyUsersRewardAdapter;
    RecyclerView recyclerView;
    RelativeLayout rlParent;
    TextView tvSelfCount;
    private int rewardLayoutHeight = 500;
    String selfRewardCount;
    public DailyWeeklyBottomSheet(Context context, Activity activity, String selfCount, List<WeeklyUserRewardResponse.WeeklyRewardData> weeklyRewardDataList){
        super(context);
        this.activity = activity;
        this.context = context;
        this.weeklyRewardDataList = weeklyRewardDataList;
        this.selfRewardCount = selfCount;

        initView();
    }

    public void initView(){

        try {
            this.setContentView(R.layout.bottom_sheet_weekly_reward);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);

        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            final float scale = getContext().getResources().getDisplayMetrics().density;
            rewardLayoutHeight = (int) (500 * scale + 0.5f);
        } catch (Exception e) {
            rewardLayoutHeight = 500;
            e.printStackTrace();
        }

        try {
            rlParent.getLayoutParams().height = rewardLayoutHeight;
            rlParent.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        rlParent = findViewById(R.id.rlParent);
        tvSelfCount = findViewById(R.id.tvSelfCount);
        recyclerView = findViewById(R.id.rvWeeklyReward);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        weeklyUsersRewardAdapter = new WeeklyUsersRewardAdapter(context, weeklyRewardDataList, activity);
        recyclerView.setAdapter(weeklyUsersRewardAdapter);
        weeklyUsersRewardAdapter.notifyDataSetChanged();
        tvSelfCount.setText(selfRewardCount);

        show();
    }
}