package com.aagnes78.cinemagoer.locations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Cinema {
    private final long id;
    @NotBlank
    private final String cinemaName;
    @Positive
    private final long cityId;
    private final String identifier;


    public Cinema(long id, String cinemaName, long cityId, String identifier) {
        this.id = id;
        this.cinemaName = cinemaName;
        this.cityId = cityId;
        this.identifier = identifier;
    }

    public long getId() {
        return id;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public long getCityId() {
        return cityId;
    }

    public String getIdentifier() {
        return identifier;
    }
}
