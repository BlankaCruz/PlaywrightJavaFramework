package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage
{
    Page page;
    private static final String FULL_NAME_LABEL = "Full Name";
    private static final String EMAIL_LABEL = "Email";
    private static final String PHONE_NUMBER_LABEL = "Phone Number";
    private static final String CONFIRM_BOOKING_BTN= "Confirm Booking";
    private static final String SUCCESS_TOAST= "Booking Confirmed!";
    private static final String RESERVED_TICKETS_MSG= "Your tickets are reserved.";



    public BookingFormPage(Page page)
    {
        this.page = page;
    }

    public void goTo(){
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/bookingForm");

    }

    public void fillAndConfirmBookingForm(String fullName, String email, String phone){
        page.getByLabel(FULL_NAME_LABEL).fill(fullName);
        page.getByLabel(EMAIL_LABEL).fill(email);
        page.getByLabel(PHONE_NUMBER_LABEL).fill(phone);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(CONFIRM_BOOKING_BTN)).click();
        assertThat(page.getByText(SUCCESS_TOAST)).isVisible();
        assertThat(page.getByText(RESERVED_TICKETS_MSG)).isVisible();
    }
}
