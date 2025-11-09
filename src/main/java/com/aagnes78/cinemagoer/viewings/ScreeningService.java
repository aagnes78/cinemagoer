package com.aagnes78.cinemagoer.viewings;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ScreeningService {
    private final ScreeningRepository screeningRepository;

    public ScreeningService(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    Optional<Screening> findById(long id) {
        return screeningRepository.findById(id);
    }

    Optional<ScreeningWithIdsAndNames> findByIdAllInfo(long id) {
        return screeningRepository.findByIdAllInfo(id);
    }

    List<Screening> findAll() {
        return screeningRepository.findAll();
    }

    List<ScreeningWithIdsAndNames> findAllWithAllInfo() {
        return screeningRepository.findAllWithAllInfo();
    }

    List<Screening> findAllByFilmId(long filmId) {
        return screeningRepository.findAllByFilmId(filmId);
    }

    List<ScreeningWithIdsAndNames> findAllInfoByFilmId(long filmId) {
        return screeningRepository.findAllInfoByFilmId(filmId);
    }

    List<Screening> findAllByCinemaId(long cinemaId) {
        return screeningRepository.findAllByCinemaId(cinemaId);
    }

    List<ScreeningWithIdsAndNames> findAllInfoByCinemaId(long cinemaId) {
        return screeningRepository.findAllInfoByCinemaId(cinemaId);
    }

    @Transactional
    long create(NewScreening newScreening) {
        try {
            var screening = new Screening(0, newScreening.filmId(),  newScreening.cinemaId(),
                    newScreening.startDate(), newScreening.endDate());
            return screeningRepository.create(screening);
        } catch (EndDateBeforeStartDateException ex) {
            throw new EndDateBeforeStartDateException();
        }
        catch (DuplicateKeyException ex) {
            throw new ScreeningAlreadyExistsException();
        } catch (DataIntegrityViolationException ex) {
            throw new ForeignKeyNotFoundException();
        }
    }
}