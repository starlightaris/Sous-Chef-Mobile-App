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

    EditText etTitle, etRecipe;

    ToggleButton btn1x, btn2x, btn3x;
    ToggleButton btnUK, btnUS;

    Button btnStart, btnSave, btnLoad;

    // Default values
    double multiplier = 1.0;
    boolean isUS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Text fields
        etTitle = findViewById(R.id.etTitle);
        etRecipe = findViewById(R.id.etRecipe);

        // Multiplier buttons
        btn1x = findViewById(R.id.btn1x);
        btn2x = findViewById(R.id.btn2x);
        btn3x = findViewById(R.id.btn3x);

        // Unit buttons
        btnUK = findViewById(R.id.btnUK);
        btnUS = findViewById(R.id.btnUS);

        // Action buttons
        btnStart = findViewById(R.id.btnStart);
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);

        // Default selections
        btn1x.setChecked(true);
        btnUK.setChecked(true);

        setupMultiplierButtons();
        setupUnitButtons();

        btnStart.setOnClickListener(v -> startCooking());

        btnSave.setOnClickListener(v -> saveRecipe());

        btnLoad.setOnClickListener(v -> {

            Intent intent =
                    new Intent(MainActivity.this,
                            com.nibm.souschef.ui.RecipeListActivity.class);

            startActivity(intent);
        });

        // Load selected recipe if exists
        if (RecipeRepository.selectedRecipe != null) {

            RecipeData data = RecipeRepository.selectedRecipe;

            etTitle.setText(data.title);
            etRecipe.setText(data.recipe);

            multiplier = data.multiplier;
            isUS = data.metric;

            updateMultiplierUI();
            updateUnitUI();
        }
    }

    // MULTIPLIER BUTTON LOGIC
    private void setupMultiplierButtons() {

        btn1x.setOnClickListener(v -> {
            multiplier = 1.0;
            updateMultiplierUI();
        });

        btn2x.setOnClickListener(v -> {
            multiplier = 2.0;
            updateMultiplierUI();
        });

        btn3x.setOnClickListener(v -> {
            multiplier = 3.0;
            updateMultiplierUI();
        });
    }

    private void updateMultiplierUI() {

        btn1x.setChecked(multiplier == 1.0);
        btn2x.setChecked(multiplier == 2.0);
        btn3x.setChecked(multiplier == 3.0);
    }

    // UNIT BUTTON LOGIC
    private void setupUnitButtons() {

        btnUK.setOnClickListener(v -> {
            isUS = false;
            updateUnitUI();
        });

        btnUS.setOnClickListener(v -> {
            isUS = true;
            updateUnitUI();
        });
    }

    private void updateUnitUI() {

        btnUK.setChecked(!isUS);
        btnUS.setChecked(isUS);
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

        RecipeDLL dll =
                RecipeParser.parseRecipe(rawText);

        StepNode node = dll.getHead();

        while (node != null) {

            // Apply scaling
            node.instruction =
                    ScalingHelper.scaleStep(
                            node.instruction,
                            multiplier);

            // Apply conversion ONLY if US selected
            if (isUS) {

                node.instruction =
                        ConversionHelper.convertMetricToUS(
                                node.instruction);
            }

            node = node.next;
        }

        RecipeRepository.recipeDLL = dll;

        startActivity(
                new Intent(this,
                        com.nibm.souschef.ui.NavigatorActivity.class));
    }

    // SAVE RECIPE
    private void saveRecipe() {

        try {

            String title = etTitle.getText().toString();
            String recipe = etRecipe.getText().toString();

            if(title.isEmpty() || recipe.isEmpty())
            {
                Toast.makeText(this,
                        "Title and Recipe required",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject newRecipe = new JSONObject();

            newRecipe.put("title", title);
            newRecipe.put("recipe", recipe);
            newRecipe.put("multiplier", multiplier);
            newRecipe.put("metric", isUS);

            JSONArray array;

            try {

                FileInputStream fis =
                        openFileInput("recipes.json");

                byte[] buffer =
                        new byte[fis.available()];

                fis.read(buffer);
                fis.close();

                String json =
                        new String(buffer);

                if(json.trim().isEmpty())
                    array = new JSONArray();
                else
                    array = new JSONArray(json);
            }

            catch (Exception e) {

                array = new JSONArray();
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

            Toast.makeText(this,
                    "Save failed",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
