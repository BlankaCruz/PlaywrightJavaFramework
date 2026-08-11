package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {
    Page page;

    public EventsPage(Page page) {
        this.page = page;
    }

    public void goTo() {
        page.locator("#nav-events").click();
    }

    public Locator waitForEventToLoad() {
        Locator eventCards = page.locator("#event-card");
        assertThat(eventCards.first()).isVisible();
        return eventCards;
    }

    public Locator findEventCard(String titleCard) {
        Locator eventCards = waitForEventToLoad();
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText(titleCard));
        assertThat(targetCard).isVisible();
        return targetCard;
    }


    public int getSeatCount(Locator targetCard) {
        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        return Integer.parseInt(seatsText.split(" ")[0]);
    }

    public BookingFormPage proceedToBookingEvent(Locator targetCard) {
        targetCard.getByTestId("book-now-btn").click();
        return new BookingFormPage(page);
    }
}
