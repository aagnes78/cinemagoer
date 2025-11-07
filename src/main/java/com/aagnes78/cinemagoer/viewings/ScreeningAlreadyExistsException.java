package com.aagnes78.cinemagoer.viewings;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ScreeningAlreadyExistsException extends RuntimeException {
    public ScreeningAlreadyExistsException() {
        super("Screening already exists");
    }
}
