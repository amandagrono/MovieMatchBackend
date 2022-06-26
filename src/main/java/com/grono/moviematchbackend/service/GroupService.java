package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.Util;
import com.grono.moviematchbackend.model.group.request.CreateGroupBody;
import com.grono.moviematchbackend.model.group.Group;
import com.grono.moviematchbackend.model.group.request.JoinGroupBody;
import com.grono.moviematchbackend.model.group.request.LeaveGroupBody;
import com.grono.moviematchbackend.model.user.User;
import com.grono.moviematchbackend.repository.GroupRepository;
import com.grono.moviematchbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    public int joinGroup(JoinGroupBody body){
        Optional<Group> groupO = groupRepository.findGroupByCode_Name(body.getCode());
        if(groupO.isPresent()){
            //code not expired
            if(groupO.get().getCode().getExpiry().compareTo(new Date()) > 0){
                groupO.get().setUsers(Util.addElement(groupO.get().getUsers(), body.getUser()));
                groupRepository.save(groupO.get());
                boolean x = userService.addGroupToUser(groupO.get().getId(), body.getUser());
                if(!x) return 3;
                return 0;
            }
            //code expired
            else{
                return 2;
            }
        }
        else{
            return 1;
        }
    }

    public int leaveGroup(LeaveGroupBody body){
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
    public Set<Integer> getGroupLikedMovies(String groupId){
        Optional<Group> group = groupRepository.findGroupById(groupId);
        if(group.isPresent()){
            Group g = group.get();
            Set<User> users = userRepository.findByUsernameIn(g.getUsers());
            List<Set<Integer>> listsOfMovies = users.stream().map(User::getListOfLikedMovies).toList();
            return Util.intersection(listsOfMovies);
        }
        else{
            return null;
        }
    }

}
