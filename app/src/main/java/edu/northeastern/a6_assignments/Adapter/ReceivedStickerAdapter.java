package edu.northeastern.a6_assignments.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.a6_assignments.R;
import edu.northeastern.a6_assignments.pojo.StickerMessage;

public class ReceivedStickerAdapter extends RecyclerView.Adapter<ReceivedStickerAdapter.ViewHolder> {

    private final Context context;
    private final List<StickerMessage> stickerMessages;
    private Map<String, Integer> stickerImageMap;
    private Map<String, String> stickerNameMap;

    public ReceivedStickerAdapter(Context context, List<StickerMessage> stickerMessages) {
        this.context = context;
        this.stickerMessages = stickerMessages;
        initializeStickerMaps();
    }

    private void initializeStickerMaps() {
        // Initialize sticker image mapping
        stickerImageMap = new HashMap<>();
        stickerImageMap.put("sticker1", R.drawable.sticker1);
        stickerImageMap.put("sticker2", R.drawable.sticker2);
        stickerImageMap.put("sticker3", R.drawable.sticker3);
        stickerImageMap.put("sticker4", R.drawable.sticker4);
        stickerImageMap.put("sticker5", R.drawable.sticker5);
        stickerImageMap.put("sticker6", R.drawable.sticker6);

        // Initialize sticker name mapping
        stickerNameMap = new HashMap<>();
        stickerNameMap.put("sticker1", "My Sticker 1");
        stickerNameMap.put("sticker2", "My Sticker 2");
        stickerNameMap.put("sticker3", "My Sticker 3");
        stickerNameMap.put("sticker4", "My Sticker 4");
        stickerNameMap.put("sticker5", "My Sticker 5");
        stickerNameMap.put("sticker6", "My Sticker 6");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_received_sticker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StickerMessage message = stickerMessages.get(position);

        // Set sender name
        holder.textViewSenderName.setText("From: " + message.getSenderId());

        // Set sticker image
        Integer stickerResource = stickerImageMap.get(message.getStickerId());
        if (stickerResource != null) {
            holder.imageViewSticker.setImageResource(stickerResource);
        } else {
            holder.imageViewSticker.setImageResource(R.drawable.sticker1); // Default sticker
        }

        // Set sticker name (optional)
        String stickerName = stickerNameMap.get(message.getStickerId());
        if (stickerName != null) {
            holder.textViewStickerName.setText(stickerName);
            holder.textViewStickerName.setVisibility(View.VISIBLE);
        } else {
            holder.textViewStickerName.setVisibility(View.GONE);
        }

        // Set timestamp
        holder.textViewTimestamp.setText(message.getTimeAgo());

        // Set detailed timestamp as content description for accessibility
        holder.itemView.setContentDescription("Sticker from " + message.getSenderId() +
                " received " + message.getFormattedTimestamp());
    }

    @Override
    public int getItemCount() {
        return stickerMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSenderName;
        ImageView imageViewSticker;
        TextView textViewStickerName;
        TextView textViewTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSenderName = itemView.findViewById(R.id.textViewSenderName);
            imageViewSticker = itemView.findViewById(R.id.imageViewSticker);
            textViewStickerName = itemView.findViewById(R.id.textViewStickerName);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}