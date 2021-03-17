package com.example.restaurantrater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Meal> meals;
    RestaurantAdapter restaurantAdapter;
    RecyclerView mealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDeleteSwitch();
    }

    @Override
    public void onResume() {
        super.onResume();
        RestaurantDataSource ds = new RestaurantDataSource(this);
        try {
            ds.open();
            meals = ds.getMeals();
            ds.close();
            if (meals.size() > 0) {
                mealList = findViewById(R.id.rvContacts);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                mealList.setLayoutManager(layoutManager);
                restaurantAdapter = new RestaurantAdapter(meals, MainActivity.this);
                mealList.setAdapter(restaurantAdapter);
                restaurantAdapter.setOnItemClickListener(onItemClickListener);
            } else {
                Intent intent = new Intent(MainActivity.this, RatingActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving meals", Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            int mealId = meals.get(position).getMealID();
            Intent intent = new Intent(MainActivity.this, RatingActivity.class);
            intent.putExtra("mealID", mealId);
            startActivity(intent);
        }
    };


    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean status = buttonView.isChecked();
                restaurantAdapter.setDelete(status);
                restaurantAdapter.notifyDataSetChanged();
            }
        });
    }

}