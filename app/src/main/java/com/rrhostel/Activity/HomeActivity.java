package com.rrhostel.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
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

import com.rrhostel.Bean.LoginResponce;
import com.rrhostel.Chat.MessageActivity;
import com.rrhostel.Fragment.HomeFragment;
import com.rrhostel.Fragment.MyCommunityFragment;
import com.rrhostel.Fragment.MyProfileFragment;
import com.rrhostel.Fragment.PaymentRequestFragment;
import com.rrhostel.R;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.StorageUtils;
import com.rrhostel.Utility.UserUtils;
import com.rrhostel.custom.CustomBoldTextView;
import com.rrhostel.custom.CustomRegularTextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int BackCount = 0;

    DrawerLayout drawer;

    private ImageView mImgUserProfile;
    private CustomRegularTextView mEtEmail;
    private CustomBoldTextView mEtName;
    private CustomRegularTextView mNotificationCountTv;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private CustomBoldTextView action_toolbar_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        action_toolbar_name = toolbar.findViewById(R.id.action_toolbar_name);

        action_toolbar_name.setText("Tarah");
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


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications

                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra(Constant.INTENT_NOTIFICATION_MSG);
                    String id = intent.getStringExtra(Constant.INTENT_NOTIFICATION_ID);
                    String imageUrl = intent.getStringExtra(Constant.INTENT_NOTIFICATION_IMAGE_URL);
                    String title = intent.getStringExtra(Constant.INTENT_NOTIFICATION_TITLE);
                    //AppData.getInstance().showDialog(SplashActivity.this, title, message, imageUrl, type, id);
                }
            }
        };


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
                Intent intent2 = new Intent(HomeActivity.this, MessageActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                UserUtils.getInstance().clearAll(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
        if (mNotificationCountTv != null) {
            mNotificationCountTv.setVisibility(View.GONE);
        }

        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.performIdentifierAction(item.getItemId(), 0);
            }
        });

        updateNotificationValue();
        return true;
    }

    public void setTitle(String title) {
        action_toolbar_name.setText(title);
    }

    private void updateNotificationValue() {

        if (mNotificationCountTv != null) {
            int count = StorageUtils.getPrefForCount(HomeActivity.this, Constant.NOTIFICATION_COUNTER_VALUE_KEY);
            if (count == 0) {
                mNotificationCountTv.setText(" " + count + " ");
                mNotificationCountTv.setVisibility(View.GONE);
            } else {
                mNotificationCountTv.setText(" " + count + " ");
                mNotificationCountTv.setVisibility(View.VISIBLE);
            }
        }

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

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));

        if (mNotificationCountTv != null) {
            updateNotificationValue();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

}
