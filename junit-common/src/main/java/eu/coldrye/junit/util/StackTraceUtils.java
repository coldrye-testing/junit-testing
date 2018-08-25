/*
 * Copyright 2018 coldrye.eu, Carsten Klein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.coldrye.junit.util;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public final class StackTraceUtils {

  public static Method resolveCaller(Predicate<Method> predicate) {

    for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {

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
