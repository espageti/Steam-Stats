package com.example.steamstats.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
public class GameConfig {

    private static final int REQUEST_DELAY_MS = 1500; // 1.5 second delay between requests


    private final int BATCH_SIZE = 1000;
    public void fillAppIds() throws Exception
    {
        String apiUrl = "http://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json";

        // Fetch JSON using RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        // Parse JSON using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, List<Map<String, Object>>>> parsedResponse =
                objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        // Extract apps array from JSON
        List<Map<String, Object>> apps = parsedResponse.get("applist").get("apps");

        // Add all app IDs to the HashSet
        for (Map<String, Object> app : apps) {
            Long appId = ((Number) app.get("appid")).longValue();
            Globals.APP_IDs.add(appId);
        }
    }

    @Bean
    CommandLineRunner addGames(GameRepository repository) {
        return args -> {

            fillAppIds();

            String apiUrl;
            String jsonResponse;
            RestTemplate restTemplate = new RestTemplate();
            List<Game> games = new ArrayList<>();

            int i = 0;
            int num = 0;
            long numGames = Globals.APP_IDs.size();
            for (long appId : Globals.APP_IDs) {
                boolean rateLimit = false;
                i++;
                num++;
                if(i == BATCH_SIZE)
                {
                    repository.saveAll(games);
                    i = 0;
                    games = new ArrayList<Game>();
                }

                System.out.println("Trying to save game " + num + "/" + numGames);
                do {
                    if(rateLimit)
                    {
                        Thread.sleep(REQUEST_DELAY_MS);
                    }
                    rateLimit = false;
                    // Sleep for a short period before making the next request
                    Thread.sleep(REQUEST_DELAY_MS); // Delay of 1 second

                    System.out.println("Trying to add " + appId);
                    // Check if the game already exists in the repository
                    Optional<Game> existingGame = repository.findById(appId);
                    if (existingGame.isPresent()) {
                        System.out.println("Game with ID " + appId + " already exists in the database. Skipping.");
                        continue;
                    }

                    try {
                        apiUrl = "https://store.steampowered.com/api/appdetails?appids=" + appId + "&key=" + System.getenv("STEAM_API_KEY");

                        // Call the Steam API using RestTemplate
                        ObjectMapper mapper = new ObjectMapper();
                        jsonResponse = restTemplate.getForObject(apiUrl, String.class);
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
                            System.out.println("Going to add " + game);
                            games.add(game);
                        }

                    } catch (Exception e) {
                        if(e.getMessage().contains("429"))
                        {
                            rateLimit = true;
                        }
                        System.out.println("Error fetching data for app ID " + appId + ": " + e.getMessage());
                    }
                } while(rateLimit);
            }
            //save anything leftover
            if(i != 0)
            {
                repository.saveAll(games);
            }
        };
    }
}
