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
import eu.coldrye.junit.diff.DiffMatchPatch.Patch;

import java.util.LinkedList;

@SuppressWarnings("squid:S1319")
public class PatchRenderer {

  public String render(LinkedList<Diff> diffs) {

    DiffMatchPatch dmp = new DiffMatchPatch();
    LinkedList<Patch> patches = dmp.patch_make(diffs);

    return dmp.patch_toText(patches);
  }
}
