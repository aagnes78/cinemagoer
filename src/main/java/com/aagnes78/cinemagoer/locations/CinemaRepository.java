package com.aagnes78.cinemagoer.locations;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CinemaRepository {
    private final JdbcClient jdbcClient;

    public CinemaRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    Optional<Cinema> findById(long id) {
        var sql = """
                SELECT id, cinemaName, cityId, identifier
                FROM cinemas
                WHERE id = ?;
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Cinema.class)
                .optional();
    }

    List<Cinema> findAll() {
        var sql = """
                SELECT id, cinemaName, cityId, identifier
                FROM cinemas
                ORDER BY cinemaName;
                """;
        return jdbcClient.sql(sql)
                .query(Cinema.class)
                .list();
    }

    List<Cinema> findAllOrderByCountry() {
        var sql = """
                SELECT cinemas.id, cinemaName, cityId, identifier
                FROM cinemas
                INNER JOIN cities
                ON cinemas.cityId = cities.id
                ORDER BY countryId, cityId, cinemaName;
                """;
        return jdbcClient.sql(sql)
                .query(Cinema.class)
                .list();
    }

    long create(Cinema cinema) {
        var sql = """
                INSERT INTO cinemas (cinemaName, cityId, identifier)
                VALUES (?, ?, ?);
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(cinema.getCinemaName(), cinema.getCityId(), cinema.getIdentifier())
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }
}
