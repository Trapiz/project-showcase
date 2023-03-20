package com.saxion.sanderh.androidproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saxion.sanderh.androidproject.R;
import com.saxion.sanderh.androidproject.models.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RaceResultAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final List<Result> results;

    public RaceResultAdapter(Context context, List<Result> results) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Result getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Result result = getItem(position);
        View v;

        if (convertView != null) v = convertView;
        else v = mInflater.inflate(R.layout.result_item_layout, null);

        TextView positionTextView = v.findViewById(R.id.position);
        TextView driverNameTextView = v.findViewById(R.id.item_name);
        TextView gapTextView = v.findViewById(R.id.points);

        positionTextView.setText(String.valueOf(result.position));
        driverNameTextView.setText(result.driverName);
        gapTextView.setText(result.gapToCarAhead);

        return v;
    }
}
