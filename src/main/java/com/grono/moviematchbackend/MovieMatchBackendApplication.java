package com.grono.moviematchbackend;

import com.grono.moviematchbackend.model.Movie;
import com.grono.moviematchbackend.model.enums.Genre;
import com.grono.moviematchbackend.model.enums.StreamingService;
import com.grono.moviematchbackend.repository.MovieRepository;
import com.mongodb.DuplicateKeyException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class MovieMatchBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieMatchBackendApplication.class, args);
    }

    /*@Bean
    CommandLineRunner runner(MovieRepository repository){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        return args -> {
            Movie movie = new Movie(
                    123L,
                    "Spider-Man: No Way Home",
                    "YOOOOOO THREE SPIDERS",
                    List.of(Genre.Action, Genre.Comedy, Genre.Fantasy, Genre.Adventure),
                    List.of(StreamingService.DisneyPlus, StreamingService.Showtime),
                    "https://image.tmdb.org/t/p/w500/nogV4th2P5QWYvQIMiWHj4CFLU9.jpg",
                    3453.23d,
                    "Jon Watts",
                    List.of("Tom Holland", "Andrew Garfield", "Toby Maguire"),
                    simpleDateFormat.parse("20211216"),
                    "tt10872600",
                    "movie",
                    "PG-13");
            try{
                System.out.println("Here");
                repository.insert(movie);
            }
            catch (Exception e){
                System.out.println("Duplicate Key!");
            }

        };*/



}
