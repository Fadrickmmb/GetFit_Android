package com.example.getfit_android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.List;

public class MainScreenAdapter extends BaseAdapter {
    private Context context;
    private List<Meal> mealList;

    public MainScreenAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    @Override
    public int getCount() {
        return mealList.size();
    }

    @Override
    public Object getItem(int position) {
        return mealList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.meal_list_item, parent, false);
        }

        TextView mealName = convertView.findViewById(R.id.mealNameListItem);
        TextView mealCalories = convertView.findViewById(R.id.mealCaloriesListItem);

        Meal meal = mealList.get(position);
        mealName.setText(meal.getName());
        mealCalories.setText(String.valueOf(meal.getCalories()) + " kcal");
        Log.d("MainScreenAdapter", "Position: " + position + ", Total Meals: " + mealList.size());


        return convertView;
    }
}
