package com.android.vish.bakenow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeIngredient implements Serializable {

    @SerializedName("quantity") private   double    mQuantity;
    @SerializedName("measure") private    String mMeasure;
    @SerializedName("ingredient") private String mIngredient;

    public double getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }

    public void setMeasure(String measure) {
        this.mMeasure = measure;
    }

    public void setIngredient(String ingredient) {
        this.mIngredient = ingredient;
    }
}
