package com.privatepe.host.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.privatepe.host.Inbox.ChatBean;
import com.privatepe.host.R;
import com.privatepe.host.adapter.SystemChatAdapter;
import com.privatepe.host.sqlite.Chat;
import com.privatepe.host.sqlite.SystemDB;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SystemMsg extends BaseActivity {
    public static RecyclerView recyclerView;
    public static SystemChatAdapter systemChatAdapter;
    public static List<ChatBean> data;
    private LinearLayoutManager layoutManager;
    SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy");
    SystemDB db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(),true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.system_msg);
        data = new ArrayList<>();
        db = new SystemDB(this);
        Date date = new Date();

        db.setTotalSystemUnreadCount(0);


        recyclerView = findViewById(R.id.chat_recyclerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //data.add(new ChatBean("System", "hello", "00.00", "", "", dateformatter.format(date)));

        List<Chat> chats = db.getAllChat("System");

        for (Chat cn : chats) {
            data.add(new ChatBean(new SessionManager(getApplicationContext()).getUserId(), cn.get_text_get(), cn.get_time_get(), cn.get_text_sent(), cn.get_time_sent(),"TEXT"));
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name() + " ,Text: " + cn.get_text_get();
            Log.d("Name: ", log);
        }
        systemChatAdapter = new SystemChatAdapter(this, data);
        recyclerView.setAdapter(systemChatAdapter);
        systemChatAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(data.size());
    }

    public void backFun(View view) {
        onBackPressed();
    }
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void back(View view) {
    }
}