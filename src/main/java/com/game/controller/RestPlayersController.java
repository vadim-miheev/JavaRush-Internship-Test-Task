package com.game.controller;

import com.game.entity.Player;
import com.game.repository.PlayersRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest")
public class RestPlayersController {

    private final PlayersRepository playersRepository;

    public RestPlayersController(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    @GetMapping("/players")
    public Iterable<Player> getPlayers() {
        return playersRepository.findAll();
    }

    @GetMapping("/players/count")
    public int getPlayersCount() {
        return 1; //TODO - temp value
    }
}
