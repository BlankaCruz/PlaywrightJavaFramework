package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildTest extends TestBase {

    @Test(groups = {"framework"}, description="Create Event-make a booking, Verify the booking")
    public void DemoTest() {
        String eventTitle = "Playwright Framework Test";

        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();

        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(eventTitle, "Blanka's test form2", "Concert",
                "Test City", "Test Venue", "2026-12-07T09:00", "100.00", "500");


        // Step 2:  Find the newly created event in the Events Page. and
        // Visibility of the newly added card:
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetCard = eventsPage.findEventCard(eventTitle);
        int numSeatsBeforeBooking = eventsPage.getSeatCount(targetCard);
        BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);
        bookingFormPage.fillAndConfirmBookingForm("Test Name", "testEmail@gmail.com", "1123456789");

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
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(eventTitle));
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
