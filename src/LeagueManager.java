import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LeagueManager {
    private ArrayList<Match> matches;
    private Set<Team> leagueStanding;
    private List<Player> topScorers;

    public LeagueManager () {
        this.leagueStanding = new TreeSet<>(Collections.reverseOrder());
        this.matches = new ArrayList<>();
        this.topScorers = new ArrayList<>();
        this.getTeams();
        this.createMatches();
        Stream.iterate(0, i -> i < Constants.ROUNDS_QUANTITY, i -> i + 1).forEach(i -> {
                this.startRound(Constants.MATCHES_PER_ROUND*i);
                this.showMenu();
        });
    }

    public List<Match> findMatchesByTeam (int teamId) {
        List<Match> matchesByTeam = new ArrayList<>();
        if (this.matches != null && !this.matches.isEmpty()) {
            matchesByTeam = matches.stream().filter(m -> m.hasTeam(teamId)).toList();
        }
        return matchesByTeam;
    }

    List<Team> findTopScoringTeams(int n) {
        List<Team> allTeams = new ArrayList<>(this.leagueStanding);
        return allTeams.stream().sorted(Comparator.comparingInt(Team::getGoalsFor).reversed()).limit(n).toList();
    }

    private void getTeams(){
        File file = new File("src/teams.csv");
        if (file.exists()) {
            try {
                List<Team> teams = Files.lines(file.toPath())
                        .map(line -> line.trim().split(","))
                        .map(this::parseTeam).toList();

                AtomicInteger playerId = new AtomicInteger(1);
                teams.forEach(team -> {
                    playerId.set(addPlayers(playerId.get(), team));
                    this.leagueStanding.add(team);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("There was an unexpected error");
        }
    }

    private Team parseTeam(String[] line) {
        int id = Integer.parseInt(line[Constants.ID_INDEX].trim());
        String name = line[Constants.NAME_INDEX].trim();
        return new Team(id, name);
    }

    private int addPlayers (int currentPlayerId, Team team) {
        Random random = new Random();
        AtomicInteger currentPlayerIdAtom = new AtomicInteger(currentPlayerId);
        IntStream.range(0,Constants.PLAYERS_PER_SQUAD_QUANTITY).forEach(i->{
            String name = Constants.FIRST_NAMES[random.nextInt(Constants.FIRST_NAMES.length)];
            String lastName = Constants.LAST_NAMES[random.nextInt(Constants.FIRST_NAMES.length)];
            team.addPlayer(new Player(currentPlayerIdAtom.get(), name, lastName));
            currentPlayerIdAtom.getAndIncrement();
        });
        return currentPlayerIdAtom.get();
    }

    private void createMatches() {

        List<Team> allTeams = new ArrayList<>(this.leagueStanding);

        AtomicInteger matchId = new AtomicInteger(1);
        AtomicInteger possibleMatches = new AtomicInteger(Constants.ROUNDS_QUANTITY);
        AtomicInteger index = new AtomicInteger(0);

        IntStream.range(0,Constants.ROUNDS_QUANTITY).forEach(round -> {
            AtomicInteger otherIndexes = new AtomicInteger(index.get());
            IntStream.range(0,possibleMatches.get()).forEach(match -> {
                otherIndexes.getAndIncrement();
                matches.add(new Match(matchId.get(),allTeams.get(index.get()),allTeams.get(otherIndexes.get())));
                matchId.getAndIncrement();
            });
            possibleMatches.getAndDecrement();
            index.getAndIncrement();
        });

        Collections.shuffle(this.matches);

    }


    private void startRound (int start) {
        this.matches.stream().skip(start).limit(Constants.MATCHES_PER_ROUND).forEach(m -> {
            System.out.println(m);
            countDown(Constants.COUNT_DOWN);
            m.playGame();
        });
        this.leagueStanding = new TreeSet<>(this.leagueStanding).descendingSet();
        System.out.println(this.leagueStanding);
        generateTopScorers();
    }

    private void generateTopScorers () {
        matches.stream()
                .filter(match -> !match.isUsed() && match.isHasPlayed())
                .flatMap(match -> {
                    match.setUsed(true);
                    return match.getGoals().stream();
                })
                .toList().forEach(goal -> {
                    goal.getScorer().addGoal();
                    if (!this.topScorers.contains(goal.getScorer())) {
                        this.topScorers.add(goal.getScorer());
                    }
                });
    }

    private List<Player> findPlayersWithAtLeastNGoals (int n) {
        List<Player> result = this.topScorers.stream().filter(scorer -> scorer.hasEnoughGoals(n)).toList();
        if (result.isEmpty()) {
            System.out.println("No players found");
        }
        return result;
    }

    private Team getTeamByPosition (int position) {
        if (position<=0) {
            return null;
        }
        return this.leagueStanding.stream().skip(position-1).findFirst().orElse(null);
    }

    public Map<Integer, Integer> getTopScorers(int n) {
        if (n<=0) {
            return null;
        }
        return this.topScorers.stream().sorted(Comparator.comparingInt(Player::getGoalCount).reversed()).limit(n).collect(Collectors.toMap(
                Player::getId, Player::getGoalCount
        ));
    }

    private void countDown (int count) {
        if (count <= 0) {
            return;
        }
        System.out.println(count);
        try {
            Thread.sleep(Constants.ONE_SECOND);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        countDown(count - 1);
    }

    private void showMenu () {
        Scanner scanner = new Scanner(System.in);
        boolean endLoop = false;
        do {
            System.out.println("------------------------------------");
            String outPut = """
                    Choose from the options below:
                    1. Find Matches By Team
                    2. Find Top Scoring Teams
                    3. Find Players With At Least N Goals
                    4. Get Team By Position
                    5. Get Top Scorers
                    6. Skip""";
            System.out.println(outPut);
            System.out.println("------------------------------------");
            String userInput = scanner.nextLine();
            int option;
            try {
                 option = Integer.parseInt(userInput.trim());
            }catch (Exception e) {
                System.out.println("Invalid input, try again");
                continue;
            }
                if (option>=Constants.MINIMUM_OPTION && option<=Constants.MAXIMUM_OPTION) {
                    switch (option) {
                        case (Constants.FIND_MATCHES_BY_TEAM)-> {
                            this.leagueStanding.stream().sorted(Comparator.comparingInt(Team::getId)).forEach(Team::printTeamWithId);
                            System.out.println("Enter the id of the team");
                            String userNum = scanner.nextLine().trim();
                            if (isNumber(userNum)) {
                                int num = Integer.parseInt(userNum);
                                if (num >= Constants.MINIMUM_NUMBER_OF_TEAMS && num <= Constants.MAXIMUM_NUMBER_OF_TEAMS) {
                                    List<Match> playedMatches = this.findMatchesByTeam(num);
                                    if (playedMatches.isEmpty()) {
                                        System.out.println("The team didnt play matches yet");
                                    } else {
                                        playedMatches.forEach(System.out::println);
                                    }
                                } else {
                                    System.out.println("The id doesn't exist");
                                }
                            }
                        }
                        case (Constants.FIND_TOP_SCORING_TEAMS) -> {
                            System.out.println("Enter the limit of teams: ");
                            String userNum = scanner.nextLine().trim();
                            if (isNumber(userNum)) {
                                System.out.println(this.findTopScoringTeams(Integer.parseInt(userNum)));
                            }
                        }
                        case (Constants.FIND_PLAYERS_WITH_AT_LEAST_N_GOALS) -> {
                            System.out.println("Enter the minimum number of goals: ");
                            String userNum = scanner.nextLine().trim();
                            if (isNumber(userNum)) {
                                this.findPlayersWithAtLeastNGoals(Integer.parseInt(userNum)).stream().sorted(Comparator.comparingInt(Player::getGoalCount).reversed()).forEach(Player::printPlayerWithGoalCount);
                            }
                        }
                        case (Constants.GET_TEAM_BY_POSITION) -> {
                            System.out.println("Enter the position: ");
                            String userNum = scanner.nextLine().trim();
                            if (isNumber(userNum)) {
                                Team result = this.getTeamByPosition(Integer.parseInt(userNum));
                                if (result==null) {
                                    System.out.println("Invalid position");
                                } else {
                                    System.out.println(result);
                                }
                            }
                        }
                        case (Constants.GET_TOP_SCORERS) -> {
                            System.out.println("Enter the numbers of players: ");
                            String userNum = scanner.nextLine().trim();
                            if (isNumber(userNum)) {
                                Map<Integer, Integer> result = getTopScorers(Integer.parseInt(userNum));
                                if (result == null) {
                                    System.out.println("Number of Players should be higher then 0");
                                } else {
                                    List<Integer> sortedById = result.entrySet().
                                            stream().
                                            sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                                            map(Map.Entry::getKey).toList();
                                    List<Player> sortedByName = new ArrayList<>();
                                    sortedById.forEach(id -> {
                                        sortedByName.add(this.topScorers.stream().filter(player -> player.matchesId(id)).findFirst().orElse(null));
                                    });
                                    sortedByName.stream().sorted(Comparator.comparingInt(Player::getGoalCount).reversed()).forEach(Player::printPlayerWithGoalCount);
                                }
                            }
                        }
                        case (Constants.SKIP) -> endLoop = true;
                    }
                } else {
                    System.out.println("Not valid option, try again");
                }
        } while (!endLoop);
    }

    private boolean isNumber (String text) {
        boolean result = false;
        try {
            Integer.parseInt(text.trim());
            result = true;
        } catch (Exception e) {
            System.out.println("Invalid input, Try again");
        }
        return result;
    }

}


