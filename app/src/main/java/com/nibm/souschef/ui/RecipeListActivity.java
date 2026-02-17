package com.nibm.souschef.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.nibm.souschef.MainActivity;
import com.nibm.souschef.R;
import com.nibm.souschef.model.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<RecipeData> recipeList =
            new ArrayList<>();

    ArrayList<String> titleList =
            new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_list);

        listView =
                findViewById(R.id.listView);

        loadRecipes();

        ArrayAdapter adapter =
                new ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        titleList);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    RecipeData data =
                            recipeList.get(position);

                    RecipeRepository.selectedRecipe =
                            data;

                    startActivity(
                            new Intent(
                                    this,
                                    MainActivity.class));

                });

    }



    private void loadRecipes() {

        try {

            FileInputStream fis =
                    openFileInput("recipes.json");

            byte[] buffer =
                    new byte[fis.available()];

            fis.read(buffer);

            fis.close();


            JSONArray array =
                    new JSONArray(
                            new String(buffer));


            for(int i=0;i<array.length();i++)
            {

                JSONObject obj =
                        array.getJSONObject(i);

                RecipeData data =
                        new RecipeData(

                                obj.getString("title"),

                                obj.getString("recipe"),

                                obj.getDouble("multiplier"),

                                obj.getBoolean("metric")

                        );


                recipeList.add(data);

                titleList.add(data.title);

            }

        }

        catch(Exception e){

            Toast.makeText(this,
                    "No recipes found",
                    Toast.LENGTH_SHORT).show();

        }

    }

}
