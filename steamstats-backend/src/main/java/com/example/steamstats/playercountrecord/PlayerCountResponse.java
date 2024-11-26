package com.example.steamstats.playercountrecord;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerCountResponse {

    @JsonProperty("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static class Response {
        @JsonProperty("player_count")
        private int playerCount;

        @JsonProperty("result")
        private int result;

        public int getPlayerCount() {
            return playerCount;
        }

        public void setPlayerCount(int playerCount) {
            this.playerCount = playerCount;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }
}
