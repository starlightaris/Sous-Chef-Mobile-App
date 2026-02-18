package com.nibm.souschef.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.nibm.souschef.MainActivity;
import com.nibm.souschef.R;
import com.nibm.souschef.model.RecipeData;
import com.nibm.souschef.model.RecipeRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<RecipeData> recipeList = new ArrayList<>();

    ArrayList<String> titleList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_list);

        listView = findViewById(R.id.listView);

        loadRecipes();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.recipe_row,
                R.id.txtTitle,
                titleList
        ) {
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {

                android.view.View view = super.getView(position, convertView, parent);

                Button btnDelete = view.findViewById(R.id.btnDelete);

                btnDelete.setOnClickListener(v -> {
                    deleteRecipe(position);
                    notifyDataSetChanged();
                });

                return view;
            }
        };

        listView.setAdapter(adapter);


        listView.setOnItemClickListener((parent, view, position, id) -> {

            RecipeRepository.selectedRecipe = recipeList.get(position);

            startActivity(
                    new Intent(this, MainActivity.class));

        });

    }

    private void loadRecipes() {

        try {

            FileInputStream fis = openFileInput("recipes.json");
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            JSONArray array = new JSONArray(new String(buffer));

            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);

                String title = obj.getString("title");
                String recipe = obj.getString("recipe");

                double multiplier = 1.0;

                try {
                    multiplier = obj.getDouble("multiplier");
                }
                catch(Exception e){ }

                boolean metric = obj.getBoolean("metric");

                RecipeData data = new RecipeData(
                                title,
                                recipe,
                                multiplier,
                                metric);
                recipeList.add(data);
                titleList.add(title);
            }
        }
        catch(Exception e){
            Toast.makeText(this, "Error loading",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteRecipe(int position) {

        try {
            recipeList.remove(position);
            titleList.remove(position);

            JSONArray newArray = new JSONArray();

            for (RecipeData data : recipeList) {

                JSONObject obj = new JSONObject();
                obj.put("title", data.title);
                obj.put("recipe", data.recipe);
                obj.put("multiplier", data.multiplier);
                obj.put("metric", data.metric);

                newArray.put(obj);
            }

            java.io.FileOutputStream fos = openFileOutput("recipes.json", MODE_PRIVATE);
            fos.write(newArray.toString().getBytes());
            fos.close();

            Toast.makeText(this, "Recipe Deleted", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
