package com.grono.moviematchbackend.model.tvshow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grono.moviematchbackend.model.Season;
import com.grono.moviematchbackend.model.enums.Genre;
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
public class TVShow {
    @Id
    private String id;

    @Indexed
    @JsonProperty("tv_id")
    @NotNull(message = "tv_id must not be blank")
    private Long tvId;

    @NotBlank(message = "Title not be blank")
    private String title;
    @NotBlank(message = "Overview must not be blank")
    private String overview;
    @NotNull(message = "Genres must not be null")
    private List<Genre> genres;
    @NotNull(message = "Countries must not be null")
    private List<Country> countries;

    @JsonProperty("poster_url")
    private String posterUrl;
    @NotNull(message = "Popularity must not be null")
    private Double popularity;
    private List<String> cast;

    @JsonProperty("release_date")
    private Date releaseDate;
    @JsonProperty("imdb_id")
    private String imdbId;
    @NotBlank(message = "Type must not be blank")
    private String type;
    private List<Season> seasons;
    @NotBlank(message = "Status must not be blank")
    private String status;
    @JsonProperty("last_air_date")
    private Date lastAirDate;
    private List<String> creators;

    public TVShow(Long tvId, String title, String overview, List<Genre> genres, List<Country> countries, String posterUrl, Double popularity, List<String> cast, Date releaseDate, String imdbId, String type, List<Season> seasons, String status, Date lastAirDate, List<String> creators) {
        this.tvId = tvId;
        this.title = title;
        this.overview = overview;
        this.genres = genres;
        this.countries = countries;
        this.posterUrl = posterUrl;
        this.popularity = popularity;
        this.cast = cast;
        this.releaseDate = releaseDate;
        this.imdbId = imdbId;
        this.type = type;
        this.seasons = seasons;
        this.status = status;
        this.lastAirDate = lastAirDate;
        this.creators = creators;
    }
}
