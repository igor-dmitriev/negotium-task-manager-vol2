package com.negotium.acceptance;


import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.File;

import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING;

public class UiTest {

  @ClassRule
  public static final BrowserWebDriverContainer CHROME = new BrowserWebDriverContainer()
      .withDesiredCapabilities(DesiredCapabilities.chrome())
      .withRecordingMode(RECORD_FAILING, new File("target"));

  @Test
  public void simplePlainSeleniumTest() {
    RemoteWebDriver driver = CHROME.getWebDriver();

    driver.get("https://wikipedia.org");
    WebElement searchInput = driver.findElementByName("search");

    searchInput.sendKeys("Rick Astley");
    searchInput.submit();

    WebElement otherPage = driver.findElementByLinkText("Rickrolling");
    otherPage.click();

    boolean expectedTextFound = driver.findElementsByCssSelector("p")
        .stream()
        .anyMatch(element -> element.getText().contains("meme"));

    assertTrue("The word 'meme' is found on a page about rickrolling", expectedTextFound);
  }
}
