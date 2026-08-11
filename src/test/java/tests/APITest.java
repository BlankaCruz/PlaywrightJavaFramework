package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITest {

    @Test
    public void e2eApiTest() {
        // Hashmap  stores values in <Key, Value> pairs
        HashMap<Object, Object> loginPayload = new HashMap<>(); //key, value
        loginPayload.put("email", "blanka.cruz@gmail.com");
        loginPayload.put("password", "Iambeloved@000");

// Login API call
        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext();
        APIResponse loginResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload));
        Assert.assertTrue(loginResponse.ok());
        System.out.println(loginResponse.text());

        String token = JsonPath.read(loginResponse.text(), "$.token");
        System.out.println("Login success " + token);

// Create Event
// Hashmap  stores values in <Key, Value> pairs
        String eventTitle = "Playwright API Testing4";
        HashMap<Object, Object> createEventPayload = new HashMap<>(); //key, value
        createEventPayload.put("title", eventTitle);
        createEventPayload.put("description", "API Test details");
        createEventPayload.put("category", "Sports");
        createEventPayload.put("venue", "Madison Square");
        createEventPayload.put("city", "New York");
        createEventPayload.put("eventDate", "2026-11-26T20:00:00.000Z");
        createEventPayload.put("price", "100");
        createEventPayload.put("totalSeats", "400");

        APIResponse eventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization", "Bearer " + token)
                        .setData(createEventPayload));

        Assert.assertTrue(eventResponse.ok(), "Create Event API should succeed.");

        int eventId = JsonPath.read(eventResponse.text(), "$.data.id");
        System.out.println("Event Created and it's ID =  " + eventId);

// Get event - to view all events: verify the one created is on the list.
        APIResponse retrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer " + token));
        Assert.assertTrue(retrieveEvents.ok(), "Event Retrieval API should succeed.");
        System.out.println(retrieveEvents.text());

        List<Integer> allEventIds = JsonPath.read(retrieveEvents.text(), "$.data[*].id");
        Assert.assertTrue(allEventIds.contains(eventId), "Created EventId is displayed in the Events List.");

// Finally, Delete Event created...
//        APIResponse deleteResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
//                RequestOptions.create().setHeader("Authorization", "Bearer " + token));
//       Assert.assertTrue(deleteResponse.ok());
//
//// Verify that the deletion is successful, -> GetEvents and confirm that EventId does not exist anymore.
//        APIResponse verifyResponse = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events/",
//                RequestOptions.create()
//                        .setQueryParam("page", "1")
//                        .setQueryParam("limit", "12")
//                        .setHeader("Authorization", "Bearer " + token));
//        Assert.assertTrue(verifyResponse.ok(), "Post-delete events list call should succeed");
//
//        List<String> titlesAfterDelete = JsonPath.read(verifyResponse.text(), "$.data[*].title");
//        Assert.assertFalse(titlesAfterDelete.contains(eventTitle), "Deleted Event Title is not displayed in the Events List.");
//        System.out.println("Deletion verified: event is no longer in the list");
    }
}
