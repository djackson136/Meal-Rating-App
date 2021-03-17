package com.example.restaurantrater;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter {
    private ArrayList<Meal> mealData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class MealViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewType;
        public TextView textViewRating;
        public Button deleteButton;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textMealName);
            textViewType = itemView.findViewById(R.id.textMealType);
            textViewRating = itemView.findViewById(R.id.textMealRating);
            deleteButton = itemView.findViewById(R.id.buttonDeleteMeal);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getNameTextView() {
            return textViewName;
        }

        public TextView getTypeTextView() {
            return textViewType;
        }

        public TextView getRatingTextView() {
            return textViewRating;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public RestaurantAdapter(ArrayList<Meal> arrayList, Context context) {
        mealData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MealViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MealViewHolder mvh = (MealViewHolder) holder;
        mvh.getNameTextView().setText(mealData.get(position).getMealName());
        mvh.getTypeTextView().setText(mealData.get(position).getMealType());
        mvh.getRatingTextView().setText(mealData.get(position).getMealRating() + "/5.0");

        if (isDeleting) {
            mvh.getDeleteButton().setVisibility(View.VISIBLE);
            mvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        } else {
            mvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Meal meal = mealData.get(position);
        RestaurantDataSource ds = new RestaurantDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteMeal(meal.getMealID());
            ds.close();
            if (didDelete) {
                mealData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return mealData.size();
    }
}
