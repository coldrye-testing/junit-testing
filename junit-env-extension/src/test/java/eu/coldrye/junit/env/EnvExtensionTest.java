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

import eu.coldrye.junit.JunitTestHelper;
import eu.coldrye.junit.env.Fixtures.FirstTestCase;
import eu.coldrye.junit.env.regression.Issue1Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.mockito.Mockito;

import java.util.Optional;

public class EnvExtensionTest {

  protected EnvExtension sut;

  protected EnvProviderManager mockManager;

  protected FieldInjector mockInjector;

  protected ParameterResolverImpl mockResolver;

  protected ExtensionContext mockContext;

  protected ParameterContext mockParameter;

  @BeforeEach
  public void setUp() {

    mockResolver = Mockito.mock(ParameterResolverImpl.class);
    mockInjector = Mockito.mock(FieldInjector.class);
    mockManager = Mockito.mock(EnvProviderManager.class);
    mockContext = JunitTestHelper.createExtensionContextMock(FirstTestCase.class, Mockito.mock(Store.class));
    mockParameter = Mockito.mock(ParameterContext.class);
    sut = new EnvExtension(mockManager, mockInjector, mockResolver);
  }

  @AfterEach
  public void tearDown() {

    sut = null;
    mockManager = null;
    mockResolver = null;
    mockInjector = null;
    mockContext = null;
    mockParameter = null;
  }

  @Test
  public void postProcessTestInstanceMustCallInjector() throws Exception {

    Object testInstance = new Object();
    sut.postProcessTestInstance(testInstance, mockContext);
    Mockito.verify(mockManager).getProviders(Mockito.eq(mockContext), Mockito.eq(EnvPhase.PREPARE));
    Mockito.verify(mockInjector).inject(Mockito.eq(testInstance), Mockito.eq(mockContext), Mockito.any());
  }

  /**
   * #1 - Illegal state exception when using @TestInstance(TestInstance.Lifecycle.PER_CLASS)
   *
   * @throws Exception
   */
  @Test
  public void postProcessTestInstanceMustCallManagerPrepareAndInitEnvironments() throws Exception {

    Object testInstance = new Issue1Test();
    mockContext = JunitTestHelper.createExtensionContextMock(Issue1Test.class, Mockito.mock(Store.class));
    Mockito.when(mockManager.isPrepared(Mockito.eq(mockContext))).thenReturn(false);
    Mockito.when(mockContext.getTestInstance()).thenReturn(Optional.of(testInstance));
    sut.postProcessTestInstance(testInstance, mockContext);
    Mockito.verify(mockManager).prepareEnvironmentProviders(Mockito.eq(mockContext));
    Mockito.verify(mockManager).setUpEnvironments(Mockito.eq(EnvPhase.INIT), Mockito.eq(mockContext));
  }

  @Test
  public void supportsParameterMustCallResolver() {

    sut.supportsParameter(mockParameter, mockContext);
    Mockito.verify(mockManager).getProviders(Mockito.eq(mockContext), Mockito.eq(EnvPhase.PREPARE));
    Mockito.verify(mockResolver).supportsParameter(Mockito.eq(mockParameter), Mockito.eq(mockContext), Mockito.any());
    Mockito.verifyNoMoreInteractions(mockManager, mockResolver, mockInjector);
  }

  @Test
  public void resolveParameterMustCallResolver() {

    sut.resolveParameter(mockParameter, mockContext);
    Mockito.verify(mockManager).getProviders(Mockito.eq(mockContext), Mockito.eq(EnvPhase.PREPARE));
    Mockito.verify(mockResolver).resolveParameter(Mockito.eq(mockParameter), Mockito.eq(mockContext), Mockito.any());
    Mockito.verifyNoMoreInteractions(mockManager, mockResolver, mockInjector);
  }

  @Test
  public void beforeEachMustCallManager() throws Exception {

    sut.beforeEach(mockContext);
    Mockito.verify(mockManager).setUpEnvironments(Mockito.eq(EnvPhase.BEFORE_EACH), Mockito.eq(mockContext));
    Mockito.verifyNoMoreInteractions(mockManager, mockResolver, mockInjector);
  }

  @Test
  public void afterEachMustCallManager() throws Exception {

    sut.afterEach(mockContext);
    Mockito.verify(mockManager).tearDownEnvironments(Mockito.eq(EnvPhase.AFTER_EACH), Mockito.eq(mockContext));
    Mockito.verifyNoMoreInteractions(mockManager, mockResolver, mockInjector);
  }

  @Test
  public void beforeAllMustNotInitializeEnvironmentsWhenPrepared() throws Exception {

    Mockito.when(mockManager.isPrepared(Mockito.eq(mockContext))).thenReturn(true);
    sut.beforeAll(mockContext);
    Mockito.verify(mockManager).isPrepared(Mockito.eq(mockContext));
    Mockito.verify(mockManager).setUpEnvironments(Mockito.eq(EnvPhase.BEFORE_ALL), Mockito.eq(mockContext));
    Mockito.verifyNoMoreInteractions(mockManager, mockResolver, mockInjector);
  }

  @Test
  public void beforeAllMustInitializeEnvironmentsWhenNotPrepared() throws Exception {

    Mockito.when(mockManager.isPrepared(Mockito.eq(mockContext))).thenReturn(false);
    sut.beforeAll(mockContext);
    Mockito.verify(mockManager).isPrepared(Mockito.eq(mockContext));
    Mockito.verify(mockManager).prepareEnvironmentProviders(Mockito.eq(mockContext));
    Mockito.verify(mockManager).setUpEnvironments(Mockito.eq(EnvPhase.INIT), Mockito.eq(mockContext));
    Mockito.verify(mockManager).setUpEnvironments(Mockito.eq(EnvPhase.BEFORE_ALL), Mockito.eq(mockContext));
    Mockito.verifyNoMoreInteractions(mockManager, mockResolver, mockInjector);
  }

  @Test
  public void afterAllMustCallManagerTearDownEnvironments() throws Exception {

    Mockito.when(mockManager.isPrepared(Mockito.eq(mockContext))).thenReturn(true);
    sut.afterAll(mockContext);
    Mockito.verify(mockManager).tearDownEnvironments(Mockito.eq(EnvPhase.AFTER_ALL), Mockito.eq(mockContext));
  }

  /**
   * #1 - Illegal state exception when using @TestInstance(TestInstance.Lifecycle.PER_CLASS)
   *
   * @throws Exception
   */
  @Test
  public void afterAllMustCallManagerPrepareEnvironments() throws Exception {

    Mockito.when(mockManager.isPrepared(Mockito.eq(mockContext))).thenReturn(false);
    sut.afterAll(mockContext);
    Mockito.verify(mockManager).prepareEnvironmentProviders(Mockito.eq(mockContext));
    Mockito.verify(mockManager).tearDownEnvironments(Mockito.eq(EnvPhase.AFTER_ALL), Mockito.eq(mockContext));
  }

  @Test
  public void defaultConstructorMustNotFail() {

    new EnvExtension();
  }
}
