package com.grono.moviematchbackend.model.group.request;

import lombok.Data;

@Data
public class JoinGroupBody {
    private String user;
    private String token;
    private String code;
}
