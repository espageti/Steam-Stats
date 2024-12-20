package com.example.steamstats.game;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @Column(name = "app_id", nullable = false, unique = true)
    private Long appId; // Steam ID as primary key

    private String title;
    private String developer;
    private LocalDate releaseDate;
    @Column(name = "average_player_count")
    private Double averagePlayerCount; // Field used to determine popularity in the last


    // Default constructor
    public Game() {
    }

    // Constructor
    public Game(Long appId, String title, String developer, LocalDate releaseDate) {
        this.appId = appId;
        this.title = title;
        this.developer = developer;
        this.releaseDate = releaseDate;
    }

    // Getters and Setters
    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getAveragePlayerCount() {
        return averagePlayerCount != null ? averagePlayerCount : -1.0; //default to -1
    }

    public void setAveragePlayerCount(double averagePlayerCount) {
        this.averagePlayerCount = averagePlayerCount;
    }

    @Override
    public String toString() {
        return "Game{" +
                "appId=" + appId +
                ", title='" + title + '\'' +
                ", developer='" + developer + '\'' +
                ", releaseDate=" + releaseDate +
                ", averagePlayerCount=" + averagePlayerCount +
                '}';
    }
}
