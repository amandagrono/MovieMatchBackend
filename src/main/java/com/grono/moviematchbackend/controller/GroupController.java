package com.grono.moviematchbackend.controller;

import com.grono.moviematchbackend.model.group.Code.Code;
import com.grono.moviematchbackend.model.group.request.CreateGroupBody;
import com.grono.moviematchbackend.model.group.Group;
import com.grono.moviematchbackend.model.group.request.JoinGroupBody;
import com.grono.moviematchbackend.model.group.request.LeaveGroupBody;
import com.grono.moviematchbackend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/group")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;


    @GetMapping("/get/code")
    public Code generateCode(){
        return Code.getInstance();
    }

    @PostMapping("/add")
    public void createGroup(@RequestBody CreateGroupBody body){
        System.out.println("Name: " + body.getGroupName());
        int status = groupService.createGroup(body.getGroupName(), body.getUsername());
        if(status == 1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(status == 2){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable String id){
        return groupService.getGroup(id);
    }

    @PostMapping("/leave")
    public HttpStatus leaveGroup(@RequestBody LeaveGroupBody body){
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
    public HttpStatus joinGroup(@RequestBody JoinGroupBody body){
        int response = groupService.joinGroup(body);
        if (response == 0){
            return HttpStatus.OK;
        }
        else if(response == 1){
            return HttpStatus.NOT_FOUND;
        }
        else if(response == 2){
            return HttpStatus.FORBIDDEN;
        }
        else if(response == 4){
            return HttpStatus.UNAUTHORIZED;
        }
        else{
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }
}
