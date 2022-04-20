package com.grono.moviematchbackend.repository;

import com.grono.moviematchbackend.model.group.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group, String> {

    Optional<Group> findGroupById(String id);
}
