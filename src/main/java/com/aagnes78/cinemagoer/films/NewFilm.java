package com.aagnes78.cinemagoer.films;

import jakarta.validation.constraints.NotBlank;

public record NewFilm(@NotBlank String title, @NotBlank String imdb) {
}
