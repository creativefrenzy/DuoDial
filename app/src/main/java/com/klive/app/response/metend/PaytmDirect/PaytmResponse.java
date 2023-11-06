package com.klive.app.response.metend.PaytmDirect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaytmResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private PaytmResult result;
    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("txnToken")
    @Expose
    private String txnToken;
    @SerializedName("deepLink")
    @Expose
    private String deepLink;
    @SerializedName("transId")
    @Expose
    private String transId;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public PaytmResult getResult() {
        return result;
    }

    public void setResult(PaytmResult result) {
        this.result = result;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTxnToken() {
        return txnToken;
    }

    public void setTxnToken(String txnToken) {
        this.txnToken = txnToken;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
