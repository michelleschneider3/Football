import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Team implements Comparable<Team>{
    private int id;
    private String name;
    private ArrayList<Player> players;
    private int goalsFor;
    private int goalsAgainst;
    private int goalsDifference;
    private int points;

    public Team (int id, String name) {
        this.id = id;
        this.name = name;
        this.players = new ArrayList<>();
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
        int result;
        if (this.points-o.points != 0) {
            result = this.points-o.points;
        } else {
            if (o.goalsDifference - this.goalsDifference != 0) {
                result = o.goalsDifference - this.goalsDifference;
            } else {
                result = this.name.compareTo(o.name)*-1;
            }
        }
        return result;
    }

    public void addPlayer (Player player) {
        this.players.add(player);
    }

    public boolean matchesId (int id) {
        return this.id == id;
    }

    public Player getRandomPlayer () {
        Random random = new Random();
        return this.players.get(random.nextInt(this.players.size()));
    }

    public void addPoints (int points) {
        this.points += points;
    }

    public void addGoalsFor (int goalsFor) {
        this.goalsFor += goalsFor;
    }

    public void addGoalsAgainst (int goalsAgainst) {
        this.goalsAgainst += goalsAgainst;
    }

    public void calculateGoalsDifference () {
        this.goalsDifference = this.goalsFor-this.goalsAgainst;
    }
}
