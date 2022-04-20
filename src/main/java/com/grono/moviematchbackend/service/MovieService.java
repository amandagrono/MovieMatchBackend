package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.model.movie.Movie;
import com.grono.moviematchbackend.model.movie.request.ViewMovieBody;
import com.grono.moviematchbackend.model.user.User;
import com.grono.moviematchbackend.repository.MovieRepository;
import com.grono.moviematchbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<Movie> fetchAllMovies(){
        return movieRepository.findAll();
    }

    public Movie getMovieById(Integer id){
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
    public void viewMovie(ViewMovieBody body){
        if(!userService.checkSession(body.getUsername(), body.getToken())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> user = userRepository.findUserByUsername(body.getUsername());
        if(user.isPresent()){
            if(body.getLike()){
                user.get().getListOfLikedMovies().add(body.getMovieId());
            }
            else{
                user.get().getListOfDislikedMovies().add(body.getMovieId());
            }
            userRepository.save(user.get());
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
