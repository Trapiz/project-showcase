package com.saxion.sanderh.androidproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saxion.sanderh.androidproject.R;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class SessionAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final ArrayList<JSONObject> sessions;

    public SessionAdapter(Context context, ArrayList<JSONObject> sessions) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.sessions = sessions;
    }

    @Override
    public int getCount() {
        return this.sessions.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return this.sessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        JSONObject session = getItem(position);
        View v;

        if (convertView != null) v = convertView;
        else v = mInflater.inflate(R.layout.session_item_layout, null);

        TextView sessionNameTextView = v.findViewById(R.id.session_name);
        TextView dateTextView = v.findViewById(R.id.date);
        TextView timeTextView = v.findViewById(R.id.time);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            String date = session.optString("date");
            String time = session.optString("time").replace("Z", "");

            Date datetime = new Date();
            datetime.setTime(sdf.parse(date + " " + time).getTime());

            System.out.println(datetime);

            dateTextView.setText(dateFormat.format(datetime));
            timeTextView.setText(timeFormat.format(datetime));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        sessionNameTextView.setText(session.optString("sessionName"));


        return v;
    }
}
