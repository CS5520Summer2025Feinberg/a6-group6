package edu.northeastern.a6_assignments.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StickerMessage {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String stickerId;
    private long timestamp;

    public StickerMessage() {
        // Default constructor for Firebase
    }

    public StickerMessage(String messageId, String senderId, String receiverId, String stickerId, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.stickerId = stickerId;
        this.timestamp = timestamp;
    }

    // Getters
    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getStickerId() {
        return stickerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Helper method to get formatted timestamp
    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    // Helper method to get time ago format
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