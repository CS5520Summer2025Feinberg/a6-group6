package edu.northeastern.a6_assignments.pojo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {

  // User's first and last name
  private String firstName;
  private String lastName;

  /**
   * Constructor to create a Users object with first and last name.
   *
   * @param firstName First name of the user
   * @param lastName  Last name of the user
   */
  public Users(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   * Default constructor required for calls to DataSnapshot.getValue(Users.class)
   * This is necessary for Firebase to deserialize the object.
   */
  public Users() {
  }

  /**
   * Getter for the first name of the user.
   * @return First name of the user
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Setter for the first name of the user.
   * @param firstName First name of the user
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Getter for the last name of the user.
   * @return Last name of the user
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Setter for the last name of the user.
   * @param lastName Last name of the user
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
