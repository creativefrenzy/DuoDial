package com.privatepe.app.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.privatepe.app.R;
import com.privatepe.app.databinding.BottomSheetWeeklyRewardBinding;
import com.privatepe.app.response.daily_weekly.WeeklyUserRewardResponse;

import java.util.ArrayList;
import java.util.List;

public class DailyWeeklyBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG = DailyWeeklyBottomSheet.class.getSimpleName();
    BottomSheetWeeklyRewardBinding bottomSheetDailyWeeklyReportBinding;
    Activity activity;
    Context context;
    List<WeeklyUserRewardResponse.WeeklyRewardData> weeklyRewardDataList = new ArrayList<>();

    public static DailyWeeklyBottomSheet newInstance(Context context, Activity activity, List<WeeklyUserRewardResponse.WeeklyRewardData> weeklyRewardDataList) {

        DailyWeeklyBottomSheet fragment = new DailyWeeklyBottomSheet();
        activity = activity;
        context = context;
        weeklyRewardDataList = weeklyRewardDataList;
        return fragment;
    }

    public DailyWeeklyBottomSheet(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.SheetDialogNew);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bottomSheetDailyWeeklyReportBinding = BottomSheetWeeklyRewardBinding.inflate(getLayoutInflater());

        init();

        return bottomSheetDailyWeeklyReportBinding.getRoot();
    }
    void init() {


    }
}