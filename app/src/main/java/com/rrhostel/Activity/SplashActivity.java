package com.rrhostel.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rrhostel.R;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.StorageUtils;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;

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
                gotoMain();
            }
        }, Constant.SPLASH_TIME_OUT);
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
}
