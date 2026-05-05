package scripty;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private static final String SEPARATOR = "─".repeat(50);

    private final Scanner scanner = new Scanner(System.in, "UTF-8");
    private final EmployeeDatabase db = new EmployeeDatabase();
    private final FileService fileService = new FileService();

    // --- lifecycle ----------------------------------------------------------

    public void start() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║          Databázový systém zaměstnanců           ║");
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
        System.out.println("  4.  Vyhledat zaměstnance dle ID");
        System.out.println("  5.  Spustit dovednost zaměstnance");
        System.out.println("  6.  Abecední výpis ve skupinách");
        System.out.println("  7.  Statistiky");
        System.out.println("  8.  Počet zaměstnanců ve skupinách");
        System.out.println("  9.  Uložit zaměstnance do souboru");
        System.out.println("  10. Načíst zaměstnance ze souboru");
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
            case "4"  -> findEmployee();
            case "5"  -> executeSkill();
            case "6"  -> listAlphabetically();
            case "7"  -> showStatistics();
            case "8"  -> showGroupCounts();
            case "9"  -> saveEmployeeToFile();
            case "10" -> loadEmployeeFromFile();
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

    // d) Find employee by ID
    private void findEmployee() {
        System.out.println("--- Vyhledání zaměstnance ---");
        try {
            System.out.print("ID zaměstnance: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Employee emp = db.findById(id);
            if (emp == null) {
                System.out.println("Zaměstnanec nenalezen.");
                return;
            }
            System.out.println(emp.getBasicInfo());
            System.out.println(emp.getCollaborationStats());
            if (!emp.getCollaborations().isEmpty()) {
                System.out.println("  Spolupracovníci:");
                for (Collaboration c : emp.getCollaborations()) {
                    Employee col = db.findById(c.getColleagueId());
                    String name = col != null
                            ? col.getFirstName() + " " + col.getLastName()
                            : "– neznámý –";
                    System.out.printf("    • [%d] %s – %s%n", c.getColleagueId(), name, c.getLevel().getDisplayName());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Neplatné ID.");
        }
    }

    // e) Execute skill
    private void executeSkill() {
        System.out.println("--- Spuštění dovednosti ---");
        try {
            System.out.print("ID zaměstnance: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Employee emp = db.findById(id);
            if (emp == null) {
                System.out.println("Zaměstnanec nenalezen.");
                return;
            }
            emp.executeSkill(db);
        } catch (NumberFormatException e) {
            System.out.println("Neplatné ID.");
        }
    }

    // f) Alphabetical listing by group
    private void listAlphabetically() {
        System.out.println("--- Abecední výpis zaměstnanců ---");

        System.out.println("\n  DATOVÍ ANALYTICI");
        List<Employee> analysts = db.getByGroup(DataAnalyst.class);
        if (analysts.isEmpty()) {
            System.out.println("  (žádní)");
        } else {
            analysts.forEach(e -> System.out.println("  " + e));
        }

        System.out.println("\n  BEZPEČNOSTNÍ SPECIALISTÉ");
        List<Employee> specialists = db.getByGroup(SecuritySpecialist.class);
        if (specialists.isEmpty()) {
            System.out.println("  (žádní)");
        } else {
            specialists.forEach(e -> System.out.println("  " + e));
        }
    }

    // g) Statistics
    private void showStatistics() {
        System.out.println("--- Statistiky ---");
        CollaborationLevel dominant = db.getDominantQuality();
        if (dominant != null) {
            System.out.println("Převažující kvalita spolupráce: " + dominant.getDisplayName());
        } else {
            System.out.println("Žádné spolupráce v databázi.");
        }

        Employee mostConnected = db.getMostConnected();
        if (mostConnected != null) {
            System.out.printf("Zaměstnanec s nejvíce vazbami : %s (%d vazeb)%n",
                    mostConnected, mostConnected.getCollaborations().size());
        } else {
            System.out.println("Žádní zaměstnanci v databázi.");
        }
    }

    // h) Group counts
    private void showGroupCounts() {
        System.out.println("--- Počty zaměstnanců ve skupinách ---");
        System.out.println("Datoví analytici          : " + db.getByGroup(DataAnalyst.class).size());
        System.out.println("Bezpečnostní specialisté  : " + db.getByGroup(SecuritySpecialist.class).size());
        System.out.println("Celkem                    : " + db.size());
    }

    // i) Save employee to file
    private void saveEmployeeToFile() {
        System.out.println("--- Uložení zaměstnance do souboru ---");
        try {
            System.out.print("ID zaměstnance: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Employee emp = db.findById(id);
            if (emp == null) {
                System.out.println("Zaměstnanec nenalezen.");
                return;
            }
            System.out.print("Název souboru (bez přípony): ");
            String name = scanner.nextLine().trim();
            String filename = name + ".emp";
            fileService.saveEmployee(emp, filename);
            System.out.println("Zaměstnanec uložen do souboru: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Neplatné ID.");
        } catch (Exception e) {
            System.out.println("Chyba při ukládání: " + e.getMessage());
        }
    }

    // j) Load employee from file
    private void loadEmployeeFromFile() {
        System.out.println("--- Načtení zaměstnance ze souboru ---");
        System.out.print("Název souboru (bez přípony): ");
        String name = scanner.nextLine().trim();
        String filename = name + ".emp";
        try {
            Employee emp = fileService.loadEmployee(filename);
            if (db.findById(emp.getId()) != null) {
                System.out.printf("Zaměstnanec s ID=%d již existuje v databázi. Import přeskočen.%n", emp.getId());
                return;
            }
            db.addEmployee(emp);
            System.out.println("Zaměstnanec načten ze souboru: " + emp);
        } catch (Exception e) {
            System.out.println("Chyba při načítání souboru '" + filename + "': " + e.getMessage());
        }
    }
}
