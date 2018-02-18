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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class JunitTestHelperTest {

  @Test
  public void createExtensionContextMockMustReturnProperlyConfiguredMock() {

    Store mockStore = Mockito.mock(Store.class);
    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(Object.class, mockStore);
    Assertions.assertAll(
      () -> mockContext.getElement().orElseThrow(() -> {
        return new AssertionError("element must not be null");
      }),
      () -> Assertions.assertSame(Object.class, mockContext.getElement().get()),
      () -> Assertions.assertSame(Object.class, mockContext.getRequiredTestClass()),
      () -> Assertions.assertSame(Object.class, mockContext.getTestClass().get()),
      () -> Assertions.assertSame(mockStore, mockContext.getStore(Namespace.create(Object.class)))
    );
  }

  @Test
  public void createParameterContextMockMustReturnProperlyConfiguredMock() throws Exception {

    ParameterContext mockParameter = JunitTestHelper.createParameterContextMock(
      Object.class, "equals", 0, Object.class);

    Method method = Object.class.getMethod("equals", Object.class);
    Parameter parameter = method.getParameters()[0];
    Assertions.assertAll(
      () -> Assertions.assertEquals(0, mockParameter.getIndex()),
      () -> Assertions.assertEquals(method, mockParameter.getDeclaringExecutable()),
      () -> Assertions.assertSame(Object.class, mockParameter.getTarget().get()),
      () -> Assertions.assertEquals(parameter, mockParameter.getParameter())
    );
  }
}
