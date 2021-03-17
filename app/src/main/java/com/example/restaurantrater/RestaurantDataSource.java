package com.example.restaurantrater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class RestaurantDataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public RestaurantDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertMeal(Meal m) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("name", m.getMealName());
            initialValues.put("type", m.getMealType());
            initialValues.put("rating", m.getMealRating());
            didSucceed = database.insert("meals", null, initialValues) > 0;
        } catch (Exception e) {
            // Do nothing - will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateMeal(Meal m) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) m.getMealID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("name", m.getMealName());
            updateValues.put("type", m.getMealType());
            updateValues.put("rating", m.getMealRating());

            didSucceed = database.update("meals", updateValues, "_id =" + rowId, null) > 0;
        }
        catch (Exception e) {
            // Do nothing - will return false if there is an exception
        }
        return didSucceed;
    }


    public int getLastMealID() {
        int lastId;
        try {
            String query = "SELECT MAX(_id) FROM meals";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<Meal> getMeals() {
        ArrayList<Meal> meals = new ArrayList<Meal>();
        try {
            String query = "SELECT * FROM meals";
            Cursor cursor = database.rawQuery(query, null);

            Meal newMeal;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newMeal = new Meal();
                newMeal.setMealID(cursor.getInt(0));
                newMeal.setMealName(cursor.getString(1));
                newMeal.setMealType(cursor.getString(2));
                newMeal.setMealRating(cursor.getString(3));

                meals.add(newMeal);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            meals = new ArrayList<Meal>();
        }
        return meals;
    }

    public Meal getSpecificMeal(int mealId) {
        Meal meal = new Meal();
        String query = "SELECT * FROM meals WHERE _id =" + mealId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            meal.setMealID(cursor.getInt(0));
            meal.setMealName(cursor.getString(1));
            meal.setMealType(cursor.getString(2));
            meal.setMealRating(cursor.getString(3));

            cursor.close();
        }
        return meal;
    }

    public boolean deleteMeal(int mealId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("meals", "_id =" + mealId, null) > 0;
        }
        catch (Exception e) {
        }
        return didDelete;
    }
}
