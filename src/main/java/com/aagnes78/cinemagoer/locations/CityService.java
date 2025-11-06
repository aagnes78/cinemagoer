package com.aagnes78.cinemagoer.locations;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    Optional<City> findById(long id) {
        return cityRepository.findById(id);
    }

    List<City> findAll() {
        return cityRepository.findAll();
    }

    @Transactional
    long create(City city) {
        try {
            return cityRepository.create(city);
        } catch (DuplicateKeyException ex) {
            throw new CityAlreadyExistsException();
        } catch (DataIntegrityViolationException ex) {
            throw new CountryNotFoundException();
        }
    }

    @Transactional
    void updateName(long id, String newName) {
        try {
            cityRepository.updateName(id, newName);
        } catch (DuplicateKeyException ex) {
            throw new CityAlreadyExistsException();
        }
    }
}
