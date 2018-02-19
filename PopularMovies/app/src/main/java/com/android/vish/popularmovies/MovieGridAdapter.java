package com.android.vish.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieGridAdapter extends BaseAdapter {

    private Context     mContext;
    private List<Movie> mMoviePosters;

    public MovieGridAdapter(Context context, List<Movie> moviePosters) {
        mContext = context;
        mMoviePosters = new ArrayList<>(moviePosters);
    }

    @Override
    public int getCount() {
        return mMoviePosters.size();
    }

    @Override
    public Object getItem(int position) {
        return mMoviePosters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(mMoviePosters.get(position).getImage()).into(imageView);
        return imageView;
    }
}
