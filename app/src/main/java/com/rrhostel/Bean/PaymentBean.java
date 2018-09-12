package com.rrhostel.Bean;

import com.google.gson.annotations.SerializedName;

public class PaymentBean {

    @SerializedName("date")
    private String mDate;

    @SerializedName("txnid")
    private String mTID;

    @SerializedName("amount")
    private String mTransactionAmount;

    @SerializedName("productinfo")
    private String mProductDesc;

    @SerializedName("payu_status")
    private String mPaymentStatus;

    @SerializedName("email")
    private String mStrEmail;

    @SerializedName("userid")
    private String mStrUserId;

    @SerializedName("transaction_id")
    private String mUserTransationId;

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTID() {
        return mTID;
    }

    public void setmTID(String mTID) {
        this.mTID = mTID;
    }

    public String getmTransactionAmount() {
        return mTransactionAmount;
    }

    public void setmTransactionAmount(String mTransactionAmount) {
        this.mTransactionAmount = mTransactionAmount;
    }

    public String getmProductDesc() {
        return mProductDesc;
    }

    public void setmProductDesc(String mProductDesc) {
        this.mProductDesc = mProductDesc;
    }

    public String getmPaymentStatus() {
        return mPaymentStatus;
    }

    public void setmPaymentStatus(String mPaymentStatus) {
        this.mPaymentStatus = mPaymentStatus;
    }


    public String getmStrEmail() {
        return mStrEmail;
    }

    public void setmStrEmail(String mStrEmail) {
        this.mStrEmail = mStrEmail;
    }

    public String getmStrUserId() {
        return mStrUserId;
    }

    public void setmStrUserId(String mStrUserId) {
        this.mStrUserId = mStrUserId;
    }

    public String getmUserTransationId() {
        return mUserTransationId;
    }

    public void setmUserTransationId(String mUserTransationId) {
        this.mUserTransationId = mUserTransationId;
    }
}
