package attic.selenium.junit.jupiter.selenium.api;

import attic.selenium.junit.jupiter.selenium.extensions.SeleniumServerAfterAllCallback;
import attic.selenium.junit.jupiter.selenium.extensions.SeleniumServerPostProcessor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.openqa.selenium.WebDriver;

@Extensions({
  @ExtendWith(SeleniumServerPostProcessor.class),
  @ExtendWith(SeleniumServerAfterAllCallback.class)
})
public abstract class SeleniumTestCase {

  protected WebDriver driver;

  public void setDriver(WebDriver driver) {

    this.driver = driver;
  }

}
