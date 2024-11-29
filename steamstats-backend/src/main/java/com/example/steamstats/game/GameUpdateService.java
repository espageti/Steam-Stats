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

    public static final long[] APP_IDs = {
            440, // Team Fortress 2
            570, // DOTA 2
    };

    private final GameRepository repository;

    public GameUpdateService(GameRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 * * * *") // Run at the top of every hour
    public void updateGames() {
        RestTemplate restTemplate = new RestTemplate();
        List<Game> games = new ArrayList<>();

        for (long appId : APP_IDs) {
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
                Map<String, GameDetailsResponse> responseMap = mapper.readValue(jsonResponse, new TypeReference<Map<String, GameDetailsResponse>>() {});
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
                System.out.println("Error fetching data for app ID " + appId + ": " + e.getMessage());
            }
        }

        repository.saveAll(games);
    }
}