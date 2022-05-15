package com.grono.moviematchbackend.model.tvshow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Season {
    @JsonProperty("air_date")
    private Date airDate;

    @JsonProperty("episode_count")
    private Integer episodeCount;

    @JsonProperty("name")
    private String name;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("season_number")
    private Integer seasonNumber;
}
