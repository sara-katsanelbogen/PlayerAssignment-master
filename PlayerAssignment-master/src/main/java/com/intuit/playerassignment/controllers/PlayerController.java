package com.intuit.playerassignment.controllers;

import com.intuit.playerassignment.annotations.ProjectAuthentication;
import com.intuit.playerassignment.exceptions.PlayerNotFoundException;
import com.intuit.playerassignment.models.Player;
import com.intuit.playerassignment.services.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api")
@ProjectAuthentication
public class PlayerController {
    public static final String NO_PLAYER_FOUND_FOR_PLAYER_ID = "No player found for playerId: ";
    public static final String DATA_RELOADED_SUCCESSFULLY = "Data reloaded successfully.";
    PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/players")
    public ResponseEntity<?> getPlayers() {
        try {
            Collection<Player> players = playerService.getPlayers();
            if (players == null || players.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(players);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve players: " + e.getMessage());
        }
    }

    @GetMapping("/players/{playerID}")
    public ResponseEntity<?> getPlayerByPlayerId(@PathVariable String playerID) {
        try {
            Player player = playerService.getPlayerByPlayerId(playerID);
            if (player == null) {
                throw new PlayerNotFoundException(NO_PLAYER_FOUND_FOR_PLAYER_ID + playerID);
            }
            return ResponseEntity.ok(player);
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NO_PLAYER_FOUND_FOR_PLAYER_ID + playerID);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve player: " + e.getMessage());
        }
    }

//    Additional EP for reloading data from the CSV file.
    @GetMapping("/players/reload")
    public ResponseEntity<String> reloadData() {
        try {
            playerService.reloadData();
            return ResponseEntity.ok(DATA_RELOADED_SUCCESSFULLY);
        } catch (Exception e) {
            String errorMessage = "Failed to reload data: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}
