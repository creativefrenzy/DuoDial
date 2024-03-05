package com.privatepe.host.response.HaodaPayResponse;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class HaodaPayModel implements Parcelable {
    public boolean success;
    public Result result;
    public String payment_link;
    public Object error;

    protected HaodaPayModel(Parcel in) {
        success = in.readByte() != 0;
        payment_link = in.readString();
    }

    public static final Creator<HaodaPayModel> CREATOR = new Creator<HaodaPayModel>() {
        @Override
        public HaodaPayModel createFromParcel(Parcel in) {
            return new HaodaPayModel(in);
        }

        @Override
        public HaodaPayModel[] newArray(int size) {
            return new HaodaPayModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(payment_link);
    }

    public static class Result implements Parcelable {
        public int amount;
        public String plan_id;
        public String order_id;

        protected Result(Parcel in) {
            amount = in.readInt();
            plan_id = in.readString();
            order_id = in.readString();
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            @Override
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(amount);
            dest.writeString(plan_id);
            dest.writeString(order_id);
        }
    }
}
