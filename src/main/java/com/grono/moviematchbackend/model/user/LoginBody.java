package com.grono.moviematchbackend.model.user;

import lombok.Data;

@Data
public class LoginBody {
    private String username;
    private String password;

}
