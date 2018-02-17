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

package eu.coldrye.junit;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.platform.commons.util.Preconditions;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * Helpers for junit tests.
 */
public final class JunitTestHelper {

  /**
   * Must not be instantiated.
   */
  //NOSONAR
  private JunitTestHelper() {

  }

  /**
   * @param klass
   * @param name
   * @param index
   * @return
   */
  public static ParameterContext createParameterContext(Class<?> klass, String name, int index,
                                                        Class<?>... parameterTypes) throws Exception {

    Preconditions.notNull(klass, "klass must not be null");
    Preconditions.notNull(name, "name must not be null");
    Preconditions.notBlank(name, "name must not be blank");

    Method method = ReflectionHelper.findMethod(klass, name, parameterTypes);
    Preconditions.notNull(method, "method " + name + " was not found");

    Parameter[] parameters = method.getParameters();
    Preconditions.notEmpty(parameters, "parameters must not be empty");
    Preconditions.condition(index >=0 && index < parameters.length, "index out of bounds");

    Parameter parameter = parameters[index];

    return new ParameterContext() {

      @Override
      public Parameter getParameter() {

        return parameter;
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

        return Optional.of(klass);
      }
    };
  }
}
