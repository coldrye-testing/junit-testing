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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO document
 */
final class EnvProviderManager {

    private final EnvProviderCollector collector;
    private boolean providersPrepared = false;
    private List<EnvProvider> providers = new ArrayList<>();

    /*
     * For use with TestExecutionListenerImpl.
     */
    static ThreadLocal<EnvProviderManager> INSTANCE = new ThreadLocal<>();

    EnvProviderManager() {
        this(new EnvProviderCollector());
    }

    // For testing only
    EnvProviderManager(EnvProviderCollector collector) {
        this.collector = collector;
        INSTANCE.set(this);
    }

    List<EnvProvider> getProviders() {
        return Collections.unmodifiableList(this.providers);
    }

    /**
     * Prepare the environment providers inherited from both super classes and implemented interfaces.
     * The order of these environments should never matters, but for fail safe reasons,
     * the order will be from top to bottom.
     */
    void prepareEnvironmentProviders(Object testInstance, ExtensionContext context) throws Exception {

        if (!testInstance.getClass().isAnnotationPresent(Environment.class)) {
            throw new IllegalStateException("the required @Environment have not been declared");
        }

        List<Class<? extends EnvProvider>> providerClasses = collector.collect(testInstance.getClass());

        for (Class<? extends EnvProvider> providerClass : providerClasses) {
            EnvProvider provider = providerClass.getConstructor().newInstance();
            ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(providerClass.getName(),
                    Thread.currentThread().getId()));
            provider.setStore(store);
            providers.add(provider);
        }
    }

    void shutdown() {
        for (EnvProvider provider : providers) {
            try {
                provider.tearDownEnvironment(EnvPhase.DEINIT);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    void setUpEnvironments(EnvPhase phase) throws Exception {
        for (EnvProvider provider : providers) {
            provider.setUpEnvironment(phase);
        }
    }

    void tearDownEnvironments(EnvPhase phase) throws Exception {
        for (EnvProvider provider : providers) {
            provider.tearDownEnvironment(phase);
        }
    }

    boolean isPrepared() {
        return providersPrepared;
    }
}
