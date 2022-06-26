package com.grono.moviematchbackend.controller;

import com.grono.moviematchbackend.Response;
import com.grono.moviematchbackend.model.group.Code.Code;
import com.grono.moviematchbackend.model.group.request.CreateGroupBody;
import com.grono.moviematchbackend.model.group.Group;
import com.grono.moviematchbackend.model.group.request.JoinGroupBody;
import com.grono.moviematchbackend.model.group.request.LeaveGroupBody;
import com.grono.moviematchbackend.model.movie.Movie;
import com.grono.moviematchbackend.service.GroupService;
import com.grono.moviematchbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/group")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;


    @GetMapping("/get/code")
    public Code generateCode(){
        return Code.getInstance();
    }

    @PostMapping("/add")
    public void createGroup(@RequestBody CreateGroupBody body, @RequestHeader(value = "Auth") String authentication){
        System.out.println("Name: " + body.getGroupName());
        if(!userService.checkSession(authentication)) Response.unauthorized();
        int status = groupService.createGroup(body.getGroupName(), body.getUsername());
        if(status == 1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(status == 2){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable String id, @RequestHeader(value = "Auth") String authentication){
        if(!userService.checkSession(authentication)) Response.unauthorized();
        return groupService.getGroup(id);
    }

    @PostMapping("/leave")
    public HttpStatus leaveGroup(@RequestBody LeaveGroupBody body, @RequestHeader(value = "Auth") String authentication){
        if(!userService.checkSession(authentication)) Response.unauthorized();
        int lg = groupService.leaveGroup(body);
        if(lg == 0){
            return HttpStatus.OK;
        }
        else{
            System.out.println("Leave Group error " + lg);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @PostMapping("/join")
    public HttpStatus joinGroup(@RequestBody JoinGroupBody body, @RequestHeader(value = "Auth") String authentication){
        if(!userService.checkSession(authentication)) Response.unauthorized();
        int response = groupService.joinGroup(body);
        System.out.println("Response: " + response);
        if (response == 0){
            return HttpStatus.OK;
        }
        else if(response == 1){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if(response == 2){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        else if(response == 3){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        else if(response == 4){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/get/{groupId}")
    public Set<Integer> getGroupLikedMovies(@PathVariable(value = "groupId") String groupId, @RequestHeader(value = "Auth") String authentication){
        if(!userService.checkSession(authentication)) Response.unauthorized();
        return groupService.getGroupLikedMovies(groupId);
    }
}
