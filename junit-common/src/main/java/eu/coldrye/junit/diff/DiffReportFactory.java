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

package eu.coldrye.junit.diff;

import eu.coldrye.junit.diff.DiffMatchPatch.Diff;
import eu.coldrye.junit.util.FileUtils;
import org.junit.platform.commons.util.Preconditions;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.LinkedList;

public class DiffReportFactory {

  public DiffReport createReport(File expected, Object actual, String encoding) throws IOException {

    Preconditions.notNull(expected, "expected must not be null"); // NOSONAR
    Preconditions.notNull(actual, "actual must not be null"); // NOSONAR
    Preconditions.notBlank(encoding, "encoding must not be blank"); // NOSONAR

    if (actual instanceof String) {

      return createReport(expected, (String) actual, encoding);
    } else if (actual instanceof Reader){

      return createReport(expected, (Reader) actual, encoding);
    } else if (actual instanceof File){

      return createReport(expected, (File) actual, encoding);
    } else {

      throw new IllegalArgumentException(
        "actual must be either String, Reader or File, found " + actual.getClass().getCanonicalName());
    }
  }

  public DiffReport createReport(File expected, File actual, String encoding) throws IOException {

    Preconditions.notNull(expected, "expected must not be null"); // NOSONAR
    Preconditions.notNull(actual, "actual must not be null"); // NOSONAR
    Preconditions.notBlank(encoding, "encoding must not be blank"); // NOSONAR

    String expectedContent = FileUtils.readFile(expected, encoding);
    String actualContent = FileUtils.readFile(actual, encoding);

    DiffMatchPatch dmp = new DiffMatchPatch();
    LinkedList<Diff> diffs = dmp.diff_main(expectedContent, actualContent);

    return new DiffReport(expected, diffs, encoding);
  }

  public DiffReport createReport(File expected, String actual, String encoding) throws IOException {

    Preconditions.notNull(actual, "actual must not be null"); // NOSONAR

    return createReport0(expected, actual, encoding);
  }

  public DiffReport createReport(File expected, Reader actual, String encoding) throws IOException {

    Preconditions.notNull(actual, "actual must not be null"); // NOSONAR

    StringWriter writer = new StringWriter();
    int len;
    char[] buf = new char[4096];
    while ((len = actual.read(buf)) > -1) {

      writer.write(buf, 0, len);
    }
    actual.close();

    return createReport0(expected, writer.toString(), encoding);
  }

  private DiffReport createReport0(File expected, String actual, String encoding) throws IOException {

    Preconditions.notNull(expected, "expected must not be null"); // NOSONAR
    Preconditions.notBlank(encoding, "encoding must not be blank"); // NOSONAR

    String expectedContent = FileUtils.readFile(expected, encoding);

    DiffMatchPatch dmp = new DiffMatchPatch();
    LinkedList<Diff> diffs = dmp.diff_main(expectedContent, actual);

    return new DiffReport(expected, diffs, encoding);
  }
}
