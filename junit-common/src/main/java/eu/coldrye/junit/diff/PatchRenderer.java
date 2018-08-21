// TBD:LICENSE

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
