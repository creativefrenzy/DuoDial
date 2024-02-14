package com.privatepe.app.adapter.chat_price;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.privatepe.app.R;
import com.privatepe.app.activity.MyChatPriceActivity;
import com.privatepe.app.response.chat_price.PriceDataModel;
import com.privatepe.app.response.chat_price.UpdateCallPriceResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;

public class UpdateChatPriceListAdapter extends RecyclerView.Adapter<UpdateChatPriceListAdapter.ViewHolder> implements ApiResponseInterface {
    private String TAG = "UpdateChatPriceListAdapter";
    private ArrayList<PriceDataModel> chatPriceList;
    private int SelectedPrice;
    private int selectedPos = 0;
    private MyChatPriceActivity myChatPriceActivity;
    private ApiManager apiManager;
    private LinearLayout toast;

    public UpdateChatPriceListAdapter(ArrayList<PriceDataModel> arrayList, int selectedPrice, MyChatPriceActivity myChatPriceActivity) {
        this.chatPriceList = arrayList;
        this.SelectedPrice = selectedPrice;
        this.selectedPos = getCurrentSelectedPos(chatPriceList, SelectedPrice);
        this.myChatPriceActivity = myChatPriceActivity;
        apiManager = new ApiManager(myChatPriceActivity, this);

    }

    private int getCurrentSelectedPos(ArrayList<PriceDataModel> chatPriceList, float selectedPrice) {
        int selPos = 0;
        for (int i = 0; i < chatPriceList.size(); i++) {
            if (selectedPrice == (int) Double.parseDouble(chatPriceList.get(i).getAmount())) {
                selPos = i;
                break;
            }
        }
        return selPos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_chat_price_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String priceText = chatPriceList.get(position).getAmount() + "/min";
        String levelText = chatPriceList.get(position).getLevel();
        holder.priceText.setText(priceText);
        holder.levelText.setText(levelText);
        int myLevel = Integer.parseInt(String.valueOf(new SessionManager(myChatPriceActivity).getChatPriceListResponse().getLevel()));
        //  int myLevel=7;
        // boolean isEligible = checkLevelEligibility1(myLevel, getLevelFromApi1(chatPriceList.get(position).getLevel()));
        boolean isEligible = checkLevelEligibility1(myLevel, Integer.parseInt(chatPriceList.get(position).getLevel()));


        if (isEligible) {
            EnableState(holder.priceText, holder.levelText, holder.checkBox);
        } else {
            DisableState(holder.priceText, holder.levelText, holder.checkBox);
        }

        holder.mainItem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                //boolean isEligible = checkLevelEligibility1(myLevel, getLevelFromApi1(chatPriceList.get(position).getLevel()));
                boolean isEligible = checkLevelEligibility1(myLevel, Integer.parseInt(chatPriceList.get(position).getLevel()));
                if (isEligible) {
                    apiManager.updateCallPriceStr(String.valueOf(chatPriceList.get(position).getAmount()));
                    selectedPos = holder.getAdapterPosition();
                    for (int i = 0; i < chatPriceList.size(); i++) {
                        if (i != selectedPos) {
                            holder.checkBox.setChecked(false);
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    showCustomToast();
                }

            }
        });


        if (selectedPos == position) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

    }

    private void showCustomToast() {
        LayoutInflater li = LayoutInflater.from(myChatPriceActivity);
        View layout = li.inflate(R.layout.level_eligibity_popup, (ViewGroup) toast);
        Toast toast = new Toast(myChatPriceActivity);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public int getItemCount() {
        return chatPriceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView priceText, levelText;
        private CheckBox checkBox;
        private RelativeLayout mainItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            priceText = itemView.findViewById(R.id.pricetxt);
            checkBox = itemView.findViewById(R.id.checkbox);
            mainItem = itemView.findViewById(R.id.firstone);
            levelText = itemView.findViewById(R.id.levelText);
            checkBox.setClickable(false);
            checkBox.setEnabled(false);
        }
    }

    @Override
    public void isError(String errorCode) {

    }

    @SuppressLint("LongLogTag")
    @Override
    public void isSuccess(Object response, int ServiceCode) {
        Log.e(TAG, "isSuccess: ");
        if (ServiceCode == Constant.UPDATE_CALL_PRICE) {
            UpdateCallPriceResponse rsp = (UpdateCallPriceResponse) response;
            myChatPriceActivity.setUpdatedCallRate(String.valueOf(chatPriceList.get(selectedPos).getAmount()));
            Toast.makeText(myChatPriceActivity, rsp.getResult(), Toast.LENGTH_SHORT).show();
        }
    }


    private int getLevelFromApi1(String level) {
        String[] Levels = level.split("Lv");

        return Integer.parseInt(Levels[1].trim());
    }


    private boolean checkLevelEligibility1(int myLevel, int apiLevel) {
        boolean isEligible;
        if (myLevel >= apiLevel) {
            isEligible = true;
        } else {
            isEligible = false;
        }
        return isEligible;
    }


    public void EnableState(TextView priceText, TextView levelText, CheckBox checkBox) {
        priceText.setTextColor(myChatPriceActivity.getResources().getColor(R.color.enable_price));
        levelText.setTextColor(myChatPriceActivity.getResources().getColor(R.color.enable_price));
        levelText.setBackgroundResource(R.drawable.price_update_list_level_bg);
        checkBox.setBackgroundResource(R.drawable.checkbox_selector);
    }


    public void DisableState(TextView priceText, TextView levelText, CheckBox checkBox) {
        priceText.setTextColor(myChatPriceActivity.getResources().getColor(R.color.disable_price));
        levelText.setTextColor(myChatPriceActivity.getResources().getColor(R.color.disable_price));
        levelText.setBackgroundResource(R.drawable.price_update_list_level_bg_disabled);
        checkBox.setBackgroundResource(R.drawable.check_box_disabled);
    }


}
