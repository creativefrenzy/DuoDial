package com.klive.app.response.Agency;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgencyResult {

    @SerializedName("commission_list")
    @Expose
    private List<Commission> commissionList = null;
    @SerializedName("subagency_commission_list")
    @Expose
    private List<SubagencyCommission> subagencyCommissionList = null;

    public List<Commission> getCommissionList() {
        return commissionList;
    }

    public void setCommissionList(List<Commission> commissionList) {
        this.commissionList = commissionList;
    }

    public List<SubagencyCommission> getSubagencyCommissionList() {
        return subagencyCommissionList;
    }

    public void setSubagencyCommissionList(List<SubagencyCommission> subagencyCommissionList) {
        this.subagencyCommissionList = subagencyCommissionList;
    }

}