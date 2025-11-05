package com.aagnes78.cinemagoer.locations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    Optional<Country> findById(long id) {
        return countryRepository.findById(id);
    }

    List<Country> findAll() {
        return countryRepository.findAll();
    }
}
