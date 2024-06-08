package com.example.getfit_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MealAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Meal> meals;

    public MealAdapter(Context context, ArrayList<Meal> meals) {
        this.context = context;
        this.meals = meals;
    }

    @Override
    public int getCount() {
        return meals.size();
    }

    @Override
    public Object getItem(int position) {
        return meals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_meal, parent, false);
        }

        TextView mealName = convertView.findViewById(R.id.mealName);
        TextView mealDetails = convertView.findViewById(R.id.mealDetails);

        Meal meal = (Meal) getItem(position);

        mealName.setText(meal.getName());
        mealDetails.setText(meal.getDetails());

        return convertView;
    }
}


