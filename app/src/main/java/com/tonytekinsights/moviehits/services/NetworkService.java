package com.tonytekinsights.moviehits.services;

import com.tonytekinsights.moviehits.models.Movie;
import com.tonytekinsights.moviehits.models.MovieResults;
import com.tonytekinsights.moviehits.models.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {
    @GET("movie/popular?")
    Call<MovieResults> getPopularMovies();

    @GET("movie/top_rated?")
    Call<MovieResults> getTopRatedMovies();

    @GET("movie/{movie_id}?")
    Call<Movie> getMovie(@Query("movie_id") int id);

    @GET("movie/{movie_id}/videos?")
    Call<Videos> getVideos(@Query("movie_id") int id);

    //@GET("movie/{id}/reviews")
    //Call<Reviews>
}
