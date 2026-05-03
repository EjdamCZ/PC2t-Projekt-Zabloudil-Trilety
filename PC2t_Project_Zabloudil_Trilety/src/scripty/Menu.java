package scripty;

import java.util.Scanner;

public class Menu {
    private static final String SEPARATOR = "─".repeat(50);

    private final Scanner scanner = new Scanner(System.in, "UTF-8");
    private final EmployeeDatabase db = new EmployeeDatabase();


    public void start() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     Databázový systém zaměstnanců v1.0           ║");
        System.out.println("╚══════════════════════════════════════════════════╝");


        boolean running = true;
        while (running) {
            printMenu();
            running = handleChoice(scanner.nextLine().trim());
        }

        System.out.println("\nProgram byl ukončen.");
    }


    // --- menu ---------------------------------------------------------------

    private void printMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  HLAVNÍ NABÍDKA");
        System.out.println(SEPARATOR);
        System.out.println("  1.  Přidat zaměstnance");
        System.out.println("  2.  Přidat spolupráci");
        System.out.println("  3.  Odebrat zaměstnance");
        System.out.println("  0.  Ukončit");
        System.out.println(SEPARATOR);
        System.out.print("  Volba: ");
    }

    private boolean handleChoice(String choice) {
        System.out.println();
        switch (choice) {
            case "1"  -> addEmployee();
            case "2"  -> addCollaboration();
            case "3"  -> removeEmployee();
            case "0"  -> { return false; }
            default   -> System.out.println("Neplatná volba. Zadejte číslo ze seznamu.");
        }
        return true;
    }

    // --- handlers -----------------------------------------------------------

    // a) Add employee
    private void addEmployee() {
        System.out.println("--- Přidání zaměstnance ---");
        System.out.println("Skupiny:  1 = Datový analytik   2 = Bezpečnostní specialista");
        System.out.print("Skupina: ");
        String group = scanner.nextLine().trim();
        if (!group.equals("1") && !group.equals("2")) {
            System.out.println("Neplatná skupina.");
            return;
        }

        System.out.print("Jméno: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Příjmení: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Rok narození: ");

        try {
            int birthYear = Integer.parseInt(scanner.nextLine().trim());
            Employee emp = db.createAndAdd(group, firstName, lastName, birthYear);
            System.out.println("Zaměstnanec přidán: " + emp);
        } catch (NumberFormatException e) {
            System.out.println("Neplatný rok narození.");
        }
    }

    // b) Add collaboration
    private void addCollaboration() {
        System.out.println("--- Přidání spolupráce ---");
        try {
            System.out.print("ID zaměstnance: ");
            int empId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("ID kolegy: ");
            int colId = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Úroveň:  1 = špatná   2 = průměrná   3 = dobrá");
            System.out.print("Úroveň: ");
            CollaborationLevel level = CollaborationLevel.fromChoice(scanner.nextLine().trim());

            if (db.addCollaboration(empId, colId, level)) {
                System.out.println("Spolupráce byla přidána.");
            } else {
                System.out.println("Nepodařilo se přidat spolupráci. Zkontrolujte ID (nesmí být stejná).");
            }
        } catch (NumberFormatException e) {
            System.out.println("Neplatné ID.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // c) Remove employee
    private void removeEmployee() {
        System.out.println("--- Odebrání zaměstnance ---");
        try {
            System.out.print("ID zaměstnance: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (db.removeEmployee(id)) {
                System.out.println("Zaměstnanec ID=" + id + " byl odebrán včetně všech vazeb.");
            } else {
                System.out.println("Zaměstnanec s ID=" + id + " nebyl nalezen.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Neplatné ID.");
        }
    }
}
