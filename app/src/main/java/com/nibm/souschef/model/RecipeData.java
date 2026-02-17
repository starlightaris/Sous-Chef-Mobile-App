package com.nibm.souschef.model;

public class RecipeData {
    public String title;
    public String recipe;
    public double multiplier;
    public boolean metricToggle;

    public RecipeData(String title, String rawText,
                      double multiplier, boolean metricToggle) {

        this.title = title;
        this.rawText = rawText;
        this.multiplier = multiplier;
        this.metricToggle = metricToggle;
    }
}
