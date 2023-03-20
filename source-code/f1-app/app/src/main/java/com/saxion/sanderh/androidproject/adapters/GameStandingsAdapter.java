package com.saxion.sanderh.androidproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saxion.sanderh.androidproject.GameActivity;
import com.saxion.sanderh.androidproject.GameDriver;
import com.saxion.sanderh.androidproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameStandingsAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final ArrayList<GameDriver> standings;

    public GameStandingsAdapter(Context context, ArrayList<GameDriver> standings) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.standings = standings;
    }

    @Override
    public int getCount() {
        return standings.size();
    }

    @Override
    public GameDriver getItem(int position) {
        return standings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GameDriver driver = getItem(position);
        View v;

        if (convertView != null) v = convertView;
        else v = mInflater.inflate(R.layout.result_item_layout, null);

        TextView positionTextView = v.findViewById(R.id.position);

        TextView driverNameTextView = v.findViewById(R.id.item_name);

        TextView statusTextView = v.findViewById(R.id.additional_info);
        TextView gapTextView = v.findViewById(R.id.points);

        positionTextView.setText(String.valueOf(driver.position));
        driverNameTextView.setText(driver.name);

        statusTextView.setText("Racing");

        if (driver.isSelected) {
            v.setBackgroundColor(v.getResources().getColor(R.color.lightgray));
        }

        String gap = "+" + GameActivity.formatTime(driver.gapToCarAhead.getTimeInMillis());
        gapTextView.setText(gap);

        return v;
    };
}
