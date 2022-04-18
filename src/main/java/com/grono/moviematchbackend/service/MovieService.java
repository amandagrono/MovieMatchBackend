package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.model.Movie;
import com.grono.moviematchbackend.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
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
        Optional<Movie> mv = movieRepository.findMovieByMovieId(movie.getMovieId());
        //if movie already exists in DB
        if(mv.isPresent()){
            movie.setId(mv.get().getId());
            movieRepository.save(movie);
            System.out.println("Updated!");
        }
        //if movie doesn't exist in DB
        else{
            System.out.println("Inserted!");
            movieRepository.insert(movie);
        }
        /*try{
            movieRepository.save(movie);
        }
        catch (DuplicateKeyException e){
            e.printStackTrace();
            movieRepository.
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
        return 0;


    }

}
