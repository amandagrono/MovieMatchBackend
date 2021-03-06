package com.grono.moviematchbackend.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grono.moviematchbackend.model.enums.StreamingService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Document
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    @JsonProperty("username")
    @NotBlank(message = "username must not be null")
    private String username;

    private List<String> token;

    //will eventually hash the password!
    private String password;

    private Integer age;

    @NotNull
    private Boolean netflix;
    @NotNull
    private Boolean hulu;
    @NotNull
    private Boolean amazon;
    @NotNull
    @JsonProperty("disney_plus")
    private Boolean disneyPlus;

    @NotNull
    @JsonProperty("hbo")
    private Boolean hbo;
    @NotNull
    @JsonProperty("paramount_plus")
    private Boolean paramountPlus;
    @NotNull
    @JsonProperty("peacock")
    private Boolean peacock;
    @NotNull
    @JsonProperty("showtime")
    private Boolean showtime;
    @NotNull
    private Set<Integer> listOfLikedMovies;
    @NotNull
    private Set<Integer> listOfDislikedMovies;
    @NotNull
    private Set<Integer> listOfLikedTvShows;
    @NotNull
    private Set<Integer> listOfDislikedTvShows;
    @NotNull
    private List<String> groups;

    @NotNull
    private String country;

    public User(String username, String password, Integer age, Boolean netflix, Boolean hulu, Boolean amazon, Boolean disneyPlus,Boolean hbo, Boolean paramountPlus, Boolean peacock, Boolean showtime, String country) {
        this.username = username;
        this.token = List.of(UUID.randomUUID().toString());
        this.password = password;
        this.age = age;
        this.netflix = netflix;
        this.hulu = hulu;
        this.amazon = amazon;
        this.disneyPlus = disneyPlus;
        this.hbo = hbo;
        this.paramountPlus = paramountPlus;
        this.peacock = peacock;
        this.showtime = showtime;
        this.listOfLikedMovies = Set.of();
        this.listOfDislikedMovies = Set.of();
        this.listOfLikedTvShows = Set.of();
        this.listOfDislikedTvShows = Set.of();
        this.groups = List.of();
        this.country = country;
    }

    public static User getInstance(User user){
        return new User(user.username, user.password, user.age, user.netflix, user.hulu, user.amazon, user.disneyPlus,user.hbo,user.paramountPlus, user.peacock, user.showtime, user.country);

    }

    public String generateToken(){
        String newToken = UUID.randomUUID().toString();
        token.add(newToken);
        return newToken;
    }

    public List<StreamingService> getStreamingServices(){
        List<StreamingService> list = new ArrayList<>();
        if(netflix) list.add(StreamingService.Netflix);
        if(hulu) list.add(StreamingService.Hulu);
        if(amazon) list.add(StreamingService.AmazonPrime);
        if(disneyPlus) list.add(StreamingService.DisneyPlus);
        if(hbo) list.add(StreamingService.HBOMax);
        if(paramountPlus) list.add(StreamingService.ParamountPlus);
        if(peacock) list.add(StreamingService.PeacockPremium);
        if(showtime) list.add(StreamingService.Showtime);

        return list;
    }
}
