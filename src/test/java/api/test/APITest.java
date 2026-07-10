package api.test;

import api.dto.DADATAStats;
import api.dto.UserData;
import api.utils.EnvLoader;
import api.utils.Specifications;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class APITest {
    private static final String TOKEN = EnvLoader.get("TOKEN");
    private static final String SECRET_KEY = EnvLoader.get("SECRET_KEY");
    private static final String DADATA_URL = EnvLoader.get("DADATA_URL");
    private static final String SUGGESTION_DADATA_URL = EnvLoader.get("SUGGESTION_DADATA_URL");
    private static final String ENDPOINT_DADATA_USAGE = EnvLoader.get("ENDPOINT_DADATA_USAGE");
    private static final String ENDPOINT_IP_LOCATE_ADDRESS = EnvLoader.get("ENDPOINT_IP_LOCATE_ADDRESS");

    private static List<String> ipList;

    @BeforeAll
    static void setUp() throws Exception {
        Properties testProps = new Properties();
        try (InputStream input = APITest.class.getClassLoader().getResourceAsStream("test-data.properties")){
            if(input == null){
                throw new RuntimeException("properties not found");
            }
            testProps.load(input);
        }
        String ips = testProps.getProperty("ip.list");
        if(ips!=null&& !ips.isEmpty()){
            ipList = Arrays.asList(ips.split(","));
        }
        else{
            ipList = List.of();
        }
    }

    /*
     * 1 Тест для проверки, что город не null
     * 2 Тест для проверки, что город не разный в City_with_type и City
     * 3 Тест для проверки, что данные не пустые (может ыбть null)
     */

    @Test
    void testIPLocate(){
        for(String ip : ipList){
            UserData userData = given()
                    .log().all()
                    .spec(Specifications.requestSpecification(SUGGESTION_DADATA_URL))
                    .header("Authorization","Token " + TOKEN)
                    .queryParam("ip", ip.trim())
                    .when()
                    .get(ENDPOINT_IP_LOCATE_ADDRESS)
                    .then()
                    .log().all()
                    .spec(Specifications.responseSpecification200())
                    .extract()
                    .jsonPath()
                    .getObject("location.data", UserData.class);


            assertAll(
                    () -> assertNotNull(userData.getCity(), "City null"),
                    () -> assertTrue(userData.getCity_with_type().contains(userData.getCity()),"Город не совпадает"),
                    () -> assertFalse(userData.getPostal_code().isEmpty(), "пустое значение в Postal_code"),
                    () -> assertFalse(userData.getCountry().isEmpty(),"пустое значение в Country"),
                    () -> assertFalse(userData.getCity_with_type().isEmpty(),"пустое значение в City_with_type"),
                    () -> assertFalse(userData.getCity().isEmpty(),"пустое значение в City")
            );

        }
    }

    /*
     * 1 Тест для того, чтобы проверить, что данные не пустые
     */

    @Test
    void testDADATAUsage(){
        DADATAStats stats = given()
                .log().all()
                .spec(Specifications.requestSpecification(DADATA_URL))
                .header("Authorization", "Token " + TOKEN)
                .header("X-Secret", SECRET_KEY)
                .when()
                .get(ENDPOINT_DADATA_USAGE)
                .then().log().all()
                .spec(Specifications.responseSpecification200())
                .extract()
                .as(DADATAStats.class);

        assertAll(
                () -> assertNotNull(stats.getDate(),"дата null"),
                () -> assertNotNull(stats.getServices(),"Services null"),
                () -> assertNotNull(stats.getRemaining(),"Remaining null")
        );
    }
}
