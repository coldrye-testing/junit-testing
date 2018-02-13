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
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/**
 * The final class EnvExtension models a Junit extension for both management and provisioning
 * of environments that have to be set up or torn down before, in between or after that individual
 * tests or all tests have been run.
 *
 * @since 1.0.0
 */
public final class EnvExtension implements TestInstancePostProcessor, ParameterResolver, BeforeAllCallback,
                                               AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private final EnvProviderManager envProviderManager;
    private final FieldInjector fieldInjector;
    private final ParameterResolverImpl parameterResolver;

    public EnvExtension() {
        this(new EnvProviderManager(), new FieldInjector(), new ParameterResolverImpl());
    }

    /**
     * @param envProviderManager
     * @param fieldInjector
     * @param parameterResolver
     */
    // For testing only
    EnvExtension(EnvProviderManager envProviderManager, FieldInjector fieldInjector,
                 ParameterResolverImpl parameterResolver) {
        this.envProviderManager = envProviderManager;
        this.fieldInjector = fieldInjector;
        this.parameterResolver = parameterResolver;
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        fieldInjector.inject(testInstance, context, envProviderManager.getProviders());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return parameterResolver.supportsParameter(parameterContext, extensionContext,
            envProviderManager.getProviders());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return parameterResolver.resolveParameter(parameterContext, extensionContext,
            envProviderManager.getProviders());
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        envProviderManager.setUpEnvironments(EnvPhase.BEFORE_EACH);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        envProviderManager.tearDownEnvironments(EnvPhase.AFTER_EACH);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!envProviderManager.isPrepared()) {
            envProviderManager.prepareEnvironmentProviders(context);
            envProviderManager.setUpEnvironments(EnvPhase.INIT);
        }
        envProviderManager.setUpEnvironments(EnvPhase.BEFORE_ALL);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        envProviderManager.tearDownEnvironments(EnvPhase.AFTER_ALL);
    }
}
