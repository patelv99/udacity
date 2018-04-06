package com.android.vish.bakenow.ui;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vish.bakenow.R;
import com.android.vish.bakenow.models.Recipe;
import com.android.vish.bakenow.models.RecipeStep;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepFragment extends Fragment {

    public static final String STEP_KEY                  = "recipeStep";
    public static final String VIDEO_POSITION            = "videoPosition";
    public static final String VIDEO_FULLSCREEN_POSITION = "videoFullScreen";

    @BindView(R.id.fragment_recipe_video_container) protected      FrameLayout mVideoContainer;
    @BindView(R.id.fragment_recipe_step_description) protected     TextView    mStepDescription;
    @BindView(R.id.fragment_recipe_step_video) protected           PlayerView  mVideoPlayerView;
    @BindView(R.id.exo_fullscreen_icon) protected                  ImageView   mFullScreenIcon;
    @BindView(R.id.fragment_recipe_step_previous_button) protected Button      mPreviousButton;
    @BindView(R.id.fragment_recipe_step_next_button) protected     Button      mNextButton;

    private Recipe          mRecipe;
    private int             mStepNumber;
    private SimpleExoPlayer mExoPlayer;
    private long            mVideoPosition;
    private Dialog          mFullScreenDialog;
    private boolean mIsFullScreen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            mRecipe = (Recipe) savedInstanceState.getSerializable(RecipeDetailFragment.RECIPE_KEY);
            mStepNumber = savedInstanceState.getInt(STEP_KEY);
            mVideoPosition = savedInstanceState.getLong(VIDEO_POSITION);
            mIsFullScreen = savedInstanceState.getBoolean(VIDEO_FULLSCREEN_POSITION);
        } else if (getArguments() != null) {
            mRecipe = (Recipe) getArguments().getSerializable(RecipeDetailFragment.RECIPE_KEY);
            mStepNumber = getArguments().getInt(STEP_KEY);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        mVideoPlayerView.setPlayer(mExoPlayer);
        mFullScreenDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mIsFullScreen)
                    showNormalVideo();
                super.onBackPressed();
            }
        };
        if (mIsFullScreen) {
            showFullScreenVideo();
        }
        if (mRecipe != null) {
            updateDisplayedStep();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mVideoPosition = Math.max(0, mExoPlayer.getContentPosition());
            mExoPlayer.release();
        }
        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RecipeDetailFragment.RECIPE_KEY, mRecipe);
        outState.putInt(STEP_KEY, mStepNumber);
        outState.putLong(VIDEO_POSITION, mVideoPosition);
        outState.putBoolean(VIDEO_FULLSCREEN_POSITION, mIsFullScreen);
    }

    @OnClick(R.id.exo_fullscreen_icon)
    public void onFullScreenClick() {
        if (!mIsFullScreen) {
            showFullScreenVideo();
        } else {
            showNormalVideo();
        }
    }

    @OnClick(R.id.fragment_recipe_step_previous_button)
    public void onPreviousClick() {
        if (mStepNumber > 0) {
            mStepNumber--;
            updateDisplayedStep();
        }
    }

    @OnClick(R.id.fragment_recipe_step_next_button)
    public void onNextClick() {
        if (mStepNumber < mRecipe.getSteps().size() - 1) {
            mStepNumber++;
            updateDisplayedStep();
        }
    }

    /**
     * Update the UI to reflect the current {@link RecipeStep}
     */
    public void updateDisplayedStep() {
        mStepDescription.setText(mRecipe.getSteps().get(mStepNumber).getDescription());
        if (!TextUtils.isEmpty(mRecipe.getSteps().get(mStepNumber).getVideoUrl())) {
            mVideoContainer.setVisibility(View.VISIBLE);
            loadVideoUri(Uri.parse(mRecipe.getSteps().get(mStepNumber).getVideoUrl()));
        } else {
            mVideoContainer.setVisibility(View.GONE);
        }
        if (mStepNumber <= 0) {
            mPreviousButton.setVisibility(View.GONE);
        } else {
            mPreviousButton.setVisibility(View.VISIBLE);
        }
        if (mStepNumber >= mRecipe.getSteps().size() - 1) {
            mNextButton.setVisibility(View.GONE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Update the associated {@link RecipeStep} with the step number given
     *
     * @param position position of the {@link RecipeStep}
     */
    public void updateStepNumber(int position) {
        mStepNumber = position;
        mStepDescription.setText(mRecipe.getSteps().get(mStepNumber).getDescription());
    }


    /**
     * Load the videoUri into a {@link ExoPlayer}
     *
     * @param videoUri Uri for the video to display
     */
    public void loadVideoUri(Uri videoUri) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                                                                            Util.getUserAgent(getActivity(), getString(R.string.app_name)),
                                                                            bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);

        mExoPlayer.prepare(videoSource);
        if (mExoPlayer.getCurrentWindowIndex() > -1) {
            mExoPlayer.seekTo(mExoPlayer.getCurrentWindowIndex(), mVideoPosition);
        }
    }

    /**
     * Open video in full screen mode
     */
    private void showFullScreenVideo() {
        ((ViewGroup) mVideoPlayerView.getParent()).removeView(mVideoPlayerView);
        mFullScreenDialog.addContentView(mVideoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.exo_controls_fullscreen_exit));
        mIsFullScreen = true;
        mFullScreenDialog.show();
    }

    /**
     * Show video in normal screen mode
     */
    private void showNormalVideo() {
        ((ViewGroup) mVideoPlayerView.getParent()).removeView(mVideoPlayerView);
        mVideoContainer.addView(mVideoPlayerView);
        mIsFullScreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.exo_controls_fullscreen_enter));
    }
}
