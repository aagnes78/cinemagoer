package com.aagnes78.cinemagoer.viewings;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScreeningController {
    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping("screenings/{id}")
    public Screening findById(@PathVariable long id) {
        return screeningService.findById(id)
                .orElseThrow(ScreeningNotFoundException::new);
    }

    @GetMapping("screenings/{id}/fullInfo")
    public ScreeningWithIdsAndNames findFullInfoById(@PathVariable long id) {
        return screeningService.findByIdAllInfo(id)
                .orElseThrow(ScreeningNotFoundException::new);
    }

    @GetMapping("screenings")
    public List<Screening> findAll() {
        return screeningService.findAll();
    }

    @GetMapping("screenings/fullInfo")
    public List<ScreeningWithIdsAndNames> findAllFullInfo() {
        return screeningService.findAllWithAllInfo();
    }

    @GetMapping("cinemas/{id}/screenings")
    public List<Screening> findAllByCinemaId(@PathVariable long id) {
        return screeningService.findAllByCinemaId(id);
    }

    @GetMapping("cinemas/{id}/screenings/fullInfo")
    public List<ScreeningWithIdsAndNames> findFullInfoByCinemaId(@PathVariable long id) {
        return screeningService.findAllInfoByCinemaId(id);
    }

    @GetMapping("films/{id}/screenings/fullInfo")
    public List<ScreeningWithIdsAndNames> findFullInfoByFilmId(@PathVariable long id) {
        return screeningService.findAllInfoByFilmId(id);
    }

    @PostMapping("screenings")
    public long addScreening(@RequestBody @Valid NewScreening newScreening) {
        return screeningService.create(newScreening);
    }

}
