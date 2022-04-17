package com.grono.moviematchbackend.controller;

import com.grono.moviematchbackend.model.Movie;
import com.grono.moviematchbackend.repository.MovieRepository;
import com.grono.moviematchbackend.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/movie")
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public List<Movie> fetchAllMovies(){
        return movieService.fetchAllMovies();
    }

    @GetMapping(value = "{id}")
    public Movie getMovieById(@PathVariable String id){
        return movieService.getMovieById(Long.valueOf(id));
    }

    @PostMapping(path = "/insert")
    public void insertMovieIntoDB(@RequestBody Movie movie){
        int status = movieService.addMovie(movie);
        if(status == 1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
