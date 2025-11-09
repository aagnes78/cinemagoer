package com.aagnes78.cinemagoer.screenings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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

    private String newScreeningTemplate = """
            {
              "filmId": %d,
              "cinemaId": %d,
              "startDate": %s,
              "endDate": %s
            }
            """;

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

    @Test
    void addNewScreeningCreatesScreening() {
        var date = LocalDateTime.of(2025, 10, 31, 20, 0);
        var jsonData = String.format(newScreeningTemplate, idOfTestFilm1(), idOfTestCinema1(),
                "\""+date+"\"", "\""+date+"\"");
        var response = mockMvcTester.post()
                .uri("/screenings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$").satisfies(newId ->
                        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, SCREENINGS_TABLE,
                                "filmId = " + idOfTestFilm1()
                        + " AND cinemaId = " + idOfTestCinema1()
                        + " AND id = " + newId)).isOne());

    }

    @Test
    void addNewScreeningWithTwoDifferentDates() {
        var jsonData = """
                {
                  "filmId": %d,
                  "cinemaId": %d,
                  "startDate": "2025-10-01T00:00",
                  "endDate": "2025-10-31T23:59"
                }
                """;
        jsonData = String.format(jsonData, idOfTestFilm1(), idOfTestCinema1());
        var response = mockMvcTester.post()
                .uri("/screenings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$").satisfies(newId ->
                        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, SCREENINGS_TABLE,
                                "filmId = " + idOfTestFilm1()
                                        + " AND cinemaId = " + idOfTestCinema1()
                                        + " AND id = " + newId)).isOne());
    }

    @Test
    void addNewScreeningFailsIfScreeningIsAlreadyPresentInDatabase() {
        var date = LocalDateTime.of(1900, 10, 31, 20, 30);
        var jsonData = String.format(newScreeningTemplate, idOfTestFilm1(), idOfTestCinema1(),
                "\""+date+"\"", "\""+date+"\"");
        var response = mockMvcTester.post()
                .uri("/screenings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.CONFLICT);
    }

    @ParameterizedTest
    @ValueSource(strings = {"screeningWithEmptyEndDate.json", "screeningWithEmptyStartDate.json",
            "screeningWithoutCinemaId.json", "screeningWithoutEndDate.json",
            "screeningWithoutFilmId.json", "screeningWithoutStartDate.json"})
    void addScreeningFailsIfSomeDataIsMissing(String fileName) throws Exception{
        var jsonData = new ClassPathResource(fileName)
                .getContentAsString(StandardCharsets.UTF_8);
        var response = mockMvcTester.post()
                .uri("/screenings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void addScreeningFailsForValidButNonExistentFilmId() {
        var date = LocalDateTime.of(2025, 10, 31, 20, 0);
        var jsonData = String.format(newScreeningTemplate, Long.MAX_VALUE, idOfTestCinema1(),
                "\""+date+"\"", "\""+date+"\"");
        var response = mockMvcTester.post()
                .uri("/screenings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void addScreeningFailsForValidButNonExistentCinemaId() {
        var date = LocalDateTime.of(2025, 10, 31, 20, 0);
        var jsonData = String.format(newScreeningTemplate, idOfTestFilm1(), Long.MAX_VALUE,
                "\""+date+"\"", "\""+date+"\"");
        var response = mockMvcTester.post()
                .uri("/screenings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void addScreeningFailsIfEndDateIsBeforeStartDate() {
        var startDate = LocalDateTime.of(2025, 10, 31, 20, 0);
        var endDate = startDate.minusDays(1);
        var jsonData = String.format(newScreeningTemplate, idOfTestFilm1(), Long.MAX_VALUE,
                "\""+startDate+"\"", "\""+endDate+"\"");
        var response = mockMvcTester.post()
                .uri("/screenings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.BAD_REQUEST);
    }

}
