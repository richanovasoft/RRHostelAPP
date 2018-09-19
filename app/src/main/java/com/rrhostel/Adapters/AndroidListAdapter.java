package com.rrhostel.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;

import com.rrhostel.Bean.EventBean;
import com.rrhostel.R;
import com.rrhostel.custom.CustomBoldTextView;
import com.rrhostel.custom.CustomRegularTextView;

import java.util.ArrayList;


public class AndroidListAdapter extends ArrayAdapter<EventBean> {

    private Context context;
    private ArrayList<EventBean> values;
    private ViewHolder viewHolder;
    private final int resourceId;
    private boolean isDescExpanded;


    public AndroidListAdapter(Context context, int resourceId, ArrayList<EventBean> values) {
        super(context, resourceId, values);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.values = values;
        this.resourceId = resourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, parent, false);


            viewHolder = new ViewHolder();
            viewHolder.tv_date = convertView.findViewById(R.id.tv_date);
            viewHolder.tv_event = convertView.findViewById(R.id.tv_event);
            viewHolder.mTvReadMore = convertView.findViewById(R.id.tv_read_more);


            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        EventBean list_obj = values.get(position);
        viewHolder.tv_date.setText("Date: " + list_obj.getUpdatedDate());
        viewHolder.tv_event.setText("Event: " + list_obj.getDescription());

        viewHolder.tv_event.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isDescExpanded) {
                    if (viewHolder.tv_event.getLineCount() > 4) {
                        viewHolder.mTvReadMore.setVisibility(View.VISIBLE);
                        ObjectAnimator animation = ObjectAnimator.ofInt(viewHolder.tv_event, "maxLines", 4);
                        animation.setDuration(0).start();
                    } else {
                        viewHolder.mTvReadMore.setVisibility(View.GONE);
                    }
                    isDescExpanded = false;

                }
            }
        });


        viewHolder.mTvReadMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!isDescExpanded) {
                    isDescExpanded = true;
                    ObjectAnimator animation = ObjectAnimator.ofInt(viewHolder.tv_event, "maxLines", 40);
                    animation.setDuration(10).start();
                    viewHolder.mTvReadMore.setText("Show less");
                    notifyDataSetChanged();

                } else {
                    isDescExpanded = false;
                    ObjectAnimator animation = ObjectAnimator.ofInt(viewHolder.tv_event, "maxLines", 4);
                    animation.setDuration(10).start();
                    viewHolder.mTvReadMore.setText("Show more...");
                    notifyDataSetChanged();

                }
            }
        });

        return convertView;
    }


    public class ViewHolder {

        CustomRegularTextView tv_event;
        CustomRegularTextView tv_date;
        CustomRegularTextView mTvReadMore;

    }
}
