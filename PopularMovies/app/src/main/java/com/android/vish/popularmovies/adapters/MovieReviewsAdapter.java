package com.android.vish.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vish.popularmovies.R;
import com.android.vish.popularmovies.models.MovieReview;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {

    private Context           mContext;
    private List<MovieReview> mMovieReviews;

    public MovieReviewsAdapter(Context context, List<MovieReview> reviews) {
        mContext = context;
        mMovieReviews = new ArrayList<>(reviews);
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_movie_review, parent, false);
        return new MovieReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        holder.mAuthor.setText(mMovieReviews.get(position).getAuthor());
        holder.mContent.setText(mMovieReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mMovieReviews.size();
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView mAuthor;
        private TextView mContent;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.movie_review_item_author);
            mContent = itemView.findViewById(R.id.movie_review_item_content);
        }
    }
}
