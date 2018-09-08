package com.rrhostel.Bean;

public class PaymentBean {


    private String mDate;
    private String mTID;
    private String mTransactionAmount;
    private String mProductDesc;
    private String mPaymentStatus;

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
}
