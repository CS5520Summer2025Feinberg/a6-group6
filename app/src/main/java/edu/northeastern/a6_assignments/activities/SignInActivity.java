package edu.northeastern.a6_assignments.activities;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import edu.northeastern.a6_assignments.R;

/**
 * SignIn is an Android activity that handles user sign-in functionality. It sets up the user
 * interface and applies edge-to-edge display settings.
 */
public class SignInActivity extends AppCompatActivity {

  // UI elements for the sign-in activity
  private EditText usernameField;
  private Button submitButton;

  // Variable to store the username entered by the user
  private String username;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_sign_in);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    // Initialize the elements in the layout
    usernameField = findViewById(R.id.usernameField);
    submitButton = findViewById(R.id.submitButton);

    // Set an OnClickListener for the submit button
    submitButton.setOnClickListener(v -> {
      username = usernameField.getText().toString();
      if (username.isEmpty()) {
        usernameField.setError("Username cannot be empty");
      } else {
        // Trigger the home page activity
      }
    });
  }
}