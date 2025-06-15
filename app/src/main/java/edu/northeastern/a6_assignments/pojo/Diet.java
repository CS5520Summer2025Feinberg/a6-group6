package edu.northeastern.a6_assignments.pojo;

/**
 * Diet is an enumeration representing various dietary preferences.
 */
public enum Diet {
    GLUTEN_FREE("Gluten Free"),
    KETOGENIC("Ketogenic"),
    VEGETARIAN("Vegetarian"),
    LACTO_VEGETARIAN("Lacto Vegetarian"),
    OVO_VEGETARIAN("Ovo Vegetarian"),
    VEGAN("Vegan"),
    PESCETARIAN("Pescetarian"),
    PALEO("Paleo"),
    PRIMAL("Primal"),
    LOW_FODMAP("Low Fodmap"),
    WHOLE30("Whole30");

    // The string representation of the diet
    private final String diet;

    /**
     * Constructor for the Diet enum.
     *
     * @param diet The string representation of the diet.
     */
    Diet(String diet) {
        this.diet = diet;
    }

    /**
     * Returns the string representation of the diet.
     *
     * @return The diet as a string.
     */
    public String getValue() {
        return diet;
    }

    /**
     * Returns the Diet enum constant corresponding to the given string value.
     *
     * @param value The string representation of the diet.
     * @return The Diet enum constant.
     * @throws IllegalArgumentException if no enum constant matches the given value.
     */
    public static Diet fromValue(String value) {
        for (Diet d : Diet.values()) {
            if (d.getValue().equalsIgnoreCase(value)) {
                return d;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}
