import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LeagueManager {
    private ArrayList<Match> matches;
    private Set<Team> leagueStanding;
    public LeagueManager () {
        this.leagueStanding = new TreeSet<>(Collections.reverseOrder());
        this.getTeams();
    }

    public List<Match> findMatchesByTeam (int teamId) {
        List<Match> matchesByTeam = new ArrayList<>();
        if (this.matches != null && !this.matches.isEmpty()) {
            matchesByTeam = this.matches.stream().filter(m -> m.matchesId(teamId)).toList();
        }
        return matchesByTeam;
    }

    private void getTeams () {
        File file = new File("src/teams.csv");
        List<String> lines = new ArrayList<>();

        if (file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                int playerId = 1;
                String currentLine;
                while ((currentLine=bufferedReader.readLine()) != null) {
                    String[] line = currentLine.trim().split(",");
                    int id = Integer.parseInt(line[0].trim());
                    Team currentTeam = new Team(id, line[1].trim());
                    playerId = addPlayers(playerId, currentTeam);
                    this.leagueStanding.add(currentTeam);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("there was an unexpected error");
        }

    }

    private int addPlayers (int currentPlayerId, Team team) {
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            String name = Constants.FIRST_NAMES[random.nextInt(Constants.FIRST_NAMES.length)];
            String lastName = Constants.LAST_NAMES[random.nextInt(Constants.FIRST_NAMES.length)];
            team.addPlayer(new Player(currentPlayerId, name, lastName));
            currentPlayerId++;
        }
        return currentPlayerId;
    }

}


