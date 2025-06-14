package edu.northeastern.a6_assignments.pojo;

public enum Diet {
  GLUTEN_FREE("Gluten Free"),
  KETOGENIC("Ketogenic"),
  VEGETARIAN("Vegetarian"),
  LACTO_VEGETARIAN("Lacto-Vegetarian"),
  OVO_VEGETARIAN("Ovo-Vegetarian"),
  VEGAN("Vegan"),
  PESCETARIAN("Pescetarian"),
  PALEO("Paleo"),
  PRIMAL("Primal"),
  LOW_FODMAP("Low FODMAP"),
  WHOLE30("Whole30");

  private final String diet;

  Diet(String diet) {
    this.diet = diet;
  }

  public String getDiet() {
    return diet;
  }
}