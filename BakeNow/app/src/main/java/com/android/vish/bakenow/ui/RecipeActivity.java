package com.android.vish.bakenow.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.models.Recipe;

import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        Recipe recipe = (Recipe) getIntent().getSerializableExtra(RecipeDetailFragment.RECIPE_KEY);
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        if (recipe != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(RecipeDetailFragment.RECIPE_KEY, recipe);
            recipeDetailFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_recipe_fragment_container_one, recipeDetailFragment, recipeDetailFragment.getClass().getSimpleName())
                .commit();
        mTwoPane = findViewById(R.id.activity_recipe_fragment_container_two) != null;
        if (mTwoPane) {
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(RecipeDetailFragment.RECIPE_KEY, recipe);
            recipeStepFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.activity_recipe_fragment_container_two, recipeStepFragment, recipeStepFragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
