package edu.northeastern.a6_assignments.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a6_assignments.Adapter.RecipeAdapter;
import edu.northeastern.a6_assignments.R;
import edu.northeastern.a6_assignments.pojo.Recipe;

public class RecipeListReportActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView rvRecipeList;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_report);

        initViews();
        setupRecyclerView();
        handleIntentData();
    }

    private void initViews() {
        rvRecipeList = findViewById(R.id.rv_recipe_list);
    }

    private void setupRecyclerView() {
        List<Recipe> recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeAdapter.setOnRecipeClickListener(this);

        rvRecipeList.setLayoutManager(new LinearLayoutManager(this));
        rvRecipeList.setAdapter(recipeAdapter);
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String response = intent.getStringExtra("response_data");
            Object responseHolder = intent.getSerializableExtra("response_holder");

            if (response != null) {
                processResponse(response);
            }
        }
    }

    private void processResponse(String response) {
        try {
            // Parse JSON manually using Android's JSONObject
            JSONObject jsonResponse = new JSONObject(response);

            List<Recipe> recipes = new ArrayList<>();
            JSONArray resultsArray = jsonResponse.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject recipeJson = resultsArray.getJSONObject(i);

                Recipe recipe = new Recipe();
                recipe.setId(recipeJson.getInt("id"));
                recipe.setTitle(recipeJson.getString("title"));
                recipe.setImage(recipeJson.optString("image", ""));
                recipe.setImageType(recipeJson.optString("imageType", ""));

                recipes.add(recipe);
            }

            recipeAdapter.updateRecipes(recipes);

        } catch (JSONException e) {
            Log.e("RecipeListActivity", "Error parsing JSON response", e);
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe, int position) {
        // Handle recipe click - maybe open details or do nothing
        Toast.makeText(this, "Clicked: " + recipe.getTitle(), Toast.LENGTH_SHORT).show();
    }
}