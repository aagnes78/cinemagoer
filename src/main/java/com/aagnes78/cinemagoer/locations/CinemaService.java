package com.aagnes78.cinemagoer.locations;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    Optional<Cinema> findById(long id) {
        return cinemaRepository.findById(id);
    }

    List<Cinema> findAll() {
        return cinemaRepository.findAll();
    }

    List<Cinema> findAllOrderByCountry() {
        return cinemaRepository.findAllOrderByCountry();
    }

    @Transactional
    long create(Cinema cinema) {
        try {
            return cinemaRepository.create(cinema);
        } catch (DuplicateKeyException ex) {
            throw new CinemaAlreadyExistsException();
        } catch (DataIntegrityViolationException ex) {
            throw new CityNotFoundException();
        }
    }

}
