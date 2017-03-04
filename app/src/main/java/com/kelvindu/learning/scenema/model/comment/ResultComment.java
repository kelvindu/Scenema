
package com.kelvindu.learning.scenema.model.comment;


import io.realm.RealmObject;

/*
* in realm documentations these pojo stuff that you already made MUST inheriting the RealmObject class
* so that when realm transaction begin these stuff can use the crud method in realm
* */

public class ResultComment extends RealmObject {

    private String id;
    private String author;
    private String content;
    private String url;
    private String movieId;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
