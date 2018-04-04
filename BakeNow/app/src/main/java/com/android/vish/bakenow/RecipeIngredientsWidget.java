package com.android.vish.bakenow;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.vish.bakenow.models.Recipe;
import com.android.vish.bakenow.models.RecipeIngredient;
import com.android.vish.bakenow.ui.RecipeActivity;
import com.android.vish.bakenow.ui.RecipeDetailFragment;
import com.google.gson.Gson;

public class RecipeIngredientsWidget extends AppWidgetProvider {
    
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, RecipeIngredientsWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            try {
                String recipeJson = PreferenceManager.getDefaultSharedPreferences(context).getString(Integer.toString(widgetId), "");
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
                Recipe recipe = new Recipe();
                if (!TextUtils.isEmpty(recipeJson)) {
                    recipe = new Gson().fromJson(recipeJson, Recipe.class);
                }
                Intent intent = new Intent(context, RecipeActivity.class);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                if (!TextUtils.isEmpty(recipe.getName())) {
                    views.setTextViewText(R.id.widget_recipe_title, recipe.getName());
                    views.setTextViewText(R.id.widget_recipe_ingredients, createIngredientsText(recipe));
                    intent.putExtra(RecipeDetailFragment.RECIPE_KEY, recipe);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setComponent(new ComponentName(context.getPackageName(), RecipeActivity.class.getName()));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                views.setOnClickPendingIntent(R.id.widget_recipe_ingredients, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, views);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context.getApplicationContext(), "There was a problem loading the application: ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * Display the ingredients in a list type
     */
    public String createIngredientsText(Recipe recipe) {
        StringBuilder ingredientsString = new StringBuilder();
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            ingredientsString.append(ingredient.getIngredient()).append(", ");
        }
        ingredientsString.replace(ingredientsString.lastIndexOf(", "), ingredientsString.length(), "");
        return ingredientsString.toString();
    }
}