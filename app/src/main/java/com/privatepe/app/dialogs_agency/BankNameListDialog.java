package com.privatepe.app.dialogs_agency;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.privatepe.app.R;
import com.privatepe.app.databinding.BankNameListDialogBinding;
import com.privatepe.app.model.BankList.BankListData;
import com.privatepe.app.model.bank.BankModel;
import com.privatepe.app.retrofit.ApiResponseInterface;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BankNameListDialog extends Dialog implements ApiResponseInterface {
    BankNameListDialogBinding binding;
    private List<String> BankList = new ArrayList<>();


    private List<BankModel> BankListNew = new ArrayList<>();

    OnMyDialogResult mDialogResult;

    public BankNameListDialog(@NonNull Context context) {
        super(context, android.R.style.ThemeOverlay);
        init();
    }

    void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.bank_name_list_dialog, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        this.setCancelable(true);
        show();
        binding.setClickListener(new EventHandler(getContext()));

      /*  BankList = new ArrayList<String>(Arrays.asList(getContext().getResources().getStringArray(R.array.bankArray)));
        final BankAdapter adapter = new BankAdapter(getContext(), android.R.layout.simple_list_item_1, BankList);
        binding.bankList.setAdapter(adapter);*/

        //    new ApiManager(getContext(), this).getBankListDetail();


        BankListNew = getListFromJson(getJsonFromAssets("banks.json"));

        Log.i("banklistsize", "" + BankListNew.get(0).getBankName());


        for (int i = 0; i < BankListNew.size(); i++) {
            BankList.add(BankListNew.get(i).getBankName());
        }
        BankList.add("Other");
        final BankAdapter adapter = new BankAdapter(getContext(), android.R.layout.simple_list_item_1, BankList);
        binding.bankList.setAdapter(adapter);


        binding.bankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                if (mDialogResult != null) {
                    mDialogResult.finish(String.valueOf(item));
                }
                dismiss();
            }

        });


    }


    private List<BankModel> getListFromJson(String jsonFromAssets) {


        Gson gson = new Gson();
        Type listCountryModel = new TypeToken<List<BankModel>>() {
        }.getType();
        List<BankModel> bankList = gson.fromJson(jsonFromAssets, listCountryModel);

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

    ArrayList<BankListData> bankListDataArrayList = new ArrayList<>();

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        //  if (ServiceCode == Constant.GET_BANK_LIST_DETAIL) {
        //   BankListResponce rsp = (BankListResponce) response;
//
        //   bankListDataArrayList.addAll(rsp.getResult().getData());
//
        //   for (int i = 0; i < bankListDataArrayList.size(); i++) {
        //       BankList.add(bankListDataArrayList.get(i).getBankName());
        //   }
        //   BankList.add("Other");
        //   final BankAdapter adapter = new BankAdapter(getContext(), android.R.layout.simple_list_item_1, BankList);
        //   binding.bankList.setAdapter(adapter);
        //  }
    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void backDialog() {
            dismiss();
        }

    }

    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult {
        void finish(String result);
    }


    private class BankAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public BankAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
