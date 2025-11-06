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
@Sql("/cities.sql")
@AutoConfigureMockMvc
class CityControllerTest {
    private final static String CITIES_TABLES = "cities";
    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;
    private String jsonNewCityTemplate = """
            {
                "cityName": %s,
                "countryId" : %d
            }
            """;

    CityControllerTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    private int idOfTestCity1() {
        return jdbcClient.sql("SELECT id FROM cities WHERE cityName = 'testCity1'")
                .query(Integer.class)
                .single();
    }

    private int idOfBelgium() {
        return jdbcClient.sql("SELECT id FROM countries WHERE countryName = 'Belgium'")
                .query(Integer.class)
                .single();
    }

    @Test
    void findByIdFindsCityByExistingId() {
        var id = idOfTestCity1();
        var response = mockMvcTester.get()
                .uri("/cities/{id}", id);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .satisfies(
                        json -> assertThat(json).extractingPath("id").isEqualTo(id),
                        json -> assertThat(json).extractingPath("cityName").isEqualTo("testCity1")
                );
    }

    @Test
    void findByIdFailsForCityWithNonexistingId() {
        var response = mockMvcTester.get()
                .uri("/cities/{id}", Long.MAX_VALUE);
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllFindsAllCities() {
        var response = mockMvcTester.get()
                .uri("/cities");
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(JdbcTestUtils.countRowsInTable(jdbcClient, CITIES_TABLES));
    }

    // addCity
    @Test
    void addCityDoesAddCityToDB() {
        var jsonData = String.format(jsonNewCityTemplate, "\"testCity42\"", idOfBelgium());
        var response = mockMvcTester.post()
                .uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .satisfies(
                        newId -> assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, CITIES_TABLES,
                                "cityName = 'testCity42' AND id = " + newId)).isOne()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void addCityFailsWithInvalidCountryId(long wrongId) {
        var response = mockMvcTester.post()
                .uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(jsonNewCityTemplate, "\"testCity42\"", wrongId));
        assertThat(response).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void addCityFailsForValidButNonExistentCountryId() {
        var response = mockMvcTester.post()
                .uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(jsonNewCityTemplate, "\"testCity42\"", Integer.MAX_VALUE));
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cityWithEmptyName.json", "cityWithoutCountryId.json", "cityWithoutName.json"} )
    void addCityFailsWhenSomeDataIsMissing(String fileName) throws Exception {
        var jsonData = new ClassPathResource(fileName)
                .getContentAsString(StandardCharsets.UTF_8);
        var response = mockMvcTester.post()
                .uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void addCityFailsForCityThatAlreadyExists() {
        var jsonData = String.format(jsonNewCityTemplate, "\"testCity1\"", idOfBelgium());
        var response = mockMvcTester.post()
                .uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);
        assertThat(response).hasStatus(HttpStatus.CONFLICT);
    }

    @Test
    void changeNameDoesUpdateCity() {
        var id =  idOfTestCity1();
        var response = mockMvcTester.put()
                .uri("/cities/{id}/name", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("testCity42");
        assertThat(response).hasStatusOk();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, CITIES_TABLES,
                "cityName = 'testCity42' AND id = " + id)).isOne();
    }

    @Test
    void changeNameFailsForCityWithNonexistingId() {
        var response = mockMvcTester.put()
                .uri("/cities/{id}/name", Long.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("testCity42");
        assertThat(response).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void changeNameFailsIfItWouldCreateDuplicateCity() {
        var id =  idOfTestCity1();
        var response = mockMvcTester.put()
                .uri("/cities/{id}/name", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("testCity2");
        assertThat(response).hasStatus(HttpStatus.CONFLICT);
    }
}
