package com.android.vish.bakenow.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.vish.bakenow.R;

import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
    }
}
