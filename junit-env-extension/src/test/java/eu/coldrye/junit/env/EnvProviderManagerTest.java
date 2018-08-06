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
import eu.coldrye.junit.env.Fixtures.EnvProvider1;
import eu.coldrye.junit.env.Fixtures.EnvProvider2;
import eu.coldrye.junit.env.Fixtures.SecondTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.platform.commons.util.PreconditionViolationException;
import org.mockito.Mockito;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class EnvProviderManagerTest {

  private TestLogger logger = TestLoggerFactory.getTestLogger(EnvProviderManager.class);

  private EnvProviderManager sut;

  private EnvProviderCollector mockCollector;

  private Store mockStore;

  @BeforeEach
  public void setUp() {

    mockCollector = Mockito.mock(EnvProviderCollector.class);
    mockStore = Mockito.mock(Store.class);
    sut = EnvProviderManager.getInstance(mockCollector);
  }

  @AfterEach
  public void tearDown() {

    sut = null;
    mockCollector = null;
    EnvProviderManager.destroyInstance();
  }

  @Test
  public void prepareEnvironmentsMustCallUponCollector() throws Exception {

    Mockito.when(mockCollector.collect(SecondTestCase.class)).thenReturn(Collections.EMPTY_LIST);
    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    sut.prepareEnvironmentProviders(mockContext);
    Mockito.verify(mockContext, Mockito.atMost(2)).getRequiredTestClass();
    Mockito.verify(mockCollector, Mockito.atMost(1)).collect(Mockito.any(Class.class));
    Mockito.verifyNoMoreInteractions(mockContext, mockCollector);
    Assertions.assertTrue(sut.isPrepared(mockContext));
  }

  @Test
  public void prepareEnvironmentsMustGetStoreFromContextForEachProvider() throws Exception {

    Mockito.when(mockCollector.collect(SecondTestCase.class)).thenReturn(Arrays.asList(
      new Class[]{EnvProvider1.class, EnvProvider2.class}));
    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    sut.prepareEnvironmentProviders(mockContext);
    Mockito.verify(mockContext, Mockito.atMost(2)).getRequiredTestClass();
    Mockito.verify(mockContext, Mockito.atMost(2)).getStore(Mockito.any(Namespace.class));
    Mockito.verifyNoMoreInteractions(mockContext);
  }

  @Test
  public void instanceMustNotBeNull() {

    Assertions.assertNotNull(EnvProviderManager.getInstance());
  }

  @Test
  public void instanceMustBeSame() {

    Assertions.assertEquals(sut, EnvProviderManager.getInstance());
  }

  @Test
  public void getProvidersMustReturnTheExpectedProviderInstances() throws Exception {

    Mockito.when(mockCollector.collect(SecondTestCase.class)).thenReturn(
      Arrays.asList(EnvProvider1.class, EnvProvider2.class));
    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    sut.prepareEnvironmentProviders(mockContext);
    List<EnvProvider> providers = sut.getProviders(mockContext, EnvPhase.INIT);
    Assertions.assertAll(
      () -> Assertions.assertEquals(2, providers.size()),
      () -> Assertions.assertTrue(providers.get(0) instanceof EnvProvider1),
      () -> Assertions.assertTrue(providers.get(1) instanceof EnvProvider2)
    );
  }

  @Test
  public void prepareEnvironmentCanBeCalledMultipleTimes() throws Exception {

    Mockito.when(mockCollector.collect(SecondTestCase.class)).thenReturn(
      Arrays.asList(EnvProvider1.class, EnvProvider2.class));
    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    sut.prepareEnvironmentProviders(mockContext);
    List<EnvProvider> providers = sut.getProviders(mockContext, EnvPhase.INIT);
    sut.prepareEnvironmentProviders(mockContext);
    List<EnvProvider> providers2 = sut.getProviders(mockContext, EnvPhase.INIT);
    Assertions.assertSame(providers, providers2);
  }

  @Test
  public void getProvidersMustFailForUnpreparedContext() throws Exception {

    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    Assertions.assertThrows(PreconditionViolationException.class, () -> {
      sut.getProviders(mockContext, EnvPhase.INIT);
    });
  }

  @Test
  public void setUpEnvironmentsMustCallProviderWithExpectedArguments() throws Exception {

    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    EnvProviderManager mockManager = Mockito.mock(EnvProviderManager.class);
    EnvProvider mockProvider = Mockito.mock(EnvProvider.class);
    Mockito.when(mockManager.getProviders(mockContext, EnvPhase.INIT)).thenReturn(Arrays.asList(mockProvider));
    Mockito.doCallRealMethod().when(mockManager).setUpEnvironments(
      Mockito.eq(EnvPhase.INIT), Mockito.eq(mockContext));
    Mockito.when(mockManager.isPrepared(mockContext)).thenReturn(true);
    mockManager.setUpEnvironments(EnvPhase.INIT, mockContext);
    Mockito.verify(mockProvider).setUpEnvironment(Mockito.eq(EnvPhase.INIT), Mockito.eq(SecondTestCase.class));
  }

  @Test
  public void setUpEnvironmentsMustBailOutOnMissingElementFromContext() throws Exception {

    Mockito.when(mockCollector.collect(SecondTestCase.class)).thenReturn(
      Arrays.asList(EnvProvider1.class, EnvProvider2.class));
    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    Mockito.when(mockContext.getElement()).thenReturn(Optional.empty());
    sut.prepareEnvironmentProviders(mockContext);
    Assertions.assertThrows(IllegalStateException.class, () -> {
      sut.setUpEnvironments(EnvPhase.BEFORE_ALL, mockContext);
    });
  }

  @Test
  public void tearDownEnvironmentsMustCallProviderWithExpectedArguments() throws Exception {

    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    Mockito.when(mockContext.getElement()).thenReturn(Optional.of(SecondTestCase.class));
    EnvProviderManager mockManager = Mockito.mock(EnvProviderManager.class);
    EnvProvider mockProvider = Mockito.mock(EnvProvider.class);
    Mockito.when(mockManager.getProviders(mockContext, EnvPhase.BEFORE_ALL)).thenReturn(Arrays.asList(mockProvider));
    Mockito.doCallRealMethod().when(mockManager).tearDownEnvironments(
      Mockito.eq(EnvPhase.BEFORE_ALL), Mockito.eq(mockContext));
    Mockito.when(mockManager.isPrepared(mockContext)).thenReturn(true);
    mockManager.tearDownEnvironments(EnvPhase.BEFORE_ALL, mockContext);
    Mockito.verify(mockProvider).tearDownEnvironment(Mockito.eq(EnvPhase.BEFORE_ALL), Mockito.eq(SecondTestCase.class));
  }

  @Test
  public void tearDownEnvironmentsMustBailOutOnMissingElementFromContext() throws Exception {

    Mockito.when(mockCollector.collect(SecondTestCase.class)).thenReturn(
      Arrays.asList(EnvProvider1.class, EnvProvider2.class));
    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    Mockito.when(mockContext.getElement()).thenReturn(Optional.empty());
    sut.prepareEnvironmentProviders(mockContext);
    Assertions.assertThrows(IllegalStateException.class, () -> {
      sut.tearDownEnvironments(EnvPhase.BEFORE_ALL, mockContext);
    });
  }

  @Test
  public void shutdownMustCallProviderWithExpectedArguments() throws Exception {

    Mockito.when(mockCollector.collect(SecondTestCase.class)).thenReturn(
      Arrays.asList(EnvProvider1.class, EnvProvider2.class));
    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    sut.prepareEnvironmentProviders(mockContext);

    List<EnvProvider> providers = sut.getProviders(mockContext, EnvPhase.INIT);
    EnvProvider provider0 = Mockito.spy(providers.get(0));
    EnvProvider provider1 = Mockito.spy(providers.get(1));
    providers.set(0, provider0);
    providers.set(1, provider1);

    sut.shutdown();

    Mockito.verify(provider0).tearDownEnvironment(Mockito.eq(EnvPhase.DEINIT), Mockito.eq(SecondTestCase.class));
    Mockito.verify(provider1).tearDownEnvironment(Mockito.eq(EnvPhase.DEINIT), Mockito.eq(SecondTestCase.class));
  }

  @Test
  public void shutdownMustLogExceptions() throws Exception {

    Mockito.when(mockCollector.collect(SecondTestCase.class)).thenReturn(
      Arrays.asList(EnvProvider1.class, EnvProvider2.class));
    ExtensionContext mockContext = JunitTestHelper.createExtensionContextMock(SecondTestCase.class, mockStore);
    sut.prepareEnvironmentProviders(mockContext);

    List<EnvProvider> providers = sut.getProviders(mockContext, EnvPhase.INIT);
    EnvProvider provider0 = Mockito.spy(providers.get(0));
    Mockito.doThrow(Exception.class).when(provider0).tearDownEnvironment(
      Mockito.eq(EnvPhase.DEINIT), Mockito.eq(SecondTestCase.class));
    providers.set(0, provider0);

    sut.shutdown();

    Assertions.assertEquals(1, logger.getAllLoggingEvents().size());
  }

  @Test
  @SuppressWarnings("squid:S2925")
  public void multipleThreadsMustNotGetDifferentInstances() throws Exception {

    final AtomicReference<Object> singleton = new AtomicReference<>();

    long seed = System.currentTimeMillis();
    Random random = new Random(seed);

    Runnable runnable = () -> {
      try {
        long sleep = Math.abs(random.nextLong());
        sleep = sleep % 10;
        Thread.sleep(sleep);
        Object instance = EnvProviderManager.getInstance();
        if (Objects.isNull(singleton.get())) {
          singleton.set(instance);
        } else {
          Assertions.assertSame(singleton.get(), instance);
        }
      } catch (InterruptedException ex) {
        Assertions.fail("unexpected interruption", ex);
      }
    };

    List<Thread> threads = new ArrayList<>();
    for (int index = 0; index < 10; index++) {
      threads.add(new Thread(runnable));
    }
    for (Thread thread : threads) {
      thread.run();
    }
    for (Thread thread : threads) {
      thread.join();
    }
  }
}
