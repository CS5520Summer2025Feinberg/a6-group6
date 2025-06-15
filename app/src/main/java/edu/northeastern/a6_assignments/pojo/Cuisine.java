package edu.northeastern.a6_assignments.pojo;

/**
 * Cuisine is an enumeration representing various types of cuisines.
 */
public enum Cuisine {
    AFRICAN("african"),
    ASIAN("asian"),
    AMERICAN("american"),
    BRITISH("british"),
    CAJUN("cajun"),
    CARIBBEAN("caribbean"),
    CHINESE("chinese"),
    EASTERN_EUROPEAN("eastern-european"),
    EUROPEAN_FRENCH("european-french"),
    GERMAN("german"),
    GREEK("greek"),
    INDIAN("indian"),
    IRISH("irish"),
    ITALIAN("italian"),
    JAPANESE("japanese"),
    JEWISH("jewish"),
    KOREAN("korean"),
    LATIN_AMERICAN("latin-american"),
    MEDITERRANEAN("mediterranean"),
    MEXICAN("mexican"),
    MIDDLE_EASTERN("middle-eastern"),
    NORDIC("nordic"),
    SOUTHERN("southern"),
    SPANISH("spanish"),
    THAI("thai"),
    VIETNAMESE("vietnamese");

    // The string representation of the cuisine
    private final String cuisine;

    /**
     * Constructor for the Cuisine enum.
     *
     * @param cuisine The string representation of the cuisine.
     */
    Cuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    /**
     * Returns the string representation of the cuisine.
     *
     * @return The string value of the cuisine.
     */
    public String getValue() {
        return cuisine;
    }

    /**
     * Returns the Cuisine enum constant that matches the given string value.
     *
     * @param value The string representation of the cuisine.
     * @return The Cuisine enum constant corresponding to the given value.
     * @throws IllegalArgumentException if no matching enum constant is found.
     */
    public static Cuisine fromValue(String value) {
        for (Cuisine c : Cuisine.values()) {
            if (c.getValue().equalsIgnoreCase(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}
