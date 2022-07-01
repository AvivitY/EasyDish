package com.example.easydish.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.easydish.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class DirectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<String> directions = new ArrayList<>();
    private int counter = 1;

    public DirectionsAdapter(Activity activity, ArrayList<String> directions){
        this.activity = activity;
        this.directions = directions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_directions, parent, false);
        StringHolder directionHolder = new StringHolder(view);
        return directionHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final StringHolder holder = (StringHolder) viewHolder;
        String direction = directions.get(position);
        holder.count.setText(String.valueOf(counter)+".");
        counter++;
        holder.direction.setText(direction);
    }

    @Override
    public int getItemCount() {
        return directions.size();
    }

    class StringHolder extends RecyclerView.ViewHolder {

        private MaterialTextView direction;
        private MaterialTextView count;

        public StringHolder(View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            direction = itemView.findViewById(R.id.direction);
        }
    }
}
