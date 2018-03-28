package com.android.vish.bakenow.utilities;

import android.net.Uri;
import android.util.Log;

import com.android.vish.bakenow.models.Recipe;
import com.android.vish.bakenow.models.RecipeIngredient;
import com.android.vish.bakenow.models.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {

    public static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static URL buildGetRecipesUrl(String requestUrl) {
        Uri builtUri = Uri.parse(requestUrl)
                .buildUpon()
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(NetworkUtils.class.getSimpleName(), e.getMessage());
        }
        return url;
    }

    /**
     * Get the json data of all recipes
     *
     * @param url The url to get the recipes json
     * @return
     * @throws IOException
     */
    public static String getRecipesJson(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static List<Recipe> parseRecipesJson(String recipesJson) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray recipesArray = new JSONArray(recipesJson);
            for (int i = 0; i < recipesArray.length(); i++) {
                Recipe recipe = new Recipe();
                recipe.setId(recipesArray.getJSONObject(i).getInt("id"));
                recipe.setName(recipesArray.getJSONObject(i).getString("name"));
                recipe.setIngredients(parseIngredients(recipesArray.getJSONObject(i).getJSONArray("ingredients")));
                recipe.setSteps(parseSteps(recipesArray.getJSONObject(i).getJSONArray("steps")));
                recipe.setServings(recipesArray.getJSONObject(i).getInt("servings"));
                recipe.setImage(recipesArray.getJSONObject(i).getString("image"));
                recipes.add(recipe);
            }

        } catch (JSONException exception) {
            Log.e(NetworkUtils.class.getSimpleName(), exception.getMessage());
        }
        return recipes;
    }

    public static List<RecipeIngredient> parseIngredients(JSONArray ingredientsJson) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        try {
            for (int i = 0; i < ingredientsJson.length(); i++) {
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setQuantity(ingredientsJson.getJSONObject(i).getInt("quantity"));
                recipeIngredient.setMeasure(ingredientsJson.getJSONObject(i).getString("measure"));
                recipeIngredient.setIngredient(ingredientsJson.getJSONObject(i).getString("ingredient"));
                ingredients.add(recipeIngredient);
            }

        } catch (JSONException exception) {
            Log.e(NetworkUtils.class.getSimpleName(), exception.getMessage());
        }
        return ingredients;
    }

    public static List<RecipeStep> parseSteps(JSONArray stepsJson) {
        List<RecipeStep> steps = new ArrayList<>();
        try {
            for (int i = 0; i < stepsJson.length(); i++) {
                RecipeStep recipeStep = new RecipeStep();
                recipeStep.setId(stepsJson.getJSONObject(i).getInt("id"));
                recipeStep.setShortDescription(stepsJson.getJSONObject(i).getString("shortDescription"));
                recipeStep.setDescription(stepsJson.getJSONObject(i).getString("description"));
                recipeStep.setVideoUrl(stepsJson.getJSONObject(i).getString("videoURL"));
                recipeStep.setThumbnailUrl(stepsJson.getJSONObject(i).getString("thumbnailURL"));
                steps.add(recipeStep);
            }

        } catch (JSONException exception) {
            Log.e(NetworkUtils.class.getSimpleName(), exception.getMessage());
        }
        return steps;
    }
}
