package scripty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String firstName;
    private final String lastName;
    private final int birthYear;

    private final List<Collaboration> collaborations = new ArrayList<>();

    protected Employee(int id, String firstName, String lastName, int birthYear) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }


    public abstract void executeSkill(EmployeeDatabase db);

    public abstract String getGroupName();


    public void addCollaboration(int colleagueId, CollaborationLevel level) {
        collaborations.removeIf(c -> c.getColleagueId() == colleagueId);
        collaborations.add(new Collaboration(colleagueId, level));
    }

    public void removeCollaboration(int colleagueId) {
        collaborations.removeIf(c -> c.getColleagueId() == colleagueId);
    }


    public String getBasicInfo() {
        return String.format("ID: %d | %s %s | Rok narození: %d | Skupina: %s | Počet spolupráci: %d",
                id, firstName, lastName, birthYear, getGroupName(), collaborations.size());
    }

    public String getCollaborationStats() {
        if (collaborations.isEmpty()) return "  Žádné spolupráce.";

        Map<CollaborationLevel, Long> counts = collaborations.stream()
                .collect(Collectors.groupingBy(Collaboration::getLevel, Collectors.counting()));

        StringBuilder sb = new StringBuilder("  Statistiky spolupráce:\n");
        for (CollaborationLevel lvl : CollaborationLevel.values()) {
            sb.append("    ").append(lvl.getDisplayName()).append(": ")
              .append(counts.getOrDefault(lvl, 0L)).append("\n");
        }
        return sb.toString().stripTrailing();
    }


    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getBirthYear() { return birthYear; }
    public List<Collaboration> getCollaborations() { return collaborations; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s (%d) – %s", id, firstName, lastName, birthYear, getGroupName());
    }
}
