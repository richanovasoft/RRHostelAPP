package com.rrhostel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.rrhostel.Bean.LoginResponce;
import com.rrhostel.Bean.ServiceBean;
import com.rrhostel.Fragment.HomeFragment;
import com.rrhostel.Fragment.MyCommunityFragment;
import com.rrhostel.Fragment.MyProfileFragment;
import com.rrhostel.Fragment.PaymentRequestFragment;
import com.rrhostel.R;
import com.rrhostel.Utility.AppController;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.UIUtils;
import com.rrhostel.Utility.UserUtils;
import com.rrhostel.Utility.Utils;
import com.rrhostel.custom.CustomBoldTextView;
import com.rrhostel.custom.CustomRegularTextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int BackCount = 0;

    DrawerLayout drawer;

    private ImageView mImgUserProfile;
    private CustomRegularTextView mEtEmail;
    private CustomBoldTextView mEtName;
    private CustomRegularTextView mNotificationCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mEtEmail = headerView.findViewById(R.id.tv_user_email);
        mEtName = headerView.findViewById(R.id.tv_username);
        mImgUserProfile = headerView.findViewById(R.id.imageView);

        LoginResponce loginResponce = UserUtils.getInstance().getUserInfo(HomeActivity.this);

        if (loginResponce.getFullName() != null && loginResponce.getEmail() != null) {

            mEtName.setText(loginResponce.getFullName());
            mEtEmail.setText(loginResponce.getEmail());

        } else {

            mEtName.setVisibility(View.GONE);
            mEtEmail.setVisibility(View.GONE);
        }

        startHttpRequestForServiceRequest();
        displaySelectedScreen(R.id.nav_home);
    }

    private void displaySelectedScreen(int nav_home) {


        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (nav_home) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_profile:
                fragment = new MyProfileFragment();
                break;
            case R.id.nav_community:
                fragment = new MyCommunityFragment();
                break;

            case R.id.nav_payments:
                fragment = new PaymentRequestFragment();
                break;

            case R.id.nav_change_password:
                Intent intent1 = new Intent(HomeActivity.this, ChangePasswordActivity.class);
                startActivity(intent1);
                break;


            case R.id.nav_chat:

                break;

            case R.id.nav_logout:
                Intent intent = new Intent(HomeActivity.this, IntroActivity.class);
                startActivity(intent);
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitAlertDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.action_notification);

        mNotificationCountTv = item.getActionView().findViewById(R.id.tv_notification_count);
        mNotificationCountTv.setVisibility(View.GONE);


        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.performIdentifierAction(item.getItemId(), 0);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_notification:
                handleNotificationButtonClick();
                break;

        }
        return super.onOptionsItemSelected(item);


    }

    public void handleNotificationButtonClick() {
        Intent intent = new Intent(this, ServiceRequestActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectedScreen(item.getItemId());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showExitAlertDialog() {

        if (BackCount == 1) {
            BackCount = 0;
            finish();
        } else {
            Toast.makeText(this, "Press Back again to exit.", Toast.LENGTH_SHORT).show();
            BackCount++;
        }
    }


    private void startHttpRequestForServiceRequest() {

        boolean internetAvailable = Utils.isConnectingToInternet(HomeActivity.this);
        if (internetAvailable) {

            String baseUrl = Constant.API_SERVICE_REQUEST;
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<ServiceBean>>() {
                                }.getType();
                                ArrayList<ServiceBean> posts = gson.fromJson(response, listType);
                                if (posts != null && posts.size() > 0) {
                                    setCountList(posts);
                                } else {


                                }

                            } catch (Exception e) {


                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getClass().equals(NoConnectionError.class)) {

                            } else {

                            }
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("userId", UserUtils.getInstance().getUserID(HomeActivity.this));
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

    private void setCountList(ArrayList<ServiceBean> posts) {
        if (posts.size() > 0) {
            mNotificationCountTv.setVisibility(View.VISIBLE);
            mNotificationCountTv.setText("" + posts.size());

        }
    }

}
