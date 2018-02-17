package attic.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;

public abstract class SeleniumDriverAdapter extends SeleniumDriverAdapter {

  private WebDriver driver;

  public SeleniumDriverAdapter() {

  }

  public WebDriver getDriver() {

    return driver;
  }

  public void get(String url) {

    getDriver().get(url);
  }

  public String getCurrentUrl() {

    return getDriver().getCurrentUrl();
  }

  String getTitle() {

    return getDriver().getTitle();
  }

  List<WebElement> findElements(By by) {

    return getDriver().findElements(by);
  }

  WebElement findElement(By by) {

    return getDriver().findElement(by);
  }

  String getPageSource() {

    return getDriver().getPageSource();
  }

  void close() {

    getDriver().close();
  }

  void quit() {

    getDriver().quit();
  }

  Set<String> getWindowHandles() {

    return getDriver().getWindowHandles();
  }

  String getWindowHandle() {

    return getDriver().getWindowHandle();
  }

  WebDriver.TargetLocator switchTo() {

    return getDriver().switchTo();
  }

  public WebDriver.Navigation navigate() {

    return getDriver().navigate();
  }

  WebDriver.Options manage() {

    return getDriver().manage();
  }
}
