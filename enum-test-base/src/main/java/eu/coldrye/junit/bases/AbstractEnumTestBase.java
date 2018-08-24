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

package eu.coldrye.junit.bases;

import eu.coldrye.junit.util.GenericTypeParameterResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @param <T>
 */
public abstract class AbstractEnumTestBase<T extends Enum<T>> {

  /**
   * This test is included for the purpose of getting full coverage for standard enum behaviour.
   */
  @Test
  public void ensureFullCoverageForStandardEnumBehaviour() {

    Class<T> clazz = getEnumClass();
    T ec = clazz.getEnumConstants()[0];
    Assertions.assertEquals(ec, Enum.valueOf(clazz, ec.name()));
  }

  @SuppressWarnings("unchecked")
  protected final Class<T> getEnumClass() {

    return (Class<T>) GenericTypeParameterResolver.resolve(this, AbstractEnumTestBase.class, 0);
  }
}
