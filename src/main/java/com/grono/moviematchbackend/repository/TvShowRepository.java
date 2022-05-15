package com.grono.moviematchbackend.repository;

import com.grono.moviematchbackend.model.tvshow.TVShow;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TvShowRepository extends MongoRepository<TVShow, String> {
}
