
package com.rrhostel.Bean;


import com.google.gson.annotations.SerializedName;

public class SelectedMealBean {


    @SerializedName("meal")
    private String mMeal;
    @SerializedName("mealnot_id")
    private String mMealnotId;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("today_date")
    private String mTodayDate;
    @SerializedName("userid")
    private String mUserid;

    public String getMeal() {
        return mMeal;
    }

    public void setMeal(String meal) {
        mMeal = meal;
    }

    public String getMealnotId() {
        return mMealnotId;
    }

    public void setMealnotId(String mealnotId) {
        mMealnotId = mealnotId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTodayDate() {
        return mTodayDate;
    }

    public void setTodayDate(String todayDate) {
        mTodayDate = todayDate;
    }

    public String getUserid() {
        return mUserid;
    }

    public void setUserid(String userid) {
        mUserid = userid;
    }
}
