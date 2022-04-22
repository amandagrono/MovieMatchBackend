package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.model.group.request.CreateGroupBody;
import com.grono.moviematchbackend.model.group.Group;
import com.grono.moviematchbackend.model.group.request.LeaveGroupBody;
import com.grono.moviematchbackend.model.user.User;
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
            if(userService.addGroupToUser(group.getId(), user)){
                return 0;
            }
            else{
                return 2;
            }

        }
        catch (Exception e){
            e.printStackTrace();
            return 1;
        }

    }
    public Group getGroup(String id){
        Optional<Group> group = groupRepository.findGroupById(id);
        return group.orElse(null);
    }

    public int leaveGroup(LeaveGroupBody body){
        if(!userService.checkSession(body.getUsername(), body.getToken())){
            return 4;
        }
        Optional<Group> groupO = groupRepository.findGroupById(body.getGroupId());
        if(groupO.isPresent()){
            boolean g = groupO.get().getUsers().remove(body.getUsername());
            if(g){
                Optional<User> userO = userRepository.findUserByUsername(body.getUsername());
                if(userO.isPresent()){
                    boolean u = userO.get().getGroups().remove(groupO.get().getId());
                    if(u){
                        groupRepository.save(groupO.get());
                        userRepository.save(userO.get());
                        return 0;
                    }
                }
                else{
                    return 1;
                }
            }
            else{
                return 2;
            }
        }
        else{
            return 3;
        }
        return 5;
    }
}
