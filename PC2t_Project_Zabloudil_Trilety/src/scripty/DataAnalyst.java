package scripty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataAnalyst extends Employee {
    private static final long serialVersionUID = 1L;

    public DataAnalyst(int id, String firstName, String lastName, int birthYear) {
        super(id, firstName, lastName, birthYear);
    }

    @Override
    public String getGroupName() {
        return "Datový analytik";
    }

    /**
     * Finds the collaborator with whom this analyst shares the most common collaborators.
     * For each of this employee's collaborators C, counts how many of C's own collaborators
     * also appear in this employee's collaborator set.
     */
    @Override
    public void executeSkill(EmployeeDatabase db) {
        System.out.println("\n=== Dovednost: Hledání nejpodobnější sítě spolupracovníků ===");

        List<Collaboration> myCollabs = getCollaborations();
        if (myCollabs.isEmpty()) {
            System.out.println("Nemám žádné spolupracovníky – dovednost nelze spustit.");
            return;
        }

        Set<Integer> myColleagueIds = myCollabs.stream()
                .map(Collaboration::getColleagueId)
                .collect(Collectors.toSet());

        Map<Integer, Long> commonCounts = new HashMap<>();

        for (int colleagueId : myColleagueIds) {
            Employee colleague = db.findById(colleagueId);
            if (colleague == null) continue;

            long commonCount = colleague.getCollaborations().stream()
                    .map(Collaboration::getColleagueId)
                    .filter(id -> myColleagueIds.contains(id) && id != getId())
                    .count();

            commonCounts.put(colleagueId, commonCount);
        }

        if (commonCounts.isEmpty()) {
            System.out.println("Žádní spolupracovníci nejsou v databázi.");
            return;
        }

        Map.Entry<Integer, Long> best = commonCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        Employee bestColleague = db.findById(best.getKey());
        System.out.printf("Spolupracovník s nejvíce společnými kolegy: %s%n",
                bestColleague != null ? bestColleague.toString() : "ID=" + best.getKey());
        System.out.printf("Počet společných spolupracovníků: %d%n", best.getValue());

        // Show full ranking
        System.out.println("\nÚplné pořadí:");
        commonCounts.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .forEach(e -> {
                    Employee col = db.findById(e.getKey());
                    String name = col != null ? col.getFirstName() + " " + col.getLastName() : "ID=" + e.getKey();
                    System.out.printf("  %s – %d společných%n", name, e.getValue());
                });
    }
}
