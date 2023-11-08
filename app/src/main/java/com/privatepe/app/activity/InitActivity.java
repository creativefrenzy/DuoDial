package com.privatepe.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.privatepe.app.R;

public class InitActivity extends AppCompatActivity {
    String token, username, receiver_id, channel_name, is_free_call, unique_id, callType, callerImage="", name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        name = getIntent().getStringExtra("name");
        token = getIntent().getStringExtra("token");
        username = getIntent().getStringExtra("username");
        receiver_id = getIntent().getStringExtra("receiver_id");
        channel_name = getIntent().getStringExtra("channel_name");
        is_free_call = getIntent().getStringExtra("is_free_call");
        unique_id = getIntent().getStringExtra("unique_id");
        callType = getIntent().getStringExtra("callType");
        callerImage = getIntent().getStringExtra("image");

        Intent incoming = new Intent(InitActivity.this, IncomingCallScreen.class);
        incoming.putExtra("receiver_id", receiver_id);
        incoming.putExtra("username", username);
        incoming.putExtra("unique_id", unique_id);
        incoming.putExtra("channel_name", channel_name);
        incoming.putExtra("token", token);
        incoming.putExtra("callType", callType);
        incoming.putExtra("is_free_call", is_free_call);
        incoming.putExtra("name", name);
        incoming.putExtra("image", callerImage);
        //incoming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        InitActivity.this.startActivity(incoming);
    }
}