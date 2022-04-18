package com.grono.moviematchbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grono.moviematchbackend.model.enums.StreamingService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Country {
    private String country;
    @JsonProperty("streaming_services")
    private List<StreamingService> streamingServices;
    @JsonProperty("age_rating")
    private String ageRating;
}
