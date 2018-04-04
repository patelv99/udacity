package com.android.vish.bakenow.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.adapters.RecipeStepsAdapter;
import com.android.vish.bakenow.models.Recipe;
import com.android.vish.bakenow.models.RecipeIngredient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements RecipeStepsAdapter.RecipeStepItemListener {

    public static final String RECIPE_KEY = "recipeKey";

    @BindView(R.id.fragment_recipe_detail_servings) protected    TextView     mServings;
    @BindView(R.id.fragment_recipe_detail_ingredients) protected TextView     mIngredientsText;
    @BindView(R.id.fragment_recipe_detail_steps_list) protected  RecyclerView mRecipeStepsView;

    private RecipeStepsAdapter mRecipeStepsAdapter;
    private Recipe             mRecipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);
        mRecipeStepsAdapter = new RecipeStepsAdapter(getActivity(), new Recipe(), this);
        mRecipeStepsView.setAdapter(mRecipeStepsAdapter);
        mRecipeStepsView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecipeStepsView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecipeStepsView.setNestedScrollingEnabled(false);
        if (getArguments() != null) {
            mRecipe = (Recipe) getArguments().getSerializable(RECIPE_KEY);
            mServings.setText(getString(R.string.serves, mRecipe.getServings()));
            createIngredientsText();
            mRecipeStepsAdapter.updateRecipe(mRecipe);
        }
        return view;
    }

    @Override
    public void onStepClick(Recipe recipe, int position) {
        RecipeStepFragment fragment = (RecipeStepFragment) getFragmentManager().findFragmentByTag(RecipeStepFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.updateStepNumber(position);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(RECIPE_KEY, recipe);
            bundle.putInt(RecipeStepFragment.STEP_KEY, position);
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.activity_recipe_fragment_container_one, recipeStepFragment, recipeStepFragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Display the ingredients in a list type
     */
    public void createIngredientsText() {
        for (RecipeIngredient ingredient : mRecipe.getIngredients()) {
            mIngredientsText.append(Double.toString(ingredient.getQuantity()));
            mIngredientsText.append(" ");
            mIngredientsText.append(ingredient.getMeasure().toLowerCase());
            mIngredientsText.append(" ");
            mIngredientsText.append(ingredient.getIngredient());
            mIngredientsText.append("\n");
        }
    }
}
