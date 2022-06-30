package com.example.easydish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    private TextView recipe_LBL_title;
    private RecyclerView recipe_LST_ing,recipe_LST_direct;
    private ImageView recipe_IMG_img,recipe_IMG_favorite;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<String> directions = new ArrayList<>();
    private IngredientsAdapter ingredientsAdapter;
    private DirectionsAdapter directionsAdapter;
    private String recipeName;
    private String recipeImg;
    private Recipe recipe;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference photoRef;
    private String uid;
    private boolean isFavorite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        recipe = new Gson().fromJson(getIntent().getExtras().getString("recipe",""),Recipe.class);
        recipeName = recipe.getName();
        recipeImg = recipe.getImg();
        ingredients = recipe.getIngredients();
        directions = recipe.getDirections();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        storage = FirebaseStorage.getInstance();
        photoRef = storage.getReference("images/"+recipeImg);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        findViews();
        isFavorite();
    }

    private void isFavorite(){
        myRef.child(uid).child("favorites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(recipeName)){
                    isFavorite = true;
                    recipe_IMG_favorite.setImageResource(R.drawable.full_heart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("ccc", "Failed to read value.", error.toException());
            }
        });
    }

    private void findViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recipe_IMG_favorite = findViewById(R.id.recipe_IMG_favorite);
        recipe_LBL_title = findViewById(R.id.recipe_LBL_title);
        recipe_IMG_img = findViewById(R.id.recipe_IMG_img);
        recipe_LST_ing = findViewById(R.id.recipe_LST_ing);
        recipe_LST_direct = findViewById(R.id.recipe_LST_direct);

        recipe_IMG_favorite.setOnClickListener(view -> {
            if(isFavorite){
                recipe_IMG_favorite.setImageResource(R.drawable.empty_heart);
                myRef.child(uid).child("favorites").child(recipeName).removeValue();
                isFavorite = false;
            }else {
                recipe_IMG_favorite.setImageResource(R.drawable.full_heart);
                myRef.child(uid).child("favorites").child(recipeName).setValue(recipeName);
                isFavorite = true;
            }
        });

        recipe_LBL_title.setText(recipeName);

        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(RecipeActivity.this).load(downloadUrl)
                        .into(recipe_IMG_img);
            }
        });

        ingredientsAdapter = new IngredientsAdapter(this, ingredients);
        recipe_LST_ing.setLayoutManager(new LinearLayoutManager(this));
        recipe_LST_ing.setHasFixedSize(true);
        recipe_LST_ing.setAdapter(ingredientsAdapter);

        directionsAdapter = new DirectionsAdapter(this,directions);
        recipe_LST_direct.setLayoutManager(new LinearLayoutManager(this));
        recipe_LST_direct.setHasFixedSize(true);
        recipe_LST_direct.setAdapter(directionsAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.favorites:
                Intent favorite = new Intent(RecipeActivity.this, ProfileActivity.class);
                favorite.putExtra("type","favorites");
                startActivity(favorite);
                finish();
                break;
            case R.id.my_recipes:
                Intent myRecipes = new Intent(RecipeActivity.this, ProfileActivity.class);
                myRecipes.putExtra("type","myRecipes");
                startActivity(myRecipes);
                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(RecipeActivity.this,Activity_Sign.class);
                startActivity(login);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}