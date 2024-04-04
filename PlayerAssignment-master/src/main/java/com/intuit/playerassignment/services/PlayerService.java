package com.intuit.playerassignment.services;

import com.intuit.playerassignment.Exceptions.PlayerNotFoundException;
import com.intuit.playerassignment.models.Player;
import com.intuit.playerassignment.utils.PlayersLoader;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    PlayersLoader playersLoader;
    public static final String FAILED_LOADING_DATA_FROM_CSV_FILE = "Failed loading data from CSV file.";

    @Autowired
    public PlayerService() {
        playersLoader = PlayersLoader.getInstance();
    }

    public Collection<Player> getPlayers() {
        if (playersLoader.isDataLoadingFailed()) {
            throw new RuntimeException(FAILED_LOADING_DATA_FROM_CSV_FILE);
        }
        return playersLoader.getPlayers().values();
    }

    public Player getPlayerByPlayerId(String playerId) {
        if (playersLoader.isDataLoadingFailed()) {
            throw new RuntimeException(FAILED_LOADING_DATA_FROM_CSV_FILE);
        }
        Player player = playersLoader.getPlayers().get(playerId);
        if (player == null) {
            throw new PlayerNotFoundException("Player not found with ID: " + playerId);
        }
        return player;
    }

    public void reloadData() {
        playersLoader.loadPlayers();
        if (playersLoader.isDataLoadingFailed()) {
            throw new RuntimeException(FAILED_LOADING_DATA_FROM_CSV_FILE);
        }
    }
}