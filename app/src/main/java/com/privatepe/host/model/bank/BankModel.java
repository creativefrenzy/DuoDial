package com.privatepe.host.model.bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankModel {

    @SerializedName("bankName")
    @Expose
    String BankName;


    public BankModel(String bankName) {
        BankName = bankName;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }
}
