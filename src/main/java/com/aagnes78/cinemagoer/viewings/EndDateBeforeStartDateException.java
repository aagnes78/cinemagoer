package com.aagnes78.cinemagoer.viewings;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EndDateBeforeStartDateException extends RuntimeException {
    public EndDateBeforeStartDateException() {
        super("End date cannot be before start date");
    }
}
