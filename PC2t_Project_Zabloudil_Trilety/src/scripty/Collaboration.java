package scripty;

import java.io.Serializable;

public class Collaboration implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int colleagueId;
    private CollaborationLevel level;

    public Collaboration(int colleagueId, CollaborationLevel level) {
        this.colleagueId = colleagueId;
        this.level = level;
    }

    public int getColleagueId() { return colleagueId; }
    public CollaborationLevel getLevel() { return level; }
    public void setLevel(CollaborationLevel level) { this.level = level; }

    @Override
    public String toString() {
        return "ID=" + colleagueId + " (" + level.getDisplayName() + ")";
    }
}
