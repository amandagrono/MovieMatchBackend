package com.grono.moviematchbackend.controller;

import com.grono.moviematchbackend.model.Code.Code;
import com.grono.moviematchbackend.model.group.CreateGroupBody;
import com.grono.moviematchbackend.model.group.Group;
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

    @PostMapping("add")
    public void createGroup(@RequestBody CreateGroupBody body){
        System.out.println("Name: " + body.getGroupName());
        int status = groupService.createGroup(body.getGroupName(), body.getUsername());
        if(status == 1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable String id){
        return groupService.getGroup(id);
    }
}
