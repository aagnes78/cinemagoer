package com.aagnes78.cinemagoer.locations;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cinemas")
public class CinemaController {
    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("{id}")
    public Cinema findById(@PathVariable long id) {
        return cinemaService.findById(id)
                .orElseThrow(CinemaNotFoundException::new);
    }

    @GetMapping
    public List<Cinema> findAll() {
        return cinemaService.findAll();
    }

    @GetMapping("listByCountry")
    public List<Cinema> listCinemasByCountry() {
        return cinemaService.findAllOrderByCountry();
    }

    @PostMapping
    public long addCinema(@RequestBody @Valid Cinema cinema) {
        return cinemaService.create(cinema);
    }

}
