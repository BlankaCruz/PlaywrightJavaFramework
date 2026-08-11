package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
//Copilot and claude code
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {

    Playwright playwright;
    Browser browser;
    Page page;

    // Invoke browser ->Invoke page ->Type url
    @BeforeMethod
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);
    }

    @Test(description = "Sandbox banner is shown when 6 events are returned")
    public void DemoTest() {
        // i) Enter email ii) Enter pwd iii) Click the 'Submit' button
        // Identify the components on the page using locators...
        page.getByPlaceholder("you@email.com").fill("blanka.cruz@gmail.com");
        page.getByLabel("Password").fill("Iambeloved@000");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        // Step 1: Create Event from the Admin page.
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_6.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();

        Assert.assertEquals(eventCards.count(), 6);
        assertThat(page.locator(".mx-1").first()).isVisible();  //Playwright
//        Assert.assertTrue(page.locator(".mx-1").isVisible());  //TestNG


        // Step 2: Create Event from the Admin page.
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_4.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards1 = page.getByTestId("event-card");
        assertThat(eventCards1.first()).isVisible();

        Assert.assertEquals(eventCards1.count(), 4);
        Assert.assertFalse(page.locator(".mx-1").first().isVisible());  //Playwright
    }

    @Test
    public void routeResumeTest() {
        //route.resume
        //Login
        page.getByPlaceholder("you@email.com").fill("blanka.cruz@gmail.com");
        page.getByLabel("Password").fill("Iambeloved@000");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        //Bookings
        page.getByTestId("nav-bookings").click();
        page.route("**/api/bookings**", route -> route.resume(
                new Route.ResumeOptions().setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/72116"))
        );

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Retry")).first().click();
        page.getByText("Couldn't load bookings").isVisible();
//        page.getByText("Access Denied").isVisible();
    }

}
