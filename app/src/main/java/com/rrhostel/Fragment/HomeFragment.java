package com.rrhostel.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rrhostel.Activity.HomeActivity;
import com.rrhostel.R;


public class HomeFragment extends Fragment {

    private View mMainView;
    private Context mContext;
    private TabLayout allTabs;
    private LatestFragment fragmentOne;
    private ServiceRequestFragment fragmentTwo;


    public HomeFragment() {
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
        mMainView = inflater.inflate(R.layout.fragment_home, container, false);

        ((HomeActivity) mContext).setTitle("R.R.Hostel");

        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();
        return mMainView;
    }


    private void getAllWidgets() {
        allTabs = mMainView.findViewById(R.id.tab_layout);

    }

    private void setupTabLayout() {
        fragmentOne = new LatestFragment();
        fragmentTwo = new ServiceRequestFragment();
        allTabs.addTab(allTabs.newTab().setText(getResources().getString(R.string.LatestText)), true);
        allTabs.addTab(allTabs.newTab().setText(getResources().getString(R.string.ServiceRequestText)), false);

        LinearLayout linearLayout = (LinearLayout)allTabs.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.WHITE);
        drawable.setSize(2, 50);
        linearLayout.setDividerPadding(5);
        linearLayout.setDividerDrawable(drawable);
    }

    private void bindWidgetsWithAnEvent() {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(fragmentOne);
                break;
            case 1:
                replaceFragment(fragmentTwo);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = null;
        if (fm != null) {
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }




}
