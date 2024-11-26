package com.example.steamstats.playercountrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlayerCountRecordService {

    private final PlayerCountRecordRepository playerCountRecordRepository;

    @Autowired
    public PlayerCountRecordService(PlayerCountRecordRepository playerCountRecordRepository) {
        this.playerCountRecordRepository = playerCountRecordRepository;
    }

    public PlayerCountRecord recordPlayerCount(Long gameId, Integer playerCount) {
        PlayerCountRecord record = new PlayerCountRecord(gameId, playerCount, LocalDateTime.now());
        return playerCountRecordRepository.save(record);
    }


    public List<PlayerCountRecord> getPlayerCountHistory(Long gameId) {
        return playerCountRecordRepository.findByGameId(gameId);
    }

}
