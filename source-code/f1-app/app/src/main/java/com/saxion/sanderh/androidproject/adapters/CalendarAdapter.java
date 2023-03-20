package com.saxion.sanderh.androidproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.saxion.sanderh.androidproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CalendarAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final JSONArray races;

    final SimpleDateFormat prevDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
    final SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);

    public CalendarAdapter(Context context, JSONArray races) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.races = races;
    }

    @Override
    public int getCount() {
        return races.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return races.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject race = getItem(position);
        View v;

        if (convertView != null) v = convertView;
        else v = mInflater.inflate(R.layout.race_item_layout, null);

        TextView roundNoTextView = v.findViewById(R.id.position);
        TextView raceNameTextView = v.findViewById(R.id.item_name);
        TextView circuitNameTextView = v.findViewById(R.id.additional_info);
        TextView circuitLocationTextView = v.findViewById(R.id.wins);
        TextView raceDateTextView = v.findViewById(R.id.race_date);

        if (position % 2 != 0) {
            v.setBackgroundColor(ContextCompat.getColor(context, R.color.lightergray));
        } else {
            v.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        }

        JSONObject circuit = race.optJSONObject("Circuit");

        roundNoTextView.setText(race.optString("round"));
        raceNameTextView.setText(race.optString("raceName").replace("Grand Prix", "GP"));
        circuitNameTextView.setText(circuit.optString("circuitName"));

        JSONObject location = circuit.optJSONObject("Location");
        String circuitLocation = location.optString("locality") + ", " + location.optString("country");

        circuitLocationTextView.setText(circuitLocation);
        raceDateTextView.setText(getRaceDate(race));

        return v;
    }

    private String getRaceDate(JSONObject race) {

        String date = null;
        try {
            Date prevDate = prevDateFormat.parse(race.optString("date"));
            date = newDateFormat.format(prevDate).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
