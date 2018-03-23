package com.android.vish.bakenow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {

    @SerializedName("id") private          int                    mId;
    @SerializedName("name") private        String                 mName;
    @SerializedName("ingredients") private List<RecipeIngredient> mIngredients;
    @SerializedName("steps") private       List<RecipeStep>       mSteps;
    @SerializedName("servings") private    int                    mServings;
    @SerializedName("image") private       String                 mImage;

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
}