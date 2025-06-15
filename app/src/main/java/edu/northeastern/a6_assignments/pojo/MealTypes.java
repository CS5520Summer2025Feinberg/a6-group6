package edu.northeastern.a6_assignments.pojo;

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

  private final String mealType;

  MealTypes(String mealType) {
    this.mealType = mealType;
  }

  public String getValue() {
    return mealType;
  }
}