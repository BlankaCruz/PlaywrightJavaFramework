package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MyBookingsPage {
    private final Page page;

    public MyBookingsPage(Page page) {
        this.page = page;
    }

    public void goTo() {
        page.getByTestId("nav-bookings").click();
    }

    public void verifyBookingsPageLoaded() {
        assertThat(page.getByText("View and manage all your ticket bookings")).isVisible();
    }

    public String getFirstBookingReference() {
        return page.locator("[data-testid='booking-card']")
                .first()
                .locator(".booking-ref")
                .textContent()
                .trim();
    }

    public BookingDetailsPage openFirstBookingDetails() {
        page.locator("[data-testid='booking-card']")
                .first()
                .locator("a[href^='/bookings/']")
                .click();
        return new BookingDetailsPage(page);
    }

    public void verifyBookingRemoved(String bookingReference) {
        page.waitForURL("**/bookings");
        Locator removedBookingCard = page.locator("[data-testid='booking-card']")
                .filter(new Locator.FilterOptions().setHasText(bookingReference));

        for (int i = 0; i < 10; i++) {
            if (removedBookingCard.count() == 0) {
                return;
            }
            page.waitForTimeout(500);
        }

        org.testng.Assert.assertEquals(removedBookingCard.count(), 0,
                "The cancelled booking should no longer appear in My Bookings");
    }
}
