package scripty;

import java.util.*;

public class EmployeeDatabase {
    private final Map<Integer, Employee> employees = new HashMap<>();
    private int nextId = 1;

    // --- creation -----------------------------------------------------------

    public Employee createAndAdd(String groupChoice, String firstName, String lastName, int birthYear) {
        int id = nextId++;
        Employee emp = switch (groupChoice) {
            case "1" -> new DataAnalyst(id, firstName, lastName, birthYear);
            case "2" -> new SecuritySpecialist(id, firstName, lastName, birthYear);
            default  -> throw new IllegalArgumentException("Neznámá skupina: " + groupChoice);
        };
        employees.put(id, emp);
        return emp;
    }

    // --- collaboration ------------------------------------------------------

    public boolean addCollaboration(int employeeId, int colleagueId, CollaborationLevel level) {
        if (employeeId == colleagueId) return false;
        Employee emp = employees.get(employeeId);
        Employee col = employees.get(colleagueId);
        if (emp == null || col == null) return false;
        emp.addCollaboration(colleagueId, level);
        return true;
    }

    // --- removal ------------------------------------------------------------

    public boolean removeEmployee(int id) {
        if (!employees.containsKey(id)) return false;
        employees.remove(id);
        for (Employee emp : employees.values()) {
            emp.removeCollaboration(id);
        }
        return true;
    }

    // --- meta ---------------------------------------------------------------

    public boolean isEmpty() { return employees.isEmpty(); }
    public int size()        { return employees.size(); }
    public int getNextId()   { return nextId; }
    public void setNextId(int nextId) { this.nextId = nextId; }
}
