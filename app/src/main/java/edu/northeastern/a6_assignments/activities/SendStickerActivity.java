package edu.northeastern.a6_assignments.activities;

import android.content.SharedPreferences;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to send stickers to other users.
 * Users can select a sticker and a recipient from a dynamically populated list.
 */
public class SendStickerActivity extends AppCompatActivity implements View.OnClickListener {

  // Views for sticker options, user selection spinner, and send button
  private ImageView[] stickerOptions;
  private ImageView selectedSticker;
  private Spinner spinnerSelectUser;
  private Button btnSend;

  // HashMap to map sticker tags to drawable resources
  private HashMap<String, Integer> stickerImageMap;

  // Dynamic user list populated from Firebase
  private List<String> usersList;

  // Adapter for the user spinner
  private ArrayAdapter<String> usersAdapter;

  // Current username of the logged-in user
  private String currentUsername;

  // Firebase reference to the users node
  private DatabaseReference usersRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_sticker);

    // Get the current username from Intent
    SharedPreferences sharedPreferences = getSharedPreferences("StickerAppPrefs", MODE_PRIVATE);
    currentUsername = sharedPreferences.getString("loggedInUser", null);

    usersRef = FirebaseDatabase.getInstance().getReference().child("users");

    // Initialize views and data structures
    initializeStickerImageMap();
    initializeViews();
    setupStickerSelection();
    setupUserSpinner();
    setupSendButton();
    loadStickerImages();
    loadUsersFromFirebase();
  }

  /**
   * Initializes the sticker image map with drawable resources.
   * Maps sticker tags to their corresponding drawable resources.
   */
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

  /**
   * Initializes the views used in this activity.
   * Sets up the ImageViews for stickers, spinner for user selection, and send button.
   */
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

  /**
   * Loads sticker images into the ImageViews based on the tags.
   * Uses the HashMap to get the drawable resources for each sticker.
   */
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

  /**
   * Sets up click listeners for all sticker options.
   * When a sticker is clicked, it will be selected and highlighted.
   */
  private void setupStickerSelection() {
    // Set click listeners for all sticker options
    for (ImageView sticker : stickerOptions) {
      sticker.setOnClickListener(this);
    }
  }

  /**
   * Initializes the user spinner with a default option.
   * The spinner will be populated with users from Firebase.
   */
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

  /**
   * Loads users from Firebase Realtime Database and populates the spinner.
   * Excludes the current user from the list.
   */
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

  /**
   * Sets up the send button click listener.
   * Validates the selection and sends the sticker if valid.
   */
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

  /**
   * Selects a sticker when clicked.
   * Deselects all other stickers and highlights the selected one.
   *
   * @param clickedSticker The ImageView of the clicked sticker.
   */
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

  /**
   * Validates the user's selection before sending the sticker.
   * Ensures a sticker is selected and a user is chosen from the spinner.
   *
   * @return true if selection is valid, false otherwise.
   */
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

  /**
   * Sends the selected sticker to the chosen user.
   * Creates a message object and saves it to Firebase Realtime Database.
   */
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

  /**
   * Returns a user-friendly name for the sticker based on its tag.
   * This is used to display the sticker name in the UI.
   *
   * @param tag The tag of the sticker.
   * @return The user-friendly name of the sticker.
   */
  private String getStickerName(String tag) {
    switch (tag) {
      case "sticker1":
        return "My Sticker 1";
      case "sticker2":
        return "My Sticker 2";
      case "sticker3":
        return "My Sticker 3";
      case "sticker4":
        return "My Sticker 4";
      case "sticker5":
        return "My Sticker 5";
      case "sticker6":
        return "My Sticker 6";
      default:
        return tag;
    }
  }

  /**
   * Updates the sticker image in the map and reloads the images.
   * This method can be used to dynamically change sticker images.
   *
   * @param stickerTag The tag of the sticker to update.
   * @param drawableResource The new drawable resource ID for the sticker.
   */
  public void updateStickerImage(String stickerTag, int drawableResource) {
    stickerImageMap.put(stickerTag, drawableResource);
    loadStickerImages();
  }

  /**
   * Returns information about the currently selected sticker.
   * This includes the tag, name, and resource ID of the sticker.
   *
   * @return A map containing the sticker information, or null if no sticker is selected.
   */
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