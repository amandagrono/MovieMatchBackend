package com.grono.moviematchbackend.model.movie.request;

import lombok.Data;

@Data
public class ViewMovieBody {
    String username;
    String token;
    Integer movieId;
    Boolean like;
}
