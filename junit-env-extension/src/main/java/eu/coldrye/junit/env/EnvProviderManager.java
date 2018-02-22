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

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.launcher.TestPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * The final class EnvProviderManager models a manager for instances of the available {@link EnvProvider}S.
 *
 * @since 1.0.0
 */
class EnvProviderManager {

  /*
   * Made accessible for TestExecutionListenerImpl
   */
  static final ThreadLocal<EnvProviderManager> INSTANCE = new ThreadLocal<>();

  private static final Logger log = LoggerFactory.getLogger(EnvProviderManager.class);

  private final EnvProviderCollector collector;

  private Map<Class<?>, List<EnvProvider>> preparedProviders = new HashMap<>();

  /**
   * @param collector
   */
  // For testing only
  EnvProviderManager(EnvProviderCollector collector) {

    Preconditions.notNull(collector, "collector must not be null");
    Preconditions.condition(Objects.isNull(INSTANCE.get()), "must not be instantiated more than once per thread");

    this.collector = collector;
  }

  /**
   * @return
   */
  static EnvProviderManager getInstance() {

    return getInstance(new EnvProviderCollector());
  }

  /**
   * @param collector
   * @return
   */
  static EnvProviderManager getInstance(EnvProviderCollector collector) {

    synchronized (EnvProviderManager.class) {
      if (Objects.isNull(INSTANCE.get())) {
        INSTANCE.set(new EnvProviderManager(collector));
      }
    }

    return INSTANCE.get();
  }

  /**
   * Destroys the instance. Called by {@link TestExecutionListenerImpl#testPlanExecutionFinished(TestPlan)} at the end
   * of the test execution.
   */
  static void destroyInstance() {

    INSTANCE.set(null);
  }

  /**
   * @param context
   * @param phase
   * @return
   */
  List<EnvProvider> getProviders(ExtensionContext context, EnvPhase phase) {

    Preconditions.notNull(context, "context must not be null");
    Preconditions.notNull(phase, "phase must not be null");
    Preconditions.condition(isPrepared(context),
      "Illegal state: environment providers have not yet been prepared during phase " + phase);

    return preparedProviders.get(context.getRequiredTestClass());
  }

  /**
   * Prepare the environment providers inherited from both super classes and implemented interfaces.
   * The order of these environments should never matters, but for fail safe reasons,
   * the order will be from top to bottom.
   *
   * @param context
   */
  void prepareEnvironmentProviders(ExtensionContext context) throws Exception {

    Preconditions.notNull(context, "context must not be null");

    if (isPrepared(context)) {
      return;
    }

    List<EnvProvider> providers = new ArrayList<>();
    Class<?> testClass = context.getRequiredTestClass();
    List<Class<? extends EnvProvider>> providerClasses = collector.collect(testClass);
    for (Class<? extends EnvProvider> providerClass : providerClasses) {
      EnvProvider provider = providerClass.getConstructor().newInstance();
      Namespace ns = Namespace.create(providerClass.getName(), Thread.currentThread().getId());
      Store store = context.getStore(ns);
      provider.setStore(store);
      providers.add(provider);
    }
    preparedProviders.put(testClass, providers);
  }

  /**
   * Shuts down all environments.
   */
  void shutdown() {

    if (preparedProviders.isEmpty()) {
      return;
    }

    for (Entry<Class<?>, List<EnvProvider>> entry : preparedProviders.entrySet()) {
      for (EnvProvider provider : entry.getValue()) {
        try {
          provider.tearDownEnvironment(EnvPhase.DEINIT, entry.getKey());
        } catch (Exception ex) {
          EnvProviderManager.log.error("There was an error during shutdown ", ex);
        }
      }
      entry.setValue(null);
    }
    preparedProviders.clear();
  }

  /**
   * @param phase
   * @param context
   * @throws Exception
   */
  void setUpEnvironments(EnvPhase phase, ExtensionContext context) throws Exception {

    for (EnvProvider provider : getProviders(context, phase)) {
      provider.setUpEnvironment(phase, context.getElement().orElseThrow(new IllegalState()));
    }
  }

  /**
   * @param phase
   * @param context
   * @throws Exception
   */
  void tearDownEnvironments(EnvPhase phase, ExtensionContext context) throws Exception {

    for (EnvProvider provider : getProviders(context, phase)) {
      provider.tearDownEnvironment(phase, context.getElement().orElseThrow(new IllegalState()));
    }
  }

  /**
   * @param context
   * @return
   */
  boolean isPrepared(ExtensionContext context) {

    return !Objects.isNull(preparedProviders.getOrDefault(context.getRequiredTestClass(), null));
  }

  class IllegalState implements Supplier<IllegalStateException> {

    @Override
    public IllegalStateException get() {

      return new IllegalStateException("context did not provide the required annotated element");
    }
  }
}
