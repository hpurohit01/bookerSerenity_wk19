package com.booker.authinfo;

import com.booker.model.AuthPojo;
import com.booker.testbase.TestBaseAuth;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class CreateAuthToken extends TestBaseAuth {
    @Title("Creating new auth token to access to PUT and DELETE booking requests")
    @Test
    public void createNewAuthToken() {
        AuthPojo authPojo = new AuthPojo();
        authPojo.setUsername("admin");
        authPojo.setPassword("password123");
        String token = SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(authPojo)
                .when()
                .post()
                .then()
                .extract()
                .path("token");
        System.out.println(token);
    }
}
