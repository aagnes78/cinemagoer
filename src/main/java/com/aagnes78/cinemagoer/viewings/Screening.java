package com.aagnes78.cinemagoer.viewings;

import java.time.LocalDateTime;

public class Screening {
    private final long id;
    private final long filmId;
    private final long cinemaId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Screening(long id, long filmId, long cinemaId, LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new EndDateBeforeStartDateException();
        }
        this.id = id;
        this.filmId = filmId;
        this.cinemaId = cinemaId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public long getFilmId() {
        return filmId;
    }

    public long getCinemaId() {
        return cinemaId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isExactTimeKnown() {
        return startDate.isEqual(endDate);
    }

    public boolean isExactDayKnown() {
        return startDate.toLocalDate().equals(endDate.toLocalDate());
    }
}
