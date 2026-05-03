package scripty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String firstName;
    private final String lastName;
    private final int birthYear;
    // Dynamic data structure: ArrayList of collaborations
    private final List<Collaboration> collaborations = new ArrayList<>();

    protected Employee(int id, String firstName, String lastName, int birthYear) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }

    // --- abstract contract --------------------------------------------------

    public abstract String getGroupName();

    // --- collaboration management -------------------------------------------

    public void addCollaboration(int colleagueId, CollaborationLevel level) {
        collaborations.removeIf(c -> c.getColleagueId() == colleagueId);
        collaborations.add(new Collaboration(colleagueId, level));
    }

    public void removeCollaboration(int colleagueId) {
        collaborations.removeIf(c -> c.getColleagueId() == colleagueId);
    }


    // --- getters ------------------------------------------------------------

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
