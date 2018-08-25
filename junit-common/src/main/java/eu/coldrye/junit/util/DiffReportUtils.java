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

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Optional;

public final class DiffReportUtils {

  public static Path determineReportRoot(Path root, String message) {

    return DiffReportUtils.determineReportRoot(root, resolveCaller(), message);
  }

  public static Path determineReportRoot(Path root, Method method, String message) {

    Path targetRoot = root.resolve(PathUtils.get(method).resolve(StringUtils.slug(message)));
    int index = 1;
    do {
      Path path = targetRoot.resolve(String.valueOf(index));

      if (!path.toFile().exists()) {

        return path;
      }

      index++;
    } while (true);
  }

  public static Path determineMostRecentReportRoot(Path root) {

    return determineMostRecentReportRoot(root, null);
  }

  public static Path determineMostRecentReportRoot(Path root, String message) {

    Path basePath = determineReportRoot(root, message);
    Path parent = Optional.ofNullable(basePath.getParent()).orElseThrow(
      () -> new IllegalStateException("parent was null"));
    Path fileName = Optional.ofNullable(basePath.getFileName()).orElseThrow(
      () -> new IllegalStateException("fileName was null"));

    return parent.resolve(Integer.toString(Integer.parseInt(fileName.toString()) - 1));
  }

  public static Method resolveCaller() {

    return StackTraceUtils.resolveCaller(method -> ReflectionUtils.isAnnotatedBy(method, "Test"));
  }

  // must not be instantiated
  private DiffReportUtils() {

  }
}
