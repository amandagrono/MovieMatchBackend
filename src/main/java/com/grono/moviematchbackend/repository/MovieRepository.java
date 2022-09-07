package com.grono.moviematchbackend.repository;

import com.grono.moviematchbackend.model.enums.Genre;
import com.grono.moviematchbackend.model.enums.StreamingService;
import com.grono.moviematchbackend.model.movie.Movie;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.*;

public interface MovieRepository extends MongoRepository<Movie, String> {


    Optional<Movie> findMovieByMovieId(Integer movieId);

    @Query(value = "{countries : {$exists: true, $size: 0}}", delete = true)
    void deleteUnsupportedMovies();

    //@Query("{movieId : {$nin : ?0}, popularity : { $gte : 5}, countries : {$elemMatch : {country : ?1, streamingServices : {$in: ?2} }}, genres : {$in : ?3}}")

    @Aggregation(pipeline = {"{$match : {movieId : {$nin : ?0}, popularity : { $gte : 5}, countries : {$elemMatch : {country : ?1, streamingServices : {$in: ?2} }}, genres : {$in : ?3},releaseDate : {$gte : ?4, $lte : ?5}, type: {$in : ?6}}}",
                             "{$sample : {size : 25}}"
                            })
    List<Movie> fetchMovies(List<Integer> viewedMovies, String country, List<StreamingService> services, List<Genre> genres, Date start, Date end, List<String> type);
    @Aggregation(pipeline = {"{$match : {movieId : {$nin : ?0}, popularity : { $gte : 5}, countries : {$elemMatch : {country : ?1, streamingServices : {$in: ?2} }}, genres : {$in : ?3}, releaseDate : {$gte : ?4, $lte : ?5}, cast : {$in : ?6}, type: {$in : ?7}}}",
                            "{$sample : {size : 25}}"
    })
    List<Movie> fetchMoviesCast(List<Integer> viewedMovies, String country, List<StreamingService> services, List<Genre> genres, Date start, Date end, Set<String> cast, List<String> type);
    @Aggregation(pipeline = {"{$match : {movieId : {$nin : ?0}, popularity : { $gte : 5}, countries : {$elemMatch : {country : ?1, streamingServices : {$in: ?2} }}, genres : {$in : ?3}, releaseDate : {$gte : ?4, $lte : ?5}, director : {$in : ?6}, type: {$in : ?7}}}",
                            "{$sample : {size : 25}}"
    })
    List<Movie> fetchMoviesDirector(List<Integer> viewedMovies, String country, List<StreamingService> services, List<Genre> genres, Date start, Date end, Set<String> directors, List<String> type);
    @Aggregation(pipeline = {"{$match : {movieId : {$nin : ?0}, popularity : { $gte : 5}, countries : {$elemMatch : {country : ?1, streamingServices : {$in: ?2} }}, genres : {$in : ?3}, releaseDate : {$gte : ?4, $lte : ?5}, cast : {$in : ?6}, director: {$in : ?7}, type: {$in : ?8}}}",
                            "{$sample : {size : 25}}"
    })
    List<Movie> fetchMoviesCastDirector(List<Integer> viewedMovies, String country, List<StreamingService> services, List<Genre> genres, Date start, Date end, Set<String> cast, Set<String> directors, List<String> type);
    @Aggregation(pipeline = {"{$match : {$and : [{movieId : {$nin : ?0}}," +
                            "{movieId : {$in : ?1 }}]," +
                            " popularity : { $gte : 5}," +
                            " countries : {$elemMatch : {country : ?2, streamingServices : {$in: ?3} }}," +
                            " genres : {$in : ?4}, releaseDate : {$gte : ?5, $lte : ?6}, type: {$in : ?7}}}",
                            "{$sample : {size : 25}}"
    })
    ArrayList<Movie> fetchMoviesWithGroup(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, String country,List<StreamingService> services, List<Genre> genres, Date start, Date end, List<String> type);

    @Aggregation(pipeline = {"{$match : {$and : [{movieId : {$nin : ?0}}," +
            "{movieId : {$in : ?1 }}]," +
            " popularity : { $gte : 5}," +
            " countries : {$elemMatch : {country : ?2, streamingServices : {$in: ?3} }}," +
            " genres : {$in : ?3}, cast : {$in : ?4}," +
            " releaseDate : {$gte : ?5, $lte : ?6}, type: {$in : ?7}}}",
            "{$sample : {size : 25}}"
    })
    ArrayList<Movie> fetchMoviesWithGroupCast(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, String country,List<StreamingService> services, List<Genre> genres, Set<String> cast, Date start, Date end, List<String> type);

    @Aggregation(pipeline = {"{$match : {$and : [{movieId : {$nin : ?0}}," +
            "{movieId : {$in : ?1 }}]," +
            " popularity : { $gte : 5}," +
            " countries : {$elemMatch : {country : ?2, streamingServices : {$in: ?3} }}," +
            " genres : {$in : ?3}, " +
            " directors : {$in : ?4}, releaseDate : {$gte : ?5, $lte : ?6}," +
            "type : {$in : ?7}}}",
            "{$sample : {size : 25}}"
    })
    ArrayList<Movie> fetchMoviesWithGroupDirector(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, String country,List<StreamingService> services, List<Genre> genres, Set<String> directors, Date start, Date end, List<String> type);

    @Aggregation(pipeline = {"{$match : {$and : [{movieId : {$nin : ?0}}," +
            "{movieId : {$in : ?1 }}]," +
            " popularity : { $gte : 5}," +
            " countries : {$elemMatch : {country : ?2, streamingServices : {$in: ?3} }}," +
            " or : {genres : {$in : ?3}, cast : {$in : ?4}}," +
            " directors : {$in : ?5}, releaseDate : {$gte : ?6, $lte : ?7}, type: {$in : ?8}}}",
            "{$sample : {size : 25}}"
    })
    ArrayList<Movie> fetchMoviesWithGroupCastDirector(List<Integer> viewedMovies, Set<Integer> groupLikedMovies, String country,List<StreamingService> services, List<Genre> genres, Set<String> cast, Set<String> directors, Date start, Date end, List<String> type);

    @Aggregation(pipeline = {"{$match : ?0}", "{$sample : {size : 25}}"})
    ArrayList<Movie> findMoviesBy(Criteria query);

}
