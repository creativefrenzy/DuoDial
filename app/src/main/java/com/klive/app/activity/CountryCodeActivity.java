package com.klive.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klive.app.R;
import com.klive.app.adapter.CountryCodeRecyclerAdapter;
import com.klive.app.login.OTPVerify;
import com.klive.app.model.CountryCodes.CountryCodeModel;
import com.klive.app.utils.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CountryCodeActivity extends BaseActivity {

    RecyclerView countryCodeRecycler;
    ImageView BackBtn;
    CountryCodeRecyclerAdapter countryCodeRecyclerAdapter;
    List<CountryCodeModel> countryCodeList = new ArrayList<>();
    List<Integer> flagdrawbleList=new ArrayList<>();
    public String phonenum;
    public String CountryCode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(),true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_code);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        BackBtn = findViewById(R.id.backbtn);

        storeFlagesinLists();


        //  addItemtoList(countryCodeList);
        //  Log.i("ss",""+getJsonFromAssets("countrylistin.json"));


        if (getIntent() != null) {
            if (getIntent().getStringExtra("phonenum") != null) {
                phonenum = getIntent().getStringExtra("phonenum").toString();
            }
            if (getIntent().getStringExtra("countryCode") != null) {
                CountryCode = getIntent().getStringExtra("countryCode").toString();

            }

        }


        countryCodeList = getListFromJson(getJsonFromAssets("countrycodelist.json"));

        countryCodeRecycler = findViewById(R.id.countryCodeRecycler);
        countryCodeRecycler.setHasFixedSize(true);
        countryCodeRecycler.setLayoutManager(new LinearLayoutManager(this));
        countryCodeRecyclerAdapter = new CountryCodeRecyclerAdapter(this, countryCodeList,flagdrawbleList);






        countryCodeRecycler.setAdapter(countryCodeRecyclerAdapter);


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void storeFlagesinLists() {

        flagdrawbleList.clear();

        flagdrawbleList.add(R.drawable.india_flag);
        flagdrawbleList.add(R.drawable.pakistan_flag);
        flagdrawbleList.add(R.drawable.indonesia_flag);
        flagdrawbleList.add(R.drawable.bangladesh_flag);
        flagdrawbleList.add(R.drawable.vietnam_flag);
        flagdrawbleList.add(R.drawable.flag_philippines);
        flagdrawbleList.add(R.drawable.morroco_flag);
        flagdrawbleList.add(R.drawable.malaysia_flag);
        flagdrawbleList.add(R.drawable.brazil_flag);
        flagdrawbleList.add(R.drawable.colombia_flag);
        flagdrawbleList.add(R.drawable.ukraine_flag);
        flagdrawbleList.add(R.drawable.turkey_flag);
        flagdrawbleList.add(R.drawable.venezuela_flag);


    }

    private List<CountryCodeModel> getListFromJson(String jsonFromAssets) {


        Gson gson = new Gson();
        Type listCountryModel = new TypeToken<List<CountryCodeModel>>() {
        }.getType();
        List<CountryCodeModel> countryList = gson.fromJson(jsonFromAssets, listCountryModel);

        return countryList;


        //    Log.i("ss",""+users.get(0).getCountry());

    }


    String getJsonFromAssets(String fileName) {
        String jsonString;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CountryCodeActivity.this, OTPVerify.class);
        intent.putExtra("phonenum", phonenum);
        intent.putExtra("countryCode", CountryCode);
        startActivity(intent);
        finish();
    }
}