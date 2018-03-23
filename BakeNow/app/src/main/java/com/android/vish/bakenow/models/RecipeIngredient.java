package com.android.vish.bakenow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeIngredient implements Serializable {

    @SerializedName("quantity") private   int    mQuantity;
    @SerializedName("measure") private    String mMeasure;
    @SerializedName("ingredient") private String mIngredient;

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredient() {
        return mIngredient;
    }
}
