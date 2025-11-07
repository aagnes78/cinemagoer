package com.aagnes78.cinemagoer.screenings;

import com.aagnes78.cinemagoer.viewings.Screening;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ScreeningTest {
    @Test
    void screeningCreatedWithOneDateTwice() {
        var now = LocalDateTime.now();
        var screening = new Screening(1L, 1L, 1L, now, now);
    }

    @Test
    void screeningCreatedWithTwoCorrectDates() {
        var now = LocalDateTime.now();
        var screening = new Screening(1L, 1L, 1L, now.minusDays(1), now);
    }

    @Test
    void screeningNotCreatedIfEndDateIsEarlierThanStartDate() {
        var start = LocalDateTime.now();
        var end = LocalDateTime.now().minusDays(1);
        assertThatIllegalArgumentException().isThrownBy(() ->
                new Screening(1L, 2L, 3L, start, end));
    }

    @Test
    void isExactTimeKnownIsTrueIfStartAndEndAreTheSame() {
        var start = LocalDateTime.of(2025, 10, 31, 20, 30);
        var end = LocalDateTime.of(2025, 10, 31, 20, 30);
        var screening = new Screening(1L, 1L, 1L, start, end);
        assertThat(screening.isExactTimeKnown()).isTrue();
    }

    @Test
    void isExactTimeKnownReturnsFalseIfStartAndEndDiffer() {
        var start = LocalDateTime.of(2025, 10, 31, 0, 0);
        var end = LocalDateTime.of(2025, 10, 31, 23, 59);
        var screening = new Screening(1L, 1L, 1L, start, end);
        assertThat(screening.isExactTimeKnown()).isFalse();
    }

    @Test
    void isExactDayKnownIsTrueIfStartAndEndAreTheSame() {
        var start = LocalDateTime.of(2025, 10, 31, 20, 0);
        var screening = new Screening(1L, 1L, 1L, start, start);
        assertThat(screening.isExactDayKnown()).isTrue();
    }

    @Test
    void isExactDayKnownReturnsTrueIfStartAndDayAreOnTheSameDay() {
        var start = LocalDateTime.of(2025, 10, 31, 0, 0);
        var end = LocalDateTime.of(2025, 10, 31, 23, 59);
        var screening = new Screening(1L, 1L, 1L, start, end);
        assertThat(screening.isExactDayKnown()).isTrue();
    }

    @Test
    void isExactTimeKnownReturnsFalseIfStartAndEndAreOnDifferentDays() {
        var start = LocalDateTime.of(2025, 10, 1, 0, 0);
        var end = LocalDateTime.of(2025, 10, 31, 23, 59);
        var screening = new Screening(1L, 1L, 1L, start, end);
        assertThat(screening.isExactDayKnown()).isFalse();
    }
}
