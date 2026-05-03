package scripty;


public class DataAnalyst extends Employee {
    private static final long serialVersionUID = 1L;

    public DataAnalyst(int id, String firstName, String lastName, int birthYear) {
        super(id, firstName, lastName, birthYear);
    }

    @Override
    public String getGroupName() {
        return "Datový analytik";
    }

}
