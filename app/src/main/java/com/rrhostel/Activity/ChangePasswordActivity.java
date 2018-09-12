package com.rrhostel.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rrhostel.Bean.LoginResponce;
import com.rrhostel.Bean.StatusBean;
import com.rrhostel.R;
import com.rrhostel.Utility.AppController;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.UIUtils;
import com.rrhostel.Utility.UserUtils;
import com.rrhostel.Utility.Utils;
import com.rrhostel.Utility.ValidatorUtils;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {


    private static final Object TAG = LoginActivity.class.getSimpleName();
    private Context mContext;
    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;

    private AutoCompleteTextView mEtLoginNewPassword, mEtConfirmPassword;
    private AutoCompleteTextView mEtLoginOldPassword;
    private Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chnage_password);
        // Set up the login form.

        ChangePasswordActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mContext = this;
        changeStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Change Password");
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

        mProgressBarLayout = findViewById(R.id.rl_progressBar);
        mEtLoginNewPassword = findViewById(R.id.new_password);
        mEtLoginOldPassword = findViewById(R.id.old_password);
        mEtConfirmPassword = findViewById(R.id.c_password);
        mSubmitBtn = findViewById(R.id.btn_update);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValidation();
            }
        });


    }

    private void setValidation() {

        UIUtils.hideKeyBoard(this);
        if (ValidatorUtils.NotEmptyValidator(mContext, mEtLoginOldPassword, true, getString(R.string.PasswordOld))
                && ValidatorUtils.NotEmptyValidator(mContext, mEtLoginNewPassword, true, getString(R.string.PasswordNew))
                && ValidatorUtils.MinimumLengthValidator(mContext, mEtLoginNewPassword, Constant.MIN_PASSWORD_LENGTH, true,
                getString(R.string.RegisterPasswordMinimumLength))
                && ValidatorUtils.NotEmptyValidator(mContext, mEtConfirmPassword, true, getString(R.string.PasswordConfirm))
                && ValidatorUtils.EqualStrValidator(mContext, mEtLoginNewPassword, mEtConfirmPassword, true,
                getString(R.string.RegisterPasswordMisMatch))) {
            startHttpRequestForResetPassword();

        }


    }


    private void startHttpRequestForResetPassword() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            String baseUrl = Constant.API_CHANGE_PASSWORD;

            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                StatusBean forgetPasswordBean = gson.fromJson(jsonResp, StatusBean.class);
                                if (forgetPasswordBean != null) {

                                    hideProgressBar();
                                    showChangePasswordPopUp(forgetPasswordBean);
                                } else {
                                    hideProgressBar();
                                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle("Alert")
                                            .setMessage(forgetPasswordBean.getMessage())
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                }
                            } catch (Exception e) {
                                hideProgressBar();
                                new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle("Alert")
                                        .setMessage("Unable to change password")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();

                            }

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error instanceof NetworkError) {

                            }
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userId", UserUtils.getInstance().getUserID(mContext));
                    params.put("NewPassword ", mEtLoginNewPassword.getText().toString());
                    params.put("ConfirmNewpassword ", mEtConfirmPassword.getText().toString());
                    return params;
                }
            };
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag(TAG);
            AppController.getInstance().addToRequestQueue(mStrRequest);
            mStrRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } else {
            UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
        }


    }

    private void showChangePasswordPopUp(StatusBean forgetPasswordBean) {

        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                .setTitle(getString(R.string.app_name))
                .setMessage(forgetPasswordBean.getMessage())
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                }).show();
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

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            window.setStatusBarColor(color);
        }
    }

}
