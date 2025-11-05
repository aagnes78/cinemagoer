package com.aagnes78.cinemagoer.locations;

public class Country {
    private final long id;
    private final String countryName;
    public Country(long id, String countryName) {
        this.id = id;
        this.countryName = countryName;
    }

    public long getId() {
        return id;
    }

    public String getCountryName() {
        return countryName;
    }
}
