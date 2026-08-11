package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UIValidationsContinuedTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
//        browser = playwright.chromium().launch(); //HEADLESS mode
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test(groups = {"smoke"})
    public void popupValidations() {
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();

        //How to handle Dialog boxes
        //1.  Turn on the Dialog listener
        page.onDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Alert")).click();
        page.waitForTimeout(3000);

        //Handling a Mouse Hover:
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        page.waitForTimeout(2000);
//        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();
//        page.waitForTimeout(1000);

        FrameLocator framesPage = page.frameLocator("#courses-iframe");
        framesPage.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning Paths")).click();
        String textCheck = framesPage.locator(".inner-box h1").textContent();
        System.out.println(textCheck);
    }

    @Test
    public void screenShotTest()
    {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.png")));
        Locator displayedEditBox = page.getByPlaceholder("Hide/Show Example");
        displayedEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("editBoxScreenshot.png")));
        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagePostScreenshot.png")));

    }

}
