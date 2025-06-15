package edu.northeastern.a6_assignments.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class FoodRecipeRequestActivity extends AppCompatActivity implements ComplexSearchRecipe.ComplexSearchCallBack {
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
    private String query = "";
    private List<String> cuisineList = new ArrayList<>();
    private List<String> excludedCuisineList = new ArrayList<>();
    private String diet = "";
    private List<String> intolerancesList = new ArrayList<>();
    private String mealType = "";

    private List<ComplexSearchResponseElement> responseHolder;
    private int numberOfRecipes;
    private Handler searchHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_recipe_request);

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

    query = queryInput.getText().toString();
    setupSelectItems(cuisineSelector, cuisineList, "Select Cuisines");
    setupSelectItems(excludedCuisineSelector, excludedCuisineList, "Exclude Cuisines");
    setupSingleItemSelector(dietSelector, "Select Diet");
    setupSelectItems(intoleranceSelector, intolerancesList, "Select Intolerances");
    setupSingleItemSelector(mealTypeSelector, "Select Meal Type");
    maxReadyTimeText.setText("Max Ready Time: " + maxReadyTime + " min");
    maxReadyTimeSlider.addOnChangeListener((s, value, fromUser) -> {
      maxReadyTime = (int) value;
      maxReadyTimeText.setText("Max Ready Time: " + maxReadyTime + " min");
    });
    maxServingsText.setText("Max Servings: " + maxServings);
    maxServingsSlider.addOnChangeListener((s, value, fromUser) -> {
      maxServings = (int) value;
      maxServingsText.setText("Max Servings: " + maxServings);
    });
    minServingsText.setText("Min Servings: " + minServings);
    minServingsSlider.addOnChangeListener((s, value, fromUser) -> {
      minServings = (int) value;
      minServingsText.setText("Min Servings: " + minServings);
    });
    String numOfRecipes = numberOfRecipesInput.getText().toString();
    numberOfRecipes = numOfRecipes.isEmpty() ? 1 : Integer.parseInt(numOfRecipes);
  }

    public void searchRecipes(View view) {
        ComplexSearchRecipe recipe = new ComplexSearchRecipe(this,searchHandler);
        searchRecipes = new Thread(recipe);
        searchRecipes.start();
    }

    @Override
    public String getRequestParameters() {
        return new ComplexSearchPOJORequest
                .ComplexSearchPOJORequestBuilder(query)
                .setCuisine(cuisineList)
                .setDiet(diet)
                .setIntolerance(intolerancesList)
                .setExcludeCuisine(excludedCuisineList)
                .setNumberOfRecipes(numberOfRecipes)
                .build()
                .getRequestParameters();
    }

    @Override
    public void createObjectFromResponse(String response) {
        responseHolder =
                ComplexSearchPOJOResponseHandlers.handleComplexSearchResponse(response);
    }

    private void setupSelectItems(TextView selector, List<String> selectedList, String dialogTitle) {
        List<String> listOfItems = new ArrayList<>();
        if (selector.getId() == R.id.cuisine_selector) {
            for (Cuisine cuisine : Cuisine.values()) {
                listOfItems.add(cuisine.getValue());
            }
        } else {
            for (Intolerance cuisine : Intolerance.values()) {
                listOfItems.add(cuisine.getValue());
            }
        }

        boolean[] selectedItems = new boolean[listOfItems.size()];

        selector.setMovementMethod(new ScrollingMovementMethod());
        selector.setOnClickListener(v -> {
            boolean[] tempSelectedItems = selectedItems.clone();
            List<String> tempSelected = new ArrayList<>(selectedList);

            AlertDialog.Builder builder = new AlertDialog.Builder(FoodRecipeRequestActivity.this);
            builder.setTitle(dialogTitle);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listOfItems
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selector.setAdapter(adapter);

        selector.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
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

    private void setupSliderWithLabel(Slider slider, TextView label, String prefix, String suffix) {
        int initialValue = (int) slider.getValue();
        label.setText(prefix + initialValue + suffix);

        slider.addOnChangeListener((s, value, fromUser) -> {
            int val = (int) value;
            label.setText(prefix + val + suffix);
        });
    }
}
