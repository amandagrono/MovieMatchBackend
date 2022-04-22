package com.grono.moviematchbackend.model.movie.request;

import com.grono.moviematchbackend.model.enums.Genre;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class FetchMoviesBody {
    private String username;
    private String token;
    private List<Genre> genres;
    private Set<String> directors;
    private Set<String> cast;
    private Date start;
    private Date end;

}
