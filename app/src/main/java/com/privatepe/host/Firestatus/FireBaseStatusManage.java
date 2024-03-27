package com.privatepe.host.Firestatus;

import static com.privatepe.host.main.Home.switchBtn;
import static com.privatepe.host.main.Home.unansweredCallCheck;
import static com.privatepe.host.main.Home.unansweredCounterSet;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.privatepe.host.R;
import com.privatepe.host.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class FireBaseStatusManage {
    DatabaseReference chatRef;
    String userId, userName, fcmToken, fGender, status;
    Context context;

    public FireBaseStatusManage(Context context, String userId, String userName, String fcmToken, String fGender, String status) {
        Log.e("unasweredCheck","HerestatusFirebaseUpdate "+status);
        if(new SessionManager(context).getUnanswerredCalls()>(unansweredCounterSet-1)){
            status="Offline";
        }
        this.context = context;
        this.userId = userId;
        this.userName = userName;
        this.fcmToken = fcmToken;
        this.fGender = fGender;
        this.status = status;

        chatRef = FirebaseDatabase.getInstance().getReference().child("Users");
        reginFirebase();
    }

    private void reginFirebase() {
        String uid = userId;
        String name = userName;

        changeLiveStatus(uid, name, status);
    }

    void changeLiveStatus(String uid, String name, String status) {
        //HashMap<String, String> details = new HashMap<>();
        Map details = new HashMap();
        details.put("uid", uid);
        details.put("name", name);
        details.put("status", status);
        details.put("fcmToken", fcmToken);
        details.put("gender", fGender);
        //chatRef.child(uid).setValue(details);

        Map messageBodyDetails = new HashMap();

        messageBodyDetails.put(uid + "/" + "", details);
        if (!details.isEmpty()) {
            chatRef.updateChildren(messageBodyDetails).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //  Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void subscribeToTopic(Context context, String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        Log.d("Check_JKFakeCall", ""+context.getResources().getString(R.string.msg_subscribed));
                        new SessionManager(context).setTopicSubscriptionStatus(topic, true);
                    } else {
                        Log.d("Check_JKFakeCall", ""+context.getResources().getString(R.string.msg_subscribed));
                    }
                });
    }

    public static void unsubscribeFromTopic(Context context, String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Check_JKFakeCall", ""+context.getResources().getString(R.string.msg_unsubscribed));
                        new SessionManager(context).setTopicSubscriptionStatus(topic, false);
                    } else {
                        Log.d("Check_JKFakeCall", ""+context.getResources().getString(R.string.msg_unsubscribe_failed));
                    }
                });
    }

}