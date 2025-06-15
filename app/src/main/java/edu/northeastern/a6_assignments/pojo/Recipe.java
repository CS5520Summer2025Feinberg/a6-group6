package edu.northeastern.a6_assignments.pojo;

import java.io.Serializable;

/**
 * Recipe class represents a recipe with its details such as id, title, image, and image type. It
 * implements Serializable to allow instances to be passed between activities or saved in a bundle.
 */
public class Recipe implements Serializable {

  // Fields for the Recipe class
  private int id;
  private String title;
  private String image;
  private String imageType;

  /**
   * Default constructor for the Recipe class. Initializes a new instance of Recipe with default
   * values.
   */
  public Recipe() {
  }

  /**
   * Parameterized constructor for the Recipe class. Initializes a new instance of Recipe with the
   * specified id, title, image, and image type.
   *
   * @param id        The unique identifier for the recipe.
   * @param title     The title of the recipe.
   * @param image     The URL or path to the recipe's image.
   * @param imageType The type of the image (e.g., "jpg", "png").
   */
  public Recipe(int id, String title, String image, String imageType) {
    this.id = id;
    this.title = title;
    this.image = image;
    this.imageType = imageType;
  }

  /**
   * Gets the unique identifier of the recipe.
   *
   * @return The id of the recipe.
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the recipe.
   *
   * @param id The id to set for the recipe.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the title of the recipe.
   *
   * @return The title of the recipe.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the recipe.
   *
   * @param title The title to set for the recipe.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the image URL or path of the recipe.
   *
   * @return The image of the recipe.
   */
  public String getImage() {
    return image;
  }

  /**
   * Sets the image URL or path of the recipe.
   *
   * @param image The image to set for the recipe.
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * Gets the type of the recipe's image.
   *
   * @return The image type of the recipe.
   */
  public String getImageType() {
    return imageType;
  }

  /**
   * Sets the type of the recipe's image.
   *
   * @param imageType The image type to set for the recipe.
   */
  public void setImageType(String imageType) {
    this.imageType = imageType;
  }
}
