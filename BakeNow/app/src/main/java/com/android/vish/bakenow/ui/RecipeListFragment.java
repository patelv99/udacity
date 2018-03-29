package com.android.vish.bakenow.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
import butterknife.OnItemClick;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.fragment_recipe_list_grid_view) protected GridView mRecipesGridView;

    private ProgressDialog    mProgressDialog;
    private RecipeListAdapter mRecipeListAdapter;
    private List<Recipe> mRecipes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(RecipeListFragment.this, view);
        mRecipeListAdapter = new RecipeListAdapter(getActivity(), mRecipes);
        mRecipesGridView.setAdapter(mRecipeListAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetRecipesTask().execute(NetworkUtils.buildGetRecipesUrl(NetworkUtils.RECIPES_URL));
    }

    @OnItemClick(R.id.fragment_recipe_list_grid_view)
    public void onRecipeClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RecipeDetailFragment.RECIPE_KEY, mRecipes.get(position));
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_recipe_fragment_container, recipeDetailFragment, recipeDetailFragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
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
                Snackbar.make(mRecipesGridView, getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
