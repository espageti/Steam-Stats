package com.example.steamstats.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class GameUpdateService {

    private static final int REQUEST_DELAY_MS = 1500;
    private final GameRepository repository;

    private final int BATCH_SIZE = 1000;
    public GameUpdateService(GameRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 0 */2 * *") // Run every other day, at 12 am
    public void updateGames() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        List<Game> games = new ArrayList<Game>();

        int i = 0;
        int num = 0;
        long numGames = Globals.APP_IDs.size();
        for (long appId : Globals.APP_IDs) {
            if(i == BATCH_SIZE)
            {
                repository.saveAll(games);
                i = 0;
                games = new ArrayList<Game>();
            }
            num++;
            System.out.println("Trying to save game " + num + "/" + numGames);
            i++;
            boolean rateLimit = false;
            do {
                if (rateLimit) {
                    Thread.sleep(REQUEST_DELAY_MS);
                }
                System.out.println("Trying to add " + appId);
                // Check if the game already exists in the repository
                Optional<Game> existingGame = repository.findById(appId);
                if (existingGame.isPresent()) {
                    System.out.println("Game with ID " + appId + " already exists in the database. Skipping.");
                    continue;
                }

                try {
                    String apiUrl = "https://store.steampowered.com/api/appdetails?appids=" + appId;

                    // Call the Steam API using RestTemplate
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonResponse = restTemplate.getForObject(apiUrl, String.class);
                    Map<String, GameDetailsResponse> responseMap = mapper.readValue(jsonResponse, new TypeReference<Map<String, GameDetailsResponse>>() {
                    });
                    GameDetailsResponse gameResponse = responseMap.get(String.valueOf(appId));

                    if (gameResponse != null && gameResponse.getData() != null) {
                        GameDetailsResponse.GameData appData = gameResponse.getData();
                        String name = appData.getName();
                        String developer = appData.getDevelopers() != null && appData.getDevelopers().length > 0 ? appData.getDevelopers()[0] : "Unknown";
                        String releaseDateStr = appData.getReleaseDate().getDate();

                        LocalDate releaseDate = LocalDate.now();
                        try {
                            releaseDate = LocalDate.parse(releaseDateStr, DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                        } catch (Exception e) {
                            System.out.println("Error parsing release date for " + name + ": " + e.getMessage());
                        }

                        Game game = new Game(appId, name, developer, releaseDate);
                        System.out.println("Going to add" + game);
                        games.add(game);
                    }

                } catch (Exception e) {
                    if(e.getMessage().contains("429"))
                    {
                        rateLimit = true;
                    }
                    System.out.println("Error fetching data for app ID " + appId + ": " + e.getMessage());
                }
            }while(rateLimit);
        }
        if(i != 0)
        {
            repository.saveAll(games);
        }
    }
}
