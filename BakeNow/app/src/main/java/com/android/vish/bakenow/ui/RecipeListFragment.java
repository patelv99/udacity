package com.android.vish.bakenow.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.adapters.RecipeListAdapter;
import com.android.vish.bakenow.models.Recipe;
import com.android.vish.bakenow.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment implements RecipeListAdapter.RecipeItemListener {

    @BindView(R.id.fragment_recipe_list_recycler_view) protected RecyclerView mRecipeRecyclerView;

    private ProgressDialog    mProgressDialog;
    private RecipeListAdapter mRecipeListAdapter;
    private List<Recipe> mRecipes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(RecipeListFragment.this, view);
        mRecipeListAdapter = new RecipeListAdapter(getActivity(), mRecipes, this);
        mRecipeRecyclerView.setAdapter(mRecipeListAdapter);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecipeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetRecipesTask().execute(NetworkUtils.buildGetRecipesUrl(NetworkUtils.RECIPES_URL));
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(getActivity(), RecipeActivity.class);
        intent.putExtra(RecipeDetailFragment.RECIPE_KEY, recipe);
        startActivity(intent);
    }

    @VisibleForTesting
    public ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }

    /**
     * Show the progress dialog spinner
     */
    protected void showProgressDialog() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(getLayoutInflater().inflate(R.layout.progress_spinner, null));
        }
    }

    /**
     * Dismiss the progress dialog spinner
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public class GetRecipesTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
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
            dismissProgressDialog();
            if (!TextUtils.isEmpty(recipesResult)) {
                mRecipes = new ArrayList<>(NetworkUtils.parseRecipesJson(recipesResult));
                mRecipeListAdapter.updateRecipes(mRecipes);
            } else {
                Snackbar.make(mRecipeRecyclerView, getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
