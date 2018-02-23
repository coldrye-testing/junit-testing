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

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.util.Preconditions;

/**
 * The final class EnvExtension models a Junit extension for both management and provisioning
 * of environments that have to be set up or torn down before, in between or after that individual
 * tests or all tests have been run.
 *
 * @since 1.0.0
 */
public final class EnvExtension implements TestInstancePostProcessor, ParameterResolver, BeforeAllCallback,
  AfterAllCallback, BeforeEachCallback, AfterEachCallback {

  private final EnvProviderManager providerManager;

  private final FieldInjector fieldInjector;

  private final ParameterResolverImpl parameterResolver;

  /**
   * Default constructor
   */
  //NOSONAR
  public EnvExtension() {

    this(EnvProviderManager.getInstance(), new FieldInjector(), new ParameterResolverImpl());
  }

  /**
   * @param providerManager
   * @param fieldInjector
   * @param parameterResolver
   */
  // For testing only
  EnvExtension(EnvProviderManager providerManager, FieldInjector fieldInjector,
               ParameterResolverImpl parameterResolver) {

    Preconditions.notNull(providerManager, "providerManager must not be null");
    Preconditions.notNull(fieldInjector, "fieldInjector must not be null");
    Preconditions.notNull(parameterResolver, "parameterResolver must not be null");

    this.providerManager = providerManager;
    this.fieldInjector = fieldInjector;
    this.parameterResolver = parameterResolver;
  }

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {

    /*
     * #1 - when using {@code @TestInstance(Lifecycle.PER_CLASS)}, then junit will first
     *      invoke the {@code AfterAllCallback}S.
     *      make sure that the environment providers have been prepared
     */
    if (!providerManager.isPrepared(context)) {
      providerManager.prepareEnvironmentProviders(context);
      providerManager.setUpEnvironments(EnvPhase.INIT, context);
    }
    fieldInjector.inject(testInstance, context, providerManager.getProviders(context, EnvPhase.PREPARE));
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {

    return parameterResolver.supportsParameter(parameterContext, extensionContext,
      providerManager.getProviders(extensionContext, EnvPhase.PREPARE));
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {

    return parameterResolver.resolveParameter(parameterContext, extensionContext,
      providerManager.getProviders(extensionContext, EnvPhase.PREPARE));
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {

    providerManager.setUpEnvironments(EnvPhase.BEFORE_EACH, context);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {

    providerManager.tearDownEnvironments(EnvPhase.AFTER_EACH, context);
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {

    Preconditions.notNull(context.getTestClass().orElse(null),
      "EnvExtension can only be used on ElementType.TYPE");

    if (!providerManager.isPrepared(context)) {
      providerManager.prepareEnvironmentProviders(context);
      providerManager.setUpEnvironments(EnvPhase.INIT, context);
    }
    providerManager.setUpEnvironments(EnvPhase.BEFORE_ALL, context);
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {

    /*
     * #1 - when using {@code @TestInstance(Lifecycle.PER_CLASS)}, then junit will first
     *      invoke the {@code AfterAllCallback}S.
     *      make sure that the environment providers have been prepared
     */
    if (!providerManager.isPrepared(context)) {
      providerManager.prepareEnvironmentProviders(context);
    }
    providerManager.tearDownEnvironments(EnvPhase.AFTER_ALL, context);
  }
}
