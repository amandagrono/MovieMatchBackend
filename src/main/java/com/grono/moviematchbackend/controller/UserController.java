package com.grono.moviematchbackend.controller;

import com.grono.moviematchbackend.model.user.request.CheckSessionBody;
import com.grono.moviematchbackend.model.user.request.LoginBody;
import com.grono.moviematchbackend.model.user.User;
import com.grono.moviematchbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

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

    @GetMapping("login")
    @ResponseBody
    public Map<String, String> login(@RequestBody LoginBody loginBody){
        Map<String, String> rtnMap = new LinkedHashMap<>();
        Map<String, String> map = userService.login(loginBody);

        if(map.get("status").equals("200")){
            rtnMap.put("token", map.get("token"));
        }
        else if(map.get("status").equals("401")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        else if(map.get("status").equals("400")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return rtnMap;
    }

    @GetMapping("checkSession")
    public HttpStatus checkSession(@RequestBody CheckSessionBody body){
        if(userService.checkSession(body.getUsername(), body.getToken())){
            return HttpStatus.OK;
        }
        return HttpStatus.UNAUTHORIZED;
    }

    @DeleteMapping("deleteUser")
    public HttpStatus deleteUser(@RequestBody LoginBody body){
        return userService.deleteUser(body) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

}
