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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    private Context                mContext;
    private Recipe                 mRecipe;
    private RecipeStepItemListener mRecipeStepItemListener;

    public RecipeStepsAdapter(Context context, Recipe recipe, RecipeStepItemListener recipeStepItemListener) {
        mContext = context;
        mRecipe = recipe;
        mRecipeStepItemListener = recipeStepItemListener;
    }

    public interface RecipeStepItemListener {

        void onStepClick(Recipe recipe, int position);
    }

    public void updateRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_recipe_step, parent, false);
        return new RecipeStepsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        holder.mRecipeStep.setText(mRecipe.getSteps().get(position).getShortDescription());
        if (!TextUtils.isEmpty(mRecipe.getSteps().get(position).getThumbnailUrl())) {
            Picasso.get().load(mRecipe.getSteps().get(position).getThumbnailUrl()).into(holder.mStepImage);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size();
    }

    protected class RecipeStepsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_recipe_step_image) protected ImageView mStepImage;
        @BindView(R.id.item_recipe_step_title) protected TextView  mRecipeStep;

        public RecipeStepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_recipe_step_card)
        public void onRecipeStepClick() {
            mRecipeStepItemListener.onStepClick(mRecipe, getAdapterPosition());
        }
    }
}
