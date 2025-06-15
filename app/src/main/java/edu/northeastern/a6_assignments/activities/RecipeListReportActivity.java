package edu.northeastern.a6_assignments.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private LinearLayout emptyStateLayout;
    private TextView tvSubtitle;
    private Button btnTryAgain;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;

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
        emptyStateLayout = findViewById(R.id.empty_state_layout);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        btnTryAgain = findViewById(R.id.btn_try_again);

        // Set up try again button
        btnTryAgain.setOnClickListener(v -> {
            finish(); // Go back to search activity
        });
    }

    private void setupRecyclerView() {
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeAdapter.setOnRecipeClickListener(this);

        rvRecipeList.setLayoutManager(new LinearLayoutManager(this));
        rvRecipeList.setAdapter(recipeAdapter);
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String response = intent.getStringExtra("response_data");
            if (response != null) {
                processResponse(response);
            } else {
                showEmptyState();
            }
        } else {
            showEmptyState();
        }
    }

    private void processResponse(String response) {
        try {
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

            if (recipes.isEmpty()) {
                showEmptyState();
            } else {
                showResults(recipes);
            }

        } catch (JSONException e) {
            Log.e("RecipeListActivity", "Error parsing JSON response", e);
            showEmptyState();
        }
    }

    private void showResults(List<Recipe> recipes) {
        recipeAdapter.updateRecipes(recipes);
        tvSubtitle.setText("Found " + recipes.size() + " recipes for your search");

        rvRecipeList.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        tvSubtitle.setText("No recipes found for your search");

        rvRecipeList.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRecipeClick(Recipe recipe, int position) {
        Toast.makeText(this, "Clicked: " + recipe.getTitle(), Toast.LENGTH_SHORT).show();
    }
}