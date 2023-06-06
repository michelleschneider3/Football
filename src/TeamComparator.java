import java.util.Comparator;

public class TeamComparator implements Comparator<Team> {

    public int compare(Team team1, Team team2) {
        return Integer.compare(team2.getPoints(), team1.getPoints());
    }

}
