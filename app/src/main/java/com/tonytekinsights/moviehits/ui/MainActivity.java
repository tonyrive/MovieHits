package com.tonytekinsights.moviehits.ui;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.tonytekinsights.moviehits.adapter.MovieAdapter;
import com.tonytekinsights.moviehits.R;
import com.tonytekinsights.moviehits.models.MovieResults;
import com.tonytekinsights.moviehits.utilities.JsonUtils;
import com.tonytekinsights.moviehits.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private MovieResults movieResults;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private int dataPage = 1;
    private ProgressBar progressBar;
    private Parcelable recyclerState;
    private String recState = "recyclerState";
    private BottomNavigationView bottomNavigationView;
    private String movieUrlType = "urlType";
    private int movieUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int numOfCol = 3;
        layoutManager = new GridLayoutManager(this, numOfCol);
        progressBar = (ProgressBar)findViewById(R.id.pg_bar);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(getItemSelectedListener());

        recyclerView = (RecyclerView)findViewById(R.id.rv_movies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(getScrollListener(layoutManager));

        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);

        if(savedInstanceState == null) {
            movieUrl = NetworkUtils.MOVIE_URL_POPULAR;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        bottomNavigationView.setSelectedItemId(itemId);

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener getItemSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(!NetworkUtils.isOnline(getBaseContext())) {
                    NetworkUtils.noInternet(findViewById(R.id.mainFrame));
                } else {
                    doItemSelected(item.getItemId());
                    movieResults = null;
                }
                return true;
            }
        };
    }

    private void doItemSelected(int itemId){
        switch (itemId) {
            case R.id.navigation_popular:
                movieUrl = NetworkUtils.MOVIE_URL_POPULAR;
                getMovies(RequestType.REFRESH);
                break;
            case R.id.navigation_top_rated:
                movieUrl = NetworkUtils.MOVIE_URL_TOP_RATED;
                getMovies(RequestType.REFRESH);
                break;
        }
    }

    private RecyclerView.OnScrollListener getScrollListener(final GridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int visibleThreshold = 16;

                if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    getMovies(RequestType.LOAD_MORE);
                }
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        recyclerState = layoutManager.onSaveInstanceState();
        outState.putParcelable(recState, recyclerState);
        outState.putInt(movieUrlType, movieUrl);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null){
            recyclerState = savedInstanceState.getParcelable(recState);
            movieUrl = savedInstanceState.getInt(movieUrlType);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (recyclerState != null){
            layoutManager.onRestoreInstanceState(recyclerState);
        }
        getMovies(RequestType.REFRESH);
    }

    private void loadMovies(String json){
        if(movieResults == null) {
            movieResults = JsonUtils.parseMovieListJson(json);
            movieAdapter.setData(movieResults);
        } else {
            MovieResults moreMovie = JsonUtils.parseMovieListJson(json);
            movieResults.addMovies(moreMovie.getMovies());
        }
        movieAdapter.notifyDataSetChanged();
    }

    private void getMovies(RequestType requestType) {
        if(NetworkUtils.isOnline(this)) {
            switch (requestType){
                case REFRESH:
                    dataPage = 1;
                    break;
                case LOAD_MORE:
                    dataPage++;
                    break;
            }
            URL url = NetworkUtils.buildUrl(this, movieUrl, String.valueOf(dataPage));
            new TmDbQueryTask().execute(url);
        } else {
            NetworkUtils.noInternet(findViewById(R.id.mainFrame));
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
            String movieResults = null;
            try {
                movieResults = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieResults;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")){
                loadMovies(s);
            }
        }
    }

    public enum RequestType {
        REFRESH,
        LOAD_MORE
    }
}
