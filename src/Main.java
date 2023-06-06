import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        Set<Team> leagueStanding = new TreeSet<>(new TeamComparator());

        Team team1 = new Team("Team 1", 15);
        Team team2 = new Team("Team 2", 10);
        Team team3 = new Team("Team 3", 12);

        leagueStanding.add(team1);
        leagueStanding.add(team2);
        leagueStanding.add(team3);

        for (Team team : leagueStanding) {
            System.out.println(team.getName() + ": " + team.getPoints());
        }


        leagueStanding.remove(team2);
        team2.setPoints(30);
        leagueStanding.add(team2);

        for (Team team : leagueStanding) {
            System.out.println(team.getName() + ": " + team.getPoints());
        }
    }
}