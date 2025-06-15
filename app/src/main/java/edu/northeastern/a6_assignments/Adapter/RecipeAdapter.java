package edu.northeastern.a6_assignments.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a6_assignments.R;
import edu.northeastern.a6_assignments.pojo.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe, int position);
    }

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList != null ? recipeList : new ArrayList<>();
    }

    public void setOnRecipeClickListener(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void updateRecipes(List<Recipe> newRecipes) {
        this.recipeList.clear();
        if (newRecipes != null) {
            this.recipeList.addAll(newRecipes);
        }
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivRecipeImage;
        private TextView tvRecipeTitle;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRecipeImage = itemView.findViewById(R.id.iv_recipe_image);
            tvRecipeTitle = itemView.findViewById(R.id.tv_recipe_title);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onRecipeClick(recipeList.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        public void bind(Recipe recipe) {
            tvRecipeTitle.setText(recipe.getTitle());

            // Load image using Thread and Handler (Android API only)
            if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
                loadImageFromUrl(recipe.getImage(), ivRecipeImage);
            } else {
                ivRecipeImage.setImageResource(R.drawable.placeholder_image);
            }
        }

        private void loadImageFromUrl(String imageUrl, ImageView imageView) {
            // Use a background thread to load the image
            Thread thread = new Thread(() -> {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    // Switch back to main thread to update UI
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            imageView.setImageResource(R.drawable.placeholder_image);
                        }
                    });

                } catch (Exception e) {
                    Log.e("ImageLoader", "Error loading image", e);
                    // On error, set placeholder on main thread
                    new Handler(Looper.getMainLooper()).post(() ->
                            imageView.setImageResource(R.drawable.placeholder_image)
                    );
                }
            });
            thread.start();
        }
    }
}