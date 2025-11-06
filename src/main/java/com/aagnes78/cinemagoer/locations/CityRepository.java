package com.aagnes78.cinemagoer.locations;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CityRepository {
    private final JdbcClient jdbcClient;
    public CityRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    Optional<City> findById(long id) {
        var sql = """
                SELECT id, cityName, countryId
                FROM cities
                WHERE id = ?;
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(City.class)
                .optional();
    }

    List<City> findAll() {
        var sql = """
                SELECT id, cityName, countryId
                FROM cities
                ORDER BY cityName;
                """;
        return jdbcClient.sql(sql)
                .query(City.class)
                .list();
    }

    long create(City city) {
        var sql = """
                INSERT INTO cities (cityName, countryId)
                VALUES (?, ?);
                """;
            var keyHolder = new GeneratedKeyHolder();
            jdbcClient.sql(sql)
                    .params(city.getCityName(), city.getCountryId())
                    .update(keyHolder);
            return keyHolder.getKey().longValue();
    }

    void updateName(long id, String cityName) {
        var sql = """
                UPDATE cities
                SET cityName = ?
                WHERE id = ?;
                """;
        if (jdbcClient.sql(sql).params(cityName, id).update() == 0) {
            throw new CityNotFoundException();
        }
    }




}
