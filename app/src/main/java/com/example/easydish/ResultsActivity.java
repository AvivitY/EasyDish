package com.example.easydish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.TreeSet;

public class ResultsActivity extends AppCompatActivity {

    private RecyclerView results_rw;
    private ArrayList<Recipe> results;
    private TreeSet<String> chosenIng;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String type;
    private String name;
    private RecipesAdapter recipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        type = getIntent().getExtras().getString("type","");
        if(type.equals("name")){
            name = getIntent().getExtras().getString("name","");
        }else {
            chosenIng = (TreeSet<String>) getIntent().getExtras().get("ingredients");
        }
        results = new ArrayList<>();
        initRecyclerView();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("recipes");
        findRecipes(chosenIng);
    }

    private void findRecipes(TreeSet<String> ing) {
        readData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setVisible(true);
        //this.invalidateOptionsMenu();
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type Here To Search..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recipesAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.favorites:
                Intent favorite = new Intent(ResultsActivity.this, ProfileActivity.class);
                favorite.putExtra("type","favorites");
                startActivity(favorite);
                finish();
                break;
            case R.id.my_recipes:
                Intent myRecipes = new Intent(ResultsActivity.this, ProfileActivity.class);
                myRecipes.putExtra("type","myRecipes");
                startActivity(myRecipes);
                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(ResultsActivity.this,Activity_Sign.class);
                startActivity(login);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readData() {
        // Read once from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if(type.equals("ing")){
                        ArrayList<String> temp = new ArrayList<>();
                        for (Ingredient ing : recipe.getIngredients()){
                            temp.add(ing.getName());
                            Log.d("ccc", "ing is: " + ing.getName());
                        }
                        if(temp.containsAll(chosenIng)){
                            results.add(recipe);
                        }
                    }else{
                        if(recipe.getName().contains(name)){
                            results.add(recipe);
                        }
                    }
                }
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ccc", "Failed to read value.", error.toException());
            }
        });
    }


    private void initRecyclerView() {
        results_rw = findViewById(R.id.results_rw);
        recipesAdapter = new RecipesAdapter(this, results,false);
        results_rw.setLayoutManager(new LinearLayoutManager(this));
        results_rw.setHasFixedSize(true);
        results_rw.setAdapter(recipesAdapter);

        recipesAdapter.setResultsListener(new RecipesAdapter.ResultsListener() {
            @Override
            public void clicked(Recipe recipe, int position) {
                Intent intent = new Intent(ResultsActivity.this,RecipeActivity.class);
                intent.putExtra("recipe",new Gson().toJson(recipe));
                startActivity(intent);
                Toast.makeText(ResultsActivity.this,recipe.getName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void delete(Recipe recipe, int position) {
                //do nothing
            }
        });
    }
}