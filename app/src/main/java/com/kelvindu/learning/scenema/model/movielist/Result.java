
package com.kelvindu.learning.scenema.model.movielist;

import io.realm.RealmObject;

/*
* in realm documentations these pojo stuff that you already made MUST inheriting the RealmObject class
* so that when realm transaction begin these stuff can use the crud method in realm
* */

public class Result extends RealmObject{

    public static final String POSTER_URL = "http://image.tmdb.org/t/p/w185";
    private String poster_path;
    private Boolean adult;
    private String overview;
    private String release_date;
    private Integer id;
    private String original_title;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private Float popularity;
    private Integer voteCount;
    private Boolean video;
    private Float vote_average;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = POSTER_URL + poster_path;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String originalTitle) {
        this.original_title = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Float getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(Float voteAverage) {
        this.vote_average = voteAverage;
    }

}
