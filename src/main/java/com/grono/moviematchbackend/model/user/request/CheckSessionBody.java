package com.grono.moviematchbackend.model.user.request;

import lombok.Data;

@Data
public class CheckSessionBody {
    private String token;
    private String username;
}
