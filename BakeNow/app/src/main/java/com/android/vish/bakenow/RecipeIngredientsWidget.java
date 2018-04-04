package com.android.vish.bakenow;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.vish.bakenow.ui.RecipeActivity;

public class RecipeIngredientsWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Get all ids
        ComponentName thisWidget = new ComponentName(context, RecipeIngredientsWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setComponent(new ComponentName(context.getPackageName(), RecipeActivity.class.getName()));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
                views.setOnClickPendingIntent(R.id.widget_recipe_ingredients, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, views);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context.getApplicationContext(), "There was a problem loading the application: ", Toast.LENGTH_SHORT).show();
            }

        }
    }
}