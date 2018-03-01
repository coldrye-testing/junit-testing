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

package eu.coldrye.junit.env.integration;

import eu.coldrye.junit.env.EnvExtension;
import eu.coldrye.junit.env.EnvProvided;
import eu.coldrye.junit.env.Environment;
import eu.coldrye.junit.env.Fixtures.SimpleEnvProvider;
import eu.coldrye.junit.env.integration.Fixtures.SimpleComponent;
import eu.coldrye.junit.env.integration.Fixtures.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ExtendWith(EnvExtension.class)
@Environment(SimpleEnvProvider.class)
@ContextConfiguration(classes = TestConfig.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SpringIntegrationTest {

  @Test
  public void mustNotInterfereWithSpringParameterInjection(@Autowired @EnvProvided SimpleComponent component,
                                                           @EnvProvided String envProvided) {
    Assertions.assertAll(
      () -> Assertions.assertNotNull(component, "component must not be null"),
      () -> Assertions.assertNotNull(envProvided, "envProvided must not be null"),
      () -> Assertions.assertEquals("envProvided", envProvided)
    );
  }
}
