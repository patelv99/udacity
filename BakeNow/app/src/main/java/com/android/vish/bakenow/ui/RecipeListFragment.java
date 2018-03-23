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

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.adapters.RecipeListAdapter;
import com.android.vish.bakenow.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.fragment_recipe_list_recycler_view) protected RecyclerView mRecipeRecyclerView;

    private RecipeListAdapter mRecipeListAdapter;
    private List<Recipe> mRecipes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(RecipeListFragment.this, view);
        mRecipeListAdapter = new RecipeListAdapter(getActivity(), mRecipes);
        mRecipeRecyclerView.setAdapter(mRecipeListAdapter);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecipeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }
}
