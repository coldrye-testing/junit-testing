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
