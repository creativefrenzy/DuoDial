package com.privatepe.app.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.privatepe.app.R;
import com.privatepe.app.adapter.ViewLevelAdapter;
import com.privatepe.app.model.LevelData.Level;
import com.privatepe.app.model.LevelData.LevelDataResponce;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity implements ApiResponseInterface {

    private boolean darkText = true;
    private int nextLevelPoints = 0;
    private int currentLevelPoints = 0;
    private int requiredPointsToNextLevel = 0;
    private float progressPercentage = 0;
    private int percentageRounded = 0;
    private int thisWeekLevelPoints = 0;
    private int clPinPointWidth = 0;
    private int tvStartTotal = 0;
    private int diffCurrentToLastPoints = 0;

    private int lastLevelPoints = 0;
    Integer lvlCurrent = 0;
    LinearProgressIndicator linearProgressIndicator;
    ConstraintLayout clPinPoint;
    TextView tvLevel, tvLastWeekBeans,tvCurrentWeekBeans,tvLevelLastWeek,tvLevelCurrentWeek,tvStartLvl,tvEndLvl,tvReachNextLvl,tvPinPoints;
    protected void hideStatusBar(Window window) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        /*int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && darkText) {
            flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        window.getDecorView().setSystemUiVisibility(flag | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow());
        setContentView(R.layout.activity_level);
        setupControls();
        new ApiManager(this, this).getLevelData();
    }

    private void setupUI() {
        tvLevel = findViewById(R.id.tvLvl);
        tvLastWeekBeans = findViewById(R.id.tvLvlValueLast);
        tvCurrentWeekBeans = findViewById(R.id.tvLvlValueCurrent);
        tvLevelCurrentWeek = findViewById(R.id.tvLvlCurrentWeek);
        tvLevelLastWeek = findViewById(R.id.tvLvlLastWeek);
        tvStartLvl = findViewById(R.id.tvStartLevel);
        tvEndLvl = findViewById(R.id.tvEndLevel);
        tvReachNextLvl =  findViewById(R.id.tvPointsToReachLvl);
        linearProgressIndicator = findViewById(R.id.linearProgressIndicator);
        tvPinPoints = findViewById(R.id.tvPinPoint);
        clPinPoint = findViewById(R.id.clBeansValPin);

    }

    private void setupControls() {
        String imageUrl = new SessionManager(getApplicationContext()).getUserProfilepic();
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .circleCrop()
                .placeholder(R.drawable.default_profile)
                .into(
                        ((ImageView) findViewById(R.id.ivUser))
                );
    }


    public void backFun(View v) {
        onBackPressed();
    }

    @Override
    public void isError(String errorCode) {

    }

    private void updatePinPosition(int progress) {
        // Ensure that the view is measured before attempting to get its width
        clPinPoint.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                clPinPoint.getViewTreeObserver().removeOnPreDrawListener(this);
                 clPinPointWidth = clPinPoint.getWidth();
               /*  LayoutParams = (ViewGroup.LayoutParams) clPinPoint.getLayoutParams();*/


                return true;
            }
        });

        tvStartLvl.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                tvStartLvl.getViewTreeObserver().removeOnPreDrawListener(this);
                int tvStartWidth = tvStartLvl.getWidth();
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tvStartLvl.getLayoutParams();
                int tvLeftMargin = layoutParams.leftMargin;
                tvStartTotal = tvStartWidth + tvLeftMargin;
                return true;
            }
        });

        linearProgressIndicator.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                // Remove the listener to avoid multiple calls
                linearProgressIndicator.getViewTreeObserver().removeOnPreDrawListener(this);

                // Now, you can get the width of the LinearProgressIndicator
                int progressBarWidth = linearProgressIndicator.getWidth();

                // Calculate the X-coordinate for the pointing pin
                float progressRatio = (float) progress / 100;
                int pinX = (int) (progressBarWidth * progressRatio);
                // Update the layout parameters to position the pointing pin
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) clPinPoint.getLayoutParams();
                int halfWidthCl = clPinPointWidth/2;
                layoutParams.leftMargin = pinX - halfWidthCl;
                clPinPoint.setLayoutParams(layoutParams);

//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) clPinPoint.getLayoutParams();
                int clx = layoutParams.getMarginStart();


                return true;
            }
        });

    }




    RecyclerView rv_leveldata;
    ViewLevelAdapter viewLevelAdapter;
    ProgressBar progressLevel;

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_LEVEL_DATA) {
            LevelDataResponce rsp = (LevelDataResponce) response;

            rv_leveldata = findViewById(R.id.rv_leveldata);
            progressLevel = findViewById(R.id.progressLevel);

            ArrayList<Level> levelArrayList = new ArrayList<>();
            levelArrayList.addAll(rsp.getResult().getLevel());

            viewLevelAdapter = new ViewLevelAdapter(getApplicationContext(), levelArrayList);

            //   rv_leveldata.setHasFixedSize(true);
            rv_leveldata.setLayoutManager(new LinearLayoutManager(this));
            rv_leveldata.setAdapter(viewLevelAdapter);
            setupUI();
          /*  tvLevel.setText("Level" + " "+ rsp.getResult().getLastWeekReport().getLevel()); */
            tvLevel.setText("Level" + " "+ rsp.getResult().getCurrentWeekReport().getLevel());
          /*  lvlCurrent = rsp.getResult().getLastWeekReport().getLevel();  */
            lvlCurrent = rsp.getResult().getCurrentWeekReport().getLevel();
            Log.e("cscsdcs", String.valueOf(lvlCurrent));
            tvLevelLastWeek.setText("Lv"+" "+rsp.getResult().getLastWeekReport().getLevel());
            /*tvStartLvl.setText("Lv"+" "+rsp.getResult().getLastWeekReport().getLevel());*/
            tvStartLvl.setText("Lv"+" "+rsp.getResult().getCurrentWeekReport().getLevel());
          /*  int newLvl = rsp.getResult().getLastWeekReport().getLevel();
            int latestLvl = newLvl + 1;*/
            int newLvl = rsp.getResult().getCurrentWeekReport().getLevel();
            int latestLvl = newLvl + 1;
            tvEndLvl.setText("Lv"+" "+latestLvl);
            tvLevelCurrentWeek.setText("Lv"+" "+rsp.getResult().getCurrentWeekReport().getLevel());
            tvLastWeekBeans.setText(String.valueOf(rsp.getResult().getLastWeekReport().getTotalCoins()) );
            currentLevelPoints = rsp.getResult().getLastWeekReport().getTotalCoins();
            tvCurrentWeekBeans.setText(String.valueOf(rsp.getResult().getCurrentWeekReport().getTotalCoins()));
            thisWeekLevelPoints = rsp.getResult().getCurrentWeekReport().getTotalCoins();
           /* Log.e("totalHostCoins",new SessionManager(this).)*/
            for (Level level : levelArrayList) {
                Log.e("Level Info", "level: " + level.getLevel());
                if (level.getLevel().equals(lvlCurrent+1) ){
                    nextLevelPoints = level.getAmount();
                     break;
                }
            }
            for (Level level : levelArrayList) {
                Log.e("Level Info", "level: " + level.getLevel());
                if (level.getLevel().equals(lvlCurrent) ){
                    lastLevelPoints = level.getAmount();
                    break;
                }
            }

            Log.e("nextLevelPoints",String.valueOf(nextLevelPoints));

         /*   requiredPointsToNextLevel = nextLevelPoints - currentLevelPoints;*/
            requiredPointsToNextLevel = nextLevelPoints - thisWeekLevelPoints;
            Log.e("checkRequiredPoints", String.valueOf(requiredPointsToNextLevel));
            tvReachNextLvl.setText("Get "+requiredPointsToNextLevel+" beans to level up");
        /*    progressPercentage =  currentLevelPoints * 100 / nextLevelPoints;*/
            /*progressPercentage =  thisWeekLevelPoints * 100 / nextLevelPoints;*/
      /*      progressPercentage = ((float) requiredPointsToNextLevel / nextLevelPoints) * 100;*/
            diffCurrentToLastPoints = nextLevelPoints - lastLevelPoints;
            Log.e("bbddddd",String.valueOf(diffCurrentToLastPoints));
       /*   progressPercentage =  thisWeekLevelPoints * 100 / nextLevelPoints;*/
           /* progressPercentage = thisWeekLevelPoints * 100/ diffCurrentToLastPoints;*/
            if (nextLevelPoints > 0) {
                progressPercentage = (1 - (float) requiredPointsToNextLevel / diffCurrentToLastPoints) * 100;
            } else {
                // Handle the case where nextLevelPoints is 0 to avoid division by zero
                progressPercentage = 100;
            }

            Log.e("checkPercentage",String.valueOf(progressPercentage));

         percentageRounded =  Math.round(progressPercentage);
         linearProgressIndicator.setProgress(percentageRounded);
         tvPinPoints.setText(String.valueOf(thisWeekLevelPoints));
         updatePinPosition(percentageRounded);

            Log.e("checkPercentageRounded",String.valueOf(percentageRounded));


            ((TextView) findViewById(R.id.tv_lastweekearning)).setText(
                    rsp.getResult().getLastWeekReport().getTotalCoins() + ""
            );


            ((TextView) findViewById(R.id.tv_thisweekearning)).setText(
                    rsp.getResult().getCurrentWeekReport().getTotalCoins() + ""
            );

//            calcLevelIndi(rsp.getResult().getLastWeekReport().getTotalCoins(), 0);
//            calcLevelIndi(rsp.getResult().getCurrentWeekReport().getTotalCoins(), 1);
//            calcLevel(rsp.getResult().getCurrentWeekReport().getTotalCoins(), rsp.getResult().getLastWeekReport().getTotalCoins());
            findViewById(R.id.rl_profilelevel).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.level3_trans_relative));
            ((TextView) findViewById(R.id.tv_profilelevel)).setText("Lv" + new SessionManager(this).getHostLevel());

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

        progressLevel.setMin(min);
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