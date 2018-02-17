package attic.selenium.junit;

public @interface Browser {

  BrowserType type();

  String version() default "";
}
