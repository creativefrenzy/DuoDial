package com.privatepe.host.response.NewWallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WalletDataNew {

    @SerializedName("last_page")
    @Expose
    private int lastPage;

    @SerializedName("current_page")
    @Expose
    private int currentPage;


    @SerializedName("next_page_available")
    @Expose
    private boolean nextPageAvailable;


    @SerializedName("walletHistory")
    @Expose
    private List<WalletHistoryDataNew> walletHistoryNew;

    @SerializedName("walletBalance")
    @Expose
    private WalletBalance walletBalance;

    public List<WalletHistoryDataNew> getWalletHistoryNew() {
        return walletHistoryNew;
    }

    public void setWalletHistoryNew(List<WalletHistoryDataNew> walletHistoryNew) {
        this.walletHistoryNew = walletHistoryNew;
    }

    public WalletBalance getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(WalletBalance walletBalance) {
        this.walletBalance = walletBalance;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
