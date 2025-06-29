package edu.northeastern.a6_assignments.pojo;

import androidx.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {
    private String firstName;

    private String lastName;

    public Users(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Users() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
