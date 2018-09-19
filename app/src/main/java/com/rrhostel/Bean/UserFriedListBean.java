package com.rrhostel.Bean;

import com.google.gson.annotations.SerializedName;

public class UserFriedListBean {

    @SerializedName("friend_id")
    private int mFrdId;

    @SerializedName("firstname")
    private String mFirstName;

    @SerializedName("email")
    private String mFrdEmailId;

    @SerializedName("lastname")
    private String mLastName;

    @SerializedName("picture")
    private String mFrdProfile;


    @SerializedName("remove_id")
    private String mRemoveId;


    public int getFrdId() {
        return mFrdId;
    }

    public void setFrdId(int mFrdId) {
        this.mFrdId = mFrdId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getFrdEmailId() {
        return mFrdEmailId;
    }

    public void setFrdEmailId(String mFrdEmailId) {
        this.mFrdEmailId = mFrdEmailId;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getRemoveId() {
        return mRemoveId;
    }

    public void setRemoveId(String mRemoveId) {
        this.mRemoveId = mRemoveId;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getFrdProfile() {
        return mFrdProfile;
    }

    public void setFrdProfile(String mFrdProfile) {
        this.mFrdProfile = mFrdProfile;
    }


    public String getUserFullName() {
        return (mFirstName + " " + " " + mLastName).trim();
    }
}
