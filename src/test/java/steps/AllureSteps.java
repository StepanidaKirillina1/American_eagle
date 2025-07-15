package steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ui.BaseTest;

import java.io.ByteArrayInputStream;

public class AllureSteps {
    @Step("Capture screenshot (extension)")
    public void captureScreenshotSpoiler() {
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) BaseTest.getDriver())
                .getScreenshotAs(OutputType.BYTES)));
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] screenshot() {
        return ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "HTML", type = "text/html")
    @Step("save HTML in report")
    public void attachPageSourceForAllure(String name) {
        String pageSource = BaseTest.getDriver().getPageSource();
        Allure.addAttachment(name, "text/html", pageSource, ".html");
    }
}
