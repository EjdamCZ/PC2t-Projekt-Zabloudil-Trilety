package scripty;

import java.util.List;

public class SecuritySpecialist extends Employee {
    private static final long serialVersionUID = 1L;

    public SecuritySpecialist(int id, String firstName, String lastName, int birthYear) {
        super(id, firstName, lastName, birthYear);
    }

    @Override
    public String getGroupName() {
        return "Bezpečnostní specialista";
    }

    @Override
    public void executeSkill(EmployeeDatabase db) {
        System.out.println("\n=== Dovednost: Výpočet rizikového skóre ===");

        List<Collaboration> collabs = getCollaborations();
        if (collabs.isEmpty()) {
            System.out.println("Žádné spolupráce – rizikové skóre: 0,00 / 100,00");
            return;
        }

        double totalWeight = collabs.stream()
                .mapToInt(c -> c.getLevel().getRiskWeight())
                .sum();

        double avgWeight = totalWeight / collabs.size();
        double networkFactor = Math.log(collabs.size() + 1) / Math.log(2); // log₂(n+1)
        double rawScore = avgWeight * networkFactor;
        double riskScore = Math.min(100.0, rawScore);

        System.out.printf("Počet spolupracovníků : %d%n", collabs.size());
        System.out.printf("Průměrná váha rizika  : %.2f%n", avgWeight);
        System.out.printf("Síťový faktor (log₂)  : %.2f%n", networkFactor);
        System.out.printf("Rizikové skóre        : %.2f / 100,00%n", riskScore);
        System.out.printf("Úroveň rizika         : %s%n", classifyRisk(riskScore));
    }

    private String classifyRisk(double score) {
        if (score >= 70) return "VYSOKÉ";
        if (score >= 40) return "STŘEDNÍ";
        return "NÍZKÉ";
    }
}
