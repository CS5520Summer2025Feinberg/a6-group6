package edu.northeastern.a6_assignments.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.*;

import edu.northeastern.a6_assignments.R;
import edu.northeastern.a6_assignments.activities.StickerReceivedActivity;
import java.util.HashSet;
import java.util.Set;

public class FirebaseMessageListener extends Service {

  private DatabaseReference messagesRef;
  private DatabaseReference usersRef;
  private ValueEventListener messageListener;
  private String currentUsername;

  private final Set<String> notifiedMessageKeys = new HashSet<>();

  private static final String PREFS_NAME = "StickerAppPrefs";
  private static final String NOTIFIED_KEYS = "notified_message_keys";
  SharedPreferences notifiedPrefs;


  @Override
  public void onCreate() {
    super.onCreate();
    startForeground(1, getServiceNotification());

    SharedPreferences sharedPreferences = getSharedPreferences("StickerAppPrefs", MODE_PRIVATE);
    String loggedInUser = sharedPreferences.getString("loggedInUser", null);

    // Handle case where user is not logged in
    if (loggedInUser == null) {
      stopSelf();
      return;
    }

    notifiedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    notifiedMessageKeys.addAll(notifiedPrefs.getStringSet(NOTIFIED_KEYS, new HashSet<>()));

    currentUsername = loggedInUser;
    messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");
    usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    attachMessageListener();
  }

  private void attachMessageListener() {
    messageListener = new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot messageSnap : snapshot.getChildren()) {
          String receiverId = String.valueOf(messageSnap.child("receiverId").getValue());
          String messageKey = messageSnap.getKey();
          if (receiverId.equals(currentUsername) && !notifiedMessageKeys.contains(messageKey)) {
            String senderId = String.valueOf(messageSnap.child("senderId").getValue());
            String stickerId = String.valueOf(messageSnap.child("stickerId").getValue());
            usersRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot userSnap) {
                String firstName = String.valueOf(userSnap.child("firstName").getValue());
                String lastName = String.valueOf(userSnap.child("lastName").getValue());
                sendNotification(firstName, lastName, stickerId);
                notifiedMessageKeys.add(messageKey);
                notifiedPrefs.edit().putStringSet(NOTIFIED_KEYS, notifiedMessageKeys).apply();
              }
              @Override
              public void onCancelled(@NonNull DatabaseError error) {}
            });
          }
        }
      }
      @Override
      public void onCancelled(@NonNull DatabaseError error) {}
    };
    messagesRef.addValueEventListener(messageListener);
  }

  private int getStickerDrawable(String stickerId) {
    switch (stickerId) {
      case "sticker1":
        return R.drawable.sticker1;
      case "sticker2":
        return R.drawable.sticker2;
      case "sticker3":
        return R.drawable.sticker3;
      case "sticker4":
        return R.drawable.sticker4;
      case "sticker5":
        return R.drawable.sticker5;
      default:
        return R.drawable.ic_launcher_vsv_foreground;
    }
  }

  private void sendNotification(String firstName, String lastName, String stickerId) {
    int stickerResId = getStickerDrawable(stickerId);
    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), stickerResId);
    int notificationId = (int) System.currentTimeMillis();

    Intent intent = new Intent(this, StickerReceivedActivity.class);
    intent.putExtra("notification_id", notificationId);
    PendingIntent historyIntent = PendingIntent.getActivity(
        this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    String channelId = getString(R.string.sticker_notification_channel_id);

    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
        .bigPicture(largeIcon)
        .bigLargeIcon((Bitmap) null)
        .setBigContentTitle("New Sticker from " + firstName + " " + lastName);

    Notification notification = new NotificationCompat.Builder(this, channelId)
        .setContentTitle(firstName + " " + lastName + " sent you a sticker!")
        .setSmallIcon(R.drawable.ic_launcher_vsv_foreground)
        .setLargeIcon(largeIcon)
        .setStyle(bigPictureStyle)
        .addAction(R.drawable.ic_history_icon, "History", historyIntent)
        .setContentIntent(historyIntent)
        .setAutoCancel(true)
        .build();

    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    notification.flags |= Notification.FLAG_AUTO_CANCEL;
    notificationManager.notify(notificationId, notification);
  }

  private Notification getServiceNotification() {
    String channelId = getString(R.string.sticker_notification_channel_id);
    return new NotificationCompat.Builder(this, channelId)
        .setContentTitle("Sticker Listener Running")
        .setSmallIcon(R.drawable.ic_launcher_vsv_foreground)
        .build();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (messagesRef != null && messageListener != null) {
      messagesRef.removeEventListener(messageListener);
    }
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}