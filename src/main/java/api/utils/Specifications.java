package api.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class Specifications {
    public static RequestSpecification requestSpecification(String url){
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setContentType(ContentType.JSON)
                .build();
    }
    public static ResponseSpecification responseSpecification200(){
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

    }
    public static ResponseSpecification responseSpecification401(){
        return new ResponseSpecBuilder()
                .expectStatusCode(401)
                .build();
    }

    public static ResponseSpecification responseSpecification403(){
        return new ResponseSpecBuilder()
                .expectStatusCode(403)
                .build();
    }

    public static ResponseSpecification responseSpecification200or400() {
        return new ResponseSpecBuilder()
                .expectStatusCode(anyOf(is(200), is(400)))
                .build();
    }
}
