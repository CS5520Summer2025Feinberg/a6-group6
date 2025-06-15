package edu.northeastern.a6_assignments.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import edu.northeastern.a6_assignments.R;
import edu.northeastern.a6_assignments.pojo.ComplexSearchResponseElement;
import edu.northeastern.a6_assignments.pojo.Diet;
import edu.northeastern.a6_assignments.pojo.Intolerance;
import edu.northeastern.a6_assignments.pojo.MealTypes;
import edu.northeastern.a6_assignments.pojo.Cuisine;
import edu.northeastern.a6_assignments.pojo.ComplexSearchPOJORequest;
import edu.northeastern.a6_assignments.helpers.ComplexSearchPOJOResponseHandlers;
import edu.northeastern.a6_assignments.services.ComplexSearchRecipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * FoodRecipeRequestActivity is an Android activity that allows users to search for food recipes
 * based on various parameters such as query, cuisine, diet, intolerances, meal type, and more. It
 * provides a user interface for inputting search criteria and displays the results.
 */
public class FoodRecipeRequestActivity extends AppCompatActivity implements
    ComplexSearchRecipe.ComplexSearchCallBack {

  // UI elements and variables for managing the recipe search
  private Thread searchRecipes;
  EditText queryInput;
  TextView cuisineSelector;
  TextView excludedCuisineSelector;
  Spinner dietSelector;
  TextView intoleranceSelector;
  Spinner mealTypeSelector;
  TextView maxReadyTimeText;
  Slider maxReadyTimeSlider;
  TextView maxServingsText;
  Slider maxServingsSlider;
  TextView minServingsText;
  Slider minServingsSlider;
  EditText numberOfRecipesInput;
  private Toast numberRangeToast;

  // Variables to hold search parameters
  private String query = "";
  private final List<String> cuisineList = new ArrayList<>();
  private boolean[] cuisineSelectedItems;
  private final List<String> excludedCuisineList = new ArrayList<>();
  private boolean[] excludedCuisineSelectedItems;
  private String diet = "";
  private final List<String> intolerancesList = new ArrayList<>();
  private boolean[] intoleranceSelectedItems;
  private String mealType = "";
  private int maxReadyTime = 10;
  private int maxServings = 1;
  private int minServings = 1;
  private int numberOfRecipes = 100;

  // Holder for the search response
  private List<ComplexSearchResponseElement> responseHolder;
  private final Handler searchHandler = new Handler();

  private ProgressBar progressBar;
  private View progressOverlay;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_food_recipe_request);

    // Initialize UI elements
    queryInput = findViewById(R.id.query);
    cuisineSelector = findViewById(R.id.cuisine_selector);
    excludedCuisineSelector = findViewById(R.id.excluded_cuisine_selector);
    intoleranceSelector = findViewById(R.id.intolerance_selector);
    dietSelector = findViewById(R.id.diet_selector);
    mealTypeSelector = findViewById(R.id.meal_type_selector);
    maxReadyTimeText = findViewById(R.id.max_ready_time);
    maxReadyTimeSlider = findViewById(R.id.max_ready_time_slider);
    maxServingsText = findViewById(R.id.max_servings_label);
    maxServingsSlider = findViewById(R.id.max_servings_slider);
    minServingsText = findViewById(R.id.min_servings_label);
    minServingsSlider = findViewById(R.id.min_servings_slider);
    numberOfRecipesInput = findViewById(R.id.number_of_recipes);

    // Initialize lists for cuisines and intolerances
    cuisineSelectedItems = new boolean[getListItems(R.id.cuisine_selector).size()];
    setupSelectItems(cuisineSelector, cuisineList, "Select Cuisines", cuisineSelectedItems);
    excludedCuisineSelectedItems = new boolean[getListItems(R.id.excluded_cuisine_selector).size()];
    setupSelectItems(excludedCuisineSelector, excludedCuisineList, "Exclude Cuisines",
        excludedCuisineSelectedItems);
    setupSingleItemSelector(dietSelector, "Select Diet");
    intoleranceSelectedItems = new boolean[getListItems(R.id.intolerance_selector).size()];
    setupSelectItems(intoleranceSelector, intolerancesList, "Select Intolerances",
        intoleranceSelectedItems);
    setupSingleItemSelector(mealTypeSelector, "Select Meal Type");
    setupSlider(maxReadyTimeSlider, maxReadyTimeText, "Max Ready Time: ", " min", maxReadyTime);
    maxReadyTimeSlider.addOnChangeListener((s, value, fromUser) -> maxReadyTime = (int) value);
    setupSlider(maxServingsSlider, maxServingsText, "Max Servings: ", "", maxServings);
    maxServingsSlider.addOnChangeListener((s, value, fromUser) -> maxServings = (int) value);
    setupSlider(minServingsSlider, minServingsText, "Min Servings: ", "", minServings);
    minServingsSlider.addOnChangeListener((s, value, fromUser) -> minServings = (int) value);
    String numOfRecipes = numberOfRecipesInput.getText().toString();
    numberOfRecipes = numOfRecipes.isEmpty() ? 1 : Integer.parseInt(numOfRecipes);

    progressBar = findViewById(R.id.progress_bar);
    progressOverlay = findViewById(R.id.progress_overlay);

    // Restore instance state if available
    restoreInstanceState(savedInstanceState);
  }

  /**
   * This method updates the selected items array based on the selected list and all items. It marks
   * the items as selected or not based on their presence in the selected list.
   *
   * @param allItems      The complete list of items to choose from.
   * @param selectedList  The list of currently selected items.
   * @param selectedItems The boolean array representing the selection state of each item.
   */
  private void updateSelectedItemsArray(List<String> allItems, List<String> selectedList,
      boolean[] selectedItems) {
    for (int i = 0; i < allItems.size(); i++) {
      selectedItems[i] = selectedList.contains(allItems.get(i));
    }
  }

  /**
   * This method restores the instance state of the activity from the saved instance state bundle.
   * It retrieves the search parameters and updates the UI elements accordingly.
   *
   * @param savedInstanceState The saved instance state bundle containing previous values.
   */
  private void restoreInstanceState(Bundle savedInstanceState) {
    if (savedInstanceState == null) {
      return;
    }

    // Restore search parameters from the saved instance state
    query = savedInstanceState.getString("query", "");
    cuisineList.clear();
    cuisineList.addAll(savedInstanceState.getStringArrayList("cuisineList"));
    excludedCuisineList.clear();
    excludedCuisineList.addAll(savedInstanceState.getStringArrayList("excludedCuisineList"));
    diet = savedInstanceState.getString("diet", "");
    intolerancesList.clear();
    intolerancesList.addAll(savedInstanceState.getStringArrayList("intolerancesList"));
    mealType = savedInstanceState.getString("mealType", "");
    maxReadyTime = savedInstanceState.getInt("maxReadyTime", 10);
    maxServings = savedInstanceState.getInt("maxServings", 1);
    minServings = savedInstanceState.getInt("minServings", 1);
    numberOfRecipes = savedInstanceState.getInt("numberOfRecipes", 100);

    // Update UI elements with restored values
    queryInput.setText(query);
    maxReadyTimeSlider.setValue(maxReadyTime);
    maxServingsSlider.setValue(maxServings);
    minServingsSlider.setValue(minServings);
    numberOfRecipesInput.setText(String.valueOf(numberOfRecipes));

    updateSelectedItemsArray(getListItems(R.id.cuisine_selector), cuisineList,
        cuisineSelectedItems);
    cuisineSelector.setText(
        cuisineList.isEmpty() ? "Select Cuisines" : TextUtils.join(", ", cuisineList));
    updateSelectedItemsArray(getListItems(R.id.excluded_cuisine_selector), excludedCuisineList,
        excludedCuisineSelectedItems);
    excludedCuisineSelector.setText(excludedCuisineList.isEmpty() ? "Exclude Cuisines"
        : TextUtils.join(", ", excludedCuisineList));
    updateSelectedItemsArray(getListItems(R.id.intolerance_selector), intolerancesList,
        intoleranceSelectedItems);
    intoleranceSelector.setText(intolerancesList.isEmpty() ? "Select Intolerances"
        : TextUtils.join(", ", intolerancesList));

    if (dietSelector.getAdapter() != null) {
      int dietPosition = ((ArrayAdapter<String>) dietSelector.getAdapter()).getPosition(
          diet.isEmpty() ? "Select Diet" : diet);
      dietSelector.setSelection(dietPosition);
    }
    if (mealTypeSelector.getAdapter() != null) {
      int mealTypePosition = ((ArrayAdapter<String>) mealTypeSelector.getAdapter()).getPosition(
          mealType.isEmpty() ? "Select Meal Type" : mealType);
      mealTypeSelector.setSelection(mealTypePosition);
    }
  }

  /**
   * This method validates the user inputs for the recipe search. It checks if the query is empty,
   * validates the number of recipes, and ensures that the values are within acceptable ranges.
   *
   * @return true if all inputs are valid, false otherwise.
   */
  private boolean validateInputs() {
    // Validate query input
    query = queryInput.getText().toString();
    if (query.isEmpty()) {
      queryInput.setError("This field is required");
      queryInput.requestFocus();
      return false;
    }

    // Validate number of recipes
    String numOfRecipes = numberOfRecipesInput.getText().toString();
    int num;
    if (numOfRecipes.isEmpty()) {
      num = 100;
    } else {
      try {
        num = Integer.parseInt(numOfRecipes);
      } catch (NumberFormatException e) {
        numberOfRecipesInput.setError("Enter a valid number");
        numberOfRecipesInput.requestFocus();
        return false;
      }
      if (num < 1 || num > 100) {
        numberOfRecipesInput.setError("Enter a value from 1 to 100");
        numberOfRecipesInput.requestFocus();
        return false;
      }
    }
    numberOfRecipes = num;
    return true;
  }

  /**
   * This method is called when the user clicks the "Search Recipes" button. It retrieves the input
   * values, starts a new thread to search for recipes, and updates the UI accordingly.
   *
   * @param view The view that was clicked.
   */
  public void searchRecipes(View view) {
    if (!validateInputs()) {
      return;
    }

    showProgress(true);

    // Start the recipe search thread
    ComplexSearchRecipe recipe = new ComplexSearchRecipe(this, searchHandler);
    searchRecipes = new Thread(recipe);
    searchRecipes.start();
  }

  @Override
  public String getRequestParameters() {
    return new ComplexSearchPOJORequest
        .ComplexSearchPOJORequestBuilder(query)
        .setCuisine(cuisineList)
        .setExcludeCuisine(excludedCuisineList)
        .setDiet(diet)
        .setIntolerance(intolerancesList)
        .setMealTypes(mealType)
        .setMaxReadyTime(maxReadyTime)
        .setMaxServings(maxServings)
        .setMinServings(minServings)
        .setNumberOfRecipes(numberOfRecipes)
        .build()
        .getRequestParameters();
  }

  @Override
  public void createObjectFromResponse(String response) {
    showProgress(false);
    responseHolder = ComplexSearchPOJOResponseHandlers.handleComplexSearchResponse(response);
    Intent intent = new Intent(this, RecipeListReportActivity.class);
    intent.putExtra("response_data", response);

    startActivity(intent);
  }


  /**
   * This method shows/hides the progress bar.
   * @param show The boolean flag to show or hide the progress bar.
   */
  private void showProgress(boolean show) {
    if (progressBar != null) {
      progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    if (progressOverlay != null) {
      progressOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }
  }


  /**
   * This is a helper method that retrieves a list of items based on the selector ID.
   *
   * @param selectorId The ID of the selector to determine which items to retrieve.
   */
  private List<String> getListItems(int selectorId) {
    List<String> items = new ArrayList<>();
    if (selectorId == R.id.cuisine_selector || selectorId == R.id.excluded_cuisine_selector) {
      for (Cuisine cuisine : Cuisine.values()) {
        items.add(cuisine.getValue());
      }
    } else {
      for (Intolerance intolerance : Intolerance.values()) {
        items.add(intolerance.getValue());
      }
    }
    return items;
  }

  /**
   * This method sets up a multi-choice dialog for selecting items from a list. It allows users to
   * select multiple items and updates the TextView with the selected items.
   *
   * @param selector     The TextView that will display the selected items.
   * @param selectedList The list that will hold the selected items.
   * @param dialogTitle  The title of the dialog.
   */
  private void setupSelectItems(TextView selector, List<String> selectedList, String dialogTitle,
      boolean[] selectedItems) {
    List<String> listOfItems = getListItems(selector.getId());
    selector.setMovementMethod(new ScrollingMovementMethod());

    // Set the initial text of the selector
    selector.setOnClickListener(v -> {
      boolean[] tempSelectedItems = selectedItems.clone();
      List<String> tempSelected = new ArrayList<>(selectedList);

      AlertDialog.Builder builder = new AlertDialog.Builder(FoodRecipeRequestActivity.this);
      builder.setTitle(dialogTitle);

      // Convert the list of items to an array and set up the multi-choice dialog
      builder.setMultiChoiceItems(listOfItems.toArray(new String[0]), tempSelectedItems,
          (dialog, indexSelected, isChecked) -> {
            String item = listOfItems.get(indexSelected);
            if (isChecked) {
              if (!tempSelected.contains(item)) {
                tempSelected.add(item);
              }
            } else {
              tempSelected.remove(item);
            }
          });

      builder.setPositiveButton("OK", (dialog, id) -> {
        System.arraycopy(tempSelectedItems, 0, selectedItems, 0, tempSelectedItems.length);
        selectedList.clear();
        selectedList.addAll(tempSelected);

        selector.setText(selectedList.isEmpty()
            ? dialogTitle
            : TextUtils.join(", ", selectedList));
      });

      builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
      builder.show();
    });
  }

  /**
   * This method sets up a single-item selector (Spinner) for selecting diet or meal type. It
   * populates the Spinner with options and handles item selection.
   *
   * @param selector    The Spinner to set up.
   * @param dialogTitle The title of the dialog shown when selecting an item.
   */
  private void setupSingleItemSelector(Spinner selector, String dialogTitle) {
    List<String> listOfItems = new ArrayList<>();
    listOfItems.add(dialogTitle);

    if (selector.getId() == R.id.diet_selector) {
      for (Diet diet : Diet.values()) {
        listOfItems.add(diet.getValue());
      }
    } else if (selector.getId() == R.id.meal_type_selector) {
      for (MealTypes mealType : MealTypes.values()) {
        listOfItems.add(mealType.getValue());
      }
    }

    // Create an ArrayAdapter to populate the Spinner with the list of items
    ArrayAdapter<String> adapter = new ArrayAdapter<>(
        this,
        android.R.layout.simple_spinner_item,
        listOfItems
    );
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    selector.setAdapter(adapter);

    // Set the initial selection to the dialog title
    selector.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view,
          int position, long id) {
        String selected = listOfItems.get(position);
        if (selector.getId() == R.id.diet_selector) {
          FoodRecipeRequestActivity.this.diet = selected.equals(dialogTitle) ? "" : selected;
        } else if (selector.getId() == R.id.meal_type_selector) {
          FoodRecipeRequestActivity.this.mealType = selected.equals(dialogTitle) ? "" : selected;
        }
      }

      @Override
      public void onNothingSelected(android.widget.AdapterView<?> parent) {
        if (selector.getId() == R.id.diet_selector) {
          FoodRecipeRequestActivity.this.diet = "";
        } else if (selector.getId() == R.id.meal_type_selector) {
          FoodRecipeRequestActivity.this.mealType = "";
        }
      }
    });
  }

  /**
   * This method sets up a Slider for selecting numerical values with a label. It updates the label
   * text based on the slider value.
   *
   * @param slider       The Slider to set up.
   * @param label        The TextView that displays the current value.
   * @param prefix       The prefix for the label text.
   * @param suffix       The suffix for the label text.
   * @param initialValue The initial value for the slider and label.
   */
  private void setupSlider(Slider slider, TextView label, String prefix, String suffix,
      int initialValue) {
    label.setText(prefix + initialValue + suffix);
    slider.addOnChangeListener((s, value, fromUser) -> {
      int intValue = (int) value;
      label.setText(prefix + intValue + suffix);
    });
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString("query", queryInput.getText().toString());
    outState.putStringArrayList("cuisineList", new ArrayList<>(cuisineList));
    outState.putStringArrayList("excludedCuisineList", new ArrayList<>(excludedCuisineList));
    outState.putString("diet", diet);
    outState.putStringArrayList("intolerancesList", new ArrayList<>(intolerancesList));
    outState.putString("mealType", mealType);
    outState.putInt("maxReadyTime", maxReadyTime);
    outState.putInt("maxServings", maxServings);
    outState.putInt("minServings", minServings);
    outState.putInt("numberOfRecipes", numberOfRecipes);
  }
}
