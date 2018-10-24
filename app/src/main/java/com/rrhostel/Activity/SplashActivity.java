package com.rrhostel.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import com.rrhostel.Bean.LoginResponce;
import com.rrhostel.R;
import com.rrhostel.Utility.AppController;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.StorageUtils;
import com.rrhostel.Utility.UserUtils;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    public static final int RequestPermissionCode = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleSplashTimeout();
            }
        }, Constant.SPLASH_TIME_OUT);

        boolean loggedIn = StorageUtils.getPrefForBool(mContext, Constant.PREF_USER_LOGGED_IN);
        if (loggedIn) {
            startHttpRequestForUserInfo();
        }
    }

    private void handleSplashTimeout() {


        if (CheckingPermissionIsEnabledOrNot()) {
            gotoMain();
        } else {
            RequestMultiplePermission();
        }
    }

    private void gotoMain() {

        boolean loggedIn = StorageUtils.getPrefForBool(mContext, Constant.PREF_USER_LOGGED_IN);
        Intent intent;
        if (loggedIn) {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
        } else {
            boolean skipIntro = StorageUtils.getPrefForBool(mContext, Constant.PREF_FIRST_LAUNCH);
            if (skipIntro) {
                boolean skipUserAccess = StorageUtils.getPrefForBool(mContext, Constant.PREF_SKIP_USER_ACCESS);
                if (skipUserAccess) {
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
            } else {
                intent = new Intent(SplashActivity.this, IntroActivity.class);

            }

        }
        startActivity(intent);
        finish();
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int ForthPermissionResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);

        return ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        }, RequestPermissionCode);

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean GetAccountsPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean GetLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (GetAccountsPermission && GetLocation) {

                        gotoMain();
                    } else {
                        finish();
                    }
                }

                break;
        }
    }


    public void startHttpRequestForUserInfo() {
        String baseUrl = Constant.API_USER_INFO;
        StringRequest mStrRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Gson gson = new GsonBuilder().create();
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                            LoginResponce loginResponseData = gson.fromJson(jsonResp, LoginResponce.class);
                            if (loginResponseData != null) {
                                UserUtils.getInstance().saveUserInfo(mContext, loginResponseData);

                            } else {
                                // UIUtils.showToast(mContext, getString(R.string.ErrorMsg));
                            }
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", UserUtils.getInstance().getUserID(mContext));
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

}
