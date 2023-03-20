package com.saxion.sanderh.androidproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saxion.sanderh.androidproject.R;
import com.saxion.sanderh.androidproject.models.Race;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class PlayerRaceAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final List<Race> races;

    public PlayerRaceAdapter(Context context, List<Race> races) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.races = races;
    }

    @Override
    public int getCount() {
        return races.size();
    }

    @Override
    public Race getItem(int position) {
        return races.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Race race = getItem(position);
        View v;

        if (convertView != null) v = convertView;
        else v = mInflater.inflate(R.layout.result_item_layout, null);

        TextView positionTextView = v.findViewById(R.id.position);
        TextView driverNameTextView = v.findViewById(R.id.item_name);
        TextView fastestLapTextView = v.findViewById(R.id.wins);

        positionTextView.setText(String.valueOf(race.finishedPosition));
        driverNameTextView.setText(race.selectedDriver);
        fastestLapTextView.setText(race.totalLapTime);

        return v;
    }
}
