package edu.northeastern.a6_assignments.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.a6_assignments.R;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.a6_assignments.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendStickerActivity extends AppCompatActivity implements View.OnClickListener {

  private ImageView[] stickerOptions;
  private ImageView selectedSticker;
  private Spinner spinnerSelectUser;
  private Button btnSend;

  // HashMap to map sticker tags to drawable resources
  private HashMap<String, Integer> stickerImageMap;

  // Dynamic user list populated from Firebase
  private List<String> usersList;
  private ArrayAdapter<String> usersAdapter;
  private String currentUsername; // Store current user to exclude from list

  private DatabaseReference usersRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_sticker);

    // Get the current username from Intent
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      currentUsername = bundle.getString("username");
    }

    usersRef = FirebaseDatabase.getInstance().getReference().child("users");

    initializeStickerImageMap();
    initializeViews();
    setupStickerSelection();
    setupUserSpinner();
    setupSendButton();
    loadStickerImages();
    loadUsersFromFirebase();
  }

  private void initializeStickerImageMap() {
    stickerImageMap = new HashMap<>();
    // Map sticker tags to drawable resources
    stickerImageMap.put("sticker1", R.drawable.sticker1);
    stickerImageMap.put("sticker2", R.drawable.sticker1);
    stickerImageMap.put("sticker3", R.drawable.sticker1);
    stickerImageMap.put("sticker4", R.drawable.sticker1);
    stickerImageMap.put("sticker5", R.drawable.sticker1);
    stickerImageMap.put("sticker6", R.drawable.sticker1);
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
    // Initialize the users list with default option
    usersList = new ArrayList<>();
    usersList.add("Select a user...");

    // Create adapter for user spinner
    usersAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            usersList
    );
    usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerSelectUser.setAdapter(usersAdapter);
  }

  private void loadUsersFromFirebase() {
    usersRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // Clear existing users (except the default option)
        usersList.clear();
        usersList.add("Select a user...");

        // Add users from Firebase
        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
          String username = userSnapshot.getKey();

          // Exclude current user from the list
          if (username != null && !username.equals(currentUsername)) {
            usersList.add(username);
          }
        }

        // Notify adapter of data change
        usersAdapter.notifyDataSetChanged();

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(SendStickerActivity.this,
                "Failed to load users: " + databaseError.getMessage(),
                Toast.LENGTH_LONG).show();
      }
    });
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
    String selectedUser = usersList.get(spinnerSelectUser.getSelectedItemPosition());
    String stickerTag = (String) selectedSticker.getTag();
    String stickerName = getStickerName(stickerTag);

    // Create the message data
    DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");
    String messageId = messagesRef.push().getKey(); // Generate unique message ID

    if (messageId != null) {
      // Create message object
      Map<String, Object> messageData = new HashMap<>();
      messageData.put("receiverId", selectedUser);
      messageData.put("senderId", currentUsername);
      messageData.put("stickerId", stickerTag);
      messageData.put("timeStamp", System.currentTimeMillis()); // Current timestamp in milliseconds

      // Save to Firebase
      messagesRef.child(messageId).setValue(messageData)
              .addOnSuccessListener(aVoid -> {
                Toast.makeText(SendStickerActivity.this,
                        "Sticker sent successfully to " + selectedUser,
                        Toast.LENGTH_LONG).show();
              })
              .addOnFailureListener(e -> {
                Toast.makeText(SendStickerActivity.this,
                        "Failed to send sticker: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
              });
    } else {
      Toast.makeText(this, "Failed to generate message ID", Toast.LENGTH_SHORT).show();
    }
  }

  private String getStickerName(String tag) {
    switch (tag) {
      case "sticker1": return "My Sticker 1";
      case "sticker2": return "My Sticker 2";
      case "sticker3": return "My Sticker 3";
      case "sticker4": return "My Sticker 4";
      case "sticker5": return "My Sticker 5";
      case "sticker6": return "My Sticker 6";
      default: return tag;
    }
  }

  public void updateStickerImage(String stickerTag, int drawableResource) {
    stickerImageMap.put(stickerTag, drawableResource);
    loadStickerImages();
  }

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