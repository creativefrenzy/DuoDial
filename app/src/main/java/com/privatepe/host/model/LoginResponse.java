package com.privatepe.host.model;

public class LoginResponse {

    String message, error, already_registered;
    boolean success;
    Result result;

    int guest_status;
    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result getResult() {
        return result;
    }

    public String getAlready_registered() {
        return already_registered;
    }

    public void setAlready_registered(String already_registered) {
        this.already_registered = already_registered;
    }



    public static class Result {

        String token, name, gender, profile_id, user_city, country, device_id;
        int is_online;
        int guest_status;
        int allow_in_app_purchase,verification_status;
        int role;
        String login_type, username, demo_password;

        public int getVerification_status() {
            return verification_status;
        }

        public void setVerification_status(int verification_status) {
            this.verification_status = verification_status;
        }

        public int getRole() {
            return role;
        }

        public String getDevice_id() {
            return device_id;
        }

        public int getIs_online() {
            return is_online;
        }

        public int getGuest_status() {
            return guest_status;
        }

        public void setGuest_status(int guest_status) {
            this.guest_status = guest_status;
        }

        public String getToken() {
            return token;
        }

        public String getName() {
            return name;
        }

        public String getGender() {
            return gender;
        }

        public String getProfile_id() {
            return profile_id;
        }

        public String getLogin_type() {
            return login_type;
        }

        public String getUsername() {
            return username;
        }

        public String getDemo_password() {
            return demo_password;
        }

        public int getAllow_in_app_purchase() {
            return allow_in_app_purchase;
        }

        public void setAllow_in_app_purchase(int allow_in_app_purchase) {
            this.allow_in_app_purchase = allow_in_app_purchase;
        }

        public String getUser_city() {
            return user_city;
        }

        public void setUser_city(String user_city) {
            this.user_city = user_city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }


        public Result(String token, String name, String gender, String profile_id, String user_city, String country, int is_online, int guest_status, int allow_in_app_purchase, int role, String login_type, String username, String demo_password, String device_id) {
            this.token = token;
            this.name = name;
            this.gender = gender;
            this.profile_id = profile_id;
            this.user_city = user_city;
            this.country = country;
            this.is_online = is_online;
            this.guest_status = guest_status;
            this.allow_in_app_purchase = allow_in_app_purchase;
            this.role = role;
            this.login_type = login_type;
            this.username = username;
            this.demo_password = demo_password;
            this.device_id = device_id;
        }
    }


    public int getGuest_status() {
        return guest_status;
    }
}