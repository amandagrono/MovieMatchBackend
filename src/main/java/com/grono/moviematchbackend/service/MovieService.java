package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.model.Movie;
import com.grono.moviematchbackend.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<Movie> fetchAllMovies(){
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id){
        Optional<Movie> movie = movieRepository.findMovieByMovieId(id);
        return movie.orElse(null);
    }

    public int addMovie(Movie movie){
        try{
            movieRepository.insert(movie);
        }
        catch (Exception e){
            e.printStackTrace();
            return 1;
        }
        return 0;


    }

}
