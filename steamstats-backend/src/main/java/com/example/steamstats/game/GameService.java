package com.example.steamstats.game;

import com.example.steamstats.playercountrecord.PlayerCountRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerCountRecordRepository playerCountRecordRepository;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerCountRecordRepository playerCountRecordRepository) {
        this.gameRepository = gameRepository;
        this.playerCountRecordRepository = playerCountRecordRepository;
    }

    public List<Game> getPopularGames(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return gameRepository.findAllWithNullsLast(pageable).getContent();
    }

    @Transactional
    public void updateAveragePlayerCount(Long appId) {
        ZonedDateTime since = ZonedDateTime.now(ZoneOffset.UTC).minusHours(24);

        // Fetch the game by appId
        Optional<Game> optionalGame = gameRepository.findById(appId);

        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();

            // Fetch player counts for the last 24 hours for the specific game
            List<Integer> playerCounts = playerCountRecordRepository.findPlayerCountsInLast24Hours(game.getAppId(), since);

            // Calculate average if there are records
            if (!playerCounts.isEmpty()) {
                double average = playerCounts.stream().mapToInt(Integer::intValue).average().orElse(0.0);

                // Update game entity
                game.setAveragePlayerCount(average);
                gameRepository.save(game);
                System.out.println("Updated average player count for game ID: " + appId);
            } else {
                System.out.println("No player count records found for game ID: " + appId);
            }
        } else {
            System.out.println("Game with ID " + appId + " not found.");
        }
    }


    public Optional<Game> getGameById(Long appId) {
        return gameRepository.findById(appId);
    }

    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    public Game updateGame(Long appId, Game gameDetails) {
        return gameRepository.findById(appId).map(game -> {
            game.setTitle(gameDetails.getTitle());
            game.setDeveloper(gameDetails.getDeveloper());
            game.setReleaseDate(gameDetails.getReleaseDate());
            game.setHeaderImage(gameDetails.getHeaderImage());
            return gameRepository.save(game);
        }).orElseThrow(() -> new RuntimeException("Game not found with id: " + appId));
    }

    public void deleteGame(Long appId) {
        gameRepository.deleteById(appId);
    }
}
