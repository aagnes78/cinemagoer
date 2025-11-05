package com.aagnes78.cinemagoer.locations;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CountryRepository {
    private final JdbcClient jdbcClient;
    public CountryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    Optional<Country> findById(long id) {
        var sql = """
                SELECT id, countryName
                FROM countries
                WHERE id = ?;
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Country.class)
                .optional();
    }

    List<Country> findAll() {
        var sql = """
                SELECT id, countryName
                FROM countries
                ORDER BY countryName;
                """;
        return jdbcClient.sql(sql)
                .query(Country.class)
                .list();
    }


}
