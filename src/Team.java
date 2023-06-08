import java.util.ArrayList;
import java.util.Objects;

public class Team implements Comparable<Team>{
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
    public int compareTo(Team o) {
        return this.points - o.points;
    }
}
