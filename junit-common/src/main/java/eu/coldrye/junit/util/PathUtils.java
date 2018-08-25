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

package eu.coldrye.junit.util;

import org.junit.platform.commons.util.Preconditions;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PathUtils {

  /**
   * Creates a path from the classes' canonical name.
   *
   * @param clazz
   * @return
   */
  public static Path get(Class<?> clazz) {

    Preconditions.notNull(clazz, "clazz must not be null"); // NOSONAR

    List<String> components = new ArrayList<>();
    components.addAll(Arrays.asList(clazz.getCanonicalName().split("[.]")));
    String first = components.get(0);
    components.remove(0);

    return Paths.get(first, components.toArray(new String[] {}));
  }

  /**
   * Creates a path from the declaring classes' canonical name and the method's name.
   *
   * @param method
   * @return
   */
  public static Path get(Method method) {

    Preconditions.notNull(method, "method must not be null"); // NOSONAR

    return Paths.get(get(method.getDeclaringClass()).toString(), method.getName());
  }

  // must not be instantiated
  private PathUtils() {

  }
}
