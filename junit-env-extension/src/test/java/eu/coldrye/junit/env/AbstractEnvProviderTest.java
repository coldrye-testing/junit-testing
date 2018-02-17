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

import eu.coldrye.junit.env.Fixtures.EnvProvider1;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.mockito.Mockito;

@TestInstance(Lifecycle.PER_CLASS)
public class AbstractEnvProviderTest {

  private EnvProvider sut;
  private Store storeMock;

  @BeforeAll
  public void setUp() {

    sut = new EnvProvider1();
    storeMock = Mockito.mock(Store.class);
  }

  @AfterAll
  public void tearDown() {

    sut = null;
    storeMock = null;
  }

  @Test
  public void setStore() {

    sut.setStore(storeMock);
  }

  @Test
  public void getStore() {

    Assertions.assertEquals(storeMock, sut.getStore());
  }
}
