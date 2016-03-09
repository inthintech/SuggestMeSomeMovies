package com.keyrelations.suggestmesomemovies;


public class Movie {

    private String id;
    private String title;
    private String releaseYear;
    private String posterPath;
    private String isSuggested;
    private String suggestedCount;
    private String imdbRating;

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getReleaseYear() { return releaseYear; }
    public String getPosterPath() { return posterPath; }
    public String getIsSuggested() { return isSuggested; }
    public String getSuggestedCount() { return suggestedCount; }
    public String getImdbRating() { return imdbRating; }

    // use this for add movies
    public Movie(String id,String title,String releaseYear,String posterPath) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.posterPath = posterPath;
    }

    public Movie(String id,String title,String releaseYear,String posterPath,String suggestedCount,String imdbRating) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.posterPath = posterPath;
        this.suggestedCount = suggestedCount;
        this.imdbRating = imdbRating;
    }

    // use this for library
    public Movie(String id,String title,String releaseYear,String posterPath,String isSuggested,String suggestedCount,String imdbRating) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.posterPath = posterPath;
        this.isSuggested = isSuggested;
        this.suggestedCount = suggestedCount;
        this.imdbRating = imdbRating;
    }

}
