package com.aagnes78.cinemagoer.films;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
class FilmAlreadyExistsException extends RuntimeException {
    public FilmAlreadyExistsException() {
        super("Film already exists");
    }
}
