package com.example.restaurantrater;

import java.util.Calendar;

public class Meal {
    private int mealID;
    private String mealName;
    private String mealType;
    private String mealRating;

    public Meal() {
        mealID = -1;
    }

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getMealRating() {
        return mealRating;
    }

    public void setMealRating(String mealRating) {
        this.mealRating = mealRating;
    }
}
