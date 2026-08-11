package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {

    Page page;
    String base_url;
    private static final String email_placeHolder = "you@email.com";
    String username = "blanka.cruz@gmail.com";
    private static final String password_label = "Password";
    String password = "Iambeloved@000";

    public LoginPage(Page page, String baseUrl) {
        this.page = page;
        this.base_url = baseUrl;
    }

    public DashboardPage loginToApplication() {
        page.navigate(base_url);
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);

        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");

        // i) Enter email ii) Enter pwd iii) Click the 'Submit' button
        // Identify the components on the page using locators...
        page.getByPlaceholder(email_placeHolder).fill(username);
        page.getByLabel(password_label).fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
        page.waitForTimeout(1000);
        DashboardPage dashboardPage = new DashboardPage(page);
        return dashboardPage;
    }
}
