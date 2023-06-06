import java.util.ArrayList;
import java.util.Objects;

public class Team {
    private int id;
    private String name;
    private ArrayList<Player> players;
    private int goalsFor;
    private int goalsAgainst;
    private int goalsDifference;
    private int points;

    public Team (String name, int points) {
        this.name = name;
        this.points = points;
    }
    public boolean playerIsInTeam (Player player) {
        return this.players.contains(player);
    }

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return name.equals(team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
