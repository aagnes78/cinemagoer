package com.aagnes78.cinemagoer.locations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CityAlreadyExistsException extends RuntimeException {
    public CityAlreadyExistsException() {
        super("City already exists");
    }
}
