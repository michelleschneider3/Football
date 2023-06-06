import java.util.ArrayList;

public class Match {
    private int id;
    private Team homeTeam;
    private Team awayTeam;
    private ArrayList<Goal> goals;

    public boolean matchesId (int teamId) {
        return this.id == teamId;
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
}
