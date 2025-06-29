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
 * StickerAppHomeActivity is an Android activity that serves as the home screen for the Sticker App.
 */
public class StickerAppHomeActivity extends AppCompatActivity {

  private String username;

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

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      username = bundle.getString("username");
    }
  }

  public void onSendStickerActivity(View view) {
    Intent intent = new Intent(StickerAppHomeActivity.this, SendStickerActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("username", username);
    intent.putExtras(bundle);
    startActivity(intent);
  }

  public void onStickerReceivedHistoryActivity(View view) {
    Intent intent = new Intent(StickerAppHomeActivity.this, StickerReceivedActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("username", username);
    intent.putExtras(bundle);
    startActivity(intent);
  }

  public void onStickerSentHistoryActivity(View view) {
    Intent intent = new Intent(StickerAppHomeActivity.this, StickerSentActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("username", username);
    intent.putExtras(bundle);
    startActivity(intent);
  }
}