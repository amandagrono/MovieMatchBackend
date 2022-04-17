package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.model.user.User;
import com.grono.moviematchbackend.repository.UserRepository;
import com.mongodb.MongoWriteException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Map<String, String> addUser(User user){
        Map<String, String> map = new HashMap<>();
        try{
            map.put("token",userRepository.insert(user).getToken());
            map.put("status", "200");
        }
        catch (Exception e){
            if(e instanceof DuplicateKeyException){
                map.put("token", null);
                map.put("status", "409");
            }
            else{
                map.put("token", null);
                map.put("Status", "500");
            }
        }
        return map;

    }
}
