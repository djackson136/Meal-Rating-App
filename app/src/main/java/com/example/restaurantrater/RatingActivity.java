package com.example.restaurantrater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RatingActivity extends AppCompatActivity {

    private Meal currentMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initMeal(extras.getInt("mealID"));
        } else {
            currentMeal = new Meal();
        }
        initSaveButton();
        initTextChangedEvents();
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar rating = findViewById(R.id.ratingMeal);
                String mealRating = String.valueOf(rating.getRating());
                currentMeal.setMealRating(mealRating);

                boolean wasSuccessful;
                RestaurantDataSource ds = new RestaurantDataSource(RatingActivity.this);
                try {
                    ds.open();
                    if (currentMeal.getMealID() == -1) {
                        wasSuccessful = ds.insertMeal(currentMeal);
                        if (wasSuccessful) {
                            int newId = ds.getLastMealID();
                            currentMeal.setMealID(newId);
                        }
                    } else {
                        wasSuccessful = ds.updateMeal(currentMeal);
                    }
                    ds.close();

                    Intent intent = new Intent(RatingActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (Exception e) {
                    wasSuccessful = false;
                }
            }
        });
    }


    private void initTextChangedEvents() {
        final EditText etMealName = findViewById(R.id.editName);
        etMealName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMeal.setMealName((etMealName.getText().toString()));
            }
        });

        final EditText etMealType = findViewById(R.id.editType);
        etMealType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMeal.setMealType(etMealType.getText().toString());
            }
        });
    }


    private void initMeal(int id) {
        RestaurantDataSource ds = new RestaurantDataSource(RatingActivity.this);
        try {
            ds.open();
            currentMeal = ds.getSpecificMeal(id);
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this, "Load Contact Failed", Toast.LENGTH_LONG).show();
        }

        EditText editName = findViewById(R.id.editName);
        EditText editType = findViewById(R.id.editType);
        RatingBar rating = findViewById(R.id.ratingMeal);

        editName.setText(currentMeal.getMealName());
        editType.setText(currentMeal.getMealType());
        rating.setRating(Float.parseFloat(currentMeal.getMealRating()));
    }

}