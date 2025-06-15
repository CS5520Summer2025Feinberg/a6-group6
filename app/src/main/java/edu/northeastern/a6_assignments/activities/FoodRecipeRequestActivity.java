package edu.northeastern.a6_assignments.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import edu.northeastern.a6_assignments.pojo.ComplexSearchResponseElement;
import edu.northeastern.a6_assignments.pojo.Diet;
import edu.northeastern.a6_assignments.pojo.Intolerance;
import edu.northeastern.a6_assignments.pojo.MealTypes;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a6_assignments.R;
import edu.northeastern.a6_assignments.pojo.Cuisine;

import edu.northeastern.a6_assignments.helpers.ComplexSearchPOJOResponseHandlers;
import edu.northeastern.a6_assignments.pojo.ComplexSearchPOJORequest;
import edu.northeastern.a6_assignments.services.ComplexSearchRecipe;

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
  private final List<String> excludedCuisineList = new ArrayList<>();
  private String diet = "";
  private final List<String> intolerancesList = new ArrayList<>();
  private String mealType = "";
  private int maxReadyTime = 10;
  private int maxServings = 1;
  private int minServings = 1;
  private int numberOfRecipes = 100;

  // Holder for the search response
  private List<ComplexSearchResponseElement> responseHolder;
  private Handler searchHandler = new Handler();

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
    setupSelectItems(cuisineSelector, cuisineList, "Select Cuisines");
    setupSelectItems(excludedCuisineSelector, excludedCuisineList, "Exclude Cuisines");
    setupSingleItemSelector(dietSelector, "Select Diet");
    setupSelectItems(intoleranceSelector, intolerancesList, "Select Intolerances");
    setupSingleItemSelector(mealTypeSelector, "Select Meal Type");
    setupSlider(maxReadyTimeSlider, maxReadyTimeText, "Max Ready Time: ", " min", maxReadyTime);
    maxReadyTimeSlider.addOnChangeListener((s, value, fromUser) -> maxReadyTime = (int) value);
    setupSlider(maxServingsSlider, maxServingsText, "Max Servings: ", "", maxServings);
    maxServingsSlider.addOnChangeListener((s, value, fromUser) -> maxServings = (int) value);
    setupSlider(minServingsSlider, minServingsText, "Min Servings: ", "", minServings);
    minServingsSlider.addOnChangeListener((s, value, fromUser) -> minServings = (int) value);
    String numOfRecipes = numberOfRecipesInput.getText().toString();
    numberOfRecipes = numOfRecipes.isEmpty() ? 1 : Integer.parseInt(numOfRecipes);

    // Set up the number of recipes input field with validation
    numberOfRecipesInput.setFilters(new InputFilter[]{
        (source, start, end, dest, dstart, dend) -> {
          try {
            String input = dest.subSequence(0, dstart)
                + source.toString()
                + dest.subSequence(dend, dest.length());
            if (input.isEmpty()) {
              return null;
            }
            int value = Integer.parseInt(input);
            if (value < 1 || value > 100) {
              if (numberRangeToast != null) {
                numberRangeToast.cancel();
              }
              numberRangeToast = Toast.makeText(
                  FoodRecipeRequestActivity.this,
                  "Please enter a value from 1 to 100",
                  Toast.LENGTH_SHORT
              );
              numberRangeToast.show();
              return "";
            }
          } catch (NumberFormatException e) {
            return "";
          }
          return null;
        }
    });
  }

  /**
   * This method is called when the user clicks the "Search Recipes" button. It retrieves the input
   * values, starts a new thread to search for recipes, and updates the UI accordingly.
   *
   * @param view The view that was clicked.
   */
  public void searchRecipes(View view) {
    query = queryInput.getText().toString();
    String numOfRecipes = numberOfRecipesInput.getText().toString();
    numberOfRecipes = numOfRecipes.isEmpty() ? 1 : Integer.parseInt(numOfRecipes);

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
    responseHolder = ComplexSearchPOJOResponseHandlers.handleComplexSearchResponse(response);
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
  private void setupSelectItems(TextView selector, List<String> selectedList, String dialogTitle) {
    List<String> listOfItems = getListItems(selector.getId());
    boolean[] selectedItems = new boolean[listOfItems.size()];

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
}
