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
import eu.coldrye.junit.env.Fixtures.EnvProvider3;
import eu.coldrye.junit.env.Fixtures.EnvProvider4;
import eu.coldrye.junit.env.Fixtures.EnvProvider5;
import eu.coldrye.junit.env.Fixtures.FifthTestCase;
import eu.coldrye.junit.env.Fixtures.FirstTestCase;
import eu.coldrye.junit.env.Fixtures.FourthTestCase;
import eu.coldrye.junit.env.Fixtures.SecondTestCase;
import eu.coldrye.junit.env.Fixtures.ThirdTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class EnvProviderCollectorTest {

  private EnvProviderCollector sut;

  @BeforeEach
  public void setup() {

    sut = new EnvProviderCollector();
  }

  @AfterEach
  public void teardown() {

    sut = null;
  }

  @Test
  public void shouldCollectAllEnvironmentsForSingularEnvironments() throws Exception {

    List<Class<? extends EnvProvider>> collected = sut.collect(FirstTestCase.class);
    List<Class<? extends EnvProvider>> expected = Arrays.asList(new Class[]{
      EnvProvider1.class, EnvProvider2.class, EnvProvider3.class});
    Assertions.assertAll(
      () -> Assertions.assertEquals(3, collected.size()),
      () -> Assertions.assertTrue(collected.containsAll(expected),
        "collected env providers must match the expected env providers")
    );
  }

  @Test
  public void shouldCollectAllEnvironmentsForMixedSingularAndRepeatedEnvironments() throws Exception {

    List<Class<? extends EnvProvider>> collected = sut.collect(SecondTestCase.class);
    List<Class<? extends EnvProvider>> expected = Arrays.asList(new Class[]{
      EnvProvider1.class, EnvProvider2.class, EnvProvider4.class, EnvProvider5.class});
    Assertions.assertAll(
      () -> Assertions.assertEquals(4, collected.size()),
      () -> Assertions.assertTrue(collected.containsAll(expected),
        "collected env providers must match the expected env providers")
    );
  }

  @Test
  public void mustNotFailOnNoProvidedEnvironments() throws Exception {

    List<Class<? extends EnvProvider>> collected = sut.collect(ThirdTestCase.class);
    Assertions.assertEquals(0, collected.size());
  }

  @Test
  public void mustNotCollectAbstractClasses() throws Exception {

    List<Class<? extends EnvProvider>> collected = sut.collect(FourthTestCase.class);
    Assertions.assertEquals(0, collected.size());
  }

  @Test
  public void mustNotCollectInterfaces() throws Exception {

    List<Class<? extends EnvProvider>> collected = sut.collect(FifthTestCase.class);
    Assertions.assertEquals(0, collected.size());
  }
}
