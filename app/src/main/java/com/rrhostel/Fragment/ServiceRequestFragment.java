package com.rrhostel.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rrhostel.Activity.ServiceRequestFromActivity;
import com.rrhostel.Adapters.ServiceRequestAdapter;
import com.rrhostel.Bean.ServiceBean;
import com.rrhostel.R;

import java.util.ArrayList;

public class ServiceRequestFragment extends Fragment {

    private View mMainView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ServiceRequestAdapter mServiceAdapter;
    private ArrayList<ServiceBean> mServiceList;
    private Context mContext;


    private RelativeLayout rl_plus;

    public ServiceRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_service_request, container, false);
        init();
        return mMainView;
    }

    private void init() {

        mRecyclerView = (RecyclerView) mMainView.findViewById(R.id.rv_service_request);
        rl_plus = mMainView.findViewById(R.id.rl_plus);
        rl_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ServiceRequestFromActivity.class);
                startActivity(intent);
            }
        });
        mServiceList = new ArrayList<>();

        setOderList();
    }

    private void setOderList() {

        mServiceList = getOrderList();
        mServiceAdapter = new ServiceRequestAdapter(mContext, mServiceList);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mServiceAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

    }

    public ArrayList<ServiceBean> getOrderList() {
        ArrayList<ServiceBean> catList = new ArrayList<>();


        for (int i = 1; i < 5; i++) {
            ServiceBean bean = new ServiceBean();
            bean.setPreferDate("8/9/18");
            bean.setPreferTime("12:00");
            bean.setServiceList("Paint Service");
            bean.setProblemDescription("In-Progress");
            bean.setService_status("1");
            catList.add(bean);
        }
        return catList;
    }
}
