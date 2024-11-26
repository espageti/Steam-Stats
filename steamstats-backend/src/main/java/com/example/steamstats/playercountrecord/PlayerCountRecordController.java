package com.example.steamstats.playercountrecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/player-count")
public class PlayerCountRecordController {

    private final PlayerCountRecordService playerCountRecordService;

    @Autowired
    public PlayerCountRecordController(PlayerCountRecordService playerCountRecordService) {
        this.playerCountRecordService = playerCountRecordService;
    }

    //i don't think I'll ever need this? like why would I
    @PostMapping
    public PlayerCountRecord recordPlayerCount(@RequestParam Long gameId, @RequestParam Integer playerCount) {
        return playerCountRecordService.recordPlayerCount(gameId, playerCount);
    }



    @GetMapping("/{appId}")
    public List<PlayerCountRecord> getPlayerCountHistory(@PathVariable Long appId) {
        System.out.println("Whatever");
        System.out.println(appId);
        return playerCountRecordService.getPlayerCountHistory(appId);
    }
}
