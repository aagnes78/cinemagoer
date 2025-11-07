package com.aagnes78.cinemagoer.films;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Optional<Film> findById(long id) {
        return filmRepository.findById(id);
    }

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Transactional
    public long create(NewFilm newFilm) {
        var link = newFilm.imdb();
        // TODO: converter from full link to "tt..."
        var film = new Film(0, newFilm.title(), link);
        try {
            return filmRepository.create(film);
        } catch (DuplicateKeyException ex) {
            throw new FilmAlreadyExistsException();
        }
     }

    @Transactional
    public long createWithTitleOnly(String title) {
        return filmRepository.createWithTitleOnly(title);
    }

    @Transactional
    public void updateTitle(long id, String title) {
        filmRepository.updateTitle(id, title);
    }

    @Transactional
    public void updateImdb(long id, String imdb) {
        // TODO: check / chop link as needed
        filmRepository.updateImdb(id, imdb);
    }

    @Transactional
    public void delete(long id) {
        filmRepository.delete(id);
    }

}
