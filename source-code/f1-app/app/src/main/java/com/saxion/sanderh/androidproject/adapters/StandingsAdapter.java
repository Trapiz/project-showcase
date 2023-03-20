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

public class StandingsAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final JSONArray standings;

    public StandingsAdapter(Context context, JSONArray standings) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.standings = standings;
    }

    @Override
    public int getCount() {
        return standings.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return standings.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        JSONObject standing = getItem(position);
        View v;

        if (convertView != null) v = convertView;
        else v = mInflater.inflate(R.layout.standings_item_layout, null);

        TextView positionTextView = v.findViewById(R.id.position);
        TextView itemNameTextView = v.findViewById(R.id.item_name);
        TextView winsTextView = v.findViewById(R.id.wins);
        TextView additionalInfoTextView = v.findViewById(R.id.additional_info);
        TextView pointsTextView = v.findViewById(R.id.points);

        JSONObject driver = standing.optJSONObject("Driver");

        if (driver != null) {

            String name = driver.optString("givenName") + " " + driver.optString("familyName");
            itemNameTextView.setText(name);

            JSONObject constructor = standing.optJSONArray("Constructors").optJSONObject(0);
            additionalInfoTextView.setText(constructor.optString("name"));

        } else {
            JSONObject constructor = standing.optJSONObject("Constructor");
            itemNameTextView.setText(constructor.optString("name"));
            additionalInfoTextView.setText(constructor.optString("nationality"));
        }

        positionTextView.setText(standing.optString("position"));
        winsTextView.setText(standing.optString("wins"));
        pointsTextView.setText(standing.optString("points"));


        return v;
    };
}
