package api;

import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GETTest {
    private final static String TOKEN= ImportantThings.Token();
    private final static String SEKRET_KEY = ImportantThings.SecretKey();
    private final static String DADATA_URL = ImportantThings.DADATAUrl();
    private final static String ENDPOINT_DADATA_USAGE = ImportantThings.GETEndpointDADATAUsage();


    /*
    * 1 Тест для того, чтобы проверить, что данные не пустые
    * 2 Тест проверки, что данные положительные или равны ноль
    */

    @Test
    public void testGETTest(){
        Specifications.installSpecification(Specifications.requestSpecification(DADATA_URL), Specifications.responseSpecification200());

        DADATAStats dadataStats = given()
                .header("Authorization", "Token " + TOKEN)
                .header("X-Secret", SEKRET_KEY)
                .when()
                .get(ENDPOINT_DADATA_USAGE)
                .then()
                .extract().body().as( DADATAStats.class);
        System.out.println("Дата :" + dadataStats.getDate());
        System.out.println("services");
        System.out.println("    brand :" + dadataStats.getServices().getBrand());
        System.out.println("    clean :" + dadataStats.getServices().getClean());
        System.out.println("    company :" + dadataStats.getServices().getCompany());
        System.out.println("    docs :" + dadataStats.getServices().getDocs());
        System.out.println("    merging :" + dadataStats.getServices().getMerging());
        System.out.println("    suggestions :" + dadataStats.getServices().getSuggestions());
        System.out.println("remaining");
        System.out.println("    brand :" + dadataStats.getRemaining().getBrand());
        System.out.println("    clean :" + dadataStats.getRemaining().getClean());
        System.out.println("    company :" + dadataStats.getRemaining().getCompany());
        System.out.println("    docs :" + dadataStats.getRemaining().getDocs());
        System.out.println("    merging :" + dadataStats.getRemaining().getMerging());
        System.out.println("    suggestions :" + dadataStats.getRemaining().getSuggestions());
    }

}
