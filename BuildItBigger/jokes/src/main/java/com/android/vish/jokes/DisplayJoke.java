package com.android.vish.jokes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DisplayJoke extends AppCompatActivity {

    public static final String JOKE_KEY = "joke_key";

    private TextView mJokeText;

    private String mJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);
        mJokeText = findViewById(R.id.display_joke_textview);
        if (getIntent().getExtras() != null) {
            mJoke = getIntent().getStringExtra(JOKE_KEY);
            mJokeText.setText(mJoke);
        }
    }


}
