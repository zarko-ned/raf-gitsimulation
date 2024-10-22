package rs.raf.ts.git;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Git branch.
 * A branch in a Git repository points to the latest commit
 * and allows new commits to be added with messages.
 */
@Getter
@AllArgsConstructor
public class Branch {

    /**
     * The name of the branch.
     */
    private String name;

    /**
     * The latest commit on the branch.
     */
    private Commit latestCommit;

    /**
     * Creates a new commit with the given message and updates the branch's latest commit.
     *
     * @param message the commit message
     */
    public void commit(String message) {
        latestCommit = new Commit(message, latestCommit);
    }

    /**
     * Returns the log of all commits in the branch, starting from the latest commit.
     * Each log entry consists of the commit hash and its message.
     *
     * @return a list of commit logs
     */
    public List<String> log() {
        List<String> log = new ArrayList<>();
        Commit current = latestCommit;
        while (current != null) {
            log.add(current.getCommitHash() + " - " + current.getMessage());
            current = current.getParent();
        }
        return log;
    }
}
