package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtil;

import java.io.IOException;
import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildDataDrivenTest extends TestBase {

    @DataProvider(name = "eventBookingData")
    public Object[][] eventBookingData() throws IOException {
        return DataProviderUtil.getJsonDataToMap("/src/test/resources/eventBookingData.json");
    }

    @Test (groups = {"framework"}, dataProvider= "eventBookingData", description="Create Event, Book that event, Verify that it is on the list")
    public void DemoTest(HashMap<String, String> data) {

        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();

        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(data.get("titlePrefix"),
                data.get("description"),
                data.get("category"),
                data.get("city"),
                data.get("venue"),
                data.get("dateTime"),
                data.get("price"),
                data.get("totalSeats"));


        // Step 2:  Find the newly created event in the Events Page. and
        // Visibility of the newly added card:
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetCard = eventsPage.findEventCard(data.get("titlePrefix"));
        int numSeatsBeforeBooking = eventsPage.getSeatCount(targetCard);
        BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);
        bookingFormPage.fillAndConfirmBookingForm(data.get("fullName"), data.get("email"), data.get("phone"));

        //Get booking-ref just added
        String bookingRef = page.locator(".booking-ref").innerText();
        System.out.println(bookingRef);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        Locator bookingCards = page.locator("#booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetBookingCard).isVisible();

        // Seat count reduction check
        page.locator("#nav-events").click();
        page.waitForTimeout(2000);
        Locator eventCardsAfterBooking = page.locator("#event-card");
//        page.waitForTimeout(2000);
////        System.out.println(eventCards.count());
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(data.get("titlePrefix")));
        assertThat(targetCardAfterBooking).isVisible();
        String seatsTextAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);
//
//        // AfterBookings < BeforeBookings
        int numSeatsAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        Assert.assertTrue(numSeatsBeforeBooking > numSeatsAfterBooking);
    }

    @AfterMethod
    public void tearDown() {

    }
}
