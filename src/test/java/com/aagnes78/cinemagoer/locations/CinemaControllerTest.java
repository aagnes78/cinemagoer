package com.aagnes78.cinemagoer.locations;

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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql({"/cities.sql", "/cinemas.sql"})
@AutoConfigureMockMvc
class CinemaControllerTest {
    private final static String CINEMAS_TABLES = "cinemas";
    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;

    CinemaControllerTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    private String jsonNewCinemaTemplate = """
            {
                "cinemaName": %s,
                "cityId" : %d,
                "identifier" : %s
            }
            """;

    private int idOfTestCinema1() {
        return jdbcClient.sql("SELECT id FROM cinemas WHERE cinemaName = 'testCinema1'")
                .query(Integer.class)
                .single();
    }

    private int idOfTestCity1() {
        return jdbcClient.sql("SELECT id FROM cities WHERE cityName = 'testCity1'")
                .query(Integer.class)
                .single();
    }

    @Test
    void findByIdFindsCinemaByExistingId() {
        var id = idOfTestCinema1();
        var response = mockMvcTester.get()
                .uri("/cinemas/{id}", id);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .satisfies(
                        json -> assertThat(json).extractingPath("id").isEqualTo(id),
                        json -> assertThat(json).extractingPath("cinemaName").isEqualTo("testCinema1")
                );
    }

    @Test
    void findByIdFailsForCinemaWithNonexistingId() {
        var response = mockMvcTester.get()
                .uri("/cinemas/{id}", Long.MAX_VALUE);
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllFindsAllCinemas() {
        var response = mockMvcTester.get()
                .uri("/cinemas");
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(JdbcTestUtils.countRowsInTable(jdbcClient, CINEMAS_TABLES));
    }

    void listCinemasByCountryFindsAllCinemas() {
        var response = mockMvcTester.get()
                .uri("/cinemas");
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(JdbcTestUtils.countRowsInTable(jdbcClient, CINEMAS_TABLES));
    }

    // addCinema
    @Test
    void addCinemaDoesAddCinemaToDb() {
        var jsonData = String.format(jsonNewCinemaTemplate, "\"testCinema42\"", idOfTestCity1(), "\"testLocation\"");
        var response = mockMvcTester.post()
                .uri("/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .satisfies(
                        newId -> assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, CINEMAS_TABLES,
                                "cinemaName = 'testCinema42' AND id = " + newId)).isOne()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void addCinemaFailsForInvalidCityId(long wrongId) {
        var jsonData = String.format(jsonNewCinemaTemplate, "\"testCinema42\"", wrongId, "\"testLocation\"");
        var response = mockMvcTester.post()
                .uri("/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void addCinemaFailsForValidButNonExistentCityId() {
        var jsonData = String.format(jsonNewCinemaTemplate, "\"testCinema42\"", Integer.MAX_VALUE, "\"testLocation\"");
        var response = mockMvcTester.post()
                .uri("/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cinemaWithEmptyName.json", "cinemaWithoutCityId.json", "cinemaWithoutName.json"} )
    void addCinemaFailsWhenSomeDataIsMissing(String fileName) throws Exception {
        var jsonData = new ClassPathResource(fileName)
                .getContentAsString(StandardCharsets.UTF_8);
        var response = mockMvcTester.post()
                .uri("/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.BAD_REQUEST);
    }


    @Test
    void addCinemaFailsForCinemaThatAlreadyExists() {
        var jsonData = String.format(jsonNewCinemaTemplate, "\"testCinema1\"", idOfTestCity1(), "\"testStreet1\"");
        var response = mockMvcTester.post()
                .uri("/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.CONFLICT);
    }



}
