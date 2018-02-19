package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final  String EXTRA_POSITION   = "extra_position";
    private static final int    DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        setTitle(sandwich.getMainName());

        ImageView imageIv = findViewById(R.id.image_iv);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(imageIv);

        TextView alsoKnownAsTv = findViewById(R.id.also_known_tv);
        String alsoKnownAsString = "";
        for (String alsoKnownAs : sandwich.getAlsoKnownAs()) {
            alsoKnownAsString = alsoKnownAsString.concat(alsoKnownAs.concat("\n"));
        }
        if (alsoKnownAsString != "") {
            alsoKnownAsTv.setText(alsoKnownAsString.substring(0, alsoKnownAsString.lastIndexOf('\n')));
        }

        TextView originTv = findViewById(R.id.origin_tv);
        originTv.setText(sandwich.getPlaceOfOrigin());

        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        String ingredientsString = "";
        for (String ingredient : sandwich.getIngredients()) {
            ingredientsString = ingredientsString.concat(ingredient.concat("\n"));
        }
        if (ingredientsString != "") {
            ingredientsTv.setText(ingredientsString.substring(0, ingredientsString.lastIndexOf('\n')));
        }

        TextView descriptionTv = findViewById(R.id.description_tv);
        descriptionTv.setText(sandwich.getDescription());
    }
}
