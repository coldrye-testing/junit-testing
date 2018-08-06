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

import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

/**
 * The final class TestExecutionListenerImpl provides a means to be able to shut down
 * existing environments after that all tests have been run.
 * <p>
 * It gets registered via {@code META-INF/services/org.junit.platform.launcher.TestExecutionListener}.
 * <p>
 * This class is internal and must not be used directly.
 *
 * @since 1.0.0
 */
public final class TestExecutionListenerImpl implements TestExecutionListener {

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {

    Preconditions.notNull(testPlan, "testPlan must not be null");

    EnvProviderManager instance = EnvProviderManager.INSTANCE.get();
    if (instance != null) {

      instance.shutdown();
      EnvProviderManager.destroyInstance();
    }
  }
}
