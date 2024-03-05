package com.privatepe.host.response;

import java.io.Serializable;

public class CallDetailData implements Serializable{

    public int id;
    public int caller_id;
    public int duration;
    public int call_status;
    public String created_at;
    public String call_quality;
    public CallerDetails caller_details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaller_id() {
        return caller_id;
    }

    public void setCaller_id(int caller_id) {
        this.caller_id = caller_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCall_quality() {
        return call_quality;
    }

    public void setCall_quality(String call_quality) {
        this.call_quality = call_quality;
    }

    public CallerDetails getCaller_details() {
        return caller_details;
    }

    public void setCaller_details(CallerDetails caller_details) {
        this.caller_details = caller_details;
    }

    public int getCall_status() {
        return call_status;
    }

    public void setCall_status(int call_status) {
        this.call_status = call_status;
    }

    public class CallerDetails {
        public int id;
        public String name;
        public int rich_level;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRich_level() {
            return rich_level;
        }

        public void setRich_level(int rich_level) {
            this.rich_level = rich_level;
        }
    }
}
