package com.privatepe.host.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.privatepe.host.Inbox.InboxDetails;
import com.privatepe.host.R;
import com.privatepe.host.activity.SystemMsg;
import com.privatepe.host.databinding.MessageNotificationDialogBinding;
import com.privatepe.host.model.FirebasePopupMsgModel;
import com.privatepe.host.response.newgiftresponse.NewGift;
import com.privatepe.host.sqlite.ChatDB;
import com.privatepe.host.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MessageNotificationDialog extends Dialog {
    private final ChatDB db;
    private MessageNotificationDialogBinding binding;
    private int width, height;
    private int paddingW = 10, paddingH = 0;
    private String TAG = "MessageNotificationDialog";
    private JSONObject dataJson;
    private long timeToHide = 3000L;
    private Handler removeDialogHandler = new Handler();
    private String Notitype;
    private String fromUserID;
    private String userName, profilePic;
    private FirebasePopupMsgModel firebasePopupMsgModel;

    public MessageNotificationDialog(@NonNull Context context, String msgData, String fromUserid, String type, FirebasePopupMsgModel firePopupMsgModel) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.message_notification_dialog, null, false);
        setContentView(binding.getRoot());
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        getWindow().setBackgroundDrawable(colorDrawable);
        getWindow().setDimAmount(0f);
        getWindow().setGravity(Gravity.TOP);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        getWindow().setLayout(width - paddingW, ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.getRoot().setClickable(false);
        db = new ChatDB(getContext());
        Notitype = type;
        fromUserID = fromUserid;
        firebasePopupMsgModel = firePopupMsgModel;
        init(msgData);

        binding.view.setOnClickListener(v -> {
            Log.e(TAG, "onClick: view ");
            if (type.equals("FIREBASE") && fromUserID.equals("System")) {
                getContext().startActivity(new Intent(getContext(), SystemMsg.class));
            } else {
                db.updateRead(fromUserID);
                getContext().startActivity(new Intent(getContext(), InboxDetails.class).putExtra("peerId", fromUserID).putExtra("peerName", userName).putExtra("peerImage", profilePic));
            }
            dismiss();
        });


    }

    private void init(String msgData) {
        try {
            if (Notitype.equals("ZEGO")) {
                JSONObject jsonObject = new JSONObject(msgData);
                if (jsonObject.has("isMessageWithChat")) {
                    dataJson = new JSONObject(jsonObject.get("ChatMessageBody").toString());
                    userName = dataJson.get("UserName").toString();
                    profilePic = dataJson.get("ProfilePic").toString();
                    binding.userName.setText(dataJson.get("UserName").toString());
                    binding.message.setText(dataJson.get("Message").toString());
                    Glide.with(getContext()).load(dataJson.get("ProfilePic")).into(binding.userImage);
                    show();
                } else if (jsonObject.has("isMessageWithChatGift")) {
                    dataJson = new JSONObject(jsonObject.get("ChatGiftMessageBody").toString());
                    userName = dataJson.get("UserName").toString();
                    profilePic = dataJson.get("ProfilePic").toString();
                    binding.userName.setText(dataJson.get("UserName").toString());
                    String giftPos = dataJson.get("GiftPos").toString();
                    HashMap<Integer, NewGift> giftImgList = new SessionManager(getContext()).getEmployeeAllGiftList();
                    NewGift gift = giftImgList.get(Integer.parseInt(giftPos));
                    //binding.message.setText("Send a " + gift.getGift_name());
                    binding.message.setText(gift.getGift_name() + " Received");
                    Glide.with(getContext()).load(gift.getImage()).into(binding.giftImg);
                    Glide.with(getContext()).load(dataJson.get("ProfilePic")).into(binding.userImage);
                    show();
                }
            } else if (Notitype.equals("FIREBASE")) {
                userName = firebasePopupMsgModel.getUserName();
                profilePic = firebasePopupMsgModel.getProfilePic();
                if (fromUserID.equals("System")) {
                    binding.userName.setText("Private Pe Team");
                    binding.message.setText(firebasePopupMsgModel.getMessage());
                    binding.userImage.setImageResource(R.drawable.logo);
                    show();
                }

            }
            HideDialogLater(timeToHide);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void HideDialogLater(long timeToHide) {
        if (removeDialogHandler != null) {
            removeDialogHandler.removeCallbacksAndMessages(null);
        }
        removeDialogHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, timeToHide);

    }


}
