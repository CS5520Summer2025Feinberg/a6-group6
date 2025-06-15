package edu.northeastern.a6_assignments.pojo;

/**
 * MealTypes is an enumeration representing different types of meals.
 */
public enum MealTypes {
  MAIN_COURSE("Main Course"),
  SIDE_DISH("Side Dish"),
  DESSERT("Dessert"),
  APPETIZER("Appetizer"),
  SALAD("Salad"),
  BREAD("Bread"),
  BREAKFAST("Breakfast"),
  SOUP("Soup"),
  BEVERAGE("Beverage"),
  SAUCE("Sauce"),
  MARINADE("Marinade"),
  FINGERFOOD("Fingerfood"),
  SNACK("Snack"),
  DRINK("Drink");

  // The string representation of the meal type
  private final String mealType;

  /**
   * Constructor for MealTypes enum.
   *
   * @param mealType The string representation of the meal type.
   */
  MealTypes(String mealType) {
    this.mealType = mealType;
  }

  // Returns the string representation of the meal type.
  public String getValue() {
    return mealType;
  }

  /**
   * Returns the MealTypes enum constant that matches the given string value.
   *
   * @param value The string representation of the meal type.
   * @return The corresponding MealTypes enum constant.
   * @throws IllegalArgumentException if no enum constant matches the given value.
   */
  public static MealTypes fromValue(String value) {
    for (MealTypes m : MealTypes.values()) {
      if (m.getValue().equalsIgnoreCase(value)) {
        return m;
      }
    }
    throw new IllegalArgumentException("No enum constant with value " + value);
  }
}