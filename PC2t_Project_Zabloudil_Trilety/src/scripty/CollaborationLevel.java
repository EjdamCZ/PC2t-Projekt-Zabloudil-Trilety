package scripty;

public enum CollaborationLevel {
    POOR("špatná"),
    AVERAGE("průměrná"),
    GOOD("dobrá");

    private final String displayName;

    CollaborationLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CollaborationLevel fromChoice(String choice) {
        return switch (choice.trim()) {
            case "1" -> POOR;
            case "2" -> AVERAGE;
            case "3" -> GOOD;
            default -> throw new IllegalArgumentException("Neplatná volba úrovně spolupráce: " + choice);
        };
    }

    public int getRiskWeight() {
        return switch (this) {
            case POOR    -> 10;
            case AVERAGE -> 5;
            case GOOD    -> 2;
        };
    }
}
