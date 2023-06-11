import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Match {
    private int id;
    private Team homeTeam;
    private Team awayTeam;
    private ArrayList<Goal> goals;
    private boolean hasPlayed;


    public Match(int id, Team homeTeam, Team awayTeam) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public boolean hasTeam(int teamId) {
        boolean result = false;
        if (this.hasPlayed) {
            result = this.homeTeam.matchesId(teamId) || this.awayTeam.matchesId(teamId);
        }
        return result;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }

    public int getTeamIdOfScorer (Player scorer) {
        int result = -1;
        if (this.homeTeam.playerIsInTeam(scorer)) {
            result = this.homeTeam.getId();
        } else if (this.awayTeam.playerIsInTeam(scorer)) {
            result = this.awayTeam.getId();
        }
        return result;
    }

    public void playGame () {
        Random random = new Random();
        int goals = random.nextInt(11);
        int goalId = 1;
        int[] homeGoals = new int[1];
        int[] awayGoals = new int[1];
        int[] goalMinute = new int[1];
        this.goals = (ArrayList<Goal>) Stream.generate(() -> {
            boolean isHomeGoal = random.nextInt(2) == 0;
            Player scorer;
            if (isHomeGoal) {
                scorer = this.homeTeam.getRandomPlayer();
                homeGoals[0]++;
            } else {
                scorer = this.awayTeam.getRandomPlayer();
                awayGoals[0]++;
            }
            goalMinute[0] = random.nextInt(goalMinute[0], 91);
            return new Goal(goalId, goalMinute[0], scorer);
        }).limit(goals).collect(Collectors.toList());
        if (homeGoals[0]>awayGoals[0]) {
            this.homeTeam.addPoints(3);
        } else if (awayGoals[0]>homeGoals[0]) {
            this.awayTeam.addPoints(3);
        } else {
            this.awayTeam.addPoints(1);
            this.homeTeam.addPoints(1);
        }
        this.organizeGoals(homeGoals[0], awayGoals[0]);
        String outPut = this.homeTeam.getName() + " " + homeGoals[0] + " : " + awayGoals[0] + " " + this.awayTeam.getName();
        System.out.println(outPut);
        this.goals.forEach(System.out::println);
        this.hasPlayed = true;
    }

    private void organizeGoals (int homeGoals, int awayGoals) {
        addGoalValues(this.homeTeam, homeGoals, awayGoals);
        addGoalValues(this.awayTeam, awayGoals, homeGoals);
    }

    private void addGoalValues (Team team, int goalsFor, int goalsAgainst) {
        team.addGoalsFor(goalsFor);
        team.addGoalsAgainst(goalsAgainst);
        team.calculateGoalsDifference();
    }

    @Override
    public String toString() {
        return "Match: " + this.homeTeam.getName() + " VS " + this.awayTeam.getName() + "\n";
    }


}
