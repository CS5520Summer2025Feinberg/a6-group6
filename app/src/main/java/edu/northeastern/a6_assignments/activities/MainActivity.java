package edu.northeastern.a6_assignments.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;
import android.widget.Toast;
import android.Manifest;

import edu.northeastern.a6_assignments.R;

/**
 * MainActivity is the entry point of the application. It sets up the user interface and handles
 * user interactions.
 */
public class MainActivity extends AppCompatActivity {

  private static final int NOTIFICATION_REQUEST_CODE = 101;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Create the notification channel for Android O and above
    createNotificationChannel();
    requestPermission(NOTIFICATION_REQUEST_CODE);

    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    // Start the Firebase message listener service
    Intent serviceIntent = new Intent(MainActivity.this,
        edu.northeastern.a6_assignments.helpers.FirebaseMessageListener.class);
    startForegroundService(serviceIntent);
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
    Intent intent = new Intent(MainActivity.this, AboutGroupActivity.class);
    startActivity(intent);
  }

  /**
   * This method is called when the user clicks the "Firebase Assignment" button. It starts the
   * Firebase Assignment activity.
   *
   * @param view The view that was clicked.
   */
  public void onFirebaseAssignmentClick(View view) {
    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
    startActivity(intent);
  }

  /**
   * This method creates a notification channel for Android O and above. It is necessary to create a
   * notification channel to display notifications on these versions of Android.
   */
  public void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = getString(R.string.sticker_notification_channel);
      String description = getString(R.string.sticker_notification_channel_description);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(
          getString(R.string.sticker_notification_channel_id), name, importance);
      channel.setDescription(description);
      channel.enableLights(true);
      channel.setLightColor(Color.RED);

      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      if (notificationManager != null) {
        notificationManager.createNotificationChannel(channel);
      }
    }
  }

  /**
   * Requests notification permission for Android Tiramisu (API level 33) and above. If the
   * permission is not granted, it prompts the user to allow notifications.
   *
   * @param requestCode The request code for the permission request.
   */
  protected void requestPermission(int requestCode) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      int permission = ContextCompat.checkSelfPermission(this,
          Manifest.permission.POST_NOTIFICATIONS);
      if (permission != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.POST_NOTIFICATIONS}, requestCode);
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == NOTIFICATION_REQUEST_CODE) {
      if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.POST_NOTIFICATIONS)) {
          Toast.makeText(this,
              "Notification permission is not granted. Notifications will not show up.",
              Toast.LENGTH_LONG).show();
        }
      }
    }
  }
}