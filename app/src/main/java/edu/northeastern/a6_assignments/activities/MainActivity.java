package edu.northeastern.a6_assignments.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.northeastern.a6_assignments.R;

/**
 * MainActivity is the entry point of the application. It sets up the user interface and handles
 * user interactions.
 */
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });
  }

  /**
   * This method is called when the user clicks the "Find Recipes" button. It starts the
   * FoodRecipeRequestActivity.
   *
   * @param view The view that was clicked.
   */
  public void onClickService(View view) {
    Intent intent = new Intent(MainActivity.this, FoodRecipeRequestActivity.class);
    startActivity(intent);
  }

  /**
   * This method is called when the user clicks the "About" button. It displays a new activity with
   * the developer's name.
   *
   * @param view The view that was clicked.
   */
  public void onAboutGroupClick(View view) {
    Intent intent = new Intent(MainActivity.this, AboutGroup.class);
    startActivity(intent);
  }

  /**
   * This method is called when the user clicks the "Firebase Assignment" button. It starts the
   * Firebase Assignment activity.
   *
   * @param view The view that was clicked.
   */
  public void onFirebaseAssignmentClick(View view) {
    Intent intent = new Intent(MainActivity.this, SignIn.class);
    startActivity(intent);
  }
}