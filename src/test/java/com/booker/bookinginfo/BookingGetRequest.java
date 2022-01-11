package com.booker.bookinginfo;

import com.booker.constants.EndPoints;
import com.booker.testbase.TestBaseBooking;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class BookingGetRequest extends TestBaseBooking {



    @Title("Get All Ids")
    @Test
    public void getAllIds(){
        SerenityRest.given().log().all()
                .when()
                .get()
                .then()
                .statusCode(200)
                .log().all();
    }

    @Title("Get booking by Id")
    @Test
    public void getBookingById() {
        SerenityRest.given().log().all()
                .header("Accept", "application/json")
                .pathParam("bookingId", "1")
                .when()
                .get(EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then()
                .statusCode(200)
                .log().all();
    }
}
