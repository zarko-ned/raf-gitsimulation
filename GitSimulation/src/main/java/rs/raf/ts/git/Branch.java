package rs.raf.ts.git;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Branch {
    private String name;
    private Commit latestCommit;

    public void commit(String message) {
        latestCommit = new Commit(message, latestCommit);
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
