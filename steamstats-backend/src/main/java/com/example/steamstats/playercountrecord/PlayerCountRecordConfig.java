package com.example.steamstats.playercountrecord;


import com.example.steamstats.game.Game;
import com.example.steamstats.game.GameRepository;
import com.example.steamstats.game.GameService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class PlayerCountRecordConfig {


    @Bean
    CommandLineRunner addPlayerCounts(PlayerCountRecordRepository repository, GameRepository gameRepository, GameService gameService) {
        return args -> {
            // Create a RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Retrieve all games from the repository
            List<Game> games = gameRepository.findAll();

            for (Game game : games) {
                Long gameId = game.getAppId(); // Get the Steam ID of the game
                String url = String.format("https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=%d", gameId);

                // Call the Steam API and parse the response
                PlayerCountResponse response = restTemplate.getForObject(url, PlayerCountResponse.class);

                if (response != null && response.getResponse() != null) {
                    int playerCount = response.getResponse().getPlayerCount(); // Get the player count

                    PlayerCountRecord record = new PlayerCountRecord(
                            gameId,  // Use the game ID
                            playerCount,  // Player count from the API
                            LocalDateTime.now() // Recorded now
                    );
                    System.out.println("Adding Player count Record " + record);
                    // Save the record to the database
                    repository.save(record);
                }
            }
            // Update average player counts for all games
            gameService.updateAveragePlayerCounts();
        };
    }
}
