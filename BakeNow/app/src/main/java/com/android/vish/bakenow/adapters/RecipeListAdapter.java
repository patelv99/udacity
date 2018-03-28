package com.android.vish.bakenow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeItemViewHolder> {

    private Context                mContext;
    private List<Recipe>           mRecipes;
    private RecipeListItemListener mRecipeListItemListener;

    public RecipeListAdapter(Context context, List<Recipe> recipes, RecipeListItemListener recipeListItemListener) {
        mContext = context;
        mRecipes = new ArrayList<>(recipes);
        mRecipeListItemListener = recipeListItemListener;
    }

    public interface RecipeListItemListener {

        void onRecipeClick(Recipe recipe);
    }

    public void updateRecipes(List<Recipe> recipes) {
        mRecipes = new ArrayList<>(recipes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_recipe, parent, false);
        return new RecipeItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemViewHolder holder, int position) {
        holder.mRecipeTitle.setText(mRecipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    protected class RecipeItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_recipe_title) protected TextView mRecipeTitle;

        public RecipeItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_recipe_card)
        public void onRecipeClick() {
            mRecipeListItemListener.onRecipeClick(mRecipes.get(getAdapterPosition()));
        }
    }
}
