package com.privatepe.host.response;

public class RecentActiveHostModel {
    public boolean success;
    public Result result;
    public Object error;

    public static class Result {
        public int user_id;
        public int profile_id;
        public String name;
        public String profile_image;
        public int call_price;
        public int call_rate;
        public int status;
    }
}