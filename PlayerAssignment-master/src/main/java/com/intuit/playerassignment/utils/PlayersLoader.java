package com.intuit.playerassignment.utils;

import com.intuit.playerassignment.models.Player;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersLoader {
    // private static final String CSV_FILE_PATH = "./src/main/resources/player.csv";
    private static final String CSV_FILE_PATH = "../PlayerAssignment-master/PlayerAssignment-master/src/main/resources/player.csv";
    private static PlayersLoader instance;
    private PlayerMapper playerMapper;
    private Map<String, Player> playerID2PlayerMap; //use map for getting a specific player in O(1).
    private boolean isDataLoadingFailed;

    // Using a Singleton pattern for a single data loading.
    private PlayersLoader() {
        loadPlayers();
    }

    public static synchronized PlayersLoader getInstance() {
        if (instance == null) {
            instance = new PlayersLoader();
        }
        return instance;
    }

    private String[] getHeaders(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine().split(",");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV headers: " + e.getMessage());
        }
    }

    public void loadPlayers() {
        playerID2PlayerMap = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            playerMapper = new PlayerMapper(getHeaders(CSV_FILE_PATH));
            List<String[]> rows = reader.readAll();
            for (String[] playerData: rows.subList(1, rows.size())) {
                if (!addPlayer(playerData)) {
                    return;
                }
            }
            isDataLoadingFailed = false;
        } catch (IOException e) {
            isDataLoadingFailed = true;
        }
    }

    private boolean addPlayer(String[] playerData) {
        try {
            Player player = playerMapper.getPlayerFromMapper(playerData); //use a mapper for converting a String[] to a Player type;
            playerID2PlayerMap.put(player.getPlayerID(), player);
        } catch (IOException e) {
            isDataLoadingFailed = true;
            return false;
        }
        return true;
    }

    public Map<String, Player> getPlayers() {
        return playerID2PlayerMap;
    }

    public boolean isDataLoadingFailed() { return isDataLoadingFailed; }
}
