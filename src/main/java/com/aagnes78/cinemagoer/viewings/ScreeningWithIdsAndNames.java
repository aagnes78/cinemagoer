package com.aagnes78.cinemagoer.viewings;

import java.time.LocalDateTime;

public record ScreeningWithIdsAndNames(long id, long filmId, String title, long cinemaId, String cinemaName,
                                       LocalDateTime startDate, LocalDateTime endDate) {
}
