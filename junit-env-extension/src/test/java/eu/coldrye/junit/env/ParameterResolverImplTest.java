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

import eu.coldrye.junit.env.fixtures.EnvProvider1;
import eu.coldrye.junit.env.fixtures.EnvProvider1ProvidedBoundaryInterface;
import eu.coldrye.junit.env.fixtures.EnvProvider2;
import eu.coldrye.junit.env.fixtures.EnvProvider2ProvidedBoundaryInterface;
import eu.coldrye.junit.env.fixtures.FirstTestCase;
import eu.coldrye.junit.env.fixtures.SecondTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.Arrays;
import java.util.List;

public class ParameterResolverImplTest {

    private ParameterResolverImpl sut;
    private List<EnvProvider> providers;

    @BeforeEach
    public void setUp() {
        sut = new ParameterResolverImpl();
        providers = Arrays.asList(new EnvProvider[]{
            new EnvProvider1(), new EnvProvider2()
        });
    }

    @AfterEach
    public void tearDown() {
        sut = null;
    }

    @Test
    public void mustResolveParameter0ForEnvProvider1ProvidedForFirstTestCase() throws Exception {
        ParameterContext context = JunitHelper.createParameterContext(FirstTestCase.class,
            "testing", 0);
        Object instance = sut.resolveParameter(context, null, providers);
        Assertions.assertTrue(instance instanceof EnvProvider1ProvidedBoundaryInterface);
    }

    @Test
    public void mustResolveParameter0ForEnvProvider2ProvidedForSecondTestCase() throws Exception {
        ParameterContext context = JunitHelper.createParameterContext(SecondTestCase.class,
            "testing2", 0);
        Object instance = sut.resolveParameter(context, null, providers);
        Assertions.assertTrue(instance instanceof EnvProvider2ProvidedBoundaryInterface);
    }

    @Test
    public void mustFailOnUnsupportedTypeParameter0ForSecondTestCase() throws Exception {
        ParameterContext context = JunitHelper.createParameterContext(SecondTestCase.class,
            "testing3", 0);
        Assertions.assertThrows(ParameterResolutionException.class, () -> {
            sut.resolveParameter(context, null, providers);
        });
    }
}
