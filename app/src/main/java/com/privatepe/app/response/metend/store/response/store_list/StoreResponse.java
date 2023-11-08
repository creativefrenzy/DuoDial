package com.privatepe.app.response.metend.store.response.store_list;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreResponse implements Parcelable {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<StoreResultModel> result;

    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<StoreResultModel> getResult() {
        return result;
    }

    public void setResult(List<StoreResultModel> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    protected StoreResponse(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        error = in.readString();
    }


    public static final Creator<StoreResponse> CREATOR = new Creator<StoreResponse>() {
        @Override
        public StoreResponse createFromParcel(Parcel in) {
            return new StoreResponse(in);
        }

        @Override
        public StoreResponse[] newArray(int size) {
            return new StoreResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        dest.writeString(error);
    }
}
