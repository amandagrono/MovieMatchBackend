package com.grono.moviematchbackend.service;

import com.grono.moviematchbackend.Constants;
import com.grono.moviematchbackend.model.user.request.LoginBody;
import com.grono.moviematchbackend.model.user.User;
import com.grono.moviematchbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Map<String, String> addUser(User user){
        Map<String, String> map = new HashMap<>();
        try{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            map.put("token",userRepository.insert(user).getToken().get(0));
            map.put("status", "200");
        }
        catch (Exception e){
            if(e instanceof DuplicateKeyException){
                map.put("token", null);
                map.put("status", "409");
            }
            else{
                e.printStackTrace();
                map.put("token", null);
                map.put("Status", "500");
            }
        }
        return map;

    }
    public Map<String, String> login(LoginBody loginBody){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<User> user = userRepository.findUserByUsername(loginBody.getUsername());
        Map<String, String> returnMap = new HashMap<>();
        if(user.isPresent()){
            if(encoder.matches(loginBody.getPassword(), user.get().getPassword())){
                String token = user.get().generateToken();
                returnMap.put("token", token);
                returnMap.put("status", "200");
                returnMap.put("message", "Successfully Logged In");
                if(user.get().getToken().size() > 3){
                    user.get().getToken().remove(0);
                }
                userRepository.save(user.get());
                return returnMap;
            }
            else{
                returnMap.put("status", "401");
                returnMap.put("message", "Password Incorrect");
            }
        }
        else{
            returnMap.put("status", "400");
            returnMap.put("message", "User Not Found");
        }
        return returnMap;

    }

    public boolean deleteUser(LoginBody body){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<User> user = userRepository.findUserByUsername(body.getUsername());
        if(user.isPresent()){
            if(encoder.matches(body.getPassword(), user.get().getPassword())){
                userRepository.delete(user.get());
                //groupRepository, delete member from all associated groups.
                Optional<User> deletedUser = userRepository.findUserByUsername(body.getUsername());
                return deletedUser.isEmpty();
            }
        }
        return false;
    }
    public boolean checkSession(String username, String token){
        Optional<User> user = userRepository.findUserByUsername(username);
        if(!Constants.securityEnabled){
            return true;
        }
        return user.map(value -> value.getToken().contains(token)).orElse(false);
    }
    public boolean checkSession(String authentication){
        return checkSession(authentication.split("\\|")[0], authentication.split("\\|")[1]);
    }

    public boolean addGroupToUser(String groupId, String username){
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isPresent() && !user.get().getGroups().contains(groupId)){
            user.get().getGroups().add(groupId);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }


}
