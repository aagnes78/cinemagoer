package com.aagnes78.cinemagoer.viewings;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record NewScreening(@Positive long filmId, @Positive long cinemaId,
                           @NotNull @PastOrPresent LocalDateTime startDate,
                           @NotNull @PastOrPresent LocalDateTime endDate) {
    public NewScreening(long filmId, long cinemaId, LocalDateTime onlyDate) {
        this(filmId, cinemaId, onlyDate, onlyDate);
    }
}
