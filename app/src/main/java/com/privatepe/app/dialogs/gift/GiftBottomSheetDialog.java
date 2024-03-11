package com.privatepe.app.dialogs.gift;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.privatepe.app.Interface.GiftSelectListener;
import com.privatepe.app.R;
import com.privatepe.app.Zego.VideoChatZegoActivity;
import com.privatepe.app.Zego.VideoChatZegoActivityMet;
import com.privatepe.app.adapter.gift.GiftTabPagerAdapter;
import com.privatepe.app.fragments.gift.GiftTabFragment;
import com.privatepe.app.response.newgiftresponse.NewGift;
import com.privatepe.app.response.newgiftresponse.NewGiftResult;
import com.privatepe.app.utils.CustomViewPager;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class GiftBottomSheetDialog extends BottomSheetDialogFragment {

    private ArrayList<NewGiftResult> giftResponseList = new ArrayList<>();
    private TextView totalCoin, SendGift;
    private LinearLayout mytotalcoins;
    TabLayout tabLayout;
    CustomViewPager tabViewPager;

    private List<GiftTabFragment> giftTabFragmentArrayList = new ArrayList<>();

    private List<String> NameList = new ArrayList<>();

    // String[] a = {"Popular", "Lucky", "Test", "Test1"};

    String TAG = "GiftBottomSheetDialog";

    NewGift GiftData = null;

    private GiftSelectListener giftSelectListener;
    private VideoChatZegoActivity videoChatZegoActivity;
    private VideoChatZegoActivityMet videoChatZegoActivityMet;


    public GiftBottomSheetDialog(VideoChatZegoActivity ctx, ArrayList<NewGiftResult> giftResponse, GiftSelectListener listener) {
        giftResponseList = giftResponse;
        giftSelectListener = listener;
        videoChatZegoActivity = ctx;
    }

    public GiftBottomSheetDialog(VideoChatZegoActivityMet ctx, ArrayList<NewGiftResult> giftResponse, GiftSelectListener listener) {
        giftResponseList = giftResponse;
        giftSelectListener = listener;
        videoChatZegoActivityMet = ctx;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_bottomsheet_dialog, container, false);
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable());

        totalCoin = view.findViewById(R.id.total_coin);
        mytotalcoins = view.findViewById(R.id.mytotalcoins);
        SendGift = view.findViewById(R.id.send_btn);
        tabLayout = view.findViewById(R.id.tab_layout);
        tabViewPager = view.findViewById(R.id.tab_viewPager);


        if (new SessionManager(getContext()).getGender().equalsIgnoreCase("female")) {
            view.findViewById(R.id.sendLay).setVisibility(View.GONE);

        } else {
            view.findViewById(R.id.sendLay).setVisibility(View.VISIBLE);


        }
        // tabViewPager.setNestedScrollingEnabled(true);

        for (int i = 0; i < giftResponseList.size(); i++) {
            giftTabFragmentArrayList.add(new GiftTabFragment());
            // NameList.add(a[i]);
        }
        setUpTabs();

        totalCoin.setText("" + new SessionManager(getContext()).getUserWallet());

        mytotalcoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  new InsufficientCoinsMyaccount(getContext(), 2, new SessionManager(getContext()).getUserWallet());
                //  videoChatZegoActivity.RechargePopup(0);
            }
        });


        SendGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendGift.setEnabled(false);

                if (GiftData != null) {
                    Log.e(TAG, "onClick: Gift Name " + GiftData.getGift_name());
                    int bal = Integer.parseInt(totalCoin.getText().toString());
                    if (bal > (long) GiftData.getAmount()) {
                        giftSelectListener.OnGiftSelect(GiftData);
                        //  dismiss();
                    } else {
                        Toast.makeText(getContext(), "Balance to low to send gift", Toast.LENGTH_LONG).show();
                        getWalbalance();
                        //  new InsufficientCoinsMyaccount(getContext(), 2, new SessionManager(getContext()).getUserWallet());
                    }
                } else {
                    getWalbalance();
                    Toast.makeText(getContext(), "Please Choose a gift.", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;

    }

    public void getWalbalance(Integer bal) {
        totalCoin.setText(String.valueOf(bal));
        SendGift.setEnabled(true);
    }

    public void getWalbalance() {
        SendGift.setEnabled(true);
    }

    private void setUpTabs() {

        GiftTabPagerAdapter adapter = new GiftTabPagerAdapter(getChildFragmentManager(), giftTabFragmentArrayList, giftResponseList, new GiftSelectListener() {
            @Override
            public void OnGiftSelect(NewGift giftData) {
                Log.e("NEWGIFTTESTT11", "OnGiftSelect: " + new Gson().toJson(giftData));
                GiftData = giftData;
            }
        });

        // tabViewPager.setOffscreenPageLimit(0);

        tabViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(tabViewPager);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);

            if (tab != null) {
                TextView tabTextView = new TextView(getContext());

                tab.setCustomView(tabTextView);

                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                tabTextView.setText(tab.getText());

                if (i == 0) {
                    tabTextView.setTextSize(17);
                    tabTextView.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    tabTextView.setTextSize(16);
                    tabTextView.setTextColor(Color.parseColor("#616161"));
                }

            }

        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildsCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextSize(17);
                        ((TextView) tabViewChild).setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildsCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextSize(16);
                        ((TextView) tabViewChild).setTextColor(Color.parseColor("#616161"));
                    }
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


       /*      new TabLayoutMediator(tabLayout, tabViewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText("" + NameList.get(position));
            }
        }).attach();*/
       /*      tabViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });*/


    }


}


