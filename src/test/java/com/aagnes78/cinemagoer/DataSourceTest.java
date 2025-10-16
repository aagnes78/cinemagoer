package com.aagnes78.cinemagoer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class DataSourceTest {
    private final DataSource dataSource;
    public DataSourceTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Test
    void deDataSourceKanEenConnectionGeven() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.getCatalog()).isEqualTo("filmtracker");
        }
    }
}
