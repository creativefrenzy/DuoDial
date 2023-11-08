package com.privatepe.app.response.metend;

import java.io.Serializable;


public class FollowProfileImg implements Serializable {

        public int id;
        public int user_id;
        public String image_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getImage_name() {
            return image_name;
        }

        public void setImage_name(String image_name) {
            this.image_name = image_name;
        }

}
