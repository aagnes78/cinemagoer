package com.aagnes78.cinemagoer.viewings;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ForeignKeyNotFoundException extends RuntimeException {
    public ForeignKeyNotFoundException() {
        super("Cinema or Film not found");
    }
}
