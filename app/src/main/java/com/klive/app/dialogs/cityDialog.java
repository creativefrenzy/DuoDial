package com.klive.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klive.app.Interface.CitySelect;
import com.klive.app.R;
import com.klive.app.adapter.CityAdapter;
import com.klive.app.model.CountryCodes.CountryCodeModel;
import com.klive.app.model.city.CityResponse;
import com.klive.app.model.city.CityResult;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.utils.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class cityDialog extends Dialog implements ApiResponseInterface, CitySelect {

    RecyclerView recyclerView;
    CityAdapter cityAdapter;
    List<CityResult> data;
    ApiManager apiManager;
    TextView select;
    private LinearLayoutManager layoutManager;
    OnMyDialogResult myDialogResult;
    private Context context;
    private String token;

    public cityDialog(@NonNull Context context, String token) {
        super(context);
        this.context = context;
        this.token = token;
        init();
    }

    void init() {
        try {
            this.setContentView(R.layout.city);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);
            select = findViewById(R.id.select);
            recyclerView = findViewById(R.id.city_recycler);
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
           /* apiManager = new ApiManager(context, this);
            apiManager.cityList(token);*/


            //cityAdapter = new CityAdapter(getContext(), data);
            //recyclerView.setAdapter(cityAdapter);
            //cityAdapter.notifyDataSetChanged();
            show();
            /*apiManager = new ApiManager(getContext());
            apiManager.cityList(getContext().getString(R.string.tokenApi));*/

            readJsonFile();

            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!CityAdapter.city.equals("")){
                      if(myDialogResult != null){
                            myDialogResult.finish(CityAdapter.city);
                            dismiss();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<CountryCodeModel> countryNameArrayList=new ArrayList<>();
    ArrayList<String> countryNameArray=new ArrayList<>();

    public void readJsonFile(){
        countryNameArrayList = getListFromJson(getJsonFromAssets("countrycodelist.json"));

        Log.e("banklistsize", "" + countryNameArrayList.get(0).getCountry());

/*
        for (int i = 0; i < countryNameArrayList.size(); i++) {
            countryNameArray.add(countryNameArrayList.get(i).getCountry());
        }*/


        cityAdapter = new CityAdapter(context, countryNameArrayList, this);
        recyclerView.setAdapter(cityAdapter);
        cityAdapter.notifyDataSetChanged();
    }

    private List<CountryCodeModel> getListFromJson(String jsonFromAssets) {


        Gson gson = new Gson();
        Type listCountryModel = new TypeToken<List<CountryCodeModel>>() {
        }.getType();
        List<CountryCodeModel> bankList = gson.fromJson(jsonFromAssets, listCountryModel);

        return bankList;


        //    Log.i("ss",""+users.get(0).getCountry());

    }


    String getJsonFromAssets(String fileName) {
        String jsonString;
        try {
            InputStream is = getContext().getAssets().open(fileName);
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
    public void isError(String errorCode) {
        Toast.makeText(context, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.CITY) {
            CityResponse cityResponse = (CityResponse) response;

           /* data = cityResponse.getGetResponse();

            cityAdapter = new CityAdapter(context, data, this);
            recyclerView.setAdapter(cityAdapter);
            cityAdapter.notifyDataSetChanged();*/

        }
    }

    public void setDialogResult(OnMyDialogResult dialogResult){
        myDialogResult = dialogResult;
    }

    @Override
    public void cityClick(boolean selected) {
        if(selected){
            select.setBackground(getContext().getDrawable(R.drawable.active));
        }
    }

    public interface OnMyDialogResult{
        void  finish(String result);
    }
}