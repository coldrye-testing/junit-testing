package eu.coldrye.junit.env.integration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public interface Fixtures {

  @Configuration
  @ComponentScan("eu.coldrye.junit.env")
  class TestConfig {

  }

  @Component
  class SimpleComponent {}
}
