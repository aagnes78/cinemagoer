package com.aagnes78.cinemagoer.viewings;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScreeningNotFoundException extends RuntimeException {
    public ScreeningNotFoundException() {
        super("Screening not found");
    }
}
