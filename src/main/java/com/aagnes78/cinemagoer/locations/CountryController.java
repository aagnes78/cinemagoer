package com.aagnes78.cinemagoer.locations;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("countries")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("{id}")
    public Country findById(@PathVariable long id) {
        return countryService.findById(id)
                .orElseThrow(CountryNotFoundException::new);
    }

    @GetMapping
    public List<Country> findAll() {
        return countryService.findAll();
    }
}
