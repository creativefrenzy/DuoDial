package com.privatepe.host.response.metend.RechargePlan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RechargePlanResponseNew {

    @SerializedName("success")
    @Expose
    Boolean success;

    @SerializedName("result")
    @Expose
    List<Data> result;

    @SerializedName("error")
    @Expose
    String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Data> getResult() {
        return result;
    }

    public void setResult(List<Data> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class Data implements Serializable {
        int id, amount, status, points, validity_in_days, type,gift_limit;
        String name, image;
        boolean is_Selected;

        public Data(int id, int amount, int status, int points, int validity_in_days, int type, boolean is_Selected) {
            this.id = id;
            this.amount = amount;
            this.status = status;
            this.points = points;
            this.validity_in_days = validity_in_days;
            this.type = type;
            this.is_Selected = is_Selected;
        }

        public int getId() {
            return id;
        }

        public int getAmount() {
            return amount;
        }

        public int getStatus() {
            return status;
        }

        public int getPoints() {
            return points;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public int getValidity_in_days() {
            return validity_in_days;
        }

        public int getType() {
            return type;
        }

        public boolean isIs_Selected() {
            return is_Selected;
        }

        public void setIs_Selected(boolean is_Selected) {
            this.is_Selected = is_Selected;
        }

        public int getGift_limit() {
            return gift_limit;
        }

        public void setGift_limit(int gift_limit) {
            this.gift_limit = gift_limit;
        }
    }

}
