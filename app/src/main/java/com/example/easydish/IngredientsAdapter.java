package com.example.easydish;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    public IngredientsAdapter(Activity activity, ArrayList<Ingredient> ingredients){
        this.activity = activity;
        this.ingredients = ingredients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ingredients, parent, false);
        IngredientHolder ingredientHolder = new IngredientHolder(view);
        return ingredientHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final IngredientHolder holder = (IngredientHolder) viewHolder;
        Ingredient ingredient = ingredients.get(position);
        holder.qty.setText(String.valueOf(ingredient.getQty()));
        holder.measure.setText(ingredient.getMeasurement());
        holder.name.setText(ingredient.getName());

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }



    class IngredientHolder extends RecyclerView.ViewHolder {

        private MaterialTextView qty;
        private MaterialTextView measure;
        private MaterialTextView name;

        public IngredientHolder(View itemView) {
            super(itemView);
            qty = itemView.findViewById(R.id.qty);
            measure = itemView.findViewById(R.id.measure);
            name = itemView.findViewById(R.id.ing);
        }

    }
}
