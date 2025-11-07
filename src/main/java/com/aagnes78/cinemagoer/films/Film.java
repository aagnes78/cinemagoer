package com.aagnes78.cinemagoer.films;

public class Film {
    private final long id;
    private final String title;
    private final String imdb;

    public Film(long id, String title, String imdb) {
        this.id = id;
        this.title = title;
        this.imdb = imdb;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImdb() {
        return imdb;
    }

    public String getImdbLink() {
        return "www.imdb.com/title/" + imdb;
    }
}
