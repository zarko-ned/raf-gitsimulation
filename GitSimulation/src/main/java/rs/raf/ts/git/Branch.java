package rs.raf.ts.git;

import java.util.ArrayList;
import java.util.List;

public class Branch {
    private String name;
    private Commit latestCommit;

    public Branch(String name, Commit initialCommit) {
        this.name = name;
        this.latestCommit = initialCommit;
    }

    public String getName() {
        return name;
    }

    public void commit(String message) {
        latestCommit = new Commit(message, latestCommit);
    }

    public Commit getLatestCommit() {
        return latestCommit;
    }

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
