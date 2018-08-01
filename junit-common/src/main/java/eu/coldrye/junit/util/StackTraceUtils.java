// TBD:LICENSE

package eu.coldrye.junit.util;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public final class StackTraceUtils {

  public static Method resolveCaller(Predicate<Method> predicate) {

    for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {

      // TODO:we might have a non canonical class name here, e.g. nested tests
      String className = ste.getClassName();
      String methodName = ste.getMethodName();

      if (!ReflectionUtils.isLambda(methodName)) {

        Method method = ReflectionUtils.findMethod(className, methodName);
        if (predicate.test(method)) {

          return method;
        }
      }
    }

    // must never happen
    throw new IllegalStateException("Unable to find suitable test method in stack trace");
  }

  // must not be instantiated
  private StackTraceUtils() {

  }
}
