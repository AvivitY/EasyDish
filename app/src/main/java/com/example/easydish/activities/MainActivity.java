package com.example.easydish.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.easydish.data.Ingredient;
import com.example.easydish.R;
import com.example.easydish.data.Recipe;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    MaterialButton main_BTN_search;
    AutoCompleteTextView autoComplete;
    RadioButton radio_name, radio_ing;
    TextInputLayout main_name_layout,main_ing_layout;
    TextInputEditText main_EDT_name;
    LinearLayout list;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TreeSet<String> ingredients;
    TreeSet<String> chosenIng;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        findViews();
        ingredients = new TreeSet<>();
        chosenIng = new TreeSet<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("recipes");
        readData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.favorites:
                Intent favorite = new Intent(MainActivity.this, ProfileActivity.class);
                favorite.putExtra("type", "favorites");
                startActivity(favorite);
                break;
            case R.id.my_recipes:
                Intent myRecipes = new Intent(MainActivity.this, ProfileActivity.class);
                myRecipes.putExtra("type", "myRecipes");
                startActivity(myRecipes);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(MainActivity.this, Activity_Sign.class);
                startActivity(login);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void readData() {
        // Read once from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    for (Ingredient ing : recipe.getIngredients()) {
                        ingredients.add(ing.getName());
                        Log.d("ccc", "ing is: " + ing.getName());
                    }
                }
                adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_dropdown_item_1line, new ArrayList<>(ingredients));
                autoComplete.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ccc", "Failed to read value.", error.toException());
            }
        });
    }

    private void findViews() {
        main_BTN_search = findViewById(R.id.main_BTN_search);
        autoComplete = findViewById(R.id.autoComplete);
        radio_name = findViewById(R.id.radio_name);
        radio_ing = findViewById(R.id.radio_ing);
        main_EDT_name = findViewById(R.id.main_EDT_name);
        main_ing_layout = findViewById(R.id.main_ing_layout);
        main_name_layout = findViewById(R.id.main_name_layout);


        list = findViewById(R.id.list);
        main_BTN_search.setOnClickListener(view -> {
            Intent results = new Intent(this, ResultsActivity.class);
            if (radio_name.isChecked()) {
                if (!main_EDT_name.getText().toString().equals("")) {
                    main_name_layout.setError("");
                    results.putExtra("type", "name");
                    results.putExtra("name", main_EDT_name.getText().toString());
                    startActivity(results);
                } else {
                    main_name_layout.setError("Please enter search value");
                }
            } else if (radio_ing.isChecked()) {
                if (chosenIng.size() != 0) {
                    main_ing_layout.setError("");
                    results.putExtra("type", "ing");
                    results.putExtra("ingredients", chosenIng);
                    startActivity(results);
                } else {
                    main_ing_layout.setError("Please enter at least one ingredient");
                }
            }
        });

        radio_name.setOnClickListener(view -> {
            main_EDT_name.setEnabled(true);
            autoComplete.setEnabled(false);
            radio_name.setChecked(true);
            radio_ing.setChecked(false);
        });
        radio_ing.setOnClickListener(view -> {
            main_EDT_name.setEnabled(false);
            autoComplete.setEnabled(true);
            radio_name.setChecked(false);
            radio_ing.setChecked(true);
        });

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout ll = new LinearLayout(MainActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 16;
                params.leftMargin = 30;
                ll.setLayoutParams(params);
                TextView ntv = new TextView(MainActivity.this);
                ntv.setText(adapter.getItem(i));
                ntv.setTextSize(20);
                //ntv.setBackgroundColor(0xFF00FF00);
                ntv.setPadding(30, 0, 10, 0);
                LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT
                        , LinearLayout.LayoutParams.WRAP_CONTENT);
                ntv.setLayoutParams(params0);

                ImageButton ib = new ImageButton(MainActivity.this);
                ib.setImageResource(R.drawable.close);
                ib.setBackgroundColor(0x00000000);
                ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        100
                        , 100);
                ib.setLayoutParams(params1);
                ll.setBackgroundResource(R.drawable.ingredients_corners);
                ll.addView(ntv);
                ll.addView(ib);
                ib.setOnClickListener(view1 -> {
                    list.removeView(ll);
                    chosenIng.remove(adapter.getItem(i));
                });
                list.addView(ll);
                autoComplete.setText("");
                chosenIng.add(adapter.getItem(i));
            }
        });
    }

}