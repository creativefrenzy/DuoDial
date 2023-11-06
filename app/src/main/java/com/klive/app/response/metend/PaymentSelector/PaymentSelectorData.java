package com.klive.app.response.metend.PaymentSelector;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentSelectorData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("paytm_upi")
    @Expose
    private String paytmUpi;
    @SerializedName("only_direct")
    @Expose
    private String onlyDirect;
    @SerializedName("upi_id")
    @Expose
    private String upiId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaytmUpi() {
        return paytmUpi;
    }

    public void setPaytmUpi(String paytmUpi) {
        this.paytmUpi = paytmUpi;
    }

    public String getOnlyDirect() {
        return onlyDirect;
    }

    public void setOnlyDirect(String onlyDirect) {
        this.onlyDirect = onlyDirect;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
