package com.grono.moviematchbackend.model.group.request;

import lombok.Data;

@Data
public class CreateGroupBody {
    String username;
    String token;
    String groupName;
}
