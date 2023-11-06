package com.klive.app.response.metend.upi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveUpiResult {

    @SerializedName("cashfree_upi")
    @Expose
    String cashfree_upi;

    @SerializedName("razor_pay_upi")
    @Expose
    String razor_pay_upi;

    @SerializedName("gpay_upi")
    @Expose
    String gpay_upi;

    @SerializedName("phonepe_upi")
    @Expose
    String phonepe_upi;

    @SerializedName("paytm_upi")
    @Expose
    String paytm_upi;

    @SerializedName("only_direct")
    @Expose
    String only_direct;

    @SerializedName("only_gpay")
    @Expose
    String only_gpay;

    @SerializedName("upi_id")
    @Expose
    String upi_id;

    @SerializedName("gpay_id")
    @Expose
    String gpay_id;

    @SerializedName("phonepe_id")
    @Expose
    String phonepe_id;

    @SerializedName("upi_ids")
    @Expose
    String upi_ids;

    @SerializedName("mc")
    @Expose
    String mc;

    @SerializedName("tr")
    @Expose
    String tr;

    @SerializedName("pn")
    @Expose
    String pn;

    @SerializedName("url")
    @Expose
    String url;

    @SerializedName("upi")
    @Expose
    String upi;

    public String getCashfree_upi() {
        return cashfree_upi;
    }

    public void setCashfree_upi(String cashfree_upi) {
        this.cashfree_upi = cashfree_upi;
    }

    public String getRazor_pay_upi() {
        return razor_pay_upi;
    }

    public void setRazor_pay_upi(String razor_pay_upi) {
        this.razor_pay_upi = razor_pay_upi;
    }

    public String getGpay_upi() {
        return gpay_upi;
    }

    public void setGpay_upi(String gpay_upi) {
        this.gpay_upi = gpay_upi;
    }

    public String getPhonepe_upi() {
        return phonepe_upi;
    }

    public void setPhonepe_upi(String phonepe_upi) {
        this.phonepe_upi = phonepe_upi;
    }

    public String getPaytm_upi() {
        return paytm_upi;
    }

    public void setPaytm_upi(String paytm_upi) {
        this.paytm_upi = paytm_upi;
    }

    public String getOnly_direct() {
        return only_direct;
    }

    public void setOnly_direct(String only_direct) {
        this.only_direct = only_direct;
    }

    public String getOnly_gpay() {
        return only_gpay;
    }

    public void setOnly_gpay(String only_gpay) {
        this.only_gpay = only_gpay;
    }

    public String getUpi_id() {
        return upi_id;
    }

    public void setUpi_id(String upi_id) {
        this.upi_id = upi_id;
    }

    public String getGpay_id() {
        return gpay_id;
    }

    public void setGpay_id(String gpay_id) {
        this.gpay_id = gpay_id;
    }

    public String getPhonepe_id() {
        return phonepe_id;
    }

    public void setPhonepe_id(String phonepe_id) {
        this.phonepe_id = phonepe_id;
    }

    public String getUpi_ids() {
        return upi_ids;
    }

    public void setUpi_ids(String upi_ids) {
        this.upi_ids = upi_ids;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }
}
