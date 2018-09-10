package com.rrhostel.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.rrhostel.Adapters.AndroidListAdapter;
import com.rrhostel.Bean.CalendarCollection;
import com.rrhostel.R;
import com.rrhostel.custom.CustomBoldTextView;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity implements OnClickListener {

    private Context mContext;

    private ListView lv_android;
    private AndroidListAdapter list_adapter;
    private CustomBoldTextView btn_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);


        mContext = this;
        changeStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Event Lists");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CalendarCollection.date_collection_arr = new ArrayList<CalendarCollection>();
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-09-01", "John Birthday"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-08-04", "Client Meeting at 5 p.m."));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-04-06", "A Small Party at my office"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-05-02", "Marriage Anniversary"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-04-07", "Live Event and Concert of sonu"));


        getWidget();
    }


    public void getWidget() {
        btn_calender = findViewById(R.id.btn_calender);
        btn_calender.setOnClickListener(this);

        lv_android = (ListView) findViewById(R.id.lv_android);
        list_adapter = new AndroidListAdapter(ListViewActivity.this, R.layout.list_item, CalendarCollection.date_collection_arr);
        lv_android.setAdapter(list_adapter);

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_calender:
                startActivity(new Intent(ListViewActivity.this, CalenderActivity.class));

                break;

            default:
                break;
        }

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