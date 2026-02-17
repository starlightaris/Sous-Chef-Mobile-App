package com.nibm.souschef.algorithm;

import com.nibm.souschef.model.RecipeDLL;

public class RecipeParser {
    public static RecipeDLL parseRecipe(String rawText) {

        int time = 0; // in seconds
        RecipeDLL dll = new RecipeDLL();

        if (rawText == null || rawText.isEmpty())
            return dll;

        // split by line
        String[] steps = rawText.split("\\r?\\n");

        for (String step : steps) {

            step = step.trim();

            if (!step.isEmpty()) {
                dll.insertNode(step, time);
            }
        }

        return dll;
    }
}
