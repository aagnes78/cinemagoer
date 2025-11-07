package com.aagnes78.cinemagoer.films;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository {
    private final JdbcClient jdbcClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public FilmRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    Optional<Film> findById(long id) {
        var sql = """
                SELECT id, title, imdb
                FROM films
                WHERE id = ?;
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Film.class)
                .optional();
    }

    List<Film> findAll() {
        var sql = """
                SELECT id, title, imdb
                FROM films
                ORDER BY title;
                """;
        return jdbcClient.sql(sql)
                .query(Film.class)
                .list();
    }

    long create(Film film) {
        var sql = """
                INSERT INTO films (title, imdb)
                VALUES (?, ?);
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(film.getTitle(), film.getImdb())
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }

    long createWithTitleOnly(String title) {
        var sql = """
                INSERT INTO films (title)
                VALUES (?);
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .param(title)
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }

    void updateTitle(long id, String title) {
        var sql = """
                UPDATE films
                SET title = ?
                WHERE id = ?;
                """;
        if (jdbcClient.sql(sql).params(title, id).update() == 0) {
            logger.info("Title update failed for non-existent film {}", id);
            throw new FilmNotFoundException(id);
        }
    }

    void updateImdb(long id, String imdb) {
        var sql = """
                UPDATE films
                SET imdb = ?
                WHERE id = ?;
                """;
        if (jdbcClient.sql(sql).params(imdb).update() == 0) {
            throw new FilmNotFoundException(id);
        }
    }

    void delete(long id) {
        var sql = """
                  DELETE FROM films
                  WHERE id = ?;
                  """;
        jdbcClient.sql(sql)
                .param(id)
                .update();
    }


}
