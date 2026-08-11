package tests;

import org.testng.annotations.Test;
import pages.BookingDetailsPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.MyBookingsPage;

public class EventHubBookingPageObjectTest extends TestBase {

    @Test(groups = {"framework"}, description = "Login, open the first booking, cancel it, and verify it is removed")
    public void cancelFirstBookingUsingPageObjects() {
        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();

        MyBookingsPage myBookingsPage = new MyBookingsPage(page);
        myBookingsPage.goTo();
        myBookingsPage.verifyBookingsPageLoaded();

        String bookingReference = myBookingsPage.getFirstBookingReference();
        BookingDetailsPage bookingDetailsPage = myBookingsPage.openFirstBookingDetails();
        bookingDetailsPage.verifyDetailsPageLoaded();

        bookingDetailsPage.cancelBooking();

        myBookingsPage.verifyBookingRemoved(bookingReference);
    }
}
