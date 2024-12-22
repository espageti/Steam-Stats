package com.example.steamstats.playercountrecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface PlayerCountRecordRepository extends JpaRepository<PlayerCountRecord, Long> {

    @Query("SELECT p.playerCount FROM PlayerCountRecord p WHERE p.gameId = :gameId AND p.recordedAt >= :since")
    List<Integer> findPlayerCountsInLast24Hours(Long gameId, ZonedDateTime since);

    List<PlayerCountRecord> findByGameId(Long gameId);
}
