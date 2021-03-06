package com.grono.moviematchbackend.model.movie;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.grono.moviematchbackend.model.tvshow.Country;
import com.grono.moviematchbackend.model.enums.Genre;
import com.grono.moviematchbackend.model.tvshow.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document
public class Movie {
    @Id
    private String id;
    @Indexed(unique = true)
    @JsonProperty("movie_id")
    @NotNull(message = "movieId must not be null")
    private Integer movieId;
    @NotBlank(message = "Title must not be blank")
    private String title;

    private String overview;
    @NotNull(message = "Genres must not be blank")
    private List<Genre> genres;
    @NotNull(message = "Streaming Services must not be blank")
    @JsonProperty("countries")
    private List<Country> countries;

    @JsonProperty("poster_url")
    private String posterUrl;
    @NotNull(message = "Popularity must not be blank")
    private Double popularity;
    @JsonProperty("release_date")
    private Date releaseDate;
    @JsonProperty("imdb_id")
    private String imdbId;
    @NotBlank(message = "Type must not be blank")
    private String type;
    @JsonProperty("age_rating")

    private List<Season> seasons;
    private String status;
    @JsonProperty("last_air_date")
    private Date lastAirDate;
    private List<String> creators;
    private String director;
    private List<String> cast;

    private String ageRating;

    public Movie(Integer movieId, String title, String overview, List<Genre> genres, List<Country> countries, String posterUrl, Double popularity, String director, List<String> cast, Date releaseDate, String imdbId, String type, String ageRating, List<Season> seasons, Date lastAirDate, String status, List<String> creators ) {
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.genres = genres;
        this.countries = countries;
        this.posterUrl = posterUrl;
        this.popularity = popularity;
        this.director = director;
        this.cast = cast;
        this.releaseDate = releaseDate;
        this.imdbId = imdbId;
        this.type = type;
        this.ageRating = ageRating;
        this.seasons = seasons;
        this.lastAirDate = lastAirDate;
        this.creators = creators;
        this.status = status;
    }
}
