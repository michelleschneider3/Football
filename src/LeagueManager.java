import java.io.*;
import java.util.*;

public class LeagueManager {
    private ArrayList<Match> matches;
    private Set<Team> leagueStanding;
    private int[] teamIds;
    public LeagueManager () {
        this.leagueStanding = new TreeSet<>(Collections.reverseOrder());
        this.matches = new ArrayList<>();
        this.getTeams();
        this.createMatches();
        startRound(0);
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

    private void createMatches() {
        this.teamIds = createArrayFromTeamIds();
        int round = 0;
        int matchId = 1;
        while (round < 9) {
            for (int i = 0, j = this.teamIds.length-1; i < this.teamIds.length; i++, j--) {
                if (j <= i) {
                    round++;
                    this.teamIds = shiftRight();
                    break;
                } else {
                    Match currentMatch = new Match(matchId, this.findTeamById(this.teamIds[i]), this.findTeamById(this.teamIds[j]));
                    this.matches.add(currentMatch);
                    matchId++;
                }
            }
        }
    }

    private int[] createArrayFromTeamIds () {
        int[] result = new int[this.leagueStanding.size()];
        int i = 0;
        for (Team team : this.leagueStanding) {
            result[i] = team.getId();
            i++;
        }
        return result;
    }

    private int[] shiftRight () {
        int[] result = new int[this.teamIds.length];
        result[0] = this.teamIds[this.teamIds.length-1];
        for (int i = 1; i < this.teamIds.length; i++) {
            result[i] = this.teamIds[i-1];
        }
        return result;
    }

    private Team findTeamById (int id) {
        return this.leagueStanding.stream().filter(t -> t.matchesId(id)).findFirst().orElse(null);
    }

    private void startRound (int start) {
        this.matches.stream().skip(start).limit(5).forEach(m -> {
            System.out.println(m);
            countDown(10);
            m.playGame();
        });
    }

    private void countDown (int count) {
        if (count <= 0) {
            return;
        }
        System.out.println(count);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        countDown(count - 1);
    }

}


