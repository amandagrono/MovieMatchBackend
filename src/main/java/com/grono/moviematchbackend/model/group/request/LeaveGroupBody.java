package com.grono.moviematchbackend.model.group.request;

import lombok.Data;

@Data
public class LeaveGroupBody {
    String username;
    String token;
    String groupId;
}
