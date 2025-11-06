package com.aagnes78.cinemagoer.locations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CinemaAlreadyExistsException extends RuntimeException {
    public CinemaAlreadyExistsException() {
        super("Cinema already exists");
    }
}
