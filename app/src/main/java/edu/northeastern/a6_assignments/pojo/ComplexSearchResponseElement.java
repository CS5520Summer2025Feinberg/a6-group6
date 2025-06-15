package edu.northeastern.a6_assignments.pojo;

/**
 * ComplexSearchResponseElement represents an element in a complex search response. It contains
 * fields for the ID, title, image URL, and image type of the element.
 */
public class ComplexSearchResponseElement {

  //Fields for the element
  int id;
  String title;
  String image;
  String imageType;

  /**
   * Method to get the ID of the element.
   */
  public int getId() {
    return id;
  }

  /**
   * Method to set the ID of the element.
   *
   * @param id The ID to set.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Method to get the title of the element.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Method to set the title of the element.
   *
   * @param title The title to set.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Method to get the image URL of the element.
   */
  public String getImage() {
    return image;
  }

  /**
   * Method to set the image URL of the element.
   *
   * @param image The image URL to set.
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * Method to get the image type of the element.
   */
  public String getImageType() {
    return imageType;
  }

  /**
   * Method to set the image type of the element.
   *
   * @param imageType The image type to set.
   */
  public void setImageType(String imageType) {
    this.imageType = imageType;
  }
}
