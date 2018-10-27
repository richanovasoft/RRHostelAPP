package com.rrhostel.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rrhostel.Bean.HomeNotificationBean;
import com.rrhostel.Bean.LoginResponce;
import com.rrhostel.Bean.SelectedMealBean;
import com.rrhostel.Bean.SelectedMealBeanResponse;
import com.rrhostel.Bean.StatusBean;
import com.rrhostel.R;
import com.rrhostel.Utility.AppController;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.UIUtils;
import com.rrhostel.Utility.UserUtils;
import com.rrhostel.Utility.Utils;
import com.rrhostel.custom.CustomBoldTextView;
import com.rrhostel.custom.CustomRegularTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LatestFragment extends Fragment {

    private View mMainView;
    private Context mContext;

    private CustomBoldTextView mTvNotification;
    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;
    private CheckBox cb_breakfast, cb_lunch, cb_dinner;

    private String mStrMeal, mStrMeal1, mStrMea2, mStrMea3;
    private Button btn_submit;
    String formattedDate;
    private Dialog dialog;

    public LatestFragment() {
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
        mMainView = inflater.inflate(R.layout.fragment_latest, container, false);
        init();

        return mMainView;
    }

    private void init() {

        mProgressBarLayout = mMainView.findViewById(R.id.rl_progressBar);
        cb_breakfast = mMainView.findViewById(R.id.cb_breakfast);
        cb_lunch = mMainView.findViewById(R.id.cb_Lunch);
        cb_dinner = mMainView.findViewById(R.id.cb_dinner);
        btn_submit = mMainView.findViewById(R.id.btn_submit);

        mTvNotification = mMainView.findViewById(R.id.tv_notification);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c);


        //First CheckBox
        cb_breakfast.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cb_breakfast.isChecked()) {
                    mStrMeal = cb_breakfast.getText().toString();
                }

            }
        });

        //First CheckBox
        cb_lunch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cb_lunch.isChecked()) {
                    mStrMeal = cb_lunch.getText().toString();
                }

            }
        });

        //First CheckBox
        cb_dinner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cb_dinner.isChecked()) {
                    mStrMeal = cb_dinner.getText().toString();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_breakfast.isChecked() || cb_lunch.isChecked() || cb_dinner.isChecked()) {
                    //do some validation

                    startHttpRequestForMeal();
                } else {

                    UIUtils.showToast(mContext, "Please select any meal type.");
                }
            }
        });

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {

            startHttpRequestForHomeNotifcation();
            startHttpRequestForSelectedMeal();
        } else {
            UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));
        }

    }

    private void startHttpRequestForMeal() {
        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {

            String baseUrl = Constant.API_ADD_MEAL;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                StatusBean statusBean = gson.fromJson(jsonResp, StatusBean.class);
                                if (statusBean != null && statusBean.getStatus().equals("Success")) {
                                    hideProgressBar();

                                    btnAlertDialogClicked();

                                } else {
                                    hideProgressBar();
                                    cb_breakfast.setChecked(false);
                                    cb_lunch.setChecked(false);
                                    cb_dinner.setChecked(false);

                                }
                            } catch (Exception e) {
                                hideProgressBar();
                                cb_breakfast.setChecked(false);
                                cb_lunch.setChecked(false);
                                cb_dinner.setChecked(false);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("today_date", formattedDate);
                    params.put("userId", UserUtils.getInstance().getUserID(mContext));
                    params.put("meal", mStrMeal);
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
        }
    }


    public void btnAlertDialogClicked() {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.custom_layout_dialog_meal);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        CustomRegularTextView custom_text_view1 = dialog.findViewById(R.id.custom_text_view1);
        CustomRegularTextView custom_text_view = dialog.findViewById(R.id.custom_text_view);
        custom_text_view1.setText("Enjoy your meal.");
        custom_text_view.setText("Meal");

        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                startHttpRequestForSelectedMeal();

            }
        });
        // show dialog on screen
        dialog.show();
    }

    private void startHttpRequestForHomeNotifcation() {


        String baseUrl = Constant.API_HOME_NOTIFICATION;
        showProgressBar();
        StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Gson gson = new GsonBuilder().create();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                            HomeNotificationBean homeNotificationBean = gson.fromJson(jsonResp, HomeNotificationBean.class);
                            if (homeNotificationBean != null && homeNotificationBean.getLatestNotify() != null) {
                                hideProgressBar();
                                mTvNotification.setText(homeNotificationBean.getLatestNotify());
                            } else {
                                hideProgressBar();

                                mTvNotification.setText(mContext.getResources().getString(R.string.app_name));

                            }
                        } catch (Exception e) {
                            hideProgressBar();

                            mTvNotification.setText(mContext.getResources().getString(R.string.app_name));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();

                    }
                });
        mStrRequest.setShouldCache(false);
        mStrRequest.setTag("");
        AppController.getInstance().addToRequestQueue(mStrRequest);
        mStrRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public void startHttpRequestForSelectedMeal() {
        showProgressBar();
        String baseUrl = Constant.API_SELECTED_MEAL;
        StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Gson gson = new GsonBuilder().create();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                            SelectedMealBeanResponse selectedMealBeanResponse = gson.fromJson(jsonResp, SelectedMealBeanResponse.class);
                            if (selectedMealBeanResponse != null && selectedMealBeanResponse.getData().size() > 0) {

                                hideProgressBar();
                                setChecked(selectedMealBeanResponse.getData());

                            } else {
                                hideProgressBar();
                                cb_breakfast.setChecked(false);
                                cb_lunch.setChecked(false);
                                cb_dinner.setChecked(false);
                            }
                        } catch (Exception e) {
                            hideProgressBar();
                            cb_breakfast.setChecked(false);
                            cb_lunch.setChecked(false);
                            cb_dinner.setChecked(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", UserUtils.getInstance().getUserID(mContext));
                params.put("today_date", formattedDate);
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

    }

    private void setChecked(ArrayList<SelectedMealBean> data) {


        for (int i = 0; i < data.size(); i++) {
            SelectedMealBean selectedMealBean = data.get(i);
            mStrMeal = cb_breakfast.getText().toString();
            mStrMea2 = cb_lunch.getText().toString();
            mStrMea3 = cb_dinner.getText().toString();

            if (mStrMeal.equals(selectedMealBean.getMeal())) {

                cb_breakfast.setChecked(true);
            } else if (mStrMea2.equals(selectedMealBean.getMeal())) {
                cb_lunch.setChecked(true);
            } else if (mStrMea3.equals(selectedMealBean.getMeal())) {
                cb_dinner.setChecked(true);
            } else {
                cb_breakfast.setChecked(false);
                cb_lunch.setChecked(false);
                cb_dinner.setChecked(false);
            }
        }

    }

    private void hideProgressBar() {
        mIsRequestInProgress = false;
        if (mProgressBarShowing) {
            mProgressBarLayout.setVisibility(View.GONE);
            mProgressBarShowing = false;
        }
    }

    private void showProgressBar() {
        if (!mProgressBarShowing) {
            mProgressBarLayout.setVisibility(View.VISIBLE);
            mProgressBarShowing = true;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
