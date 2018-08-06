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

import eu.coldrye.junit.util.ReflectionUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.platform.commons.util.Preconditions;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * Helpers for junit tests.
 */
public final class JunitTestHelper {

  private static final String PRECOND_CLASS_MUST_NOT_BE_NULL = "klass must not be null";

  /**
   * Must not be instantiated.
   */
  //NOSONAR
  private JunitTestHelper() {

  }

  /**
   * @param klass
   * @param store
   * @return
   */
  public static ExtensionContext createExtensionContextMock(Class<?> klass, Store store) {

    Preconditions.notNull(klass, PRECOND_CLASS_MUST_NOT_BE_NULL);
    Preconditions.notNull(store, "store must not be null");

    ExtensionContext result = Mockito.mock(ExtensionContext.class);
    Mockito.when(result.getStore(Mockito.any(Namespace.class))).thenReturn(store);
    Mockito.when(result.getTestClass()).thenReturn(Optional.of(klass));
    Mockito.when(result.getElement()).thenReturn(Optional.of(klass));
    Mockito.when(result.getRequiredTestClass()).thenReturn((Class)klass);
    return result;
  }

  /**
   * @param klass
   * @param name
   * @param index
   * @return
   */
  public static ParameterContext createParameterContextMock(Class<?> klass, String name, int index,
                                                            Class<?>... parameterTypes) throws Exception {

    Preconditions.notNull(klass, PRECOND_CLASS_MUST_NOT_BE_NULL);
    Preconditions.notNull(name, "name must not be null");
    Preconditions.notBlank(name, "name must not be blank");

    Method method = ReflectionUtils.findMethod(klass, name, parameterTypes);
    Preconditions.notNull(method, "method " + name + " was not found");

    Parameter[] parameters = method.getParameters();
    Preconditions.notEmpty(parameters, "parameters must not be empty");
    Preconditions.condition(index >=0 && index < parameters.length, "index out of bounds");

    Parameter parameter = parameters[index];

    ParameterContext result = Mockito.mock(ParameterContext.class);
    Mockito.when(result.getParameter()).thenReturn(parameter);
    Mockito.when(result.getIndex()).thenReturn(index);
    Mockito.when(result.getTarget()).thenReturn(Optional.of(klass));
    Mockito.when(result.getDeclaringExecutable()).thenReturn(method);
    return result;
  }
}
