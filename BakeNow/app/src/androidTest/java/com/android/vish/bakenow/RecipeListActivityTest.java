package com.android.vish.bakenow;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.vish.bakenow.ui.RecipeListActivity;
import com.android.vish.bakenow.ui.RecipeListFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    private IdlingResource mProgressIdlingResource;

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityRule = new ActivityTestRule<>(RecipeListActivity.class);

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
    public void testRecipeClick() {
        onView(withId(R.id.fragment_recipe_list_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_recipe_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.activity_recipe_fragment_container_one)).check(matches(isDisplayed()));
    }
}
