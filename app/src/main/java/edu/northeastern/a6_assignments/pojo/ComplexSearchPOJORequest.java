package edu.northeastern.a6_assignments.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * ComplexSearchPOJORequest is a data transfer object that encapsulates the parameters for a complex
 * search request in a recipe application. It allows users to specify various search criteria such
 * as cuisine, diet, intolerances, meal types, and more.
 */
public class ComplexSearchPOJORequest {

  // Fields representing the search parameters
  private final String query;
  private final List<Cuisine> cuisine;
  private final List<Cuisine> excludeCuisine;
  private final Diet diet;
  private final List<Intolerance> intolerance;
  private final MealTypes mealTypes;
  private final int maxReadyTime;
  private final int maxServings;
  private final int minServings;
  private final int numberOfRecipes;

  /**
   * Private constructor to enforce the use of the builder pattern.
   *
   * @param complexSearchPOJORequestBuilder The builder instance containing the search parameters.
   */
  private ComplexSearchPOJORequest(
      ComplexSearchPOJORequestBuilder complexSearchPOJORequestBuilder) {
    this.cuisine = complexSearchPOJORequestBuilder.cuisine;
    this.diet = complexSearchPOJORequestBuilder.diet;
    this.excludeCuisine = complexSearchPOJORequestBuilder.excludeCuisine;
    this.maxReadyTime = complexSearchPOJORequestBuilder.maxReadyTime;
    this.maxServings = complexSearchPOJORequestBuilder.maxServings;
    this.intolerance = complexSearchPOJORequestBuilder.intolerance;
    this.numberOfRecipes = complexSearchPOJORequestBuilder.numberOfRecipes;
    this.mealTypes = complexSearchPOJORequestBuilder.mealTypes;
    this.minServings = complexSearchPOJORequestBuilder.minServings;
    this.query = complexSearchPOJORequestBuilder.query;
  }

  /**
   * Constructs a query string with the search parameters formatted for an HTTP request.
   *
   * @return A string representing the request parameters.
   */
  public String getRequestParameters() {
    StringBuilder request = new StringBuilder();

    addStringParam(request, "query", query);
    addListParam(request, "cuisine", cuisine);
    addListParam(request, "excludeCuisine", excludeCuisine);
    addStringParam(request, "diet", diet != null ? diet.getValue() : null);
    addListParam(request, "intolerances", intolerance);
    addStringParam(request, "type", mealTypes != null ? mealTypes.getValue() : null);
    addIntParam(request, "maxReadyTime", maxReadyTime);
    addIntParam(request, "maxServings", maxServings);
    addIntParam(request, "minServings", minServings);
    addIntParam(request, "number", numberOfRecipes);

    return request.toString();
  }

  /**
   * Adds a string parameter to the request if the value is not null or empty.
   *
   * @param request   The StringBuilder to append the parameter to.
   * @param paramName The name of the parameter.
   * @param value     The value of the parameter.
   */
  private void addStringParam(StringBuilder request, String paramName, String value) {
    if (value != null && !value.trim().isEmpty()) {
      if (request.length() > 0) {
        request.append("&");
      }
      request.append(paramName).append("=");
      String[] parts = value.split(" ");
      if (parts.length != 1) {
        request.append("\\\"");
        request.append(value);
        request.append("\\\"");
      } else {
        request.append(value);
      }
    }
  }

  /**
   * Adds an integer parameter to the request if the value is greater than zero.
   *
   * @param request   The StringBuilder to append the parameter to.
   * @param paramName The name of the parameter.
   * @param value     The integer value of the parameter.
   */
  private void addIntParam(StringBuilder request, String paramName, int value) {
    if (value > 0) {
      if (request.length() > 0) {
        request.append("&");
      }
      request.append(paramName).append("=").append(value);
    }
  }

  /**
   * Adds a list parameter to the request, formatting each item appropriately.
   *
   * @param request   The StringBuilder to append the parameter to.
   * @param paramName The name of the parameter.
   * @param list      The list of items to be added as a parameter.
   * @param <T>       The type of items in the list, expected to be Cuisine or Intolerance.
   */
  private <T> void addListParam(StringBuilder request, String paramName, List<T> list) {
    if (list != null && !list.isEmpty()) {
      if (request.length() > 0) {
        request.append("&");
      }
      request.append(paramName).append("=");

      boolean firstFlag = true;
      for (T item : list) {
        if (!firstFlag) {
          request.append(",");
        }

        if (item instanceof Cuisine) {
          String cuisine = ((Cuisine) item).getValue();
          String[] parts = cuisine.split(" ");
          if (parts.length != 1) {
            request.append("\\\"");
            request.append(cuisine);
            request.append("\\\"");
          } else {
            request.append(cuisine);
          }
        } else if (item instanceof Intolerance) {
          String intolerance = ((Intolerance) item).getValue();
          String[] parts = intolerance.split(" ");
          if (parts.length != 1) {
            request.append("\\\"");
            request.append(intolerance);
            request.append("\\\"");
          } else {
            request.append(intolerance);
          }
        }
        firstFlag = false;
      }
    }
  }

  /**
   * Builder class for constructing ComplexSearchPOJORequest instances. It provides methods to set
   * various search parameters and build the request object.
   */
  public static class ComplexSearchPOJORequestBuilder {

    // Fields for the builder
    private String query;
    private List<Cuisine> cuisine;
    private List<Cuisine> excludeCuisine;
    private Diet diet;
    private List<Intolerance> intolerance;
    private MealTypes mealTypes;
    private int maxReadyTime;
    private int maxServings;
    private int minServings;
    private int numberOfRecipes;

    /**
     * Constructor for the builder that initializes the query field.
     *
     * @param query The search query string. If empty, it will not be set.
     */
    public ComplexSearchPOJORequestBuilder(String query) {
      if (!query.isEmpty()) {
        this.query = query;
      }
    }

    /**
     * Default constructor for the builder.
     */
    public ComplexSearchPOJORequestBuilder setCuisine(List<String> cuisine) {
      List<Cuisine> cus = new ArrayList<>();
      if (!cuisine.isEmpty()) {
        for (String cui : cuisine) {
          cus.add(Cuisine.fromValue(cui));
        }
      }
      this.cuisine = cus;
      return this;
    }

    /**
     * Sets the cuisine to be excluded from the search.
     *
     * @param excludeCuisine A list of cuisine types to exclude.
     * @return The builder instance for method chaining.
     */
    public ComplexSearchPOJORequestBuilder setExcludeCuisine(List<String> excludeCuisine) {
      List<Cuisine> excluCus = new ArrayList<>();
      if (!excludeCuisine.isEmpty()) {
        ;
        for (String cui : excludeCuisine) {
          excluCus.add(Cuisine.fromValue(cui));
        }
      }
      this.excludeCuisine = excluCus;
      return this;
    }

    /**
     * Sets the diet type for the search.
     *
     * @param diet The diet type as a string. If empty, it will not be set.
     * @return The builder instance for method chaining.
     */
    public ComplexSearchPOJORequestBuilder setDiet(String diet) {
      if (!diet.isEmpty()) {
        this.diet = Diet.fromValue(diet);
      }

      return this;
    }

    /**
     * Sets the list of intolerances for the search.
     *
     * @param intolerance A list of intolerance types as strings. If empty, it will not be set.
     * @return The builder instance for method chaining.
     */
    public ComplexSearchPOJORequestBuilder setIntolerance(List<String> intolerance) {
      List<Intolerance> intole = new ArrayList<>();
      if (!intolerance.isEmpty()) {
        for (String intoler : intolerance) {
          intole.add(Intolerance.fromValue(intoler));
        }
      }
      this.intolerance = intole;
      return this;
    }

    /**
     * Sets the meal types for the search.
     *
     * @param mealTypes The meal types as a string. If empty, it will not be set.
     * @return The builder instance for method chaining.
     */
    public ComplexSearchPOJORequestBuilder setMealTypes(String mealTypes) {
      if (!mealTypes.isEmpty()) {
        this.mealTypes = MealTypes.fromValue(mealTypes);
      }
      return this;
    }

    /**
     * Sets the maximum ready time for the search.
     *
     * @param maxReadyTime The maximum ready time in minutes. If zero, it will not be set.
     * @return The builder instance for method chaining.
     */
    public ComplexSearchPOJORequestBuilder setMaxReadyTime(int maxReadyTime) {
      this.maxReadyTime = maxReadyTime;
      return this;
    }

    /**
     * Sets the maximum number of servings for the search.
     *
     * @param maxServings The maximum number of servings. If zero, it will not be set.
     * @return The builder instance for method chaining.
     */
    public ComplexSearchPOJORequestBuilder setMaxServings(int maxServings) {
      this.maxServings = maxServings;
      return this;
    }

    /**
     * Sets the minimum number of servings for the search.
     *
     * @param minServings The minimum number of servings. If zero, it will not be set.
     * @return The builder instance for method chaining.
     */
    public ComplexSearchPOJORequestBuilder setMinServings(int minServings) {
      this.minServings = minServings;
      return this;
    }

    /**
     * Sets the number of recipes to be returned in the search results.
     *
     * @param numberOfRecipes The number of recipes. If zero, it will not be set.
     * @return The builder instance for method chaining.
     */
    public ComplexSearchPOJORequestBuilder setNumberOfRecipes(int numberOfRecipes) {
      this.numberOfRecipes = numberOfRecipes;
      return this;
    }

    /**
     * Builds the ComplexSearchPOJORequest instance with the specified parameters.
     *
     * @return A new ComplexSearchPOJORequest object.
     */
    public ComplexSearchPOJORequest build() {
      return new ComplexSearchPOJORequest(this);
    }
  }
}