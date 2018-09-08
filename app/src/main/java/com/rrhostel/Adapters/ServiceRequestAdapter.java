package com.rrhostel.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rrhostel.Bean.ServiceBean;
import com.rrhostel.R;
import com.rrhostel.custom.CustomBoldTextView;
import com.rrhostel.custom.CustomRegularTextView;

import java.util.ArrayList;
import java.util.List;


public class ServiceRequestAdapter extends RecyclerView.Adapter<ServiceRequestAdapter.MyViewHolder> {

    private ArrayList<ServiceBean> mOrderList;
    private Context mContext;


    public ServiceRequestAdapter(Context aContext, ArrayList<ServiceBean> aOrderlist) {
        this.mContext = aContext;
        this.mOrderList = aOrderlist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_list, parent, false);

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

        holder.tv_service1.setText(curBean.getServiceList() + " (" + status + " )");
        holder.tv_service2.setText(curBean.getProblemDescription());
        holder.tv_service3.setText(curBean.getPreferDate());
        holder.tv_service4.setText(curBean.getPreferTime());


    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomRegularTextView tv_service1;
        CustomRegularTextView tv_service2;
        CustomRegularTextView tv_service3;
        CustomRegularTextView tv_service4;


        MyViewHolder(View itemView) {
            super(itemView);
            tv_service1 = itemView.findViewById(R.id.tv_service1);
            tv_service2 = itemView.findViewById(R.id.tv_service2);
            tv_service3 = itemView.findViewById(R.id.tv_service3);
            tv_service4 = itemView.findViewById(R.id.tv_service4);

        }
    }
}
