package com.grono.moviematchbackend.model.user.request;

import lombok.Data;

@Data
public class LoginBody {
    private String username;
    private String password;

}
