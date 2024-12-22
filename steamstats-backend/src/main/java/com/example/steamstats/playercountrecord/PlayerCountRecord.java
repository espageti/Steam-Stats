package com.example.steamstats.playercountrecord;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "player_count_record")
public class PlayerCountRecord {

    @Id
    @SequenceGenerator(
            name = "player_count_record_sequence",
            sequenceName = "player_count_record_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "player_count_record_sequence"
    )
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId; // Foreign key to the game

    @Column(name = "player_count", nullable = false)
    private Integer playerCount; // Number of players

    @Column(name = "recorded_at", nullable = false)
    private ZonedDateTime recordedAt; // Timestamp of when the count was recorded

    // Default constructor
    public PlayerCountRecord() {
    }

    // Constructor
    public PlayerCountRecord(Long gameId, Integer playerCount, ZonedDateTime recordedAt) {
        this.gameId = gameId;
        this.playerCount = playerCount;
        this.recordedAt = recordedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(Integer playerCount) {
        this.playerCount = playerCount;
    }

    public ZonedDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(ZonedDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    @Override
    public String toString() {
        return "PlayerCountRecord{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", playerCount=" + playerCount +
                ", recordedAt=" + recordedAt +
                '}';
    }
}

