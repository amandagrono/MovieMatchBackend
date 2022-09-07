package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.model.enums.Genre;
import com.grono.moviematchbackend.model.group.Group;
import com.grono.moviematchbackend.model.movie.Movie;
import com.grono.moviematchbackend.model.movie.request.FetchMoviesBody;
import com.grono.moviematchbackend.model.movie.request.ViewMovieBody;
import com.grono.moviematchbackend.model.user.User;
import com.grono.moviematchbackend.repository.GroupRepository;
import com.grono.moviematchbackend.repository.MovieRepository;
import com.grono.moviematchbackend.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    private final MongoTemplate mongoTemplate;

    public List<Movie> fetchAllMovies(){
        return movieRepository.findAll();
    }
    public void delete(String id){
        movieRepository.deleteById(id);
    }
    public void deleteEmpty(){

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

    public Set<Movie> fetch(FetchMoviesBody body){
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

        Set<Movie> retList = new HashSet<>();
        Optional<User> user = userRepository.findUserByUsername(body.getUsername());
        if(user.isPresent()){
            List<Integer> viewedMovies = Stream.concat(user.get().getListOfLikedMovies().stream(), user.get().getListOfDislikedMovies().stream()).toList();
            Set<Integer> groupLikedMovies = getGroupLikedMovies(user.get().getGroups());
            if(body.getType().isEmpty()) body.setType(List.of("movie", "tv"));
            /*if(body.getCast().isEmpty() && body.getDirectors().isEmpty()){
                retList = fetch(viewedMovies, groupLikedMovies, user.get(), body.getGenres(), start, end, body.getType());
            }
            else if(!body.getCast().isEmpty() && body.getDirectors().isEmpty()){
                retList = fetchCast(viewedMovies, groupLikedMovies, user.get(), body.getGenres(), start, end, body.getCast(), body.getType());
            }
            else if(body.getCast().isEmpty() && !body.getDirectors().isEmpty()){
                retList = fetchDirector(viewedMovies, groupLikedMovies, user.get(), body.getGenres(), start, end, body.getDirectors(), body.getType());
            }
            else{
                retList = fetchCastDirector(viewedMovies, groupLikedMovies, user.get(), body.getGenres(), start, end, body.getDirectors(), body.getCast(), body.getType());
            }*/
            retList = fetchMoviesBy(viewedMovies, groupLikedMovies, user.get(), body.getGenres(), start, end, body.getDirectors(), body.getCast(), body.getType());
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

    public List<Movie> fetch(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, User user, List<Genre> genres, Date start, Date end, List<String> type){
        System.out.println("Here 1");
        List<Movie> retList;
        retList = movieRepository.fetchMoviesWithGroup(viewedMovies, groupLikedMovies, user.getCountry(), user.getStreamingServices(), genres, start, end, type);
        if(retList.size() > 15){
            return retList;
        }
        else{
            retList.addAll(movieRepository.fetchMovies(viewedMovies, user.getCountry(), user.getStreamingServices(), genres, start,end, type));
        }
        return retList;
    }
    public List<Movie> fetchCast(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, User user, List<Genre> genres, Date start, Date end, Set<String> cast, List<String> type){
        System.out.println("Here 2");
        List<Movie> retList;
        retList = movieRepository.fetchMoviesWithGroupCast(viewedMovies, groupLikedMovies, user.getCountry(),user.getStreamingServices(),genres, cast, start, end, type);
        if(retList.size() > 15){
            return retList;
        }
        else{
            retList.addAll(movieRepository.fetchMoviesCast(viewedMovies, user.getCountry(), user.getStreamingServices(), genres, start,end,cast, type));
        }
        return retList;
    }
    public List<Movie> fetchDirector(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, User user, List<Genre> genres, Date start, Date end, Set<String> directors, List<String> type){
        System.out.println("Here 3");
        List<Movie> retList;
        retList = movieRepository.fetchMoviesWithGroupDirector(viewedMovies, groupLikedMovies, user.getCountry() ,user.getStreamingServices(), genres, directors, start, end, type);
        if(retList.size() > 15){
            return retList;
        }
        else{
            retList.addAll(movieRepository.fetchMoviesDirector(viewedMovies, user.getCountry(), user.getStreamingServices(), genres, start, end, directors, type));
        }
        return retList;
    }
    public List<Movie> fetchCastDirector(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, User user, List<Genre> genres, Date start, Date end, Set<String> directors, Set<String> cast, List<String> type){
        System.out.println("Here 4");
        List<Movie> retList;
        retList = movieRepository.fetchMoviesWithGroupCastDirector(viewedMovies, groupLikedMovies, user.getCountry(),user.getStreamingServices(), genres, cast, directors, start, end, type);
        if(retList.size() > 15){
            return retList;
        }
        else{
            retList.addAll(movieRepository.fetchMoviesCastDirector(viewedMovies, user.getCountry(), user.getStreamingServices(), genres, start, end, cast, directors, type));
        }
        return retList;
    }
    public Set<Movie> fetchMoviesBy(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, User user, List<Genre> genres, Date start, Date end, Set<String> directors, Set<String> cast, List<String> type){
        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        Criteria groupCriteria = null;
        criteriaList.add(Criteria.where("movieId").nin(viewedMovies));
        if(groupLikedMovies != null && !groupLikedMovies.isEmpty()){
            groupCriteria = Criteria.where("movieId").in(groupLikedMovies);
            criteriaList.add(groupCriteria);
        }
        if(genres != null && !genres.isEmpty()){
            criteriaList.add(Criteria.where("genres").in(genres));
        }
        criteriaList.add(Criteria.where("countries").elemMatch(Criteria.where("country").is(user.getCountry())).and("streamingServices").in(user.getStreamingServices()));
        criteriaList.add(Criteria.where("releaseDate").gte(start).lte(end));
        if(directors != null && !directors.isEmpty()){
            criteriaList.add(Criteria.where("director").in(directors));
        }
        if(cast != null && !cast.isEmpty()){
            criteriaList.add(Criteria.where("cast").in(cast));
        }
        criteriaList.add(Criteria.where("type").in(type));

        criteria.andOperator(criteriaList);
        SampleOperation sampleOperation = Aggregation.sample(25);
        MatchOperation matchOperation = Aggregation.match(criteria);
        TypedAggregation<Movie> aggregation = Aggregation.newAggregation(Movie.class, matchOperation, sampleOperation);
        AggregationResults<Movie> movies = mongoTemplate.aggregate(aggregation, Movie.class);
        Set<Movie> movieList = new HashSet<>(movies.getMappedResults());
        if(movies.getMappedResults().size() < 25){
            sampleOperation = Aggregation.sample(25 - movieList.size());
            criteriaList.remove(groupCriteria);
            criteria = new Criteria();
            criteria.andOperator(criteriaList);
            matchOperation = Aggregation.match(criteria);
            aggregation = Aggregation.newAggregation(Movie.class, matchOperation, sampleOperation);
            movies = mongoTemplate.aggregate(aggregation, Movie.class);
            movieList.addAll(movies.getMappedResults());
        }
        return movieList;
    }

}
