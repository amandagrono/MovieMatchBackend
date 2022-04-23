package com.grono.moviematchbackend.controller;

import com.grono.moviematchbackend.model.movie.Movie;
import com.grono.moviematchbackend.model.movie.request.FetchMoviesBody;
import com.grono.moviematchbackend.model.movie.request.ViewMovieBody;
import com.grono.moviematchbackend.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/movie")
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/fetch")
    public List<Movie> getAll(){
        return movieService.fetchAllMovies();
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        movieService.delete(id);
    }
    @GetMapping(value = "{id}")
    public Movie getMovieById(@PathVariable String id){
        return movieService.getMovieById(Integer.valueOf(id));
    }

    @PostMapping(path = "/insert")
    public void insertMovieIntoDB(@RequestBody Movie movie){
        int status = movieService.addMovie(movie);
        if(status == 1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/view")
    public void viewMovie(@RequestBody ViewMovieBody body){
        movieService.viewMovie(body);
    }
    @GetMapping("/fetch")
    public List<Movie> fetchMovies(@RequestBody FetchMoviesBody body){
        return movieService.fetch(body);
        //return null;
    }
}
