package com.tonytekinsights.moviehits.utilities;

import com.tonytekinsights.moviehits.models.Movie;
import com.tonytekinsights.moviehits.models.MovieResults;
import com.tonytekinsights.moviehits.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static MovieResults parseMovieListJson(String json){
        Integer page = null;
        Integer totalMovies = null;
        Integer totalPages = null;
        List<Movie> movies = null;

        Integer id = null;
        String title = null;
        String posterPath = null;
        String backdropPath = null;
        String overview = null;
        String releaseDate = null;
        String score = null;


        try{
            JSONObject data = new JSONObject(json);
            page = data.optInt("page");
            totalMovies = data.optInt("total_results");
            totalPages = data.optInt("total_pages");

            JSONArray results = data.getJSONArray("results");
            JSONObject movieJson = null;
            movies = new ArrayList<>();
            if (results != null){
               for (int i = 0; i < results.length(); i++){
                   movieJson = results.getJSONObject(i);
                   posterPath = movieJson.optString("poster_path");
                   if (!posterPath.trim().isEmpty()) {
                       id = movieJson.optInt("id");
                       title = movieJson.optString("title");
                       backdropPath = movieJson.optString("backdrop_path");
                       overview = movieJson.optString("overview");
                       releaseDate = movieJson.optString("release_date");

                       score = movieJson.optString("vote_average");


                       movies.add(new Movie(id, title, posterPath, backdropPath, overview, releaseDate, score));
                   }
               }
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return new MovieResults(page, totalMovies, totalPages, movies);
    }


    public static Movie parseMovieDetailsJson(String json){
        Integer id = null;
        String title = null;
        String posterPath = null;
        String backdropPath = null;
        String overview = null;
        String releaseDate = null;
        String score = null;
        String runtime = null;
        List<Video> videos = null;

        Integer videoId = null;
        String key = null;
        String name = null;
        String site = null;
        String type = null;


        try{
            JSONObject movieJson = new JSONObject(json);
            id = movieJson.optInt("id");
            title = movieJson.optString("title");
            posterPath = movieJson.optString("poster_path");
            backdropPath = movieJson.optString("backdrop_path");
            overview = movieJson.optString("overview");
            releaseDate = movieJson.optString("release_date");
            score = movieJson.optString("vote_average");
            runtime = movieJson.optString("runtime");

            JSONObject videosJson = movieJson.getJSONObject("videos");
            JSONArray results = videosJson.getJSONArray("results");

            videos = new ArrayList<>();
            JSONObject videoJson = null;
            if (results != null){
                for (int i = 0; i < results.length(); i++){
                    videoJson = results.getJSONObject(i);
                    videoId = videoJson.optInt("id");
                    key = videoJson.optString("key");
                    name = videoJson.optString("name");
                    site = videoJson.optString("site");
                    type = videoJson.optString("type");


                    videos.add(new Video(videoId, key, name, site, type));
                }
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return new Movie(id, title, posterPath, backdropPath, overview, releaseDate, score, runtime, videos);
    }
}
