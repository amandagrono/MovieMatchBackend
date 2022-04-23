package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.model.enums.Genre;
import com.grono.moviematchbackend.model.enums.StreamingService;
import com.grono.moviematchbackend.model.group.Group;
import com.grono.moviematchbackend.model.movie.Movie;
import com.grono.moviematchbackend.model.movie.request.FetchMoviesBody;
import com.grono.moviematchbackend.model.movie.request.ViewMovieBody;
import com.grono.moviematchbackend.model.user.User;
import com.grono.moviematchbackend.repository.GroupRepository;
import com.grono.moviematchbackend.repository.MovieRepository;
import com.grono.moviematchbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public List<Movie> fetchAllMovies(){
        return movieRepository.findAll();
    }
    public void delete(String id){
        movieRepository.deleteById(id);
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
            //System.out.println("Updated!");
        }
        //if movie doesn't exist in DB
        else{
            //System.out.println("Inserted!");
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

    public List<Movie> fetch(FetchMoviesBody body){
        if(!userService.checkSession(body.getUsername(), body.getToken()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if(body.getGenres().isEmpty()){
            body.setGenres(Genre.getAllGenres());
        }

        Date start = body.getStart();
        Date end = body.getEnd();
        if(start == null) {
            start = new Date();
            start.setTime(-2208955851000L);
        }
        if(end == null) {
            end = new Date();
        }

        List<Movie> retList = new ArrayList<>();
        Optional<User> user = userRepository.findUserByUsername(body.getUsername());
        if(user.isPresent()){
            List<Integer> viewedMovies = Stream.concat(user.get().getListOfLikedMovies().stream(), user.get().getListOfDislikedMovies().stream()).toList();
            Set<Integer> groupLikedMovies = getGroupLikedMovies(user.get().getGroups());

            if(body.getCast().isEmpty() && body.getDirectors().isEmpty()){
                retList = fetch(viewedMovies, groupLikedMovies, user.get(), body.getGenres(), start, end);
            }
            else if(!body.getCast().isEmpty() && body.getDirectors().isEmpty()){
                retList = fetchCast(viewedMovies, groupLikedMovies, user.get(), body.getGenres(), start, end, body.getCast());
            }
            else if(body.getCast().isEmpty() && !body.getDirectors().isEmpty()){
                retList = fetchDirector(viewedMovies, groupLikedMovies, user.get(), body.getGenres(), start, end, body.getDirectors());
            }
            else{
                retList = fetchCastDirector(viewedMovies, groupLikedMovies, user.get(), body.getGenres(), start, end, body.getDirectors(), body.getCast());
            }



        }

        return retList;
    }

    public Set<Integer> getGroupLikedMovies(List<String> groupIDs){
        Set<String> users = new HashSet<>();
        List<Group> groups = groupRepository.findAllById(groupIDs);
        Set<Integer> groupLikedMovies = new HashSet<>();

        for(Group g : groups){
            users.addAll(g.getUsers());
        }
        List<User> usersList = userRepository.findAllById(users);
        for(User u : usersList){
            groupLikedMovies.addAll(u.getListOfLikedMovies());
        }
        return groupLikedMovies;


    }

    public List<Movie> fetch(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, User user, List<Genre> genres, Date start, Date end){
        System.out.println("Here 1");
        List<Movie> retList;
        retList = movieRepository.fetchMoviesWithGroup(viewedMovies, groupLikedMovies, user.getCountry(), user.getStreamingServices(), genres, start, end);
        if(retList.size() > 15){
            return retList;
        }
        else{
            retList.addAll(movieRepository.fetchMovies(viewedMovies, user.getCountry(), user.getStreamingServices(), genres, start,end));
        }
        return retList;
    }
    public List<Movie> fetchCast(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, User user, List<Genre> genres, Date start, Date end, Set<String> cast){
        System.out.println("Here 2");
        List<Movie> retList;
        retList = movieRepository.fetchMoviesWithGroupCast(viewedMovies, groupLikedMovies, user.getCountry(),user.getStreamingServices(),genres, cast, start, end);
        if(retList.size() > 15){
            return retList;
        }
        else{
            retList.addAll(movieRepository.fetchMoviesCast(viewedMovies, user.getCountry(), user.getStreamingServices(), genres, start,end,cast));
        }
        return retList;
    }
    public List<Movie> fetchDirector(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, User user, List<Genre> genres, Date start, Date end, Set<String> directors){
        System.out.println("Here 3");
        List<Movie> retList;
        retList = movieRepository.fetchMoviesWithGroupDirector(viewedMovies, groupLikedMovies, user.getCountry() ,user.getStreamingServices(), genres, directors, start, end);
        if(retList.size() > 15){
            return retList;
        }
        else{
            retList.addAll(movieRepository.fetchMoviesDirector(viewedMovies, user.getCountry(), user.getStreamingServices(), genres, start, end, directors));
        }
        return retList;
    }
    public List<Movie> fetchCastDirector(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, User user, List<Genre> genres, Date start, Date end, Set<String> directors, Set<String> cast){
        System.out.println("Here 4");
        List<Movie> retList;
        retList = movieRepository.fetchMoviesWithGroupCastDirector(viewedMovies, groupLikedMovies, user.getCountry(),user.getStreamingServices(), genres, cast, directors, start, end);
        if(retList.size() > 15){
            return retList;
        }
        else{
            retList.addAll(movieRepository.fetchMoviesCastDirector(viewedMovies, user.getCountry(), user.getStreamingServices(), genres, start, end, cast, directors));
        }
        return retList;
    }


}
