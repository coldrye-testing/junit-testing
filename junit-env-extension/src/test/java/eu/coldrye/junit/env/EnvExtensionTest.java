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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EnvExtensionTest {

  private EnvExtension sut;

  private EnvProviderManager mockManager;

  private FieldInjector mockInjector;

  private ParameterResolverImpl mockResolver;

  @BeforeEach
  public void setUp() {

    mockResolver = Mockito.mock(ParameterResolverImpl.class);
    mockInjector = Mockito.mock(FieldInjector.class);
    mockManager = Mockito.mock(EnvProviderManager.class);
    sut = new EnvExtension(mockManager, mockInjector, mockResolver);
  }

  @AfterEach
  public void tearDown() {

    sut = null;
    mockManager = null;
    mockResolver = null;
    mockInjector = null;
  }

  @Test
  public void toDO() {

//    Assertions.fail("no tests have been implemented yet");
  }
}
