package edu.northeastern.a6_assignments.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.a6_assignments.R;
import java.util.HashMap;
import java.util.Map;

public class SendStickerActivity extends AppCompatActivity implements View.OnClickListener {

  private ImageView[] stickerOptions;
  private ImageView selectedSticker;
  private Spinner spinnerSelectUser;
  private Button btnSend;

  // HashMap to map sticker tags to drawable resources
  private HashMap<String, Integer> stickerImageMap;

  // Sample user list - replace with your actual user data
  private String[] users = {"Select a user...", "John Doe", "Jane Smith", "Mike Johnson", "Sarah Wilson"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_sticker);

    initializeStickerImageMap();
    initializeViews();
    setupStickerSelection();
    setupUserSpinner();
    setupSendButton();
    loadStickerImages();
  }

  private void initializeStickerImageMap() {
    stickerImageMap = new HashMap<>();
    // Map sticker tags to drawable resources
    stickerImageMap.put("sticker1", R.drawable.sticker1);
    stickerImageMap.put("sticker2", R.drawable.sticker2);
    stickerImageMap.put("sticker3", R.drawable.sticker3);
    stickerImageMap.put("sticker4", R.drawable.sticker4);
    stickerImageMap.put("sticker5", R.drawable.sticker5);
    stickerImageMap.put("sticker6", R.drawable.sticker6);
  }

  private void initializeViews() {
    // Initialize sticker ImageViews
    stickerOptions = new ImageView[6];
    stickerOptions[0] = findViewById(R.id.stickerOption1);
    stickerOptions[1] = findViewById(R.id.stickerOption2);
    stickerOptions[2] = findViewById(R.id.stickerOption3);
    stickerOptions[3] = findViewById(R.id.stickerOption4);
    stickerOptions[4] = findViewById(R.id.stickerOption5);
    stickerOptions[5] = findViewById(R.id.stickerOption6);

    spinnerSelectUser = findViewById(R.id.spinnerSelectUser);
    btnSend = findViewById(R.id.btnSend);
  }

  private void loadStickerImages() {
    // Load images into ImageViews using the HashMap
    for (ImageView stickerView : stickerOptions) {
      String tag = (String) stickerView.getTag();
      if (tag != null && stickerImageMap.containsKey(tag)) {
        Integer imageResource = stickerImageMap.get(tag);
        if (imageResource != null) {
          stickerView.setImageResource(imageResource);
        }
      }
    }
  }

  private void setupStickerSelection() {
    // Set click listeners for all sticker options
    for (ImageView sticker : stickerOptions) {
      sticker.setOnClickListener(this);
    }
  }

  private void setupUserSpinner() {
    // Create adapter for user spinner
    ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            users
    );
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerSelectUser.setAdapter(adapter);
  }

  private void setupSendButton() {
    btnSend.setOnClickListener(v -> {
      if (validateSelection()) {
        sendSticker();
      }
    });
  }

  @Override
  public void onClick(View v) {
    // Handle sticker selection
    if (v instanceof ImageView) {
      selectSticker((ImageView) v);
    }
  }

  private void selectSticker(ImageView clickedSticker) {
    // Deselect all stickers first
    for (ImageView sticker : stickerOptions) {
      sticker.setSelected(false);
    }

    // Select the clicked sticker
    clickedSticker.setSelected(true);
    selectedSticker = clickedSticker;

    // Optional: Add visual feedback
    String stickerName = getStickerName((String) clickedSticker.getTag());
    Toast.makeText(this, "Selected: " + stickerName, Toast.LENGTH_SHORT).show();
  }

  private boolean validateSelection() {
    // Check if a sticker is selected
    if (selectedSticker == null) {
      Toast.makeText(this, "Please select a sticker", Toast.LENGTH_SHORT).show();
      return false;
    }

    // Check if a user is selected (not the default option)
    if (spinnerSelectUser.getSelectedItemPosition() == 0) {
      Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show();
      return false;
    }

    return true;
  }

  private void sendSticker() {
    String selectedUser = users[spinnerSelectUser.getSelectedItemPosition()];
    String stickerTag = (String) selectedSticker.getTag();
    String stickerName = getStickerName(stickerTag);

    // Implement your send sticker logic here
    Toast.makeText(this,
            "Sending " + stickerName + " to " + selectedUser,
            Toast.LENGTH_LONG).show();

    // Optional: Close activity after sending
    // finish();
  }

  // Helper method to get user-friendly sticker names
  private String getStickerName(String tag) {
    switch (tag) {
      case "sticker1": return "Happy Face";
      case "sticker2": return "Thumbs Up";
      case "sticker3": return "Heart";
      case "sticker4": return "Star";
      case "sticker5": return "Fire";
      case "sticker6": return "Peace";
      default: return tag;
    }
  }

  // Method to programmatically update sticker images (if needed later)
  public void updateStickerImage(String stickerTag, int drawableResource) {
    stickerImageMap.put(stickerTag, drawableResource);
    loadStickerImages(); // Reload all images
  }

  // Method to set users programmatically
  public void setUsers(String[] userList) {
    this.users = userList;
    setupUserSpinner();
  }

  // Method to get selected sticker info
  public Map<String, Object> getSelectedStickerInfo() {
    if (selectedSticker != null) {
      Map<String, Object> info = new HashMap<>();
      String tag = (String) selectedSticker.getTag();
      info.put("tag", tag);
      info.put("name", getStickerName(tag));
      info.put("resourceId", stickerImageMap.get(tag));
      return info;
    }
    return null;
  }
}