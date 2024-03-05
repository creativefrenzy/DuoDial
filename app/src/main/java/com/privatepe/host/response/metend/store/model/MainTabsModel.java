package com.privatepe.host.response.metend.store.model;

import androidx.fragment.app.Fragment;

public class MainTabsModel {
    private Fragment fragment;
    private String tabName;

    public MainTabsModel(Fragment fragment, String tabName) {
        this.fragment = fragment;
        this.tabName = tabName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
