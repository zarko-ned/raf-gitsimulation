package rs.raf.ts.git;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitSimulator {

    private Map<String, Branch> branches = new HashMap<>();
    private Branch currentBranch;
    private boolean hasUncommittedChanges = false;
    private boolean hasUntrackedFiles = false;
    private boolean hasMergeConflicts = false;
    private int numberOfChanges = 0;

    public GitSimulator() {
        // Kreiramo početnu granu "main" sa početnim commitom
        Commit initialCommit = new Commit("Initial commit", null);
        Branch mainBranch = new Branch("main", initialCommit);
        branches.put("main", mainBranch);
        currentBranch = mainBranch;
    }

    // Metod za promenu trenutne grane
    public void checkout(String branchName) {
        if (branches.containsKey(branchName)) {
            currentBranch = branches.get(branchName);
            System.out.println("Switched to branch '" + branchName + "'");
        } else {
            System.out.println("Branch '" + branchName + "' does not exist.");
        }
    }

    // Metod za kreiranje nove grane
    public void createBranch(String branchName) {
        if (!branches.containsKey(branchName)) {
            Branch newBranch = new Branch(branchName, currentBranch.getLatestCommit());
            branches.put(branchName, newBranch);
            System.out.println("Created new branch '" + branchName + "'");
        } else {
            System.out.println("Branch '" + branchName + "' already exists.");
        }
    }

    // Metod koji simulira commit promena
    public void commit(String commitMessage) {
        String commitStatus = validateCommit(
                currentBranch.getName(),
                numberOfChanges,
                hasUntrackedFiles,
                hasUncommittedChanges,
                hasMergeConflicts,
                !hasUncommittedChanges && !hasUntrackedFiles);

        // Provera statusa commita pre nego što se izvrši
        if (!commitStatus.equals("Ready for commit") && !commitStatus.equals("Commit ready for non-main branch")) {
            System.out.println(commitStatus);
            return;
        }

        currentBranch.commit(commitMessage);
        hasUncommittedChanges = false;
        hasUntrackedFiles = false;
        numberOfChanges = 0;  // Reset changes count after commit
        System.out.println("Changes committed on branch '" + currentBranch.getName() + "' with message: " + commitMessage);
    }

    // Metod za proveru statusa pre commita
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

    // Metod za simulaciju git status
    public void gitStatus() {
        if (hasUncommittedChanges || hasUntrackedFiles) {
            System.out.println("Changes to be committed:");
            if (hasUncommittedChanges) {
                System.out.println("  - Staged changes are ready to be committed.");
            }
            if (hasUntrackedFiles) {
                System.out.println("  - You have untracked files.");
            }
        } else {
            System.out.println("Nothing to commit, working tree clean on branch '" + currentBranch.getName() + "'.");
        }
    }

    // Metod za prikaz git log-a trenutne grane
    public void gitLog() {
        List<String> log = currentBranch.log();
        if (log.isEmpty()) {
            System.out.println("No commits yet on branch '" + currentBranch.getName() + "'.");
            return;
        }

        System.out.println("Commit history for branch '" + currentBranch.getName() + "':");
        for (String entry : log) {
            System.out.println(entry);
        }
    }

    // Metod za simulaciju promena u radnom direktorijumu
    public void makeChanges(boolean hasUntrackedFiles, int changesCount) {
        this.hasUncommittedChanges = changesCount > 0;
        this.hasUntrackedFiles = hasUntrackedFiles;
        this.numberOfChanges = changesCount;
        System.out.println("Changes made to the working directory on branch '" + currentBranch.getName() + "'.");
    }
}
