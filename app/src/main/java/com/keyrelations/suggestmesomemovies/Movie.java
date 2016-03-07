package com.keyrelations.suggestmesomemovies;


public class Movie {

    private String id;
    private String title;
    private String releaseYear;
    private String posterPath;
    private String isSuggested;
    private Integer suggestedCount;

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getReleaseYear() { return releaseYear; }
    public String getPosterPath() { return posterPath; }
    public String getIsSuggested() { return isSuggested; }
    public Integer getSuggestedCount() { return suggestedCount; }

    public Movie(String id,String title,String releaseYear,String posterPath) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.posterPath = posterPath;
    }

    public Movie(String id,String title,String releaseYear,String posterPath,Integer suggestedCount) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.posterPath = posterPath;
        this.suggestedCount = suggestedCount;
    }

    public Movie(String id,String title,String releaseYear,String posterPath,String isSuggested) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.posterPath = posterPath;
        this.isSuggested = isSuggested;
    }

}
