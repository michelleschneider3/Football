public class Goal {
    private int id;
    private int minute;
    private Player scorer;

    public Goal(int id, int minute, Player scorer) {
        this.id = id;
        this.minute = minute;
        this.scorer = scorer;
    }

    public Player getScorer() {
        return scorer;
    }

    @Override
    public String toString() {
        return this.scorer + " " + this.minute + "' ";
    }
}
