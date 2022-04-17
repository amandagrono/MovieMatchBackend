package com.grono.moviematchbackend.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StreamingService {
    @JsonProperty("Netflix")
    Netflix,
    @JsonProperty("Hulu")
    Hulu,
    @JsonProperty("Amazon Prime Video")
    AmazonPrime,
    @JsonProperty("HBO Max")
    HBOMax,
    @JsonProperty("Disney Plus")
    DisneyPlus,
    @JsonProperty("Paramount+ Amazon Channel")
    ParamountPlus,
    @JsonProperty("Peacock Premium")
    PeacockPremium,
    @JsonProperty("Showtime")
    Showtime
}
