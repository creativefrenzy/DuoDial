package com.klive.app.model.PriceList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceDataModel {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("level")
    @Expose
    private String level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
