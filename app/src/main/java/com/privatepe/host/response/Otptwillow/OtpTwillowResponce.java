package com.privatepe.host.response.Otptwillow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpTwillowResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
  /*  @SerializedName("result")
    @Expose
    private String result;*/
    @SerializedName("session_uuid")
    @Expose
    private String session_uuid;
    /*@SerializedName("otp")
    @Expose
    private Integer otp;*/
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

  /*  public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }*/

    /*public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }*/

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public String getSession_uuid() {
        return session_uuid;
    }

    public void setSession_uuid(String session_uuid) {
        this.session_uuid = session_uuid;
    }
}
