package eu.coldrye.junit.env.regression;

import eu.coldrye.junit.env.EnvExtension;
import eu.coldrye.junit.env.EnvProvided;
import eu.coldrye.junit.env.Environment;
import eu.coldrye.junit.env.Fixtures.SimpleEnvProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * #1 - Illegal state exception when using @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 *
 * just a dummy test here, the actual test is that the test class can be instantiated
 * and the after all callback implemented by EnvExtension does not fail
 */
@ExtendWith(EnvExtension.class)
@Environment(SimpleEnvProvider.class)
@TestInstance(Lifecycle.PER_CLASS)
public class Issue1Test {

  @EnvProvided
  private String envProvided;

  @BeforeAll
  public void beforeAll() {
    // hook must not break EnvExtension when using @TestInstance(Lifecycle.PER_CLASS)
  }

  @BeforeEach
  public void beforeEach() {
    // hook must not break EnvExtension when using @TestInstance(Lifecycle.PER_CLASS)
  }

  @AfterEach
  public void afterEach() {
    // hook must not break EnvExtension when using @TestInstance(Lifecycle.PER_CLASS)
  }

  @AfterAll
  public void afterAll() {
    // hook must not break EnvExtension when using @TestInstance(Lifecycle.PER_CLASS)
  }

  @Test
  public void envProvidedMustExist() {
    Assertions.assertAll(
      () -> Assertions.assertNotNull(envProvided, "envProvided must not be null"),
      () -> Assertions.assertEquals("envProvided", envProvided)
    );
  }
}
