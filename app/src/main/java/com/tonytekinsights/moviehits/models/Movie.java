package com.tonytekinsights.moviehits.models;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    private int id;
    private String title;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private String releaseDate;
    private String score;
    private String runtime;
    private List<Video> videos;

    public Movie() { }

    public Movie(int id, String title, String posterPath, String backdropPath, String overview, String releaseDate, String score) {
        this(id, title, posterPath, backdropPath, overview, releaseDate, score, null, null);
    }

    public Movie(int id, String title, String posterPath, String backdropPath, String overview, String releaseDate, String score, String runtime, List<Video> videos) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.score = score;
        this.runtime = runtime;
        this.videos = videos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title.trim();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String releaseDate) {
        this.score = score;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
