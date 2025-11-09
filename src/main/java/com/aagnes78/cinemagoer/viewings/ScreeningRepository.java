package com.aagnes78.cinemagoer.viewings;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ScreeningRepository {
    private final JdbcClient jdbcClient;

    public ScreeningRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    Optional<Screening> findById(long id) {
        var sql = """
                SELECT id, filmId, cinemaId, startDate, endDate
                FROM screenings
                WHERE id = ?;
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Screening.class)
                .optional();
    }

    Optional<ScreeningWithIdsAndNames> findByIdAllInfo(long id) {
        var sql = """
                SELECT s.id, filmId, title, cinemaId, cinemaName, startDate, endDate
                FROM screenings AS s
                INNER JOIN films
                ON s.filmId = films.id
                INNER JOIN cinemas
                ON s.cinemaId = cinemas.id
                WHERE s.id = ?;
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(ScreeningWithIdsAndNames.class)
                .optional();
    }

    List<Screening> findAll() {
        var sql = """
                SELECT id, filmId, cinemaId, startDate, endDate
                FROM screenings
                """;
        return jdbcClient.sql(sql)
                .query(Screening.class)
                .list();
    }

    List<ScreeningWithIdsAndNames> findAllWithAllInfo() {
        var sql = """
                SELECT s.id, filmId, title, cinemaId, cinemaName, startDate, endDate
                FROM screenings AS s
                INNER JOIN films
                ON s.filmId = films.id
                INNER JOIN cinemas
                ON s.cinemaId = cinemas.id;
                """;
        return jdbcClient.sql(sql)
                .query(ScreeningWithIdsAndNames.class)
                .list();
    }

    List<Screening> findAllByFilmId(long filmId) {
        var sql = """
                SELECT id, filmId, cinemaId, startDate, endDate
                FROM screenings
                WHERE filmId = ?;
                """;
        return jdbcClient.sql(sql)
                .param(filmId)
                .query(Screening.class)
                .list();
    }

    List<ScreeningWithIdsAndNames> findAllInfoByFilmId(long filmId) {
        var sql = """
                SELECT s.id, filmId, title, cinemaId, cinemaName, startDate, endDate
                FROM screenings AS s
                INNER JOIN films
                ON s.filmId = films.id
                INNER JOIN cinemas
                ON s.cinemaId = cinemas.id
                WHERE filmId = ?;
                """;
        return jdbcClient.sql(sql)
                .param(filmId)
                .query(ScreeningWithIdsAndNames.class)
                .list();
    }

    List<Screening> findAllByCinemaId(long cinemaId) {
        var sql = """
                SELECT id, filmId, cinemaId, startDate, endDate
                FROM screenings
                WHERE cinemaId = ?;
                """;
        return jdbcClient.sql(sql)
                .param(cinemaId)
                .query(Screening.class)
                .list();
    }

    List<ScreeningWithIdsAndNames> findAllInfoByCinemaId(long cinemaId) {
        var sql = """
                SELECT s.id, filmId, title, cinemaId, cinemaName, startDate, endDate
                FROM screenings AS s
                INNER JOIN films
                ON s.filmId = films.id
                INNER JOIN cinemas
                ON s.cinemaId = cinemas.id
                WHERE cinemaId = ?;
                """;
        return jdbcClient.sql(sql)
                .param(cinemaId)
                .query(ScreeningWithIdsAndNames.class)
                .list();
    }

    long create(Screening screening) {
        var sql = """
                INSERT INTO screenings (filmId, cinemaId, startDate, endDate)
                VALUES (?, ?, ?, ?);
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(screening.getFilmId(),  screening.getCinemaId(), screening.getStartDate(), screening.getEndDate())
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }


}
