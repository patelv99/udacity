package com.android.vish.bakenow.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.android.vish.bakenow.models.RecipeStep;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    public static String RECIPE_KEY = "recipeKey";

    @BindView(R.id.fragment_recipe_detail_ingredients) protected TextView     mIngredientsText;
    @BindView(R.id.fragment_recipe_detail_steps_list) protected  RecyclerView mRecipeStepsView;

    private RecipeStepsAdapter mRecipeStepsAdapter;
    private Recipe             mRecipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);
        mRecipeStepsAdapter = new RecipeStepsAdapter(getActivity(), new ArrayList<RecipeStep>());
        mRecipeStepsView.setAdapter(mRecipeStepsAdapter);
        mRecipeStepsView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecipeStepsView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        if (getArguments() != null) {
            mRecipe = (Recipe) getArguments().getSerializable(RECIPE_KEY);
            mRecipeStepsAdapter.updateSteps(mRecipe.getSteps());
        }

        return view;
    }

}
