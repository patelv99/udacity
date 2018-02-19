package com.android.vish.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {

    private static final String MOVIES_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIES_DB_API_KEY  = ""; //TODO update with personal API key
    private static final String API_KEY_PARAM      = "?api_key=";

    public static final String POPULAR_ENDPOINT   = "popular";
    public static final String TOP_RATED_ENDPOINT = "top_rated";


    /**
     * Build a URL with the sort criteria
     *
     * @param sortCriteria user's preferred method of sorting
     * @return URL for API request
     */
    public static URL buildUrl(String sortCriteria) {
        Uri builtUri = Uri.parse(MOVIES_DB_BASE_URL + sortCriteria + API_KEY_PARAM + MOVIES_DB_API_KEY)
                .buildUpon()
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(NetworkUtils.class.getSimpleName(), e.getMessage());
        }
        return url;
    }

    /**
     * Get the API response from the URL provided
     *
     * @param url URL to get the response from
     * @return String of JSON
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Parse the given json String in to a {@link List} of {@link Movie}
     *
     * @param moviesJson json string
     * @return {@link List} of {@link Movie}
     */
    public static List<Movie> parseMoviesJson(String moviesJson) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject moviesJsonObject = new JSONObject(moviesJson);
            JSONArray results = moviesJsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                Movie movie = new Movie();
                movie.setId(results.getJSONObject(i).getInt("id"));
                movie.setImage(results.getJSONObject(i).getString("poster_path"));
                movie.setTitle(results.getJSONObject(i).getString("title"));
                movie.setReleaseDate(results.getJSONObject(i).getString("release_date"));
                movie.setAverageVote(results.getJSONObject(i).getInt("vote_average"));
                movie.setPlot(results.getJSONObject(i).getString("overview"));
                movie.setPopularity(results.getJSONObject(i).getInt("popularity"));
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

}
