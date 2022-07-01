package com.example.easydish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef, myRef1;
    ArrayList<Ingredient> ingredients;
    ArrayList<String> directions;
    TableLayout tl;
    Spinner add_SPN_measure;
    LinearLayout add_LL_direction;
    MaterialButton add_BTN_img, add_BTN_save;
    ImageButton add_BTN_ing, add_BTN_direct;
    TextInputLayout add_name_layout;
    TextInputEditText add_EDT_name;
    TextView add_LBL_imgErr;
    ImageView add_IMG_img;
    ProgressDialog progressDialog;
    Uri uri;
    FirebaseStorage storage;
    StorageReference storageReference;

    String imageName;

    private String[] measures = {"Measurement", "CUP", "TBSP", "TSP", "PINCH","GRAM","KILOGRAM",""};
    private int ingCount = 1;
    private int directCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        findViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        ingredients = new ArrayList<>();
        directions = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("recipes");
        myRef1 = database.getReference("users");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void addRecipe() {
        if(add_EDT_name.getText().toString().equals("")) {
            add_name_layout.setError("please enter recipe name");
        }else{
            add_name_layout.setError(null);
            getRecipeData();
            uploadImage();
        }
    }

    private void addImage() {
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .cropSquare()
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        add_IMG_img.setImageURI(uri);
    }

    private void addIngredientRow() {
        TableRow tr_head = new TableRow(this);
        tr_head.setId(ingCount++);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT);
        params.topMargin = 16;
        tr_head.setLayoutParams(params);

        EditText qty = new EditText(this);
        qty.setPadding(8, 8, 8, 8);
        qty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        TableRow.LayoutParams params0 = new TableRow.LayoutParams();
        params0.column = 0;
        params0.width = 70;
        qty.setLayoutParams(params0);

        Spinner measure = new Spinner(this);
        measure.setPadding(8, 8, 8, 8);
        setSpinner(measure, measures);
        TableRow.LayoutParams params1 = new TableRow.LayoutParams();
        params1.column = 1;
        measure.setLayoutParams(params1);

        EditText name = new EditText(this);
        name.setHint("Name");
        name.setPadding(8, 8, 8, 8);
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams();
        params2.column = 2;
        name.setLayoutParams(params2);

        ImageButton ib = new ImageButton(this);
        ib.setPadding(30, 30, 30, 30);
        ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ib.setBackgroundColor(0x00FFFFFF);
        ib.setImageResource(R.drawable.close);
        TableRow.LayoutParams params3 = new TableRow.LayoutParams();
        params3.height = TableRow.LayoutParams.MATCH_PARENT;
        params3.width = TableRow.LayoutParams.MATCH_PARENT;
        params3.column = 3;
        params3.span = 2;
        ib.setLayoutParams(params3);

        tr_head.addView(qty);
        tr_head.addView(measure);
        tr_head.addView(name);
        tr_head.addView(ib);
        qty.requestFocus();
        tl.addView(tr_head);
        ib.setOnClickListener(view -> {
            tl.removeView(tr_head);
            ingCount--;
        });
    }

    private void addDirectionRow() {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.topMargin = 16;
        ll.setLayoutParams(params);
        TextView count = new TextView(this);
        count.setText(String.valueOf(directCount + 1 + "."));
        count.setTextSize(26);
        directCount++;
        count.setPadding(8, 8, 8, 8);
        count.setPadding(16, 16, 16, 16);
        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params0.weight = 1;
        params0.leftMargin = 30;
        count.setLayoutParams(params0);
        EditText direct = new EditText(this);
        direct.setHint("Name");
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params1.weight = 5;
        direct.setLayoutParams(params1);
        ll.addView(count);
        ll.addView(direct);
        direct.requestFocus();
        add_LL_direction.addView(ll);
    }

    private void getRecipeData() {
        for (int i = 1; i <= ingCount; i++) {
            TableRow row = (TableRow) tl.getChildAt(i);
            double qty = 0;
            if(!((EditText) row.getChildAt(0)).getText().toString().equals("")){
                qty = Double.parseDouble(((EditText) row.getChildAt(0)).getText().toString());
            }
            String measure = ((Spinner) row.getChildAt(1)).getSelectedItem().toString();
            String name = ((EditText) row.getChildAt(2)).getText().toString();
            Ingredient ingredient = new Ingredient().setQty(qty).setMeasurement(measure).setName(name);
            ingredients.add(ingredient);
            Log.d("ccc", i + " name " + name);
        }
        //get directions
        for (int i = 0; i < directCount; i++) {
            LinearLayout ll = (LinearLayout) add_LL_direction.getChildAt(i);
            EditText et = (EditText) ll.getChildAt(1);
            String direction = et.getText().toString();
            directions.add(direction);
            Log.d("ccc", i + " direction " + direction);
        }
    }

    private void findViews() {
        add_BTN_img = findViewById(R.id.add_BTN_img);
        add_LBL_imgErr = findViewById(R.id.add_LBL_imgErr);
        add_BTN_save = findViewById(R.id.add_BTN_save);
        add_BTN_ing = findViewById(R.id.add_BTN_ing);
        add_BTN_direct = findViewById(R.id.add_BTN_direct);
        add_IMG_img = findViewById(R.id.add_IMG_img);
        add_EDT_name = findViewById(R.id.add_EDT_name);
        add_name_layout = findViewById(R.id.add_name_layout);
        add_SPN_measure = findViewById(R.id.add_SPN_measure);
        setSpinner(add_SPN_measure, measures);
        add_LL_direction = findViewById(R.id.add_LL_direction);
        tl = (TableLayout) findViewById(R.id.add_table);

        add_BTN_img.setOnClickListener(view -> addImage());

        add_BTN_save.setOnClickListener(view -> addRecipe());

        add_BTN_ing.setOnClickListener(view -> addIngredientRow());

        add_BTN_direct.setOnClickListener(view -> addDirectionRow());


    }

    private void setSpinner(Spinner spn, String[] arr) {
        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arr) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
    }

    // UploadImage method
    private void uploadImage() {
        if (uri != null) {
            add_LBL_imgErr.setText("");
            // Code for showing progressDialog while uploading
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child(
                    "images/"
                            + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(uri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    imageName = taskSnapshot.getStorage().getName();
                                    Recipe recipe = new Recipe();
                                    recipe.setName(add_EDT_name.getText().toString())
                                            .setIngredients(ingredients)
                                            .setDirections(directions)
                                            .setImg(imageName);
                                    Log.d("ccc", "recipe " + recipe.getName());
                                    myRef.child(recipe.getName()).setValue(recipe);
                                    myRef1.child(FirebaseAuth.getInstance().getUid()).child("myRecipes").child(recipe.getName()).setValue(recipe.getName());
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }else {
            add_LBL_imgErr.setText("Please choose an image");
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }
}