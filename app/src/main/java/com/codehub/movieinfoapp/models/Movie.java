package com.codehub.movieinfoapp.models;

public class Movie {
    public int id;
    public String movieName;
    public String movieThumbnailUrl;

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
