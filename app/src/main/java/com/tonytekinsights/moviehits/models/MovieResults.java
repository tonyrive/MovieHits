package com.tonytekinsights.moviehits.models;

import java.util.List;

public class MovieResults {
    private Integer page;
    private Integer totalMovies;
    private Integer totalPages;
    private List<Movie> movies;

    public MovieResults() { }

    public MovieResults(Integer page, Integer totalMovies, Integer totalPages, List<Movie> movies) {
        super();
        this.page = page;
        this.totalMovies = totalMovies;
        this.totalPages = totalPages;
        this.movies = movies;
    }

    public Integer getSize() { return this.movies.size() -1;}

    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalMovies() {
        return totalMovies;
    }

    public void setTotalMovies(Integer totalMovies) {
        this.totalMovies = totalMovies;
    }
    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getMovies() {
        return movies;
    }
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void addMovies(List<Movie> movies){
        this.movies.addAll(movies);
    }
}