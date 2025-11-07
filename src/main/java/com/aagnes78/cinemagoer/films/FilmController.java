package com.aagnes78.cinemagoer.films;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("{id}")
    public Film findById(@PathVariable long id) {
        return filmService.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(id));
    }

    @PostMapping
    public long addFilm(@RequestBody @Valid NewFilm newFilm) {
        return filmService.create(newFilm);
    }

    @PostMapping("/noLink")
    public long addFilmNoLink(@RequestBody @NotBlank String title) {
        return filmService.createWithTitleOnly(title);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        filmService.delete(id);
    }

    @PutMapping("{id}/updateTitle")
    public void updateTitle(@PathVariable long id, @RequestBody @NotBlank String title) {
        filmService.updateTitle(id, title);
    }

    @PutMapping("{id}/updateLink")
    public void updateLink(@PathVariable long id, @RequestBody @NotBlank String link) {
        filmService.updateImdb(id, link);
    }

}
