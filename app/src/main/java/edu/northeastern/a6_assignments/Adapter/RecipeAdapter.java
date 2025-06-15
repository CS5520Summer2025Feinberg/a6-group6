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

/**
 * RecipeAdapter is a RecyclerView adapter that binds Recipe data to the RecyclerView. It handles
 * displaying recipe titles and images, and provides a click listener for recipe items.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

  // List of Recipe objects to be displayed in the RecyclerView
  private List<Recipe> recipeList;
  private Context context;
  private OnRecipeClickListener listener;

  /**
   * OnRecipeClickListener is an interface for handling click events on recipe items.
   */
  public interface OnRecipeClickListener {

    /**
     * Called when a recipe item is clicked.
     *
     * @param recipe   The clicked Recipe object.
     * @param position The position of the clicked item in the list.
     */
    void onRecipeClick(Recipe recipe, int position);
  }

  /**
   * Constructor for RecipeAdapter.
   *
   * @param context    The context in which the adapter is being used.
   * @param recipeList The initial list of recipes to display. If null, an empty list is created.
   */
  public RecipeAdapter(Context context, List<Recipe> recipeList) {
    this.context = context;
    this.recipeList = recipeList != null ? recipeList : new ArrayList<>();
  }

  /**
   * Sets the listener for recipe item click events.
   *
   * @param listener The listener to be set.
   */
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

  /**
   * Updates the list of recipes and notifies the adapter of the change.
   *
   * @param newRecipes The new list of recipes to display. If null, the current list is cleared.
   */
  public void updateRecipes(List<Recipe> newRecipes) {
    this.recipeList.clear();
    if (newRecipes != null) {
      this.recipeList.addAll(newRecipes);
    }
    notifyDataSetChanged();
  }

  /**
   * RecipeViewHolder is a ViewHolder for displaying individual recipe items in the RecyclerView.
   */
  public class RecipeViewHolder extends RecyclerView.ViewHolder {

    // Views for displaying recipe image and title
    private ImageView ivRecipeImage;
    private TextView tvRecipeTitle;

    /**
     * Constructor for RecipeViewHolder.
     *
     * @param itemView The view for the recipe item.
     */
    public RecipeViewHolder(@NonNull View itemView) {
      super(itemView);
      ivRecipeImage = itemView.findViewById(R.id.iv_recipe_image);
      tvRecipeTitle = itemView.findViewById(R.id.tv_recipe_title);

      // Set click listener for the recipe item
      itemView.setOnClickListener(v -> {
        if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
          listener.onRecipeClick(recipeList.get(getAdapterPosition()), getAdapterPosition());
        }
      });
    }

    /**
     * Binds a Recipe object to the views in this ViewHolder.
     *
     * @param recipe The Recipe object to bind.
     */
    public void bind(Recipe recipe) {
      tvRecipeTitle.setText(recipe.getTitle());

      // Load image using Thread and Handler
      if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
        loadImageFromUrl(recipe.getImage(), ivRecipeImage);
      } else {
        ivRecipeImage.setImageResource(R.drawable.placeholder_image);
      }
    }

    /**
     * Loads an image from a URL in a background thread and sets it to the ImageView.
     *
     * @param imageUrl  The URL of the image to load.
     * @param imageView The ImageView to set the loaded image.
     */
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