package com.grono.moviematchbackend;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Response {
    public static void unauthorized(){
        response(HttpStatus.UNAUTHORIZED);
    }
    public static void forbidden() { response(HttpStatus.FORBIDDEN); }


    private static void response(HttpStatus status){
        throw new ResponseStatusException(status);
    }
}
