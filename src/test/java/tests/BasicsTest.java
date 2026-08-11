package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest {
    Playwright playwright;
    Browser browser;
    Page page;

    // Invoke browser ->Invoke page ->Type url
    @BeforeMethod
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
//        page.setDefaultTimeout(8000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);
    }

    @Test
    public void DemoTest() {
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");

        // i) Enter email ii) Enter pwd iii) Click the 'Submit' button
        // Identify the components on the page using locators...
        page.getByPlaceholder("you@email.com").fill("blanka.cruz@gmail.com");
        page.getByLabel("Password").fill("Iambeloved@000");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        // Step 1: Create Event from the Admin page.

        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        page.locator("#event-title-input").fill("QA Test Event", new Locator.FillOptions().setTimeout(10000));
        page.locator("#admin-event-form textarea").fill("Blanka's test form2");
        page.getByLabel("Category").selectOption("Concert");
        page.getByLabel("City").fill("Test City");
        page.getByLabel("Venue").fill("Test Venue");
        page.getByLabel("Event Date & Time").fill("2026-12-07T09:00");
        page.getByLabel("Price ($)").fill("100.00");
        page.getByLabel("Total Seats").fill("50");
        page.locator("#add-event-btn").click(new Locator.ClickOptions().setTimeout(12000));
        assertThat(page.getByText("Event created!")).isVisible(); //5 sec


        // Step 2:  Find the newly created event in the Events Page.
        page.locator("#nav-events").click();
        Locator eventCards = page.locator("#event-card");
        page.waitForTimeout(2000);
        System.out.println(eventCards.count());


        // Visibility of the newly added card:
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("QA Test Event"));
        assertThat(targetCard).isVisible();
        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        int numSeatsBeforeBooking = Integer.parseInt(seatsText.split(" ")[0]);

        targetCard.getByTestId("book-now-btn").click();
        page.getByLabel("Full Name").fill("Test Name");
        page.getByLabel("Email").fill("testEmail@gmail.com");
        page.getByLabel("Phone Number").fill("1123456789");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Booking Confirmed!")).isVisible();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();

        //Get booking-ref just added
        String bookingRef = page.locator(".booking-ref").innerText();
        System.out.println(bookingRef);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        Locator bookingCards = page.locator("#booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetBookingCard).isVisible();

        // Seat count reduction check
        page.locator("#nav-events").click();
        Locator eventCardsAfterBooking = page.locator("#event-card");
        page.waitForTimeout(2000);
        System.out.println(eventCards.count());
        Locator targetCardAfterBooking = eventCards.filter(new Locator.FilterOptions().setHasText("QA Test Event"));
        assertThat(targetCardAfterBooking).isVisible();
        String seatsTextAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);

        // AfterBookings < BeforeBookings
        int numSeatsAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        Assert.assertTrue(numSeatsBeforeBooking > numSeatsAfterBooking);
    }

    @AfterMethod
    public void tearDown() {

    }
}
