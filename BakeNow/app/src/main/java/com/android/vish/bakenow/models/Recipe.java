package com.android.vish.bakenow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {

    @SerializedName("id") private   int    mId;
    @SerializedName("name") private String mName;
    @SerializedName("ingredients") private List<RecipeIngredient> mIngredients = new ArrayList<>();
    @SerializedName("steps") private       List<RecipeStep>       mSteps       = new ArrayList<>();
    @SerializedName("servings") private int    mServings;
    @SerializedName("image") private    String mImage;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public List<RecipeIngredient> getIngredients() {
        return mIngredients;
    }

    public List<RecipeStep> getSteps() {
        return mSteps;
    }

    public int getServings() {
        return mServings;
    }

    public String getImage() {
        return mImage;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.mIngredients = ingredients;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.mSteps = steps;
    }

    public void setServings(int servings) {
        this.mServings = servings;
    }

    public void setImage(String image) {
        this.mImage = image;
    }
}