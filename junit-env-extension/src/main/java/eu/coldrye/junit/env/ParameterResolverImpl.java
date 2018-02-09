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
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.lang.reflect.Parameter;
import java.util.List;

/**
 * TODO document
 */
final class ParameterResolverImpl {
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext,
                                     List<EnvProvider> providers) throws ParameterResolutionException {
        boolean result = false;
        Parameter parameter = parameterContext.getParameter();
        for (EnvProvider provider : providers) {
            if (provider.canProvideInstance(parameter)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext,
                                   List<EnvProvider> providers) throws ParameterResolutionException {
        Object result = null;
        Parameter parameter = parameterContext.getParameter();
        for (EnvProvider provider : providers) {
            if (provider.canProvideInstance(parameter)) {
                result = provider.getOrCreateInstance(parameter, parameter.getType());
                break;
            }
        }
        if (result == null) {
            throw new ParameterResolutionException("unable to resolve parameter " + parameter.toString());
        }
        return result;
    }
}
