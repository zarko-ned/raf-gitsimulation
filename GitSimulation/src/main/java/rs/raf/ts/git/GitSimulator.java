package rs.raf.ts.git;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simulates basic Git operations such as branches, commits, and status checking.
 * This class provides a simplified version of Git functionalities for educational or testing purposes.
 */
public class GitSimulator {

    /**
     * A map of branch names to their corresponding branch objects.
     */
    private Map<String, Branch> branches = new HashMap<>();

    /**
     * The currently active branch.
     */
    private Branch currentBranch;

    /**
     * Flags for tracking the repository state.
     */
    private boolean hasUncommittedChanges = false;
    private boolean hasUntrackedFiles = false;
    private boolean hasMergeConflicts = false;

    /**
     * The number of changes in the working directory.
     */
    private int numberOfChanges = 0;

    /**
     * Constructor that initializes the Git simulator with a default "main" branch and an initial commit.
     */
    public GitSimulator() {
        // Create the initial commit on the "main" branch
        Commit initialCommit = new Commit("Initial commit", null);
        Branch mainBranch = new Branch("main", initialCommit);
        branches.put("main", mainBranch);
        currentBranch = mainBranch;
    }

    /**
     * Switches to the specified branch if it exists.
     *
     * @param branchName the name of the branch to switch to
     */
    public void checkout(String branchName) {
        if (branches.containsKey(branchName)) {
            currentBranch = branches.get(branchName);
            System.out.println("Switched to branch '" + branchName + "'");
        } else {
            System.out.println("Branch '" + branchName + "' does not exist.");
        }
    }

    /**
     * Creates a new branch with the given name if it doesn't already exist.
     *
     * @param branchName the name of the new branch
     */
    public void createBranch(String branchName) {
        if (!branches.containsKey(branchName)) {
            Branch newBranch = new Branch(branchName, currentBranch.getLatestCommit());
            branches.put(branchName, newBranch);
            System.out.println("Created new branch '" + branchName + "'");
        } else {
            System.out.println("Branch '" + branchName + "' already exists.");
        }
    }

    /**
     * Simulates committing the changes on the current branch.
     * Validates the commit status before performing the commit.
     *
     * @param commitMessage the message describing the commit
     */
    public void commit(String commitMessage) {
        String commitStatus = validateCommit(
                currentBranch.getName(),
                numberOfChanges,
                hasUntrackedFiles,
                hasUncommittedChanges,
                hasMergeConflicts,
                !hasUncommittedChanges && !hasUntrackedFiles);

        // Check the commit status before proceeding
        if (!commitStatus.equals("Ready for commit") && !commitStatus.equals("Commit ready for non-main branch")) {
            System.out.println(commitStatus);
            return;
        }

        currentBranch.commit(commitMessage);
        hasUncommittedChanges = false;
        hasUntrackedFiles = false;
        numberOfChanges = 0; // Reset change count after committing
        System.out.println("Changes committed on branch '" + currentBranch.getName() + "' with message: " + commitMessage);
    }

    /**
     * Validates the commit status based on the repository state, current branch, and merge conflicts.
     *
     * @param branchName         the name of the branch
     * @param numberOfChanges    the number of changes in the working directory
     * @param hasUntrackedFiles  true if there are untracked files
     * @param hasStagedChanges   true if there are staged changes
     * @param hasMergeConflicts  true if there are merge conflicts
     * @param isRepoClean        true if the working directory is clean
     * @return a string representing the commit status
     */
    public String validateCommit(String branchName, int numberOfChanges, boolean hasUntrackedFiles, boolean hasStagedChanges,
                                 boolean hasMergeConflicts, boolean isRepoClean) {

        String commitStatus = "Ready for commit";

        // Conditions for commit validation
        boolean isMainBranch = branchName.equals("main") || branchName.equals("master");
        boolean tooManyChanges = numberOfChanges > 100;
        boolean notStagedProperly = !hasStagedChanges && numberOfChanges > 0;
        boolean untrackedIssue = hasUntrackedFiles && isMainBranch;
        boolean conflictDetected = hasMergeConflicts && isMainBranch && !hasStagedChanges;
        boolean minorBranch = branchName.startsWith("feature/") || branchName.startsWith("bugfix/");

        // Determine commit readiness based on repository conditions
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

        if (!isMainBranch && !notStagedProperly && !hasMergeConflicts && hasStagedChanges) {
            commitStatus = "Commit ready for non-main branch";
        }

        return commitStatus;
    }

    /**
     * Simulates the `git status` command by displaying the state of the working directory.
     */
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

    /**
     * Displays the commit log of the current branch.
     */
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

    /**
     * Simulates making changes in the working directory by adjusting the repository state.
     *
     * @param hasUntrackedFiles true if untracked files are present
     * @param changesCount      the number of changes made
     */
    public void makeChanges(boolean hasUntrackedFiles, int changesCount) {
        this.hasUncommittedChanges = changesCount > 0;
        this.hasUntrackedFiles = hasUntrackedFiles;
        this.numberOfChanges = changesCount;
        System.out.println("Changes made to the working directory on branch '" + currentBranch.getName() + "'.");
    }
}
