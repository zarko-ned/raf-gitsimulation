package rs.raf.ts.git;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter

public class Commit {
    private String message;
    private String commitHash;
    private Commit parent;

    public Commit(String message, Commit parent) {
        this.message = message;
        this.commitHash = generateHash(message);
        this.parent = parent;
    }

    private String generateHash(String message) {
        // Generišemo jednostavan hash na osnovu poruke commita
        return Integer.toHexString(message.hashCode() + (parent != null ? parent.hashCode() : 0));
    }


}

