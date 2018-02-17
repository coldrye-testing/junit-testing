package attic.selenium.junit;

public enum BrowserType {
  FIREFOX(),
  CHROME,
  IE,
  EDGE,
  SAFARI,
  PHANTOMJS,
  HTMLUNIT;

  public String driverName() {

    return name().toLowerCase();
  }

  public String capitalizedName() {

    return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
  }
}
