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

package eu.coldrye.junit.env;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * TODO document
 * REFACTOR move to junit-test-helper project
 */
final class JunitTestHelper {

  /**
   * Must not be instantiated.
   */
  //NOSONAR
  private JunitTestHelper() {

  }

  /**
   * @param testClass
   * @param name
   * @param index
   * @return
   */
  public static ParameterContext createParameterContext(Class<?> testClass, String name, int index,
                                                        Class<?>... parameterTypes) throws Exception {

    Parameter resolvedParameter;

    Method method = ReflectionHelper.findMethod(testClass, name, parameterTypes);

    Assertions.assertNotNull(method, "method " + name + " was not found");

    Parameter[] parameters = method.getParameters();
    Assertions.assertTrue(index < parameters.length, "parameter index is out of bounds");

    resolvedParameter = parameters[index];

    return new ParameterContext() {

      @Override
      public Parameter getParameter() {

        return resolvedParameter;
      }

      @Override
      public int getIndex() {

        return index;
      }

      @Override
      public Executable getDeclaringExecutable() {

        return method;
      }

      @Override
      public Optional<Object> getTarget() {

        return Optional.of(testClass);
      }
    };
  }
}
