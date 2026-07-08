package api;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetTest {

    private final static String TOKEN=Token.getToken();
    private final static String SUGGESTION_URL = "https://suggestions.dadata.ru";
    private final static String ENDPOINT_IPLOCATE_ADDRESS = "/suggestions/api/4_1/rs/iplocate/address";
    public String temp="46.226.227.21";

    @Test //Тест для проверки, что город не разный в City_with_type и City
    public void testIPLocate(){
        Specifications.installSpecification(Specifications.requestSpecification(SUGGESTION_URL), Specifications.responseSpecification200());
        UserData userData = given()
                .header("Authorization", "Token " + TOKEN)
                .queryParam("ip", temp)
                .when()
                //.contentType(ContentType.JSON)
                .get(/*SUGGESTION_URL+*/ENDPOINT_IPLOCATE_ADDRESS)
                .then().log().all()
                .extract().body().jsonPath().getObject("location.data", UserData.class);

        System.out.println("Почтовый индекс: " + userData.getPostal_code());
        System.out.println("Страна: " + userData.getCountry());
        System.out.println("Город с типом: " + userData.getCity_with_type());
        System.out.println("Город: " + userData.getCity());
        System.out.println("Часовой пояс: " + userData.getTimezone());

        Assert.assertTrue(userData.getCity_with_type().contains(userData.getCity()));

    }

}
