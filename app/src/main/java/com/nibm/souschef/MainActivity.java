package com.nibm.souschef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.nibm.souschef.algorithm.ConversionHelper;
import com.nibm.souschef.algorithm.RecipeParser;
import com.nibm.souschef.algorithm.ScalingHelper;
import com.nibm.souschef.model.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText etTitle, etRecipe, etMultiplier;
    Switch switchMetric;
    Button btnStart, btnSave, btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.etTitle);
        etRecipe = findViewById(R.id.etRecipe);
        etMultiplier = findViewById(R.id.etMultiplier);
        switchMetric = findViewById(R.id.switchMetric);

        btnStart = findViewById(R.id.btnStart);
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);

        btnStart.setOnClickListener(v -> startCooking());

        btnSave.setOnClickListener(v -> saveRecipe());

        // LOAD BUTTON OPENS RECIPE LIST SCREEN
        btnLoad.setOnClickListener(v -> {

            Intent intent =
                    new Intent(MainActivity.this,
                            com.nibm.souschef.ui.RecipeListActivity.class);

            startActivity(intent);

        });

    }

    // START COOKING
    private void startCooking() {

        String rawText = etRecipe.getText().toString();

        if (rawText.isEmpty()) {

            Toast.makeText(this,
                    "Recipe cannot be empty",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        double multiplier = 1.0;

        try {

            if (!etMultiplier.getText().toString().isEmpty()) {

                multiplier =
                        Double.parseDouble(
                                etMultiplier.getText().toString());

            }

        }

        catch (Exception e){

            multiplier = 1.0;

        }

        boolean metric = switchMetric.isChecked();

        RecipeDLL dll =
                RecipeParser.parseRecipe(rawText);

        StepNode node = dll.getHead();

        while (node != null) {

            node.instruction =
                    ScalingHelper.scaleStep(
                            node.instruction,
                            multiplier);

            if (metric) {

                node.instruction =
                        ConversionHelper.convertGramsToOunces(
                                node.instruction);

            }

            node = node.next;

        }

        RecipeRepository.recipeDLL = dll;

        startActivity(
                new Intent(this,
                        com.nibm.souschef.ui.NavigatorActivity.class));

    }

    // SAVE RECIPE TO JSON FILE
    private void saveRecipe() {

        try {

            String title =
                    etTitle.getText().toString();

            String recipe =
                    etRecipe.getText().toString();

            double multiplier = 1.0;

            if (!etMultiplier.getText().toString().isEmpty()) {

                multiplier =
                        Double.parseDouble(
                                etMultiplier.getText().toString());

            }

            boolean metric =
                    switchMetric.isChecked();

            JSONObject newRecipe =
                    new JSONObject();

            newRecipe.put("title", title);
            newRecipe.put("recipe", recipe);
            newRecipe.put("multiplier", multiplier);
            newRecipe.put("metric", metric);


            JSONArray array;

            try {

                FileInputStream fis =
                        openFileInput("recipes.json");

                byte[] buffer =
                        new byte[fis.available()];

                fis.read(buffer);

                fis.close();

                array =
                        new JSONArray(
                                new String(buffer));

            }

            catch (Exception e) {

                array =
                        new JSONArray();

            }

            array.put(newRecipe);

            FileOutputStream fos =
                    openFileOutput(
                            "recipes.json",
                            MODE_PRIVATE);

            fos.write(
                    array.toString().getBytes());

            fos.close();


            Toast.makeText(this,
                    "Recipe Saved",
                    Toast.LENGTH_SHORT).show();

        }

        catch (Exception e){

            e.printStackTrace();

        }

    }

}
