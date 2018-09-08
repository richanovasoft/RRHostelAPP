package com.rrhostel.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrhostel.Adapters.PaymentRequestAdapter;
import com.rrhostel.Bean.PaymentBean;
import com.rrhostel.R;

import java.util.ArrayList;

public class PaymentRequestFragment extends Fragment {

    private View mMainView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private PaymentRequestAdapter mServiceAdapter;
    private ArrayList<PaymentBean> mServiceList;
    private Context mContext;


    public PaymentRequestFragment() {
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
        mMainView = inflater.inflate(R.layout.fragment_payment_request, container, false);
        init();
        return mMainView;
    }

    private void init() {

        mRecyclerView = (RecyclerView) mMainView.findViewById(R.id.rv_payment_request);
        mServiceList = new ArrayList<>();

        setOderList();
    }

    private void setOderList() {

        mServiceList = getOrderList();
        mServiceAdapter = new PaymentRequestAdapter(mContext, mServiceList);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mServiceAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

    }

    public ArrayList<PaymentBean> getOrderList() {
        ArrayList<PaymentBean> catList = new ArrayList<>();


        for (int i = 1; i < 3; i++) {
            PaymentBean bean = new PaymentBean();
            bean.setmDate("8/9/18");
            bean.setmPaymentStatus("Success");
            bean.setmProductDesc("Paint Service");
            bean.setmTID("2c640eaaab2a39cda4da");
            bean.setmTransactionAmount("1000");
            catList.add(bean);
        }
        return catList;
    }
}
