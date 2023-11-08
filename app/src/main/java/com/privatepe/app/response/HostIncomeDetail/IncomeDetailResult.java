package com.privatepe.app.response.HostIncomeDetail;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncomeDetailResult {
    @SerializedName("wallet_history")
    @Expose
    private List<IncomeDetailWalletHistory> walletHistory = null;

    public List<IncomeDetailWalletHistory> getWalletHistory() {
        return walletHistory;
    }

    public void setWalletHistory(List<IncomeDetailWalletHistory> walletHistory) {
        this.walletHistory = walletHistory;
    }

}
