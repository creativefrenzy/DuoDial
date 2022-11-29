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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klive.app.Interface.LanguageSelect;
import com.klive.app.R;
import com.klive.app.adapter.CityAdapter;
import com.klive.app.adapter.LangAdapter;
import com.klive.app.model.CountryCodes.CountryCodeModel;
import com.klive.app.model.language.LanguageData;
import com.klive.app.model.language.LanguageJsonModel;
import com.klive.app.model.language.LanguageResponce;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.utils.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class languageDialog extends Dialog implements ApiResponseInterface, LanguageSelect {
    RecyclerView recyclerView;
    LangAdapter langAdapter;
    List<LanguageData> data;
    ApiManager apiManager;
    OnMyDialogResult myDialogResult;
    private GridLayoutManager layoutManager;
    private Context context;
    TextView select;
    private String token;
    public languageDialog(@NonNull Context context, String token) {
        super(context);
        this.context = context;
        this.token = token;
        init();
    }
    void init() {
        try {
            this.setContentView(R.layout.language);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);
            select = findViewById(R.id.select);
            recyclerView = findViewById(R.id.languageRecycler);
            layoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(layoutManager);
         /*   apiManager = new ApiManager(context, this);
            apiManager.languageList(token);*/
            show();

            readJsonFile();

            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!LangAdapter.language.equals("")){
                        if(myDialogResult != null){
                            myDialogResult.finish(LangAdapter.language, LangAdapter.id);
                            dismiss();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

List<LanguageJsonModel> languageJsonModelArrayList=new ArrayList<>();

    public void readJsonFile(){
        languageJsonModelArrayList = getListFromJson(getJsonFromAssets("language.json"));

        Log.e("banklistsize", "" + languageJsonModelArrayList.get(0).getLanguageName());

/*
        for (int i = 0; i < countryNameArrayList.size(); i++) {
            countryNameArray.add(countryNameArrayList.get(i).getCountry());
        }*/


        langAdapter = new LangAdapter(context, languageJsonModelArrayList, this);
        recyclerView.setAdapter(langAdapter);
        langAdapter.notifyDataSetChanged();
    }

    private List<LanguageJsonModel> getListFromJson(String jsonFromAssets) {


        Gson gson = new Gson();
        Type LanguageJsonModel = new TypeToken<List<LanguageJsonModel>>() {
        }.getType();
        List<LanguageJsonModel> bankList = gson.fromJson(jsonFromAssets, LanguageJsonModel);

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

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.LANGUAGE) {
            LanguageResponce languageResponce = (LanguageResponce) response;

         /*   data = languageResponce.getResult();

            langAdapter = new LangAdapter(context, data, this);
            recyclerView.setAdapter(langAdapter);
            langAdapter.notifyDataSetChanged();*/

        }
    }

    public void setDialogResult(languageDialog.OnMyDialogResult dialogResult){
        myDialogResult = dialogResult;
    }

    @Override
    public void languageClick(boolean selected) {
        if(selected){
            select.setBackground(getContext().getDrawable(R.drawable.active));
        }
    }

    public interface OnMyDialogResult{
        void  finish(String result, String id);
    }

}