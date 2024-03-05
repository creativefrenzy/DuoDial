package com.privatepe.host.model.UserListResponseNew;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FemaleImage implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("is_profile_image")
    @Expose
    private Integer isProfileImage;
    @SerializedName("image_name")
    @Expose
    private String imageName;

    public FemaleImage() {
        // Default constructor required by Parcelable
    }

    protected FemaleImage(Parcel in) {
        userId = in.readInt();
        isProfileImage = in.readInt();
        imageName = in.readString();
    }

    public static final Creator<FemaleImage> CREATOR = new Creator<FemaleImage>() {
        @Override
        public FemaleImage createFromParcel(Parcel in) {
            return new FemaleImage(in);
        }

        @Override
        public FemaleImage[] newArray(int size) {
            return new FemaleImage[size];
        }
    };

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsProfileImage() {
        return isProfileImage;
    }

    public void setIsProfileImage(Integer isProfileImage) {
        this.isProfileImage = isProfileImage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(isProfileImage);
        dest.writeString(imageName);
    }
}