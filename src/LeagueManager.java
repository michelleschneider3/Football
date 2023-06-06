import java.util.*;
import java.util.stream.Collectors;

public class LeagueManager {

    private ArrayList<Match> matches;

    public LeagueManager () {



    }

    public List<Match> findMatchesByTeam (int teamId) {
        List<Match> matchesByTeam = new ArrayList<>();
        if (this.matches != null && !this.matches.isEmpty()) {
            matchesByTeam = this.matches.stream().filter(m -> m.matchesId(teamId)).toList();
        }
        return matchesByTeam;
    }





}


