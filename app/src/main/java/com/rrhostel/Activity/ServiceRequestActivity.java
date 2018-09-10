package com.rrhostel.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rrhostel.Adapters.ServiceRequestAdapterForActivity;
import com.rrhostel.Bean.ServiceBean;
import com.rrhostel.R;

import java.util.ArrayList;

public class ServiceRequestActivity extends AppCompatActivity {


    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ServiceRequestAdapterForActivity mServiceAdapter;
    private ArrayList<ServiceBean> mServiceList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request);
        // Set up the login form.

        mContext = this;
        changeStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Service Requests List");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();

    }

    private void init() {

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_service_request);
        mServiceList = new ArrayList<>();

        setOderList();
    }

    private void setOderList() {

        mServiceList = getOrderList();
        mServiceAdapter = new ServiceRequestAdapterForActivity(mContext, mServiceList);
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
            bean.setServiceList("Paint Service");
            bean.setService_status("1");
            catList.add(bean);
        }
        return catList;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            window.setStatusBarColor(color);
        }
    }
}
