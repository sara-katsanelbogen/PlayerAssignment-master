package com.intuit.playerassignment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intuit.playerassignment.models.Player;

import java.io.IOException;
import java.util.stream.IntStream;

public class PlayerMapper {
    private ObjectMapper objectMapper;
    private String[] fields;

    public PlayerMapper(String[] headers) {
        objectMapper = new ObjectMapper();
        this.fields = headers;
    }

    public Player getPlayerFromMapper(String[] playerData) throws IOException {
        try {
            return objectMapper.readValue(convertValuesByFields(playerData), Player.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to map player data: " + e.getMessage());
        }
    }

    private String convertValuesByFields(String[] playerValues) {
        ObjectNode playerNode = objectMapper.createObjectNode();
        IntStream.range(0, Math.min(fields.length, playerValues.length)).forEach(index -> playerNode.put(fields[index], playerValues[index]));
        return playerNode.toString();
    }
}