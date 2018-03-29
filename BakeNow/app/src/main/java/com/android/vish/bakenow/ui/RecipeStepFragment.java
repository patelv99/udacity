package com.android.vish.bakenow.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepFragment extends Fragment {

    public static final String STEP_KEY = "recipeStep";

    @BindView(R.id.fragment_recipe_step_description) protected TextView mStepDescription;

    private Recipe mRecipe;
    private int    mStepNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            mRecipe = (Recipe) getArguments().getSerializable(RecipeDetailFragment.RECIPE_KEY);
            mStepNumber = getArguments().getInt(STEP_KEY);
            mStepDescription.setText(mRecipe.getSteps().get(mStepNumber).getDescription());
        }
        return view;
    }

    @OnClick(R.id.fragment_recipe_step_previous_button)
    public void onPreviousClick() {
        if (mStepNumber > 0) {
            mStepNumber--;
        }
        mStepDescription.setText(mRecipe.getSteps().get(mStepNumber).getDescription());
    }

    @OnClick(R.id.fragment_recipe_step_next_button)
    public void onNextClick() {
        if (mStepNumber < mRecipe.getSteps().size() - 1) {
            mStepNumber++;
        }
        mStepDescription.setText(mRecipe.getSteps().get(mStepNumber).getDescription());
    }
}
