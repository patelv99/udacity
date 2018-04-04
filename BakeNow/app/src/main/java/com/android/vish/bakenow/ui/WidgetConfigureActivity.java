package com.android.vish.bakenow.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.GridView;
import android.widget.RemoteViews;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.adapters.RecipeListAdapter;
import com.android.vish.bakenow.models.Recipe;
import com.android.vish.bakenow.models.RecipeIngredient;
import com.android.vish.bakenow.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class WidgetConfigureActivity extends AppCompatActivity {

    @BindView(R.id.activity_widget_configure_recipe_list) protected GridView mRecipesGridView;

    private RecipeListAdapter mRecipeListAdapter;
    private List<Recipe> mRecipes = new ArrayList<>();
    private int mAppWidgetId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            mAppWidgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.activity_widget_configure);
        ButterKnife.bind(this);

        mRecipeListAdapter = new RecipeListAdapter(this, mRecipes);
        mRecipesGridView.setAdapter(mRecipeListAdapter);
        new GetRecipesTask().execute(NetworkUtils.buildGetRecipesUrl(NetworkUtils.RECIPES_URL));
    }


    @OnItemClick(R.id.activity_widget_configure_recipe_list)
    public void onRecipeClick(int position) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_recipe);
        views.setTextViewText(R.id.widget_recipe_title, mRecipes.get(position).getName());
        views.setTextViewText(R.id.widget_recipe_ingredients, createIngredientsText(mRecipes.get(position)));
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, intent);
        finish();
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

    public class GetRecipesTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL recipesResponseUrl = params[0];
            String recipesResponse = null;
            try {
                recipesResponse = NetworkUtils.getRecipesJson(recipesResponseUrl);
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
            return recipesResponse;
        }

        @Override
        protected void onPostExecute(String recipesResult) {
            if (!TextUtils.isEmpty(recipesResult)) {
                mRecipes = new ArrayList<>(NetworkUtils.parseRecipesJson(recipesResult));
                mRecipeListAdapter.updateRecipes(mRecipes);
            } else {
                Snackbar.make(mRecipesGridView, getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

}
