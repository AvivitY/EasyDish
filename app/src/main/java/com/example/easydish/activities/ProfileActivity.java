package com.example.easydish.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easydish.R;
import com.example.easydish.data.Recipe;
import com.example.easydish.adapters.RecipesAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef, myRef1;
    private RecipesAdapter recipesAdapter;
    private RecyclerView profile_rv;
    private LinearLayout ll;
    private TextView title,profile_LBL_empty;
    private ImageButton add;
    private String type;
    private String uid;
    private ArrayList<String> rec = new ArrayList<>();
    private ArrayList<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        type = getIntent().getExtras().getString("type","");
        findViews();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef1 = database.getReference("recipes");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        readData();
    }

    private void findViews() {
        ll = findViewById(R.id.ll);
        title = findViewById(R.id.title);
        profile_LBL_empty = findViewById(R.id.profile_LBL_empty);
        add = findViewById(R.id.add);
        if (type.equals("favorites")){
            title.setText("Favorites");
            ll.removeView(add);
        }else{
            add.setOnClickListener(view -> {
                openSomeActivityForResult();
            });
        }
    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        onButtonShowPopupWindowClick();
                        readData();
                    }
                }
            });

    private void readData() {
        // Read once from the database
        myRef.child(uid).child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (!dataSnapshot.hasChildren()) {
                    profile_LBL_empty.setVisibility(View.VISIBLE);
                } else {
                    profile_LBL_empty.setVisibility(View.GONE);
                    rec.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String recipeName = snapshot.getValue(String.class);
                        rec.add(recipeName);
                    }
                    getRecipes();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ccc", "Failed to read value.", error.toException());
            }
        });
    }

    private void getRecipes() {
        recipes.clear();
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if (rec.contains(recipe.getName())) {
                        recipes.add(recipe);
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
        profile_rv = findViewById(R.id.profile_rv);
        if (type.equals("favorites")){
            recipesAdapter = new RecipesAdapter(this, recipes,false);
        }else{
            recipesAdapter = new RecipesAdapter(this, recipes,true);
        }
        profile_rv.setLayoutManager(new LinearLayoutManager(this));
        profile_rv.setHasFixedSize(true);
        profile_rv.setAdapter(recipesAdapter);

        recipesAdapter.setResultsListener(new RecipesAdapter.ResultsListener() {
            @Override
            public void clicked(Recipe recipe, int position) {
                Intent intent = new Intent(ProfileActivity.this, RecipeActivity.class);
                intent.putExtra("recipe", new Gson().toJson(recipe));
                startActivity(intent);
            }

            @Override
            public void delete(Recipe recipe, int position) {
               onDeleteShowPopupWindowClick(recipe);
            }
        });
    }

    private void onButtonShowPopupWindowClick() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_window);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MaterialButton close = dialog.findViewById(R.id.popup_close);
        close.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
    private void onDeleteShowPopupWindowClick(Recipe recipe) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_window);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MaterialButton no = dialog.findViewById(R.id.delete_no);
        MaterialButton yes = dialog.findViewById(R.id.delete_yes);
        no.setOnClickListener(view -> dialog.dismiss());
        yes.setOnClickListener(view -> {
            int i = recipes.indexOf(recipe);
            recipes.remove(recipe);
            recipesAdapter.notifyItemRemoved(i);
            myRef.child(uid).child("myRecipes").child(recipe.getName()).removeValue();
            myRef1.child(recipe.getName()).removeValue();
            dialog.dismiss();
        });
        dialog.show();
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
                Intent favorite = new Intent(ProfileActivity.this, ProfileActivity.class);
                favorite.putExtra("type","favorites");
                startActivity(favorite);
                finish();
                break;
            case R.id.my_recipes:
                Intent myRecipes = new Intent(ProfileActivity.this, ProfileActivity.class);
                myRecipes.putExtra("type","myRecipes");
                startActivity(myRecipes);
                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(ProfileActivity.this, Activity_Sign.class);
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