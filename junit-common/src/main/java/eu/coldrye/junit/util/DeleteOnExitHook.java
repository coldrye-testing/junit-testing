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

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DeleteOnExitHook {

  private static final List<Path> paths = new ArrayList<>();
  private static final Thread shutdownThread = new Thread(DeleteOnExitHook::runHook);

  static {

    Runtime.getRuntime().addShutdownHook(shutdownThread);
  }

  public static final void add(Path path) {

    paths.add(path);
  }

  static final void runHook() {

    for (Path path: paths) {

      try {

        FileUtils.deleteRecursively(path);
      } catch (IOException ex) {

        // just ignore
      }
    }
  }

  // must not be instantiated
  private DeleteOnExitHook() {

  }
}
