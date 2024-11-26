package com.example.steamstats.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameDetailsResponse {

    private boolean success; // Add the success field
    private GameData data;


    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }
    @JsonProperty("data")
    public GameData getData() {
        return data;
    }

    public void setData(GameData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GameData {
        private String name;
        private String[] developers; // Changed to array to capture multiple developers
        private ReleaseDate release_date;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("developers")
        public String[] getDevelopers() {
            return developers;
        }

        public void setDevelopers(String[] developers) {
            this.developers = developers;
        }

        @JsonProperty("release_date")
        public ReleaseDate getReleaseDate() {
            return release_date;
        }

        public void setReleaseDate(ReleaseDate release_date) {
            this.release_date = release_date;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ReleaseDate {
            private String date;

            @JsonProperty("date")
            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
}
