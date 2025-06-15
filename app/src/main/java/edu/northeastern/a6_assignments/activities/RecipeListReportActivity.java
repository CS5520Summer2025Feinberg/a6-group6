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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

/**
 * RecipeListReportActivity displays a list of recipes based on the search results. It handles the
 * display of recipes, empty states, and user interactions.
 */
public class RecipeListReportActivity extends AppCompatActivity implements
    RecipeAdapter.OnRecipeClickListener {

  // UI elements
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
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    // Initialize views and set up RecyclerView
    initViews();
    setupRecyclerView();
    handleIntentData();
  }

  /**
   * Initializes the UI components and sets up the "Try Again" button.
   */
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

  /**
   * Sets up the RecyclerView with a LinearLayoutManager and initializes the adapter.
   */
  private void setupRecyclerView() {
    recipeList = new ArrayList<>();
    recipeAdapter = new RecipeAdapter(this, recipeList);
    recipeAdapter.setOnRecipeClickListener(this);

    rvRecipeList.setLayoutManager(new LinearLayoutManager(this));
    rvRecipeList.setAdapter(recipeAdapter);
  }

  /**
   * Handles the intent data. It retrieves the response data and processes it to display recipes.
   */
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

  /**
   * Processes the JSON response to extract recipe data and update the UI.
   *
   * @param response The JSON response string containing recipe data.
   */
  private void processResponse(String response) {
    try {
      JSONObject jsonResponse = new JSONObject(response);

      List<Recipe> recipes = new ArrayList<>();
      JSONArray resultsArray = jsonResponse.getJSONArray("results");

      for (int i = 0; i < resultsArray.length(); i++) {
        JSONObject recipeJson = resultsArray.getJSONObject(i);

        // Create a Recipe object from the JSON data
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

  /**
   * Displays the list of recipes in the RecyclerView and updates the subtitle.
   *
   * @param recipes The list of recipes to display.
   */
  private void showResults(List<Recipe> recipes) {
    recipeAdapter.updateRecipes(recipes);
    tvSubtitle.setText("Found " + recipes.size() + " recipes for your search");

    rvRecipeList.setVisibility(View.VISIBLE);
    emptyStateLayout.setVisibility(View.GONE);
  }

  /**
   * Displays an empty state message when no recipes are found.
   */
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