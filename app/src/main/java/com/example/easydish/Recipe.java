package com.example.easydish;

import java.util.ArrayList;

public class Recipe {

    private String name;
    private String img;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<String> directions = new ArrayList<>();

    public Recipe() {
    }

    public String getName() {
        return name;
    }

    public Recipe setName(String name) {
        this.name = name;
        return this;
    }

    public String getImg() {
        return img;
    }

    public Recipe setImg(String img) {
        this.img = img;
        return this;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Recipe setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public ArrayList<String> getDirections() {
        return directions;
    }

    public Recipe setDirections(ArrayList<String> directions) {
        this.directions = directions;
        return this;
    }
}
