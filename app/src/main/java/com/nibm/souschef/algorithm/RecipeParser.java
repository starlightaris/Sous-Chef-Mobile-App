package com.nibm.souschef.algorithm;

import com.nibm.souschef.model.RecipeDLL;

public class RecipeParser {
    public static RecipeDLL parseRecipe(String rawText) {

        RecipeDLL dll = new RecipeDLL();

        if (rawText == null || rawText.isEmpty())
            return dll;

        // split by line
        String[] steps = rawText.split("\\r?\\n");

        for (String step : steps) {

            step = step.trim();

            if (!step.isEmpty()) {
                dll.insertNode(step);
            }
        }

        return dll;
    }
}
