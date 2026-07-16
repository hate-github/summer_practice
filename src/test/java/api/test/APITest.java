package api.test;

import api.dto.DADATAStats;
import api.dto.SuggestAddressResponse;
import api.dto.UserData;
import api.utils.ConfigLoader;
import api.utils.EnvLoader;
import api.utils.Specifications;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class APITest {
    private static final String TOKEN = EnvLoader.get("TOKEN");
    private static final String SECRET_KEY = EnvLoader.get("SECRET_KEY");
    private static final String DADATA_URL = ConfigLoader.get("DADATA_URL");
    private static final String SUGGESTION_DADATA_URL = ConfigLoader.get("SUGGESTION_DADATA_URL");
    private static final String ENDPOINT_DADATA_USAGE = ConfigLoader.get("ENDPOINT_DADATA_USAGE");
    private static final String ENDPOINT_IP_LOCATE_ADDRESS = ConfigLoader.get("ENDPOINT_IP_LOCATE_ADDRESS");
    private static final String ENDPOINT_SUGGEST_ADDRESS = ConfigLoader.get("ENDPOINT_SUGGEST_ADDRESS");

    private static Properties testProps;

    @BeforeAll
    static void setUp() throws Exception {
        testProps = new Properties();
        try (InputStream input = APITest.class.getClassLoader().getResourceAsStream("test-data.properties")) {
            if (input == null) {
                throw new RuntimeException("test-data.properties not found");
            }
            try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                testProps.load(reader);
            }
        }
    }

    private Stream<String> ipForMethod() {
        return Arrays.stream(testProps.getProperty("ip.list", "").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty());
    }

    private Stream<String> ipNoResult() {
        return Stream.of(testProps.getProperty("ip.noresult", "1.1.1.1").trim());
    }

    private Stream<String> invalidToken() {
        return Stream.of(testProps.getProperty("invalid.token", "invalid").trim());
    }

    private Stream<String> emptyQuery() {
        return Stream.of(testProps.getProperty("empty.query", "").trim());
    }

    private Stream<String> shortQuery() {
        return Stream.of(testProps.getProperty("short.query", "1").trim());
    }

    private Stream<String> positiveAddressQueries() {
        return Arrays.stream(testProps.getProperty("address.query.positive", "Москва").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty());
    }

    private Stream<String> noResultAddressQueries() {
        return Arrays.stream(testProps.getProperty("address.query.noresult", "AAA,aaa").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty());
    }

    /*
     * 1 Тест для проверки, что город не null
     * 2 Тест для проверки, что город не разный в City_with_type и City
     * 3 Тест для проверки, что данные не пустые (может ыбть null)
     * 4 Тест для проверки, что страна - Россия
     * 5 Тест проверки ввода токена
     * 6 Тест проверки наличие токена
     * 7 Тест для неправильного IP
     */

    @ParameterizedTest(name = "данные по IP {0}")
    @DisplayName("Проверка города по IP - позитивные тесты")
    @MethodSource("ipForMethod")
    void testIPLocate(String ip) {
        UserData userData = given()
                .log().all()
                .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                .header("Authorization", "Token " + TOKEN)
                .queryParam("ip", ip)
                .when()
                .get(ENDPOINT_IP_LOCATE_ADDRESS)
                .then()
                .log().all()
                .spec(Specifications.responseSpecification200())
                .extract()
                .jsonPath()
                .getObject("location.data", UserData.class);

        String expectedCountry = testProps.getProperty("expected.country", "Россия");

        assertAll(
                () -> assertNotNull(userData.getCity(), "City null"),
                () -> assertTrue(userData.getCity_with_type().contains(userData.getCity()), "Город не совпадает"),
                () -> assertFalse(userData.getPostal_code().isEmpty(), "Postal_code пуст"),
                () -> assertFalse(userData.getCountry().isEmpty(), "Country пуст"),
                () -> assertFalse(userData.getCity_with_type().isEmpty(), "City_with_type пуст"),
                () -> assertFalse(userData.getCity().isEmpty(), "City пуст"),
                () -> assertEquals(expectedCountry, userData.getCountry(), "Не ожидаемая страна")
        );
    }

    @ParameterizedTest(name = "невалидный токен: {0}")
    @DisplayName("Проверка города по IP - неправильный токен")
    @MethodSource("invalidToken")
    void testIPLocateInvalidToken(String invalidToken) {
        given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                .header("Authorization", "Token " + invalidToken)
                .queryParam("ip", "46.226.227.20")
                .when()
                .get(ENDPOINT_IP_LOCATE_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification403());
    }

    @Test
    @DisplayName("Проверка города по IP - нет токена")
    void testIPLocateNoToken() {
        given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                .queryParam("ip", "46.226.227.20")
                .when()
                .get(ENDPOINT_IP_LOCATE_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification401());
    }

    @ParameterizedTest(name = "IP без результата: {0}")
    @DisplayName("Проверка города по IP - неправильный IP")
    @MethodSource("ipNoResult")
    void testIPLocateNoResult(String ipNoResult) {
        UserData userData = given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                .header("Authorization", "Token " + TOKEN)
                .queryParam("ip", ipNoResult)
                .when()
                .get(ENDPOINT_IP_LOCATE_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification200())
                .extract()
                .jsonPath()
                .getObject("location.data", UserData.class);

        assertNull(userData, "Ожидается null, т.к. данных по IP нет");
    }


    /*
     * 1 Тест для того, чтобы проверить, что данные не пустые
     * 2 Тест проверки ввода токена
     * 3 Тест проверки наличия токена
     */

    @Test
    @DisplayName("Проверка статистики Dadata.ru")
    void testDADATAUsage() {
        DADATAStats stats = given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(DADATA_URL))
                .header("Authorization", "Token " + TOKEN)
                .header("X-Secret", SECRET_KEY)
                .when()
                .get(ENDPOINT_DADATA_USAGE)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification200())
                .extract()
                .as(DADATAStats.class);

        assertAll(
                () -> assertNotNull(stats.getDate(), "дата null"),
                () -> assertNotNull(stats.getServices(), "Services null"),
                () -> assertNotNull(stats.getRemaining(), "Remaining null")
        );
    }

    @Test
    @DisplayName("Проверка статистики Dadata.ru - неправильный токен")
    void testDADATAUsageInvalidToken() {
        given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(DADATA_URL))
                .header("Authorization", "Token " + testProps.getProperty("invalid.token"))
                .header("X-Secret", SECRET_KEY)
                .when()
                .get(ENDPOINT_DADATA_USAGE)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification401());
    }

    @Test
    @DisplayName("Проверка статистики Dadata.ru - нет токена")
    void testDADATAUsageNoToken() {
        given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(DADATA_URL))
                .header("X-Secret", SECRET_KEY)
                .when()
                .get(ENDPOINT_DADATA_USAGE)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification401());
    }

    /*
    * 1 Тест для проверки, что данные не пустые
    * 2 Тест проверки, что правильно обрабатывается пустой запрос
    * 3 Тест для проверки, что правильно обрабатывается очередь
    * 4 Тест без токена
    * 5 Тест с неправильными данными
    */

    @ParameterizedTest(name = "Поиск адреса {0}")
    @DisplayName("Подсказки по адресу - правильные данные")
    @MethodSource("positiveAddressQueries")
    void testSuggestAddressPositive(String query) {
        SuggestAddressResponse response = given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                .header("Authorization", "Token " + TOKEN)
                .queryParam("query", query)
                .when()
                .get(ENDPOINT_SUGGEST_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification200())
                .extract()
                .as(SuggestAddressResponse.class);

        assertNotNull(response.getSuggestions(), "Suggestions null");
        assertFalse(response.getSuggestions().isEmpty(), "No suggestions");
        String city = response.getSuggestions().get(0).getData().getCity();
        assertNotNull(city, "City null");
        assertFalse(city.isEmpty(), "City empty");
    }

    @ParameterizedTest(name = "Поиск адреса ")
    @DisplayName("Подсказки по адресу - пустой запрос")
    @MethodSource("emptyQuery")
    void testSuggestAddressEmptyQuery(String emptyQuery) {
        given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                .header("Authorization", "Token " + TOKEN)
                .queryParam("query", emptyQuery)
                .when()
                .get(ENDPOINT_SUGGEST_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification200());
    }

    @ParameterizedTest(name = "Поиск адреса")
    @DisplayName("Подсказки по адресу - короткий запрос")
    @MethodSource("shortQuery")
    void testSuggestAddressShortQuery(String shortQuery) {
        given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                .header("Authorization", "Token " + TOKEN)
                .queryParam("query", shortQuery)
                .when()
                .get(ENDPOINT_SUGGEST_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification200());
    }

    @Test
    @DisplayName("Подсказки по адресу - без токена")
    void testSuggestAddressNoToken() {
        given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                .queryParam("query", "Москва")
                .when()
                .get(ENDPOINT_SUGGEST_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification401());
    }

    @ParameterizedTest(name = "Подсказки по адресу {0}")
    @DisplayName("Подсказки по адресу - неправильные данные")
    @MethodSource("noResultAddressQueries")
    void testSuggestAddressNoResult(String query) {
        SuggestAddressResponse response = given()
                .log().ifValidationFails()
                .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                .header("Authorization", "Token " + TOKEN)
                .queryParam("query", query)
                .when()
                .get(ENDPOINT_SUGGEST_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(Specifications.responseSpecification200())
                .extract()
                .as(SuggestAddressResponse.class);

        assertNotNull(response.getSuggestions(), "Suggestions is null");
        assertTrue(response.getSuggestions().isEmpty(), "Expected empty list, but got some suggestions");
    }
}