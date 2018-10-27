
package com.rrhostel.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SelectedMealBeanResponse {

    @SerializedName("data")
    private ArrayList<SelectedMealBean> mData;

    public ArrayList<SelectedMealBean> getData() {
        return mData;
    }

    public void setData(ArrayList<SelectedMealBean> data) {
        mData = data;
    }

}
