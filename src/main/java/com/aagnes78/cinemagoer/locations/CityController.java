package com.aagnes78.cinemagoer.locations;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("{id}")
    public City findById(@PathVariable("id") long id) {
        return cityService.findById(id)
                .orElseThrow(CityNotFoundException::new);
    }

    @GetMapping
    public List<City> findAll() {
        return cityService.findAll();
    }

    @PostMapping
    public long addCity(@RequestBody @Valid City city) {
        return cityService.create(city);
    }

    @PutMapping("{id}/name")
    public void changeName(@PathVariable long id, @RequestBody @NotBlank String newName) {
        cityService.updateName(id, newName);
    }
}
