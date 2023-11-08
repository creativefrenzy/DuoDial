package com.privatepe.app.model;

import com.google.gson.annotations.SerializedName;

public class Authenticate {
    @SerializedName("receiver_id")
    private String receiver_id;
    @SerializedName("unique_id")
    private String unique_id;
    @SerializedName("call_type")
    private String call_type;
    @SerializedName("is_free_call")
    private boolean is_free_call;

    public Authenticate(String receiver_id, String unique_id, String call_type, boolean is_free_call) {
        this.receiver_id = receiver_id;
        this.unique_id = unique_id;
        this.call_type = call_type;
        this.is_free_call = is_free_call;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public String getCall_type() {
        return call_type;
    }

    public boolean getIs_free_call() {
        return is_free_call;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public void setIs_free_call(boolean is_free_call) {
        this.is_free_call = is_free_call;
    }
}
