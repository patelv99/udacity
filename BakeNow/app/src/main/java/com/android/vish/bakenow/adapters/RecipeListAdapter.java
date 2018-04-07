package com.android.vish.bakenow.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends BaseAdapter {

    private Context      mContext;
    private List<Recipe> mRecipes;

    public RecipeListAdapter(Context context, List<Recipe> recipes) {
        mContext = context;
        mRecipes = new ArrayList<>(recipes);
    }

    @Override
    public int getCount() {
        return mRecipes.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mRecipes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        }
        TextView recipeName = convertView.findViewById(R.id.item_recipe_title);
        ImageView recipeImage = convertView.findViewById(R.id.item_recipe_image);
        recipeName.setText(mRecipes.get(position).getName());
        if (!TextUtils.isEmpty(mRecipes.get(position).getImage())) {
            Picasso.get().load(mRecipes.get(position).getImage()).into(recipeImage);
        }
        return convertView;
    }

    public void updateRecipes(List<Recipe> recipes) {
        mRecipes = new ArrayList<>(recipes);
        notifyDataSetChanged();
    }
}
