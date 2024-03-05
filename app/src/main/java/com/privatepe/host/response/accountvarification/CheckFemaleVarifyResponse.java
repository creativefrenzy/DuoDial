package com.privatepe.host.response.accountvarification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckFemaleVarifyResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private Integer is_female_verify;

    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getIs_female_verify() {
        return is_female_verify;
    }

    public void setIs_female_verify(Integer is_female_verify) {
        this.is_female_verify = is_female_verify;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
