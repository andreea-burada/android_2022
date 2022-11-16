package com.example.seminar04;

import java.util.Date;

public class Movie {
    private String title;
    private Genre genre;
    private Integer duration;
    private Date release;
    private byte rating;
    private String poster;
    private boolean isRecommended;
    private boolean isWatchlist;

    public Movie() {
    }

    public Movie(String title, Genre genre, Date release, Integer duration, byte rating, String poster,
                 boolean isRecommended, boolean isWatchlist) {
        this.title = title;
        this.genre = genre;
        this.release = release;
        this.duration = duration;
        this.rating = rating;
        this.poster = poster;
        this.isRecommended = isRecommended;
        this.isWatchlist = isWatchlist;
    }

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public boolean isWatchlist() {
        return isWatchlist;
    }

    public void setWatchlist(boolean watchlist) {
        isWatchlist = watchlist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", genre=" + genre +
                ", release=" + release +
                ", duration=" + duration +
                ", rating=" + rating +
                ", poster=" + poster +
                ", recommended=" + isRecommended +
                ", watchlist=" + isWatchlist +
                '}';
    }
}
