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
 * StickerAppHomeActivity is an Android activity that serves as the home screen for the Sticker
 * App.
 */
public class StickerAppHomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_sticker_app_home);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });
  }

  /**
   * Handles the click event for the "Send Sticker" button.
   * Starts the SendStickerActivity to allow users to send stickers.
   *
   * @param view The view that was clicked.
   */
  public void onSendStickerActivity(View view) {
    Intent intent = new Intent(StickerAppHomeActivity.this, SendStickerActivity.class);
    startActivity(intent);
  }

  /**
   * Handles the click event for the "Sticker Received Sticker" button.
   * Starts the ReceiveStickerActivity to allow users to receive stickers.
   *
   * @param view The view that was clicked.
   */
  public void onStickerReceivedHistoryActivity(View view) {
    Intent intent = new Intent(StickerAppHomeActivity.this, StickerReceivedActivity.class);
    startActivity(intent);
  }

  /**
   * Handles the click event for the "Sticker Sent History" button.
   * Starts the StickerSentActivity to show the history of sent stickers.
   *
   * @param view The view that was clicked.
   */
  public void onStickerSentHistoryActivity(View view) {
    Intent intent = new Intent(StickerAppHomeActivity.this, StickerSentActivity.class);
    startActivity(intent);
  }

  /**
   * Handles the click event for the "Logout" button.
   * Clears the SharedPreferences and starts the SignInActivity.
   *
   * @param view The view that was clicked.
   */
  public void onLogout(View view) {
    // Clear SharedPreferences
    getSharedPreferences("StickerAppPrefs", MODE_PRIVATE)
        .edit()
        .clear()
        .apply();

    // Start LoginActivity (replace with your actual login activity class)
    Intent intent = new Intent(StickerAppHomeActivity.this, SignInActivity.class);
    startActivity(intent);
    finish();
  }
}