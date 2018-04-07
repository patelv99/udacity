package com.android.vish.bakenow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private Context            mContext;
    private List<Recipe>       mRecipes;
    private RecipeItemListener mRecipeItemListener;

    public RecipeListAdapter(Context context, List<Recipe> recipes, RecipeItemListener recipeItemListener) {
        mContext = context;
        mRecipes = new ArrayList<>(recipes);
        mRecipeItemListener = recipeItemListener;
    }

    public interface RecipeItemListener {

        void onRecipeClick(Recipe recipe);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.mRecipeTitle.setText(mRecipes.get(position).getName());
        if (!TextUtils.isEmpty(mRecipes.get(position).getImage())) {
            Picasso.get().load(mRecipes.get(position).getImage()).into(holder.mRecipeImage);
        }
    }

    @Override
    public long getItemId(int position) {
        return mRecipes.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void updateRecipes(List<Recipe> recipes) {
        mRecipes = new ArrayList<>(recipes);
        notifyDataSetChanged();
    }


    protected class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_recipe_title) protected TextView  mRecipeTitle;
        @BindView(R.id.item_recipe_image) protected ImageView mRecipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_recipe_card)
        public void onRecipeStepClick() {
            mRecipeItemListener.onRecipeClick(mRecipes.get(getAdapterPosition()));
        }
    }
}
