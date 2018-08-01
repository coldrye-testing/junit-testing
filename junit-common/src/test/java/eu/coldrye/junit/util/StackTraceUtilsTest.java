// TBD:LICENSE

package eu.coldrye.junit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class StackTraceUtilsTest {

  @Test
  public void resolveCallerMustReturnExpectedTestMethod() {

    Method method = StackTraceUtils.resolveCaller(m -> ReflectionUtils.isAnnotatedBy(m, "Test"));

    Assertions.assertEquals("resolveCallerMustReturnExpectedTestMethod", method.getName());
  }

  @Test
  public void resolveCallerMustReturnExpectedTestMethodAndSkipAnyLambdas() {

    Method method = StackTraceUtils.resolveCaller(m -> ReflectionUtils.isAnnotatedBy(m, "Test"));

    Assertions.assertAll(
      () -> Assertions.assertEquals("resolveCallerMustReturnExpectedTestMethodAndSkipAnyLambdas", method.getName())
    );
  }
}
