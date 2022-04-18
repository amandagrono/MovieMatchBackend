package com.grono.moviematchbackend.model.enums;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum Genre {
    @JsonProperty("Action & Adventure")
    ActionAndAdventure("Action & Adventure"),
    Action("Action"),
    Adventure("Adventure"),
    Animation("Animation"),
    Comedy("Comedy"),
    Crime("Crime"),
    Documentary("Documentary"),
    Drama("Drama"),
    Family("Family"),
    Fantasy("Fantasy"),
    Kids("Kids"),
    History("History"),
    Horror("Horror"),
    Music("Music"),
    Mystery("Mystery"),
    News("News"),
    Reality("Reality"),
    Romance("Romance"),
    @JsonProperty("Science Fiction")
    Science_Fiction("Science Fiction"),
    @JsonProperty("Sci-Fi & Fantasy")
    SciFiAndFantasy("Sci-Fi & Fantasy"),
    Soap("Soap"),
    Talk("Talk"),
    @JsonProperty("TV Movie")
    TV_Movie("TV Movie"),
    Thriller("Thriller"),
    War("War"),
    @JsonProperty("War & Politics")
    WasAndPolitics("War & Politics"),
    Western("Western");

    private final String genreType;

    Genre(String genreType) {
        this.genreType = genreType;
    }
    public String getGenreType(){
        return genreType;
    }
}
