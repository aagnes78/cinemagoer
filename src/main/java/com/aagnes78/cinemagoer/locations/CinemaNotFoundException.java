package com.aagnes78.cinemagoer.locations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CinemaNotFoundException extends RuntimeException {
    public CinemaNotFoundException() {
        super("Cinema not found");
    }
}
