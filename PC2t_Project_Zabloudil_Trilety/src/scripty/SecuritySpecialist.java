package scripty;

public class SecuritySpecialist extends Employee {
    private static final long serialVersionUID = 1L;

    public SecuritySpecialist(int id, String firstName, String lastName, int birthYear) {
        super(id, firstName, lastName, birthYear);
    }

    @Override
    public String getGroupName() {
        return "Bezpečnostní specialista";
    }

}
