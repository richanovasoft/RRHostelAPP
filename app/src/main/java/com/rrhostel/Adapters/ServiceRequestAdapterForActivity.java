package com.rrhostel.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrhostel.Bean.ServiceBean;
import com.rrhostel.R;
import com.rrhostel.custom.CustomRegularTextView;

import java.util.ArrayList;


public class ServiceRequestAdapterForActivity extends RecyclerView.Adapter<ServiceRequestAdapterForActivity.MyViewHolder> {

    private ArrayList<ServiceBean> mOrderList;
    private Context mContext;


    public ServiceRequestAdapterForActivity(Context aContext, ArrayList<ServiceBean> aOrderlist) {
        this.mContext = aContext;
        this.mOrderList = aOrderlist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_list_for_activity, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final ServiceBean curBean = mOrderList.get(position);
        String status = "";

        if (curBean.getService_status().equals("0")) {

            status = "Pending";
        } else {

            status = "Completed";
        }

        holder.tv_service1.setText("Your submitted service request for " + curBean.getServiceList() + " has been " + status);


    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomRegularTextView tv_service1;


        MyViewHolder(View itemView) {
            super(itemView);
            tv_service1 = itemView.findViewById(R.id.tv1);

        }
    }
}
