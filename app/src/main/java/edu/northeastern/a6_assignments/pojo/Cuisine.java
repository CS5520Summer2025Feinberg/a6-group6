package edu.northeastern.a6_assignments.pojo;

/**
 * Cuisine is an enumeration representing various types of cuisines.
 */
public enum Cuisine {
    AFRICAN("African"),
    ASIAN("Asian"),
    AMERICAN("American"),
    BRITISH("British"),
    CAJUN("Cajun"),
    CARIBBEAN("Caribbean"),
    CHINESE("Chinese"),
    EASTERN_EUROPEAN("Eastern European"),
    EUROPEAN_FRENCH("European French"),
    GERMAN("German"),
    GREEK("Greek"),
    INDIAN("Indian"),
    IRISH("Irish"),
    ITALIAN("Italian"),
    JAPANESE("Japanese"),
    JEWISH("Jewish"),
    KOREAN("Korean"),
    LATIN_AMERICAN("Latin American"),
    MEDITERRANEAN("Mediterranean"),
    MEXICAN("Mexican"),
    MIDDLE_EASTERN("Middle Eastern"),
    NORDIC("Nordic"),
    SOUTHERN("Southern"),
    SPANISH("Spanish"),
    THAI("Thai"),
    VIETNAMESE("Vietnamese");

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
