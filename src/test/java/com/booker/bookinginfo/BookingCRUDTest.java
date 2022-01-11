package com.booker.bookinginfo;


import com.booker.testbase.TestBaseBooking;
import com.booker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SerenityRunner.class)
public class BookingCRUDTest extends TestBaseBooking {

    static String firstname = TestUtils.getRandomValue();
    static String lastname = TestUtils.getRandomValue();
    static int totalprice = 300;
    static boolean depositpaid = true;
    static String additionalneeds = "Custom service";
    static List<Integer> bookingIdsBeforePost;
    static List<Integer> bookingIdsAfterPost;
    static int bookingid;

    @Steps
    BookingSteps bookingSteps;

    @Title("Extracting a list of Ids before creating new record")
    @Test
    public void test001(){
        // gather a list of ids before creating any new record
        // list will be used for comparison later on

        bookingIdsAfterPost = SerenityRest.given()
                .when()
                .get()
                .then()
                .extract()
                .path("bookingid");

        System.out.println("Old list of Ids: "+ bookingIdsAfterPost);
    }

    @Title("Creating a new booking using Post method")
    @Test
    public void test002(){
        HashMap<String, Object> booking = new HashMap<>();
        booking.put("checkin", "2022-01-01");
        booking.put("checkout", "2022-02-01");
        // create new booking
        ValidatableResponse response = bookingSteps.createBooking(firstname,
                lastname, totalprice, depositpaid,booking,additionalneeds);
        // validating the new record with the status code 200
        response.statusCode(200).log().all();
    }

    /**
     * This test will
     * 1. find the ids after posting
     * 2. compare the 2 sets of ids (Comparing the BeforePost and AfterPost List)
     * 3. extract the newly added id from the difference and store it in the id variable
     * 4. find the new record by id
     */
    @Title("Extracting ID and Finding the new booking by id to verify if it exists in the application")
    @Test
    public void test003() {
        //finding the list of ids after posting with intention to find new record's id
        bookingIdsAfterPost = SerenityRest.given()
                .when()
                .get()
                .then()
                .extract()
                .path("bookingid");

        System.out.println("Old List of ID's are :" + bookingIdsBeforePost);
        System.out.println("New List of ID's are :" + bookingIdsAfterPost);
        System.out.println("********************************************");

        //remove all old ids from new id list using removeAll method of list
        // (the remainder will be id of new record)
        bookingIdsAfterPost.removeAll(bookingIdsBeforePost);

        //extract the newly added id from the updated list and assign value to bookingid variable
        bookingid = (bookingIdsAfterPost.get(0));
        System.out.println("The newly generated id is: " + bookingid);
        System.out.println("********************************************");

        //finding the new booking by id
        ValidatableResponse response = bookingSteps.findNewRecordById(bookingid);
        //validating the new record by status code verification
        response.statusCode(200)
                .log().all();
        System.out.println(bookingid);
    }

    @Title("Updating the newly created record with ID and verifying it by extracting id " +
            "with updated firstname as query parameter")
    @Test
    public void test004() {
        //updating the record by amending the first name using method from bookingsteps
        firstname = firstname + "updated";
        HashMap<String, Object> booking = new HashMap<>();
        booking.put("checkin", "2022-01-01");
        booking.put("checkout", "2022-02-01");
        ValidatableResponse response = bookingSteps.updateBookingRecordById
                (firstname, lastname, totalprice, depositpaid, booking, additionalneeds, bookingid);
        //validating the response for put method
        response.statusCode(200)
                .log().all();

        //validating by comparing booking id from extraction of actual record by firstname
        List<Integer> id = bookingSteps.findSingleBookingRecordByFirstName(firstname, bookingid);
        System.out.println("Actual id is : " + id.get(0));
        System.out.println("Expected id is : " + bookingid);
        Assert.assertThat(id.get(0), equalTo(bookingid));
    }

    @Title("Delete the newly created record with ID")
    @Test
    public void test005(){
        //deleting the booking
        bookingSteps.deleteBooking(bookingid);
        //verifying the deletion by finding the record with id
        ValidatableResponse response = bookingSteps.findNewRecordById(bookingid);
        //After finding the response validating that the status code should be 404 to confirm deletion
        response.statusCode(404)
                .log().all();
    }}