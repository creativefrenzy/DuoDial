package com.klive.app.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.klive.app.R;
import com.klive.app.adapter.ViewLevelAdapter;
import com.klive.app.model.level.Level;
import com.klive.app.model.level.LevelDataResponce;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.utils.Constant;
import com.klive.app.utils.SessionManager;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity implements ApiResponseInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_level);


        setupControls();
        new ApiManager(this, this).getLevelData();
    }

    private void setupControls() {
        String imageUrl = new SessionManager(getApplicationContext()).getUserProfilepic();
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .circleCrop()
                .placeholder(R.drawable.default_profile)
                .into(((ImageView) findViewById(R.id.user_image)));
    }


    public void backFun(View v) {
        onBackPressed();
    }

    @Override
    public void isError(String errorCode) {

    }

    RecyclerView rv_leveldata;
    ViewLevelAdapter viewLevelAdapter;
    ProgressBar progressLevel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_LEVEL_DATA) {
            LevelDataResponce rsp = (LevelDataResponce) response;

            rv_leveldata = findViewById(R.id.rv_leveldata);
            progressLevel = findViewById(R.id.progressLevel);

            ArrayList<Level> levelArrayList = new ArrayList<>();
            levelArrayList.addAll(rsp.getResult().getLevel());

            for (int i = 0; i < levelArrayList.size(); i++) {
                Log.e("LevelActivity", "isSuccess: " + levelArrayList.get(0).getLevel());
            }


            viewLevelAdapter = new ViewLevelAdapter(getApplicationContext(), levelArrayList);

            //   rv_leveldata.setHasFixedSize(true);
            rv_leveldata.setLayoutManager(new LinearLayoutManager(this));
            rv_leveldata.setAdapter(viewLevelAdapter);

            ((TextView) findViewById(R.id.tv_lastweekearning)).setText(
                    rsp.getResult().getLastWeekReport().getTotalCoins() + ""
            );

            ((TextView) findViewById(R.id.tv_thisweekearning)).setText(
                    rsp.getResult().getCurrentWeekReport().getTotalCoins() + ""
            );

            calcLevelIndi(rsp.getResult().getLastWeekReport().getTotalCoins(), 0);
            calcLevelIndi(rsp.getResult().getCurrentWeekReport().getTotalCoins(), 1);
            calcLevel(rsp.getResult().getCurrentWeekReport().getTotalCoins(), rsp.getResult().getLastWeekReport().getTotalCoins());

            Log.e("LevelActivity", "isSuccess: " + rsp.getResult().getCurrentWeekReport().getTotalCoins() + "      " + rsp.getResult().getLastWeekReport().getTotalCoins());


        }

    }

    private void calcLevelIndi(int leveldata, int type) {

        if (leveldata <= 3600) {
            // lv1
            setLevelData("Lv1", type);
        } else if (leveldata <= 10000) {
            // lv2
            setLevelData("Lv2", type);
        } else if (leveldata <= 20000) {
            //lv3
            setLevelData("Lv3", type);
        } else if (leveldata <= 40000) {
            //lv4
            setLevelData("Lv4", type);
        } else if (leveldata <= 60000) {
            //lv5
            setLevelData("Lv5", type);
        } else if (leveldata <= 80000) {
            //lv6
            setLevelData("Lv6", type);
        } else if (leveldata <= 100000) {
            //lv7
            setLevelData("Lv7", type);
        } else if (leveldata <= 140000) {
            //lv8
            setLevelData("Lv8", type);
        } else if (leveldata <= 200000) {
            //lv9
            setLevelData("Lv9", type);
        } else if (leveldata <= 500000) {
            //lv10
            setLevelData("Lv10", type);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void calcLevel(int thisWeek, int lastWeek) {
        int dataTocalc = 0;

        dataTocalc = Math.max(thisWeek, lastWeek);

        if (dataTocalc <= 3600) {
            // lv1
            displayProgress(0, 3600, dataTocalc);
            setLevelUI("Lv1", "Lv2");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level10_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level10_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level9_trans_relative));
        } else if (dataTocalc <= 10000) {
            // lv2
            displayProgress(3600, 10000, dataTocalc);
            setLevelUI("Lv2", "Lv3");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level9_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level9_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level8_trans_relative));
        } else if (dataTocalc <= 20000) {
            //lv3
            displayProgress(10000, 20000, dataTocalc);
            setLevelUI("Lv3", "Lv4");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level8_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level8_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level7_trans_relative));
        } else if (dataTocalc <= 40000) {
            //lv4
            displayProgress(20000, 40000, dataTocalc);
            setLevelUI("Lv4", "Lv5");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level7_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level7_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level6_trans_relative));
        } else if (dataTocalc <= 60000) {
            //lv5
            displayProgress(40000, 60000, dataTocalc);
            setLevelUI("Lv5", "Lv6");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level6_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level6_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level5_trans_relative));
        } else if (dataTocalc <= 80000) {
            //lv6
            displayProgress(60000, 80000, dataTocalc);
            setLevelUI("Lv6", "Lv7");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level5_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level5_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level4_trans_relative));
        } else if (dataTocalc <= 100000) {
            //lv7
            displayProgress(80000, 100000, dataTocalc);
            setLevelUI("Lv7", "Lv8");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level4_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level4_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level3_trans_relative));
        } else if (dataTocalc <= 140000) {
            //lv8
            displayProgress(100000, 140000, dataTocalc);
            setLevelUI("Lv8", "Lv9");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level3_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level3_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level2_trans_relative));
        } else if (dataTocalc <= 200000) {
            //lv9
            displayProgress(140000, 200000, dataTocalc);
            setLevelUI("Lv9", "Lv10");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level2_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level2_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level1_trans_relative));
        } else if (dataTocalc <= 500000) {
            //lv10
            displayProgress(200000, 500000, dataTocalc);
            setLevelUI("Lv10", "Lv11");

            ((RelativeLayout) findViewById(R.id.rl_profilelevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level1_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_startlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level1_trans_relative));
            ((RelativeLayout) findViewById(R.id.rl_endlevel)).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level1_trans_relative));
        }
    }

    private void setLevelUI(String start, String end) {
        ((TextView) findViewById(R.id.tv_profilelevel)).setText(start);
        ((TextView) findViewById(R.id.tv_startlevel)).setText(start);
        ((TextView) findViewById(R.id.tv_endlevel)).setText(end);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayProgress(int min, int max, int progress) {

     //   progressLevel.setMin(min);
        progressLevel.setMax(max);
        progressLevel.setProgress(progress);

        ((LinearLayout) findViewById(R.id.ll_earninfo)).setVisibility(View.VISIBLE);
        int diffInLevel = max - progress;
        ((TextView) findViewById(R.id.tv_earninfo)).setText(String.valueOf(diffInLevel));
    }

    private void setLevelData(String level, int type) {
        if (type == 0) {
            ((TextView) findViewById(R.id.tv_lastweekleveldata)).setText(level);
        } else if (type == 1) {
            ((TextView) findViewById(R.id.tv_thisweekleveldata)).setText(level);
        }
    }
}