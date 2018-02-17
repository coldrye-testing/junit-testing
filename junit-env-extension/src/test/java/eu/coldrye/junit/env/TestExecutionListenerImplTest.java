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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.TestPlan;
import org.mockito.Mockito;

import java.util.Collections;

public class TestExecutionListenerImplTest {

  @Test
  public void testPlanExecutionFinishedMustInstructManagerToShutdown() {

    EnvProviderManager mockManager = Mockito.mock(EnvProviderManager.class);
    EnvProviderManager.INSTANCE.set(mockManager);
    TestPlan testPlan = TestPlan.from(Collections.emptyList());
    new TestExecutionListenerImpl().testPlanExecutionFinished(testPlan);
    Mockito.verify(mockManager).shutdown();
    Assertions.assertNull(EnvProviderManager.INSTANCE.get());
  }

  @Test
  public void testPlanExecutionFinishedMustNotFailOnMissingManagerInstance() {

    TestPlan testPlan = TestPlan.from(Collections.emptyList());
    EnvProviderManager.destroyInstance();
    new TestExecutionListenerImpl().testPlanExecutionFinished(testPlan);
  }
}
