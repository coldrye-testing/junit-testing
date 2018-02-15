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
import eu.coldrye.junit.env.Fixtures.EnvProvider2;
import eu.coldrye.junit.env.Fixtures.FirstTestCase;
import eu.coldrye.junit.env.Fixtures.SecondTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class FieldInjectorTest {

  private FieldInjector sut;

  private List<EnvProvider> providers;

  @BeforeEach
  public void setUp() {

    sut = new FieldInjector();
    providers = Arrays.asList(new EnvProvider[]{
      new EnvProvider1(), new EnvProvider2()
    });
  }

  @AfterEach
  public void tearDown() {

    sut = null;
    providers = null;
  }

  @Test
  public void mustInjectInheritedFields() throws Exception {

    FirstTestCase instance = new FirstTestCase();
    sut.inject(instance, null, providers);
    Assertions.assertNotNull(instance.service);
  }

  @Test
  public void mustHaveInjectedAnnotatedFields() throws Exception {

    SecondTestCase instance = new SecondTestCase();
    sut.inject(instance, null, providers);
    Assertions.assertAll(
      () -> Assertions.assertNotNull(instance.service),
      () -> Assertions.assertNull(instance.service1),
      () -> Assertions.assertNotNull(instance.service2),
      () -> Assertions.assertNotNull(instance.service3),
      () -> Assertions.assertNull(instance.service4),
      () -> Assertions.assertNull(instance.service5),
      () -> Assertions.assertNull(instance.service6)
    );
  }
}
