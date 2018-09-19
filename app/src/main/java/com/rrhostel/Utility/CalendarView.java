/*
 * Copyright (C) 2014 Mukesh Y authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rrhostel.Utility;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rrhostel.Adapters.AndroidListAdapter;
import com.rrhostel.Adapters.CalendarAdapter;
import com.rrhostel.Bean.EventBean;
import com.rrhostel.R;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Mukesh Y
 */
public class CalendarView extends AppCompatActivity {

    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    // public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker
    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;


    private ListView lv_android;
    private AndroidListAdapter list_adapter;

    private Context mContext;

    ArrayList<EventBean> values;

    private String strMonth, strYear;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        Locale.setDefault(Locale.US);


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

        mContext = CalendarView.this;
        values = new ArrayList<>();
        rLayout = (LinearLayout) findViewById(R.id.text);
        month = (GregorianCalendar) GregorianCalendar.getInstance();


        // Calendar calendar = new GregorianCalendar();

       /* strMonth = calendar.get(Calendar.MONTH);

        strYear = calendar.get(Calendar.YEAR);
*/

        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();

        adapter = new CalendarAdapter(this, month);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);


        String strCurrentdate = adapter.addCurrentDate();


        String[] separatedTime = strCurrentdate.split("-");
        strMonth = separatedTime[1];
        strYear = separatedTime[0];

        System.out.println("strCurrentdate = " + strCurrentdate);


        //handler = new Handler();
        // handler.post(calendarUpdater);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
               /* // removing the previous view if added
                if (((LinearLayout) rLayout).getChildCount() > 0) {
                    ((LinearLayout) rLayout).removeAllViews();
                }
                desc = new ArrayList<String>();
                date = new ArrayList<String>();
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                for (int i = 0; i < Utility.startDates.size(); i++) {
                    if (Utility.startDates.get(i).equals(selectedGridDate)) {
                        desc.add(Utility.nameOfEvent.get(i));
                    }
                }

                if (desc.size() > 0) {
                    for (int i = 0; i < desc.size(); i++) {
                        TextView rowTextView = new TextView(CalendarView.this);

                        // set some properties of rowTextView or something
                        rowTextView.setText("Event:" + desc.get(i));
                        rowTextView.setTextColor(Color.BLACK);

                        // add the textview to the linearlayout
                        rLayout.addView(rowTextView);

                    }

                }

                desc = null;

                ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CalendarView.this);
            }
*/

                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);

                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                //((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CalendarView.this);


            }
        });


        setEvents();
    }

    private void setEvents() {

        lv_android = (ListView) findViewById(R.id.lv_android);

        startHttpRequestForAddEvents(strMonth, strYear);

    }

    private void startHttpRequestForAddEvents(final String strMonth, final String strYear) {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            String baseUrl = Constant.API_EVENT_LIST;
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                if (response.startsWith("[")) {

                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<EventBean>>() {
                                    }.getType();
                                    ArrayList<EventBean> posts = gson.fromJson(response, listType);
                                    if (posts != null && posts.size() > 0) {
                                        setEventItemList(posts);
                                    } else {
                                        lv_android.setVisibility(View.GONE);
                                    }
                                } else {

                                    JSONObject root = new JSONObject(response);
                                    String strStatus = root.optString("status");
                                    String strMsg = root.optString("message");
                                    if (strStatus.equals("Failed")) {
                                        lv_android.setVisibility(View.GONE);

                                    }

                                }

                            } catch (Exception e) {
                                lv_android.setVisibility(View.GONE);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getClass().equals(NoConnectionError.class)) {
                                lv_android.setVisibility(View.GONE);
                            } else {
                                UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("month", "" + strMonth);
                    params.put("year", "" + strYear);
                    return params;
                }
            };
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag("");
            AppController.getInstance().addToRequestQueue(mStrRequest);
            mStrRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));
            lv_android.setVisibility(View.GONE);
        }
    }


    public void setEventItemList(ArrayList<EventBean> posts) {
        lv_android.setVisibility(View.VISIBLE);
        values = posts;
        list_adapter = new AndroidListAdapter(CalendarView.this, R.layout.list_item, values);
        lv_android.setAdapter(list_adapter);
    }

    protected void setNextMonth() {

        System.out.println(">>>>GregorianCalendar.MONTH = " + GregorianCalendar.MONTH);
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);

        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);

            //strMonth = GregorianCalendar.MONTH + 1;
            //strYear = GregorianCalendar.YEAR;
        }


        String selectedDate = adapter.addSelectedDate();
        String[] separatedTime = selectedDate.split("-");
        strMonth = separatedTime[1];
        strYear = separatedTime[0];

        startHttpRequestForAddEvents(strMonth, strYear);


        /*Calendar calendar = new GregorianCalendar();

        //calendar.add(Calendar.MONTH, 1);

        strMonth = calendar.get(Calendar.MONTH + 1);

        strYear = calendar.get(Calendar.YEAR);
*/

        //startHttpRequestForAddEvents(strMonth, strYear);

    }

    protected void setPreviousMonth() {

        System.out.println(">>>>GregorianCalendar.MONTH = " + GregorianCalendar.MONTH);
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);

        }

        String selectedDate = adapter.addSelectedDate();

        String[] separatedTime = selectedDate.split("-");
        strMonth = separatedTime[1];
        strYear = separatedTime[0];

        startHttpRequestForAddEvents(strMonth, strYear);


       /* Calendar calendar = new GregorianCalendar();

        //calendar.add(Calendar.MONTH, 1);

        strMonth = calendar.get(Calendar.MONTH - 1);

        strYear = calendar.get(Calendar.YEAR);


        startHttpRequestForAddEvents(strMonth, strYear);*/

    }

    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

    }

    public void refreshCalendar() {
        TextView title = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        // handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String itemvalue;
            event = Utility.readCalendarEvent(CalendarView.this);
            Log.d("=====Event====", event.toString());
            Log.d("=====Date ARRAY====", Utility.startDates.toString());

            for (int i = 0; i < Utility.startDates.size(); i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(GregorianCalendar.DATE, 1);
                items.add(Utility.startDates.get(i).toString());
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };
}
