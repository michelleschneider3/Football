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

    public LeagueManager () {
        this.leagueStanding = new TreeSet<>(Collections.reverseOrder());
        this.matches = new ArrayList<>();
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
        int id = Integer.parseInt(line[0].trim());
        String name = line[1].trim();
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
            countDown(3);
            m.playGame();
        });
        this.leagueStanding = new TreeSet<>(this.leagueStanding).descendingSet();
        System.out.println(this.leagueStanding);
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
            String outPut = "Choose from the options below:\n" +
                    "1. Find Matches By Team\n" +
                    "2. Find Top Scoring Teams\n" +
                    "3. Find Players With At Least N Goals\n" +
                    "4. Get Team By Position\n" +
                    "5. Get Top Scorers\n" +
                    "6. Skip";
            System.out.println(outPut);
            String userInput = scanner.nextLine();
            int option;
            try {
                 option = Integer.parseInt(userInput.trim());
            }catch (Exception e) {
                System.out.println("Invalid input, try again");
                continue;
            }
                if (option>=1 && option<=6) {
                    switch (option) {
                        case 1 -> System.out.println(this.findMatchesByTeam(1));
                        case 2 -> {
                            System.out.println("Enter the limit of teams: ");
                            try {
                                int num = scanner.nextInt();
                                System.out.println(this.findTopScoringTeams(num));
                            }catch (Exception e) {
                                System.out.println("Invalid input, try again");
                            }
                            scanner.nextLine();
                        }
                        case 3 -> System.out.println("3");
                        case 4 -> System.out.println("4");
                        case 5 -> System.out.println("5");
                        case 6 -> endLoop = true;
                    }
                } else {
                    System.out.println("Not valid option, try again");
                }
        } while (!endLoop);
    }

}


