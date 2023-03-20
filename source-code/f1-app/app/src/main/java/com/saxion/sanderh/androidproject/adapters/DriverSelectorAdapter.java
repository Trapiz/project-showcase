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

public class DriverSelectorAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final JSONArray drivers;

    public DriverSelectorAdapter(Context context, JSONArray drivers) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.drivers = drivers;
    }

    @Override
    public int getCount() {
        return drivers.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return drivers.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject driver = getItem(position);
        View v;

        if (convertView != null) v = convertView;
        else v = mInflater.inflate(R.layout.driver_selector_item_layout, null);

        TextView numberTextView = v.findViewById(R.id.number);
        TextView nameTextView = v.findViewById(R.id.name);
        TextView nationalityTextView = v.findViewById(R.id.nationality);

        String name = driver.optString("givenName") + " " + driver.optString("familyName");

        nameTextView.setText(name);
        numberTextView.setText(driver.optString("permanentNumber"));
        nationalityTextView.setText(driver.optString("nationality"));

        return v;
    }
}
