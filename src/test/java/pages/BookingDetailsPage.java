package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingDetailsPage {
    private final Page page;

    public BookingDetailsPage(Page page) {
        this.page = page;
    }

    public void verifyDetailsPageLoaded() {
        assertThat(page.getByText("Event Details")).isVisible();
    }

    public void cancelBooking() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel Booking")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes, cancel it")).click();
    }
}
