package com.rrhostel.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rrhostel.R;
import com.rrhostel.Utility.Constant;
import com.rrhostel.Utility.PrefManager;
import com.rrhostel.Utility.StorageUtils;
import com.rrhostel.custom.CustomRegularTextView;


public class IntroActivity extends AppCompatActivity {


    private LinearLayout mDotsLayout;
    private int[] mLayouts;
    private Context mContext;

    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = this;
        changeStatusBarColor();
        ViewPager mViewPager = findViewById(R.id.view_pager);
        mDotsLayout = findViewById(R.id.layoutDots);


        prefManager = new PrefManager(mContext);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        mLayouts = new int[]{
                R.layout.activity_first_slide,
                R.layout.activity_second_slide};
        addBottomDots(0);

        CustomRegularTextView mText_introSkip = findViewById(R.id.text_introSkip);
        MyViewPagerAdapter mViewPagerAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        mText_introSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchHomeScreen();

            }
        });

    }

    private void addBottomDots(int currentPage) {
        ImageView[] mDots = new ImageView[mLayouts.length];

        mDotsLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new ImageView(this);
            mDots[i].setBackgroundResource(R.drawable.ic_intro_white);
            mDotsLayout.addView(mDots[i]);
            if (i != mDots.length - 1) {
                ImageView hiddenDotIv = new ImageView(this);
                //hiddenDotIv.setBackgroundResource(R.drawable.ic_selected_green_dots);
                hiddenDotIv.setVisibility(View.INVISIBLE);
                mDotsLayout.addView(hiddenDotIv);
            }
        }
        if (mDots.length > 0)
            mDots[currentPage].setBackgroundResource(R.drawable.ic_intro_blue);
    }


    private void launchHomeScreen() {
        // firstCheckLaunch();


        prefManager.setFirstTimeLaunch(false);

        boolean loggedIn = StorageUtils.getPrefForBool(mContext, Constant.PREF_USER_LOGGED_IN);
        Intent intent;
        if (loggedIn) {
            intent = new Intent(mContext, HomeActivity.class);

        } else {
            intent = new Intent(IntroActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(mLayouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return mLayouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void firstCheckLaunch() {
        StorageUtils.putPref(this, Constant.PREF_FIRST_LAUNCH, true);
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
