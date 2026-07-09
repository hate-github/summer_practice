package api;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class POSTTest {

    private final static String TOKEN= ImportantThings.Token();
    private final static String SUGGESTION_URL = ImportantThings.SuggestionDADATAUrl();
    private final static String ENDPOINT_IP_LOCATE_ADDRESS = ImportantThings.POSTEndpointIpLocateAddress();
    private final List<String> addresses = IPList.CreateIPs().getIpList();
    //public String temp="46.226.227.20";

    /*
    * 1 Тест для проверки, что город не null
    * 2 Тест для проверки, что город не разный в City_with_type и City
    * 3 Тест для проверки, что данные не пустые (может ыбть null)
    */

    @Test
    public void testIPLocate(){
        Specifications.installSpecification(Specifications.requestSpecification(SUGGESTION_URL), Specifications.responseSpecification200());
        for(String IP : addresses){
            UserData userData = given()
                    .header("Authorization", "Token " + TOKEN)
                    .queryParam("ip", IP)
                    .when()
                    //.contentType(ContentType.JSON)
                    .get(/*SUGGESTION_URL+*/ENDPOINT_IP_LOCATE_ADDRESS)
                    .then()
                    .extract().body().jsonPath().getObject("location.data", UserData.class);
            System.out.println("Данные по IP: " + IP);
            System.out.println("Почтовый индекс: " + userData.getPostal_code());
            System.out.println("Страна: " + userData.getCountry());
            System.out.println("Город с типом: " + userData.getCity_with_type());
            System.out.println("Город: " + userData.getCity() + "\n");

            //1 тест
            Assert.assertNotNull("Город содержит null", userData.getCity());

            //2 тест
            Assert.assertTrue(userData.getCity_with_type().contains(userData.getCity()));

            //3 тест
            Assert.assertFalse("Пустое значение в Postal_code", userData.getPostal_code().isEmpty());
            Assert.assertFalse("Пустое значение в Country", userData.getCountry().isEmpty());
            Assert.assertFalse("Пустое значение в City_with_type", userData.getCity_with_type().isEmpty());
            Assert.assertFalse("Пустое значение в City", userData.getCity().isEmpty());

        }

    }

}
