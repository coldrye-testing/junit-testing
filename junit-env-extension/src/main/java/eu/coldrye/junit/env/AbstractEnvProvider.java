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

import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.platform.commons.util.Preconditions;

/**
 * The abstract class AbstractEnvProvider models the root of a hierarchy of
 * derived classes that realize the {@link EnvProvider} interface.
 * <p>
 * The class is provided for convenience only and you are free to derive
 * from it in order to save you some time implementing the {@link EnvProvider#getStore()}
 * and {@link EnvProvider#setStore(Store)} methods.
 *
 * @see EnvProvider
 * @since 1.0.0
 */
public abstract class AbstractEnvProvider implements EnvProvider {

  /**
   * The {@link Store} where environment specific data is to be stored.
   */
  private Store store;

  @Override
  public final Store getStore() {

    return store;
  }

  @Override
  public final void setStore(Store store) {

    Preconditions.notNull(store, "store must not be null");

    this.store = store;
  }
}
