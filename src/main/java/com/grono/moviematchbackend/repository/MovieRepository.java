package com.grono.moviematchbackend.repository;

import com.grono.moviematchbackend.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MovieRepository extends MongoRepository<Movie, String> {


    Optional<Movie> findMovieByMovieId(Long movieId);


}
