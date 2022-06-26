package com.grono.moviematchbackend.repository;

import com.grono.moviematchbackend.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByUsername(String username);

    Set<User> findByUsernameIn(List<String> users);
}
