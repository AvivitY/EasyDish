package com.example.easydish;

import java.util.ArrayList;

public class DataManager {

    public DataManager() {
    }

    public static ArrayList<Recipe> generateData() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient().setName("water").setMeasurement("CUPS").setQty(3));
        ingredients.add(new Ingredient().setName("flour").setMeasurement("CUPS").setQty(5));
        ArrayList<String> directions = new ArrayList<>();
        directions.add("put water and flour");
        directions.add("mix it together");
        recipes.add(new Recipe().setName("hhh").setIngredients(ingredients).setDirections(directions));

        return recipes;
    }


}
