package com.privatepe.app.model.BankList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankListData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("bank_name")
    @Expose
    private String bankName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
