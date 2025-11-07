package com.aagnes78.cinemagoer.screenings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql({"/cities.sql", "/cinemas.sql", "/films.sql", "/screenings.sql"})
@AutoConfigureMockMvc
class ScreeningControllerTest {
    private final static String SCREENINGS_TABLE = "screenings";
    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;

    ScreeningControllerTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    private int idOfTestFilm1() {
        return jdbcClient.sql("SELECT id FROM films WHERE title = 'testFilm1'")
                .query(Integer.class)
                .single();
    }

    private int idOfTestCinema1() {
        return jdbcClient.sql("SELECT id FROM cinemas WHERE cinemaName = 'testCinema1'")
                .query(Integer.class)
                .single();
    }

    private int idOfTestScreening1() {
        return jdbcClient.sql("SELECT id FROM screenings WHERE startDate = '1900-10-31 20:30:00'")
                .query(Integer.class)
                .single();
    }

    @Test
    void findAllFindsAll() {
        var response = mockMvcTester.get()
                .uri("/screenings");
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(JdbcTestUtils.countRowsInTable(jdbcClient, SCREENINGS_TABLE));
    }

    @Test
    void findAllFullInfoFindsAll() {
        var response = mockMvcTester.get()
                .uri("/screenings/fullInfo");
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(JdbcTestUtils.countRowsInTable(jdbcClient, SCREENINGS_TABLE));
    }

    @Test
    void findByIdFindsTheRightScreening() {
        var id = idOfTestScreening1();
        var response = mockMvcTester.get()
                .uri("/screenings/{id}", id);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .satisfies(
                        json -> assertThat(json).extractingPath("id").isEqualTo(id),
                        json -> assertThat(json).extractingPath("filmId").isEqualTo(idOfTestFilm1()),
                        json -> assertThat(json).extractingPath("cinemaId").isEqualTo(idOfTestCinema1())
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, Integer.MAX_VALUE})
    void findByIdFailsForNonExistingId(long wrongId) {
        var response = mockMvcTester.get()
                .uri("/screenings/{id}", wrongId);
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void findFullInfoByIdFindsTheRightScreening() {
        var id = idOfTestScreening1();
        var response = mockMvcTester.get()
                .uri("/screenings/{id}/fullInfo", id);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .satisfies(
                        json -> assertThat(json).extractingPath("id").isEqualTo(id),
                        json -> assertThat(json).extractingPath("filmId").isEqualTo(idOfTestFilm1()),
                        json -> assertThat(json).extractingPath("title").isEqualTo("testFilm1"),
                        json -> assertThat(json).extractingPath("cinemaId").isEqualTo(idOfTestCinema1())
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, Integer.MAX_VALUE})
    void findFullInfoByIdFailsForNonExistingId(long wrongId) {
        var response = mockMvcTester.get()
                .uri("/screenings/{id}/fullInfo", wrongId);
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void findFullInfoByCinemaIdFindsTheRightScreening() {
        var id = idOfTestCinema1();
        var response = mockMvcTester.get()
                .uri("/cinemas/{id}/screenings/fullInfo", id);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(JdbcTestUtils.countRowsInTableWhere(jdbcClient, SCREENINGS_TABLE,
                        "cinemaId = " + id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, Integer.MAX_VALUE})
    void findFullInfoByCinemaIdFindsNothingMatchingForNonExistingId(long wrongId) {
        var response = mockMvcTester.get()
                .uri("/cinemas/{id}/screenings/fullInfo", wrongId);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()").isEqualTo(0);
    }

    @Test
    void findFullInfoByFilmIdFindsTheRightScreening() {
        var id = idOfTestFilm1();
        var response = mockMvcTester.get()
                .uri("/films/{id}/screenings/fullInfo", id);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(JdbcTestUtils.countRowsInTableWhere(jdbcClient, SCREENINGS_TABLE,
                        "filmId = " + id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, Integer.MAX_VALUE})
    void findFullInfoByFilmIdFindsNothingMatchingForNonExistingId(long wrongId) {
        var response = mockMvcTester.get()
                .uri("/films/{id}/screenings/fullInfo", wrongId);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()").isEqualTo(0);
    }

}
