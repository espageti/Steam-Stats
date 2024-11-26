package com.example.steamstats.game;

import com.example.steamstats.playercountrecord.PlayerCountRecord;
import com.example.steamstats.playercountrecord.PlayerCountRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/games")
public class GameController {

    private final GameService gameService;
    private final PlayerCountRecordService playerCountRecordService;

    @Autowired
    public GameController(GameService gameService, PlayerCountRecordService playerCountRecordService) {
        this.gameService = gameService;
        this.playerCountRecordService = playerCountRecordService;
    }

    @GetMapping("/popular")
    public List<Game> getPopularGames(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return gameService.getPopularGames(page - 1, limit);
    }


    @GetMapping("/{appId}")
    public Optional<Game> getGameById(@PathVariable("appId") Long appId) {
        return gameService.getGameById(appId);
    }

    @PostMapping
    public Game addGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    @PutMapping("/{appId}")
    public Game updateGame(@PathVariable("appId") Long appId, @RequestBody Game gameDetails) {
        return gameService.updateGame(appId, gameDetails);
    }

    @DeleteMapping("/{appId}")
    public void deleteGame(@PathVariable("appId") Long appId) {
        gameService.deleteGame(appId);
    }

    // New endpoint to get player count records for a specific game by appId
    @GetMapping("/{appId}/records")
    public List<PlayerCountRecord> getPlayerCountRecordsByGameId(@PathVariable("appId") Long appId) {
        return playerCountRecordService.getPlayerCountHistory(appId);
    }
}
