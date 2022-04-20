package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.model.group.Group;
import com.grono.moviematchbackend.repository.GroupRepository;
import com.grono.moviematchbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public int createGroup(String name, String user){
        Group group = new Group(name, List.of(user));
        try{
            groupRepository.insert(group);
            //insert group to user who called the method
            //userService.addGroupToUser(group.getId())

        }
        catch (Exception e){
            e.printStackTrace();
            return 1;
        }
        return 0;

    }
    public Group getGroup(String id){
        Optional<Group> group = groupRepository.findGroupById(id);
        return group.orElse(null);
    }
}
