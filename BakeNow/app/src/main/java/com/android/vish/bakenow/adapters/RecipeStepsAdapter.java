package com.android.vish.bakenow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.models.RecipeStep;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    private Context                mContext;
    private List<RecipeStep>       mSteps;
    private RecipeStepItemListener mRecipeStepItemListener;

    public RecipeStepsAdapter(Context context, List<RecipeStep> steps, RecipeStepItemListener recipeStepItemListener) {
        mContext = context;
        mSteps = new ArrayList<>(steps);
        mRecipeStepItemListener = recipeStepItemListener;
    }

    public interface RecipeStepItemListener {

        void onStepClick(RecipeStep recipeStep);
    }

    public void updateSteps(List<RecipeStep> steps) {
        mSteps = new ArrayList<>(steps);
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
        holder.mRecipeStep.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    protected class RecipeStepsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_recipe_step_title) protected TextView mRecipeStep;

        public RecipeStepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_recipe_step_card)
        public void onRecipeStepClick() {
            mRecipeStepItemListener.onStepClick(mSteps.get(getAdapterPosition()));
        }
    }
}
