package com.grono.moviematchbackend.service;

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
                System.out.println("Here 1");
                retList = movieRepository.fetchMoviesWithGroup(viewedMovies, groupLikedMovies, user.get().getCountry(), user.get().getStreamingServices(), body.getGenres(), start, end);
                if(retList.size() > 15){
                    return retList;
                }
                else{
                    retList.addAll(movieRepository.fetchMovies(viewedMovies, user.get().getCountry(), user.get().getStreamingServices(), body.getGenres(), start,end));
                }
            }
            else if(!body.getCast().isEmpty() && body.getDirectors().isEmpty()){
                System.out.println("Here 2");
                retList = movieRepository.fetchMoviesWithGroupCast(viewedMovies, groupLikedMovies, user.get().getCountry(),user.get().getStreamingServices(),body.getGenres(), body.getCast(), start, end);
                if(retList.size() > 15){
                    return retList;
                }
                else{
                    retList.addAll(movieRepository.fetchMoviesCast(viewedMovies, user.get().getCountry(), user.get().getStreamingServices(), body.getGenres(), start,end,body.getCast()));
                }
            }
            else if(body.getCast().isEmpty() && !body.getDirectors().isEmpty()){
                System.out.println("Here 3");
                retList = movieRepository.fetchMoviesWithGroupDirector(viewedMovies, groupLikedMovies, user.get().getCountry() ,user.get().getStreamingServices(),body.getGenres(), body.getDirectors(), start, end);
                if(retList.size() > 15){
                    return retList;
                }
                else{
                    retList.addAll(movieRepository.fetchMoviesDirector(viewedMovies, user.get().getCountry(), user.get().getStreamingServices(), body.getGenres(), start, end, body.getDirectors()));
                }
            }
            else{
                System.out.println("Here 4");
                retList = movieRepository.fetchMoviesWithGroupCastDirector(viewedMovies, groupLikedMovies, user.get().getCountry(),user.get().getStreamingServices(), body.getGenres(), body.getCast() , body.getDirectors(), start, end);
                if(retList.size() > 15){
                    return retList;
                }
                else{
                    retList.addAll(movieRepository.fetchMoviesCastDirector(viewedMovies, user.get().getCountry(), user.get().getStreamingServices(), body.getGenres(), start, end, body.getCast(), body.getDirectors()));
                }
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


}
