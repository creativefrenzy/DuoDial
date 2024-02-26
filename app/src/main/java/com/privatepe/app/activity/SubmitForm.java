package com.privatepe.app.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.privatepe.app.R;
import com.privatepe.app.activity.addalbum.AddAlbumActivity;
import com.privatepe.app.main.Home;
import com.privatepe.app.model.SystemMsgModel;
import com.privatepe.app.utils.BaseActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SubmitForm extends BaseActivity {
    private ArrayList<SystemMsgModel> systemMsgModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(),true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_submit_form);
    }

    public void confirm(View view) {
        Intent intent = new Intent(this, Home.class);
        finishAffinity();
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("System_MSG", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("Welcome to Private Pe. Enjoy your trip and find your true love here!\n\nDo not reveal your personal information, or open any unknown links to avoid information theft and financial loss.", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<SystemMsgModel>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        systemMsgModel = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (systemMsgModel == null) {
            // if the array list is empty
            // creating a new array list.
            systemMsgModel = new ArrayList<>();
        }
    }

    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("System_MSG", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = "Welcome to Private Pe. Enjoy your trip and find your true love here!\n\nDo not reveal your personal information, or open any unknown links to avoid information theft and financial loss.";

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("description", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.
        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }
}