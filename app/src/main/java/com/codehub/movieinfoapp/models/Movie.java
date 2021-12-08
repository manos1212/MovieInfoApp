package com.codehub.movieinfoapp.models;

public class Movie {
    public int id;
    public String movieName;
    public String movieThumbnailUrl;
    public String movieDescription;
    public Double movieRating;

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
