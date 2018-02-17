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
import org.junit.platform.commons.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The final class EnvProviderManager models a manager for instances of the available {@link EnvProvider}S.
 *
 * @since 1.0.0
 */
class EnvProviderManager {

  /*
   * For use with TestExecutionListenerImpl.
   */
  static final ThreadLocal<EnvProviderManager> INSTANCE = new ThreadLocal<>();

  private static final Logger log = LoggerFactory.getLogger(EnvProviderManager.class);

  private final EnvProviderCollector collector;

  private final List<EnvProvider> providers = new ArrayList<>();

  private boolean providersPrepared = false;

  //NOSONAR
  EnvProviderManager() {

    this(new EnvProviderCollector());
  }

  // For testing only
  EnvProviderManager(EnvProviderCollector collector) {

    Preconditions.notNull(collector, "collector must not be null");

    this.collector = collector;
    EnvProviderManager.INSTANCE.set(this);
  }

  /**
   * @return
   */
  List<EnvProvider> getProviders() {

    return Collections.unmodifiableList(providers);
  }

  /**
   * Prepare the environment providers inherited from both super classes and implemented interfaces.
   * The order of these environments should never matters, but for fail safe reasons,
   * the order will be from top to bottom.
   */
  void prepareEnvironmentProviders(ExtensionContext context) throws Exception {

    Preconditions.notNull(context, "context must not be null");

    if (providersPrepared) {
      throw new IllegalStateException("providers have already been prepared");
    }

    List<Class<? extends EnvProvider>> providerClasses = collector.collect(context.getRequiredTestClass());
    for (Class<? extends EnvProvider> providerClass : providerClasses) {
      EnvProvider provider = providerClass.getConstructor().newInstance();
      ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(providerClass.getName(),
        Thread.currentThread().getId()));
      provider.setStore(store);
      providers.add(provider);
    }
    providersPrepared = true;
  }

  /**
   * Shuts down all environments.
   */
  void shutdown() {

    if (providersPrepared) {
      providersPrepared = false;
      for (EnvProvider provider : providers) {
        try {
          provider.tearDownEnvironment(EnvPhase.DEINIT);
        } catch (Exception ex) {
          EnvProviderManager.log.error("There was an error during shutdown ", ex);
        }
      }
      providers.clear();
    }
  }

  /**
   * @param phase
   * @throws Exception
   */
  void setUpEnvironments(EnvPhase phase) throws Exception {

    Preconditions.notNull(phase, "phase must not be null");

    if (!providersPrepared) {
      throw new IllegalStateException("environment providers have not yet been prepared during phase " + phase);
    }
    for (EnvProvider provider : providers) {
      provider.setUpEnvironment(phase);
    }
  }

  /**
   * @param phase
   * @throws Exception
   */
  void tearDownEnvironments(EnvPhase phase) throws Exception {

    Preconditions.notNull(phase, "phase must not be null");

    if (!providersPrepared) {
      throw new IllegalStateException("providers have not yet been prepared");
    }
    for (EnvProvider provider : providers) {
      provider.tearDownEnvironment(phase);
    }
  }

  /**
   * @return
   */
  boolean isPrepared() {

    return providersPrepared;
  }
}
