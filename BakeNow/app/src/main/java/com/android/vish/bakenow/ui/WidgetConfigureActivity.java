package com.android.vish.bakenow.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.RecipeIngredientsWidget;
import com.android.vish.bakenow.adapters.RecipeListAdapter;
import com.android.vish.bakenow.models.Recipe;
import com.android.vish.bakenow.utilities.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class WidgetConfigureActivity extends AppCompatActivity implements RecipeListAdapter.RecipeItemListener {

    @BindView(R.id.activity_widget_configure_recipe_list) protected RecyclerView mRecipesRecyclerView;

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

        mRecipeListAdapter = new RecipeListAdapter(this, mRecipes, this);
        mRecipesRecyclerView.setAdapter(mRecipeListAdapter);
        mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecipesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        new GetRecipesTask().execute(NetworkUtils.buildGetRecipesUrl(NetworkUtils.RECIPES_URL));
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString(Integer.toString(mAppWidgetId), new Gson().toJson(recipe))
                .apply();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_recipe);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
        Intent intent = new Intent(this, RecipeIngredientsWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{mAppWidgetId});
        sendBroadcast(intent);
        setResult(RESULT_OK, intent);
        finish();
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
                Snackbar.make(mRecipesRecyclerView, getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

}
