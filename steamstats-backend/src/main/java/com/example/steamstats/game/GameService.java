package com.example.steamstats.game;

import com.example.steamstats.playercountrecord.PlayerCountRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public void updateAveragePlayerCounts() {
        LocalDateTime since = LocalDateTime.now().minusHours(24);

        // Fetch all games
        List<Game> games = gameRepository.findAll();

        for (Game game : games) {
            // Fetch player counts for the last 24 hours
            List<Integer> playerCounts = playerCountRecordRepository.findPlayerCountsInLast24Hours(game.getAppId(), since);

            // Calculate average if there are records
            if (!playerCounts.isEmpty()) {
                double average = playerCounts.stream().mapToInt(Integer::intValue).average().orElse(0.0);

                // Update game entity
                game.setAveragePlayerCount(average);
                gameRepository.save(game);
            }
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
            return gameRepository.save(game);
        }).orElseThrow(() -> new RuntimeException("Game not found with id: " + appId));
    }

    public void deleteGame(Long appId) {
        gameRepository.deleteById(appId);
    }
}
