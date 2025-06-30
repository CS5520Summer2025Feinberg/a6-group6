package edu.northeastern.a6_assignments.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import edu.northeastern.a6_assignments.R;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity to display the number of stickers sent by the current user.
 */
public class StickerSentActivity extends AppCompatActivity {

  // Firebase database reference
  private DatabaseReference messagesRef;
  String currentUsername;
  private final TextView[] stickerCountViews = new TextView[6];

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_sticker_sent);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    // Retrieve the current username from SharedPreferences
    SharedPreferences sharedPreferences = getSharedPreferences("StickerAppPrefs", MODE_PRIVATE);
    currentUsername = sharedPreferences.getString("loggedInUser", null);

    messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");

    // Initialize TextViews for sticker counts
    stickerCountViews[0] = findViewById(R.id.sticker1_count);
    stickerCountViews[1] = findViewById(R.id.sticker2_count);
    stickerCountViews[2] = findViewById(R.id.sticker3_count);
    stickerCountViews[3] = findViewById(R.id.sticker4_count);
    stickerCountViews[4] = findViewById(R.id.sticker5_count);
    stickerCountViews[5] = findViewById(R.id.sticker6_count);

    fetchAndDisplayStickerCounts();
  }

  /**
   * Fetches the sticker counts from Firebase and updates the UI.
   */
  private void fetchAndDisplayStickerCounts() {
    messagesRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        // Map stickerId to count
        Map<String, Integer> stickerCounts = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
          stickerCounts.put("sticker" + i, 0);
        }

        for (DataSnapshot messageSnap : snapshot.getChildren()) {
          String senderId = String.valueOf(messageSnap.child("senderId").getValue());
          String stickerId = String.valueOf(messageSnap.child("stickerId").getValue());
          if (senderId.equals(currentUsername) && stickerCounts.containsKey(stickerId)) {
            stickerCounts.put(stickerId, stickerCounts.get(stickerId) + 1);
          }
        }

        // Update UI
        for (int i = 1; i <= 6; i++) {
          String stickerKey = "sticker" + i;
          int count = stickerCounts.get(stickerKey) != null ? stickerCounts.get(stickerKey) : 0;
          stickerCountViews[i - 1].setText(getString(R.string.count, count));
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {}
    });
  }
}