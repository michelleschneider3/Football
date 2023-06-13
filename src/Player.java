import java.util.Objects;

public class Player {
    private int id;
    private String firstName;
    private String lastName;
    private int goalCount;

    public Player(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    public void addGoal () {
        this.goalCount++;
    }

    public boolean hasEnoughGoals(int n) {
        return this.goalCount>=n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getGoalCount() {
        return goalCount;
    }

    public void printPlayerWithGoalCount () {
        System.out.println(this + " : " + this.goalCount);
    }

    public int getId() {
        return id;
    }

    public boolean matchesId (int id) {
        return this.id == id;
    }
}
