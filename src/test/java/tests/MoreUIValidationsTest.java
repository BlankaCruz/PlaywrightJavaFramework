package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MoreUIValidationsTest {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod (alwaysRun = true)
    public void setUp()
    {
        playwright = Playwright.create();
//        browser = playwright.chromium().launch(); //HEADLESS mode
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Chrome Browser -incognito, browser
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginPagePractise/");
    }

    @AfterMethod
    public void tearDown(){
    }

    @Test
    public void ChildWindowHandle(){

        Locator blinkingTexts = page.locator(".blinkingText");
        Page newPage = context.waitForPage(()-> blinkingTexts.first().click());  // ()-> is a lamda expression
        newPage.waitForLoadState();
        String childText = newPage.locator(".red").textContent();
        String emailId = childText.split("at ")[1].split(" ")[0];
        page.getByLabel("Username:").fill(emailId);
//        page.waitForTimeout(3000);
        System.out.println(page.getByLabel("Username:").inputValue());
    }

    @Test(groups={"smoke"})
    public void uiControls() {
        Locator userRdBn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        userRdBn.click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(userRdBn.isChecked());

        Locator checkBoxTerms = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));

        checkBoxTerms.check();
        Assert.assertTrue(checkBoxTerms.isChecked());

        checkBoxTerms.uncheck();
        Assert.assertFalse(checkBoxTerms.isChecked());

        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");
        page.waitForTimeout(3000);
    }
}
