package attic.selenium.junit.jupiter.selenium.api;

import attic.selenium.junit.BrowserType;

public @interface Browser {

  BrowserType browser();

  String version() default "";
}
