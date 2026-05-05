package scripty;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDatabase {
    private final Map<Integer, Employee> employees = new HashMap<>();
    private int nextId = 1;


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

    public void addEmployee(Employee emp) {
        employees.put(emp.getId(), emp);
        if (emp.getId() >= nextId) {
            nextId = emp.getId() + 1;
        }
    }


    public boolean addCollaboration(int employeeId, int colleagueId, CollaborationLevel level) {
        if (employeeId == colleagueId) return false;
        Employee emp = employees.get(employeeId);
        Employee col = employees.get(colleagueId);
        if (emp == null || col == null) return false;
        emp.addCollaboration(colleagueId, level);
        return true;
    }


    public boolean removeEmployee(int id) {
        if (!employees.containsKey(id)) return false;
        employees.remove(id);
        for (Employee emp : employees.values()) {
            emp.removeCollaboration(id);
        }
        return true;
    }


    public Employee findById(int id) {
        return employees.get(id);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    public List<Employee> getByGroup(Class<? extends Employee> type) {
        return employees.values().stream()
                .filter(type::isInstance)
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
    }


    public CollaborationLevel getDominantQuality() {
        Map<CollaborationLevel, Long> counts = employees.values().stream()
                .flatMap(e -> e.getCollaborations().stream())
                .collect(Collectors.groupingBy(Collaboration::getLevel, Collectors.counting()));

        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public Employee getMostConnected() {
        return employees.values().stream()
                .max(Comparator.comparingInt(e -> e.getCollaborations().size()))
                .orElse(null);
    }


    public boolean isEmpty() { return employees.isEmpty(); }
    public int size()        { return employees.size(); }
    public int getNextId()   { return nextId; }
    public void setNextId(int nextId) { this.nextId = nextId; }
}
