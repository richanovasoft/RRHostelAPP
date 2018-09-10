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
import android.widget.Toast;

import com.rrhostel.Fragment.HomeFragment;
import com.rrhostel.Fragment.MyCommunityFragment;
import com.rrhostel.Fragment.MyProfileFragment;
import com.rrhostel.Fragment.PaymentRequestFragment;
import com.rrhostel.R;
import com.rrhostel.custom.CustomRegularTextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int BackCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        CustomRegularTextView mNotificationCountTv = item.getActionView().findViewById(R.id.tv_notification_count);
        if (mNotificationCountTv != null) {
            mNotificationCountTv.setVisibility(View.VISIBLE);
        }
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

}
