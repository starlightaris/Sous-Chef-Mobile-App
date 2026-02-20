package com.nibm.souschef.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.souschef.MainActivity;
import com.nibm.souschef.R;
import com.nibm.souschef.model.RecipeData;
import com.nibm.souschef.model.RecipeRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity
        implements RecipeAdapter.OnRecipeClickListener {

    RecyclerView recyclerView;
    RecipeAdapter adapter;

    ArrayList<RecipeData> recipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recyclerView = findViewById(R.id.recyclerView);

        loadRecipes();

        adapter = new RecipeAdapter(recipeList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        enableSwipeToDelete();
    }

    // Handle recipe click
    @Override
    public void onRecipeClick(int position) {
        RecipeRepository.selectedRecipe = recipeList.get(position);
        startActivity(new Intent(this, MainActivity.class));
    }

    // Load from JSON
    private void loadRecipes() {
        try {
            FileInputStream fis = openFileInput("recipes.json");
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            JSONArray array = new JSONArray(new String(buffer));

            recipeList.clear();

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String title = obj.getString("title");
                String recipe = obj.getString("recipe");

                double multiplier = obj.optDouble("multiplier", 1.0);
                boolean metric = obj.optBoolean("metric", true);

                RecipeData data = new RecipeData(
                        title,
                        recipe,
                        multiplier,
                        metric
                );

                recipeList.add(data);
            }

        } catch (Exception e) {
            Toast.makeText(this, "No saved recipes yet",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Swipe delete
    private void enableSwipeToDelete() {

        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {

                        int position = viewHolder.getAdapterPosition();

                        recipeList.remove(position);
                        adapter.notifyItemRemoved(position);
                        saveRecipesToFile();

                        Toast.makeText(RecipeListActivity.this,
                                "Recipe deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onChildDraw(android.graphics.Canvas c,
                                            RecyclerView recyclerView,
                                            RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY,
                                            int actionState,
                                            boolean isCurrentlyActive) {

                        View itemView = viewHolder.itemView;

                        android.graphics.Paint paint = new android.graphics.Paint();
                        paint.setColor(android.graphics.Color.parseColor("#F44336")); // red

                        // draw red background
                        c.drawRect(
                                itemView.getRight() + dX,
                                itemView.getTop(),
                                itemView.getRight(),
                                itemView.getBottom(),
                                paint
                        );

                        // draw trash icon
                        android.graphics.drawable.Drawable icon =
                                getResources().getDrawable(R.drawable.ic_delete);

                        int iconMargin =
                                (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;

                        int iconTop = itemView.getTop() + iconMargin;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();

                        int iconLeft =
                                itemView.getRight() - iconMargin - icon.getIntrinsicWidth();

                        int iconRight =
                                itemView.getRight() - iconMargin;

                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);

                        super.onChildDraw(c, recyclerView, viewHolder,
                                dX, dY, actionState, isCurrentlyActive);
                    }
                };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    // Save updated list back to JSON
    private void saveRecipesToFile() {
        try {
            JSONArray array = new JSONArray();

            for (RecipeData r : recipeList) {
                JSONObject obj = new JSONObject();
                obj.put("title", r.getTitle());
                obj.put("recipe", r.getRecipe());
                obj.put("multiplier", r.getMultiplier());
                obj.put("metric", r.isMetric());

                array.put(obj);
            }

            FileOutputStream fos = openFileOutput(
                    "recipes.json", MODE_PRIVATE);
            fos.write(array.toString().getBytes());
            fos.close();

        } catch (Exception e) {
            Toast.makeText(this,
                    "Error saving recipes",
                    Toast.LENGTH_SHORT).show();
        }
    }
}