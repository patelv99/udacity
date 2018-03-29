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
import com.android.vish.bakenow.models.RecipeStep;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment {

    public static final String STEP_KEY = "recipeStep";

    @BindView(R.id.fragment_recipe_step_description) protected TextView mStepDescription;

    private RecipeStep mRecipeStep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mRecipeStep = (RecipeStep) getArguments().getSerializable(STEP_KEY);
            mStepDescription.setText(mRecipeStep.getDescription());
        }
        return view;
    }
}
