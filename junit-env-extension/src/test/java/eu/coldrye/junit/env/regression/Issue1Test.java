package eu.coldrye.junit.env.regression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * #1 - Illegal state exception when using @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 *
 * just a dummy test here, the actual test is that the test class can be instantiated
 * and the after all callback implemented by EnvExtension does not fail
 */
@TestInstance(Lifecycle.PER_CLASS)
public class Issue1Test {

  @Test
  public void afterAllCallbackMustNotFail() {
    Assertions.assertTrue(true);
  }
}
