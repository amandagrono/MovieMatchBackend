package com.grono.moviematchbackend.repository;

import com.grono.moviematchbackend.model.group.Group;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group, String> {

    Optional<Group> findGroupById(String id);


    Optional<Group> findGroupByCode_Name(String code);
}
