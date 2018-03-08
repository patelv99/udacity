package com.android.vish.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vish.popularmovies.R;
import com.android.vish.popularmovies.models.MovieVideos;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailerViewHolder> {

    private Context           mContext;
    private List<MovieVideos> mMovieTrailers;

    public MovieTrailersAdapter(Context context, List<MovieVideos> reviews) {
        mContext = context;
        mMovieTrailers = new ArrayList<>(reviews);
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_movie_trailer, parent, false);
        return new MovieTrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        holder.mName.setText(mMovieTrailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mMovieTrailers.size();
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.movie_trailer_name);
        }
    }
}
