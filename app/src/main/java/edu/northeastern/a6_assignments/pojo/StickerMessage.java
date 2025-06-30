package edu.northeastern.a6_assignments.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Represents a sticker message in the chat application.
 * This class is used to store information about a sticker message sent between users.
 */
public class StickerMessage {

  // Elements of a sticker message
  private String messageId;
  private String senderId;
  private String receiverId;
  private String stickerId;
  private long timestamp;

  /**
   * Default constructor required for Firebase to deserialize the object.
   * It is important to have a no-argument constructor when using Firebase Realtime Database.
   */
  public StickerMessage() {
    // Default constructor for Firebase
  }

  /**
   * Constructor to create a StickerMessage object with all necessary fields.
   *
   * @param messageId   Unique identifier for the message.
   * @param senderId    ID of the user who sent the message.
   * @param receiverId  ID of the user who received the message.
   * @param stickerId   ID of the sticker being sent.
   * @param timestamp   Timestamp of when the message was sent.
   */
  public StickerMessage(String messageId, String senderId, String receiverId, String stickerId,
      long timestamp) {
    this.messageId = messageId;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.stickerId = stickerId;
    this.timestamp = timestamp;
  }

  /**
   * Getter for messageId.
   * @return the unique identifier of the message.
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Getter for senderId.
   * @return the ID of the user who sent the message.
   */
  public String getSenderId() {
    return senderId;
  }

  /**
   * Getter for receiverId.
   * @return the ID of the user who received the message.
   */
  public String getReceiverId() {
    return receiverId;
  }

  /**
   * Getter for stickerId.
   * @return the ID of the sticker being sent.
   */
  public String getStickerId() {
    return stickerId;
  }

  /**
   * Getter for timestamp.
   * @return the timestamp of when the message was sent.
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Setter for messageId.
   * @param messageId the unique identifier of the message.
   */
  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  /**
   * Setter for senderId.
   * @param senderId the ID of the user who sent the message.
   */
  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  /**
   * Setter for receiverId.
   * @param receiverId the ID of the user who received the message.
   */
  public void setReceiverId(String receiverId) {
    this.receiverId = receiverId;
  }

  /**
   * Setter for stickerId.
   * @param stickerId the ID of the sticker being sent.
   */
  public void setStickerId(String stickerId) {
    this.stickerId = stickerId;
  }

  /**
   * Setter for timestamp.
   * @param timestamp the timestamp of when the message was sent.
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Returns a formatted timestamp string.
   * The format is "MMM dd, yyyy HH:mm" (e.g., "Jan 01, 2023 12:00").
   *
   * @return the formatted timestamp string.
   */
  public String getFormattedTimestamp() {
    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    return sdf.format(new Date(timestamp));
  }

  /**
   * Returns a human-readable time ago string.
   * @return a string representing how long ago the message was sent, e.g., "2 days ago", "1 hour ago", etc.
   */
  public String getTimeAgo() {
    long now = System.currentTimeMillis();
    long diff = now - timestamp;

    long seconds = diff / 1000;
    long minutes = seconds / 60;
    long hours = minutes / 60;
    long days = hours / 24;

    if (days > 0) {
      return days + " day" + (days == 1 ? "" : "s") + " ago";
    } else if (hours > 0) {
      return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
    } else if (minutes > 0) {
      return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
    } else {
      return "Just now";
    }
  }
}