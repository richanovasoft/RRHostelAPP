package com.rrhostel.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rrhostel.Activity.CalenderActivity;
import com.rrhostel.Activity.HomeActivity;
import com.rrhostel.R;
import com.rrhostel.Utility.CalendarView;

public class MyCommunityFragment extends Fragment {


    private View mMainView;
    private Context mContext;

    private RelativeLayout rl_plus;

    public MyCommunityFragment() {
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
        mMainView = inflater.inflate(R.layout.community_fragment, container, false);

        init();

        ((HomeActivity) mContext).setTitle("My Community");

        return mMainView;

    }

    private void init() {

        rl_plus = mMainView.findViewById(R.id.rl_plus);
        rl_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CalendarView.class);
                startActivity(intent);
            }
        });
    }
}