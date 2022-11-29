package com.klive.app.Firestatus;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FireBaseStatusManage {
    DatabaseReference chatRef;
    String userId, userName, fcmToken, fGender, status;
    Context context;

    public FireBaseStatusManage(Context context, String userId, String userName, String fcmToken, String fGender, String status) {
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
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
