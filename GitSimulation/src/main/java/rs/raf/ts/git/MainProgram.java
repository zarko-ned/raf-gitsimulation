package rs.raf.ts.git;

public class MainProgram {
    public static void main(String[] args) {
        GitSimulator gitSimulator = new GitSimulator();

        // Različiti scenariji za testiranje validateCommit metode

        // 1. Scenarijo: Main branch, mnogo promena, repo čist
        String status1 = gitSimulator.validateCommit("main", 120, false, true, false, true);
        System.out.println("Status 1: " + status1); // Očekivano: Ready for commit

        // 2. Scenarijo: Feature branch, nema staged promena, repo nije čist
        String status2 = gitSimulator.validateCommit("feature/new-feature", 30, false, false, false, false);
        System.out.println("Status 2: " + status2); // Očekivano: Nothing to commit, working tree clean

        // 3. Scenarijo: Master branch, untracked fajlovi, previše promena
        String status3 = gitSimulator.validateCommit("master", 150, true, false, false, false);
        System.out.println("Status 3: " + status3); // Očekivano: Cannot commit: Untracked files or unstaged changes!

        // 4. Scenarijo: Bugfix branch, merge konflikti, staged promene
        String status4 = gitSimulator.validateCommit("bugfix/fix-issue", 20, false, true, true, false);
        System.out.println("Status 4: " + status4); // Očekivano: Commit partially possible: Review changes!

        // 5. Scenarijo: Main branch, merge konflikti i unstaged promene
        String status5 = gitSimulator.validateCommit("main", 10, false, false, true, false);
        System.out.println("Status 5: " + status5); // Očekivano: Cannot commit: Merge conflicts detected!

        // 6. Scenarijo: Feature branch, staged promene, bez problema
        String status6 = gitSimulator.validateCommit("feature/add-ui", 5, false, true, false, true);
        System.out.println("Status 6: " + status6); // Očekivano: Commit ready for non-main branch
    }
}
