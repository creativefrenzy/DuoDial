package com.privatepe.app.nGiftSec.giftFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zeeplive.app.R;
import com.zeeplive.app.activity.party.adapter.GiftGroupAdapter;
import com.zeeplive.app.activity.party.model.PartyModel;
import com.zeeplive.app.activity.party.utils.CommonMethods;
import com.zeeplive.app.activity.party.utils.custom_switch.LabeledSwitch;
import com.zeeplive.app.dialog.InsufficientCoins;
import com.zeeplive.app.model.PKUserModel;
import com.zeeplive.app.nGiftSec.giftAdapter.TotalCoinsAdapter;
import com.zeeplive.app.nGiftSec.helper.MyViewPager;
import com.zeeplive.app.nGiftSec.listner.GiftAniListner;
import com.zeeplive.app.nGiftSec.listner.LuckyCoinListner;
import com.zeeplive.app.nGiftSec.listner.MyTabListner;
import com.zeeplive.app.response.N_Gift.CoinSelectionModel;
import com.zeeplive.app.response.N_Gift.Gift;
import com.zeeplive.app.response.N_Gift.GiftResult;
import com.zeeplive.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

public class BottomSheetDialog extends BottomSheetDialogFragment implements GiftGroupAdapter.GroupGiftListener {
    private ArrayList<GiftResult> alMainTab = new ArrayList<>();

    private Gift selectedGifts;

    private RelativeLayout mainRelative;
    private ArrayList<CoinSelectionModel> alTotalCoins = new ArrayList<>();
    private RecyclerView rvTotalCoins;
    private TotalCoinsAdapter totalCoinsAdapter;

    private MyTabListner callback;
    private AppCompatTextView sendGift, availableCoinTv, countDownTv;
    LinearLayoutCompat diamondLayout;
    ConstraintLayout sendLl;

    public static int MAIN_ARRAY_SELECTION = -1;
    public static int SUB_ARRAY_SELECTION = -1;
    public static int ADAPTER_POSITION = -1;
    public static int GROUP_COIN_SELECTION_POSTION = 0;

    private List<TabLayoutFragment> tablayoutList = new ArrayList<>();
    LuckyCoinListner luckyCoinListner;
    GiftAniListner giftAniListner;
    ArrayList<GiftResult> giftResultArrayList;
    private LinearLayoutCompat rlCoins;
    boolean isVisible, isVideoCall = false;

    View giftPKLayout, giftGroupLayout;
    RecyclerView groupRecycler;
    LabeledSwitch switchBtn;
    GiftGroupAdapter giftGroupAdapter;
    private List<PartyModel> selectedUserLists = new ArrayList<>();
    private List<PartyModel> giftSelectedUserLists = new ArrayList<>();
    private String ownerId = "";
    private int giftLayoutHeight = 350;
    public static onCancelListenerDialog onCancelListener;

    private ConstraintLayout leftUserConstraint, rightUserConstraint;
    private CircleImageView leftUserImg, rightUserImg;
    private AppCompatTextView leftUserName, rightUserName;
    private CommonMethods commonMethods;
    private PKUserModel pkUserModel;
    private boolean isPK = false;
    private PartyModel pkLeftUserModel;
    private PartyModel pkRightUserModel;

    public BottomSheetDialog(LuckyCoinListner luckyCoinListner, ArrayList<GiftResult> giftResult, GiftAniListner giftAniListner, boolean isVisible) {
        this.luckyCoinListner = luckyCoinListner;
        this.giftResultArrayList = giftResult;
        this.giftAniListner = giftAniListner;
        this.isVisible = isVisible;
    }

    public BottomSheetDialog(LuckyCoinListner luckyCoinListner, ArrayList<GiftResult> giftResult, GiftAniListner giftAniListner, boolean isVisible, onCancelListenerDialog onCancelListener) {
        this.luckyCoinListner = luckyCoinListner;
        this.giftResultArrayList = giftResult;
        this.giftAniListner = giftAniListner;
        this.isVisible = isVisible;
        this.onCancelListener = onCancelListener;
    }

    public BottomSheetDialog(LuckyCoinListner luckyCoinListner, ArrayList<GiftResult> giftResult, GiftAniListner giftAniListner, HashMap<Object, PartyModel> selectedUserLists,
                             String ownerId, boolean isVisible, onCancelListenerDialog onCancelListener) {
        this.luckyCoinListner = luckyCoinListner;
        this.giftResultArrayList = giftResult;
        this.giftAniListner = giftAniListner;
        this.selectedUserLists = new ArrayList<PartyModel>(selectedUserLists.values());
        this.ownerId = ownerId;
        this.isVisible = isVisible;
        this.onCancelListener = onCancelListener;
        Log.e("CHeck_GiftDialog", "BottomSheet Size : " + selectedUserLists.size());
    }

    public BottomSheetDialog(LuckyCoinListner luckyCoinListner, ArrayList<GiftResult> giftResult, GiftAniListner giftAniListner,
                             boolean isVisible, PKUserModel pkUserModel, boolean isVideoCall, onCancelListenerDialog onCancelListener) {
        this.luckyCoinListner = luckyCoinListner;
        this.giftResultArrayList = giftResult;
        this.giftAniListner = giftAniListner;
        this.isVisible = isVisible;
        this.pkUserModel = pkUserModel;
        this.isVideoCall = isVideoCall;
        this.onCancelListener = onCancelListener;
    }

    public BottomSheetDialog(LuckyCoinListner luckyCoinListner, ArrayList<GiftResult> giftResultArrayList, GiftAniListner giftAniListner, boolean isVisible, PKUserModel pkUserModel, boolean isVideoCall) {
        this.luckyCoinListner = luckyCoinListner;
        this.giftResultArrayList = giftResultArrayList;
        this.giftAniListner = giftAniListner;
        this.isVisible = isVisible;
        this.pkUserModel = pkUserModel;
        this.isVideoCall = isVideoCall;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.GiftBottomSheetDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable());

        commonMethods = new CommonMethods();
        pkLeftUserModel = new PartyModel();
        pkRightUserModel = new PartyModel();

        mainRelative = v.findViewById(R.id.mainRelative);
        rvTotalCoins = v.findViewById(R.id.rvTotalCoins);
        availableCoinTv = v.findViewById(R.id.availableCoinTv);
        countDownTv = v.findViewById(R.id.countDownTv);
        sendGift = v.findViewById(R.id.sendTv);
        diamondLayout = v.findViewById(R.id.diamondLayout);
        sendLl = v.findViewById(R.id.sendLl);
        rlCoins = v.findViewById(R.id.rlCoins);
        giftPKLayout = v.findViewById(R.id.giftPKLayout);
        leftUserConstraint = giftPKLayout.findViewById(R.id.leftUserConstraint);
        rightUserConstraint = giftPKLayout.findViewById(R.id.rightUserConstraint);
        leftUserImg = giftPKLayout.findViewById(R.id.leftUserImg);
        rightUserImg = giftPKLayout.findViewById(R.id.rightUserImg);
        leftUserName = giftPKLayout.findViewById(R.id.leftUserName);
        rightUserName = giftPKLayout.findViewById(R.id.rightUserName);
        giftGroupLayout = v.findViewById(R.id.giftGroupLayout);
        groupRecycler = giftGroupLayout.findViewById(R.id.groupRecycler);
        switchBtn = giftGroupLayout.findViewById(R.id.switchBtn);

        if (pkUserModel != null) {
            isPK = true;
            giftPKLayout.setVisibility(View.VISIBLE);
            leftUserName.setText(pkUserModel.ownHostName);
            rightUserName.setText(pkUserModel.remoteHostName);
            commonMethods.imageLoaderView(getActivity(), leftUserImg, pkUserModel.ownHostImage);
            commonMethods.imageLoaderView(getActivity(), rightUserImg, pkUserModel.remoteHostImage);

            pkLeftUserModel.user_id = pkUserModel.ownHostID;
            pkLeftUserModel.user_name = pkUserModel.ownHostName;
            pkLeftUserModel.user_image = pkUserModel.ownHostImage;
            giftSelectedUserLists.add(pkLeftUserModel);

            leftUserConstraint.setSelected(false);
            rightUserConstraint.setSelected(true);
        } else {
            isPK = false;
            giftPKLayout.setVisibility(View.GONE);
        }

        leftUserConstraint.setOnClickListener(view -> {
            if (leftUserConstraint.isSelected()) {
                leftUserConstraint.setBackgroundResource(R.drawable.bg_left_rounded);
                leftUserConstraint.setBackgroundTintList(null);
                leftUserConstraint.setSelected(false);
            } else {
                leftUserConstraint.setBackgroundResource(R.drawable.bg_left_rounded);
                leftUserConstraint.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.gray)));
                leftUserConstraint.setSelected(true);

                giftSelectedUserLists.remove(pkLeftUserModel);
            }
        });

        rightUserConstraint.setOnClickListener(view -> {
            if (rightUserConstraint.isSelected()) {
                rightUserConstraint.setBackgroundResource(R.drawable.bg_right_rounded);
                rightUserConstraint.setBackgroundTintList(null);
                rightUserConstraint.setSelected(false);

                pkRightUserModel.user_id = pkUserModel.remoteHostID;
                pkRightUserModel.user_name = pkUserModel.remoteHostName;
                pkRightUserModel.user_image = pkUserModel.remoteHostImage;
                giftSelectedUserLists.add(pkRightUserModel);
            } else {
                rightUserConstraint.setBackgroundResource(R.drawable.bg_right_rounded);
                rightUserConstraint.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.gray)));
                rightUserConstraint.setSelected(true);

                giftSelectedUserLists.remove(pkRightUserModel);
            }
        });

        try {
            final float scale = getContext().getResources().getDisplayMetrics().density;
            if (ownerId != null && !ownerId.equals("") || isPK) {
                giftLayoutHeight = (int) (380 * scale + 0.5f);
            } else {
                giftLayoutHeight = (int) (350 * scale + 0.5f);
            }
        } catch (Exception e) {
            giftLayoutHeight = 350;
            e.printStackTrace();
        }

        try {
            mainRelative.getLayoutParams().height = giftLayoutHeight;
            mainRelative.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        switchBtn.setMode(true);

        if (isVisible) {
            diamondLayout.setVisibility(View.VISIBLE);
            sendLl.setVisibility(View.VISIBLE);
        } else {
            diamondLayout.setVisibility(View.GONE);
            sendLl.setVisibility(View.GONE);
        }

        switchBtn.setOnToggledListener((toggleableView, isOn) -> {
            giftGroupAdapter.setSelectAllUser(isOn);
            try {
                if (isOn) {
                    if (giftSelectedUserLists.size() > 0)
                        giftSelectedUserLists.clear();
                    giftSelectedUserLists.addAll(selectedUserLists);
                } else {
                    if (giftSelectedUserLists.size() > 0)
                        giftSelectedUserLists.clear();
                    giftSelectedUserLists.add(selectedUserLists.get(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        setUpRecycler();

        if (selectedUserLists.size() > 0) {
            giftGroupLayout.setVisibility(View.VISIBLE);
//            rvTotalCoins.setVisibility(View.VISIBLE);
        } else {
            giftGroupLayout.setVisibility(View.GONE);
//            rvTotalCoins.setVisibility(View.GONE);
        }

        if (isVideoCall) {
            sendGift.setOnClickListener(view -> {
                try {
                    checkGift(giftSelectedUserLists);
                } catch (Exception e) {
                    e.printStackTrace();
                    dismiss();
                }
            });
        } else {
            sendGift.setOnClickListener(view -> {
                try {
                    if (ownerId != null && !ownerId.equals("")) {
                        if (giftSelectedUserLists.size() > 0) {
                            Log.e("CHeck_Gift", "BottomSheet Click ==> : " + new Gson().toJson(giftSelectedUserLists));
                            checkGift(giftSelectedUserLists);
                        } else
                            Toast.makeText(getActivity(), "Please select any one person", Toast.LENGTH_SHORT).show();
                    } else {
                        if (isPK) {
                            if (pkUserModel != null) {
                                checkGift(giftSelectedUserLists);
                            } else
                                Toast.makeText(getActivity(), "Please select any one person", Toast.LENGTH_SHORT).show();
                        } else {
                            checkGift(null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dismiss();
                }
            });
        }

        /*sendGift.setOnClickListener(view -> {
            try {
                if (ownerId != null && !ownerId.equals("")) {
                    if (giftSelectedUserLists.size() > 0) {
                        Log.e("CHeck_Gift", "BottomSheet Click ==> : "+new Gson().toJson(giftSelectedUserLists));
                        if (selectedGifts != null) {
                            giftAniListner.onSelectClick(selectedGifts, alTotalCoins.get(GROUP_COIN_SELECTION_POSTION).getName(), giftSelectedUserLists,countDownTv);
                            sendGift.setVisibility(View.INVISIBLE);
                            //countDownTv.setVisibility(View.VISIBLE);
                            sendLl.setVisibility(View.INVISIBLE);
                            CountDownTimer();
                        } else
                            Toast.makeText(getActivity(), "Please select any one Gift", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Please select any one person", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedGifts != null) {
                        giftAniListner.onClick(selectedGifts);
                       // sendGift.setVisibility(View.INVISIBLE);
                        countDownTv.setVisibility(View.VISIBLE);
                        sendLl.setVisibility(View.INVISIBLE);

                        CountDownTimer();
                    } else {
                        Toast.makeText(getActivity(), "Please select any one Gift", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                dismiss();
            }
        });*/
        countDownTv.setOnClickListener(view -> {
            Vibrator vibrateNow = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            try {
                if (ownerId != null && !ownerId.equals("")) {
                    if (giftSelectedUserLists.size() > 0) {
                        Log.e("CHeck_Gift", "BottomSheet Click ==> : " + new Gson().toJson(giftSelectedUserLists));
                        checkGift(giftSelectedUserLists);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrateNow.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrateNow.vibrate(500);
                        }
                    } else
                        Toast.makeText(getActivity(), "Please select any one person", Toast.LENGTH_SHORT).show();
                } else {
                    if (isPK) {
                        if (pkUserModel != null) {
                            checkGift(giftSelectedUserLists);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrateNow.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                vibrateNow.vibrate(500);
                            }
                        } else
                            Toast.makeText(getActivity(), "Please select any one person", Toast.LENGTH_SHORT).show();
                    } else {
                        checkGift(null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrateNow.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrateNow.vibrate(500);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                dismiss();
            }
         /*   try {
                if (ownerId != null && !ownerId.equals("")) {
                    if (giftSelectedUserLists.size() > 0) {
                        Log.e("CHeck_Gift", "BottomSheet Click ==> : "+new Gson().toJson(giftSelectedUserLists));
                        if (selectedGifts != null) {
                            giftAniListner.onSelectClick(selectedGifts, alTotalCoins.get(GROUP_COIN_SELECTION_POSTION).getName(), giftSelectedUserLists,countDownTv);
                            sendGift.setVisibility(View.INVISIBLE);
                            //countDownTv.setVisibility(View.VISIBLE);
                            sendLl.setVisibility(View.INVISIBLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrateNow.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                vibrateNow.vibrate(500);
                            }
                            CountDownTimer();
                        } else
                            Toast.makeText(getActivity(), "Please select any one Gift", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Please select any one person", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedGifts != null) {
                        giftAniListner.onClick(selectedGifts);
                        sendGift.setVisibility(View.INVISIBLE);
                        //countDownTv.setVisibility(View.VISIBLE);
                        sendLl.setVisibility(View.INVISIBLE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrateNow.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrateNow.vibrate(500);
                        }
                        CountDownTimer();
                    } else {
                        Toast.makeText(getActivity(), "Please select any one Gift", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                dismiss();
            }*/
        });

        callback = new MyTabListner() {
            @Override
            public void onClick(int mainArrayPosition, int subArrayPosition, int adapterPosition, boolean isSelected, Gift selectedGift) {
                MAIN_ARRAY_SELECTION = mainArrayPosition;
                SUB_ARRAY_SELECTION = subArrayPosition;
                ADAPTER_POSITION = adapterPosition;
                selectedGifts = selectedGift;
                disposables();
//Log.e("IsSelectedGiftDefault","Yes"+selectedGift.getAmount());
                try {
                    if (ownerId != null /*&& !ownerId.equals("")*/) {
                        if (selectedGift.getAmount() <= 1000) {
                            rvTotalCoins.setVisibility(View.VISIBLE);
                        } else {
                            GROUP_COIN_SELECTION_POSTION = 0;
                            rvTotalCoins.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                availableCoinTv.setText(String.valueOf(new SessionManager(getContext()).getUserWallet()));
                //makeAPIcall(v);

                CoinSelectionModel coinSelectionModel;
                coinSelectionModel = new CoinSelectionModel("1", 1, true);
                alTotalCoins.add(coinSelectionModel);
                coinSelectionModel = new CoinSelectionModel("11", 11, false);
                alTotalCoins.add(coinSelectionModel);
                coinSelectionModel = new CoinSelectionModel("33", 33, false);
                alTotalCoins.add(coinSelectionModel);
                coinSelectionModel = new CoinSelectionModel("77", 77, false);
                alTotalCoins.add(coinSelectionModel);

                alTotalCoins.get(0).setSelected(true);
                GROUP_COIN_SELECTION_POSTION = 0;
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                totalCoinsAdapter = new TotalCoinsAdapter(alTotalCoins, new TotalCoinsAdapter.CoinSelectListner() {
                    @Override
                    public void onSelectedPosition(int position) {
                        for (int i = 0; i < alTotalCoins.size(); i++) {
                            alTotalCoins.get(i).setSelected(false);
                        }
                        if (alTotalCoins.get(position).isSelected()) {
                            alTotalCoins.get(position).setSelected(true);
                        } else {
                            for (int i = 0; i < alTotalCoins.size(); i++) {
                                alTotalCoins.get(i).setSelected(false);
                            }
                            alTotalCoins.get(position).setSelected(true);
                            if (MAIN_ARRAY_SELECTION != -1 && SUB_ARRAY_SELECTION != -1 && ADAPTER_POSITION != -1) {
                                tablayoutList.get(MAIN_ARRAY_SELECTION).callUpdatePrice(alTotalCoins.get(position).getName());
                            }
                        }
                        GROUP_COIN_SELECTION_POSTION = position;
                        totalCoinsAdapter.notifyDataSetChanged();
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false);
                rvTotalCoins.setLayoutManager(mLayoutManager);
                rvTotalCoins.setAdapter(totalCoinsAdapter);
//        rvTotalCoins.setAdapter(totalCoinsAdapter);
            }
        }.execute();

        setData(v);

        diamondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                new InsufficientCoins(getContext(), 2, 25);

               /* if (new SessionManager(getContext()).getGender().equals("male")) {
                    new InsufficientCoins(getContext(), 2, 25);

                }*/
            }
        });

        return v;
    }

    private void checkGift(List<PartyModel> giftSelectedUserLists) {
        if (selectedGifts != null) {
            Log.e("GiftTypeAnimation", " " + selectedGifts.getIsAnimated() + " " + selectedGifts.getAnimationType());
            countDownTv.setVisibility(View.VISIBLE);

            if (isVideoCall)
              //  giftAniListner.onClick(selectedGifts);
                giftAniListner.onSelectClick(selectedGifts, alTotalCoins.get(GROUP_COIN_SELECTION_POSTION).getName(), giftSelectedUserLists, countDownTv);
            else
                giftAniListner.onSelectClick(selectedGifts, alTotalCoins.get(GROUP_COIN_SELECTION_POSTION).getName(), giftSelectedUserLists, countDownTv);
            sendGift.setVisibility(View.INVISIBLE);
            CountDownTimer();
            if (selectedGifts.getAnimationType() != 0) {
                dismiss();
            }
        } else
            Toast.makeText(getActivity(), "Please select any one Gift", Toast.LENGTH_SHORT).show();
    }

    private void setUpRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        groupRecycler.setLayoutManager(layoutManager);
        giftGroupAdapter = new GiftGroupAdapter(getActivity(), selectedUserLists, BottomSheetDialog.this, ownerId);
        groupRecycler.setAdapter(giftGroupAdapter);
        setNotifyData();
    }

    private void setNotifyData() {
        groupRecycler.getRecycledViewPool().clear();
        giftGroupAdapter.notifyDataSetChanged();
    }

    private void setData(View v) {
        TabLayout tabLayout = v.findViewById(R.id.tab_layout);
        MyViewPager viewPager = v.findViewById(R.id.pager);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                alMainTab = new ArrayList<>();
                alMainTab.addAll(giftResultArrayList);
                for (int i = 0; i < alMainTab.size(); i++) {
                    tablayoutList.add(new TabLayoutFragment());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                //loadArrayList(model);

                DemoCollectionPagerAdapter adapter = new DemoCollectionPagerAdapter(getChildFragmentManager());
                viewPager.setOffscreenPageLimit(8);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);

                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            }
        }.execute();


    }

    @Override
    public void onSelectedGiftUser(List<PartyModel> selectedUserItems) {
        Log.e("giftSetLog456789", "onSelectedGiftUser selectedUserItems Size Before : " + selectedUserItems.size());
        giftSelectedUserLists = selectedUserItems;
        Log.e("giftSetLog456789", "onSelectedGiftUser selectedUserLists Size After : " + giftSelectedUserLists.size());
        if (selectedUserLists.size() == selectedUserItems.size()) {
            switchBtn.setOn(true);
        } else {
            switchBtn.setOn(false);
        }
    }


    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        try {
            onCancelListener.onCancelGiftDialog(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            TabLayoutFragment fragment = tablayoutList.get(i);
            Bundle args = new Bundle();
            args.putSerializable("main_list", alMainTab);
            args.putSerializable("main_position", i);
            args.putSerializable("listner", callback);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return alMainTab.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return alMainTab.get(position).getName();
        }
    }

   /* private void makeAPIcall(View v) {
        Call<GiftModel> call = RetrofitClient.getInstance().getMyApi().getGiftsData();
        call.enqueue(new Callback<GiftModel>() {
            @Override
            public void onResponse(Call<GiftModel> call, Response<GiftModel> response) {
                GiftModel model = response.body();
                //Load array for tabs
                if (model.isStatus()) {
                    alMainTab = new ArrayList<>();
                    alMainTab.addAll(model.getTab());
                    for (int i = 0; i < alMainTab.size(); i++) {
                        tablayoutList.add(new TabLayoutFragment());
                    }
                    setData(v, model);
                } else {
                    Toast.makeText(getActivity(), "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GiftModel> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }*/

    Disposable disposable;

    public final void CountDownTimer() {
        Disposable bVar = disposable;
        if (bVar != null && !bVar.isDisposed()) {
            disposable.dispose();
        }
        disposable = Observable.interval(0L, 200L, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Throwable {
                setCountDown(aLong);
            }
        });
    }

    public void setCountDown(Long l2) throws Exception {
        long longValue = 30 - l2.longValue();
        if (longValue <= 0) {
            disposables();
        } else {
            countDownTv.setText(String.valueOf(longValue));
        }
    }

    public final void disposables() {
        Disposable bVar = disposable;
        if (bVar != null && !bVar.isDisposed()) {
            disposable.dispose();
        }
        countDownTv.setVisibility(View.GONE);
        sendGift.setVisibility(View.VISIBLE);
        sendLl.setVisibility(View.VISIBLE);

        try {
            availableCoinTv.setText(String.valueOf(new SessionManager(getContext()).getUserWallet()));
        } catch (Exception e) {

        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.e("Check_JKGift", "dismiss");
        if (giftSelectedUserLists.size() > 0)
            giftSelectedUserLists.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Check_JKGift", "onDetach");
        if (giftSelectedUserLists.size() > 0)
            giftSelectedUserLists.clear();
    }

    public interface onCancelListenerDialog {
        void onCancelGiftDialog(Boolean isCancelled);
    }
}