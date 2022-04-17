package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.model.Group;
import com.grono.moviematchbackend.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public int createGroup(String name){
        Group group = new Group(name);
        try{
            groupRepository.insert(group);
        }
        catch (Exception e){
            e.printStackTrace();
            return 1;
        }
        return 0;

    }
}
