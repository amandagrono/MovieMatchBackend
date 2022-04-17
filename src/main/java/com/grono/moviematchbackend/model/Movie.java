package com.grono.moviematchbackend.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.grono.moviematchbackend.model.enums.Genre;
import com.grono.moviematchbackend.model.enums.StreamingService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNullFields;
import org.springframework.validation.annotation.Validated;

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
    private Long movieId;
    @NotBlank(message = "Title must not be blank")
    private String title;
    @NotBlank(message = "Overview must not be blank")
    private String overview;
    @NotNull(message = "Genres must not be blank")
    private List<Genre> genres;
    @NotNull(message = "Streaming Services must not be blank")
    @JsonProperty("streaming_services")
    private List<StreamingService> streamingServices;

    @JsonProperty("poster_url")
    private String posterUrl;
    @NotNull(message = "Popularity must not be blank")
    private Double popularity;
    @NotBlank(message = "Director must not be blank")
    private String director;
    private List<String> cast;
    @JsonProperty("release_date")
    private Date releaseDate;
    @JsonProperty("imdb_id")
    private String imdbId;
    @NotBlank(message = "Type must not be blank")
    private String type;
    @JsonProperty("age_rating")
    private String ageRating;

    public Movie(Long movieId, String title, String overview, List<Genre> genres, List<StreamingService> streamingServices, String posterUrl, Double popularity, String director, List<String> cast, Date releaseDate, String imdbId, String type, String ageRating) {
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.genres = genres;
        this.streamingServices = streamingServices;
        this.posterUrl = posterUrl;
        this.popularity = popularity;
        this.director = director;
        this.cast = cast;
        this.releaseDate = releaseDate;
        this.imdbId = imdbId;
        this.type = type;
        this.ageRating = ageRating;
    }
}
