package com.aagnes78.cinemagoer.viewings;

import java.time.LocalDateTime;

public record ScreeningTitleCinemaName(long id, String title, String cinemaName,
                                       LocalDateTime startDate, LocalDateTime endDate) {
}
