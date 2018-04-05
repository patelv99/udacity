package com.android.vish.bakenow;

import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.vish.bakenow.models.Recipe;
import com.android.vish.bakenow.ui.RecipeActivity;
import com.android.vish.bakenow.ui.RecipeDetailFragment;
import com.android.vish.bakenow.ui.RecipeListFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    private int    mServings     = 8;
    private String mServingsText = "Serves: " + mServings;

    private IdlingResource mProgressIdlingResource;

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityRule = new ActivityTestRule<RecipeActivity>(RecipeActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            Recipe recipe = new Recipe();
            recipe.setServings(mServings);
            intent.putExtra(RecipeDetailFragment.RECIPE_KEY, recipe);
            return intent;
        }
    };

    @Before
    public void setUp() {
        mProgressIdlingResource = new ProgressDialogIdlingResource(mActivityRule.getActivity().getSupportFragmentManager(), RecipeListFragment.class.getSimpleName());
        IdlingRegistry.getInstance().register(mProgressIdlingResource);
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(mProgressIdlingResource);
    }

    @Test
    public void testRecipeDetails() {
        onView(withId(R.id.activity_recipe_fragment_container_one)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_recipe_detail_servings)).check(matches(withText(mServingsText)));
    }
}
