package com.privatepe.app.response.metend.store;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.privatepe.app.R;
import com.privatepe.app.databinding.ActivityStoreBinding;
import com.privatepe.app.response.metend.store.adapters.MainUpperTabPagerAdapter;
import com.privatepe.app.response.metend.store.dialog.StoreItemPurchaseDialog;
import com.privatepe.app.response.metend.store.dialog.UseOrRemoveItemDialog;
import com.privatepe.app.response.metend.store.fragments.MineTabFragment;
import com.privatepe.app.response.metend.store.fragments.StoreTabFragment;
import com.privatepe.app.response.metend.store.interfaces.OnStoreItemClickListener;
import com.privatepe.app.response.metend.store.model.MainTabsModel;
import com.privatepe.app.utils.BaseActivity;

import java.util.ArrayList;

public class StoreActivity extends BaseActivity {
    private ActivityStoreBinding binding;
    private final ArrayList<MainTabsModel> upperTabsFragmentList = new ArrayList<>();
    private OnStoreItemClickListener onStoreItemClickListener;

    private int CURRENT_SELECTED_TAB=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), false);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_store);
        binding.backArrow.setOnClickListener(v -> onBackPressed());
        Log.e("BackgroundCrash_Test", "StoreActivity onCreate: " );
        setupUpperTabs();
    }

    private void setupUpperTabs() {
        upperTabsFragmentList.clear();
        upperTabsFragmentList.add(new MainTabsModel(new StoreTabFragment(), "Store"));
        upperTabsFragmentList.add(new MainTabsModel(new MineTabFragment(), "Mine"));


        MainUpperTabPagerAdapter adapter = new MainUpperTabPagerAdapter(getSupportFragmentManager(), upperTabsFragmentList, new OnStoreItemClickListener() {
            @Override
            public void onStoreItemClick(com.privatepe.app.response.metend.store_list.StoreItemModel storeItemModel, String type) {
                if (type.equals("StoreTab")) {
                    new StoreItemPurchaseDialog(StoreActivity.this, storeItemModel);
                } else if (type.equals("MineTab")) {
                    new UseOrRemoveItemDialog(StoreActivity.this, storeItemModel);
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel dest, int flags) {

            }
        });
/*

        MainUpperTabPagerAdapter adapter = new MainUpperTabPagerAdapter(getSupportFragmentManager(), upperTabsFragmentList, (OnStoreItemClickListener) (storeItemModel, type) -> {
          //  Log.e("StoreActivity", "onItemClickListener: item clicked. item  " + new Gson().toJson(storeItemModel) + " \n  type " + type);
            if (type.equals("StoreTab")) {
                new StoreItemPurchaseDialog(StoreActivity.this, storeItemModel);
            } else if (type.equals("MineTab")) {
                new UseOrRemoveItemDialog(StoreActivity.this, storeItemModel);
            }
        });

*/

        Log.e("BackgroundCrash_Test", "StoreActivity onCreate:setupUpperTabs " );
        binding.mainViewPager.setAdapter(adapter);
        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager);


        for (int i = 0; i < binding.mainTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = binding.mainTabLayout.getTabAt(i);
            if (tab != null) {
                TextView tabTextView = new TextView(this);
                tab.setCustomView(tabTextView);
                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                tabTextView.setText(tab.getText());
                tabTextView.setTypeface(null, Typeface.BOLD);
                if (i == 0) {
                    tabTextView.setTextSize(17);
                    tabTextView.setTextColor(getResources().getColor(R.color.store_main_tab_selcted_color));
                } else {
                    tabTextView.setTextSize(16);
                    tabTextView.setTextColor(getResources().getColor(R.color.store_unselected_tab_text_color));
                }

            }

        }

        binding.mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewGroup vg = (ViewGroup) binding.mainTabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildsCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextSize(17);
                        ((TextView) tabViewChild).setTypeface(null, Typeface.BOLD);
                        ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.store_main_tab_selcted_color));
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ViewGroup vg = (ViewGroup) binding.mainTabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildsCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextSize(16);
                        ((TextView) tabViewChild).setTypeface(null, Typeface.BOLD);
                        ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.store_unselected_tab_text_color));
                    }
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        binding.mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CURRENT_SELECTED_TAB=position;
              //  Log.e("StoreActivity_Crash_Test", "onPageSelected: "+CURRENT_SELECTED_TAB );

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        binding.mainViewPager.setNestedScrollingEnabled(true);

       /*
         binding.mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("InternalTabScrollProblem", "onPageScrolled: position  "+position+" pixelsOffset  "+positionOffset+" offset "+positionOffsetPixels );
            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        */


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("BackgroundCrash_Test", "StoreActivity onPause " );
    }

}