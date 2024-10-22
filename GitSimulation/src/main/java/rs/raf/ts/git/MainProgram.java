package rs.raf.ts.git;

public class MainProgram {

    public static void main(String[] args) {
        GitSimulator gitSimulator = new GitSimulator();

        // Prikaz statusa na početku
        gitSimulator.gitStatus();

        // Simuliraj promene i commit
        gitSimulator.makeChanges(true, 50);  // Promene sa untracked fajlovima
        gitSimulator.gitStatus();
        gitSimulator.commit("Added initial feature");

        // Prikaz git log-a nakon commita
        gitSimulator.gitLog();

        // Kreiraj novu granu i pređi na nju
        gitSimulator.createBranch("feature/cool-feature");
        gitSimulator.checkout("feature/cool-feature");

        // Simuliraj promene i commit na novoj grani
        gitSimulator.makeChanges(false, 30);
        gitSimulator.commit("Implemented cool feature");

        // Prikaz git log-a na novoj grani
        gitSimulator.gitLog();

        // Vrati se na "main" i prikaži istoriju commit-ova
        gitSimulator.checkout("main");
        gitSimulator.gitLog();
    }

}
