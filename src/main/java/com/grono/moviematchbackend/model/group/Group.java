package com.grono.moviematchbackend.model.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grono.moviematchbackend.model.group.Code.Code;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Group {
    @Id
    private String id;

    @NotBlank
    private String name;
    private List<String> users;
    private List<Long> movies;
    @JsonProperty("tv_shows")
    private List<Long> tvShows;
    @NotNull
    private Code code;

    public Group(String name, List<String> users) {
        this.name = name;
        this.users = users;
        this.movies = List.of();
        this.tvShows = List.of();
        this.code = Code.getInstance();
    }
}
