// TBD:LICENSE

package eu.coldrye.junit.diff;

import eu.coldrye.junit.diff.DiffMatchPatch.Diff;
import eu.coldrye.junit.diff.DiffMatchPatch.Patch;
import eu.coldrye.junit.util.UnexpectedError;

import java.util.LinkedList;

// TODO:make rendered patch compatible with existing tools
@SuppressWarnings("squid:S1319")
public class PatchRenderer {

  public String render(LinkedList<Diff> diffs) {

    String result;

    DiffMatchPatch dmp = new DiffMatchPatch();
    LinkedList<Patch> patches = dmp.patch_make(diffs);

    Object[] patch = dmp.patch_apply(patches, dmp.diff_text1(diffs));

    result = (String)patch[0];

    boolean[] states = (boolean[]) patch[1];
    for (boolean state: states) {

      if (!state) {

        throw new UnexpectedError("Error while applying patch");
      }
    }

    return result;
  }
}
