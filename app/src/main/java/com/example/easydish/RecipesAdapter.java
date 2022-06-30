package com.example.easydish;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;

public class RecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public interface ResultsListener {
        void clicked(Recipe recipe, int position);
        void delete(Recipe recipe,int position);
    }

    private Activity activity;
    private boolean isMyRecipes;
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ArrayList<Recipe> recipesAll = new ArrayList<>();
    private ResultsListener resultslistener;
    private FirebaseStorage storage;
    private StorageReference photoRef;

    public RecipesAdapter(Activity activity, ArrayList<Recipe> recipes,boolean isMyRecipes){
        this.activity = activity;
        this.recipes = recipes;
        this.recipesAll = new ArrayList<>(recipes);
        this.isMyRecipes = isMyRecipes;
    }

    public RecipesAdapter setResultsListener(ResultsListener resultslistener) {
        this.resultslistener = resultslistener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipes, parent, false);
        ResultsHolder resultsHolder = new ResultsHolder(view);
        return resultsHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ResultsHolder holder = (ResultsHolder) viewHolder;
        Recipe recipe = recipes.get(position);
        if (isMyRecipes){
            holder.recipe_delete.setVisibility(View.VISIBLE);
        }
        holder.recipe_name.setText(String.valueOf(recipe.getName()));
        storage = FirebaseStorage.getInstance();
        photoRef = storage.getReference("images/"+recipe.getImg());
        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(activity).load(downloadUrl)
                        .into(holder.recipe_img);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    public Recipe getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Recipe> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()){
                filteredList.addAll(recipesAll);
            }
            else {
                for (Recipe recipe : recipesAll) {
                    if (recipe.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(recipe);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            recipes.clear();
            recipes.addAll((Collection<? extends Recipe>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    class ResultsHolder extends RecyclerView.ViewHolder {

        private MaterialTextView recipe_name;
        private ImageView recipe_img;
        private ImageButton recipe_delete;

        public ResultsHolder(View itemView) {
            super(itemView);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            recipe_img = itemView.findViewById(R.id.recipe_img);
            recipe_delete = itemView.findViewById(R.id.recipe_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(resultslistener != null) {
                        resultslistener.clicked(getItem(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });

            recipe_delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(resultslistener != null) {
                        resultslistener.delete(getItem(getAdapterPosition()), getAdapterPosition());
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
