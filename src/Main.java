import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<Team> leagueStanding = new TreeSet<>(Collections.reverseOrder());

        Team team1 = new Team("Barca", 15);
        Team team2 = new Team("Real", 10);
        Team team3 = new Team("PSG", 12);

        leagueStanding.add(team1);
        leagueStanding.add(team2);
        leagueStanding.add(team3);


        for (Team team : leagueStanding) {
            System.out.println(team.getName() + ": " + team.getPoints());
        }

        System.out.println("-------------------");

        team2.setPoints(30);

        leagueStanding = new TreeSet<>(leagueStanding).descendingSet();

        for (Team team : leagueStanding) {
            System.out.println(team.getName() + ": " + team.getPoints());
        }
        System.out.println("-------------------");
    }
}