package edu.northeastern.a6_assignments.pojo;

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

  private final String intolerance;

  Intolerance(String intolerance) {
    this.intolerance = intolerance;
  }

  public String getValue() {
    return intolerance;
  }
}
