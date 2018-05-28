package com.tonytekinsights.moviehits.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonytekinsights.moviehits.models.Movie;
import com.tonytekinsights.moviehits.utilities.JsonUtils;
import com.tonytekinsights.moviehits.utilities.NetworkUtils;
import com.tonytekinsights.moviehits.R;

import java.io.IOException;
import java.net.URL;

public class MovieInfoActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        progressBar = (ProgressBar)findViewById(R.id.pg_bar);

        if(!NetworkUtils.isOnline(getBaseContext())) {
            NetworkUtils.noInternet(findViewById(R.id.detailLayout));
            return;
        }

        Intent intent = getIntent();
        String movieId = getString(R.string.movie_id);
        if (intent.hasExtra(movieId)){
            int Id = intent.getIntExtra(movieId, 0);
            getMovie(Id);
        }
    }

    private void getMovie(int movieId){
        if(NetworkUtils.isOnline(this)) {
            URL url = NetworkUtils.buildUrl(this, NetworkUtils.MOVIE_URL_INFO, null, movieId);
            new TmDbQueryTask().execute(url);
        }
    }

    private void loadMovie(String json){
        if(movie == null) {
            movie = JsonUtils.parseMovieDetailsJson(json);

            ImageView mvPoster = (ImageView) findViewById(R.id.mv_poster);
            ImageView mvBackdrop = (ImageView) findViewById(R.id.mv_backdrop);

            TextView mvTitle = (TextView) findViewById(R.id.mv_title);
            TextView mvOverview = (TextView) findViewById(R.id.mv_overview);
            TextView mvYear = (TextView) findViewById(R.id.mv_year);
            TextView mvMins = (TextView) findViewById(R.id.mv_mins);
            TextView mvRating = (TextView) findViewById(R.id.mv_rating);

            URL urlImage = NetworkUtils.buildUrl(this, NetworkUtils.MOVIE_URL_IMAGE);
            Picasso.with(this)
                    .load(urlImage + movie.getPosterPath())
                    .placeholder(R.drawable.test_poster)
                    .into(mvPoster);

            Picasso.with(this)
                    .load(urlImage + movie.getBackdropPath())
                    .placeholder(R.drawable.test_backdrop)
                    .into(mvBackdrop);

            mvTitle.setText(movie.getTitle());
            mvOverview.setText(movie.getOverview());

            String[] dt = movie.getReleaseDate().split("-");
            mvYear.setText(dt[0]);

            mvMins.setText(String.format("%smins", movie.getRuntime()));
            mvRating.setText(String.format("%s/10", movie.getScore()));
        }
    }

    public class TmDbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String movieDetails = null;
            try {
                movieDetails = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieDetails;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")){
                loadMovie(s);
            }
        }
    }

}
