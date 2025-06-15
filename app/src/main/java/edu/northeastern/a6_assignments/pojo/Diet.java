package edu.northeastern.a6_assignments.pojo;

public enum Diet {
    GLUTEN_FREE("gluten-free"),
    KETOGENIC("ketogenic"),
    VEGETARIAN("vegetarian"),
    LACTO_VEGETARIAN("lacto-vegetarian"),
    OVO_VEGETARIAN("ovo-vegetarian"),
    VEGAN("vegan"),
    PESCETARIAN("pescetarian"),
    PALEO("paleo"),
    PRIMAL("primal"),
    LOW_FODMAP("low-fodmap"),
    WHOLE30("whole30");

    private final String diet;

    Diet(String diet) {
        this.diet = diet;
    }

    public String getValue() {
        return diet;
    }
}
