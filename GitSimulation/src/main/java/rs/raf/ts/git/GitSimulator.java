package rs.raf.ts.git;

public class GitSimulator {

    // Ovo je neka metoda koja proverava da li su sve promene spremne za commit.
    public String validateCommit(String branchName, int numberOfChanges, boolean hasUntrackedFiles, boolean hasStagedChanges,
                                 boolean hasMergeConflicts, boolean isRepoClean) {

        String commitStatus = "Ready for commit";

        // Složeni uslovi
        boolean isMainBranch = branchName.equals("main") || branchName.equals("master");
        boolean tooManyChanges = numberOfChanges > 100;
        boolean notStagedProperly = !hasStagedChanges && numberOfChanges > 0;
        boolean untrackedIssue = hasUntrackedFiles && isMainBranch;
        boolean conflictDetected = hasMergeConflicts && isMainBranch && !hasStagedChanges;
        boolean minorBranch = branchName.startsWith("feature/") || branchName.startsWith("bugfix/");

        // Ako postoji više složenih problema, commit se ne može izvršiti
        if (isRepoClean && !hasStagedChanges) {
            commitStatus = "Nothing to commit, working tree clean";
        }

        if (conflictDetected || (hasMergeConflicts && minorBranch && tooManyChanges)) {
            commitStatus = "Cannot commit: Merge conflicts detected!";
        }

        if (untrackedIssue || notStagedProperly) {
            commitStatus = "Cannot commit: Untracked files or unstaged changes!";
        }

        if (!isRepoClean && tooManyChanges && hasUntrackedFiles && !hasMergeConflicts) {
            commitStatus = "Commit partially possible: Review changes!";
        }

        // Finalna proverka stanja grane i promene stanja u zavisnosti od ostalih uslova
        if (!isMainBranch && !notStagedProperly && !hasMergeConflicts && hasStagedChanges) {
            commitStatus = "Commit ready for non-main branch";
        }

        return commitStatus;
    }
}
