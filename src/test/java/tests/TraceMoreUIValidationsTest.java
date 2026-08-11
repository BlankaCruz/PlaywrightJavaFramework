package tests;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class TraceMoreUIValidationsTest {

    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod
    public void setUp()
    {
        playwright = Playwright.create();
//        browser = playwright.chromium().launch(); //HEADLESS mode
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Chrome Browser -incognito, browser
        context = browser.newContext();

        // Start tracing before creating / navigating a page.
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginPagePractise/");
    }

    @AfterMethod
    public void tearDown(){
        // Stop tracing and export it into a zip archive.
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
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

//  After the test has been run:
//  1.  Refresh the "PlaywrightFramework"
//  2.  In a Browser, Go to: "trace.playwright.dev"
//  3.  Click on the "Select file" button.
//  4.  Upload the "trace.zip" file from: "C:\IntelliJ_Projects\PlaywrightFramework\trace.zip"
//  5.  Now you will be in the TRACE-VIEWER and you can see the before and After views is needed.

}
