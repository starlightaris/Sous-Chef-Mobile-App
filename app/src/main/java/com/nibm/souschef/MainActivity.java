package com.nibm.souschef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.nibm.souschef.algorithm.RecipeParser;
import com.nibm.souschef.algorithm.ScalingHelper;
import com.nibm.souschef.algorithm.ConversionHelper;
import com.nibm.souschef.model.*;

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
        btnLoad.setOnClickListener(v -> loadRecipe());
    }

    private void startCooking() {

        String title = etTitle.getText().toString();
        String rawText = etRecipe.getText().toString();

        if (rawText.isEmpty()) {
            Toast.makeText(this, "Recipe cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        double multiplier = 1.0;
        if (!etMultiplier.getText().toString().isEmpty()) {
            multiplier = Double.parseDouble(etMultiplier.getText().toString());
        }

        boolean metric = switchMetric.isChecked();

        RecipeDLL dll = RecipeParser.parseRecipe(rawText);

        StepNode node = dll.getHead();

        while (node != null) {

            node.instruction =
                    ScalingHelper.scaleStep(node.instruction, multiplier);

            if (metric) {
                node.instruction =
                        ConversionHelper.convertGramsToOunces(node.instruction);
            }

            node = node.next;
        }

        RecipeRepository.recipeDLL = dll;

        Intent intent = new Intent(MainActivity.this,
                com.nibm.souschef.ui.NavigatorActivity.class);

        startActivity(intent);
    }

    private void saveRecipe() {

        try {

            JSONObject json = new JSONObject();

            json.put("title", etTitle.getText().toString());
            json.put("recipe", etRecipe.getText().toString());
            json.put("multiplier", etMultiplier.getText().toString());
            json.put("metric", switchMetric.isChecked());

            FileOutputStream fos =
                    openFileOutput("recipe.json", MODE_PRIVATE);

            fos.write(json.toString().getBytes());
            fos.close();

            Toast.makeText(this, "Recipe Saved", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRecipe() {

        try {

            FileInputStream fis =
                    openFileInput("recipe.json");

            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            String jsonString = new String(buffer);

            JSONObject json = new JSONObject(jsonString);

            etTitle.setText(json.getString("title"));
            etRecipe.setText(json.getString("recipe"));
            etMultiplier.setText(json.getString("multiplier"));
            switchMetric.setChecked(json.getBoolean("metric"));

            Toast.makeText(this, "Recipe Loaded", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "No saved recipe found", Toast.LENGTH_SHORT).show();
        }
    }
}
