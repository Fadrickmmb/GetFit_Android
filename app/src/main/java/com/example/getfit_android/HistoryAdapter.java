package com.example.getfit_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<Day> {

    public HistoryAdapter(Context context, List<Day> days) {
        super(context, 0, days);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
        }

        // Get the data item for this position
        Day day = getItem(position);

        // Lookup view for data population
        TextView dayName = convertView.findViewById(R.id.dayName);
        TextView dayCalories = convertView.findViewById(R.id.dayCalories);

        // Populate the data into the template view using the data object
        dayName.setText("Day " + day.getDayId());
        dayCalories.setText("Calories: " + day.getDayCalories());

        // Return the completed view to render on screen
        return convertView;
    }
}
