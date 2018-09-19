package com.rrhostel.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrhostel.Bean.UserFriedListBean;
import com.rrhostel.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFriendListAdapter extends RecyclerView.Adapter<MyFriendListAdapter.MyViewHolder> {

    private List<UserFriedListBean> mFriendsList;
    private Context mContext;


    public MyFriendListAdapter(Context aContext, List<UserFriedListBean> aSearchItemList) {
        this.mFriendsList = aSearchItemList;
        mContext = aContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rc_item_friend, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final UserFriedListBean userFriedListBean = mFriendsList.get(position);

        try {
            if (userFriedListBean != null) {
                holder.txtName.setText(userFriedListBean.getUserFullName());
                holder.txtMessage.setText(userFriedListBean.getFrdEmailId());
                holder.avata.setImageResource(R.drawable.ic_user_profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mFriendsList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avata;
        public TextView txtName, txtTime, txtMessage;

        MyViewHolder(View view) {
            super(view);
            avata = (CircleImageView) itemView.findViewById(R.id.icon_avata);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);

        }
    }

}
