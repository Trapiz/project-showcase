package com.saxion.sanderh.androidproject.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saxion.sanderh.androidproject.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final ArrayList<String> notifications;

    public NotificationAdapter(Context context, ArrayList<String> notifications) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public String getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String notification = getItem(getCount() - 1 - position);
        View v;

        if (convertView != null) v = convertView;
        else v = mInflater.inflate(R.layout.notification_item_layout, null);

        TextView notificationTextView = v.findViewById(R.id.notification);
        notificationTextView.setText(notification);

        return v;
    }
}
