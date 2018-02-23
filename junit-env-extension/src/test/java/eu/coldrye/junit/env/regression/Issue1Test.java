package eu.coldrye.junit.env.regression;

import eu.coldrye.junit.env.EnvExtension;
import eu.coldrye.junit.env.EnvProvided;
import eu.coldrye.junit.env.Environment;
import eu.coldrye.junit.env.Fixtures.SimpleEnvProvider;
import org.junit.jupiter.api.Assertions;
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
  private Object envProvided;

  @Test
  public void envProvidedMustExist() {
    Assertions.assertNotNull(envProvided);
  }
}
