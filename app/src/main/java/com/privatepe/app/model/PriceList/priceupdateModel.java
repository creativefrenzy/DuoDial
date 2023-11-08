package com.privatepe.app.model.PriceList;

import com.google.gson.annotations.SerializedName;

public class priceupdateModel {

    @SerializedName("call_rate")
    private String callprice;


    public priceupdateModel(String callprice) {
        this.callprice = callprice;
    }

    public String getCallprice() {
        return callprice;
    }

    public void setCallprice(String callprice) {
        this.callprice = callprice;
    }
}
