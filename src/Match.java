import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Match {
    private int id;
    private Team homeTeam;
    private Team awayTeam;
    private ArrayList<Goal> goals;
    private boolean hasPlayed;
    private boolean used;


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
        int goals = random.nextInt(Constants.MAX_GOALS_PER_GAME);
        AtomicInteger goalId = new AtomicInteger(0);
        AtomicInteger homeGoals = new AtomicInteger(0);
        AtomicInteger awayGoals = new AtomicInteger(0);
        AtomicInteger goalMinute = new AtomicInteger(Constants.FIRST_FOOTBALL_MINUTE);

        this.goals = (ArrayList<Goal>) Stream.generate(() -> {
            boolean isHomeGoal = random.nextInt(Constants.ZERO_OR_ONE) == 0;
            Player scorer;
            if (isHomeGoal) {
                scorer = this.homeTeam.getRandomPlayer();
                homeGoals.getAndIncrement();
            } else {
                scorer = this.awayTeam.getRandomPlayer();
                awayGoals.getAndIncrement();
            }
            goalMinute.set(random.nextInt(goalMinute.get(), Constants.LAST_FOOTBALL_MINUTE+1));
            goalId.getAndIncrement();

            return new Goal(goalId.get(), goalMinute.get(), scorer);
        }).limit(goals).collect(Collectors.toList());

        if (homeGoals.get() > awayGoals.get()) {
            this.homeTeam.addPoints(Constants.WIN_POINTS);
        } else if (awayGoals.get() > homeGoals.get()) {
            this.awayTeam.addPoints(Constants.WIN_POINTS);
        } else {
            this.awayTeam.addPoints(Constants.DRAW_POINTS);
            this.homeTeam.addPoints(Constants.DRAW_POINTS);
        }

        this.organizeGoals(homeGoals.get(), awayGoals.get());
        String outPut = this.homeTeam.getName() + " " + homeGoals.get() + " : " + awayGoals.get() + " " + this.awayTeam.getName();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id == match.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.homeTeam.getName() + " VS " + this.awayTeam.getName() + "\n";
    }

}
