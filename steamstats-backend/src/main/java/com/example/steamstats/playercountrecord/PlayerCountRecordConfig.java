package com.example.steamstats.playercountrecord;


import com.example.steamstats.game.Game;
import com.example.steamstats.game.GameRepository;
import com.example.steamstats.game.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Configuration
@Profile("debug") //only load all that stuff if it's in debug mode
public class PlayerCountRecordConfig {
    private static final Logger logger = LoggerFactory.getLogger(PlayerCountRecordConfig.class);
    private static final int REQUEST_DELAY_MS = 1500;

    @Bean
    CommandLineRunner addPlayerCounts(PlayerCountRecordRepository repository, GameRepository gameRepository, GameService gameService) {
        return args -> {
            RestTemplate restTemplate = new RestTemplate();

            // Retrieve all games from the repository
            List<Game> games = gameRepository.findAll();

            int index = 0;
            int numGames = games.size();
            ZonedDateTime startTime = ZonedDateTime.now(ZoneOffset.UTC);
            logger.debug("Starting recording at ", startTime);
            for (Game game : games) {
                index++;
                System.out.println("Trying to add player count record: " + index + "/" + numGames);
                Long gameId = game.getAppId(); // Get the Steam ID of the game
                String url = String.format("https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=%d", gameId);

                boolean rateLimit = false;

                do {
                    if(rateLimit)
                    {
                        Thread.sleep(REQUEST_DELAY_MS);
                    }
                    try {
                        // Call the Steam API and parse the response
                        PlayerCountResponse response = restTemplate.getForObject(url, PlayerCountResponse.class);

                        if (response != null && response.getResponse() != null) {
                            int playerCount = response.getResponse().getPlayerCount(); // Get the player count

                            PlayerCountRecord record = new PlayerCountRecord(
                                    gameId,  // Use the game ID
                                    playerCount,  // Player count from the API
                                    ZonedDateTime.now(ZoneOffset.UTC) // Recorded now, make sure it's UTC
                            );

                            System.out.println("Adding Player count Record: " + record + "at time " + ZonedDateTime.now(ZoneOffset.UTC));
                            // Save the record to the database
                            repository.save(record);
                            gameService.updateAveragePlayerCount(gameId);
                        }
                    } catch (Exception e) {
                        System.out.println("Error updating player count for game ID " + gameId + ": " + e.getMessage());
                    }
                } while (rateLimit);
            }

            ZonedDateTime endTime = ZonedDateTime.now(ZoneOffset.UTC);
            Duration duration = Duration.between(startTime, endTime);
            System.out.println("Took " + duration + " To complete ");
            System.out.println(startTime + " - " + endTime);

        };
    }
}
