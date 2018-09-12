
package com.rrhostel.Bean;

import com.google.gson.annotations.SerializedName;

public class HomeNotificationBean {

    @SerializedName("date")
    private String mDate;

    @SerializedName("latest_notify")
    private String mLatestNotify;

    @SerializedName("notify_id")
    private String mNotifyId;

    @SerializedName("notify_status")
    private String mNotifyStatus;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getLatestNotify() {
        return mLatestNotify;
    }

    public void setLatestNotify(String latestNotify) {
        mLatestNotify = latestNotify;
    }

    public String getNotifyId() {
        return mNotifyId;
    }

    public void setNotifyId(String notifyId) {
        mNotifyId = notifyId;
    }

    public String getNotifyStatus() {
        return mNotifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        mNotifyStatus = notifyStatus;
    }

}
