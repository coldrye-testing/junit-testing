package attic.selenium;

import org.openqa.selenium.WebDriver;

import java.util.Properties;

public class WebDriverFactory {

  public final static WebDriverFactory INSTANCE = new WebDriverFactory();

  WebDriverFactory() {

  }

  /*
  selenium.timeouts.implicitWait = <int>
  selenium.timeouts.scriptTimeout = <int>
  selenium.timeouts.pageLoadTimeout = <int>

  selenium.logging.
   */
  public WebDriver createNewInstance(String browser, Properties defaults) {

    try {
      String browserName = browser.substring(0, 1).toUpperCase() + browser.substring(1);
      String className = "junit.framework.Selenium" + browserName + "DriverFactory";
      Class<?> klass = ClassLoader.getSystemClassLoader().loadClass(className);
      SeleniumDriverFactory factory = (SeleniumDriverFactory) klass.newInstance();
      return factory.createNewInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("add dependency junit::junit-selenium-" + browser + "-driver to your pom", e);
    }
  }

  public WebDriver createNewInstance(String browser) {

    return createNewInstance(browser, null);
  }
}
