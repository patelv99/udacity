package com.android.vish.bakenow;


import android.app.ProgressDialog;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;

import com.android.vish.bakenow.ui.RecipeListFragment;

public class ProgressDialogIdlingResource implements IdlingResource {

    private final FragmentManager  mManager;
    private final String           mTag;
    private       ResourceCallback mResourceCallback;

    public ProgressDialogIdlingResource(FragmentManager manager, String tag) {
        mManager = manager;
        mTag = tag;
    }

    @Override
    public String getName() {
        return ProgressDialogIdlingResource.class.getName() + ":" + mTag;
    }

    @Override
    public boolean isIdleNow() {
        ProgressDialog dialog = null;
        if (mManager.findFragmentByTag(mTag) != null) {
            RecipeListFragment fragment = (RecipeListFragment) mManager.findFragmentByTag(mTag);
            if (fragment != null) {
                dialog = fragment.getProgressDialog();
            }
        }
        boolean idle = (dialog == null || !dialog.isShowing());
        if (idle) {
            mResourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }
}