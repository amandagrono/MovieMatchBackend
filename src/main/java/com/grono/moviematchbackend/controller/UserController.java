package com.grono.moviematchbackend.controller;

import com.grono.moviematchbackend.model.user.User;
import com.grono.moviematchbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("add")
    public String addUser(@RequestBody User user){
        Map<String,String> uuid = userService.addUser(User.getInstance(user));
        if(uuid.get("status").equals("200")){
            return uuid.get("token");
        }
        else if(uuid.get("status").equals("409")){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
