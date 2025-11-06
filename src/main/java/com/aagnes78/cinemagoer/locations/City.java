package com.aagnes78.cinemagoer.locations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class City {
    private final long id;
    @NotBlank
    private final String cityName;
    @Positive
    private final long countryId;

    public City(long id, String cityName, long countryId) {
        this.id = id;
        this.cityName = cityName;
        this.countryId = countryId;
    }

    public long getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public long getCountryId() {
        return countryId;
    }
}
