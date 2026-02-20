package com.nibm.souschef.model;

public class RecipeData {
    public String title;
    public String recipe;
    public double multiplier;
    public boolean metric;

    public RecipeData(String title, String recipe,
                      double multiplier, boolean metric) {

        this.title = title;
        this.recipe = recipe;
        this.multiplier = multiplier;
        this.metric = metric;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public boolean isMetric() {
        return metric;
    }

    public void setMetric(boolean metric) {
        this.metric = metric;
    }
}
