package com.nibm.souschef;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nibm.souschef.algorithm.RecipeParser;
import com.nibm.souschef.model.RecipeDLL;
import com.nibm.souschef.model.StepNode;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText inputRecipe;
    Button btnParse;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputRecipe = findViewById(R.id.inputRecipe);
        btnParse = findViewById(R.id.btnParse);
        output = findViewById(R.id.output);

        btnParse.setOnClickListener(v -> {

            String rawText = inputRecipe.getText().toString();

            RecipeDLL dll = RecipeParser.parseRecipe(rawText);

            StepNode node = dll.getHead();

            String result = "";

            while (node != null) {
                result += node.instruction + "\n";
                node = node.next;
            }

            output.setText(result);

        });
    }
}