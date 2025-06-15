package edu.northeastern.a6_assignments.pojo;

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

    private final String cuisine;

    Cuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getValue() {
        return cuisine;
    }
}
