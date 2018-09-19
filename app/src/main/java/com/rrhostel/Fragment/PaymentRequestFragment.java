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
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rrhostel.Activity.HomeActivity;
import com.rrhostel.Adapters.PaymentRequestAdapter;
import com.rrhostel.Bean.PaymentBean;
import com.rrhostel.Bean.ServiceBean;
import com.rrhostel.R;
import com.rrhostel.Utility.AppController;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.UIUtils;
import com.rrhostel.Utility.UserUtils;
import com.rrhostel.Utility.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentRequestFragment extends Fragment {

    private View mMainView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private PaymentRequestAdapter mServiceAdapter;
    private ArrayList<PaymentBean> mServiceList;
    private Context mContext;
    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;


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
        ((HomeActivity) getActivity()).setTitle("Payment");
        init();
        return mMainView;
    }

    private void init() {

        mRecyclerView = (RecyclerView) mMainView.findViewById(R.id.rv_payment_request);
        mServiceList = new ArrayList<>();
        mProgressBarLayout = mMainView.findViewById(R.id.rl_progressBar);

        startHttpRequestForPaymentRequest();
    }


    private void startHttpRequestForPaymentRequest() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {

            String baseUrl = Constant.API_PAYMENT_API;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<PaymentBean>>() {
                                }.getType();
                                ArrayList<PaymentBean> posts = gson.fromJson(response, listType);
                                if (posts != null && posts.size() > 0) {
                                    hideProgressBar();
                                    setOderList(posts);
                                } else {
                                    hideProgressBar();
                                    mRecyclerView.setVisibility(View.GONE);

                                }

                            } catch (Exception e) {
                                hideProgressBar();
                                mRecyclerView.setVisibility(View.GONE);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error.getClass().equals(NoConnectionError.class)) {
                                mRecyclerView.setVisibility(View.GONE);
                            } else {
                                UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("userId", UserUtils.getInstance().getUserID(mContext));
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
            mRecyclerView.setVisibility(View.GONE);
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

    private void setOderList(ArrayList<PaymentBean> posts) {

        if (posts.size() > 0) {
            mServiceList = posts;
            mServiceAdapter = new PaymentRequestAdapter(mContext, mServiceList);
            mLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mServiceAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
        }

    }

}
