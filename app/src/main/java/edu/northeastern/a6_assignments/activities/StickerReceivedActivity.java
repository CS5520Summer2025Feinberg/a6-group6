package edu.northeastern.a6_assignments.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a6_assignments.Adapter.ReceivedStickerAdapter;
import edu.northeastern.a6_assignments.R;

import edu.northeastern.a6_assignments.pojo.StickerMessage;

public class StickerReceivedActivity extends AppCompatActivity {

  private RecyclerView recyclerViewReceivedStickers;
  private SwipeRefreshLayout swipeRefreshLayout;
  private ReceivedStickerAdapter adapter;
  private List<StickerMessage> receivedStickers;

  private DatabaseReference messagesRef;
  private String currentUsername;
  private Handler refreshHandler;
  private Runnable refreshRunnable;
  private static final int REFRESH_INTERVAL = 5000; // 5 seconds

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sticker_received);

    // Get current username from intent
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      currentUsername = bundle.getString("username");
    }

    initializeViews();
    setupRecyclerView();
    setupSwipeRefresh();
    loadReceivedStickers();
    setupAutoRefresh();
  }

  private void initializeViews() {
    recyclerViewReceivedStickers = findViewById(R.id.recyclerViewReceivedStickers);
    swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");
  }

  private void setupRecyclerView() {
    receivedStickers = new ArrayList<>();
    adapter = new ReceivedStickerAdapter(this, receivedStickers);
    recyclerViewReceivedStickers.setLayoutManager(new LinearLayoutManager(this));
    recyclerViewReceivedStickers.setAdapter(adapter);
  }

  private void setupSwipeRefresh() {
    swipeRefreshLayout.setOnRefreshListener(this::loadReceivedStickers);
    swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
    );
  }

  private void setupAutoRefresh() {
    refreshHandler = new Handler(Looper.getMainLooper());
    refreshRunnable = new Runnable() {
      @Override
      public void run() {
        loadReceivedStickers();
        refreshHandler.postDelayed(this, REFRESH_INTERVAL);
      }
    };
  }

  private void loadReceivedStickers() {
    if (currentUsername == null) {
      Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
      return;
    }

    messagesRef.orderByChild("receiverId").equalTo(currentUsername)
            .addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receivedStickers.clear();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                  try {
                    String senderId = messageSnapshot.child("senderId").getValue(String.class);
                    String stickerId = messageSnapshot.child("stickerId").getValue(String.class);
                    Long timestamp = messageSnapshot.child("timeStamp").getValue(Long.class);

                    if (senderId != null && stickerId != null && timestamp != null) {
                      StickerMessage message = new StickerMessage(
                              messageSnapshot.getKey(),
                              senderId,
                              currentUsername,
                              stickerId,
                              timestamp
                      );
                      receivedStickers.add(message);
                    }
                  } catch (Exception e) {
                    // Skip malformed messages
                  }
                }

                // Sort by timestamp (most recent first)
                receivedStickers.sort((m1, m2) ->
                        Long.compare(m2.getTimestamp(), m1.getTimestamp()));

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StickerReceivedActivity.this,
                        "Failed to load stickers: " + databaseError.getMessage(),
                        Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
              }
            });
  }

  @Override
  protected void onResume() {
    super.onResume();
    // Start auto-refresh when activity becomes visible
    refreshHandler.post(refreshRunnable);
  }

  @Override
  protected void onPause() {
    super.onPause();
    // Stop auto-refresh when activity is not visible
    refreshHandler.removeCallbacks(refreshRunnable);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (refreshHandler != null) {
      refreshHandler.removeCallbacks(refreshRunnable);
    }
  }
}