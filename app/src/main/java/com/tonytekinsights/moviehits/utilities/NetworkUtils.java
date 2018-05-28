package com.tonytekinsights.moviehits.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.tonytekinsights.moviehits.BuildConfig;
import com.tonytekinsights.moviehits.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    public final static int MOVIE_URL_POPULAR = 0;
    public final static int MOVIE_URL_TOP_RATED = 1;
    public final static int MOVIE_URL_INFO = 2;
    public final static int MOVIE_URL_IMAGE = 3;

    public static URL buildUrl(Context context, int movieUrl){
        return buildUrl(context, movieUrl, null, null);
    }

    public static URL buildUrl(Context context, int movieUrl, String pageNum) {
        return buildUrl(context, movieUrl, pageNum, null);
    }

    public static URL buildUrl(Context context, int movieUrl, String pageNum, Integer movieId){
        Uri uri = null;
        Uri.Builder builder = null;

        if (movieUrl == MOVIE_URL_IMAGE) {
            uri = Uri.parse(context.getString(R.string.tmdb_image_base_url));
        } else {
            uri = Uri.parse(context.getString(R.string.tmdb_api_base_url));
        }

        builder = uri.buildUpon();

        if (movieUrl != MOVIE_URL_IMAGE) {
            builder.appendQueryParameter("api_key", BuildConfig.ApiKey);
        }

        switch (movieUrl){
            case MOVIE_URL_POPULAR:
                if(pageNum != null)
                    builder.appendQueryParameter("page", pageNum);
                builder.appendEncodedPath(context.getString(R.string.tmdb_popular_movies));
                break;
            case MOVIE_URL_TOP_RATED:
                if(pageNum != null)
                    builder.appendQueryParameter("page", pageNum);
                builder.appendEncodedPath(context.getString(R.string.tmdb_top_rated_movies));
                break;
            case MOVIE_URL_INFO:
                builder.appendEncodedPath(context.getString(R.string.tmdb_movie_info));
                builder.appendEncodedPath(movieId.toString());
                builder.appendQueryParameter("append_to_response", "videos");
                break;
            case MOVIE_URL_IMAGE:
                builder.appendEncodedPath(context.getString(R.string.tmdb_file_size_500));
                break;
            default:
                break;
        }

        uri = builder.build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

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

    public static boolean isOnline(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static void noInternet(View v){
        final Snackbar snackbar = Snackbar.make(v,
                R.string.no_network, Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
