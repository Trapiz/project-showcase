package com.saxion.sanderh.androidproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.saxion.sanderh.androidproject.AppDatabase;
import com.saxion.sanderh.androidproject.DriverSelectorActivity;
import com.saxion.sanderh.androidproject.R;
import com.saxion.sanderh.androidproject.daos.PlayerDao;
import com.saxion.sanderh.androidproject.models.Player;

import java.util.List;

public class PlayerAdapter extends BaseAdapter {

    final LayoutInflater mInflater;
    final Context context;
    final List<Player> players;

    public PlayerAdapter(Context context, List<Player> players) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.players = players;
    }

    @Override
    public int getCount() {
        return this.players.size();
    }

    @Override
    public Player getItem(int position) {
        return this.players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Player player = getItem(position);

        View v = mInflater.inflate(R.layout.player_item_layout, null);
        TextView playerTextView = v.findViewById(R.id.player_name);
        Button deleteButton = v.findViewById(R.id.delete);
        Button selectButton = v.findViewById(R.id.select);

        deleteButton.setOnClickListener(view -> {
            PlayerDao dao = AppDatabase.getInstance(view.getContext()).PlayerDao();
            dao.deletePlayer(player);
        });

        selectButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, DriverSelectorActivity.class);
            intent.putExtra("id", player.id);
            context.startActivity(intent);
        });

        playerTextView.setText(player.name);

        return v;

    }
}
