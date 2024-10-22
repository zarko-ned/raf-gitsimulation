package rs.raf.ts.git;

import lombok.Getter;

/**
 * Represents a Git commit.
 * A commit stores a message, a unique commit hash, and a reference to its parent commit.
 */
@Getter
public class Commit {

    /**
     * The message describing the commit.
     */
    private String message;

    /**
     * A unique hash representing the commit.
     */
    private String commitHash;

    /**
     * The parent commit in the chain, or null if this is the first commit.
     */
    private Commit parent;

    /**
     * Constructs a new commit with the specified message and parent commit.
     * The commit hash is generated based on the message and the parent commit.
     *
     * @param message the commit message
     * @param parent  the parent commit, or null if there is no parent
     */
    public Commit(String message, Commit parent) {
        this.message = message;
        this.commitHash = generateHash(message);
        this.parent = parent;
    }

    /**
     * Generates a unique hash for the commit based on its message and parent.
     *
     * @param message the commit message
     * @return a hash as a hexadecimal string
     */
    private String generateHash(String message) {
        // Generate a simple hash based on the commit message and the parent commit
        return Integer.toHexString(message.hashCode() + (parent != null ? parent.hashCode() : 0));
    }
}
