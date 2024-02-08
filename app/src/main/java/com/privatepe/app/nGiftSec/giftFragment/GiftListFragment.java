package com.privatepe.app.nGiftSec.giftFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeplive.app.R;
import com.zeeplive.app.nGiftSec.giftAdapter.GiftAdapter;
import com.zeeplive.app.nGiftSec.listner.MyTabListner;
import com.zeeplive.app.response.N_Gift.Gift;

import java.util.ArrayList;
import java.util.List;

public class GiftListFragment extends Fragment {
    private RecyclerView rvGifts;
    private GiftAdapter giftAdapter;
    private List<Gift> alGifts = new ArrayList<>();
    private int currentTabPosition = 0;
    private int currentItemPosition = 0;
    private MyTabListner callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gift_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        int selectedTabPosition = BottomSheetDialog.MAIN_ARRAY_SELECTION;
        int selectedItemPosition = BottomSheetDialog.SUB_ARRAY_SELECTION;

        Log.d("==>", " Tab Array position  " + currentTabPosition + " Item array position " + currentItemPosition);
        Log.d("==>", " Selected Tab position  " + selectedTabPosition + " Selected Item Araay position " + selectedItemPosition);

        /*for (int i = 0; i < alGifts.size(); i++) {
            if (alGifts.get(i).isSelected()) {
                if (currentTabPosition != selectedTabPosition) {
                    alGifts.get(i).setSelected(false);
                } else if (selectedItemPosition != currentItemPosition) {
                    alGifts.get(i).setSelected(false);

                }
            }
        }
        giftAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /*alGifts.addAll((ArrayList<Gift>) getArguments().getSerializable("sublist"));

        currentTabPosition = getArguments().getInt("main_position");
        currentItemPosition = getArguments().getInt("sub_list_position");
        callback = (MyTabListner) getArguments().getSerializable("listner");*/
        rvGifts = view.findViewById(R.id.rvGifts);
        rvGifts.setHasFixedSize(true);
        rvGifts.setItemViewCacheSize(20);
        rvGifts.setDrawingCacheEnabled(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                alGifts.addAll((ArrayList<Gift>) getArguments().getSerializable("sublist"));

                currentTabPosition = getArguments().getInt("main_position");
                currentItemPosition = getArguments().getInt("sub_list_position");
                callback = (MyTabListner) getArguments().getSerializable("listner");
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                giftAdapter = new GiftAdapter(alGifts, new GiftAdapter.GiftSelectListner() {
                    @Override
                    public void onSelectedPosition(int position) {
                        if (alGifts.get(position).isSelected()) {
                            alGifts.get(position).setSelected(false);

                            callback.onClick(-1, -1, -1, true, null);
                        } else {
                            for (int i = 0; i < alGifts.size(); i++) {
                                alGifts.get(i).setSelected(false);
                            }
                            alGifts.get(position).setSelected(true);
                            callback.onClick(currentTabPosition, currentItemPosition, position, true, alGifts.get(position));

                        }
                        giftAdapter.notifyDataSetChanged();
                    }
                });
                rvGifts.setAdapter(giftAdapter);
                for (int i = 0; i < alGifts.size(); i++) {
                    alGifts.get(i).setSelected(false);
                }
                if(!alGifts.isEmpty() && currentItemPosition == 0 && currentTabPosition == 0) {
                    alGifts.get(0).setSelected(true);
                    callback.onClick(currentTabPosition, currentItemPosition, 0, true, alGifts.get(0));
                    giftAdapter.notifyItemChanged(currentItemPosition);
                }
            }
        }.execute();

    }

    public void callUpdatePrice(String count) {
        if (giftAdapter != null) {
            giftAdapter.callUpdatePrice(count);
            giftAdapter.notifyDataSetChanged();
        }
    }

}