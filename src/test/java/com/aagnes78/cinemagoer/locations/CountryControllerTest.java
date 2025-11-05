package com.aagnes78.cinemagoer.locations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CountryControllerTest {
    private final static String COUNTRIES_TABLES = "countries";
    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;

    CountryControllerTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    private int idOfBelgium() {
        return jdbcClient.sql("SELECT id FROM countries WHERE countryName = 'Belgium'")
                .query(Integer.class)
                .single();
    }

    @Test
    void findByIdFindsTheCountry() {
        var id = idOfBelgium();
        System.out.println("Belgium: " + id);
        var response = mockMvcTester.get()
                .uri("/countries/{id}", id);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .satisfies(
                        json -> assertThat(json).extractingPath("id").isEqualTo(id),
                        json -> assertThat(json).extractingPath("countryName").isEqualTo("Belgium")
                );
    }

    @Test
    void findByIdFailsForNonExistentId() {
        var response = mockMvcTester.get()
                .uri("/countries/{id}", Long.MAX_VALUE);
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllFindsAllCountries() {
        var response = mockMvcTester.get()
                .uri("/countries");
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(JdbcTestUtils.countRowsInTable(jdbcClient, COUNTRIES_TABLES));
    }

}
