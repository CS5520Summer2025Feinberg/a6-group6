package edu.northeastern.a6_assignments.pojo;

/**
 * Intolerance is an enum representing various food intolerances.
 */
public enum Intolerance {
  DAIRY("Dairy"),
  EGG("Egg"),
  GLUTEN("Gluten"),
  GRAIN("Grain"),
  PEANUT("Peanut"),
  SEAFOOD("Seafood"),
  SESAME("Sesame"),
  SHELLFISH("Shellfish"),
  SOY("Soy"),
  SULFITE("Sulfite"),
  TREE_NUT("Tree Nut"),
  WHEAT("Wheat");

  // The string representation of the intolerance
  private final String intolerance;

  /**
   * Constructor for the Intolerance enum.
   *
   * @param intolerance The string representation of the intolerance.
   */
  Intolerance(String intolerance) {
    this.intolerance = intolerance;
  }

  /**
   * Returns the string representation of the intolerance.
   *
   * @return The string value of the intolerance.
   */
  public String getValue() {
    return intolerance;
  }

  /**
   * Returns the Intolerance enum constant that matches the given string value.
   *
   * @param value The string value to match.
   * @return The corresponding Intolerance enum constant.
   * @throws IllegalArgumentException if no matching enum constant is found.
   */
  public static Intolerance fromValue(String value) {
    for (Intolerance i : Intolerance.values()) {
      if (i.getValue().equalsIgnoreCase(value)) {
        return i;
      }
    }
    throw new IllegalArgumentException("No enum constant with value " + value);
  }
}
