package com.rrhostel.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rrhostel.R;
import com.rrhostel.Utility.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServiceRequestFromActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Context mContext;
    private Spinner mSpinnerServices;
    private AutoCompleteTextView et_prefer_date, et_prefer_time;
    private Calendar mSelectedDOBCalendar;

    private int mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request_form);
        // Set up the login form.

        changeStatusBarColor();
        mContext = this;


        ServiceRequestFromActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mContext = this;
        changeStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Service Request form");
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


        et_prefer_date = findViewById(R.id.et_prefer_date);
        et_prefer_time = findViewById(R.id.et_prefer_time);

        mSpinnerServices = findViewById(R.id.sp_services);


        mSelectedDOBCalendar = Calendar.getInstance();
        mSelectedDOBCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mSelectedDOBCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        mSelectedDOBCalendar.set(Calendar.YEAR, 1990);
        String dateStr = DateUtils.getDateStr(mSelectedDOBCalendar, DateUtils.DATE_WITHOUT_TIME_SERVER_FORMAT);
        et_prefer_date.setText(dateStr);
        mSpinnerServices.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);

        mSpinnerServices.setOnItemSelectedListener(this);
        String compareValue = "Select Services";

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Laundry Service");
        categories.add("Room Service");
        categories.add("Electrical Service");
        categories.add("Carpenter Service");
        categories.add("Paint Service");
        categories.add("Pest Control  Service");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mSpinnerServices.setAdapter(dataAdapter);

        int spinnerPosition = dataAdapter.getPosition(compareValue);
        mSpinnerServices.setSelection(spinnerPosition);


        et_prefer_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();

            }
        });

        et_prefer_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePicker();
                    et_prefer_date.clearFocus();
                }
            }
        });


        et_prefer_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();

            }
        });

        et_prefer_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    timePicker();
                    et_prefer_time.clearFocus();
                }
            }
        });

    }

    private void timePicker() {


        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        et_prefer_time.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void datePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mSelectedDOBCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mSelectedDOBCalendar.set(Calendar.MONTH, monthOfYear);
                        mSelectedDOBCalendar.set(Calendar.YEAR, year);
                        String dateStr = DateUtils.getDateStr(mSelectedDOBCalendar, DateUtils.DATE_WITHOUT_TIME_SERVER_FORMAT);
                        et_prefer_date.setText(dateStr);

                    }
                },
                mSelectedDOBCalendar.get(Calendar.YEAR),
                mSelectedDOBCalendar.get(Calendar.MONTH),
                mSelectedDOBCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());


        datePickerDialog.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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
