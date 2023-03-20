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

import java.util.Date;

public class ResultAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final JSONArray results;

    public ResultAdapter(Context context, JSONArray results) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return results.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject result = getItem(position);
        View v;

        if (convertView != null) v = convertView;
        else v = mInflater.inflate(R.layout.result_item_layout, null);

        TextView positionTextView = v.findViewById(R.id.position);

        TextView driverNameTextView = v.findViewById(R.id.item_name);

        TextView statusTextView = v.findViewById(R.id.additional_info);
        TextView gapTextView = v.findViewById(R.id.points);
        TextView fastestLapTextView = v.findViewById(R.id.wins);

        JSONObject driver = result.optJSONObject("Driver");
        JSONObject time = result.optJSONObject("Time");
        JSONObject fastestLap = result.optJSONObject("FastestLap");

        if (fastestLap != null) {
            JSONObject fastestLapTime = fastestLap.optJSONObject("Time");
            fastestLapTextView.setText(fastestLapTime.optString("time"));
        }

        positionTextView.setText(result.optString("position"));

        String driverName = driver.optString("givenName") + " " + driver.optString("familyName");
        driverNameTextView.setText(driverName);

        if (time != null) {
            gapTextView.setText(time.optString("time"));
        } else {
            gapTextView.setText("DNF");
        }

        String status = result.optString("status");
        if (status.endsWith("Lap")) {
            statusTextView.setText("Finished");
            gapTextView.setText(status);

        } else {
            statusTextView.setText(status);
        }

        return v;
    }
}
