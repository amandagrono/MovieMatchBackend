package com.grono.moviematchbackend.model.enums;


public enum Genre {
    Action("Action"),
    Adventure("Adventure"),
    Animation("Animation"),
    Comedy("Comedy"),
    Crime("Crime"),
    Documentary("Documentary"),
    Drama("Drama"),
    Family("Family"),
    Fantasy("Fantasy"),
    History("History"),
    Horror("Horror"),
    Music("Music"),
    Mystery("Mystery"),
    Romance("Romance"),
    Science_Fiction("Science Fiction"),
    TV_Movie("TV Movie"),
    Thriller("Thriller"),
    War("War"),
    Western("Western");

    private final String genreType;

    Genre(String genreType) {
        this.genreType = genreType;
    }
    public String getGenreType(){
        return genreType;
    }
}
