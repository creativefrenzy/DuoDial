package com.privatepe.app.model;

public class SystemMsgModel {
    private String name;
    private String description;

    // creating constructor for our variables.
    public SystemMsgModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
