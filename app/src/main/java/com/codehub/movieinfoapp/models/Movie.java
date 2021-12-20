package com.codehub.movieinfoapp.models;

public class Movie {
    private int id;
    private String movieName;
    private String movieThumbnailUrl;
    private String movieThumbnailUrlNoTitle;

    public String getMovieThumbnailUrlNoTitle() {
        return movieThumbnailUrlNoTitle;
    }

    public void setMovieThumbnailUrlNoTitle(String movieThumbnailUrlNoTitle) {
        this.movieThumbnailUrlNoTitle = movieThumbnailUrlNoTitle;
    }

    private String movieDescription;
    private Double movieRating;

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(Double movieRating) {
        this.movieRating = movieRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieThumbnailUrl() {
        return movieThumbnailUrl;
    }

    public void setMovieThumbnailUrl(String movieThumbnailUrl) {
        this.movieThumbnailUrl = movieThumbnailUrl;
    }
}
