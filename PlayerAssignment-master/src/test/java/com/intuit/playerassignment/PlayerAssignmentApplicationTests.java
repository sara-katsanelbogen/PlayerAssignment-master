package com.intuit.playerassignment;

import com.intuit.playerassignment.controllers.PlayerController;
import com.intuit.playerassignment.models.Player;
import com.intuit.playerassignment.services.PlayerService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

@SpringBootTest
public class PlayerAssignmentApplicationTests {
    PlayerController playerController = new PlayerController(new PlayerService());

    @Test
    public void isMatchByNameTest() {
        Player p = (Player) playerController.getPlayerByPlayerId("aardsda01").getBody();
        Assert.assertEquals(p.getNameFirst(), "David");
    }

    @Test
    public void missingPlayerTest() {
        HttpStatus statusCode = playerController.getPlayerByPlayerId("missingPlayer").getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Test
    public void getAllPlayersTest() {
        ResponseEntity<?> responseEntity  = playerController.getPlayers();
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Collection<Player> players = (Collection<Player>) responseEntity.getBody();
        Assert.assertEquals(players.size(), 19370);
    }
}
