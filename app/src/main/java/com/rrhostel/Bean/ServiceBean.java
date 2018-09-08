package com.rrhostel.Bean;

import com.google.gson.annotations.SerializedName;

public class ServiceBean {

    @SerializedName("service_id")
    private String mServiceId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("serviceList")
    private String serviceList;

    @SerializedName("problemDescription")
    private String problemDescription;

    @SerializedName("preferDate")
    private String preferDate;

    @SerializedName("preferTime")
    private String preferTime;


    @SerializedName("service_status")
    private String service_status;

    @SerializedName("service_notify_status")
    private String service_notify_status;

    @SerializedName("date")
    private String date;


    public String getmServiceId() {
        return mServiceId;
    }

    public void setmServiceId(String mServiceId) {
        this.mServiceId = mServiceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceList() {
        return serviceList;
    }

    public void setServiceList(String serviceList) {
        this.serviceList = serviceList;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getPreferDate() {
        return preferDate;
    }

    public void setPreferDate(String preferDate) {
        this.preferDate = preferDate;
    }

    public String getPreferTime() {
        return preferTime;
    }

    public void setPreferTime(String preferTime) {
        this.preferTime = preferTime;
    }

    public String getService_status() {
        return service_status;
    }

    public void setService_status(String service_status) {
        this.service_status = service_status;
    }

    public String getService_notify_status() {
        return service_notify_status;
    }

    public void setService_notify_status(String service_notify_status) {
        this.service_notify_status = service_notify_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
